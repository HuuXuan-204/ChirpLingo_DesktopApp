package com.chirplingo.service.impls;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import com.chirplingo.client.interfaces.RemoteTableClient;
import com.chirplingo.domain.Vocabulary;
import com.chirplingo.domain.User;
import com.chirplingo.domain.LearnHistory;
import com.chirplingo.domain.TodoItem;
import com.chirplingo.domain.Result;
import com.chirplingo.domain.base.BaseEntity;
import com.chirplingo.repository.interfaces.Repository;
import com.chirplingo.repository.interfaces.VocabRepository;
import com.chirplingo.service.interfaces.SyncService;
import com.chirplingo.repository.interfaces.ProfileRepository;
import com.chirplingo.repository.interfaces.SyncMetaDataRepository;
import com.chirplingo.repository.interfaces.LearnHistoryRepository;
import com.chirplingo.repository.interfaces.TodoRepository;
import com.chirplingo.service.interfaces.NetworkService;

public class SyncServiceImpl implements SyncService {
    private final AtomicBoolean isSyncing = new AtomicBoolean(false);

    private VocabRepository vocabRepo;
    private ProfileRepository profileRepo;
    private LearnHistoryRepository learnHistoryRepo;
    private TodoRepository todoRepo;

    private RemoteTableClient<Vocabulary> vocabClient;
    private RemoteTableClient<User> profileClient;
    private RemoteTableClient<LearnHistory> learnHistoryClient;
    private RemoteTableClient<TodoItem> todoClient;

    private NetworkService networkService;
    private SyncMetaDataRepository syncMetaDataRepo;

    public SyncServiceImpl(VocabRepository vocabRepo, ProfileRepository profileRepo, LearnHistoryRepository learnHistoryRepo, TodoRepository todoRepo, RemoteTableClient<Vocabulary> vocabClient, RemoteTableClient<User> profileClient, RemoteTableClient<LearnHistory> learnHistoryClient, RemoteTableClient<TodoItem> todoClient, NetworkService networkService, SyncMetaDataRepository syncMetaDataRepo) {
        this.vocabRepo = vocabRepo;
        this.profileRepo = profileRepo;
        this.learnHistoryRepo = learnHistoryRepo;
        this.todoRepo = todoRepo;
        this.vocabClient = vocabClient;
        this.profileClient = profileClient;
        this.learnHistoryClient = learnHistoryClient;
        this.todoClient = todoClient;
        this.networkService = networkService;
        this.syncMetaDataRepo = syncMetaDataRepo;
    }

    @Override
    public Result syncVocabs() {
        return syncTable("vocabulary", vocabRepo, vocabClient);
    }

    @Override
    public Result syncProfile() {
        return syncTable("profile", profileRepo, profileClient);
    }

    @Override
    public Result syncLearnHistory() {
        return syncTable("learn_history", learnHistoryRepo, learnHistoryClient);
    }

    @Override
    public Result syncTodos() {
        return syncTable("todo_list", todoRepo, todoClient);
    }

    @Override
    public Result syncAll() {
        if (!networkService.isNetworkAvailable()) {
            return Result.fail("Không có kết nối mạng", "NO_INTERNET");
        }

        if (!isSyncing.compareAndSet(false, true)) {
            System.out.println("[SyncAll] Đang sync dở, bỏ qua lệnh gọi mới.");
            return Result.fail("Đang đồng bộ, vui lòng chờ", "SYNCING");
        }

        try {
            System.out.println("[SyncAll] Bắt đầu đồng bộ all");
            CompletableFuture<Result> vocabFuture = CompletableFuture.supplyAsync(this::syncVocabs);
            CompletableFuture<Result> profileFuture = CompletableFuture.supplyAsync(this::syncProfile);
            CompletableFuture<Result> historyFuture = CompletableFuture.supplyAsync(this::syncLearnHistory);
            CompletableFuture<Result> todoFuture = CompletableFuture.supplyAsync(this::syncTodos);

            CompletableFuture.allOf(vocabFuture, profileFuture, historyFuture, todoFuture).join();

            List<String> failures = new ArrayList<>();
            for (Result r : List.of(vocabFuture.get(), profileFuture.get(), historyFuture.get(), todoFuture.get())) {
                if (!r.isSuccess()) failures.add(r.getMessage());
            }

            if (failures.isEmpty()) {
                System.out.println("[SyncAll] Đồng bộ hoàn tất.");
                return Result.success("Đồng bộ tất cả thành công!");
            }
            return Result.fail("Một số bảng đồng bộ thất bại: " + String.join(", ", failures), "PARTIAL_SYNC_FAIL");

        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail("Lỗi khi đồng bộ: " + e.getMessage(), "SYNC_ERROR");
        } finally {
            isSyncing.set(false);
        }
    }

