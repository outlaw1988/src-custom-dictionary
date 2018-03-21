/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dictionary;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;

/**
 *
 * @author jakub
 */
public class WordsSetDefEdit extends JFrame {
    
    // New set mode
    public WordsSetDefEdit(String categoryName, 
                        java.util.List<Map<String, String>> defaultSettings) {
    
        this.defaultSettings = defaultSettings;
        this.isEditMode = false;
        this.categoryName = categoryName;
        initComponents();
        
    }
    
    // Edit mode
    public WordsSetDefEdit(String categoryName, String setName) {
    
        this.categoryName = categoryName;
        this.setName = setName;
        this.isEditMode = true;
        this.getDataFromDatabase();
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
            this.enterWordsForSetLab.setText("Enter words for new set - category " 
                                             + this.categoryName);
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
        if (this.isEditMode) this.setNameField.setText(this.setName);
        this.upperPanel.add(this.setNameField);
        
        this.targetLanLab = new JLabel("Target language:");
        this.targetLanLab.setFont(new Font("Dialog", 1, 14));
        this.targetLanLab.setBounds(frameWidth/2 - 150, 80, 150, 30);
        this.targetLanLab.setHorizontalAlignment(SwingConstants.RIGHT);
        this.upperPanel.add(this.targetLanLab);
        
        if (!this.isEditMode) {
            this.srcLanguage = this.defaultSettings.get(0).get("defSrcLanguage");
            this.targetLanguage = this.defaultSettings.get(0)
                                       .get("defTargetLanguage");
        }
        else {
            this.srcLanguage = (String)this.setupData.get(0).get("srcLanguage");
            this.targetLanguage = (String)this.setupData.get(0).get("targetLanguage");
        }
        
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
        
        if (!this.isEditMode) {
            this.targetSide = this.defaultSettings.get(0).get("defTargetSide");
            if (this.targetSide.equals("left")) this.srcSide = "right";
            else this.srcSide = "left";
        }
        else {
            this.targetSide = (String)this.setupData.get(0).get("targetSide");;
            if (this.targetSide.equals("left")) this.srcSide = "right";
            else this.srcSide = "left";
        }
        
        String[] optionsSide = {this.targetSide, this.srcSide};
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
        this.centerPanel.setPreferredSize(new Dimension(this.frameWidth, 
                                                        this.centerPanHeight));
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
        
        // TODO - next feature
//        this.saveButt = new JButton("Save");
//        this.saveButt.setFont(new Font("Dialog", 1, 14));
//        this.saveButt.setBounds(frameWidth/2 + 100, 0, 80, 30);
//        this.saveButt.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                saveButtActionPerformed(e);
//            }
//        });
//        this.lowerPanel.add(this.saveButt);
        
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
    
    private void getDataFromDatabase() {
    
        this.database = new DataBase();
        String sqlCommand = String.format("SELECT * FROM setup WHERE category = \'%s\'"
                + " AND setName = \'%s\'", this.categoryName, this.setName);
        this.setupData = this.database.getData(sqlCommand);
        
        sqlCommand = String.format("SELECT * FROM words WHERE category = \'%s\'"
                + " AND setName = \'%s\'", this.categoryName, this.setName);
        this.wordsData = this.database.getData(sqlCommand);
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
        
        // Edit mode - fill with words
        if (this.isEditMode) this.makeFieldsAndFill();
    }
    
    private void updateHeaderCenterPanel() {
        
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
        
        this.position = 60;
        
        for (int i = 0; i < this.initFieldsNum; i++) {
            
            this.addNewWord();
        }
        
        this.wordsEditPanel.setBounds(0, 0, 770, this.centerPanHeight);
        this.centerPanel.add(this.wordsEditPanel);
    }
    
    private void makeFieldsAndFill() {
    
        int fieldsToDraw = this.wordsData.size() - this.initFieldsNum;

        for (int i = 0; i < fieldsToDraw; i++) {
            
            this.addNewWord();
        }

        for (int i = 0; i < this.wordsData.size(); i++) {
        
            TextField srcWord = this.srcWordFields.get(i);
            srcWord.setText((String)this.wordsData.get(i).get("srcWord"));
            
            TextField targetWord = this.targetWordFields.get(i);
            targetWord.setText((String)this.wordsData.get(i).get("targetWord"));
        }
    }
    
    private void addNewWord() {
    
        TextField srcWord = new TextField(srcLanguage, currRowIdx, this.srcSide);
        TextField targetWord = new TextField(targetLanguage, currRowIdx, 
                                             this.targetSide);

        srcWord.setFont(new Font("Dialog", 1, 12));
        srcWord.setHorizontalAlignment(SwingConstants.LEFT);

        targetWord.setFont(new Font("Dialog", 1, 12));
        targetWord.setHorizontalAlignment(SwingConstants.LEFT);

        if (this.targetSide.equals("left")){
            targetWord.setBounds(50, this.position, 300, 28);
            srcWord.setBounds(410, this.position, 300, 28);
        } 
        else if (this.targetSide.equals("right")) {
            srcWord.setBounds(50, this.position, 300, 28);
            targetWord.setBounds(410, this.position, 300, 28);
        }
        
        this.srcWordFields.add(srcWord);
        this.targetWordFields.add(targetWord);

        this.position += this.wordsGap;
        this.currRowIdx += 1;

        this.wordsEditPanel.add(srcWord);
        this.wordsEditPanel.add(targetWord);
        
        if (this.currRowIdx > 8) expandAndReDrawCenterPanel();
    }
    
    private void expandAndReDrawCenterPanel() {
    
        this.centerPanHeight += 40;
        this.wordsEditPanel.setBounds(0, 0, 770, this.centerPanHeight);
        this.centerPanel.setPreferredSize(new Dimension(this.frameWidth, 
                                                        this.centerPanHeight));
        
        this.wordsEditPanel.revalidate();
        this.wordsEditPanel.repaint();
        this.centerPanel.revalidate();
        this.centerPanel.repaint();
    }
    
    private void updateFieldsCenterPanelSwapSides() {
        
        this.position = 60;
        
        for (int i = 0; i < srcWordFields.size(); i++) {
        
            if (this.targetSide.equals("left")) {
                
                TextField targetWord = this.targetWordFields.get(i);
                targetWord.setBounds(50, this.position, 300, 28);
                
                TextField srcWord = this.srcWordFields.get(i);
                srcWord.setBounds(410, this.position, 300, 28);
            }
            else if (this.targetSide.equals("right")) {
            
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
    
    private void insertWordsToDatabase() {
    
        for (int i = 0; i < this.srcWordFields.size(); i++) {
        
            String srcWord = this.srcWordFields.get(i).getText();
            String targetWord = this.targetWordFields.get(i).getText();
            
            if (srcWord.equals("") || srcWord.equals("")) continue; 
            
            this.database.insertToWord(this.categoryName, this.setName, 
                                       srcWord, targetWord);
        }
    
    }
    
    private void dbOperationsForEditMode() {
    
        String sqlCommand = String.format("UPDATE setup SET setName = \'%s\'"
                    + ", srcLanguage = \'%s\', targetLanguage = \'%s\'"
                    + ", targetSide = \'%s\'"
                    + " WHERE category = \'%s\' AND setName = \'%s\'", this.setName,
                    this.srcLanguage, this.targetLanguage, this.targetSide,
                    this.categoryName, this.prevSetName);
        this.database.updateRecords(sqlCommand);
        
        sqlCommand = String.format("DELETE from words WHERE category "
                    + "= \'%s\' AND setName = \'%s\'", this.categoryName, this.setName);
        this.database.removeRecords(sqlCommand);
        
        this.insertWordsToDatabase();
    }
    
    private void dbOperationsForNewSetMode() {
        
        this.database.insertToSetup(this.categoryName, this.setName, this.srcLanguage, 
                                    this.targetLanguage, this.targetSide);
        
        this.insertWordsToDatabase();
    }
    
    private void showWordsSetsScreen() {
    
        WordsSets wordSetsScreen = new WordsSets(this.categoryName);
        wordSetsScreen.setLocationRelativeTo(this);
        this.dispose();
        wordSetsScreen.setVisible(true);
    }

    private void cancelButtActionPerformed(ActionEvent evt) {
    
        String message = "Are you sure you want to cancel editing?"
                       + "\nUnsaved changes will be discarded.";
        
        int dialogResult = JOptionPane.showConfirmDialog(this, 
                message, "Warning", JOptionPane.YES_NO_OPTION, 
                JOptionPane.WARNING_MESSAGE);
        
        if(dialogResult == JOptionPane.YES_OPTION){
            
            this.showWordsSetsScreen();
        }

    }
    
    private void addWordButtActionPerformed(ActionEvent evt) {
        
        this.addNewWord();
    }
    
    // TODO Next feature
//    private void saveButtActionPerformed(ActionEvent evt) {
//    
//        
//    
//    }
    
    private void confirmButtActionPerformed(ActionEvent evt) {
    
        if (isEditMode) this.prevSetName = this.setName;
        else this.prevSetName = null;
        
        this.setName = this.setNameField.getText();
        database = new DataBase();
        
        String sqlComm = String.format("SELECT COUNT(*) AS rowcount FROM setup "
                    + "WHERE category = \'%s\' AND setName = \'%s\'", this.categoryName, 
                    this.setName);
        
        // check in edit mode - provided setName != previous setName
        if (this.database.countRecords(sqlComm) != 0 && !this.setName.
            equals(this.prevSetName)) {
            String message = "Set " + this.setName + " for category " + this.categoryName + 
                             " already exists!";
            JOptionPane.showMessageDialog(this, message, "Warning", 
                                          JOptionPane.WARNING_MESSAGE);
        }
        else if (this.setName.equals("")) {
            String message = "Set name is empty!";
            JOptionPane.showMessageDialog(this, message, "Warning", 
                                          JOptionPane.WARNING_MESSAGE);
        }
        else {
            
            if (this.isEditMode) dbOperationsForEditMode();
            else dbOperationsForNewSetMode();
        }
    
        this.showWordsSetsScreen();
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
                
                java.util.List<Map<String, String>> defaultSettings = new ArrayList<>();
                Map<String, String> row = new HashMap<>();
                row.put("defSrcLanguage", "Polish");
                row.put("defTargetLanguage", "English");
                row.put("defTargetSide", "left");
                defaultSettings.add(row);
                
                WordsSetDefEdit wordsSetDefEditScreen = new WordsSetDefEdit("Test4", 
                                                        defaultSettings);
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
    private String prevSetName;
    private final String categoryName;
    private final boolean isEditMode;
    private String srcLanguage, targetLanguage;
    private int currRowIdx = 0;
    private int position;
    private final int wordsGap = 40;
    private String targetSide;
    private String srcSide;
    private int centerPanHeight = 375;
    private final int initFieldsNum = 8;
    private DataBase database;
    
    private java.util.List<Map<String, String>> defaultSettings;
    private java.util.List<Map<String, Object>> setupData;
    private java.util.List<Map<String, Object>> wordsData;
    
    private ArrayList<TextField> srcWordFields = new ArrayList<>();
    private ArrayList<TextField> targetWordFields = new ArrayList<>();
    
    private JPanel upperPanel, centerPanel, lowerPanel, headerCenterPanel, 
            wordsEditPanel;
    private JScrollPane centerScrollPanel;
    
    private JLabel enterWordsForSetLab, setNameLab, targetLanLab, targetLanLocationLab, 
            leftLanTypeLab, rightLanTypeLab, leftLanLab, rightLanLab;
    private JTextField setNameField;
    private JComboBox targetLanCombo, targetLanLocationCombo;
    private JButton cancelButt, addWordButt, confirmButt;
    
    private int frameWidth, frameHeight;
    
}
