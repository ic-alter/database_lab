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

    private String csv_sentence_to_sql_tuple(String sentence){
        List<String> tuple = new ArrayList<>();
        tuple = String2List.string2List(sentence);
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
        if (tuple.size() < columns.size()){
            for (int i = 0;i< columns.size()-tuple.size();i++){
                sb_tuple.append("'',");
            }
        }
        sb_tuple.deleteCharAt(sb_tuple.length()-1);
        sb_tuple.append(")");
        return sb_tuple.toString();
    }

    public void insertTuple(String sentence){

        //String insert_sql = "INSERT INTO " +table_name + " ("+sb_column.toString() + ") VALUES ("+ sb_tuple.toString()+ ");";
        String insert_sql = "INSERT INTO " +table_name +  " VALUES "+ csv_sentence_to_sql_tuple(sentence)+ ";";
        //System.out.println(insert_sql);
        try {
            statement_execute(insert_sql);
        } catch (SQLException e) {
            System.out.println("Table.insertTuple:插入失败");
            e.printStackTrace();
        }
    }
    public void insertAll(List<String> sentences,int start_index){
        //stringBuilder最大长度65535,所以不能一次拉太长的字符串。拉500个差不多了。
        final int step = 500;
        int max_index = sentences.size()-1;
        int start = start_index, end = start_index-1;
        while (end<max_index){
            start = end + 1;
            end = end + step;
            StringBuilder sb = new StringBuilder();
            sb.append("INSERT INTO ").append(table_name).append(" VALUES ");
            for (int i=start; i<= Math.min(end,max_index); i++){
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
