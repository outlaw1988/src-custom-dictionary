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
        String url = "jdbc:sqlite:sqlite/database2.db";
        
        // SQL statements
        String sql = "CREATE TABLE IF NOT EXISTS setup (\n"
                + " id integer PRIMARY KEY,\n"
                + " category text NOT NULL,\n"
                + " setName text,\n"
                + " language1 text,\n"
                + " language2 text,\n"
                + " lastResult integer,\n"
                + " bestResult integer\n"
                + ");";
        
        String sql2 = "CREATE TABLE IF NOT EXISTS words (\n"
                + " id integer PRIMARY KEY,\n"
                + " category text NOT NULL,\n"
                + " setName text,\n"
                + " word1 text,\n"
                + " word2 text\n"
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
        String url = "jdbc:sqlite:sqlite/database2.db";
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }
    
    public void updateResult(int result, String setName) {
    
        String sql = String.format("UPDATE setup SET lastResult = %d WHERE "
                + "setName = \'%s\'", result, setName);
 
        try (Connection conn = this.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        
        String sqlGetBestResult = String.format("SELECT bestResult FROM setup "
                + "WHERE setName = \'%s\'", setName);
        
        int bestResult = 0;
        
        try (Connection conn = this.connect();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sqlGetBestResult)) {
            
            while (rs.next()) {
                bestResult = rs.getInt("bestResult");
            }
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        
        if (result > bestResult) {
        
            bestResult = result;
            String sqlUpdateBestResult = String.format("UPDATE setup SET bestResult = %d WHERE "
                + "setName = \'%s\'", bestResult, setName);
            
            try (Connection conn = this.connect();
                PreparedStatement pstmt = conn.prepareStatement(sqlUpdateBestResult)) {
                pstmt.executeUpdate();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        
        }
 
    }
    
    public void getDataFromSetup() {
        String sql = "SELECT id, setName, language1, language2 FROM setup";

        try (Connection conn = this.connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    
    public void insertToWord(String category, String setName, String word1, 
                             String word2) {
        String sql = "INSERT INTO words(category,setName,word1,word2) VALUES(?,?,?,?)";
 
        try (Connection conn = this.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, category);
            pstmt.setString(2, setName);
            pstmt.setString(3, word1);
            pstmt.setString(4, word2);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    
    public void insertToSetup(String category, String setName, String language1, 
                              String language2) {
        
        String sql = "INSERT INTO setup(category,setName,language1,language2,"
                + "lastResult,bestResult) VALUES(?,?,?,?,?,?)";
 
        try (Connection conn = this.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, category);
            pstmt.setString(2, setName);
            pstmt.setString(3, language1);
            pstmt.setString(4, language2);
            // add zeros as result in initialization
            pstmt.setInt(5, 0);
            pstmt.setInt(6, 0);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    
    public void getDataFromWords(String setName) throws SQLException {
        String sql = String.format("SELECT id, setName, word1, word2 FROM words "
                + "WHERE setName LIKE \'%s\' ", setName);
        
        try (Connection conn = this.connect();
            Statement stmt = conn.createStatement();
            ResultSet rset = stmt.executeQuery(sql)) {
            
            while (rset.next()) {
                words1.add(rset.getString("word1"));
                words2.add(rset.getString("word2"));
            }
   
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
    
    public ArrayList<String> getWords1() {
        return this.words1;
    }
    
    public ArrayList<String> getWords2() {
        return this.words2;
    }
    
    // TODO always use this function is case of getting data from db
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
    
    //private ResultSet rs;
    private ArrayList<String> words1 = new ArrayList<String>();
    private ArrayList<String> words2 = new ArrayList<String>();
    
}
