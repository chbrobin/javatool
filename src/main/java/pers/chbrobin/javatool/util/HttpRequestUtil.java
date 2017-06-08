package pers.chbrobin.javatool.util;

import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

/**
 * Created by Administrator on 2017/6/7 0007.
 */
public class HttpRequestUtil {

    public static Map<String, String> parseParam(String query) {
        Map<String, String> map = new HashedMap();
        String[] qs = StringUtils.split(query,"&");
        for(String q : qs) {
            String[] ss = StringUtils.split(q, "=");
            if(ss != null && ss.length > 1) {
                map.put(ss[0], ss[1]);
            }
        }
        return map;
    }

    public static String getRequestByPost(String u, String postParam, String encoding) {
        if (u == null) {
            throw new RuntimeException("无效的路径");
        }
        try {
            URL url = new URL(u);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url
                    .openConnection();
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setRequestProperty("User-agent","Mozilla/4.0");
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setReadTimeout(40000);

            if (postParam != null && !postParam.equals("")) {
                OutputStream os = httpURLConnection.getOutputStream();
                os.write(postParam.getBytes());
                os.close();
            }

            httpURLConnection.connect();

            InputStream inputStream = (InputStream) httpURLConnection
                    .getInputStream();

            ByteArrayOutputStream bops = new ByteArrayOutputStream();
            int count = 0;
            byte[] b = new byte[1024];
            while ((count = inputStream.read(b)) != -1) {
                bops.write(b, 0, count);
            }
            inputStream.close();

            httpURLConnection.disconnect();

            return new String(bops.toByteArray(), 0, bops.size(), encoding);
        } catch (Exception e) {
            throw new RuntimeException("获取给定的资源失败:" + e, e);
        }
    }
}
