package db;

import com.mysql.cj.jdbc.Driver;

import java.sql.*;
public class Conn {
    public static Connection connection;
    public static Connection getConnection(){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/Hider", "root" ,"apoorv");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        System.out.println("connection succesfull!");
        return connection;
    }

    public static void closedConnection(){
        if (connection != null){
            try {
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
