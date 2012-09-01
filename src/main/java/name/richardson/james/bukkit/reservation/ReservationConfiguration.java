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
import java.util.Map;

import name.richardson.james.bukkit.utilities.configuration.PluginConfiguration;
import name.richardson.james.bukkit.utilities.plugin.Plugin;

public class ReservationConfiguration extends PluginConfiguration {

  public enum ReservationType {
    FULL,
    KICK
  }

  public ReservationConfiguration(final Plugin plugin) throws IOException {
    super(plugin);
  }

  @SuppressWarnings("unchecked")
  public Map<String, ReservationType> getPlayers() {
    return (Map<String, ReservationType>) this.getConfiguration().getMapList("reserved-players");
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

  public void setPlayers(final Map<String, ReservationType> players) {
    this.getConfiguration().createSection("reserved-players", players);
    this.save();
  }

}
