package gui;

import java.util.EventObject;

public class FormEvent  extends EventObject {

    private String phrase;
    private String polish;
    private int languageCategory;
    private String levelCat;


    // stores info temporarily and relays it

    public String getPhrase() {
        return phrase;
    }
    public String getPolish() {
        return polish;
    }
    public void setPolish(String polish) {
        this.polish = polish;
    }
    public int getLanguageCategory() {
        return languageCategory;
    }
    public String getLevelCat() {
        return levelCat;
    }



    public FormEvent(Object source, String phrase, String polish, int languageCategory, String levelCat) {
        super( source );
        this.phrase = phrase;
        this.polish = polish;
        this.languageCategory = languageCategory;
        this.levelCat = levelCat;
    }


}
