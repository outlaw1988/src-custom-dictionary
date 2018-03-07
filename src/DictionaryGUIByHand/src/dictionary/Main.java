/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dictionary;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 * @author jakub
 */
public class Main extends JFrame {
    
    public Main() {
        initComponents();
    }
    
    private void initComponents() {
        welcomeLab = new JLabel();
        enterSetLab = new JLabel();
        setNameField = new JTextField();
        
        int frameWidth = 770;
        int frameHeight = 550;
        
        welcomeLab.setFont(new Font("Dialog", 1, 24));
        welcomeLab.setHorizontalAlignment(SwingConstants.CENTER);
        welcomeLab.setText("Welcome in interactive dictionary");
        
        enterSetLab.setFont(new Font("Dialog", 1, 18));
        enterSetLab.setHorizontalAlignment(SwingConstants.CENTER);
        enterSetLab.setText("Type the set name and press enter:");
        
        setNameField.setFont(new Font("Dialog", 1, 16));
        setNameField.setHorizontalAlignment(SwingConstants.CENTER);
        
        add(welcomeLab);
        add(enterSetLab);
        add(setNameField);
        
        this.setLayout(null);
        welcomeLab.setBounds(0, frameHeight/2 - 100, frameWidth, 30);
        enterSetLab.setBounds(0, frameHeight/2 - 50, frameWidth, 20);
        setNameField.setBounds(frameWidth/2 - 100, frameHeight/2, 200, 30);
        setNameField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setNameActionPerformed(e);
            }
        });
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Interactive dictionary");
        setResizable(false);
        setSize(frameWidth, frameHeight);
    }
    
    private void setNameActionPerformed(java.awt.event.ActionEvent evt) {                                        
        wordsSetNameStr = setNameField.getText();
        WordSet wordSetScreen = new WordSet(wordsSetNameStr);
        wordSetScreen.setLocationRelativeTo(null);
        wordSetScreen.setVisible(true);
        this.dispose();
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
                Main gui = new Main();
                gui.setLocationRelativeTo(null);
                gui.setVisible(true);
            }
        });
    }
    
    private JLabel welcomeLab;
    private JLabel enterSetLab;
    private JTextField setNameField;
    
    private String wordsSetNameStr;
    
}
