package database;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class String2List {

    public static String safeString(String str){
        return str.replace("'","''");
    }
    public static List<String> string2List(String sentence) throws Exception{
        int length = sentence.length();
        int start = 0;
        List<String> res = new ArrayList<>();
        while( start < length){
            if (sentence.charAt(start) == '\"'){
                //引号处理
                int now_quotation = start+1;
                for(;;) {
                    int next_quotation = sentence.indexOf("\"",now_quotation);
                    if (next_quotation == -1){
                        throw new Exception("csv引号使用格式错误");
                    }
                    if (next_quotation >= length-1 || sentence.charAt(next_quotation+1)!='\"'){
                        now_quotation = next_quotation;
                        break;
                    } else {
                        now_quotation = next_quotation+2;
                    }
                }
                if (now_quotation == start+1) res.add("");
                else res.add(safeString(sentence.substring(start+1,now_quotation).replace("\"\"","\"")));
                start = now_quotation+2;
            } else{
                //逗号处理
                int end = sentence.indexOf(",",start);
                if (end != -1){
                    res.add(safeString(sentence.substring(start,end)));
                    start = end + 1;
                } else {
                    res.add(safeString(sentence.substring(start,length)));
                    start = length;
                }
            }
        }
        if (sentence.charAt(length-1) == ',')
            res.add("");
        /*String[] strings = sentence.split(",");
        return new ArrayList<>(Arrays.asList(strings));*/
        return res;
    }
}
