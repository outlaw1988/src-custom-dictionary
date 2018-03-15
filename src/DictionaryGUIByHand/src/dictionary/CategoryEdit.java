/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dictionary;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

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
                        String targetLanguage) {
    
        this.editMode = true;
        this.initCategoryName = categoryName;
        this.srcLanguage = srcLanguage;
        this.targetLanguage = targetLanguage;
        initComponents();
        
    }
    
    private void initComponents() {
        
        // TODO make global config with these parameters
        int frameWidth = 770;
        int frameHeight = 550;
    
        //this.upperPanel.removeAll();
        this.categoryPanel.removeAll();
        //this.centerScrollPanel.removeAll();
        this.lowerPanel.removeAll();
        
        // Central panel
        //this.categoryPanel = new JPanel();
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
        
        this.srcLanLab = new JLabel("Source language:");
        this.srcLanLab.setFont(new Font("Dialog", 1, 14));
        this.srcLanLab.setBounds(frameWidth/2 - 150, 90, 150, 30);
        this.srcLanLab.setHorizontalAlignment(SwingConstants.RIGHT);
        this.categoryPanel.add(this.srcLanLab);
        
        String[] sourceLanguages = {"Polish", "English", "Spanish", "German", "Italian"};
        this.srcLanBox = new JComboBox(sourceLanguages);
        this.srcLanBox.setFont(new Font("Dialog", 1, 14));

        if (this.editMode) this.srcLanBox.setSelectedItem(this.srcLanguage);
        
        this.srcLanBox.setBounds(frameWidth/2 + 10, 90, 150, 30);
        this.categoryPanel.add(this.srcLanBox);
        
        this.targetLanLab = new JLabel("Target language:");
        this.targetLanLab.setFont(new Font("Dialog", 1, 14));
        this.targetLanLab.setBounds(frameWidth/2 - 150, 130, 150, 30);
        this.targetLanLab.setHorizontalAlignment(SwingConstants.RIGHT);
        this.categoryPanel.add(this.targetLanLab);
        
        // TODO check whether languages not repeat
        String[] targetLanguages = {"English", "Polish", "Spanish", "German", "Italian"};
        this.targetLanBox = new JComboBox(targetLanguages);
        this.targetLanBox.setFont(new Font("Dialog", 1, 14));
        
        if (this.editMode) this.targetLanBox.setSelectedItem(this.targetLanguage);
        
        this.targetLanBox.setBounds(frameWidth/2 + 10, 130, 150, 30);
        this.categoryPanel.add(this.targetLanBox);
        
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
    
    // TODO Refactor this method
    private void confirmButtActionPerformed(ActionEvent evt) {
        
        if (this.categoryName.equals("")) {
            String message = "Category name is empty!";
            JOptionPane.showMessageDialog(this, message, "Warning", 
                                          JOptionPane.WARNING_MESSAGE);
        }
        else if (this.presCategories.contains(this.categoryName) && !this.editMode) {
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
            
            // TODO consider case when new category name is the same as one of categories
            if (this.editMode) {
                System.out.println("Edit mode...");
                
                String sql = String.format("UPDATE setup SET category = \'%s\', "
                        + "language1 = \'%s\', language2 = \'%s\' WHERE category "
                        + "= \'%s\'", this.categoryName, this.srcLanBox.getSelectedItem().
                                toString(), 
                        this.targetLanBox.getSelectedItem().toString(), 
                        this.initCategoryName);
                this.database.updateRecords(sql);
                
                sql = String.format("UPDATE words SET category = \'%s\'"
                        + " WHERE category = \'%s\'", this.categoryName,
                        this.initCategoryName);
                this.database.updateRecords(sql);
            }
            else {
                this.database.insertToSetup(categoryName, null, 
                                this.srcLanBox.getSelectedItem().toString(), 
                                this.targetLanBox.getSelectedItem().toString());
            }
            
            this.showMainScreen();
        }
        
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
    private JLabel catNameLab, srcLanLab, targetLanLab;
    private JTextField catNameField;
    private JComboBox srcLanBox, targetLanBox;
    
    private String categoryName;
    private String initCategoryName;
    private String srcLanguage;
    private String targetLanguage;
    private final boolean editMode;
    
}
