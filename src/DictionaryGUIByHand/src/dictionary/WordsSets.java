/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dictionary;

import java.awt.*;
import java.awt.event.*;
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
    
    // TODO deafultSrcLanguage, defaultTargetLanguage based on category name,
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
        this.upperPanel = new JPanel(null);
        this.upperPanel.setPreferredSize(new Dimension(770, 30));
        
        this.wordsSetIntroLab = new JLabel("Sets of words for category: " + 
                                           this.categoryName);
        this.wordsSetIntroLab.setFont(new Font("Dialog", 1, 18));
        this.wordsSetIntroLab.setBounds(0, 0, frameWidth, 30);
        this.wordsSetIntroLab.setHorizontalAlignment(SwingConstants.CENTER);
        
        this.upperPanel.add(this.wordsSetIntroLab);
        
        // Center panel
        this.wordsSetsPanel = new JPanel();
        this.wordsSetsPanel.setLayout(new GridBagLayout());
        
        this.drawBoxes();
        
        this.centerScrollPanel = new JScrollPane(this.wordsSetsPanel, 
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
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
            String srcLanguage = (String)this.organizedData.get(i).get("srcLanguage");
            String targetLanguage = (String)this.organizedData.get(i).get("targetLanguage");
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
            wordsNumLab.setBounds(100 - 90, 25, 120, 15);
            box.add(wordsNumLab);
            
            // change
            JLabel lastResLab = new JLabel("Last result: " + 
                        this.organizedData.get(i).get("lastResult").toString() + "%");
            lastResLab.setHorizontalAlignment(SwingConstants.LEFT);
            lastResLab.setBounds(100 - 90, 42, 120, 15);
            box.add(lastResLab);
            
            // Remove button
            BufferedImage img = null;
            
            try {
                img = ImageIO.read(new File("../../assets/three_dots_res.png"));
            } catch (IOException e) {
                e.printStackTrace();
            }

            ImageIcon dots = new ImageIcon(img);
            JLabel dotsLab = new JLabel(dots);
            dotsLab.setBounds(170, 30, 30, 30);

            dotsLab.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    popUpMenuAction(e, setName);
                }
            });
            
            box.add(dotsLab);
                 
            JLabel bestResLab = new JLabel("Best result: " + 
                        this.organizedData.get(i).get("bestResult").toString() + "%");
            bestResLab.setHorizontalAlignment(SwingConstants.LEFT);
            bestResLab.setBounds(100 - 90, 60, 200, 15);
            box.add(bestResLab);
            
            JLabel lanLabel = new JLabel(srcLanguage + " -> " + targetLanguage);

            lanLabel.setHorizontalAlignment(SwingConstants.CENTER);
            lanLabel.setBounds(0, 80, 200, 15);
            box.add(lanLabel);
            
            this.setBoxes.add(box);
            this.wordsSetsPanel.add(box, gc);
        }
        
        this.wordsSetsPanel.revalidate();
        this.wordsSetsPanel.repaint();
        
    }
    
    private void popUpMenuAction(MouseEvent evt, String setName) {
    
        this.menu = new JPopupMenu();
        
        JMenuItem renameItem = new MenuItem(setName);
        renameItem.setText("rename");
        renameItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                renameSetClicked(e);
            }
        });
        
        JMenuItem removeItem = new MenuItem(setName);
        removeItem.setText("remove");
        removeItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeIconClicked(e);
            }
        });
        
        JMenuItem editItem = new MenuItem(setName);
        editItem.setText("edit");
        editItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editItemClicked(e);
            }
        });
        
        // TODO Currently - not used, requires more work
//        JMenuItem changeCatItem = new MenuItem(setName);
//        changeCatItem.setText("change category");
//        changeCatItem.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                System.out.println("Change category clicked...");
//                changeCategoryClicked(e);
//            }
//        });
        
        this.menu.add(renameItem);
        this.menu.add(removeItem);
        this.menu.add(editItem);
//        this.menu.add(changeCatItem);
        
        this.menu.show(evt.getComponent(), evt.getX(), evt.getY());
        
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
        this.defaultSettings = new ArrayList<>();
        
        for (int i = 0; i < setupData.size(); i++) {
            
            // Default settings
            if (setupData.get(i).get("setName") == null) {
                Map<String, String> row = new HashMap<>();
                row.put("defSrcLanguage", (String)setupData.get(i).get("srcLanguage"));
                row.put("defTargetLanguage", (String)setupData.get(i)
                                             .get("targetLanguage"));
                row.put("defTargetSide", (String)setupData.get(i).get("targetSide"));
                
                this.defaultSettings.add(row);
            }
            // Present sets of words
            else {
                Map<String, Object> row = new HashMap<>();
                String setName = (String)setupData.get(i).get("setName");

                row.put("setName", setName);

                String sqlCommWordsNum = String.format("SELECT COUNT(*) AS rowcount "
                        + "FROM words "
                        + "WHERE category = \'%s\' AND setName = \'%s\'", 
                        this.categoryName, setName);
                row.put("wordsNum", this.database.countRecords(sqlCommWordsNum));
                row.put("srcLanguage", (String)setupData.get(i).get("srcLanguage"));
                row.put("targetLanguage", (String)setupData.get(i).get("targetLanguage"));
                row.put("lastResult", (int)setupData.get(i).get("lastResult"));
                row.put("bestResult", (int)setupData.get(i).get("bestResult"));

                this.organizedData.add(row);
            }

        }
        
    }
    
    // TODO Currently - not used, requires more work
