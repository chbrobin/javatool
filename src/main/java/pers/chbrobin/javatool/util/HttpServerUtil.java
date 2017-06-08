package pers.chbrobin.javatool.util;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * Created by Administrator on 2017/6/8 0008.
 */
public class HttpServerUtil {

    public static String DEFAULT_ENCODING = "UTF-8";

    public static String readLine(InputStream is, int contentLe) throws IOException {
        ArrayList lineByteList = new ArrayList();
        byte readByte;
        int total = 0;
        if (contentLe != 0) {
            do {
                readByte = (byte) is.read();
                lineByteList.add(Byte.valueOf(readByte));
                total++;
            } while (total < contentLe);//消息体读还未读完
        } else {
            do {
                readByte = (byte) is.read();
                lineByteList.add(Byte.valueOf(readByte));
            } while (readByte != 10);
        }

        byte[] tmpByteArr = new byte[lineByteList.size()];
        for (int i = 0; i < lineByteList.size(); i++) {
            tmpByteArr[i] = ((Byte) lineByteList.get(i)).byteValue();
        }
        lineByteList.clear();

        String tmpStr = new String(tmpByteArr, DEFAULT_ENCODING);
        if (tmpStr.startsWith("Referer")) {//如果有Referer头时，使用UTF-8编码
            tmpStr = new String(tmpByteArr, "UTF-8");
        }
        return tmpStr;
    }

    public static void closeSocket(Socket socket) {
        try {
            socket.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        System.out.println(socket + "离开了HTTP服务器");
    }

    public static void fileShow(String fileName, Socket socket) {
        try {
            PrintStream out = new PrintStream(socket.getOutputStream(), true);
            String fileString = FileUtil.readFileString(HttpServerUtil.class.getClassLoader().getResourceAsStream(fileName));

            // 用 writer 对客户端 socket 输出一段 HTML 代码
            out.println("HTTP/1.0 200 OK");//返回应答消息,并结束应答
            out.println("Content-Type:text/html;charset=" + DEFAULT_ENCODING);
            out.println();// 根据 HTTP 协议, 空行将结束头信息

            out.println(fileString);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}