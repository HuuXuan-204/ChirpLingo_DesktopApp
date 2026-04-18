package com.chirplingo.service.interfaces;

import com.chirplingo.domain.DictionaryEntry;
import java.util.List;

public interface DictionaryService {

    /**
     * Tìm kiếm từ trong từ điển
     * @param keyword Từ cần tìm kiếm
     * @return Danh sách các từ tìm thấy
     */
    public List<DictionaryEntry> search(String keyword);
    
}
