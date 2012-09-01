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

package name.richardson.james.bukkit.reservation;

import java.io.IOException;
import java.util.Map;

import name.richardson.james.bukkit.reservation.ReservationConfiguration.ReservationType;
import name.richardson.james.bukkit.reservation.administration.AddCommand;
import name.richardson.james.bukkit.reservation.administration.ListCommand;
import name.richardson.james.bukkit.reservation.administration.RemoveCommand;
import name.richardson.james.bukkit.utilities.command.CommandManager;
import name.richardson.james.bukkit.utilities.plugin.AbstractPlugin;

public class Reservation extends AbstractPlugin {
  
  private ReservationConfiguration configuration;

  public void registerListeners() {
    new PlayerListener(this, this.configuration.getPlayers());
  }

  public void registerCommands() {
    final CommandManager commandManager = new CommandManager(this);
    this.getCommand("reserve").setExecutor(commandManager);
    commandManager.addCommand(new AddCommand(this));
    commandManager.addCommand(new RemoveCommand(this));
    commandManager.addCommand(new ListCommand(this, this.configuration.getPlayers()));
  }
  
  @Override
  public void loadConfiguration() throws IOException {
    super.loadConfiguration();
    this.configuration = new ReservationConfiguration(this);
  }
  
  public String getArtifactID() {
    return "reservation";
  }
  
  public void addPlayer(String name, ReservationType type) {
    final Map<String, ReservationType>players = this.configuration.getPlayers();
    players.put(name, type);
    this.configuration.setPlayers(players);
  }

  public void removePlayer(String name) {
    final Map<String, ReservationType>players = this.configuration.getPlayers();
    players.remove(name);
    this.configuration.setPlayers(players);
  }
  
  public int getHiddenSlotCount() {
    return (this.configuration.isHideReservedSlots()) ? this.configuration.getReservedSlots() : 0;
  }
  
  public int getReservedSlotCount() {
    return this.configuration.getReservedSlots();
  }
  
}
