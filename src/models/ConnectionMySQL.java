package models;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionMySQL {
    private String database_name = "pharmacy_database";
    private String user = "root";
    private String password = "root";
    private String url = "jdbc:mysql://localhost:3306/" + database_name;
    
    Connection conn = null;
    
    public Connection getConnection(){
        try{
            //Obtension del diver de conexion
            Class.forName("com.mysql.cj.jdbc.Driver");
            //Creacion de conexion
            conn = DriverManager.getConnection(url, user, password);
            
        }catch(ClassNotFoundException e){
            System.err.println("Ocurrio un class not foun exception" + e.getMessage());
        }catch(SQLException e){
            System.err.print("Ocurrio un SQLException" + e.getMessage());
        }
        return conn;
    }
}
