package database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Table {
    private final String table_name;
    private List<String> columns;
    private final Connection connection;

    public Table(Connection connection, String table_name , String columns_sentence) {
        this.connection = connection;
        this.table_name = table_name;
        columns = String2List.string2List(columns_sentence);
    }

    private void statement_execute(String sql) throws SQLException {
        Statement statement = connection.createStatement();
        statement.execute(sql);
        statement.close();
    }

    public void createTable(){
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE IF NOT EXISTS ").append(table_name).append(" (");
        for (String column:columns){
            sb.append(column).append(" VARCHAR(255),");
        }

        sb.deleteCharAt(sb.length()-1);//删除最后多的逗号
        sb.append(");");
        System.out.println(sb.toString());
        try {
            statement_execute(sb.toString());
        } catch (SQLException e) {
            System.out.println("Table.createTable:建表失败");
            e.printStackTrace();
        }
    }

    public void insertTuple(String sentence){
        List<String> tuple = new ArrayList<>();
        tuple = String2List.string2List(sentence);
        /*StringBuilder sb_column = new StringBuilder();
        for (String column:columns){
            sb_column.append(column).append(",");
        }
        sb_column.deleteCharAt(sb_column.length()-1);*/

        StringBuilder sb_tuple = new StringBuilder();
        for(String value:tuple){
            sb_tuple.append("'").append(value).append("',");
        }
        sb_tuple.deleteCharAt(sb_tuple.length()-1);
        //String insert_sql = "INSERT INTO " +table_name + " ("+sb_column.toString() + ") VALUES ("+ sb_tuple.toString()+ ");";
        String insert_sql = "INSERT INTO " +table_name +  " VALUES ("+ sb_tuple.toString()+ ");";
        System.out.println(insert_sql);
        try {
            statement_execute(insert_sql);
        } catch (SQLException e) {
            System.out.println("Table.insertTuple:插入失败");
            e.printStackTrace();
        }
    }
}
