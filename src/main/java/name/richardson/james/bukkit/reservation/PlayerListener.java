/*******************************************************************************
 * Copyright (c) 2011 James Richardson.
 * 
 * PlayerListener.java is part of Reservation.
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

import java.util.Map;

import name.richardson.james.bukkit.reservation.ReservationConfiguration.ReservationType;
import name.richardson.james.bukkit.utilities.listener.Listener;
import name.richardson.james.bukkit.utilities.localisation.Localisation;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerPreLoginEvent;
import org.bukkit.event.server.ServerListPingEvent;

public class PlayerListener implements Listener {

  public enum ServerState {
    NOT_FULL,
    FULL,
    RESERVED_AVAILABLE
  }
  
  private final Reservation plugin;
  private final Map<String, ReservationType> reservedPlayers;
  private final Localisation localisation;

  public PlayerListener(final Reservation plugin, final Map<String, ReservationType> reservedPlayers) {
    this.reservedPlayers = reservedPlayers;
    this.plugin = plugin;
    this.localisation = plugin.getLocalisation();
  }

  @EventHandler(priority=EventPriority.NORMAL, ignoreCancelled=true)
  public void onPlayerPreLogin(final PlayerPreLoginEvent event) {
    final String playerName = event.getName().toLowerCase();
    final ReservationType reservation = reservedPlayers.get(playerName);
    
    switch (this.getServerState()) {
    case NOT_FULL:
      event.allow();
    case FULL:
      if (reservation != null && this.kickPlayer()) {
        event.allow();
      } else {
        event.disallow(PlayerPreLoginEvent.Result.KICK_FULL, this.localisation.getMessage(this, "server-full"));
      }
      break;
    case RESERVED_AVAILABLE:
      if (reservation == ReservationType.KICK && this.kickPlayer()) {
        event.allow();
      } else if (reservation == ReservationType.FULL) {
        event.allow();
      } else {
        event.disallow(PlayerPreLoginEvent.Result.KICK_FULL, this.localisation.getMessage(this, "server-full"));
      }
      break;
    }
  
  }
  
  @EventHandler(priority=EventPriority.NORMAL, ignoreCancelled=true)
  public void onServerListPing(final ServerListPingEvent event) {
    event.setMaxPlayers(event.getMaxPlayers() - plugin.getHiddenSlotCount());
  }
  
  private ServerState getServerState() {
    int playerCount = Bukkit.getServer().getOnlinePlayers().length;
    int maxPlayers = Bukkit.getServer().getMaxPlayers();
    int reservedSlots = plugin.getReservedSlotCount();
    if (playerCount == maxPlayers) {
      return ServerState.FULL;
    } else if (playerCount >= maxPlayers - reservedSlots) {
      return ServerState.RESERVED_AVAILABLE;
    } else {
      return ServerState.NOT_FULL;
    }
  }

  private boolean kickPlayer() {
    for (final Player player : Bukkit.getServer().getOnlinePlayers()) {
      if (!player.hasPermission("reservation.kick-immune")) {
        player.kickPlayer(this.localisation.getMessage(this, "kicked-to-make-room"));
        return true;
      }
    }
    return false;
  }

}
