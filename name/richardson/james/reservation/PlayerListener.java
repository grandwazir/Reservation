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

package name.richardson.james.reservation;

import java.util.Map;

import name.richardson.james.reservation.util.Configuration;
import name.richardson.james.reservation.util.Logger;

import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerPreLoginEvent;
import org.bukkit.event.player.PlayerPreLoginEvent.Result;

public class PlayerListener extends org.bukkit.event.player.PlayerListener {

  private final Logger logger = new Logger(this.getClass());
  private final Map<String, ReservationRecord.Type> reservations = ReservationHandler.reservations;
  private final int maxSlots;
  private final int visibleSlots;
  private final int reservedSlots;
  private final Server server;

  public PlayerListener(final Server server) {
    this.reservedSlots = Configuration.getInstance().getReservedSlots();
    this.maxSlots = server.getMaxPlayers();
    this.visibleSlots = maxSlots - reservedSlots;
    this.server = server;
  }

  @Override
  public void onPlayerPreLogin(final PlayerPreLoginEvent event) {
    final String playerName = event.getName().toLowerCase();
    if (isVisibleSlotsUsed() && reservations.containsKey(event.getName())) {
      logger.debug(playerName + "is attempting to join a full server.");
      if (isReservedSlotAvailable() && (reservations.get(playerName) == ReservationRecord.Type.FULL)) {
        logger.debug(playerName + " using a reserved slot.");
        event.allow();
      } else if (isServerFull()) {
        if (kickPlayer()) {
          logger.debug("Kicked another player so " + playerName + "could join.");
          event.allow();
        }
      } else {
        logger.debug("Unable to make room for " + playerName);
        event.disallow(Result.KICK_FULL, "The server is full!");
      }
    } else {
      logger.debug("No visible slots available for " + playerName + ".");
      event.disallow(Result.KICK_FULL, "The server is full!");
    }
  }

  private boolean isVisibleSlotsUsed() {
    return this.server.getOnlinePlayers().length >= this.visibleSlots;
  }
  
  private boolean isServerFull() {
    return this.server.getOnlinePlayers().length == maxSlots;
  }
  
  private boolean isReservedSlotAvailable() {
    if ((this.server.getOnlinePlayers().length >= this.visibleSlots) && (this.server.getOnlinePlayers().length != maxSlots)) {
      return true;
    } else {
      return false;
    }
  }

  private boolean kickPlayer() {
    for (final Player player : this.server.getOnlinePlayers()) {
      if (!player.hasPermission("reservation.kick-immune")) {
        player.kickPlayer("You have been kicked to make room for another player.");
        return true;
      }
    }
    return false;
  }


}
