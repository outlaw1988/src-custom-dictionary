/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dictionary;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;

/**
 *
 * @author jakub
 */
public class WordsSetDefEdit extends JFrame {
    
    // New set mode
    public WordsSetDefEdit(String srcLanguage, String targetLanguage) {
    
        this.srcLanguage = srcLanguage;
        this.targetLanguage = targetLanguage;
        this.isEditMode = false;
        initComponents();
        
    }
    
    // Edit mode
    public WordsSetDefEdit(String setName, String srcLanguage, String targetLanguage) {
    
        this.setName = setName;
        this.srcLanguage = srcLanguage;
        this.targetLanguage = targetLanguage;
        this.isEditMode = true;
        initComponents();
    }
    
    private void initComponents() {
    
        // TODO make global config with these parameters
        this.frameWidth = 770;
        this.frameHeight = 550;
        
        // Upper panel
        this.upperPanel = new JPanel(null);
        this.upperPanel.setPreferredSize(new Dimension(770, 110));
        
        this.enterWordsForSetLab = new JLabel();

        if (isEditMode) {
            this.enterWordsForSetLab.setText("Enter words for set: " + 
                                             this.setName);
        }
        else {
            this.enterWordsForSetLab.setText("Enter words for new set");
        }
        
        this.enterWordsForSetLab.setFont(new Font("Dialog", 1, 18));
        this.enterWordsForSetLab.setBounds(0, 0, frameWidth, 30);
        this.enterWordsForSetLab.setHorizontalAlignment(SwingConstants.CENTER);
        this.upperPanel.add(this.enterWordsForSetLab);
        
        this.setNameLab = new JLabel("Set name:");
        this.setNameLab.setFont(new Font("Dialog", 1, 14));
        this.setNameLab.setBounds(frameWidth/2 - 150, 40, 150, 30);
        this.setNameLab.setHorizontalAlignment(SwingConstants.RIGHT);
        this.upperPanel.add(this.setNameLab);
        
        this.setNameField = new JTextField();
        this.setNameField.setFont(new Font("Dialog", 1, 14));
        this.setNameField.setBounds(frameWidth/2 + 10, 40, 150, 30);
        this.upperPanel.add(this.setNameField);
        
        this.targetLanLab = new JLabel("Target language:");
        this.targetLanLab.setFont(new Font("Dialog", 1, 14));
        this.targetLanLab.setBounds(frameWidth/2 - 150, 80, 150, 30);
        this.targetLanLab.setHorizontalAlignment(SwingConstants.RIGHT);
        this.upperPanel.add(this.targetLanLab);
        
        // Choosing target language
        String[] optionsTargetLan = {this.targetLanguage, this.srcLanguage};
        this.targetLanCombo = new JComboBox(optionsTargetLan);
        this.targetLanCombo.setFont(new Font("Dialog", 1, 14));
        this.targetLanCombo.setBounds(frameWidth/2 + 10, 80, 150, 30);
        this.targetLanCombo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateLanguages();
                updateHeaderCenterPanel();
                updateFieldsCentralPanelLanguages();
            }
        });
        this.upperPanel.add(this.targetLanCombo);
        
        this.targetLanLocationLab = new JLabel("Position:");
        this.targetLanLocationLab.setFont(new Font("Dialog", 1, 14));
        this.targetLanLocationLab.setBounds(frameWidth/2 + 100, 80, 150, 30);
        this.targetLanLocationLab.setHorizontalAlignment(SwingConstants.RIGHT);
        this.upperPanel.add(this.targetLanLocationLab);
        
        // Choosing target side
        String[] optionsSide = {"left", "right"};
        this.targetLanLocationCombo = new JComboBox(optionsSide);
        this.targetLanLocationCombo.setFont(new Font("Dialog", 1, 14));
        this.targetLanLocationCombo.setBounds(frameWidth/2 + 260, 80, 100, 30);
        this.targetLanLocationCombo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                determSides();
                updateHeaderCenterPanel();
                updateFieldsCenterPanelSwapSides();
            }
        });
        
        this.determSides();
        this.upperPanel.add(this.targetLanLocationCombo);
        // Upper panel end
        
        // Center panel
        this.centerPanel = new JPanel(null);
        this.headerCenterPanel = new JPanel(null);
        
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
        
        this.cancelButt = new JButton("Cancel");
        this.cancelButt.setFont(new Font("Dialog", 1, 14));
        this.cancelButt.setBounds(10, 0, 100, 30);
        this.cancelButt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cancelButtActionPerformed(e);
            }
        });
        this.lowerPanel.add(this.cancelButt);
        
        this.addWordButt = new JButton("Add word");
        this.addWordButt.setFont(new Font("Dialog", 1, 14));
        this.addWordButt.setBounds(frameWidth/2 - 75, 0, 150, 30);
        this.addWordButt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addWordButtActionPerformed(e);
            }
        });
        this.lowerPanel.add(this.addWordButt);
        
        this.saveButt = new JButton("Save");
        this.saveButt.setFont(new Font("Dialog", 1, 14));
        this.saveButt.setBounds(frameWidth/2 + 100, 0, 80, 30);
        this.saveButt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                savedButtActionPerformed(e);
            }
        });
        this.lowerPanel.add(this.saveButt);
        
        this.confirmButt = new JButton("Confirm");
        this.confirmButt.setFont(new Font("Dialog", 1, 14));
        this.confirmButt.setBounds(frameWidth/2 + 270, 0, 100, 30);
        this.confirmButt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                confirmButtActionPerformed(e);
            }
        });
        this.lowerPanel.add(this.confirmButt);
        
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
    
    private void determSides() {
    
        this.targetSide = this.targetLanLocationCombo.getSelectedItem().toString();
        if (this.targetSide.equals("left")) this.srcSide = "right";
        else this.srcSide = "left";
    
    }
    
    private void updateLanguages() {
    
        String temp = this.targetLanguage;
        this.targetLanguage = this.targetLanCombo.getSelectedItem().toString();
        if (this.targetLanguage.equals(this.srcLanguage)) {
            this.srcLanguage = temp;
        }
    }
    
    private void initCenterPanel() {
    
        this.leftLanTypeLab = new JLabel();
        this.leftLanLab = new JLabel();
        
        this.leftLanTypeLab.setFont(new Font("Dialog", 1, 14));
        this.leftLanTypeLab.setBounds(this.frameWidth/2 - 250, 10, 150, 30);
        this.leftLanTypeLab.setHorizontalAlignment(SwingConstants.RIGHT);
        this.headerCenterPanel.add(this.leftLanTypeLab);
        
        this.leftLanLab.setFont(new Font("Dialog", 1, 14));
        this.leftLanLab.setBounds(this.frameWidth/2 - 300, 30, 150, 30);
        this.leftLanLab.setHorizontalAlignment(SwingConstants.RIGHT);
        this.headerCenterPanel.add(this.leftLanLab);
        
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
        this.headerCenterPanel.add(this.rightLanTypeLab);
        
        this.rightLanLab.setFont(new Font("Dialog", 1, 14));
        this.rightLanLab.setBounds(this.frameWidth/2 + 150, 30, 150, 30);
        this.rightLanLab.setHorizontalAlignment(SwingConstants.LEFT);
        this.headerCenterPanel.add(this.rightLanLab);
        
        this.headerCenterPanel.setBounds(0, 0, 770, 60);
        this.centerPanel.add(this.headerCenterPanel);
        
        this.initFieldsCenterPanel();
    }
    
    private void updateHeaderCenterPanel() {
    
        System.out.println("Update header...");
        
        if (this.targetSide.equals("left")){
            this.leftLanTypeLab.setText("Target language:");
            this.rightLanTypeLab.setText("Source language:");
            this.leftLanLab.setText(this.targetLanguage);
            this.rightLanLab.setText(this.srcLanguage);
        }
        else if (this.targetSide.equals("right")) {
            this.leftLanTypeLab.setText("Source Language:");
            this.rightLanTypeLab.setText("Target Language:");
            this.leftLanLab.setText(this.srcLanguage);
            this.rightLanLab.setText(this.targetLanguage);
        }
    
    }
    
    private void initFieldsCenterPanel() {

        // TODO Check if other layout is possible to not stretch cells or move 
        // to the middle
        this.wordsEditPanel = new JPanel(null);
        
        int initRepetition = 8; // def 8
        this.position = 60;
        
        for (int i = 0; i < initRepetition; i++) {
            
            TextField srcWord = new TextField(srcLanguage, currRowIdx, this.srcSide);
            TextField targetWord = new TextField(targetLanguage, currRowIdx, 
                                                 this.targetSide);

            srcWord.setFont(new Font("Dialog", 1, 12));
            
            //word1.setText("word1");
            srcWord.setHorizontalAlignment(SwingConstants.LEFT);
            srcWord.addFocusListener(new FocusAdapter() {
                @Override
                public void focusLost(FocusEvent evt) {
                    textFieldAction(evt);
                }
            });

            targetWord.setFont(new Font("Dialog", 1, 12));
            
            if (this.targetSide.equals("left")){
                targetWord.setBounds(50, this.position, 300, 28);
                srcWord.setBounds(410, this.position, 300, 28);
            } 
            else if (this.targetSide.equals("right")) {
                srcWord.setBounds(50, this.position, 300, 28);
                targetWord.setBounds(410, this.position, 300, 28);
            }
            
            //word2.setText("word2");
            targetWord.setHorizontalAlignment(SwingConstants.LEFT);
            targetWord.addFocusListener(new FocusAdapter() {
                @Override
                public void focusLost(FocusEvent evt) {
                    textFieldAction(evt);
                }
            });

            this.srcWordFields.add(srcWord);
            this.targetWordFields.add(targetWord);
            
            //System.out.println(this.position);
            
            this.position += this.wordsGap;
            this.currRowIdx += 1;

            this.wordsEditPanel.add(srcWord);
            this.wordsEditPanel.add(targetWord);
        
        }
        
        // TODO Change preferred size if new field has been added
//        this.wordsEditPanel.setPreferredSize(new Dimension(770, 3000));
//        this.wordsEditPanel.setPreferredSize(new Dimension(770, 1000));
        this.wordsEditPanel.setBounds(0, 0, 770, 400);
        this.centerPanel.add(this.wordsEditPanel);
    }
    
    private void updateFieldsCenterPanelSwapSides() {
        
        this.position = 60;
        boolean isTargetOnLeft = this.targetSide.equals("left");
        
        for (int i = 0; i < srcWordFields.size(); i++) {
        
            if (isTargetOnLeft) {
                
                TextField targetWord = this.targetWordFields.get(i);
                targetWord.setBounds(50, this.position, 300, 28);
                
                TextField srcWord = this.srcWordFields.get(i);
                srcWord.setBounds(410, this.position, 300, 28);
            }
            else {
            
                TextField srcWord = this.srcWordFields.get(i);
                srcWord.setBounds(50, this.position, 300, 28);
                
                TextField targetWord = this.targetWordFields.get(i);
                targetWord.setBounds(410, this.position, 300, 28);
            
            }
            
            this.position += this.wordsGap;
        }
        
    }
    
    private void updateFieldsCentralPanelLanguages() {
        
         for (int i = 0; i < srcWordFields.size(); i++) {

            TextField targetWord = this.targetWordFields.get(i);
            String prevTargetWord = targetWord.getText();

            TextField srcWord = this.srcWordFields.get(i);
            String prevSrcWord = srcWord.getText();
            
            // Swapping languages after languages update
            targetWord.setText(prevSrcWord);
            targetWord.language = this.targetLanguage;
            
            srcWord.setText(prevTargetWord);
            srcWord.language = this.srcLanguage;
            
        }
    
    }
    
    private void textFieldAction(FocusEvent evt) {
    
        TextField txtField = (TextField) evt.getSource();
        System.out.println(txtField.getText());
    
    }

    private void cancelButtActionPerformed(ActionEvent evt) {
    
    
    
    }
    
    private void addWordButtActionPerformed(ActionEvent evt) {
    
    
    
    }
    
    private void savedButtActionPerformed(ActionEvent evt) {
    
    
    
    }
    
    private void confirmButtActionPerformed(ActionEvent evt) {
    
    
    
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
                
                WordsSetDefEdit wordsSetDefEditScreen = new WordsSetDefEdit("Polish", 
                                                        "English");
                wordsSetDefEditScreen.setLocationRelativeTo(null);
                wordsSetDefEditScreen.setVisible(true);
            }
        });
    }
    
    private class TextField extends JTextField {
    
        TextField(String language, int row, String side) {
        
            super();
            this.language = language;
            this.row = row;
            this.currSide = side;
            
        }
        
        protected String language;
        protected int row;
        protected String currSide;
    
    }
    
    private String setName;
    private boolean isEditMode;
    private String srcLanguage, targetLanguage;
    private int currRowIdx = 0;
    private int position;
    private final int wordsGap = 40;
    private String targetSide;
    private String srcSide;
    
    private ArrayList<TextField> srcWordFields = new ArrayList<>();
    private ArrayList<TextField> targetWordFields = new ArrayList<>();
//    private ArrayList<String> words1 = new ArrayList<>();
//    private ArrayList<String> words2 = new ArrayList<>();
    
    private JPanel upperPanel, centerPanel, lowerPanel, headerCenterPanel, 
            wordsEditPanel;
    private JScrollPane centerScrollPanel;
    
    private JLabel enterWordsForSetLab, setNameLab, targetLanLab, targetLanLocationLab, 
            leftLanTypeLab, rightLanTypeLab, leftLanLab, rightLanLab;
    private JTextField setNameField;
    private JComboBox targetLanCombo, targetLanLocationCombo;
    private JButton cancelButt, addWordButt, saveButt, confirmButt;
    
    private int frameWidth, frameHeight;
    
}
