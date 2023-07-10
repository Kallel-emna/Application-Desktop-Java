/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Clinimate.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author moham
 */


public class DataSource {
    private static DataSource instance;
    private Connection cnx; //cnx est le canal de communication
    private final String URL="jdbc:mysql://localhost:3306/integ";
    private final String USERNAME="root";
    private final String PASSWORD="";
    
    private DataSource(){
     
      try {
        cnx=DriverManager.getConnection(URL,USERNAME , PASSWORD);
          System.out.println("LA CONNEXION A ETE ETABLIE AVEC SUCCES");
      
      } catch (SQLException ex){
         System.out.println(ex.getMessage());
      }
    }
    public static DataSource getInstance(){
        if (instance==null)
            instance = new DataSource();
        return instance;
    
    }

    public Connection getCnx() {
        return cnx;
    }
    
}
