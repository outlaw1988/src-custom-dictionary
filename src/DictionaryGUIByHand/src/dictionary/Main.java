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
        int frameWidth = 770;
        int frameHeight = 550;
        
        // Upper panel
        this.upperPanel = new JPanel();
        this.upperPanel.setLayout(new GridLayout(1, 1));
        this.catIntroLab = new JLabel("Categories");
        this.catIntroLab.setFont(new Font("Dialog", 1, 18));
        this.catIntroLab.setHorizontalAlignment(SwingConstants.CENTER);
        this.upperPanel.add(this.catIntroLab);
        
        System.out.println("Organized data" + this.organizedData);
        // Center panel
        this.categoryPanel = new JPanel();
        this.categoryPanel.setLayout(new GridBagLayout());
        
        this.drawBoxes();
        
        this.centerScrollPanel = new JScrollPane(this.categoryPanel, 
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, 
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        // increase speed of scrolling
        this.centerScrollPanel.getVerticalScrollBar().setUnitIncrement(16);

        // int boxesNum = 30;
        // TODO Change, now it's for testing purposes
//        for(int i = 0; i < boxesNum; i++) {
//            GridBagConstraints gc = new GridBagConstraints();
//            gc.anchor = GridBagConstraints.LAST_LINE_END;
//            gc.insets = new Insets(10, 10, 10, 10);
//            gc.gridx = i % boxInRow;
//            if ((i % boxInRow) == 0) gc.gridy = ++gridy;
//            JPanel box = new JPanel();
//            box.setPreferredSize(new Dimension(200, 100));
//            box.setBorder(BorderFactory.createLineBorder(Color.black));
//            JLabel boxLabel = new JLabel("Box test message " + i);
//            box.add(boxLabel);
//            categoryPanel.add(box, gc);
//        }

        // Lower panel
        this.lowerPanel = new JPanel();
        this.lowerPanel.setLayout(new GridLayout(1, 1));
        this.addCatButt = new JButton("Add new category");
        this.addCatButt.setFont(new Font("Dialog", 1, 14));
        this.addCatButt.setHorizontalAlignment(SwingConstants.CENTER);
        this.addCatButt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                addActionListenerActionPerformed(evt);
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
            //gc.anchor = GridBagConstraints.LAST_LINE_END;
            gc.insets = new Insets(10, 10, 10, 10);
            gc.gridx = i % boxInRow;
            if ((i % boxInRow) == 0) gc.gridy = gridy++;
            
            String category = (String)this.organizedData.get(i).get("category");
            Box box = new Box(category);
            box.setLayout(new GridLayout(0, 1));
            
            box.setPreferredSize(new Dimension(200, 100));
            box.setBorder(BorderFactory.createLineBorder(Color.black));
            JLabel boxLabel = new JLabel(category);
            boxLabel.setFont(new Font("Dialog", Font.BOLD, 16));
            
            JLabel setsNumDescrLab = new JLabel("Sets number:");
            JLabel setsNum = new JLabel(this.organizedData.get(i).get("setsNum")
                    .toString());
            
            JLabel wordsNumDescrLab = new JLabel("Words number:");
            JLabel wordsNum = new JLabel(this.organizedData.get(i).get("wordsNum")
                    .toString());
            
            BufferedImage img = null;
            
            try {
                img = ImageIO.read(new File("../../assets/remove_icon_res.png"));
            } catch (IOException e) {
                e.printStackTrace();
            }

//            Image dimg = img.getScaledInstance(25, 25, Image.SCALE_SMOOTH);
            ImageIcon imageIcon = new ImageIcon(img);
            JLabel removeIcon = new JLabel(imageIcon);
            
            box.add(boxLabel);
            box.add(setsNumDescrLab);
            box.add(setsNum);
            box.add(wordsNumDescrLab);
            box.add(wordsNum);
            box.add(removeIcon);
            
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
        sqlCommand = "SELECT * FROM words";
        this.wordsData = this.database.getData(sqlCommand);
        this.createOrganizedData();
        
    }
    
    private void createOrganizedData() {
    
        this.organizedData = new ArrayList<>();
        Map<String, Object> row;
        // For purpose when category is duplicated in database (few sets)
        ArrayList<String> presCategories = new ArrayList<>();
        
        for (int i = 0; i < this.setupData.size(); i++) {
            row = new HashMap<>();
            String category = (String)this.setupData.get(i).get("category");
            
            if (presCategories.contains(category)) continue;
            else presCategories.add(category);
            
            row.put("category", category);
            row.put("setsNum", getNum(category, this.setupData));
            row.put("wordsNum", getNum(category, this.wordsData));
            row.put("language1", (String)this.setupData.get(i).get("language1"));
            row.put("language2", (String)this.setupData.get(i).get("language2"));
            this.organizedData.add(row);
        }
    }
    
    private int getNum(String categoryName, 
                       java.util.List<Map<String, Object>> data) {
    
        int counter = 0;
        
        for (Map<String, Object> dataItem : data) {
            String category = (String)dataItem.get("category");
            String setName = (String)dataItem.get("setName");
            if (category.equals(categoryName) && setName != null) counter++;
        }
        
        return counter;
    }
    
    private void addActionListenerActionPerformed(ActionEvent evt) {
        
        String message = "Please, enter category name:";
        String text = JOptionPane.showInputDialog(this, message);
        
        if (text != null) {
            this.database.insertToSetup(text, null, null, null);
            this.getDataFromDatabase();
            this.drawBoxes();
        }
        
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
    
        public Box(String category) {
            super();
            this.categoryId = category;
        }
        
        String categoryId;
        int setsNum;
        int wordsNum;
        String language1;
        String language2;
        
    }
    
    private DataBase database;
    java.util.List<Map<String, Object>> setupData;
    java.util.List<Map<String, Object>> wordsData;
    java.util.List<Map<String, Object>> organizedData;
    
    private JPanel upperPanel;
    private JScrollPane centerScrollPanel;
    private JPanel categoryPanel;
    private JPanel lowerPanel;
    
    private JLabel catIntroLab;
    private ArrayList<Box> boxes;
    private JButton addCatButt;
    
}
