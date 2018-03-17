/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dictionary;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
        
        String[] optionsTargetLan = {this.targetLanguage, this.srcLanguage};
        this.targetLanCombo = new JComboBox(optionsTargetLan);
        this.targetLanCombo.setFont(new Font("Dialog", 1, 14));
        this.targetLanCombo.setBounds(frameWidth/2 + 10, 80, 150, 30);
        this.targetLanCombo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                drawCenterPanel();
            }
        });
        this.upperPanel.add(this.targetLanCombo);
        
        this.targetLanLocationLab = new JLabel("Position:");
        this.targetLanLocationLab.setFont(new Font("Dialog", 1, 14));
        this.targetLanLocationLab.setBounds(frameWidth/2 + 100, 80, 150, 30);
        this.targetLanLocationLab.setHorizontalAlignment(SwingConstants.RIGHT);
        this.upperPanel.add(this.targetLanLocationLab);
        
        String[] optionsSide = {"left", "right"};
        this.targetLanLocationCombo = new JComboBox(optionsSide);
        this.targetLanLocationCombo.setFont(new Font("Dialog", 1, 14));
        this.targetLanLocationCombo.setBounds(frameWidth/2 + 260, 80, 100, 30);
        this.targetLanLocationCombo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                 drawCenterPanel();
            }
        });
        this.upperPanel.add(this.targetLanLocationCombo);
        
        // Upper panel end
        
        // Center panel
        this.wordsEditPanel = new JPanel(null);
        
        this.drawCenterPanel();
        
        this.centerScrollPanel = new JScrollPane(this.wordsEditPanel, 
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
        this.add(lowerPanel, BorderLayout.PAGE_END);
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Interactive dictionary");
        setResizable(false);
        setSize(frameWidth, frameHeight);
        
    }
    
    private void updateLanguages() {
    
        String temp = this.targetLanguage;
        this.targetLanguage = this.targetLanCombo.getSelectedItem().toString();
        if (this.targetLanguage.equals(this.srcLanguage)) {
            this.srcLanguage = temp;
        }
    }
    
    private void drawCenterPanel() {
    
        this.wordsEditPanel.removeAll();

        String selectedSideOpt = this.targetLanLocationCombo.getSelectedItem().toString();
        this.updateLanguages();
        
        this.leftLanTypeLab = new JLabel();
        this.leftLanLab = new JLabel();
        
        if (selectedSideOpt.equals("left")){
            this.leftLanTypeLab.setText("Target language:");
            this.leftLanLab.setText(this.targetLanguage);
        }
        else {
            this.leftLanTypeLab.setText("Source Language:");
            this.leftLanLab.setText(this.srcLanguage);
        }
            
        this.leftLanTypeLab.setFont(new Font("Dialog", 1, 14));
        this.leftLanTypeLab.setBounds(this.frameWidth/2 - 250, 10, 150, 30);
        this.leftLanTypeLab.setHorizontalAlignment(SwingConstants.RIGHT);
        this.wordsEditPanel.add(this.leftLanTypeLab);
        
        this.leftLanLab.setFont(new Font("Dialog", 1, 14));
        this.leftLanLab.setBounds(this.frameWidth/2 - 300, 30, 150, 30);
        this.leftLanLab.setHorizontalAlignment(SwingConstants.RIGHT);
        this.wordsEditPanel.add(this.leftLanLab);
        
        this.rightLanTypeLab = new JLabel();
        this.rightLanLab = new JLabel();
        
        if (selectedSideOpt.equals("left")){
            this.rightLanTypeLab.setText("Source language:");
            this.rightLanLab.setText(this.srcLanguage);
        }
        else {
            this.rightLanTypeLab.setText("Target Language:");
            this.rightLanLab.setText(this.targetLanguage);
        }
        
        this.rightLanTypeLab.setFont(new Font("Dialog", 1, 14));
        this.rightLanTypeLab.setBounds(this.frameWidth/2 + 100, 10, 150, 30);
        this.rightLanTypeLab.setHorizontalAlignment(SwingConstants.LEFT);
        this.wordsEditPanel.add(this.rightLanTypeLab);
        
        this.rightLanLab.setFont(new Font("Dialog", 1, 14));
        this.rightLanLab.setBounds(this.frameWidth/2 + 150, 30, 150, 30);
        this.rightLanLab.setHorizontalAlignment(SwingConstants.LEFT);
        this.wordsEditPanel.add(this.rightLanLab);
        
        this.wordsEditPanel.revalidate();
        this.wordsEditPanel.repaint();
    
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
    
        TextField(String language, int row) {
        
            super();
            this.language = language;
            this.row = row;
            
        }
        
        protected String language;
        protected int row;
    
    }
    
    private String setName;
    private boolean isEditMode;
    private String srcLanguage, targetLanguage;
    
    private JPanel upperPanel, wordsEditPanel, lowerPanel;
    private JScrollPane centerScrollPanel;
    
    private JLabel enterWordsForSetLab, setNameLab, targetLanLab, targetLanLocationLab, 
            leftLanTypeLab, rightLanTypeLab, leftLanLab, rightLanLab;
    private JTextField setNameField;
    private JComboBox targetLanCombo, targetLanLocationCombo;
    private JButton cancelButt, addWordButt, saveButt, confirmButt;
    
    private int frameWidth, frameHeight;
    
}
