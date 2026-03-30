package com.chirplingo.domain;
import java.util.ArrayList;

public class DictionaryEntry {
    //khai bao thuoc tinh
    private String word;
    private String meaning;
    private String type;
    private String example;
    private ArrayList<String> synonyms;
    private ArrayList<String> antonyms;
    public DictionaryEntry(String word, String meaning, String type, String example, ArrayList<String> synonyms, ArrayList<String> antonyms){
        this.word = word;
        this.meaning = meaning;
        this.type = type;
        this.example = example;
        this.synonyms = synonyms;
        this.antonyms = antonyms;
    }
    public String getWord(){
        return word;
    }
    public String getMeaning(){
        return meaning;
    }
    public String getType(){
        return type;
    }
    public String getExample(){
        return example;
    }
    public ArrayList<String> getSynonyms(){
        return synonyms;
    }
     public ArrayList<String> getAntonyms(){
        return antonyms;
    }
     //Phuong thuc kiem tra du lieu co khop khong
     public boolean isMatch(String input){
        return word.equalsIgnoreCase(input);// so sanh hai chuoi ma khong phan biet chu hoa hay chu thuong
     }

}