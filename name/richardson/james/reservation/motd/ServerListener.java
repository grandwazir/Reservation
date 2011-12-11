/*******************************************************************************
 * Copyright (c) 2011 James Richardson.
 * 
 * ServerListener.java is part of Reservation.
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

package name.richardson.james.reservation.motd;

import name.richardson.james.reservation.ReservationConfiguration;
import name.richardson.james.reservation.util.Logger;

import org.bukkit.Server;
import org.bukkit.event.server.ServerListPingEvent;

public class ServerListener extends org.bukkit.event.server.ServerListener {

  private final static Logger logger = new Logger(ServerListener.class);
 
  private final int maxSlots;
  private final int visibleSlots;
  private final int reservedSlots;

  public ServerListener(final Server server) {
    this.reservedSlots = ReservationConfiguration.getInstance().getReservedSlots();
    this.maxSlots = server.getMaxPlayers();
    this.visibleSlots = maxSlots - reservedSlots;
  }

  @Override
  public void onServerListPing(final ServerListPingEvent event) {
    logger.debug(String.format("Displaying %d player slots while %d slots are actually available", visibleSlots, maxSlots));
    event.setMaxPlayers(visibleSlots);
  }

}
