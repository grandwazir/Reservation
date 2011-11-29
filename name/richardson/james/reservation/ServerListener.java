/*******************************************************************************
 * Copyright (c) 2011 James Richardson.
 * 
 * ServerListener.java is part of Reservation.
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

import org.bukkit.event.server.ServerListPingEvent;

public class ServerListener extends org.bukkit.event.server.ServerListener {
  
  private int maxPlayers;
  private int reservedSlots;
  private boolean hideReservedSlots;

  public ServerListener(int maxPlayers, int reservedSlots, boolean hideReservedSlots) {
    this.maxPlayers = maxPlayers;
    this.reservedSlots = reservedSlots;
    this.hideReservedSlots = hideReservedSlots;
  }
  
  public void onServerListPing(ServerListPingEvent event) {
    if (hideReservedSlots) {
      int visibleSlots = maxPlayers - reservedSlots;
      event.setMaxPlayers(visibleSlots);
    }
  }
  
}
