/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dictionary;

import java.sql.*;

/**
 *
 * @author jakub
 */
public class DataBaseTest {
    
    public static void createNewDatabase(String fileName) {
        
        // Connection string
        String url = "jdbc:sqlite:sqlite/" + fileName;
        
        try (Connection conn = DriverManager.getConnection(url)) {
            
            if (conn != null) {
                DatabaseMetaData meta = conn.getMetaData();
                
                System.out.println("The driver name is: " + meta.getDriverName());
                System.out.println("A new database has been created");
            }
        
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    
    public static void createNewTable() {
    
        // Connection string
        String url = "jdbc:sqlite:sqlite/test.db";
        
        // SQL statement
        String sql = "CREATE TABLE IF NOT EXISTS warehouses (\n"
                + " id integer PRIMARY KEY,\n"
                + " name text NOT NULL,\n"
                + " capacity real\n"
                + ");";
        
        try (Connection conn = DriverManager.getConnection(url); 
            Statement stmt = conn.createStatement()) {
            
            stmt.execute(sql);
        
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        
    }
    
    private Connection connect() {
        // SQLite connection string
        String url = "jdbc:sqlite:sqlite/test.db";
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }
    
    public void insert(String name, double capacity) {
        String sql = "INSERT INTO warehouses(name,capacity) VALUES(?,?)";
 
        try (Connection conn = this.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setDouble(2, capacity);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    
    public static void main(String[] args) {
        
        createNewDatabase("database.db");
        //createNewTable();
//        DataBaseTest db = new DataBaseTest();
//        db.insert("Raw materials", 3000);
//        db.insert("Semifinished Goods", 4000);
//        db.insert("Finished Goods", 5000);
    
    }
    
}
