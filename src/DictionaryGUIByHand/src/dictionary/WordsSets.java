/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dictionary;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import javax.imageio.ImageIO;
import javax.swing.*;

/**
 *
 * @author jakub
 */
public class WordsSets extends JFrame {
    
    public WordsSets(String categoryName) {
        this.categoryName = categoryName;
        this.getDataFromDatabase();
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
        this.add(this.centerScrollPanel, BorderLayout.CENTER);
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
            
            String setName = (String)this.organizedData.get(i).get("setName");
            SetBox box = new SetBox(setName);
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
            JLabel boxLabel = new JLabel(setName);
            boxLabel.setFont(new Font("Dialog", Font.BOLD, 16));
            boxLabel.setHorizontalAlignment(SwingConstants.CENTER);
            boxLabel.setBounds(0, 2, 200, 20);
            box.add(boxLabel);
            
            JLabel wordsNumLab = new JLabel("Words number: " + 
                        this.organizedData.get(i).get("wordsNum").toString());
            wordsNumLab.setHorizontalAlignment(SwingConstants.LEFT);
            wordsNumLab.setBounds(100 - 90, 30, 120, 15);
            box.add(wordsNumLab);
            
            // change
            JLabel lastResLab = new JLabel("Last result: " + 
                        this.organizedData.get(i).get("lastResult").toString() + "%");
            lastResLab.setHorizontalAlignment(SwingConstants.LEFT);
            lastResLab.setBounds(100 - 90, 50, 120, 15);
            box.add(lastResLab);
            
            // Remove button
            BufferedImage img = null;
            
            try {
                img = ImageIO.read(new File("../../assets/remove_icon_res.png"));
            } catch (IOException e) {
                e.printStackTrace();
            }

            ImageIcon imageIcon = new ImageIcon(img);
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
                 
            JLabel bestResLab = new JLabel("Best result: " + 
                        this.organizedData.get(i).get("bestResult").toString() + "%");
            bestResLab.setHorizontalAlignment(SwingConstants.LEFT);
            bestResLab.setBounds(100 - 90, 70, 200, 15);
            box.add(bestResLab);
            
            this.setBoxes.add(box);
            this.wordsSetsPanel.add(box, gc);
        }
        
        this.wordsSetsPanel.revalidate();
        this.wordsSetsPanel.repaint();
        
    }
    
    private void getDataFromDatabase() {
    
        this.database = new DataBase();
        String sqlCommand = String.format("SELECT * FROM setup WHERE category "
                                        + "= \'%s\'", this.categoryName);
        java.util.List<Map<String, Object>> setupData = this.database.getData(sqlCommand);
        this.createOrganizedData(setupData);
        
    }
    
    private void createOrganizedData(java.util.List<Map<String, Object>> setupData) {
    
        this.organizedData = new ArrayList<>();
        Map<String, Object> row;
        this.presSets = new ArrayList<>();
        
        for (int i = 0; i < setupData.size(); i++) {

            if (setupData.get(i).get("setName") == null) break;
            
            row = new HashMap<>();
            String setName = (String)setupData.get(i).get("setName");
            this.presSets.add(setName);
            
            row.put("setName", setName);
            
            String sqlCommWordsNum = String.format("SELECT COUNT(*) AS rowcount "
                    + "FROM words "
                    + "WHERE category = \'%s\' AND setName = \'%s\'", 
                    this.categoryName, setName);
            row.put("wordsNum", this.database.countRecords(sqlCommWordsNum));

            row.put("lastResult", (int)setupData.get(i).get("lastResult"));
            row.put("bestResult", (int)setupData.get(i).get("bestResult"));
            this.organizedData.add(row);
        }
        
    }
    
    private void removeIconClicked(ActionEvent evt) {
        
        String setClicked = ((SetBox)((JButton)evt.getSource()).getParent()).setName;
        
        String message = "Are you sure you want to remove set " + setClicked + 
                         " ?";
        
        int dialogResult = JOptionPane.showConfirmDialog(this, 
                           message, "Warning", JOptionPane.YES_NO_OPTION, 
                           JOptionPane.WARNING_MESSAGE);
        
        if(dialogResult == JOptionPane.YES_OPTION){
            
            String sqlCommand = String.format("DELETE from setup WHERE setName "
                                            + "= \'%s\'", setClicked);
            this.database.removeRecords(sqlCommand);
            
            sqlCommand = String.format("DELETE from words WHERE setName "
                                            + "= \'%s\'", setClicked);
            this.database.removeRecords(sqlCommand);
            
            this.getDataFromDatabase();
            this.drawBoxes();
        }
    }
    
    private void boxClicked(MouseEvent me) {
    
        String setClicked = ((SetBox)(me.getSource())).setName;
        
        // TODO place to show new screen
        System.out.println("Box clicked for set: " + setClicked);
        
//        WordsSets wordsSetsScreen = new WordsSets(categoryClicked);
//        wordsSetsScreen.setLocationRelativeTo(this);
//        this.dispose();
//        wordsSetsScreen.setVisible(true);
        
    }
    
    private void addSetButtActionPerformed(ActionEvent evt) {
    
        String message = "Please, enter set name:";
        String setName = JOptionPane.showInputDialog(this, message);
        
        if (setName != null) {
            
            if (this.presSets.contains(setName)) {
                message = "Set " + setName + " already exists!";
                JOptionPane.showMessageDialog(this, message, "Warning", 
                                              JOptionPane.WARNING_MESSAGE);
            }
            else {
                // If setName list is empty update setName field in database
                if (this.organizedData.isEmpty()) {
                    String sql = String.format("UPDATE setup SET setName = \'%s\' "
                            + "WHERE "
                            + "category = \'%s\'", setName, this.categoryName);
                    this.database.updateRecords(sql);
                }
                // If not empty add new record
                else {
                    this.database.insertToSetup(this.categoryName, setName, 
                                                null, null);
                }
                
                this.getDataFromDatabase();
                this.drawBoxes();
            }

        }
        
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
                WordsSets wordsSetsScreen = new WordsSets("Test2");
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
        
        protected String setName;
        
    }
    
    private final String categoryName;
    private ArrayList<SetBox> setBoxes;
    private DataBase database;
    private java.util.List<Map<String, Object>> organizedData;
    private ArrayList<String> presSets;
    
    private JPanel upperPanel;
    private JPanel wordsSetsPanel;
    private JPanel lowerPanel;
    private JScrollPane centerScrollPanel;
    
    private JLabel wordsSetIntroLab;
    private JButton addSetButt;
    private JButton goBackButt;
    
}