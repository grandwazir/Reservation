/*******************************************************************************
 * Copyright (c) 2011 James Richardson.
 * 
 * ReservationRecordHandler.java is part of Reservation.
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

package name.richardson.james.reservation.database;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import name.richardson.james.reservation.util.Handler;
import name.richardson.james.reservation.util.Logger;

import org.bukkit.OfflinePlayer;

public final class ReservationRecordHandler extends Handler {

  protected final static Logger logger = new Logger(ReservationRecordHandler.class);
  protected final static Map<String, ReservationRecord.Type> reservations = new HashMap<String, ReservationRecord.Type>();

  public ReservationRecordHandler(Class<?> owner) {
    super(owner);
    if (reservations.isEmpty() && ReservationRecord.list().size() != 0) loadReservations();
  }

  public boolean addReservation(OfflinePlayer player, ReservationRecord.Type reservationType) {
    if (player == null) throw new IllegalArgumentException("You must provide a OfflinePlayer.");
    if (isPlayerReserved(player)) return false;
    // check to see if the player already is reserved.
    logger.debug(this.owner + " is creating a Reservation.");
    ReservationRecord.create(player.getName(), reservationType);
    reservations.put(player.getName().toLowerCase(), reservationType);
    logger.info("A new reservation has been created for " + player.getName() + ".");
    return true;
  }

  public boolean isPlayerReserved(OfflinePlayer player) {
    ReservationRecord example = new ReservationRecord();
    example.setPlayerName(player.getName());
    if (Record.count(example) == 1) {
      return true;
    } else {
      return false;
    }
  }

  public Map<String, ReservationRecord.Type> listReservations() {
    return Collections.unmodifiableMap(reservations);
  }

  public boolean removeReservation(OfflinePlayer player) {
    if (player == null) throw new IllegalArgumentException("You must provide a OfflinePlayer.");
    if (!isPlayerReserved(player)) return false;
    // check to see if the player already is reserved.
    logger.debug(this.owner + " is attempting to remove a Reservation.");
    ReservationRecord example = new ReservationRecord();
    example.setPlayerName(player.getName());
    Record.findFirst(example).delete();
    reservations.remove(player.getName().toLowerCase());
    logger.info("Reservation for " + player.getName() + " has been deleted.");
    return true;
  }

  private void loadReservations() {
    logger.debug("Reservation map empty, assuming it needs to be refreshed.");
    for (ReservationRecord record : ReservationRecord.list()) {
      reservations.put(record.getPlayerName(), record.getReservationType());
    }
    logger.info(Integer.toString(reservations.size()) + " reservation(s) loaded.");
  }

}
