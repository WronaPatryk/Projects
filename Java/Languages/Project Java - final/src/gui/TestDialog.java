package gui;


import model.Word;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;
import java.util.Random;

public class TestDialog extends JDialog {

    private JButton okButton;
    private JButton quitButton;

    private JLabel phraseLabel;
    private JLabel translateLabel;
    private JTextField phraseField;
    private JTextField translateField;
    private TextPanel textPanel;

    JScrollPane tableContainer;
    private JTable diacritical;
    JScrollPane tableContainer2;
    private JTable diacritical2;


    private int points;
    private int numberOfTests;
    private int counterOfTests;
    private boolean toPolish = false;

    private Word chosenWord;
    private String level;


    public TestDialog(JFrame parent, String dialogName, int numberOfTests, java.util.List<Word> listOfWords, Word word, String level){
        super(parent, dialogName,false);

        if (dialogName.equals( "Translate to Polish" )) toPolish = true;
        okButton = new JButton("CHECK!");
        quitButton = new JButton( "CANCEL" );

        phraseLabel = new JLabel( "Phrase: " );
        translateLabel = new JLabel( "Translation: " );
        phraseField = new JTextField( 20 );
        phraseField.setEnabled( false );
        translateField = new JTextField( 20 );
        textPanel = new TextPanel();
        textPanel.setPreferredSize( new Dimension( 200,60 ) );

        // diacritical signs
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



        this.chosenWord = word;
        this.level = level;
        if(toPolish) phraseField.setText(chosenWord.getPhrase() );
        else phraseField.setText(chosenWord.getPolish() );

        this.numberOfTests = numberOfTests;
        counterOfTests = 0;
        points = 0;


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
                translateField.setText( translateField.getText() + selectedLetter );
                translateField.requestFocusInWindow();
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
                translateField.setText( translateField.getText() + selectedLetter );
                translateField.requestFocusInWindow();
                diacritical2.clearSelection();

            }
        });



        // layoutComponents :

        setLayout( new GridBagLayout() );

        GridBagConstraints gc = new GridBagConstraints(  );

        gc.gridy = 0;
        gc.weightx = 1;
        gc.weighty = 1;
        gc.fill = GridBagConstraints.NONE;


        // FIRST ROW
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

        // SECOND ROW

        gc.gridx = 0;
        gc.gridy++;
        gc.fill = GridBagConstraints.NONE; // pretty important to set this, if not, u tend to have problems later
        gc.anchor = GridBagConstraints.LINE_END;
        gc.insets = new Insets( 0,0,0,5);

        add( translateLabel, gc);

        gc.gridx++;


        gc.insets = new Insets( 0,0,0,0 );
        gc.anchor = GridBagConstraints.LINE_START;
        add( translateField, gc);

        // 2.5 row //
        gc.gridy++;

        gc.gridx = 1;
        add( tableContainer, gc);
        gc.gridy++;
        add( tableContainer2, gc);

        // THIRD ROW

        gc.gridy++;

        gc.gridx = 1;
        gc.anchor = GridBagConstraints.FIRST_LINE_START;
        gc.insets = new Insets( 0,0,0,5);
        add(okButton, gc);

        gc.gridx = 1;
        gc.gridy++;
        gc.insets = new Insets( 0,0,0,0 );
        gc.anchor = GridBagConstraints.FIRST_LINE_START;
        add( quitButton, gc);

        gc.gridy++;
        gc.weighty = 5;
        add(textPanel, gc);


        okButton.setMnemonic( KeyEvent.VK_ENTER );
        quitButton.setMnemonic( KeyEvent.VK_BACK_SPACE );

        okButton.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                // textFieldUpgrade

                // a word must be already set at this point!
                String phrase, solution;
                if(toPolish) {
                    phrase = chosenWord.getPolish();
                    solution = chosenWord.getPhrase();
                }
                else {
                    phrase = chosenWord.getPhrase();
                    solution = chosenWord.getPolish();
                }
                String translation = translateField.getText();

                if(phrase.equals( translation )) {
                    textPanel.appendText( "Good Answer!\n" );
                    points++;
                } else {
                    textPanel.appendText(phrase + " = " + solution + "\n");
                }
                TestDialog.this.counterOfTests++;



                // System.out.println(points);

                if(TestDialog.this.numberOfTests == TestDialog.this.counterOfTests) {
                double procent = ((double)points/(double) TestDialog.this.numberOfTests)*100;
                    if(procent <50){
                        JOptionPane.showMessageDialog( TestDialog.this,
                            "You shall not pass!\n"+points+"/"+ TestDialog.this.numberOfTests + " = "+
                                    new DecimalFormat("#.##").format(  procent) + "%" ,
                            "Results", 0);}
                    if(procent >=50 && procent < 100)JOptionPane.showMessageDialog( TestDialog.this,
                            "Points: "+ points+"/"+ TestDialog.this.numberOfTests + " = "+
                                    new DecimalFormat("#.##").format(  procent) + "%",
                            "Results", 1);
                    if(procent == 100) {
                        ImageIcon img = new ImageIcon(".\\wellDone.png");   // TODO
                        JOptionPane.showMessageDialog( TestDialog.this,
                                "Congratulations!\nPoints: " + points + "/" + TestDialog.this.numberOfTests + " = " +
                                        new DecimalFormat( "#.##" ).format( procent ) + "%",
                                "Results", 1, img);
                    }
                    setVisible( false );
                }


                // a new word:
                TestDialog.this.chosenWord = listOfWords.get(new Random().nextInt( listOfWords.size() ));

                if(toPolish) phraseField.setText(chosenWord.getPhrase() );
                else phraseField.setText(chosenWord.getPolish() );
                translateField.setText( "" );
                translateField.requestFocusInWindow();

            }
        } );

        quitButton.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible( false );
            }
        } );
        setMinimumSize( new Dimension( 450, 350 ) );
        setSize(400, 300);
        setLocationRelativeTo( parent );
    }

}
