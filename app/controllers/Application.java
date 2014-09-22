package controllers;

import play.*;
import play.mvc.*;
import play.db.*;
import java.sql.*;
import java.util.*;

import models.*;
import java.net.Authenticator;

public class Application extends Controller {

    public static void index() {

        // access "default" database
   Connection conn = null;
   Statement stmt = null;
   try{
      //STEP 2: Register JDBC driver
      Class.forName("org.postgresql.Driver");
      String DB_URL="jdbc:postgresql://54.235.245.180:5432/d50vt48u98fijd?ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory";
	  String USER = "lvrdcwczyzpzjb";
	  String PASS="y1dkkfMg9WP2GA9z1a3GFLSvB9";
      //STEP 3: Open a connection
      System.out.println("Connecting to database...");
      conn = DriverManager.getConnection(DB_URL,USER,PASS);
	  //conn = DB.getConnection();
      //STEP 4: Execute a query
      System.out.println("Creating statement...");
      stmt = conn.createStatement();
      String sql;
      sql = "SELECT 1+1 as answer";
      ResultSet rs = stmt.executeQuery(sql);
       while(rs.next()){
         //Retrieve by column name
         String res = rs.getString("answer");
         System.out.println("Result from db is: "+res);
      }
	 rs.close();
      stmt.close();
      conn.close();
   }catch(SQLException se){
      //Handle errors for JDBC
      se.printStackTrace();
   }catch(Exception e){
      //Handle errors for Class.forName
      e.printStackTrace();
   }finally{
      //finally block used to close resources
      try{
         if(stmt!=null)
            stmt.close();
      }catch(SQLException se2){
      }// nothing we can do
      try{
         if(conn!=null)
            conn.close();
      }catch(SQLException se){
         se.printStackTrace();
      }//end finally try
   }//end try
       render();
    }

}
