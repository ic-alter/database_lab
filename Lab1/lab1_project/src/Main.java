import database.MyConnection;
import database.Table;

public class Main {
    public static void main(String[] args) {
        System.out.println("database lab1");
        MyConnection myConnection = new MyConnection();
        myConnection.connect();
        Table testTable = new Table(myConnection.getConnection(),"testMain","id,username,password");
        testTable.createTable();
        testTable.insertTuple("1,倪马旺,fjepwoie3jiEHUI2");
        myConnection.close();
    }
}
