package dao;

import db.Conn;
import model.Data;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DataDAO {
    public static List<Data> getAllFiles(String email) throws SQLException {
        Connection connection = Conn.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("select * from data where email = ?");
        preparedStatement.setString(1, email);
        ResultSet resultSet = preparedStatement.executeQuery();
        List<Data> files = new ArrayList<>();
        while (resultSet.next()){
            int id  = resultSet.getInt(1);
            String name = resultSet.getString(2);
            String path = resultSet.getString(3);
            files.add(new Data(id, name, path));
        }
        return files;
    }

    public static int hideFile(Data file) throws SQLException, IOException {
        Connection connection = Conn.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement
                ("insert into data(name, path, email, bin_data) values(?,?,?,?)");
        preparedStatement.setString(1,file.getFileName());
        preparedStatement.setString(2,file.getPath());
        preparedStatement.setString(3, file.getEmail());
        File f = new File(file.getPath());
        FileReader fileReader = new FileReader(f);
        preparedStatement.setCharacterStream(4,fileReader,f.length());
        int ans = preparedStatement.executeUpdate();
        fileReader.close();
        f.delete();
        return ans;
    }

    public static void unhide(int id) throws SQLException, IOException {
        Connection connection = Conn.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement
                ("select path, bin_data from data where id = ?");
        preparedStatement.setInt(1,id);
        ResultSet rs = preparedStatement.executeQuery();
        rs.next();

        String path = rs.getString("path");
        Clob clob = rs.getClob("bin_data");

        Reader reader = clob.getCharacterStream();
        FileWriter fileWriter = new FileWriter(path);

        int i;
        while((i = reader.read()) != -1){
            fileWriter.write((char)i);
        }
        fileWriter.close();
        preparedStatement = connection.prepareStatement("delete from data where id = ?");
        preparedStatement.setInt(1,id);
        preparedStatement.executeUpdate();
    }
}
