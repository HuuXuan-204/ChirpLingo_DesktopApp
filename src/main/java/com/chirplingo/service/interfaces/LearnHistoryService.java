package com.chirplingo.service.interfaces;

import com.chirplingo.domain.LearnHistory;

public interface LearnHistoryService {
    /**
     * Lưu lịch sử học
     * @param lh Lịch sử học cần lưu
     * @return true nếu lưu thành công, false nếu thất bại
     */
    public boolean saveLearnHistory(LearnHistory lh);

    /**
     * Lấy số ngày học liên tục
     * @return Số ngày học liên tục
     */
    public int getStreak();

    /**
     * Lấy lịch sử học
     * @return Lịch sử học
     */
    public LearnHistory getLearnHistory();
}