    /**
     * Logic sync chính cho 1 bảng là PULL trước, xử lý conflict, sau đó PUSH
     * @param tableNameLocal Tên bảng trong local DB (dùng để lưu và đọc lastSyncTime của từng bảng)
     * @param repo Repository thao tác local DB
     * @param client Client PULL và PUSH lên server
     */
    private <T extends BaseEntity> Result syncTable(String tableNameLocal, Repository<T> repo, RemoteTableClient<T> client) {
        if (!networkService.isNetworkAvailable()) {
            return Result.fail("Không có kết nối mạng", "NO_INTERNET");
        }

        try {
            //PULL
            OffsetDateTime lastSync = getLastSyncTime(tableNameLocal);
            System.out.println("[Sync][" + tableNameLocal + "] PULL từ " + lastSync);
            List<T> remoteItems = client.pullChanges(lastSync);
            boolean isPullSuccess = (remoteItems != null);
            OffsetDateTime newLastSync = lastSync;

            if (isPullSuccess && !remoteItems.isEmpty()) {
                System.out.println("[Sync][" + tableNameLocal + "] PULL: nhận " + remoteItems.size() + " items từ server.");

                List<String> remoteIds = remoteItems.stream()
                        .map(BaseEntity::getId)
                        .filter(id -> id != null && !id.isBlank())
                        .collect(Collectors.toList());

                List<T> localItems = repo.findByIds(remoteIds);
                Map<String, T> localMap = localItems.stream()
                        .collect(Collectors.toMap(BaseEntity::getId, item -> item));

                List<String> needDelete = new ArrayList<>();
                List<T> needSave = new ArrayList<>();

                for (T serverItem : remoteItems) {
                    if (serverItem.getId() == null || serverItem.getId().isBlank()) {
                        System.err.println("[Sync][" + tableNameLocal + "] PULL: item thiếu id, bỏ qua.");
                        continue;
                    }

                    T localItem = localMap.get(serverItem.getId());
                    boolean shouldApply = resolveConflict(serverItem, localItem);

                    if (!shouldApply) continue;

                    if (serverItem.getUpdatedAt() != null &&
                            (newLastSync == null || serverItem.getUpdatedAt().isAfter(newLastSync))) {
                        newLastSync = serverItem.getUpdatedAt();
                    }

                    if (serverItem.getDeletedAt() != null) {
                        needDelete.add(serverItem.getId());
                    } else {
                        serverItem.markAsSynced();
                        needSave.add(serverItem);
                    }
                }

                if (!needDelete.isEmpty()) {
                    repo.hardDeleteMulti(needDelete);
                    System.out.println("[Sync][" + tableNameLocal + "] PULL: xóa " + needDelete.size() + " items local.");
                }

                if (!needSave.isEmpty()) {
                    boolean saved = repo.saveMulti(needSave);
                    if (saved) {
                        System.out.println("[Sync][" + tableNameLocal + "] PULL: upsert " + needSave.size() + " items local.");
                        if (newLastSync != null && newLastSync.isAfter(lastSync)) {
                            syncMetaDataRepo.saveLastSyncTime(tableNameLocal, newLastSync);
                        }
                    }
                } else if (newLastSync != null && newLastSync.isAfter(lastSync)) {
                    syncMetaDataRepo.saveLastSyncTime(tableNameLocal, newLastSync);
                }
            } else if (!isPullSuccess) {
                System.err.println("[Sync][" + tableNameLocal + "] PULL: thất bại, bỏ qua PUSH để tránh lệch lastSync.");
                return Result.fail("Lỗi khi Pull bảng " + tableNameLocal, "PULL_ERROR");
            } else {
                System.out.println("[Sync][" + tableNameLocal + "] PULL: Không có thay đổi từ server.");
            }

            //PUSH
            List<T> unsyncedItems = repo.getUnsynced();
            if (unsyncedItems != null && !unsyncedItems.isEmpty()) {
                System.out.println("[Sync][" + tableNameLocal + "] PUSH: đẩy " + unsyncedItems.size() + " items lên server.");
                boolean success = client.pushUnsynced(unsyncedItems);
                if (success) {
                    List<String> idsToDelete = unsyncedItems.stream()
                            .filter(item -> item.getDeletedAt() != null)
                            .map(BaseEntity::getId)
                            .collect(Collectors.toList());

                    List<String> idsToMarkSynced = unsyncedItems.stream()
                            .filter(item -> item.getDeletedAt() == null)
                            .map(BaseEntity::getId)
                            .collect(Collectors.toList());

                    if (!idsToDelete.isEmpty()) {
                        repo.hardDeleteMulti(idsToDelete);
                        System.out.println("[Sync][" + tableNameLocal + "] PUSH: xóa " + idsToDelete.size() + " items đã sync deletion.");
                    }

                    if (!idsToMarkSynced.isEmpty()) {
                        repo.markSynced(idsToMarkSynced);
                        System.out.println("[Sync][" + tableNameLocal + "] PUSH: đánh dấu synced " + idsToMarkSynced.size() + " items.");
                    }

                    if (isPullSuccess) {
                        newLastSync = findMaxUpdatedAt(unsyncedItems, newLastSync);
                        syncMetaDataRepo.saveLastSyncTime(tableNameLocal, newLastSync);
                    }
                    System.out.println("[Sync][" + tableNameLocal + "] PUSH: thành công.");
                } else {
                    System.err.println("[Sync][" + tableNameLocal + "] PUSH: thất bại.");
                }
            }

            return Result.success("Đồng bộ bảng " + tableNameLocal + " thành công");
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Lỗi khi đồng bộ bảng " + tableNameLocal + ": " + e.getMessage());
            return Result.fail("Lỗi khi đồng bộ bảng " + tableNameLocal, "SYNC_ERROR");
        }
    }

