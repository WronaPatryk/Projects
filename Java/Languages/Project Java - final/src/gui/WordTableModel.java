package gui;

import model.Word;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class WordTableModel extends AbstractTableModel {

    private List<Word> db;

    private String[] colNames = {"ID", "Phrase", "Polish", "Language", "Level"};

    @Override
    public String getColumnName(int column) {
        return colNames[column];
    }

    public void setData(List<Word> db){
        this.db = db;
    }

    @Override
    public int getRowCount() {
        return db.size();
    }

    @Override
    public int getColumnCount() {
        return 5;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Word word = db.get(rowIndex);

        switch (columnIndex){
            case 0: return word.getId();
            case 1: return word.getPhrase();
            case 2: return word.getPolish();
            case 3: return word.getLanguageCategory();
            case 4: return word.getLevelCat();
        }
        return null;
    }
}
