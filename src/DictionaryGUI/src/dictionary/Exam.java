/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dictionary;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

/**
 *
 * @author jakub
 */
public class Exam extends JFrame {
    
    public Exam(String categoryName, String setName) {
        
        this.categoryName = categoryName;
        this.setName = setName;
        getDataFromDatabase();
        assignLanguages();
        createShuffledList();
        initComponents();
        performExam();
    }
    
    private void initComponents() {
        
        // Upper panel
        this.upperPanel = new JPanel(null);
        this.upperPanel.setPreferredSize(new Dimension(770, 30));
        
        // Main header label
        this.mainHeaderLab = new JLabel("Exam is performing for category: " + 
                this.categoryName + ", set: " + this.setName);
        this.mainHeaderLab.setFont(new Font("Dialog", 1, 18));
        this.mainHeaderLab.setBounds(0, 0, this.frameWidth, 30);
        this.mainHeaderLab.setHorizontalAlignment(SwingConstants.CENTER);
        this.upperPanel.add(this.mainHeaderLab);
        // Upper panel end
        
        // Center panel
        this.centerPanel = new JPanel(null);
        int yOffset = 60;
        
        this.counterLab = new JLabel();
        this.counterLab.setText(String.valueOf(this.wordCounter));
        this.counterLab.setFont(new Font("Dialog", 1, 18));
        this.counterLab.setBounds(this.frameWidth/2 - 50, 30 + yOffset, 50, 40);
        this.counterLab.setHorizontalAlignment(SwingConstants.RIGHT);
        this.centerPanel.add(this.counterLab);
        
        this.sizeLab = new JLabel("/" + String.valueOf(this.wordsData.size()));
        //this.sizeLab = new JLabel("/100");
        this.sizeLab.setFont(new Font("Dialog", 1, 18));
        this.sizeLab.setBounds(this.frameWidth/2, 30 + yOffset, 50, 40);
        this.centerPanel.add(this.sizeLab);
        
        this.instructLab = new JLabel("Translate from " + this.srcLanguage + 
                        " to " + this.targetLanguage + " word/phrase:");
        this.instructLab.setFont(new Font("Dialog", 1, 16));
        this.instructLab.setBounds(0, 70 + yOffset, this.frameWidth, 30);
        this.instructLab.setHorizontalAlignment(SwingConstants.CENTER);
        this.centerPanel.add(this.instructLab);
        
        this.srcWordLab = new JLabel();
        //this.srcWordLab = new JLabel("Testowanko to wlasnie ja");
        this.srcWordLab.setFont(new Font("Dialog", 1, 16));
        this.srcWordLab.setBounds(0, 100 + yOffset, this.frameWidth, 30);
        this.srcWordLab.setHorizontalAlignment(SwingConstants.CENTER);
        this.centerPanel.add(this.srcWordLab);
        
        this.targetWordField = new JTextField();
        this.targetWordField.setFont(new Font("Dialog", 1, 14));
        this.targetWordField.setBounds(this.frameWidth/2 - 200, 140 + yOffset, 400, 30);    
        this.targetWordField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                wordChecking(e);
            }
        });
        this.centerPanel.add(this.targetWordField);
        
        this.checkButt = new JButton("Check");
        this.checkButt.setFont(new Font("Dialog", 1, 14));
        this.checkButt.setBounds(this.frameWidth/2 - 150, 180 + yOffset, 100, 30);
        this.checkButt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                wordChecking(e);
            }
        });
        this.centerPanel.add(this.checkButt);
        
        this.enterLab = new JLabel("[enter]");
        this.enterLab.setFont(new Font("Dialog", 1, 12));
        this.enterLab.setBounds(this.frameWidth/2 - 125, 210 + yOffset, 100, 20);
        this.centerPanel.add(this.enterLab);
        
        // TODO Add control shortcut - next feature
        this.nextButt = new JButton("Next");
        this.nextButt.setFont(new Font("Dialog", 1, 14));
        this.nextButt.setBounds(this.frameWidth/2 + 50, 180 + yOffset, 100, 30);
        this.nextButt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                nextWord(e);
            }
        });
