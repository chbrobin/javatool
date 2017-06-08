package pers.chbrobin.javatool.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

/**
 * Created by Administrator on 2017/6/7 0007.
 */
public class ConfigProperties {
    public static Properties properties = null;
    static {
        properties = new Properties();
        InputStream inputStream = ConfigProperties.class.getClassLoader().getResourceAsStream("config.properties");
        try {
            properties.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }

    public static String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }

    public static List<String> getPropertyNames() {
        Enumeration enumeration = properties.propertyNames();
        List<String> list = new ArrayList<String>();
        while (enumeration.hasMoreElements()) {
            list.add(enumeration.nextElement().toString());
        }
        return list;
    }
}