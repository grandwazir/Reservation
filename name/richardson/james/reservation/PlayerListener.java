/*******************************************************************************
 * Copyright (c) 2011 James Richardson.
 * 
 * PlayerListener.java is part of Reservation.
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

import java.util.HashMap;
import java.util.Set;

import name.richardson.james.reservation.ReservationRecord;
import name.richardson.james.reservation.util.Logger;

import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerPreLoginEvent;


public class PlayerListener extends org.bukkit.event.player.PlayerListener {
  
  private HashMap<String, ReservationRecord.Type> reservations;
  private int reservedSlots;
  private Server server;

  public PlayerListener(HashMap<String, ReservationRecord.Type> reservations, int reservedSlots, Server server) {
    this.reservations = reservations;
    this.reservedSlots = reservedSlots;
    this.server = server;
  }
  
  public void onPlayerPreLogin(PlayerPreLoginEvent event) {
    if (isServerFull()) {
      Logger.debug(String.format("%s is attempting to join the server.", event.getName()));
      String playerName = event.getName().toLowerCase();
      if (reservations.containsKey(playerName)) {
        switch (reservations.get(playerName)) {
          case RESERVED:          
            event.allow();
            break;
          case KICK:
            if (kickPlayer()) {
              event.allow();
            } else {
              event.disallow(PlayerPreLoginEvent.Result.KICK_FULL, "Unable to make room for you.");
            }
            break;
        }
      } else {
        event.disallow(PlayerPreLoginEvent.Result.KICK_FULL, "The server is full!");
      }
    }
  }
  
  private boolean isServerFull() {
    return server.getOnlinePlayers().length >= (server.getMaxPlayers() - this.reservedSlots);
  }
  
  private boolean kickPlayer() {
    for (Player player : server.getOnlinePlayers()) {
      if (!player.hasPermission("reservation.kick-immune")) {
        player.kickPlayer("You have been kicked to make room for another player.");
        return true;
      }
    }
    return false;
  }
  
}
