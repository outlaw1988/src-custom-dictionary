/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dictionary;

import java.awt.Font;
import java.awt.event.*;
import javax.swing.*;

/**
 *
 * @author jakub
 */
public class CategoryEdit extends Main {
    
    public CategoryEdit() {
    
        this.editMode = false;
        initComponents();
        
    }
    
    public CategoryEdit(String categoryName, String srcLanguage, 
                        String targetLanguage, String targetSide) {
    
        this.editMode = true;
        this.initCategoryName = categoryName;
        this.srcLanguage = srcLanguage;
        this.targetLanguage = targetLanguage;
        this.targetSide = targetSide;
        initComponents();
        
    }
    
    private void initComponents() {
        
        // TODO make global config with these parameters
        int frameWidth = 770;
        int frameHeight = 550;
    
        this.categoryPanel.removeAll();
        this.lowerPanel.removeAll();
        
        // Central panel
        this.categoryPanel.setLayout(null);
        
        this.catNameLab = new JLabel("Category name:");
        this.catNameLab.setFont(new Font("Dialog", 1, 14));
        this.catNameLab.setBounds(frameWidth/2 - 150, 50, 150, 30);
        this.catNameLab.setHorizontalAlignment(SwingConstants.RIGHT);
        this.categoryPanel.add(this.catNameLab);
        
        this.catNameField = new JTextField();
        this.catNameField.setFont(new Font("Dialog", 1, 14));
        
        if (this.editMode) this.catNameField.setText(this.initCategoryName);
        
        this.catNameField.setBounds(frameWidth/2 + 10, 50, 150, 30);
        this.catNameField.setHorizontalAlignment(SwingConstants.LEFT);
        this.catNameField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent evt) {
                catNameFieldAction();
            }
        });
        this.categoryPanel.add(this.catNameField);
        
        this.srcLanLab = new JLabel("Default source language:");
        this.srcLanLab.setFont(new Font("Dialog", 1, 14));
        this.srcLanLab.setBounds(frameWidth/2 - 200, 90, 200, 30);
        this.srcLanLab.setHorizontalAlignment(SwingConstants.RIGHT);
        this.categoryPanel.add(this.srcLanLab);
        
        String[] sourceLanguages = {"Polish", "English", "Spanish", "German", "Italian"};
        this.srcLanBox = new JComboBox(sourceLanguages);
        this.srcLanBox.setFont(new Font("Dialog", 1, 14));

        if (this.editMode) this.srcLanBox.setSelectedItem(this.srcLanguage);
        
        this.srcLanBox.setBounds(frameWidth/2 + 10, 90, 150, 30);
        this.categoryPanel.add(this.srcLanBox);
        
        this.targetLanLab = new JLabel("Default target language:");
        this.targetLanLab.setFont(new Font("Dialog", 1, 14));
        this.targetLanLab.setBounds(frameWidth/2 - 200, 130, 200, 30);
        this.targetLanLab.setHorizontalAlignment(SwingConstants.RIGHT);
        this.categoryPanel.add(this.targetLanLab);
        
        String[] targetLanguages = {"English", "Polish", "Spanish", "German", "Italian"};
        this.targetLanBox = new JComboBox(targetLanguages);
        this.targetLanBox.setFont(new Font("Dialog", 1, 14));
        
        if (this.editMode) this.targetLanBox.setSelectedItem(this.targetLanguage);
        
        this.targetLanBox.setBounds(frameWidth/2 + 10, 130, 150, 30);
        this.categoryPanel.add(this.targetLanBox);
        
        this.defTargetSideLab = new JLabel("Default target language side:");
        this.defTargetSideLab.setFont(new Font("Dialog", 1, 14));
        this.defTargetSideLab.setBounds(frameWidth/2 - 300, 170, 300, 30);
        this.defTargetSideLab.setHorizontalAlignment(SwingConstants.RIGHT);
        this.categoryPanel.add(this.defTargetSideLab);
        
        String[] sideOptions = {"left", "right"};
        this.defTargetSideBox = new JComboBox(sideOptions);
        this.defTargetSideBox.setFont(new Font("Dialog", 1, 14));
        this.defTargetSideBox.setBounds(frameWidth/2 + 10, 170, 150, 30);
        if (this.editMode) this.defTargetSideBox.setSelectedItem(this.targetSide);
        this.categoryPanel.add(this.defTargetSideBox);
        
        // Central panel end

        // Lower panel
        this.confirmButt = new JButton("Confirm");
        this.confirmButt.setFont(new Font("Dialog", 1, 14));
        this.confirmButt.setBounds(frameWidth/2 - 80, 0, 160, 30);
        this.confirmButt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                confirmButtActionPerformed(evt);
            }
        });
        this.lowerPanel.add(confirmButt);
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Interactive dictionary");
        setResizable(false);
        setSize(frameWidth, frameHeight);
        
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
        // lower panel end
    }
    
    private void catNameFieldAction() {
    
        this.categoryName = this.catNameField.getText();
    }
    
    private void confirmButtActionPerformed(ActionEvent evt) {
        
        if (this.categoryName.equals("")) {
            String message = "Category name is empty!";
            JOptionPane.showMessageDialog(this, message, "Warning", 
                                          JOptionPane.WARNING_MESSAGE);
        }
        else if (this.presCategories.contains(this.categoryName) && 
                !(this.initCategoryName.equals(this.categoryName))) {
            String message = "Category " + this.categoryName + " already exists!";
            JOptionPane.showMessageDialog(this, message, "Warning", 
                                          JOptionPane.WARNING_MESSAGE);
        }
        else if (this.srcLanBox.getSelectedItem().toString()
                .equals(this.targetLanBox.getSelectedItem().toString())) {
            String message = "Please choose different languages!";
            JOptionPane.showMessageDialog(this, message, "Warning", 
                                          JOptionPane.WARNING_MESSAGE);
        }
        else {
            
            this.updateOrInsertToDatabase();
        }
        
    }
    
    private void updateOrInsertToDatabase() {
    
        String chosenDefSrcLan = this.srcLanBox.getSelectedItem().toString();
        String chosenDefTargetLan = this.targetLanBox.getSelectedItem().toString();
        String chosenDefTargetSide = this.defTargetSideBox.getSelectedItem().toString();
        
        if (this.editMode) {
            String sql = String.format("UPDATE setup SET category = \'%s\', "
                    + "srcLanguage = \'%s\', targetLanguage = \'%s\', targetSide "
                    + "= \'%s\' WHERE category = \'%s\'", this.categoryName, 
                    chosenDefSrcLan, chosenDefTargetLan, chosenDefTargetSide,
                    this.initCategoryName);
            
            this.database.updateRecords(sql);

            sql = String.format("UPDATE words SET category = \'%s\'"
                    + " WHERE category = \'%s\'", this.categoryName,
                    this.initCategoryName);
            this.database.updateRecords(sql);
        }
        else {
            this.database.insertToSetup(categoryName, null, chosenDefSrcLan, 
                            chosenDefTargetLan, chosenDefTargetSide);
        }

        this.showMainScreen();
    }
    
    private void cancelButtActionPerformed(ActionEvent evt) {
    
        this.showMainScreen();
    }
    
    private void showMainScreen() {
        
        Main mainScreen = new Main();
        mainScreen.setLocationRelativeTo(this);
        this.dispose();
        mainScreen.setVisible(true);
    }
    
    private JButton confirmButt, cancelButt;
    private JLabel catNameLab, srcLanLab, targetLanLab, defTargetSideLab;
    private JTextField catNameField;
    private JComboBox srcLanBox, targetLanBox, defTargetSideBox;
    
    private String categoryName;
    private String initCategoryName;
    private String srcLanguage;
    private String targetLanguage;
    private String targetSide;
    private final boolean editMode;
    
}
