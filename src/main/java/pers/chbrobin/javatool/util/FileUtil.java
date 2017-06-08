package pers.chbrobin.javatool.util;

import java.io.*;
import java.nio.Buffer;

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
            reader.close();
            inputStream.close();
            return sb.toString();
        } catch(IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static void writeFileString(File file, String content) {
        try {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
            writer.write(content);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
