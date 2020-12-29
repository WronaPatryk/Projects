package model;

import java.io.Serializable;

public class Word implements Serializable {

    private static final long serialVersionUID = - 81292343382123L;

    private static int count = 1;

    private int id;
    private String phrase;
    private String polish;
    private LanguageCategory languageCategory;
    private LevelCategory levelCat;

    public Word(String phrase, String polish, LanguageCategory languageCategory, LevelCategory levelCat) {
        this.phrase = phrase;
        this.polish = polish;
        this.languageCategory = languageCategory;
        this.levelCat = levelCat;

        this.id = count;
        count++;
    }

    // GETTERS:

    public int getId() {
        return id;
    }



    public String getPhrase() {
        return phrase;
    }



    public String getPolish() {
        return polish;
    }



    public LanguageCategory getLanguageCategory() {
        return languageCategory;
    }



    public LevelCategory getLevelCat() {
        return levelCat;
    }


}
