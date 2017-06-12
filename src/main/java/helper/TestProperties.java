package helper;

import java.io.IOException;
import java.util.Properties;

public class TestProperties {

    private static Properties properties;


    public static Properties getProperties(){
        if (null == properties){
            properties = new Properties();
            try {
                properties.load(TestProperties.class.getClassLoader().getResourceAsStream("test.properties"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return properties;
    }


}
