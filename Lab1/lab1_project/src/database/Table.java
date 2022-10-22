package database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Table {
    private final String table_name;
    private List<String> columns;
    private final MyConnection connection;

    public Table(MyConnection connection, String table_name , String columns_sentence) {
        this.connection = connection;
        this.table_name = table_name;
        try {
            columns = String2List.string2List(columns_sentence);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*private void statement_execute(String sql) throws SQLException {
        Statement statement = connection.createStatement();
        statement.execute(sql);
        statement.close();
    }*/

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
            connection.statement_execute(sb.toString());
        } catch (SQLException e) {
            System.out.println("Table.createTable:建表失败");
            e.printStackTrace();
        }
    }

    private String csv_sentence_to_sql_tuple(String sentence) throws Exception {
        List<String> tuple = new ArrayList<>();
        tuple = String2List.string2List(sentence);
        if(tuple.size()!= columns.size()){
            throw new Exception("列数与数据库格式不符");
        }
        /*StringBuilder sb_column = new StringBuilder();
        for (String column:columns){
            sb_column.append(column).append(",");
        }
        sb_column.deleteCharAt(sb_column.length()-1);*/

        StringBuilder sb_tuple = new StringBuilder();
        sb_tuple.append("(");
        for(String value:tuple){
            sb_tuple.append("'").append(value).append("',");
        }
        sb_tuple.deleteCharAt(sb_tuple.length()-1);
        sb_tuple.append(")");
        return sb_tuple.toString();
    }

    /*public void insertTuple(String sentence) {
        String insert_sql = "INSERT INTO " +table_name +  " VALUES "+ csv_sentence_to_sql_tuple(sentence)+ ";";
        try {
            statement_execute(insert_sql);
        } catch (SQLException e) {
            System.out.println("Table.insertTuple:插入失败");
            e.printStackTrace();
        }
    }*/
    public void insertAll(List<String> sentences,int start_index) {
        //stringBuilder最大长度65535,所以不能一次拉太长的字符串。拉1000个差不多了。
        final int step = 1000;
        int max_index = sentences.size()-1;
        int start = start_index, end = start_index-1;
        while (end<max_index){
            start = end + 1;
            end = end + step;
            StringBuilder sb = new StringBuilder();
            sb.append("INSERT INTO ").append(table_name).append(" VALUES ");
            for (int i=start; i<= Math.min(end,max_index); i++){
                try {
                    sb.append(csv_sentence_to_sql_tuple(sentences.get(i))).append(",");
                } catch (Exception e) {
                    System.out.println("文件第" + i + "行数据格式出现问题，请检查");
                    e.printStackTrace();
                }
            }
            sb.deleteCharAt(sb.length()-1);
            sb.append(";");
            System.out.println(sb);
            try {
                connection.statement_execute(sb.toString());
            } catch (SQLException e) {
                System.out.println("Table.insertTuple:插入失败");
                e.printStackTrace();
            }
        }
        /*StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO ").append(table_name).append(" VALUES ");
        for (int i=start_index; i< sentences.size(); i++){
            sb.append(csv_sentence_to_sql_tuple(sentences.get(i))).append(",");
        }
        sb.deleteCharAt(sb.length()-1);
        sb.append(";");
        System.out.println(sb);
        try {
            statement_execute(sb.toString());
        } catch (SQLException e) {
            System.out.println("Table.insertTuple:插入失败");
            e.printStackTrace();
        }*/
    }
}
