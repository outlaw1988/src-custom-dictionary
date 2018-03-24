/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dictionary;

import java.sql.*;
import java.util.*;

/**
 *
 * @author jakub
 */
public class DataBase {
    
    public static void createNewTable() {
    
        // Connection string
        String url = "jdbc:sqlite:sqlite/database.db";
        
        // SQL statements
        String sql = "CREATE TABLE IF NOT EXISTS setup (\n"
                + " id integer PRIMARY KEY,\n"
                + " category text NOT NULL,\n"
                + " setName text,\n"
                + " srcLanguage text,\n"
                + " targetLanguage text,\n"
                + " targetSide text,\n"
                + " lastResult integer,\n"
                + " bestResult integer\n"
                + ");";
        
        String sql2 = "CREATE TABLE IF NOT EXISTS words (\n"
                + " id integer PRIMARY KEY,\n"
                + " category text NOT NULL,\n"
                + " setName text,\n"
                + " srcWord text,\n"
                + " targetWord text\n"
                + ");";
        
        try (Connection conn = DriverManager.getConnection(url); 
            Statement stmt = conn.createStatement()) {
            
            stmt.execute(sql);
            stmt.execute(sql2);
        
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        
    }
    
    private Connection connect() {
        // SQLite connection string
        String url = "jdbc:sqlite:sqlite/database.db";
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }
    
    public void insertToWord(String category, String setName, String srcWord, 
                             String targetWord) {
        
        String sql = "INSERT INTO words(category,setName,srcWord,targetWord) "
                   + "VALUES(?,?,?,?)";
 
        try (Connection conn = this.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, category);
            pstmt.setString(2, setName);
            pstmt.setString(3, srcWord);
            pstmt.setString(4, targetWord);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    
    // Use it as initialization
    public void insertToSetup(String category, String setName, String srcLanguage, 
                              String targetLanguage, String targetSide) {
        
        String sql = "INSERT INTO setup(category,setName,srcLanguage,targetLanguage,"
                + "targetSide,lastResult,bestResult) VALUES(?,?,?,?,?,?,?)";
 
        try (Connection conn = this.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, category);
            pstmt.setString(2, setName);
            pstmt.setString(3, srcLanguage);
            pstmt.setString(4, targetLanguage);
            pstmt.setString(5, targetSide);
            // add zeros as result in initialization
            pstmt.setInt(6, 0);
            pstmt.setInt(7, 0);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    
    public void removeRecords(String sql) {

        try (Connection conn = this.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        
    }
    
    public void updateRecords(String sql) {
 
        try (Connection conn = this.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        
    }
    
    public int countRecords(String sqlCommand) {
    
        int counter = 0;
        
        try (Connection conn = this.connect();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sqlCommand)){

            rs.next();
            counter = rs.getInt("rowcount");
            rs.close();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        
        return counter;
    }
    
    public List<Map<String, Object>> getData(String sqlCommand) {

        List<Map<String, Object>> resultList = new ArrayList<>();
        Map<String, Object> row = null;

        try (Connection conn = this.connect();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sqlCommand)){

            ResultSetMetaData metaData = rs.getMetaData();
            Integer columnCount = metaData.getColumnCount();

            while (rs.next()) {
                row = new HashMap<>();
                for (int i = 1; i <= columnCount; i++) {
                    row.put(metaData.getColumnName(i), rs.getObject(i));
                }
                resultList.add(row);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        
        return resultList;
    }
    
    public static void main(String[] args) {
        
        DataBase db = new DataBase();
        createNewTable();
//        db.insertToSetup("animals", "Polish", "English");
//        db.insertToWord("animals", "pies", "dog");
        //db.getData();
    
    }
    
}
