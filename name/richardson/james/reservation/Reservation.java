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
import java.util.List;

import javax.persistence.PersistenceException;

import name.richardson.james.reservation.administration.AddCommand;
import name.richardson.james.reservation.administration.ListCommand;
import name.richardson.james.reservation.administration.RemoveCommand;
import name.richardson.james.reservation.database.ReservationRecord;
import name.richardson.james.reservation.motd.ServerListener;
import name.richardson.james.reservation.util.Database;
import name.richardson.james.reservation.util.Logger;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Reservation extends JavaPlugin {

  public final static int BUILT_AGAINST = 1572;
  private final static Logger logger = new Logger(Reservation.class);
  private CommandManager commandManager;
  private PluginDescriptionFile description;
  private PlayerListener playerListener;
  private PluginManager pluginManager;
  private ServerListener serverListener;
  private static Reservation instance;

  public Reservation() {
    Reservation.instance = this;
  }

  public static Reservation getInstance() {
    return instance;
  }

  private static void checkVersionCompatability() {
    try {
      int bukkitBuild = Integer.parseInt(Bukkit.getVersion().subSequence(33, 37).toString());
      if (bukkitBuild < BUILT_AGAINST) {
        Reservation.logger.warning(String.format("Reservation has not been tested with your build of Bukkit (%d).", bukkitBuild));
        Reservation.logger.warning("It will most likely function correctly but you may encounter bugs.");
        Reservation.logger.warning(String.format("To avoid problems upgrade to build %d or higher.", BUILT_AGAINST));
      }
    } catch (NumberFormatException exception) {
      Reservation.logger.warning("Unable to determine Bukkit version.");
    }
  }

  @Override
  public List<Class<?>> getDatabaseClasses() {
    return Database.getDatabaseClasses();
  }

  @Override
  public void onDisable() {
    Reservation.logger.info(this.description.getName() + " is now disabled.");
  }

  @Override
  public void onEnable() {
    Reservation.checkVersionCompatability();
    this.commandManager = new CommandManager();
    this.pluginManager = this.getServer().getPluginManager();
    this.description = this.getDescription();

    try {
      this.checkOnlineMode();
      this.loadConfiguration();
      this.isConfigurationSane();
      this.setupDatabase();
      new Database(this);
      this.registerListeners();
      this.setupCommands();
    } catch (final IOException exception) {
      Reservation.logger.severe("Unable to load configuration!");
      this.pluginManager.disablePlugin(this);
    } catch (final IllegalArgumentException exception) {
      Reservation.logger.severe(exception.getMessage());
      this.pluginManager.disablePlugin(this);
    } catch (final IllegalStateException exception) {
      Reservation.logger.severe(exception.getMessage());
      this.pluginManager.disablePlugin(this);
    } finally {
      if (!this.pluginManager.isPluginEnabled(this)) return;
    }

    Reservation.logger.info(this.description.getFullName() + " is now enabled.");

  }

  private void checkOnlineMode() {
    if (!this.getServer().getOnlineMode()) throw new IllegalStateException("Unable to function when online-mode is set to false!");
  }
  
  private void isConfigurationSane() {
    final int reservedSlots = ReservationConfiguration.getInstance().getReservedSlots();
    if ((this.getServer().getMaxPlayers() - reservedSlots) < 0) throw new IllegalArgumentException("Your total player slots must be equal or higher than the number of reserved slots.");
  }

  private void loadConfiguration() throws IOException {
    ReservationConfiguration configuration = new ReservationConfiguration();
    if (configuration.isDebugging()) {
      Logger.enableDebugging();
      configuration.logValues();
    }
  }

  private void registerListeners() {
    final PluginManager pm = this.getServer().getPluginManager();
    this.playerListener = new PlayerListener(this.getServer());
    pm.registerEvent(Event.Type.PLAYER_PRELOGIN, this.playerListener, Event.Priority.High, this);
    if (ReservationConfiguration.getInstance().isHideReservedSlots()) {
      this.serverListener = new ServerListener(this.getServer());
      pm.registerEvent(Event.Type.SERVER_LIST_PING, this.serverListener, Event.Priority.Normal, this);
    }
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
      Reservation.logger.warning("No database schema found. Generating a new one.");
      this.installDDL();
    }
  }

}
