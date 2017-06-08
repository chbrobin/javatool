package pers.chbrobin.javatool.util;

import java.io.*;

/**
 * Created by Administrator on 2017/6/7 0007.
 */
public class FileUtil {

    public static String readFileString(InputStream inputStream) {
        try {
            StringBuffer sb = new StringBuffer();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String s = null;
            while ((s = reader.readLine()) != null) {
                sb.append(s);
            }
            return sb.toString();
        } catch(IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}
