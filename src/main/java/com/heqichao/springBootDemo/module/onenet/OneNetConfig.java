package com.heqichao.springBootDemo.module.onenet;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
/**
 * ONENET GITHUB URL : https://github.com/cm-heclouds/JAVA-HTTP-SDK
 * Created by heqichao on 2019-6-8.
 */
public class OneNetConfig {

    private final static Properties properties;
    private static org.slf4j.Logger logger = LoggerFactory.getLogger(OneNetConfig.class);
    static {
        InputStream in = OneNetConfig.class.getClassLoader().getResourceAsStream("property/service.properties");
        properties = new Properties();
        try {
            properties.load(in);
        } catch (IOException e) {
            logger.error("init config error", e);
        }
    }


    /**
     * 读取字符串
     *
     * @param conf
     * @return
     */
    private static String getString(String conf) {
        return properties.getProperty(conf);
    }

    public static String getUrl(){
        return getString("onenet.url");
    }
    public static String getMasterKey(){
        return getString("onenet.masterKey");
    }
    public static String getOnenetDevId(){
        return getString("onenet.devId");
    }
}
