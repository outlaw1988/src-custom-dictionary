/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dictionary;

import java.awt.*;
import javax.swing.*;

/**
 *
 * @author jakub
 */
public class Exam extends JFrame {
    
    public Exam(String categoryName, String setName) {
        
        this.categoryName = categoryName;
        this.setName = setName;
        initComponents();
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
    
        // Layout
        this.setLayout(new BorderLayout());
        this.add(this.upperPanel, BorderLayout.PAGE_START);
//        this.add(this.centerScrollPanel, BorderLayout.CENTER);
//        this.add(this.lowerPanel, BorderLayout.PAGE_END);
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Interactive dictionary");
        setResizable(false);
        setSize(frameWidth, frameHeight);
    
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
    
    private JPanel upperPanel;
    private JLabel mainHeaderLab;
    
    private int frameWidth = 770;
    private int frameHeight = 550;
    
}
