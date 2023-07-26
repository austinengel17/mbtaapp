package com.mbtaapp.tools;

import java.io.IOException;
import java.net.URL;
import java.util.Properties;

/**
 * The class Properties reader.
 */
public final class PropertiesReader {


    /**
     * Constant PROPERTIES.
     */
    private static final Properties PROPERTIES;

    /**
     * Constant PROP_FILE.
     */
    private static final String PROP_FILE = "apikeys.properties";

    /**
     * Default private constructor PropertiesReader.
     */
    private PropertiesReader() {
    }

    static {
        PROPERTIES = new Properties();
        final URL props = ClassLoader.getSystemResource(PROP_FILE);
        try {
            PROPERTIES.load(props.openStream());
        } catch (IOException ex) {
            System.out.print("error");
        }
    }

    /**
     * Method getProperty.
     *
     * @param name String name file.
     * @return Return property
     */
    public static String getProperty(final String name) {
        return PROPERTIES.getProperty(name);
    }
}