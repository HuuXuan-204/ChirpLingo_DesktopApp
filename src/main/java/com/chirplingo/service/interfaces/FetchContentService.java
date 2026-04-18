package com.chirplingo.service.interfaces;

import com.chirplingo.domain.QuoteOfDay;
import com.chirplingo.domain.newspaper.Newspaper;
import com.chirplingo.domain.Podcast;
import com.chirplingo.domain.WordOfDay;
import java.util.List;

public interface FetchContentService {

    /**
     * Lấy từ vựng của ngày
     * @return Từ vựng của ngày
     */
    public WordOfDay getWordOfDay();

    /**
     * Lấy quote của ngày
     * @return Quote của ngày
     */
    public QuoteOfDay getQuoteOfDay();

    /**
     * Lấy danh sách báo
     * @return Danh sách báo
     */
    public List<Newspaper> getNewspapers();

    /**
     * Lấy danh sách podcast
     * @return Danh sách podcast
     */
    public List<Podcast> getPodcasts();

    /**
     * Lấy báo mới nhất
     * @return Báo mới nhất
     */
    public Newspaper getLatestNewspaper();

    /**
     * Lấy podcast mới nhất
     * @return Podcast mới nhất
     */
    public Podcast getLatestPodcast();
}
