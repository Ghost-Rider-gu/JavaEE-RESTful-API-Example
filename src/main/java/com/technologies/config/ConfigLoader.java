package com.technologies.config;

import lombok.extern.slf4j.Slf4j;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Loader configuration app.
 */
@Slf4j
public class ConfigLoader {

    private static Properties appProperties = new Properties();
    private static final String config = "application.properties";

    static {
        LoadConfiguration();
    }

    /**
     * Load configuration file
     */
    private static void LoadConfiguration() {
        log.info("Loading application configuration ...");
        try (InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(config)) {
            appProperties.load(inputStream);
        } catch (FileNotFoundException fileException) {
                log.error("Can't find the configuration file: " + config);
            } catch (IOException ioException) {
                log.error("Can't load the configuration file: " + config);
            }
    }

    /**
     * Get one of the application property by key
     *
     * @param keyProperty {@link String}
     *
     * @return valueProperty {@link String}
     */
    public static String getAppProperty(String keyProperty) {
        String valueProperty = appProperties.getProperty(keyProperty);
        if (valueProperty == null) {
            log.error("Can't find application property: " + keyProperty);
            throw new IllegalArgumentException("");
        }
        return valueProperty;
    }

}
