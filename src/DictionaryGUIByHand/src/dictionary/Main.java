/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dictionary;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import javax.imageio.ImageIO;

/**
 *
 * @author jakub
 */
public class Main extends JFrame {
    
    public Main() {
        this.getDataFromDatabase();
        this.initComponents();
    }
    
    private void initComponents() {
        // TODO make global config with these parameters
        int frameWidth = 770;
        int frameHeight = 550;
        
        // Upper panel
        this.upperPanel = new JPanel();
        this.upperPanel.setLayout(null);
        this.upperPanel.setPreferredSize(new Dimension(770, 30));
        
        this.catIntroLab = new JLabel("Categories");
        this.catIntroLab.setFont(new Font("Dialog", 1, 18));
        this.catIntroLab.setBounds(0, 0, frameWidth, 30);
        this.catIntroLab.setHorizontalAlignment(SwingConstants.CENTER);
        this.upperPanel.add(this.catIntroLab);
        
        // Center panel
        this.categoryPanel = new JPanel();
        this.categoryPanel.setLayout(new GridBagLayout());
        
        this.drawBoxes();
        
        this.centerScrollPanel = new JScrollPane(this.categoryPanel, 
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, 
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        // increase speed of scrolling
        this.centerScrollPanel.getVerticalScrollBar().setUnitIncrement(16);

        // Lower panel
        this.lowerPanel = new JPanel();
        this.lowerPanel.setLayout(null);
        this.lowerPanel.setPreferredSize(new Dimension(770, 30));
        
        this.addCatButt = new JButton("Add new category");
        this.addCatButt.setFont(new Font("Dialog", 1, 14));
        this.addCatButt.setBounds(frameWidth/2 - 100, 0, 200, 30);
        this.addCatButt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                addCatButtActionPerformed(evt);
            }
        });
        this.lowerPanel.add(addCatButt);
        
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
    
        this.categoryPanel.removeAll();
        this.boxes = new ArrayList<>();
        
        int gridy = 0;
        int boxInRow = 3;
        
        for (int i = 0; i < this.organizedData.size(); i++) {

            GridBagConstraints gc = new GridBagConstraints();
            gc.insets = new Insets(10, 10, 10, 10);
            gc.gridx = i % boxInRow;
            if ((i % boxInRow) == 0) gc.gridy = gridy++;
            
            String category = (String)this.organizedData.get(i).get("category");
            String srcLanguage = (String)this.organizedData.get(i).get("language1");
            String targetLanguage = (String)this.organizedData.get(i).get("language2");
            
            Box box = new Box(category, srcLanguage, targetLanguage);
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
            
            JLabel setsNumDescrLab = new JLabel("Sets number: " + 
                        this.organizedData.get(i).get("setsNum").toString());
            setsNumDescrLab.setHorizontalAlignment(SwingConstants.LEFT);
            setsNumDescrLab.setBounds(100 - 90, 30, 100, 15);
            box.add(setsNumDescrLab);
            
            JLabel wordsNumDescrLab = new JLabel("Words number: " + 
                        this.organizedData.get(i).get("wordsNum").toString());
            wordsNumDescrLab.setHorizontalAlignment(SwingConstants.LEFT);
            wordsNumDescrLab.setBounds(100 - 90, 50, 120, 15);
            box.add(wordsNumDescrLab);
            
            BufferedImage img = null;
            
            try {
                img = ImageIO.read(new File("../../assets/three_dots_res.png"));
            } catch (IOException e) {
                e.printStackTrace();
            }

//            Image dimg = img.getScaledInstance(25, 25, Image.SCALE_SMOOTH);
            ImageIcon dots = new ImageIcon(img);
            JLabel dotsLab = new JLabel(dots);
            //JButton removeIcon = new JButton(imageIcon);
            dotsLab.setBounds(170, 30, 30, 30);

            dotsLab.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    popUpMenuAction(e, category, srcLanguage, targetLanguage);
                }
            });
            
            box.add(dotsLab);
            
            // Languages
            if (this.organizedData.get(i).get("language1") != null) {
            
                JLabel lanLabel = new JLabel(srcLanguage + " -> " + targetLanguage);
                
                lanLabel.setHorizontalAlignment(SwingConstants.CENTER);
                lanLabel.setBounds(0, 80, 200, 15);
                box.add(lanLabel);
            }
            
            this.boxes.add(box);
            this.categoryPanel.add(box, gc);
        }
        
        this.categoryPanel.revalidate();
        this.categoryPanel.repaint();
    
    }
    
    private void getDataFromDatabase() {
    
        this.database = new DataBase();
        String sqlCommand = "SELECT * FROM setup";
        this.setupData = this.database.getData(sqlCommand);
        this.createOrganizedData();
        
    }
    
    private void createOrganizedData() {
    
        this.organizedData = new ArrayList<>();
        Map<String, Object> row;
        this.presCategories = new ArrayList<>();
        
        for (int i = 0; i < this.setupData.size(); i++) {
            row = new HashMap<>();
            String category = (String)this.setupData.get(i).get("category");
            
            if (this.presCategories.contains(category)) continue;
            else this.presCategories.add(category);
            
            row.put("category", category);

            String sqlCommSetsNum = String.format("SELECT COUNT(*) AS rowcount FROM setup "
                    + "WHERE category = \'%s\' AND setName IS NOT NULL", category);
            row.put("setsNum", this.database.countRecords(sqlCommSetsNum));
            
            String sqlCommWordsNum = String.format("SELECT COUNT(*) AS rowcount FROM words "
                    + "WHERE category = \'%s\' AND setName IS NOT NULL", category);
            row.put("wordsNum", this.database.countRecords(sqlCommWordsNum));

            row.put("language1", (String)this.setupData.get(i).get("language1"));
            row.put("language2", (String)this.setupData.get(i).get("language2"));
            this.organizedData.add(row);
        }
    }
    
    private void boxClicked(MouseEvent me) {
    
        String categoryClicked = ((Box)(me.getSource())).categoryId;
        String srcLanguage = ((Box)(me.getSource())).srcLanguage;
        String targetLanguage = ((Box)(me.getSource())).targetLanguage;
        
        WordsSets wordsSetsScreen = new WordsSets(categoryClicked, srcLanguage, 
                                                  targetLanguage, this.presCategories);
        wordsSetsScreen.setLocationRelativeTo(this);
        this.dispose();
        wordsSetsScreen.setVisible(true);
        
    }
    
    private void removeIconClicked(ActionEvent evt) {
        
        String categoryClicked = ((MenuItem)evt.getSource()).categoryName;
        
        String message = "Are you sure you want to remove category " + categoryClicked + 
                         " ?";
        
        int dialogResult = JOptionPane.showConfirmDialog(this, 
                message, "Warning", JOptionPane.YES_NO_OPTION, 
                JOptionPane.WARNING_MESSAGE);
        
        if(dialogResult == JOptionPane.YES_OPTION){
            
            String sqlCommand = String.format("DELETE from setup WHERE category "
                                            + "= \'%s\'", categoryClicked);
            this.database.removeRecords(sqlCommand);
            
            sqlCommand = String.format("DELETE from words WHERE category "
                                            + "= \'%s\'", categoryClicked);
            this.database.removeRecords(sqlCommand);
            
            this.getDataFromDatabase();
            this.drawBoxes();
        }
    }
    
    private void popUpMenuAction(MouseEvent evt, String categoryName, 
                                 String srcLanguage, String targetLanguage){
    
        this.menu = new JPopupMenu();
        
//        JMenuItem renameItem = new MenuItem(categoryName, srcLanguage, 
//                                            targetLanguage);
//        renameItem.setText("rename");
//        renameItem.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                System.out.println("Rename clicked...");
//            }
//        });
        
        JMenuItem removeItem = new MenuItem(categoryName, srcLanguage, 
                                            targetLanguage);
        removeItem.setText("remove");
        removeItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeIconClicked(e);
            }
        });
        
        JMenuItem editItem = new MenuItem(categoryName, srcLanguage, 
                                          targetLanguage);
        editItem.setText("edit");
        editItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editItemClicked(e);
            }
        });
        
        //this.menu.add(renameItem);
        this.menu.add(removeItem);
        this.menu.add(editItem);
        
        this.menu.show(evt.getComponent(), evt.getX(), evt.getY());
        
    }
    
    private void editItemClicked(ActionEvent evt) {
    
        System.out.println("Edit clicked, action perfomed...");
        //System.out.println(((Box)((JLabel)((JPopupMenu)((JMenuItem)evt.getSource()).getParent()).getInvoker()).getParent()).categoryId);
        String categoryName = ((MenuItem)evt.getSource()).categoryName;
        String srcLanguage = ((MenuItem)evt.getSource()).srcLanguage;
        String targetLanguage = ((MenuItem)evt.getSource()).targetLanguage;
        
        CategoryEdit newCat = new CategoryEdit(categoryName, srcLanguage, 
                                               targetLanguage);
        newCat.setLocationRelativeTo(this);
        this.dispose();
        newCat.setVisible(true);
        
    }
    
    private void addCatButtActionPerformed(ActionEvent evt) {
        
        CategoryEdit newCat = new CategoryEdit();
        newCat.setLocationRelativeTo(this);
        this.dispose();
        newCat.setVisible(true);
        
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
    
    private class Box extends JPanel {
    
        public Box(String category, String srcLanguage, String targetLanguage) {
            super();
            this.categoryId = category;
            this.srcLanguage = srcLanguage;
            this.targetLanguage = targetLanguage;
        }
        
        protected String categoryId;
        protected String srcLanguage;
        protected String targetLanguage;
    }
    
    private class MenuItem extends JMenuItem {
    
        public MenuItem(String categoryName, String srcLanguage, 
                        String targetLanguage) {
            super();
            this.categoryName = categoryName;
            this.srcLanguage = srcLanguage;
            this.targetLanguage = targetLanguage;
        }
        
        protected String categoryName;
        protected String srcLanguage;
        protected String targetLanguage;
    
    } 
    
    protected DataBase database;
    private java.util.List<Map<String, Object>> setupData;
    protected java.util.List<Map<String, Object>> organizedData;
    
    // For purpose when category is duplicated in database (few sets)
    protected ArrayList<String> presCategories;
    
    protected JPanel upperPanel;
    protected JScrollPane centerScrollPanel;
    protected JPanel categoryPanel;
    protected JPanel lowerPanel;
    
    private JLabel catIntroLab;
    private ArrayList<Box> boxes;
    private JButton addCatButt;
    
    private JPopupMenu menu;
    
}
