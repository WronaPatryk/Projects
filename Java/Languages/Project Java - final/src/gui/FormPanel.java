package gui;

import javax.swing.*;
import javax.swing.border.Border;

import java.awt.*;
import java.awt.event.*;

public class FormPanel extends JPanel {


    private JLabel phraseLabel;
    private JLabel polishLabel;
    private JTextField phraseField;
    private JTextField polishField;
    private JButton addBtn;
    private JButton testBtn;
    private JButton trainingBtn;
    private FormListener formListener;
    private JComboBox levelTestCombo;
    private JComboBox languageCombo;
    private JComboBox levelCombo;
    private JSpinner portSpinner;
    private SpinnerNumberModel spinnerModel;
    JScrollPane tableContainer;
    JScrollPane tableContainer2;

    private JComboBox testCombo;
    private JTable diacritical;
    private JTable diacritical2;

    public JComboBox getTestCombo() {
        return testCombo;
    }

    public FormPanel(){
        Dimension dim = getPreferredSize();
        dim.width = 250;
        setPreferredSize( dim );

        phraseLabel = new JLabel( "Word/phrase: " );
        polishLabel = new JLabel( "Polish: " );
        phraseField = new JTextField( 11 );
        polishField = new JTextField( 11 );
        languageCombo = new JComboBox();
        levelTestCombo = new JComboBox();
        levelCombo = new JComboBox();
        addBtn = new JButton( "ADD" );
        testBtn = new JButton( "TEST" );
        trainingBtn = new JButton( "LEARN" );
        spinnerModel = new SpinnerNumberModel( 10, 1, 200, 1 );
        portSpinner = new JSpinner( spinnerModel );
        testCombo = new JComboBox();
        String[][] data = {{"\u00e0","\u00e2","\u00e9","\u00e8","\u00ea","\u00ef"}};
        String[] colnames = {"\u00e0","\u00e2","\u00e9","\u00e8","\u00ea","\u00ef"};
        diacritical = new JTable( data, colnames ){
            public boolean isCellEditable(int row, int column){
                return false;
            }
        };
        diacritical.setTableHeader(null);
        tableContainer = new JScrollPane();
        tableContainer.setPreferredSize(new Dimension(104, 19));
        tableContainer.getViewport().add(diacritical);

        data = new String[][] {{"\u00ef","\u00f4","\u00f6","\u00f9","\u00fb","\u00e7"}};
        colnames = new String[] {"\u00ef","\u00f4","\u00f6","\u00f9","\u00fb","\u00e7"};
        diacritical2 = new JTable( data, colnames ){
            public boolean isCellEditable(int row, int column){
                return false;
            }
        };
        diacritical2.setTableHeader(null);
        tableContainer2 = new JScrollPane();
        tableContainer2.setPreferredSize(new Dimension(104, 19));
        tableContainer2.getViewport().add(diacritical2);



        // Set up mnemomics
        addBtn.setMnemonic( KeyEvent.VK_A );
        testBtn.setMnemonic( KeyEvent.VK_T );
        trainingBtn.setMnemonic( KeyEvent.VK_L );
        phraseLabel.setDisplayedMnemonic( KeyEvent.VK_N );
        phraseLabel.setLabelFor( phraseField );


        // Set up language combo box
        DefaultComboBoxModel languageModel = new DefaultComboBoxModel();
        languageModel.addElement( new LanguageCategory( 0, "English" ));
        languageModel.addElement( new LanguageCategory(1,"French"));
        languageCombo.setModel( languageModel );
        languageCombo.setSelectedIndex( 0 );



        // Set up level combo box
        DefaultComboBoxModel levelModel = new DefaultComboBoxModel(  );
        levelModel.addElement( "elementary" );
        levelModel.addElement( "intermediate" );
        levelModel.addElement( "advanced" );
        levelCombo.setModel( levelModel );
        levelCombo.setSelectedIndex( 2 );
        // levelCombo.setEditable( true );


        // Set up level test combo box
        DefaultComboBoxModel levelTestModel = new DefaultComboBoxModel(  );
        levelTestModel.addElement( "---" );
        levelTestModel.addElement( "elementary" );
        levelTestModel.addElement( "intermediate" );
        levelTestModel.addElement( "advanced" );
        levelTestCombo.setModel( levelTestModel );
        levelTestCombo.setSelectedIndex( 0 );


        // Set up test combo box
        DefaultComboBoxModel testModel = new DefaultComboBoxModel(  );
        testModel.addElement( "Polish -> *" );
        testModel.addElement( "* -> Polish" );
        testCombo.setModel( testModel );
        testCombo.setSelectedIndex(0);


        // Set LISTENERS

        addBtn.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String phrase = phraseField.getText();
                String polish = polishField.getText();
                LanguageCategory languageCat = (LanguageCategory) languageCombo.getSelectedItem();
                String levelCat = (String) levelCombo.getSelectedItem();
                // maybe for multiple values:  languageList.setSelectionMode( 2 );

                System.out.println(levelCat);

                // WARNING: languageCat can be null!
                FormEvent ev = new FormEvent( this, phrase, polish, languageCat.getId(), levelCat);

                if(formListener != null){
                    formListener.formEventOccurred( ev );
                }
                polishField.setText( "" );
                phraseField.setText( "" );
            }
        } );

        // Listeners for diacritical sign tables

        diacritical.addMouseListener( new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(! SwingUtilities.isLeftMouseButton( e )) return;
                String selectedLetter = null;

                int[] selectedRow = diacritical.getSelectedRows();
                int[] selectedColumns = diacritical.getSelectedColumns();

                for (int i = 0; i < selectedRow.length; i++) {
                    for (int j = 0; j < selectedColumns.length; j++) {
                        selectedLetter = (String) diacritical.getValueAt(selectedRow[i], selectedColumns[j]);
                    }
                }
                    phraseField.setText( phraseField.getText() + selectedLetter );
                    phraseField.requestFocusInWindow();
                    diacritical.clearSelection();
            }
        });

        diacritical2.addMouseListener( new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(! SwingUtilities.isLeftMouseButton( e )) return;
                String selectedLetter = null;

                int[] selectedRow = diacritical2.getSelectedRows();
                int[] selectedColumns = diacritical2.getSelectedColumns();

                for (int i = 0; i < selectedRow.length; i++) {
                    for (int j = 0; j < selectedColumns.length; j++) {
                        selectedLetter = (String) diacritical2.getValueAt(selectedRow[i], selectedColumns[j]);
                    }
                }
                    phraseField.setText( phraseField.getText() + selectedLetter );
                    phraseField.requestFocusInWindow();
                    diacritical2.clearSelection();

            }
        });
        diacritical.setVisible( false );
        diacritical2.setVisible( false );

        // whether to show French diacriticals or not
        languageCombo.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = languageCombo.getSelectedIndex();
                if(selectedIndex == 0) {
                    diacritical.setVisible( false );
                    diacritical2.setVisible( false );
                }
                if(selectedIndex == 1) {
                    diacritical.setVisible( true );
                    diacritical2.setVisible( true );
                }
            }
        } );



        Border outerBorder = BorderFactory.createTitledBorder( "Add & test words" );
        Border innerBorder = BorderFactory.createEmptyBorder(5,5,5,5);
        setBorder( BorderFactory.createCompoundBorder( outerBorder, innerBorder ) );
        layoutComponents();

    }

    public void layoutComponents(){
        setLayout( new GridBagLayout() );
        GridBagConstraints gc = new GridBagConstraints( );

        gc.weightx = 1;
        gc.weighty = 0.1;

        // First row //

        gc.gridx = 0;
        gc.gridy = 0;
        gc.fill = GridBagConstraints.NONE; // pretty important to set this, if not, u tend to have problems later
        gc.anchor = GridBagConstraints.LINE_END;
        gc.insets = new Insets( 0,0,0,5);

        add( phraseLabel, gc);

        gc.gridx = 1;
        gc.gridy = 0;
        gc.insets = new Insets( 0,0,0,0 );
        gc.anchor = GridBagConstraints.LINE_START;
        add( phraseField, gc);

        // 1.5 row //
        gc.gridy++;

        gc.gridx = 0;
        gc.anchor = GridBagConstraints.FIRST_LINE_END;
        gc.insets = new Insets( 0,0,0,5);
        add(new JLabel( "Diacritics: " ), gc);
        gc.anchor = GridBagConstraints.FIRST_LINE_START;
        gc.gridx = 1;
        add( tableContainer, gc);
        gc.weighty = 0.05;
        gc.gridy++;
        add( tableContainer2, gc);

        gc.weighty = 0.1;
        // Second row //
        gc.gridy++;

        gc.gridx = 0;
        gc.anchor = GridBagConstraints.LINE_END;
        gc.insets = new Insets( 0,0,0,5);
        add( polishLabel, gc);

        gc.gridx = 1;

        gc.anchor = GridBagConstraints.LINE_START;
        gc.insets = new Insets( 0,0,0,0 );
        add( polishField, gc);

        // Next row //
        gc.gridy++;

        gc.gridx = 0;
        gc.anchor = GridBagConstraints.FIRST_LINE_END;
        gc.insets = new Insets( 0,0,0,5);
        add(new JLabel( "Language: " ), gc);

        gc.gridx = 1;
        gc.anchor = GridBagConstraints.FIRST_LINE_START;
        gc.insets = new Insets( 0,0,0,0 );
        add( languageCombo, gc);

        // Next row //
        gc.gridy++;

        gc.gridx = 0;
        gc.anchor = GridBagConstraints.FIRST_LINE_END;
        gc.insets = new Insets( 0,0,0,5);
        add(new JLabel( "Level: " ), gc);

        gc.gridx = 1;
        gc.anchor = GridBagConstraints.FIRST_LINE_START;
        gc.insets = new Insets( 0,0,0,0 );
        add( levelCombo, gc);

        // Next row //
        gc.gridy++;


        gc.gridx = 1;

        gc.anchor = GridBagConstraints.FIRST_LINE_START;
        gc.insets = new Insets( 0,0,0,0 );
        add( addBtn, gc);

        // Next row //
        gc.gridy++;


        gc.gridx = 0;
        gc.anchor = GridBagConstraints.FIRST_LINE_END;
        gc.insets = new Insets( 0,0,0,5);
        add(new JLabel( "Test choice: " ), gc);

        gc.gridx = 1;
        gc.anchor = GridBagConstraints.FIRST_LINE_START;
        gc.insets = new Insets( 0,0,0,0 );
        add( testCombo, gc);

        // Spinner

        gc.gridy++;


        gc.gridx = 0;
        gc.anchor = GridBagConstraints.FIRST_LINE_END;
        gc.insets = new Insets( 0,0,0,5);
        add(new JLabel( "How many: " ), gc);

        gc.gridx++;
        gc.anchor = GridBagConstraints.FIRST_LINE_START;
        gc.insets = new Insets( 0,0,0,0 );
        add(portSpinner, gc);


        // Test Level Choice

        gc.gridy++;


        gc.gridx = 0;
        gc.anchor = GridBagConstraints.FIRST_LINE_END;
        gc.insets = new Insets( 0,0,0,5);
        add(new JLabel( "Test Level: " ), gc);

        gc.gridx++;
        gc.anchor = GridBagConstraints.FIRST_LINE_START;
        gc.insets = new Insets( 0,0,0,0 );
        add(levelTestCombo, gc);

        // Next row //
        gc.gridy++;

        gc.weighty = 0.1;

        gc.anchor = GridBagConstraints.FIRST_LINE_START;
        gc.insets = new Insets( 0,0,0,0 );
        add(testBtn, gc);

        gc.gridy++;

        gc.weighty = 0.5;


        gc.anchor = GridBagConstraints.FIRST_LINE_START;
        gc.insets = new Insets( 0,0,0,0 );
        add(trainingBtn, gc);



    }

    public void setFormListener(FormListener fl){
        this.formListener = fl;
    }

    public JSpinner getSpinner() {
        return portSpinner;
    }

    public JComboBox getLevelBtn() {
        return levelTestCombo;
    }

    class LanguageCategory { // utility class
        private String text;
        private int id;

        public LanguageCategory(int id, String text){
            this.id = id;
            this.text = text;
        }

        @Override
        public String toString() {
            return text;
        }

        public int getId() {
            return id;
        }
    }

    public JButton getTestBtn(){
        return testBtn;
    }
    public JButton getTrainingBtn() {return trainingBtn;}

}
