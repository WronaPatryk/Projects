package gui;

import model.Word;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class TablePanel extends JPanel {

    private JTable table;
    private WordTableModel tableModel;
    private JPopupMenu popup;
    private WordTableListener wordTableListener;

    public TablePanel(){
        tableModel = new WordTableModel();
        table = new JTable(tableModel);
        popup = new JPopupMenu();

        JMenuItem removeItem = new JMenuItem("Delete row");
        popup.add( removeItem );

        table.addMouseListener( new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {

                int row = table.rowAtPoint( e.getPoint() );

                table.getSelectionModel().setSelectionInterval( row, row );

                if(e.getButton() == MouseEvent.BUTTON3){
                    popup.show( table, e.getX(), e.getY() );
                }
            }
        } );

        removeItem.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = table.getSelectedRow();

                if(wordTableListener != null){
                    wordTableListener.rowDeleted( row );
                    tableModel.fireTableRowsDeleted( row, row );
                }
            }
        } );
        setLayout(new BorderLayout(  ) );

        add(new JScrollPane(table), BorderLayout.CENTER);
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment( JLabel.CENTER );
        table.getColumnModel().getColumn(0).setCellRenderer( centerRenderer );
        table.getColumnModel().getColumn(1).setCellRenderer( centerRenderer );
        table.getColumnModel().getColumn(2).setCellRenderer( centerRenderer );
        table.getColumnModel().getColumn(3).setCellRenderer( centerRenderer );
        table.getColumnModel().getColumn(4).setCellRenderer( centerRenderer );
    }

    public void setData(List<Word> db){
        tableModel.setData( db );

    }

    public void refresh() {
        tableModel.fireTableDataChanged();
    }
    public void setWordTableListener(WordTableListener listener){
        this.wordTableListener = listener;
    }
}
