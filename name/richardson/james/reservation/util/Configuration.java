package name.richardson.james.reservation.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.bukkit.configuration.file.YamlConfiguration;

public class Configuration {
  
  protected Logger logger = new Logger(this.getClass());
  
  protected YamlConfiguration configuration;
  protected final String fileName = "config.yml";
  protected static Configuration instance;
  
  public Configuration(File dataFolder, InputStream defaults) throws IOException {
    if (configuration != null) throw new IllegalStateException();
    logger.info("Loading configuration: " + fileName);
    final File configurationFile = new File(dataFolder + "/" + fileName);
    logger.debug("Using path: " + configurationFile.toString());
    final InputStream defaultConfigurationStream = defaults;
    // create configurations
    configuration = YamlConfiguration.loadConfiguration(configurationFile);  
    YamlConfiguration defaultConfiguration = YamlConfiguration.loadConfiguration(defaultConfigurationStream);
    // copy defaults if required.
    configuration.setDefaults(defaultConfiguration);
    configuration.options().copyDefaults(true);
    configuration.save(configurationFile);
    instance = this;
  }
  
  public void logValues() {
    logger.config("debugging: " + configuration.getBoolean("debugging"));
    logger.config("hide-reserved-slots: " + configuration.getBoolean("hide-reserved-slots"));
    logger.config("reserved-slots" + ": " + configuration.getInt("debugging"));
  }
  
  public boolean isDebugging() {
    return configuration.getBoolean("debugging");
  }
  
  public boolean isHideReservedSlots() {
   return configuration.getBoolean("hide-reserved-slots");
  }
  
  public int getReservedSlots() {
    return configuration.getInt("reserved-slots");
  }
  
  public static Configuration getInstance() {
    return instance;
  }
  
}
