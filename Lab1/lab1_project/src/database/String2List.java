package database;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class String2List {
    public static List<String> string2List(String sentence){
        String[] strings = sentence.split(",");
        return new ArrayList<>(Arrays.asList(strings));
    }
}