//    private void changeCategoryClicked(ActionEvent evt) {
//    
//        this.presCategories.remove(this.categoryName);
//        String[] catsToChoose = this.presCategories.toArray(new String[0]);
//        String setClicked = ((MenuItem)evt.getSource()).setName;
//        
//        String message = "Select desired category:";
//        
//        String newCategory = (String)JOptionPane.showInputDialog(
//                                this,
//                                message,
//                                "Select category",
//                                JOptionPane.PLAIN_MESSAGE,
//                                null,
//                                catsToChoose,
//                                null);
//        
//        System.out.println(newCategory);
//        
//        if (newCategory != null) {
//        
//            String sqlCommand = String.format("UPDATE setup SET category = \'%s\'"
//                    + " WHERE category = \'%s\' AND setName = \'%s\'", newCategory, 
//                    this.categoryName, setClicked);
//            this.database.updateRecords(sqlCommand);
//            
//            sqlCommand = String.format("UPDATE words SET category = \'%s\'"
//                    + " WHERE category = \'%s\' AND setName = \'%s\'", newCategory, 
//                    this.categoryName, setClicked);
//            this.database.updateRecords(sqlCommand);
//                        
//            this.getDataFromDatabase();
//            this.drawBoxes();
//        }
//    }
    
    private void renameSetClicked(ActionEvent evt) {
    
        String setClicked = ((MenuItem)evt.getSource()).setName;
       
        String message = "Please, enter set name:";
        String setName = JOptionPane.showInputDialog(this, message, setClicked);
        
        String sqlCommand = String.format("SELECT COUNT(*) AS rowcount FROM setup "
                    + "WHERE category = \'%s\' AND setName = \'%s\'", this.categoryName, 
                    setName);
        
        if (setName.equals("")) {
            String messageEmpty = "Set name is empty!";
            JOptionPane.showMessageDialog(this, messageEmpty, "Warning", 
                                          JOptionPane.WARNING_MESSAGE);
            
        }
        else if (this.database.countRecords(sqlCommand) != 0 && 
                !setName.equals(setClicked)) {
            String messageExst = "Set " + setName + " for category " + 
                                this.categoryName + " already exists!";
            JOptionPane.showMessageDialog(this, messageExst, "Warning", 
                                JOptionPane.WARNING_MESSAGE);
        }
        else {
        
            sqlCommand = String.format("UPDATE setup SET setName = \'%s\'"
                    + " WHERE category = \'%s\' AND setName = \'%s\'", setName, 
                    this.categoryName, setClicked);
            
            this.database.updateRecords(sqlCommand);
            
            sqlCommand = String.format("UPDATE words SET setName = \'%s\'"
                    + " WHERE category = \'%s\' AND setName = \'%s\'", setName, 
                    this.categoryName, setClicked);
            
            this.database.updateRecords(sqlCommand);
            
            this.getDataFromDatabase();
            this.drawBoxes();
        }

    }
    
    private void removeIconClicked(ActionEvent evt) {
        
        String setClicked = ((MenuItem)evt.getSource()).setName;
        
        String message = "Are you sure you want to remove set " + setClicked + 
                         " ?";
        
        int dialogResult = JOptionPane.showConfirmDialog(this, 
                           message, "Warning", JOptionPane.YES_NO_OPTION, 
                           JOptionPane.WARNING_MESSAGE);
        
        if(dialogResult == JOptionPane.YES_OPTION){
            
            String sqlCommand = String.format("DELETE from setup WHERE category "
                                    + "= \'%s\' AND setName = \'%s\'", 
                                    this.categoryName, setClicked);
            this.database.removeRecords(sqlCommand);
            
            sqlCommand = String.format("DELETE from words WHERE category "
                                    + "= \'%s\' AND setName = \'%s\'", 
                                    this.categoryName, setClicked);
            this.database.removeRecords(sqlCommand);
            
            this.getDataFromDatabase();
            this.drawBoxes();
        }
    }
    
    private void editItemClicked(ActionEvent evt) {
    
        String setName = ((MenuItem)evt.getSource()).setName;
                
        WordsSetDefEdit editScreen = new WordsSetDefEdit(categoryName, setName);
        editScreen.setLocationRelativeTo(this);
        this.dispose();
        editScreen.setVisible(true);
    }
    
    private void boxClicked(MouseEvent me) {
    
        String setClicked = ((SetBox)(me.getSource())).setName;
        
        WordsSetDefPreview previewSc = new WordsSetDefPreview(this.categoryName, 
                                                              setClicked);
        previewSc.setLocationRelativeTo(this);
        this.dispose();
        previewSc.setVisible(true);
    }
    
    private void addSetButtActionPerformed(ActionEvent evt) {
    
        WordsSetDefEdit wordsSetDefEdit = new WordsSetDefEdit(this.categoryName, 
                                          this.defaultSettings);
        wordsSetDefEdit.setLocationRelativeTo(this);
        this.dispose();
        wordsSetDefEdit.setVisible(true);
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
                WordsSets wordsSetsScreen = new WordsSets("Test3");
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
    
    private class MenuItem extends JMenuItem {
    
        public MenuItem(String setName) {
            super();
            this.setName = setName;
        }
        
        protected String setName;
    
    }
    
    private final String categoryName;
    private ArrayList<SetBox> setBoxes;
    private DataBase database;
    private java.util.List<Map<String, Object>> organizedData;
    private java.util.List<Map<String, String>> defaultSettings;
    
    private JPanel upperPanel, wordsSetsPanel, lowerPanel;
    private JScrollPane centerScrollPanel;
    private JPopupMenu menu;
    
    private JLabel wordsSetIntroLab;
    private JButton addSetButt;
    private JButton goBackButt;
    
}
