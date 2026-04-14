package com.chirplingo.domain;
import java.util.List;

public class DictionaryEntry {
    private String word;
    private String meaning;
    private String type;
    private String example;
    private List<String> synonyms;
    private List<String> antonyms;

    public DictionaryEntry(String word, String meaning, String type, String example, List<String> synonyms, List<String> antonyms){
        this.word = word;
        this.meaning = meaning;
        this.type = type;
        this.example = example;
        this.synonyms = synonyms;
        this.antonyms = antonyms;
    }

    public String getWord(){
        return this.word;
    }

    public String getMeaning(){
        return this.meaning;
    }

    public String getType(){
        return this.type;
    }

    public String getExample(){
        return this.example;
    }

    public List<String> getSynonyms(){
        return this.synonyms;
    }

     public List<String> getAntonyms(){
        return this.antonyms;
    }
     
    /**
     * Kiểm tra xem từ khóa có xuất hiện trong word hoặc meaning hay không
     * @param keyword là từ khóa tìm kiếm
     * @return true nếu xuất hiện (Không phân biệt hoa thường) và ngược lại
     */
    public boolean isMatch(String keyword){
        if(keyword == null || keyword.trim().isEmpty()) return false;

        keyword = keyword.toLowerCase();
        boolean matchWord = this.word != null && word.toLowerCase().contains(keyword);
        boolean matchMeaning = this.meaning != null && meaning.toLowerCase().contains(keyword);
        
        return matchWord || matchMeaning;
    }

}