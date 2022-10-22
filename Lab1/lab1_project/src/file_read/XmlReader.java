package file_read;

import file_read.xmlInf_store.CsvFileInf;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class XmlReader {
    private HashMap<String,String> DatabaseInf;
    private List<CsvFileInf> csvFileInfList;
    public XmlReader(){
        DatabaseInf = new HashMap<>();
        csvFileInfList = new ArrayList<>();
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try{
            DocumentBuilder db = dbf.newDocumentBuilder();
            InputStream is = new FileInputStream(new File(System.getProperty("user.dir")+"\\src\\config\\config.xml"));
            Document document = db.parse(is);
            //解析数据库配置
            NodeList database = document.getElementsByTagName("database");
            NodeList database_childNodes = database.item(0).getChildNodes();
            for (int k = 0;k<database_childNodes.getLength();k++){
                if (database_childNodes.item(k).getNodeType() == Node.ELEMENT_NODE){
                    DatabaseInf.put(database_childNodes.item(k).getNodeName(),database_childNodes.item(k).getFirstChild().getNodeValue()) ;
                }
            }
            //解析csv文件
            NodeList csvFiles = document.getElementsByTagName("csv_file");
            for (int i=0;i<csvFiles.getLength();i++){
                NodeList csv_childNodes = csvFiles.item(i).getChildNodes();
                String csv_url="",table_name="";
                for(int j=0;j<csv_childNodes.getLength();j++){
                    if (csv_childNodes.item(j).getNodeType() == Node.ELEMENT_NODE){
                        if (csv_childNodes.item(j).getNodeName().equals("url")){
                            csv_url = csv_childNodes.item(j).getFirstChild().getNodeValue();
                        }
                        if (csv_childNodes.item(j).getNodeName().equals("table_name")){
                            table_name = csv_childNodes.item(j).getFirstChild().getNodeValue();
                        }
                    }
                }
                csvFileInfList.add(new CsvFileInf(csv_url,table_name));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public HashMap<String, String> getDatabaseInf() {
        return DatabaseInf;
    }

    public List<CsvFileInf> getCsvFileInfList() {
        return csvFileInfList;
    }
}
