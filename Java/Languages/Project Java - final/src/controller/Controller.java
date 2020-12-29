package controller;

import gui.FormEvent;
import model.*;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class Controller {

    DataBase db = new DataBase();

    public List<Word> getWords(){
        return db.getWords();
    }

    public Word getWord(int index){
        return db.getWords().get( index );
    }

    public void addWord(FormEvent ev){
        String phrase = ev.getPhrase();
        String polish = ev.getPolish();
        int languageCatId = ev.getLanguageCategory();
        String levelCat = ev.getLevelCat();

        LanguageCategory languageCategory = null;
        switch(languageCatId){
            case 0:
                languageCategory = LanguageCategory.english;
                break;
            case 1:
                languageCategory = LanguageCategory.french;
                break;
        }

        LevelCategory levelCategory;
        if(levelCat.equals( "elementary" )) levelCategory = LevelCategory.elementary;
        else if(levelCat.equals( "intermediate" )) levelCategory = LevelCategory.intermediate;
        else if(levelCat.equals( "advanced" )) levelCategory = LevelCategory.advanced;
        else{
            levelCategory = LevelCategory.other;
            System.out.println(levelCat);
        }


        Word word = new Word(phrase, polish, languageCategory, levelCategory);
        db.addPerson( word );
    }
    public void saveToFile(File file) throws IOException {
        db.saveToFile( file );
    }
    public void loadFromFile(File file) throws IOException {
        db.loadFromFile( file );
    }

    public void removeWord(int index) {
        db.removePerson(index);
    }
}
