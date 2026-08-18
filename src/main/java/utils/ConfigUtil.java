package utils;

import java.io.FileInputStream;
import java.util.Properties;

public class ConfigUtil {

    private static final Properties props = new Properties();

    static {
        try (FileInputStream input =
                     new FileInputStream("src/main/resources/config.properties")) {

            props.load(input);

        } catch (Exception e) {
            throw new RuntimeException("Failed to load config.properties", e);
        }
    }

    public static String getKey(String key) {
        return props.getProperty(key);
    }
}