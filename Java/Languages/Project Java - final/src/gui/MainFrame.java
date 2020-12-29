package gui;

import controller.Controller;
import model.LevelCategory;
import model.Word;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class MainFrame extends JFrame {

    private FormPanel formPanel;
    private JFileChooser fileChooser;
    private Controller controller;
    private TablePanel tablePanel;
    private TestDialog testDialog;
    private TrainingDialog trainingDialog;

    MainFrame(){
        super("Languages");

        setLayout( new BorderLayout() );
        formPanel = new FormPanel();
        tablePanel = new TablePanel();

        controller = new Controller();

        tablePanel.setData(controller.getWords());

        tablePanel.setWordTableListener( new WordTableListener(){
            public void rowDeleted(int row){
                controller.removeWord(row);
            }
        });

        fileChooser = new JFileChooser();
        fileChooser.addChoosableFileFilter( new WordFileFilter() );

        setJMenuBar( createMenuBar() );





        formPanel.setFormListener(new FormListener(){
            public void formEventOccurred(FormEvent e){

                controller.addWord( e );
                tablePanel.refresh();
            }

        });


        formPanel.getTestBtn().addActionListener( new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String level = (String) formPanel.getLevelBtn().getSelectedItem();
                String dialogName = (String) formPanel.getTestCombo().getSelectedItem();
                if (dialogName.equals( "Polish -> *" )) dialogName = "Translate to other language";
                else if(dialogName.equals( "* -> Polish" )) dialogName = "Translate to Polish";


                Integer numberOfTests = (Integer) formPanel.getSpinner().getValue();

                java.util.List<Word> listOfWords = controller.getWords();


                // tworzy liste slowek z danym poziomem:
                if(level == "elementary") {
                    java.util.List<Word> lista = new LinkedList<Word>();
                    for ( Word word: listOfWords) {
                        if(word.getLevelCat() == LevelCategory.elementary) lista.add( word );
                    }
                    listOfWords = lista;
                }
                if(level == "intermediate") {
                    java.util.List<Word> lista = new LinkedList<Word>();
                    for ( Word word: listOfWords) {
                        if(word.getLevelCat() == LevelCategory.intermediate) lista.add( word );
                    }
                    listOfWords = lista;
                }
                if(level == "advanced") {
                    java.util.List<Word> lista = new LinkedList<Word>();
                    for ( Word word: listOfWords) {
                        if(word.getLevelCat() == LevelCategory.advanced) lista.add( word );
                    }
                    listOfWords = lista;
                }



                Word word = listOfWords.get(new Random().nextInt( listOfWords.size() ));
                testDialog = new TestDialog(MainFrame.this , dialogName, numberOfTests, listOfWords, word, level);
                testDialog.setVisible( true );
            }
        } );

        formPanel.getTrainingBtn().addActionListener( new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String dialogName =  "Learning mode";

                //Integer numberOfTests = (Integer) formPanel.getSpinner().getValue();
                Word word = controller.getWord(new Random().nextInt( controller.getWords().size() ));
                trainingDialog = new TrainingDialog(MainFrame.this , dialogName, 500, controller, word);
                trainingDialog.setVisible( true );
            }
        } );


        add(formPanel, BorderLayout.WEST);
        add(tablePanel, BorderLayout.CENTER);

        setMinimumSize( new Dimension( 800, 650 ) );
        setSize( 600, 500 );
        setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        setVisible( true );


    }



    private JMenuBar createMenuBar(){
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu( "File" );
        JMenuItem exportDataItem = new JMenuItem( "Export Data..." );
        JMenuItem importDataItem = new JMenuItem( "Import Data..." );
        JMenuItem exitItem = new JMenuItem( "Exit" );

        fileMenu.add( exportDataItem );
        fileMenu.add( importDataItem );
        fileMenu.addSeparator();
        fileMenu.add( exitItem );

        JMenu windowMenu = new JMenu( "Window" );
        JMenu showMenu = new JMenu( "Show" );
        JMenuItem prefsItem = new JMenuItem( "Last Test Dialog" );

        JMenuItem showFormItem = new JCheckBoxMenuItem( "Word Addition" );
        showFormItem.setSelected( true );
        JMenuItem showDataItem = new JCheckBoxMenuItem( "Data Table" );
        showDataItem.setSelected( true );

        showMenu.add( showFormItem );
        showMenu.add( showDataItem );
        windowMenu.add( showMenu );
        windowMenu.add(prefsItem);

        menuBar.add( fileMenu );
        menuBar.add( windowMenu );

        prefsItem.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                testDialog.setVisible( true );
            }
        } );

        showFormItem.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JCheckBoxMenuItem menuItem = (JCheckBoxMenuItem) e.getSource();

                formPanel.setVisible( menuItem.isSelected() );
            }
        } );
        showDataItem.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JCheckBoxMenuItem menuItem = (JCheckBoxMenuItem) e.getSource();

                tablePanel.setVisible( menuItem.isSelected() );
            }
        } );

        fileMenu.setMnemonic( KeyEvent.VK_F );
        exitItem.setMnemonic( KeyEvent.VK_X );

        exitItem.setAccelerator( KeyStroke.getKeyStroke( KeyEvent.VK_X, ActionEvent.CTRL_MASK ) );

        importDataItem.setAccelerator( KeyStroke.getKeyStroke( KeyEvent.VK_I, ActionEvent.CTRL_MASK ) );


        importDataItem.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(fileChooser.showOpenDialog( MainFrame.this ) == JFileChooser.APPROVE_OPTION){
                    try {
                        controller.loadFromFile( fileChooser.getSelectedFile() );
                        tablePanel.refresh();
                    } catch (IOException e1) {
                        JOptionPane.showMessageDialog( MainFrame.this, "Loading data failed.", "Error", JOptionPane.ERROR_MESSAGE );
                    }
                    System.out.println(fileChooser.getSelectedFile() );
                }
            }
        } );

        exportDataItem.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(fileChooser.showSaveDialog( MainFrame.this ) == JFileChooser.APPROVE_OPTION){
                    try {
                        controller.saveToFile( fileChooser.getSelectedFile() );
                    } catch (IOException e1) {
                        JOptionPane.showMessageDialog( MainFrame.this, "Saving data failed.", "Error", JOptionPane.ERROR_MESSAGE );
                    }
                    System.out.println(fileChooser.getSelectedFile() );
                }
            }
        } );

        exitItem.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                int action = JOptionPane.showConfirmDialog( MainFrame.this, "Do your really want to exit the application?",
                        "Confirm Exit", JOptionPane.OK_CANCEL_OPTION); // WARNING MESSAGE
                if(action == JOptionPane.OK_OPTION) {
                    System.exit( 0 );
                }
            }
        } );

        return menuBar;
    }
}
