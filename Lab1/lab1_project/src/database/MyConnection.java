package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MyConnection {
    private Connection connection;
    private final String db_name;
    private final String username;
    private final String password;

    public MyConnection(String db_name, String username, String password) {
        this.db_name = db_name;
        this.username = username;
        this.password = password;
    }
    public MyConnection(){
        db_name = "database_lab";
        username = "root";
        password = "12345678";
    }

    public void connect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/"+db_name,username,password);
            System.out.println("connection is "+ connection);
        } catch (SQLException e) {
            System.out.println("MyConnection.connect():数据库连接失败");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection(){
        return connection;
    }

    public void close(){
        try {
            connection.close();
        } catch (SQLException e) {
            System.out.println("MyConnection.close():数据库关闭失败");
            e.printStackTrace();
        }
    }
}
