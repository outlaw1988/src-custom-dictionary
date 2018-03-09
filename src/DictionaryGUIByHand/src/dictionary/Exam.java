/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dictionary;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.*;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author jakub
 */
public class Exam extends JFrame {
    
    public Exam(String wordSetNameStr) {
    
        this.wordSetName = wordSetNameStr;
        initComponents();
        //readJsonFile();
        //getWordsLists();
        getWordsListsFromDatabase();
        createShuffledIndList();
        performExam();
        
    }
    
    private void initComponents() {
    
        int frameWidth = 770;
        int frameHeight = 550;
        
        // transWordLab
        this.transWordLab = new JLabel("Translate word:");
        this.transWordLab.setFont(new Font("Dialog", 1, 18));
        this.transWordLab.setHorizontalAlignment(SwingConstants.CENTER);
        this.transWordLab.setBounds(0, 150, frameWidth, 20);
        this.add(this.transWordLab);
        
        // wordToTransLab
        this.wordToTransLab = new JLabel("Pupka");
        this.wordToTransLab.setFont(new Font("Dialog", 1, 18));
        this.wordToTransLab.setHorizontalAlignment(SwingConstants.CENTER);
        this.wordToTransLab.setBounds(0, 180, frameWidth, 20);
        this.add(this.wordToTransLab);
        
        // okOrFailLab
        this.okOrFailLab = new JLabel("");
        this.okOrFailLab.setFont(new Font("Dialog", 1, 18));
        this.okOrFailLab.setHorizontalAlignment(SwingConstants.CENTER);
        this.okOrFailLab.setBounds(0, 220, frameWidth, 20);
        this.add(this.okOrFailLab);
        
        // answer
        this.answer = new JTextField();
        this.answer.setFont(new Font("Dialog", 1, 16));
        this.answer.setHorizontalAlignment(SwingConstants.CENTER);
        this.answer.setBounds(frameWidth/2 - 100, 250, 200, 30);
        this.answer.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                transTextFieldActionPerformed(evt);
            }
        });
        this.add(this.answer);
        
        // nextButt
        this.nextButt = new JButton("Next");
        this.nextButt.setFont(new Font("Dialog", 1, 14));
        this.nextButt.setHorizontalAlignment(SwingConstants.CENTER);
        this.nextButt.setBounds(frameWidth/2 - 35, 290, 70, 25);
        this.nextButt.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                nextButtActionPerformed(evt);
            }
        });
        this.add(this.nextButt);
        
        this.setLayout(null);
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Interactive dictionary");
        setResizable(false);
        setSize(frameWidth, frameHeight);
        
    }
    
    private void transTextFieldActionPerformed(ActionEvent evt) {
        this.currAnswer = this.answer.getText();
        
         if (this.currAnswer.equals(this.rightAnswer)) {
             okOrFailLab.setText("OK");
             this.corrNum += 1;
         }
         else {
             okOrFailLab.setText("FAIL");
         }
    }
    
    private void nextButtActionPerformed(ActionEvent evt) {
        this.wordToTransLab.setText("");
        this.okOrFailLab.setText("");
        this.answer.setText("");
        if (this.questionNum < (this.wordsToTranslate.size() - 1)) {
            performExam();
        }
        else {
            System.out.println("Your score is: " + this.corrNum);
            int percentCorr = (int)(((float)this.corrNum / (float)this.wordsToTranslate.size()) * 100.0);
            this.database.updateResult(percentCorr, wordSetName);
            JOptionPane.showMessageDialog(this, "Your score is: " + percentCorr + " %");
        }
    }
    
    private void readJsonFile() {
    
        JSONParser parser = new JSONParser();
        
        try {
            Object obj = parser.parse(new FileReader("wordsSet.json"));
            this.jsonObj = (JSONObject) obj;
        }
        catch(FileNotFoundException e) { e.printStackTrace(); }
        catch(IOException e) { e.printStackTrace(); }
        catch(ParseException e) { e.printStackTrace(); }
        catch(Exception e) { e.printStackTrace(); }
        
    }
    
    private void getWordsLists() {
        
        JSONObject categoryObj = (JSONObject)this.jsonObj.get(this.wordSetName);
        this.wordsToTranslate = (ArrayList<String>)categoryObj.get("words2");
        this.answers = (ArrayList<String>)categoryObj.get("words1");
        
    }
    
    private void getWordsListsFromDatabase() {
        
        this.database = new DataBase();
        
        try {
            this.database.getDataFromWords(this.wordSetName);
        }
        catch(SQLException e) { System.out.println(e.getMessage()); }
        
        this.wordsToTranslate = this.database.getWords2();
        this.answers = this.database.getWords1();
        
        System.out.println(this.wordsToTranslate);
        System.out.println(this.answers);
    }
    
    private void createShuffledIndList() {
    
        int len = this.wordsToTranslate.size();
        
        for (int i = 0; i < len; i++) {
            this.shuffledInd.add(i);
        }
        
        Collections.shuffle(this.shuffledInd);
    }
    
    private void performExam() {
        
        this.questionNum += 1;
        int currInd = shuffledInd.get(this.questionNum);
        
        String wordToTranslate = this.wordsToTranslate.get(currInd);
        this.wordToTransLab.setText(wordToTranslate);
        
        this.rightAnswer = this.answers.get(currInd);
        
    }
    
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Exam.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Exam.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Exam.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Exam.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Exam("family").setVisible(true);
            }
        });
    }
    
    private final String wordSetName;
    
    private JLabel transWordLab;
    private JLabel wordToTransLab;
    private JLabel okOrFailLab;
    private JTextField answer;
    private JButton nextButt;
    
    private JSONObject jsonObj;
    private ArrayList<String> wordsToTranslate; //= new ArrayList<String>();
    private ArrayList<String> answers; //= new ArrayList<String>();
    private List<Integer> shuffledInd = new ArrayList<>();
    private int questionNum = -1;
    private int corrNum = 0;
    private String currAnswer;
    private String rightAnswer;
    
    private DataBase database;
    
}
