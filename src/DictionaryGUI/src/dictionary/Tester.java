/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dictionary;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author jakub
 */
public class Tester extends JFrame {
    
    public Tester() {
        initComponents();
    }
    
    private void initComponents() {
        this.frameWidth = 770;
        this.frameHeight = 550;
        
        this.pan = new JPanel(null);
        pan.setPreferredSize(new Dimension(this.frameWidth, this.frameHeight));
        
        this.lab1 = new JLabel("Test right");
        lab1.setFont(new Font("Dialog", 1, 14));
        lab1.setBounds(600, 100, 150, 30);
        pan.add(lab1);
        
        JTextField field1 = new JTextField();
        field1.setFont(new Font("Dialog", 1, 14));
        field1.setBounds(frameWidth/2 - 200, 200, 200, 30);
        field1.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent evt) {
                focusLostPerformed(evt);
            }
        });
        pan.add(field1);
        fields.add(field1);
        
        JTextField field2 = new JTextField();
        field2.setFont(new Font("Dialog", 1, 14));
        field2.setBounds(frameWidth/2 + 100, 200, 200, 30);
        field2.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent evt) {
                focusLostPerformed(evt);
            }
        });
        pan.add(field2);
        fields.add(field2);
        
        JButton butt1 = new JButton("Swap");
        butt1.setFont(new Font("Dialog", 1, 14));
        butt1.setBounds(frameWidth/2, 300, 200, 30);
        butt1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                swapFields();
            }
        });
        pan.add(butt1);
        
        this.add(pan);
          
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Interactive dictionary");
        setResizable(false);
        setSize(frameWidth, frameHeight);
    }
    
    private void focusLostPerformed(FocusEvent evt) {
    
        JTextField field = (JTextField)evt.getSource();
        System.out.println("Word: " + field.getText());
    
    }
    
    private void swapFields() {
    
        JTextField field1 = fields.get(0);
        JTextField field2 = fields.get(1);
        
//        String word1 = field1.getText();
//        String word2 = field2.getText();
        
        if (this.textPos.equals("right")) {
            this.textPos = "left";
            this.lab1.setText("Text left");
            this.lab1.setBounds(0, 100, 200, 30);
            field2.setBounds(frameWidth/2 - 200, 200, 200, 30);
            field1.setBounds(frameWidth/2 + 100, 200, 200, 30);
        }
        else if(this.textPos.equals("left")) {
            this.textPos = "right";
            this.lab1.setText("Text right");
            this.lab1.setBounds(600, 100, 150, 30);
            field2.setBounds(frameWidth/2 + 100, 200, 200, 30);
            field1.setBounds(frameWidth/2 - 200, 200, 200, 30);
        }
        
        
        
//        field1.setText(word2);
//        field2.setText(word1);
        
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
                Tester gui = new Tester();
                gui.setLocationRelativeTo(null);
                gui.setVisible(true);
            }
        });
    }
    
    private JPanel pan;
    private ArrayList<JTextField> fields = new ArrayList<>();
    private JLabel lab1;
    private String textPos = "right";
    
    private int frameWidth, frameHeight;
    
}

