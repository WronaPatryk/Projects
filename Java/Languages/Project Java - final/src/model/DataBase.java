package model;


import java.io.*;
import java.util.*;

public class DataBase {
    private List<Word> words;

    public DataBase(){
        words = new LinkedList<Word>();
    }

    public void addPerson(Word word){
        words.add( word );
    }

    public List<Word> getWords(){
        return Collections.unmodifiableList( words );
    }

    public void saveToFile(File file) throws IOException {
        FileOutputStream fos = new FileOutputStream( file );
        ObjectOutputStream oos = new ObjectOutputStream( fos );

        Word[] words = this.words.toArray(new Word[ this.words.size()]);

        oos.writeObject( words );

        oos.close();
    }
    public void loadFromFile(File file) throws IOException{
        FileInputStream fis = new FileInputStream( file );
        ObjectInputStream ois = new ObjectInputStream( fis );

        try {
            Word[] words = (Word[]) ois.readObject();

            this.words.clear();

            this.words.addAll( Arrays.asList( words ));

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        ois.close();
    }
    public void removePerson(int index) {
        words.remove( index );
    }

}
