package database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class Table {
    private final String table_name;
    private List<String> columns;
    private List<String> types;
    private final MyConnection connection;

    public Table(MyConnection connection, String table_name , String columns_sentence,String types_sentence) {
        this.connection = connection;
        this.table_name = table_name;
        try {
            columns = String2List.string2List(columns_sentence);
            types = String2List.string2List(types_sentence);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*private void statement_execute(String sql) throws SQLException {
        Statement statement = connection.createStatement();
        statement.execute(sql);
        statement.close();
    }*/

    public void createTable() throws Exception{
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE IF NOT EXISTS ").append(table_name).append(" (");
        if(columns.size()!=types.size()){
            throw new Exception("列名与格式未能一一对应");
        }
        try{
            type_valid();
        }catch (NumberFormatException e){
            throw new Exception("括号内应当为数字");
        }
        for (int i=0;i<columns.size();i++){
            sb.append(columns.get(i)).append(" ").append(types.get(i)).append(",");
        }

        sb.deleteCharAt(sb.length()-1);//删除最后多的逗号
        sb.append(");");
        System.out.println(sb);
        try {
            connection.statement_execute(sb.toString());
        } catch (SQLException e) {
            throw new Exception("未能成功创建表");
        }
    }

    private void type_valid() throws Exception{
        for(String type:types){
            int brackets_index = !type.contains("(") ?type.length():type.indexOf("(");
            String type_switch = type.substring(0,brackets_index);
            switch (type_switch) {
                case "INT":
                case "DATETIME":
                    break;
                case "CHAR"://char可以加括号也可以不加，varchar必须加括号
                    int max_size = brackets_index==type.length()?1:Integer.parseInt(type.substring(brackets_index+1,type.indexOf(")")));
                    if (max_size>65532){
                        throw new Exception("CHAR最大长度不能超过65532");
                    }
                    break;
                case "VARCHAR":
                    if (brackets_index==type.length()) throw new Exception("VARCHAR类型后边必须有括号");
                    if (Integer.parseInt(type.substring(brackets_index+1,type.indexOf(")")))>65532) throw new Exception("VARCHAR最大长度不能超过65532");
                    break;
                default:
                    throw new Exception("暂不支持的数据格式");
            }
        }
    }

    private boolean type_check(String data,String type) throws Exception{
        int brackets_index = !type.contains("(") ?type.length():type.indexOf("(");
        String type_switch = type.substring(0,brackets_index);
        switch (type_switch) {
            case "INT":
                try {
                    int num = Integer.parseInt(data);
                }catch (NumberFormatException e){
                    throw new Exception("INT类型格式错误");
                }
                break;
            case "VARCHAR":
            case "CHAR"://char可以加括号也可以不加，varchar必须加括号
                int max_size = brackets_index==type.length()?1:Integer.parseInt(type.substring(brackets_index+1,type.indexOf(")")));
                if (data.length()>max_size){
                    throw new Exception("字符串过长");
                }
                break;
            case "DATETIME":
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                simpleDateFormat.setLenient(false);
                try{
                    simpleDateFormat.parse(data);
                }catch (ParseException e){
                    throw new Exception("时间格式错误");
                }
                break;
            default:
                throw new Exception("暂不支持的数据格式");
        }
        return true;
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
        for(int i = 0;i < tuple.size();i++){
            if (type_check(tuple.get(i),types.get(i))){
                sb_tuple.append("'").append(tuple.get(i)).append("',");
            }
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
                    System.out.println("文件第" + (i+1) + "行数据格式出现问题，请检查:"+e.getMessage());
                    //e.printStackTrace();
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
