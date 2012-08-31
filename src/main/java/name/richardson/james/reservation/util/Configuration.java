/*******************************************************************************
 * Copyright (c) 2011 James Richardson.
 * 
 * Configuration.java is part of Reservation.
 * 
 * Reservation is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * Reservation is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * Reservation. If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/

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