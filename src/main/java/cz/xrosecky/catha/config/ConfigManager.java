package cz.xrosecky.catha.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.lang.reflect.Constructor;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConfigManager<T> {

    private final Logger LOGGER;
    private final File CONFIGURATION_FILE;
    private Constructor<T> configConstructor;
    private final Class<T> configClass;
    private T configuration;

    public ConfigManager(JavaPlugin paramPlugin, File configPath, Class<T> configClass) {
        this.LOGGER = paramPlugin.getLogger();
        this.CONFIGURATION_FILE = configPath;
        this.CONFIGURATION_FILE.getParentFile().mkdirs();
        this.configClass = configClass;
        try {
            this.configConstructor = Objects.requireNonNull(configClass.getConstructor());
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        this.initConfiguration();
    }

    /**
     * Returns the configuration for the plugin. If the configuration is null the default will be returned.
     *
     * @return The configuration.
     */
    public T getConfiguration() {
        if (Objects.isNull(this.configuration)) {
            this.LOGGER.log(Level.WARNING, "Configuration is Null. The configuration on disk will not be used.");
            T config = null;
            try {
                config = this.configConstructor.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return config;
        } else {
            return this.configuration;
        }
    }

    private void initConfiguration() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        FileReader fileReader;
        FileWriter fileWriter;

        try {
            fileReader = new FileReader(CONFIGURATION_FILE);
            this.configuration = gson.fromJson(fileReader, configClass);
            fileReader.close();
        } catch (FileNotFoundException fileNotFoundException) {
            this.LOGGER.log(Level.INFO, "The file:\"" + this.CONFIGURATION_FILE.getName() + "\"was not found.");
            this.LOGGER.log(Level.INFO, "A new file will be generated and the default configuration will be used instead.");
            try {
                this.configuration = this.configConstructor.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

        try {
            fileWriter = new FileWriter(CONFIGURATION_FILE);
            gson.toJson(this.configuration, fileWriter);
            fileWriter.close();
        } catch (IOException ioException) {
            this.LOGGER.log(Level.WARNING, "An IOException was thrown. The FileWriter was unable to write to the file:\"configuration.json\"");
        }
    }
}
