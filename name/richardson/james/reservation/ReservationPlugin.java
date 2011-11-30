/*******************************************************************************
 * Copyright (c) 2011 James Richardson.
 * 
 * ReservationPlugin.java is part of Reservation.
 * 
 * Reservation is free software: you can redistribute it and/or modify it 
 * under the terms of the GNU General Public License as published by the Free 
 * Software Foundation, either version 3 of the License, or (at your option) 
 * any later version.
 * 
 * Reservation is distributed in the hope that it will be useful, but WITHOUT ANY 
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS 
 * FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License 
 * along with Reservation.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package name.richardson.james.reservation;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.persistence.PersistenceException;

import name.richardson.james.reservation.util.Logger;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Event;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;


public class ReservationPlugin extends JavaPlugin {

  private CommandManager commandManager;
  private YamlConfiguration configuration;
  private PluginDescriptionFile description;
  private PlayerListener playerListener;
  private PluginManager pluginManager;
  private HashMap<String, ReservationRecord.Type> reservations;
  private ServerListener serverListener;


  public boolean addReservation(String playerName, String reservationTypeString) {
    ReservationRecord.Type reservationType = ReservationRecord.Type.valueOf(reservationTypeString); 
    if (!reservations.containsKey(playerName)) {
      ReservationRecord record = new ReservationRecord();
      record.setPlayerName(playerName);
      record.setReservationType(reservationType);
      record.save();
      reservations.put(playerName, reservationType);
      Logger.debug(String.format("Reservation added for %s. Type: %s", playerName, reservationType.toString()));
      return true;
    } else {
      return false;
    }
  }

  @Override
  public List<Class<?>> getDatabaseClasses() {
    final List<Class<?>> list = new ArrayList<Class<?>>();
    list.add(ReservationRecord.class);
    return list;
  }
  
  public HashMap<String, ReservationRecord.Type> getReservations() {
    return reservations;
  }

  @Override
  public void onDisable() {
    Logger.info(description.getName() + " is now disabled.");
  }

  @Override
  public void onEnable() {
    commandManager = new CommandManager();
    pluginManager = this.getServer().getPluginManager();
    description = this.getDescription();
    
    try {
      Logger.initalise();
      ReservationRecord.initalise(this.getDatabase());
      configuration = loadConfiguration();
      Logger.setDebugging(configuration.getBoolean("debugging", true));
      isConfigurationSane();
      setupDatabase();
      reservations = loadReservations();
      registerListeners();
      setupCommands();
    } catch (IOException exception) {
      Logger.severe("Unable to load configuration!");
      this.pluginManager.disablePlugin(this);
    } catch (IllegalArgumentException exception) {
      Logger.severe(exception.getMessage());
      this.pluginManager.disablePlugin(this);
    } finally {
      if (!this.pluginManager.isPluginEnabled(this)) {
        return;
      }
    }

    Logger.info(description.getFullName() + " is now enabled.");
    
  }

  public boolean removeReservation(String playerName) {
    if (reservations.containsKey(playerName)) {
      ReservationRecord record = ReservationRecord.find(playerName);
      record.destroy();
      reservations.remove(playerName);
      Logger.debug(String.format("Reservation removed for %s.", playerName));
      return true;
    } else {
      return false;
    }
  }
  
  private void isConfigurationSane() {
    int reservedSlots = configuration.getInt("reserved-slots");
    if ( (this.getServer().getMaxPlayers() - reservedSlots) < 0) {
      throw new IllegalArgumentException("Your total player slots must be equal or higher than the number of reserved slots.");
    }
  }
  
  private YamlConfiguration loadConfiguration() throws IOException {
    Logger.info("Loading configuration: config.yml.");
    File configurationFile = new File(this.getDataFolder() + "/config.yml");
    YamlConfiguration configuration = YamlConfiguration.loadConfiguration(configurationFile);
    configuration.addDefault("debugging", true);
    configuration.addDefault("reserved-slots", 4);
    configuration.addDefault("hide-reserved-slots", true);
    configuration.options().copyDefaults(true);
    configuration.save(configurationFile);
    return configuration;
  }

  private HashMap<String, ReservationRecord.Type> loadReservations() {
    HashMap<String, ReservationRecord.Type> map = new HashMap<String, ReservationRecord.Type>();
    for (ReservationRecord record : ReservationRecord.list()) {
      map.put(record.getPlayerName().toLowerCase(), record.getReservationType());
    }
    Logger.info(String.format("%d reservation(s) loaded.", map.size()));
    return map;
  }
  
  private void registerListeners() {
    int reservedSlots = configuration.getInt("reserved-slots");
    PluginManager pm = this.getServer().getPluginManager();
    this.playerListener = new PlayerListener(reservations, reservedSlots ,this.getServer());
    this.serverListener = new ServerListener(this.getServer().getMaxPlayers(), reservedSlots, configuration.getBoolean("hide-reserved-slots"));
    pm.registerEvent(Event.Type.PLAYER_PRELOGIN, this.playerListener, Event.Priority.High, this);
    pm.registerEvent(Event.Type.SERVER_LIST_PING, this.serverListener, Event.Priority.Normal, this);
    
  }
  
  private void setupCommands() {
    this.getCommand("reserve").setExecutor(this.commandManager);
    this.commandManager.registerCommand("add", new AddCommand(this));
    this.commandManager.registerCommand("remove", new RemoveCommand(this));
    this.commandManager.registerCommand("list", new ListCommand(this));
  }
  
  private void setupDatabase() {
    try {
      getDatabase().find(ReservationRecord.class).findRowCount();
    } catch (final PersistenceException ex) {
      Logger.warning("No database schema found. Generating a new one.");
      installDDL();
    }
  }

}
