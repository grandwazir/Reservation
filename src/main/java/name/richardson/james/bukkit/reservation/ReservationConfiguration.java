/*******************************************************************************
 * Copyright (c) 2012 James Richardson.
 * 
 * ReservationConfiguration.java is part of Reservation.
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
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.configuration.ConfigurationSection;

import name.richardson.james.bukkit.utilities.configuration.PluginConfiguration;
import name.richardson.james.bukkit.utilities.plugin.Plugin;

public class ReservationConfiguration extends PluginConfiguration {

  private final Map<String, ReservationType> reservedPlayers = new HashMap<String, ReservationType>();
  
  public enum ReservationType {
    FULL,
    KICK
  }

  public ReservationConfiguration(final Plugin plugin) throws IOException {
    super(plugin);
    this.setPlayers();
  }

  public Map<String, ReservationType> getPlayers() {
    return Collections.unmodifiableMap(this.reservedPlayers);
  }

  public int getReservedSlots() {
    return this.getConfiguration().getInt("reserved-slots", 0);
  }

  @Override
  public boolean isDebugging() {
    return this.getConfiguration().getBoolean("debugging");
  }

  public boolean isHideReservedSlots() {
    return this.getConfiguration().getBoolean("hide-reserved-slots", true);
  }

  public void addPlayer(String name, ReservationType type) {
    ConfigurationSection section = this.getConfiguration().getConfigurationSection("reserved-players");
    section.set(name, type.name());
    this.save();
    this.reservedPlayers.put(name, type);
  }
  
  public void removePlayer(String name) {
    ConfigurationSection section = this.getConfiguration().getConfigurationSection("reserved-players");
    section.set(name, null);
    this.save();
    this.reservedPlayers.remove(name);
  }
  
  private void setPlayers() {
    ConfigurationSection section = this.getConfiguration().getConfigurationSection("reserved-players");
    this.reservedPlayers.clear();
    for (String key : section.getKeys(false)) {
      try {
        this.reservedPlayers.put(key, ReservationType.valueOf(section.getString(key)));
      } catch (final IllegalArgumentException exception) {
        this.getLogger().warning(this, "invalid-reservation", key);
      }
    }
  }

}
