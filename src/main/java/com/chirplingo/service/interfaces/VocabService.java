package com.chirplingo.service.interfaces;

import java.util.List;
import com.chirplingo.domain.Vocabulary;
import com.chirplingo.domain.Result;

public interface VocabService {
    /**
     * Tạo một entry từ vựng mới
     * @return Vocabulary với đầy đủ thông tin 
     */
    public Vocabulary addNewVocabEntry();
    /**
     * Lưu nhiều từ vựng cùng lúc
     * @param vocabs Danh sách từ vựng cần lưu
     * @return true nếu lưu thành công, false nếu thất bại
     */
    public Result saveMultiVocabs(List<Vocabulary> vocabs);
    
    /**
     * Lấy tất cả từ vựng
     * @return Danh sách tất cả từ vựng
     */
    public List<Vocabulary> getAllVocabs();

    /**
     * Lấy danh sách từ vựng đến hạn ôn
     * @return Danh sách từ vựng đến hạn ôn
     */
    public List<Vocabulary> getDueVocabs();
 
    /**
     * Lấy số lượng từ vựng đến hạn ôn
     * @return Số lượng từ vựng đến hạn ôn
     */
    public int getDueCount();

    /**
     * Xóa mềm danh sách từ vựng
     * @param vocabs Danh sách từ vựng cần xóa
     * @return true nếu xóa thành công, false nếu thất bại
     */
    public Result softDeleteVocabs(List<Vocabulary> vocabs);

    /**
     * Tìm kiếm từ vựng
     * @param keyword Từ khóa tìm kiếm
     * @return Danh sách từ vựng tìm thấy
     */
    public List<Vocabulary> searchVocabs(String keyword);

    /**
     * Lưu danh sách từ vựng vào bộ nhớ khi đang thêm dở từ vựng
     * @param vocabs Danh sách từ vựng cần lưu
     * @return true nếu lưu thành công, false nếu thất bại
     */
    public boolean saveDraft(List<Vocabulary> vocabs);

    /**
     * Tải danh sách từ vựng đang thêm dở từ bộ nhớ 
     * @return Danh sách từ vựng từ bộ nhớ
     */
    public List<Vocabulary> loadDraft();

    /**
     * Xóa bản nháp hiện tại
     * @return true nếu xóa thành công và ngược lại
     */
    public boolean clearDraft();

}
