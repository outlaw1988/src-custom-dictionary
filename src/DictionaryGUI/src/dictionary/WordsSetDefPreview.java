/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dictionary;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import javax.swing.*;

/**
 *
 * @author jakub
 */
public class WordsSetDefPreview extends JFrame {
    
    public WordsSetDefPreview(String categoryName, String setName) {
    
        this.categoryName = categoryName;
        this.setName = setName;
        getDataFromDatabase();
        assignData();
        initComponents();
    }
    
    private void initComponents() {
    
        // Upper panel
        this.upperPanel = new JPanel(null);
        this.upperPanel.setPreferredSize(new Dimension(770, 30));
        
        // Main header label
        this.mainHeaderLab = new JLabel("Category: " + this.categoryName + ", set: "
                                        + this.setName);
        this.mainHeaderLab.setFont(new Font("Dialog", 1, 18));
        this.mainHeaderLab.setBounds(0, 0, this.frameWidth, 30);
        this.mainHeaderLab.setHorizontalAlignment(SwingConstants.CENTER);
        this.upperPanel.add(this.mainHeaderLab);
        // Upper panel end
        
        // Center panel
        this.initCenterPanel();
        
        this.centerScrollPanel = new JScrollPane(this.centerPanel, 
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        // increase speed of scrolling
        this.centerScrollPanel.getVerticalScrollBar().setUnitIncrement(16);
        // Center panel end
        
        // Bottom panel
        this.lowerPanel = new JPanel(null);
        this.lowerPanel.setPreferredSize(new Dimension(770, 30));
        
        this.goBackButt = new JButton("Go back");
        this.goBackButt.setFont(new Font("Dialog", 1, 14));
        this.goBackButt.setBounds(10, 0, 100, 30);
        this.goBackButt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                goBackButtActionPerformed(e);
            }
        });
        this.lowerPanel.add(this.goBackButt);
        
        this.editButt = new JButton("Edit");
        this.editButt.setFont(new Font("Dialog", 1, 14));
        this.editButt.setBounds(frameWidth/2 - 50, 0, 100, 30);
        this.editButt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editButtActionPerformed(e);
            }
        });
        this.lowerPanel.add(this.editButt);
        
        this.perfExamButt = new JButton("Perform exam");
        this.perfExamButt.setFont(new Font("Dialog", 1, 14));
        this.perfExamButt.setBounds(frameWidth/2 + 220, 0, 150, 30);
        this.perfExamButt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                perfExamButtActionPerformed(e);
            }
        });
        this.lowerPanel.add(this.perfExamButt);
        // Bottom panel end
        
        // Layout
        this.setLayout(new BorderLayout());
        this.add(this.upperPanel, BorderLayout.PAGE_START);
        this.add(this.centerScrollPanel, BorderLayout.CENTER);
        this.add(this.lowerPanel, BorderLayout.PAGE_END);
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Interactive dictionary");
        setResizable(false);
        setSize(frameWidth, frameHeight);
    }
    
    private void initCenterPanel() {
    
        this.centerPanel = new JPanel(null);
        this.centerPanel.setPreferredSize(new Dimension(this.frameWidth, 400));
        
        this.leftLanTypeLab = new JLabel();
        this.leftLanLab = new JLabel();
        
        this.leftLanTypeLab.setFont(new Font("Dialog", 1, 14));
        this.leftLanTypeLab.setBounds(this.frameWidth/2 - 250, 10, 150, 30);
        this.leftLanTypeLab.setHorizontalAlignment(SwingConstants.RIGHT);
        this.centerPanel.add(this.leftLanTypeLab);
        
        this.leftLanLab.setFont(new Font("Dialog", 1, 14));
        this.leftLanLab.setBounds(this.frameWidth/2 - 300, 30, 150, 30);
        this.leftLanLab.setHorizontalAlignment(SwingConstants.RIGHT);
        this.centerPanel.add(this.leftLanLab);
        
        this.rightLanTypeLab = new JLabel();
        this.rightLanLab = new JLabel();
        
        if (this.targetSide.equals("left")){
            this.leftLanTypeLab.setText("Target language:");
            this.leftLanLab.setText(this.targetLanguage);
            this.rightLanTypeLab.setText("Source language:");
            this.rightLanLab.setText(this.srcLanguage);
        }
        else {
            this.leftLanTypeLab.setText("Source Language:");
            this.leftLanLab.setText(this.srcLanguage);
            this.rightLanTypeLab.setText("Target Language:");
            this.rightLanLab.setText(this.targetLanguage);
        }
        
        this.rightLanTypeLab.setFont(new Font("Dialog", 1, 14));
        this.rightLanTypeLab.setBounds(this.frameWidth/2 + 100, 10, 150, 30);
        this.rightLanTypeLab.setHorizontalAlignment(SwingConstants.LEFT);
        this.centerPanel.add(this.rightLanTypeLab);
        
        this.rightLanLab.setFont(new Font("Dialog", 1, 14));
        this.rightLanLab.setBounds(this.frameWidth/2 + 150, 30, 150, 30);
        this.rightLanLab.setHorizontalAlignment(SwingConstants.LEFT);
        this.centerPanel.add(this.rightLanLab);
        
        this.centerPanel.setBounds(0, 0, 770, 500);
        //this.centerPanel.add(this.headerCenterPanel);
        this.initWords();
    }
    
    private void initWords() {
    
        String[] columnNames = {"word1", "word2"};
        Object[][] words = new Object[this.wordsData.size()][2];
        
        for (int i = 0; i < this.wordsData.size(); i++) {
        
            String srcWord = (String)this.wordsData.get(i).get("srcWord");
            String targetWord = (String)this.wordsData.get(i).get("targetWord");
            
            if (this.targetSide.equals("left")) {
                words[i][0] = targetWord;
                words[i][1] = srcWord;
            }
            else {
                words[i][0] = srcWord;
                words[i][1] = targetWord;
            }
            
            this.centerPanHeight += 30;
        }
        
        this.wordsTable = new JTable(words, columnNames);
        //DefaultTableModel tableModel = new DefaultTableModel();
        //this.wordsTable.setModel(tableModel);
        this.wordsTable.setRowHeight(30);
        this.wordsTable.setFont(new Font("Dialog", 1, 12));
        this.wordsTable.setBounds(30, 60, 700, this.centerPanHeight);
        this.centerPanel.setPreferredSize(new Dimension(this.frameWidth, 
                                          this.centerPanHeight + 60));
        this.centerPanel.add(this.wordsTable);
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
    
    private void assignData() {
    
        this.srcLanguage = (String)this.setupData.get(0).get("srcLanguage");
        this.targetLanguage = (String)this.setupData.get(0).get("targetLanguage");
        this.targetSide = (String)this.setupData.get(0).get("targetSide");
    }
    
    private void editButtActionPerformed(ActionEvent evt) {
    
        WordsSetDefEdit editSc = new WordsSetDefEdit(this.categoryName, this.setName);
        editSc.setLocationRelativeTo(this);
        this.dispose();
        editSc.setVisible(true);
    }
    
    private void goBackButtActionPerformed(ActionEvent evt) {
    
        WordsSets wordSetsScreen = new WordsSets(this.categoryName);
        wordSetsScreen.setLocationRelativeTo(this);
        this.dispose();
        wordSetsScreen.setVisible(true);
    }
    
    private void perfExamButtActionPerformed(ActionEvent evt) {
    
        Exam examSc = new Exam(this.categoryName, this.setName);
        examSc.setLocationRelativeTo(this);
        this.dispose();
        examSc.setVisible(true);
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
                
                WordsSetDefPreview previewScreen = new WordsSetDefPreview("Test3", 
                                                   "Var_1");
                previewScreen.setLocationRelativeTo(null);
                previewScreen.setVisible(true);
            }
        });
    }
    
    private String categoryName, setName, srcLanguage, targetLanguage, targetSide;
    
    private JPanel upperPanel, centerPanel, lowerPanel;
    private JScrollPane centerScrollPanel;
    private JLabel mainHeaderLab;
    private JLabel leftLanTypeLab, rightLanTypeLab, leftLanLab, rightLanLab;
    private JButton goBackButt, editButt, perfExamButt;
    private JTable wordsTable;
    
    private java.util.List<Map<String, Object>> setupData;
    private java.util.List<Map<String, Object>> wordsData; 
    private DataBase database;
    
    private int frameWidth = 770;
    private int frameHeight = 550;
    private int centerPanHeight = 0;
    
}
