/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dictionary;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import javax.swing.*;
import java.util.*;


/**
 *
 * @author jakub
 */
public class WordSet extends JFrame{
    
    public WordSet(String wordSetNameStr) {
        this.wordSetName = wordSetNameStr;
        initComponents();
    }
    
    private void initComponents() {
        int frameWidth = 770;
        int frameHeight = 550;
        
        // Upper panel
        upperPanel = new JPanel();
        //upperPanel.setBackground(Color.red);
        upperPanel.setLayout(new GridLayout(0, 1));
        enterForSetLab = new JLabel("Enter words for set:");
        enterForSetLab.setFont(new Font("Dialog", 1, 18));
        enterForSetLab.setHorizontalAlignment(SwingConstants.CENTER);
        setNameLab = new JLabel(this.wordSetName);
        setNameLab.setFont(new Font("Dialog", 1, 18));
        setNameLab.setHorizontalAlignment(SwingConstants.CENTER);
        //upperPanel.add(Box.createRigidArea(new Dimension(100, 100)));
        upperPanel.add(enterForSetLab);
        upperPanel.add(setNameLab);
        
        // Center panel
        wordsPanel = new JPanel();
        wordsPanel.setLayout(null);
        // Language labels
        lan1Lab = new JLabel("Polish");
        lan1Lab.setFont(new Font("Dialog", 1, 14));
        lan1Lab.setBounds(200, 30, 100, 20);
        lan2Lab = new JLabel("English");
        lan2Lab.setFont(new Font("Dialog", 1, 14));
        lan2Lab.setBounds(500, 30, 100, 20);
        wordsPanel.add(lan1Lab);
        wordsPanel.add(lan2Lab);
        // Text fields
        // TODO Add scrolling
        TextField word1Init = new TextField(1, 0);
        word1Init.setFont(new Font("Dialog", 1, 14));
        word1Init.setBounds(150, this.position, 150, 30);
        word1Init.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent evt) {
                textFieldAction(evt);
            }
        });
        
        TextField word2Init = new TextField(2, 0);
        word2Init.setFont(new Font("Dialog", 1, 14));
        word2Init.setBounds(450, this.position, 150, 30);
        word2Init.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent evt) {
                textFieldAction(evt);
            }
        });
        
        words1Fields.add(word1Init);
        words2Fields.add(word2Init);
        
        wordsPanel.add(word1Init);
        wordsPanel.add(word2Init);
        
        // Lower panel
        lowerPanel = new JPanel();
        lowerPanel.setLayout(new GridLayout(1, 3, 100, 10));
        addWordButt = new JButton("Add word");
        addWordButt.setFont(new Font("Dialog", 1, 14));
        addWordButt.setHorizontalAlignment(SwingConstants.CENTER);
        
        addWordButt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                addWordButtActionPerformed(evt);
            }
        });
        
        nextButt = new JButton("Next");
        nextButt.setFont(new Font("Dialog", 1, 14));
        nextButt.setHorizontalAlignment(SwingConstants.CENTER);
        prevButt = new JButton("Previous");
        prevButt.setFont(new Font("Dialog", 1, 14));
        prevButt.setHorizontalAlignment(SwingConstants.CENTER);
        lowerPanel.add(prevButt);
        lowerPanel.add(addWordButt);
        lowerPanel.add(nextButt);
        
        // Layout
        this.setLayout(new BorderLayout());
        this.add(upperPanel, BorderLayout.PAGE_START);
        this.add(wordsPanel, BorderLayout.CENTER);
        this.add(lowerPanel, BorderLayout.PAGE_END);
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Interactive dictionary");
        setResizable(false);
        setSize(frameWidth, frameHeight);
    }
    
    private void textFieldAction(FocusEvent evt) {
        System.out.println("Action!!");
        TextField b = (TextField) evt.getSource();
        //JOptionPane.showMessageDialog(b, "Button " + b.index);
        System.out.println("Type: " + b.type + " Row: " + b.row);
    }
    
    private void addWordButtActionPerformed(ActionEvent evt) {
        
        int currIdx = words1Fields.size() - 1;
        
        TextField word1 = new TextField(1, currIdx + 1);
        TextField word2 = new TextField(2, currIdx + 1);
        
        this.position += this.wordsGap;
        
        word1.setFont(new Font("Dialog", 1, 14));
        word1.setBounds(150, this.position, 150, 30);
        word1.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent evt) {
                textFieldAction(evt);
            }
        });
        
        word2.setFont(new Font("Dialog", 1, 14));
        word2.setBounds(450, this.position, 150, 30);
        word2.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent evt) {
                textFieldAction(evt);
            }
        });
        
        words1Fields.add(word1);
        words2Fields.add(word2);
        
        this.wordsPanel.add(word1);
        this.wordsPanel.add(word2);
        this.wordsPanel.revalidate();
        this.wordsPanel.repaint();
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
            java.util.logging.Logger.getLogger(WordSet.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(WordSet.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(WordSet.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(WordSet.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new WordSet("Test").setVisible(true);
            }
        });
    }
    
    private class TextField extends JTextField {
        
        int type; // 1 - word1, 2 - word 2
        int row; // number of row
    
        TextField(int type, int row) {
        
            super();
            this.type = type;
            this.row = row;
            
        }
        
    }
    
    private final String wordSetName;
    private final int wordsGap = 40;
    private int position = 50;
    
    private JPanel upperPanel;
    private JPanel wordsPanel;
    private JPanel lowerPanel;
    
    private JLabel enterForSetLab;
    private JLabel setNameLab;
    private JLabel lan1Lab;
    private JLabel lan2Lab;
    
    private JButton addWordButt;
    private JButton nextButt;
    private JButton prevButt;
    
    private ArrayList<TextField> words1Fields = new ArrayList<TextField>();
    private ArrayList<TextField> words2Fields = new ArrayList<TextField>();
    
}