package gui;

import controller.Controller;
import model.Word;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class TrainingDialog extends JDialog {

    private JButton nextButton;
    private JButton quitButton;

    private JLabel phraseLabel;
    private JLabel translateLabel;
    private JTextField phraseField;
    private JTextField translateField;


    private int numberOfTests;
    private int counterOfTests;

    private Word chosenWord;


    public TrainingDialog(JFrame parent, String dialogName, int numberOfTests, Controller controller, Word word){
        super(parent, dialogName,false);

        nextButton = new JButton("NEXT");
        quitButton = new JButton( "STOP" );

        phraseLabel = new JLabel( "Phrase: " );
        translateLabel = new JLabel( "Translation: " );
        phraseField = new JTextField( 20 );
        phraseField.setEnabled( false );
        translateField = new JTextField( 20 );
        translateField.setEnabled( false );




        this.chosenWord = word;
        phraseField.setText(chosenWord.getPhrase() );
        translateField.setText(chosenWord.getPolish() );

        this.numberOfTests = numberOfTests;
        counterOfTests = 0;


        // Listeners for diacritical sign tables


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


        // THIRD ROW

        gc.gridy++;

        gc.gridx = 1;
        gc.anchor = GridBagConstraints.FIRST_LINE_START;
        gc.insets = new Insets( 0,0,0,5);
        add( nextButton, gc);


        // FOURTH ROW
        gc.gridy++;

        gc.gridx = 1;

        gc.insets = new Insets( 0,0,0,0 );
        gc.anchor = GridBagConstraints.FIRST_LINE_START;
        add( quitButton, gc);


        nextButton.setMnemonic( KeyEvent.VK_ENTER );
        quitButton.setMnemonic( KeyEvent.VK_BACK_SPACE );

        nextButton.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                // textFieldUpgrade

                // juz tutaj musi byc ustawione slowko! TODO
                String phrase, solution;
                phrase = chosenWord.getPhrase();
                solution = chosenWord.getPolish();

                phraseField.setText( phrase );
                translateField.setText( solution );

                TrainingDialog.this.counterOfTests++;


                // System.out.println(points);

                if (TrainingDialog.this.numberOfTests == TrainingDialog.this.counterOfTests) setVisible( false );

                TrainingDialog.this.chosenWord = controller.getWord( new Random().nextInt( controller.getWords().size() ) );
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
