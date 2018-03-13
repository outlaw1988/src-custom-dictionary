/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dictionary;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

/**
 *
 * @author jakub
 */
public class WordsSets extends JFrame {
    
    public WordsSets(String categoryName) {
        this.categoryName = categoryName;
        this.initComponents();
    }
    
    private void initComponents() {
        // TODO make global config with these parameters
        int frameWidth = 770;
        int frameHeight = 550;
        
        // Upper panel
        this.upperPanel = new JPanel();
        this.upperPanel.setLayout(new GridLayout(1, 1));
        this.wordsSetIntroLab = new JLabel("Sets of words for category: " + this.categoryName);
        this.wordsSetIntroLab.setFont(new Font("Dialog", 1, 18));
        this.wordsSetIntroLab.setHorizontalAlignment(SwingConstants.CENTER);
        this.upperPanel.add(this.wordsSetIntroLab);
        
        // Center panel
        this.wordsSetsPanel = new JPanel();
        this.wordsSetsPanel.setLayout(new GridBagLayout());
        
        this.drawBoxes();
        
        this.centerScrollPanel = new JScrollPane(this.wordsSetsPanel, 
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        // increase speed of scrolling
        this.centerScrollPanel.getVerticalScrollBar().setUnitIncrement(16);
        
        // Lower panel
        this.lowerPanel = new JPanel();
        this.lowerPanel.setLayout(null);
        this.lowerPanel.setPreferredSize(new Dimension(770, 30));
        
        this.addSetButt = new JButton("Add new set");
        this.addSetButt.setFont(new Font("Dialog", 1, 14));
        this.addSetButt.setBounds(frameWidth/2 - 80, 0, 160, 30);
        this.addSetButt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                addSetButtActionPerformed(evt);
            }
        });
        this.lowerPanel.add(addSetButt);
        
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

        // Layout
        this.setLayout(new BorderLayout());
        this.add(this.upperPanel, BorderLayout.PAGE_START);
//        this.add(this.centerScrollPanel, BorderLayout.CENTER);
        this.add(lowerPanel, BorderLayout.PAGE_END);
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Interactive dictionary");
        setResizable(false);
        setSize(frameWidth, frameHeight);
    }
    
    private void drawBoxes() {
    
        this.wordsSetsPanel.removeAll();
        this.setBoxes = new ArrayList<>();
        
        int gridy = 0;
        int boxInRow = 3;
        
        for (int i = 0; i < this.organizedData.size(); i++) {

            GridBagConstraints gc = new GridBagConstraints();
            gc.insets = new Insets(10, 10, 10, 10);
            gc.gridx = i % boxInRow;
            if ((i % boxInRow) == 0) gc.gridy = gridy++;
            
            String category = (String)this.organizedData.get(i).get("category");
            SetBox box = new SetBox(category);
            box.setLayout(null);
            
            box.addMouseListener(new MouseAdapter() {
                
                public void mouseEntered(MouseEvent me) {
                    Cursor handCursor = new Cursor(Cursor.HAND_CURSOR);
                    setCursor(handCursor);
                 }
                 public void mouseExited(MouseEvent me) {
                    Cursor defaultCursor = new Cursor(Cursor.DEFAULT_CURSOR);
                    setCursor(defaultCursor);
                 }
                
                public void mouseClicked(MouseEvent me) {
                    boxClicked(me);
                }
            });
            
            box.setPreferredSize(new Dimension(200, 100));
            box.setBorder(BorderFactory.createLineBorder(Color.black));
            JLabel boxLabel = new JLabel(category);
            boxLabel.setFont(new Font("Dialog", Font.BOLD, 16));
            boxLabel.setHorizontalAlignment(SwingConstants.CENTER);
            boxLabel.setBounds(0, 2, 200, 20);
            box.add(boxLabel);
            
            JLabel setsNumDescrLab = new JLabel("Sets number: " + this.organizedData.get(i).get("setsNum").toString());
            setsNumDescrLab.setHorizontalAlignment(SwingConstants.LEFT);
            setsNumDescrLab.setBounds(100 - 90, 30, 100, 15);
            box.add(setsNumDescrLab);
            
            JLabel wordsNumDescrLab = new JLabel("Words number: " + this.organizedData.get(i).get("wordsNum").toString());
            wordsNumDescrLab.setHorizontalAlignment(SwingConstants.LEFT);
            wordsNumDescrLab.setBounds(100 - 90, 50, 120, 15);
            box.add(wordsNumDescrLab);
            
            BufferedImage img = null;
            
            try {
                img = ImageIO.read(new File("../../assets/remove_icon_res.png"));
            } catch (IOException e) {
                e.printStackTrace();
            }

//            Image dimg = img.getScaledInstance(25, 25, Image.SCALE_SMOOTH);
            ImageIcon imageIcon = new ImageIcon(img);
            //JLabel removeIcon = new JLabel(imageIcon);
            JButton removeIcon = new JButton(imageIcon);
            removeIcon.setBounds(150, 30, 30, 30);
            removeIcon.setToolTipText("Remove category");

            removeIcon.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    removeIconClicked(e);
                }
            });
            
            box.add(removeIcon);
            
            // Languages
            if (this.organizedData.get(i).get("language1") != null) {
            
                JLabel lanLabel = new JLabel(this.organizedData.get(i).
                                            get("language1").toString() + 
                                            " <-> " + this.organizedData.get(i).
                                            get("language2").toString()
                                            );
                
                lanLabel.setHorizontalAlignment(SwingConstants.CENTER);
                lanLabel.setBounds(0, 80, 200, 15);
                box.add(lanLabel);
            }
            
            this.setBoxes.add(box);
            this.categoryPanel.add(box, gc);
        }
        
        this.categoryPanel.revalidate();
        this.categoryPanel.repaint();
        
    }
    
    private void addSetButtActionPerformed(ActionEvent evt) {
    
    }
    
    private void goBackButtActionPerformed(ActionEvent evt) {
        
        Main catScreen = new Main();
        catScreen.setLocationRelativeTo(this);
        this.dispose();
        catScreen.setVisible(true);
        
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
                WordsSets wordsSetsScreen = new WordsSets("Test");
                wordsSetsScreen.setLocationRelativeTo(null);
                wordsSetsScreen.setVisible(true);
            }
        });
    }
    
    private class SetBox extends JPanel {
    
        public SetBox(String setName) {
            super();
            this.setName = setName;
        }
        
        String setName;
//        int setsNum;
//        int wordsNum;
//        String language1;
//        String language2;
        
    }
    
    String categoryName;
    private ArrayList<Box> setBoxes;
    
    private JPanel upperPanel;
    private JPanel wordsSetsPanel;
    private JPanel lowerPanel;
    private JScrollPane centerScrollPanel;
    
    private JLabel wordsSetIntroLab;
    private JButton addSetButt;
    private JButton goBackButt;
    
}
