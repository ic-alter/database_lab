package file_read.xmlInf_store;

public class CsvFileInf {
    private String url;
    private String table_name;

    public CsvFileInf(String url, String table_name) {
        this.url = url;
        this.table_name = table_name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTable_name() {
        return table_name;
    }

    public void setTable_name(String table_name) {
        this.table_name = table_name;
    }
}
