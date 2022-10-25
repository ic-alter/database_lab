package file_read.xmlInf_store;

public class CsvFileInf {
    private String url;
    private String table_name;
    private String primary_key;

    public CsvFileInf(String url, String table_name,String primary_key) {
        this.url = url;
        this.table_name = table_name;
        this.primary_key = primary_key;
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

    public String getPrimary_key() {
        return primary_key;
    }

    public void setPrimary_key(String primary_key) {
        this.primary_key = primary_key;
    }
}
