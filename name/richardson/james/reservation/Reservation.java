/*******************************************************************************
 * Copyright (c) 2011 James Richardson.
 * 
 * Reservation.java is part of Reservation.
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

package name.richardson.james.reservation;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.persistence.PersistenceException;

import name.richardson.james.reservation.administration.AddCommand;
import name.richardson.james.reservation.administration.ListCommand;
import name.richardson.james.reservation.administration.RemoveCommand;
import name.richardson.james.reservation.motd.ServerListener;
import name.richardson.james.reservation.util.Configuration;
import name.richardson.james.reservation.util.Database;
import name.richardson.james.reservation.util.Logger;

import org.bukkit.event.Event;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Reservation extends JavaPlugin {

  private final Logger logger = new Logger(this.getClass());
  private CommandManager commandManager;
  private Configuration configuration;
  private PluginDescriptionFile description;
  private PlayerListener playerListener;
  private PluginManager pluginManager;
  private ServerListener serverListener;

  @Override
  public List<Class<?>> getDatabaseClasses() {
    return Database.getDatabaseClasses();
  }

  @Override
  public void onDisable() {
    this.logger.info(this.description.getName() + " is now disabled.");
  }

  @Override
  public void onEnable() {
    this.commandManager = new CommandManager();
    this.pluginManager = this.getServer().getPluginManager();
    this.description = this.getDescription();

    try {
      this.configuration = this.loadConfiguration();
      this.isConfigurationSane();
      this.setupDatabase();
      new Database(this);
      this.registerListeners();
      this.setupCommands();
    } catch (final IOException exception) {
      this.logger.severe("Unable to load configuration!");
      this.pluginManager.disablePlugin(this);
    } catch (final IllegalArgumentException exception) {
      this.logger.severe(exception.getMessage());
      this.pluginManager.disablePlugin(this);
    } catch (final Exception exception) {
      this.logger.severe("Unknown exception has occured!");
      exception.printStackTrace();
    } finally {
      if (!this.pluginManager.isPluginEnabled(this)) return;
    }

    this.logger.info(this.description.getFullName() + " is now enabled.");

  }

  private void isConfigurationSane() {
    final int reservedSlots = this.configuration.getReservedSlots();
    if ((this.getServer().getMaxPlayers() - reservedSlots) < 0) throw new IllegalArgumentException("Your total player slots must be equal or higher than the number of reserved slots.");
  }

  private Configuration loadConfiguration() throws IOException {
    final InputStream defaultConfigurationStream = this.getResource("config.yml");
    final Configuration configuration = new Configuration(this.getDataFolder(), defaultConfigurationStream);
    // enable debugging if necessary
    if (configuration.isDebugging()) {
      Logger.enableDebugging();
      configuration.logValues();
    }
    return configuration;
  }

  private void registerListeners() {
    final PluginManager pm = this.getServer().getPluginManager();
    this.playerListener = new PlayerListener(this.getServer());
    this.serverListener = new ServerListener(this.getServer());
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
      this.getDatabase().find(ReservationRecord.class).findRowCount();
    } catch (final PersistenceException ex) {
      this.logger.warning("No database schema found. Generating a new one.");
      this.installDDL();
    }
  }

}
