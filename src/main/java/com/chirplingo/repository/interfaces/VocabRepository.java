package com.chirplingo.repository.interfaces;

import java.util.List;
import com.chirplingo.domain.Vocabulary;

public interface VocabRepository extends Repository<Vocabulary> {
    
    /**------------------------------------------------------------------
     *  Lấy các từ vựng đến hạn ôn tập
     * @return List các từ vựng đến hạn ôn tập
     */
    public List<Vocabulary> getDue();

    /**------------------------------------------------------------------
     * Đếm số lượng từ vựng đến hạn ôn tập
     * @return Số lượng từ vựng đến hạn ôn tập
     */
    public int countDue();

    /**------------------------------------------------------------------
     * Tìm kiếm từ vựng theo từ khóa (Tìm trong cả trường word và meaning)
     * @param keyword Từ khóa cần tìm
     * @return List các từ vựng tìm được
     */
    public List<Vocabulary> searchVocabs (String keyword);

    /**
     * Lấy random từ phục vụ cho chế độ ôn thường, không update SRS info
     * @param number số từ User muốn ôn
     * @return List các từ vựng random để ôn
     */
    public List<Vocabulary> getRandom(int number);

}
