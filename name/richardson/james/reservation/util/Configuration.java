
package name.richardson.james.reservation.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import name.richardson.james.reservation.Reservation;

import org.bukkit.configuration.file.YamlConfiguration;

public abstract class Configuration {

  protected static Configuration instance;
  protected final static Logger logger = new Logger(Configuration.class);
  
  protected final InputStream defaults = Reservation.getInstance().getResource("config.yml");
  protected final File dataFolder = Reservation.getInstance().getDataFolder();
  protected YamlConfiguration defaultConfiguration;
  protected YamlConfiguration configuration;
  protected final String fileName = "config.yml";

  public Configuration() throws IOException {
    if (this.configuration != null) throw new IllegalStateException("");
    // load configuration
    logger.info("Loading configuration: " + fileName + ".");
    final File configurationFile = new File(dataFolder + "/" + fileName);
    logger.debug("Using path: " + configurationFile.toString());
    configuration = YamlConfiguration.loadConfiguration(configurationFile);
    // set defaults if provided.
    if (defaults != null) {
      defaultConfiguration = YamlConfiguration.loadConfiguration(defaults);
      configuration.setDefaults(defaultConfiguration);
      configuration.options().copyDefaults(true);
      configuration.save(configurationFile);
      defaults.close();
    }
    // allow methods to access this configuration.
    instance = this;
  }

  public static Configuration getInstance() {
    return instance;
  }

}
