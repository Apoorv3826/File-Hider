package dao;

import db.Conn;
import model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {
    public static boolean isExist(String email) throws SQLException {
        Connection connection = Conn.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("select email from users");
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()){
            String e = resultSet.getString(1);
            if (e.equals(email)){
                return true;
            }
        }
        return false;
    }

    public static int saveUser(User user) throws SQLException {
        Connection connection = Conn.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("insert into users values(default, ? , ?)");
        preparedStatement.setString(1,user.getName());
        preparedStatement.setString(2, user.getEmail());
        return preparedStatement.executeUpdate();
    }
}
