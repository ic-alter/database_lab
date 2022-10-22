import database.MyConnection;
import database.Table;
import file_read.MyCsvReader;
import file_read.XmlReader;
import file_read.xmlInf_store.CsvFileInf;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("database lab1");
        XmlReader xmlReader = new XmlReader();
        HashMap<String,String> databaseInf = xmlReader.getDatabaseInf();
        //System.out.println(databaseInf.get("drive")+databaseInf.get("url")+databaseInf.get("username")+databaseInf.get("password"));
        MyConnection myConnection = new MyConnection(databaseInf.get("drive"),databaseInf.get("url"),databaseInf.get("username"),databaseInf.get("password"));
        myConnection.connect();

        //读csv
        List<CsvFileInf> csvFileInfList = xmlReader.getCsvFileInfList();
        for (int i=0;i<csvFileInfList.size();i++){
            MyCsvReader myCsvReader = new MyCsvReader(csvFileInfList.get(i).getUrl());
            List<String> csvData = new ArrayList<>();
            try {
                csvData = myCsvReader.read();
            }catch (IOException e){
                e.printStackTrace();
            }
            Table table = new Table(myConnection.getConnection(),csvFileInfList.get(i).getTable_name(), csvData.get(0));
            table.createTable();
            table.insertAll(csvData,1);
        }

        /*
        String project_path = System.getProperty("user.dir");
        MyCsvReader myRoomReader = new MyCsvReader(project_path+"\\data_source\\room.csv");
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

        MyCsvReader studentReader = new MyCsvReader(project_path+"\\data_source\\student.csv");
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
        */

        myConnection.close();


    }
}
