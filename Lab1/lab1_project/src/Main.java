import database.MyConnection;
import database.Table;
import file_read.MyCsvReader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("database lab1");
        String project_path = System.getProperty("user.dir");
        MyConnection myConnection = new MyConnection();
        myConnection.connect();

        //读room表
        MyCsvReader myRoomReader = new MyCsvReader(project_path+"\\src\\data_source\\room.csv");
        List<String> roomData = new ArrayList<>();
        try {
            roomData = myRoomReader.read();
        } catch (IOException e) {
            System.out.println("读文件失败");
            e.printStackTrace();
        }

        Table roomTable = new Table(myConnection.getConnection(),"room",roomData.get(0));
        roomTable.createTable();
        roomTable.insertAll(roomData,1);

        //读student表
        MyCsvReader studentReader = new MyCsvReader(project_path+"\\src\\data_source\\student.csv");
        List<String> studentData = new ArrayList<>();
        try {
            studentData = studentReader.read();
        } catch (IOException e) {
            System.out.println("读文件失败");
            e.printStackTrace();
        }

        Table studentTable = new Table(myConnection.getConnection(),"student",roomData.get(0));
        studentTable.createTable();
        studentTable.insertAll(studentData,1);


        myConnection.close();


    }
}
