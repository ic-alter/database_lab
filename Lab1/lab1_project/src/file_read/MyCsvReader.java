package file_read;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class MyCsvReader {
    private final String filePath;

    public MyCsvReader(String filePath) {
        this.filePath = filePath;
    }

    public List<String> read() throws IOException {
        File file = new File(filePath);
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
        String line = null;
        List<String> res = new ArrayList<>();
        while ((line=br.readLine())!=null){
            res.add(line);
        }
        br.close();
        return res;
    }
}
