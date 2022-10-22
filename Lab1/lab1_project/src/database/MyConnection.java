package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class MyConnection {
    private Connection connection;
    private final String drive;
    private final String url;
    private final String username;
    private final String password;

    public MyConnection(String drive,String url, String username, String password) {
        this.drive = drive;
        this.url = url;
        this.username = username;
        this.password = password;
    }

    public void connect() {
        try {
            Class.forName(drive);
            connection = DriverManager.getConnection(url,username,password);
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

    public void statement_execute(String sql) throws SQLException {
        Statement statement = connection.createStatement();
        statement.execute(sql);
        statement.close();
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