//        Action nextWordAction = new AbstractAction() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                nextWord(e);
//            }
//        };
//        
//        this.nextButt.getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW).put(
//            KeyStroke.getKeyStroke(KeyEvent.CTRL_DOWN_MASK, KeyEvent.VK_D), "nextWord");
//        this.nextButt.getActionMap().put("nextWord", nextWordAction);

        this.centerPanel.add(this.nextButt);
        
        // TODO - next feature
//        this.rightAltLab = new JLabel("[right ctrl]");
//        this.rightAltLab.setFont(new Font("Dialog", 1, 12));
//        this.rightAltLab.setBounds(this.frameWidth/2 + 65, 210 + yOffset, 100, 20);
//        this.centerPanel.add(this.rightAltLab);
        
        this.resultLab = new JLabel();
        //this.resultLab = new JLabel("WRONG");
        this.resultLab.setFont(new Font("Dialog", 1, 18));
        this.resultLab.setForeground(Color.red);
        this.resultLab.setBounds(0, 240 + yOffset, this.frameWidth, 30);
        this.resultLab.setHorizontalAlignment(SwingConstants.CENTER);
        this.resultLab.setVisible(false);
        this.centerPanel.add(this.resultLab);
        
        this.explanationLab = new JLabel();
        //this.explanationLab = new JLabel("The right answer is: The testing is just me");
        this.explanationLab.setFont(new Font("Dialog", 1, 16));
        this.explanationLab.setForeground(Color.black);
        this.explanationLab.setBounds(0, 270 + yOffset, this.frameWidth, 30);
        this.explanationLab.setHorizontalAlignment(SwingConstants.CENTER);
        this.explanationLab.setVisible(false);
        this.centerPanel.add(this.explanationLab);
        // Center panel end
        
        // Bottom panel
        this.lowerPanel = new JPanel(null);
        this.lowerPanel.setPreferredSize(new Dimension(this.frameWidth, 30));
        
        this.cancelButt = new JButton("Cancel");
        this.cancelButt.setFont(new Font("Dialog", 1, 14));
        this.cancelButt.setBounds(this.frameWidth/2 - 75, 0, 150, 30);
        this.cancelButt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cancelButtActionPerformed(e);
            }
        });
        this.lowerPanel.add(this.cancelButt);
        // Bottom panel end
    
        // Layout
        this.setLayout(new BorderLayout());
        this.add(this.upperPanel, BorderLayout.PAGE_START);
        this.add(this.centerPanel, BorderLayout.CENTER);
        this.add(this.lowerPanel, BorderLayout.PAGE_END);
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Interactive dictionary");
        setResizable(false);
        setSize(frameWidth, frameHeight);
    
    }
    
    private void getDataFromDatabase() {
    
        this.database = new DataBase();
        
        String sqlCommand = String.format("SELECT * FROM setup WHERE category = \'%s\'"
                + " AND setName = \'%s\'", this.categoryName, this.setName);
        this.setupData = this.database.getData(sqlCommand);
        
        sqlCommand = String.format("SELECT * FROM words WHERE category = \'%s\'"
                + " AND setName = \'%s\'", this.categoryName, this.setName);
        this.wordsData = this.database.getData(sqlCommand);
    }
    
    private void assignLanguages() {
    
        this.srcLanguage = (String)this.setupData.get(0).get("srcLanguage");
        this.targetLanguage = (String)this.setupData.get(0).get("targetLanguage");
    }
    
    private void createShuffledList() {
    
        int len = this.wordsData.size();
        
        for (int i = 0; i < len; i++) {
            this.shuffledIdxs.add(i);
        }
        
        Collections.shuffle(this.shuffledIdxs);
    }
    
    private void performExam() {
    
        // Getting idx from shuffled list
        int idx = this.shuffledIdxs.get(this.wordCounter - 1);
        String srcWord = (String)this.wordsData.get(idx).get("srcWord");
        this.currRightAnswer = (String)this.wordsData.get(idx).get("targetWord");
        
        this.targetWordField.setEditable(true);
        
        this.counterLab.setText(String.valueOf(this.wordCounter));
        this.srcWordLab.setText(srcWord);
        this.wordCounter++;
    }
    
    private void wordChecking(ActionEvent evt) {
    
        String answer = this.targetWordField.getText();
        this.targetWordField.setEditable(false);
    
        if (answer.equals(this.currRightAnswer)) {
            this.resultLab.setText("OK");
            this.resultLab.setForeground(Color.GREEN);
            this.resultLab.setVisible(true);
            this.corrAnsNum++;
        }
        else {
            this.resultLab.setText("WRONG");
            this.resultLab.setForeground(Color.RED);
            this.resultLab.setVisible(true);
            
            this.explanationLab.setText("The right answer is: " + this.currRightAnswer);
            this.explanationLab.setVisible(true);
        }
    }
    
    private void nextWord(ActionEvent evt) {
    
        this.targetWordField.setText("");
        this.resultLab.setVisible(false);
        this.explanationLab.setVisible(false);
        
        if (this.wordCounter <= (this.wordsData.size())) {
            performExam();
        }
        else {
            int currResult = (int)(((float)this.corrAnsNum / (float)
                               this.wordsData.size()) * 100.0);
            this.updateResults(currResult);
            JOptionPane.showMessageDialog(this, "Your score is: " + currResult + " %");
            this.showWordsSetsScreen();
        }
    
    }
    
    private void updateResults(int currResult) {
    
        int bestResult = (int)this.setupData.get(0).get("bestResult");
    
        String sqlCommand = String.format("UPDATE setup SET lastResult = %d WHERE "
                + " category = \'%s\' AND setName = \'%s\'", currResult, 
                this.categoryName, this.setName);
        this.database.updateRecords(sqlCommand);
        
        if (currResult > bestResult) {
            sqlCommand = String.format("UPDATE setup SET bestResult = %d " + 
                    "WHERE category = \'%s\' AND setName = \'%s\'", currResult, 
                    this.categoryName, this.setName);
            this.database.updateRecords(sqlCommand);
        }
    }
    
    private void showWordsSetsScreen() {
    
        WordsSets wordsSetsSc = new WordsSets(this.categoryName);
        wordsSetsSc.setLocationRelativeTo(this);
        this.dispose();
        wordsSetsSc.setVisible(true);
    }
    
    private void cancelButtActionPerformed(ActionEvent evt) {
    
        String message = "Are you sure you want to cancel the exam?";
        
        int dialogResult = JOptionPane.showConfirmDialog(this, 
                message, "Warning", JOptionPane.YES_NO_OPTION, 
                JOptionPane.WARNING_MESSAGE);
        
        if(dialogResult == JOptionPane.YES_OPTION){
            
            this.showWordsSetsScreen();
        }
    
    }
    
    public static void main(String[] args) {
        
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {   
                Exam examSc = new Exam("Test3", "Var_1");
                examSc.setLocationRelativeTo(null);
                examSc.setVisible(true);
            }
        });
    }
    
    private final String categoryName, setName;
    private String srcLanguage, targetLanguage;
    private String currRightAnswer;
    private int wordCounter = 1;
    private int corrAnsNum = 0;
    private java.util.List<Integer> shuffledIdxs = new ArrayList<>();
    
    private DataBase database;
    private java.util.List<Map<String, Object>> wordsData, setupData;
    
    private JPanel upperPanel, centerPanel, lowerPanel;
    private JLabel mainHeaderLab, counterLab, sizeLab, instructLab, srcWordLab, 
                   enterLab, rightAltLab, resultLab, explanationLab;
    private JTextField targetWordField;
    private JButton checkButt, nextButt, cancelButt;
    
    private final int frameWidth = 770;
    private final int frameHeight = 550;
    
}
