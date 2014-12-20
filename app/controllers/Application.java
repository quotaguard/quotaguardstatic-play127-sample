package controllers;

import play.*;
import play.mvc.*;
import play.db.*;
import java.sql.*;
import java.util.*;

import helpers.*;
import models.*;
import java.net.Authenticator;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.zip.GZIPInputStream;

public class Application extends Controller {

    public static void index() {
      String protectedResult = protectedDBTest();
      String publicResult = publicDBTest();
      String apiResult = publicAPITest();
      render();
    }

    public static String protectedDBTest(){      
     Connection conn = null;
     Statement stmt = null;
     try{
        //STEP 2: Register JDBC driver
        Class.forName("org.postgresql.Driver");
        String database = "database";
        String DB_URL="jdbc:postgresql://host:port/"+database+"?ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory";
        String USER = "username";
        String PASS="password";
        //STEP 3: Open a connection
        System.out.println("Connecting to database...");
        conn = DriverManager.getConnection(DB_URL,USER,PASS);
        //STEP 4: Execute a query
        stmt = conn.createStatement();
        String sql;
        sql = " SELECT client_addr FROM pg_stat_activity where client_addr is not null and datname='"+database+"' limit 10;";
        ResultSet rs = stmt.executeQuery(sql);
         while(rs.next()){
           //Retrieve by column name
           String res = rs.getString("client_addr");
           System.out.println("Result from PROTECTED DB is: "+res);
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
      return "";
    }

    public static String publicDBTest({
      Connection conn = null;
      Statement stmt = null;
      try{
        //STEP 2: Register JDBC driver
        Class.forName("org.postgresql.Driver");
        String database="database";
        String DB_URL="jdbc:postgresql://host:5432/"+database+"?ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory";
        String USER = "username";
        String PASS="password";
        //STEP 3: Open a connection
        System.out.println("Connecting to database...");
        conn = DriverManager.getConnection(DB_URL,USER,PASS);
        //STEP 4: Execute a query
        stmt = conn.createStatement();
        String sql;
        sql = "SELECT client_addr FROM pg_stat_activity where client_addr is not null and datname='"+database+"' limit 10;";
        ResultSet rs = stmt.executeQuery(sql);
         while(rs.next()){
           //Retrieve by column name
           String res = rs.getString("client_addr");
           System.out.println("Result from PUBLIC DB is: "+res);
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
   return "";
    }

    public static String publicAPITest(){
      String urlToRead =  "http://ip.jsontest.com/";
        String result = "";
        try {
         URL url = new URL(urlToRead);
           HttpURLConnection conn = (HttpURLConnection) url.openConnection();
           QuotaGuardProxyAuthenticator proxy = new QuotaGuardProxyAuthenticator();
           conn.setRequestProperty("Proxy-Authorization", "Basic " + proxy.getEncodedAuth());
           conn.setRequestProperty("Accept-Encoding", "gzip");
           conn.setRequestMethod("GET");
           InputStream is = conn.getInputStream();
           if(conn.getContentEncoding()!=null && conn.getContentEncoding().equalsIgnoreCase("gzip")){
             is = new GZIPInputStream(is);
           }
           byte[] buffer = new byte[1024];
           int len;
           ByteArrayOutputStream bos = new ByteArrayOutputStream();
           while (-1 != (len = is.read(buffer))) {
             bos.write(buffer, 0, len);
           }           
           result = new String(bos.toByteArray());
           is.close();
        } catch (IOException e) {
           e.printStackTrace();
        } catch (Exception e) {
           e.printStackTrace();
        }
        System.out.println("HTTP Request originated from: "+ result);
    return "";
    }
}
