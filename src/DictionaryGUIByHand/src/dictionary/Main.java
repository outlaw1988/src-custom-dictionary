/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dictionary;

import javax.swing.*;
import java.awt.*;
import java.util.*;

/**
 *
 * @author jakub
 */
public class Main extends JFrame {
    
    public Main() {
        getDataFromDatabase();
        initComponents();
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
        
        // Center panel
        JPanel categoryPanel = new JPanel();
//        categoryPanel.setLayout(new GridLayout(0, 3, 20, 20));
        categoryPanel.setLayout(new FlowLayout());
        
        this.centerScrollPanel = new JScrollPane(categoryPanel, 
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, 
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        // increase speed of scrolling
        this.centerScrollPanel.getVerticalScrollBar().setUnitIncrement(16);
        
//        int boxesNum = 6;
//        // TODO Change, now it's for testing purposes
//        for(int i = 0; i < boxesNum; i++) {
//            JPanel box = new JPanel();
//            box.setPreferredSize(new Dimension(150, 100));
//            box.setBorder(BorderFactory.createLineBorder(Color.black));
//            JLabel boxLabel = new JLabel("Box test message");
//            box.add(boxLabel);
//            categoryPanel.add(box);
//        }

        for (int i = 0; i < this.organizedData.size(); i++) {
            // TODO one category displays few times when few sets present
            String category = (String)this.organizedData.get(i).get("category");
            Box box = new Box(category);
            box.setSize(new Dimension(150, 100));
            //box.set
            box.setBorder(BorderFactory.createLineBorder(Color.black));
            JLabel boxLabel = new JLabel(category);
            box.add(boxLabel);
            categoryPanel.add(box);
        }
        
        // Lower panel
        this.lowerPanel = new JPanel();
        this.lowerPanel.setLayout(new GridLayout(1, 1));
        this.addCatButt = new JButton("Add new category");
        this.addCatButt.setFont(new Font("Dialog", 1, 14));
        this.addCatButt.setHorizontalAlignment(SwingConstants.CENTER);
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
    
    private void getDataFromDatabase() {
    
        this.database = new DataBase();
        String sqlCommand = "SELECT * FROM setup";
        this.setupData = this.database.getData(sqlCommand);
        sqlCommand = "SELECT * FROM words";
        this.wordsData = this.database.getData(sqlCommand);
        //System.out.println(response.get(0).get("lastResult"));
        this.createOrganizedData();
        
    }
    
    private void createOrganizedData() {
    
        this.organizedData = new ArrayList<>();
        Map<String, Object> row;
        
        for (int i = 0; i < this.setupData.size(); i++) {
            row = new HashMap<>();
            String category = (String)this.setupData.get(i).get("category");
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
            if (category.equals(categoryName)) counter++;
        }
        
        return counter;
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
    private JPanel lowerPanel;
    
    private JLabel catIntroLab;
    private ArrayList<Box> boxes;
    private JButton addCatButt;
    
}