    /**
     * Quyết định có nên áp dụng thay đổi từ server lên local hay không
     * @param serverItem Item từ server
     * @param localItem  Item tương ứng trong local DB
     * @return true nếu nên apply thay đổi server lên local và ngược lại
     */
    private <T extends BaseEntity> boolean resolveConflict(T serverItem, T localItem) {
        if (localItem == null) {
            return true;
        }

        if (localItem.isSynced()) {
            return true;
        }

        if (localItem.getDeletedAt() != null && serverItem.getDeletedAt() == null) {
            OffsetDateTime localDelTime  = localItem.getUpdatedAt();
            OffsetDateTime serverUpdTime = serverItem.getUpdatedAt();
            if (localDelTime != null && serverUpdTime != null && localDelTime.isAfter(serverUpdTime)) {
                return false;
            }
            return true;
        }

        OffsetDateTime serverTime = serverItem.getUpdatedAt();
        OffsetDateTime localTime  = localItem.getUpdatedAt();

        if (serverTime != null && localTime != null) {
            return !serverTime.isBefore(localTime);
        }

        return true;
    }

    /**
     * Tìm thời gian cập nhật mới nhất trong list item
     * @param items    Danh sách items cần kiểm tra
     * @param lastSync Mốc ban đầu để so sánh
     * @return Thời gian cập nhật mới nhất, dùng để lưu vào syncMetaData phục vụ lần sync sau
     */
    private <T extends BaseEntity> OffsetDateTime findMaxUpdatedAt(List<T> items, OffsetDateTime lastSync) {
        OffsetDateTime maxSync = lastSync;

        for (T item : items) {
            OffsetDateTime updatedAt = item.getUpdatedAt();
            if (updatedAt != null && (maxSync == null || updatedAt.isAfter(maxSync))) {
                maxSync = updatedAt;
            }
        }

        return maxSync;
    }

    /**
     * Lấy thời gian sync cuối cùng của 1 bảng
     * @param tableName Tên bảng muốn lấy thời gian sync cuối cùng
     * @return Thời gian sync cuối cùng
     */
    private OffsetDateTime getLastSyncTime(String tableName) {
        OffsetDateTime lastSync = syncMetaDataRepo.getLastSyncTime(tableName);
        if (lastSync == null) {
            return OffsetDateTime.of(2004, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC);
        }
        return lastSync;
    }
}
