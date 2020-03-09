package com.javainuse;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class AppProperties {

    public static String getPropValue(String key) throws FileNotFoundException, IOException {
        List<InputStream> inputStreams = new ArrayList<InputStream>();
        Properties prop = new Properties();
        String propFileName1 = "config.properties";
        String propFileName2 = "config_uat.properties";
        AppProperties obj = new AppProperties();
        inputStreams.add(obj.getClass().getClassLoader().getResourceAsStream(propFileName1));
        inputStreams.add(obj.getClass().getClassLoader().getResourceAsStream(propFileName2));
        for (InputStream inputStream : inputStreams) {
            if (inputStream != null) {
                prop.load(inputStream);
            } else {
                throw new FileNotFoundException("property file  not found in the classpath");
            }
        }
        return prop.getProperty(key);
    }
}
