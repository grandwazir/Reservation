/*******************************************************************************
 * Copyright (c) 2011 James Richardson.
 * 
 * ReservationRecord.java is part of Reservation.
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

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import name.richardson.james.reservation.util.Database;



import com.avaje.ebean.validation.NotNull;


@Entity()
@Table(name = "reservation_list")
public class ReservationRecord extends Record {
  
  public enum Type {
    FULL,
    KICK
  }

  @NotNull
  private long createdAt;

  @Id
  private String playerName;
  
  @NotNull
  private ReservationRecord.Type reservationType;
  
  @Override
  public String toString() {
    StringBuilder message = new StringBuilder();
    message.append(this.getClass().getSimpleName() + ": [");
    message.append("createdAt: " + Long.toString(getCreatedAt()));
    message.append(", playerName: " + getPlayerName());
    message.append("].");
    return message.toString();
  }

  public long getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(long createdAt) {
    this.createdAt = createdAt;
  }

  public String getPlayerName() {
    return playerName;
  }

  public void setPlayerName(String playerName) {
    this.playerName = playerName;
  }

  public ReservationRecord.Type getReservationType() {
    return reservationType;
  }

  public void setReservationType(ReservationRecord.Type reservationType) {
    this.reservationType = reservationType;
  }

  protected static List<ReservationRecord> list() {
    return Database.getInstance().find(ReservationRecord.class).findList();
  }
  
  protected static void create(String playerName, ReservationRecord.Type reservationType) {
    ReservationRecord record = new ReservationRecord();
    record.setCreatedAt(System.currentTimeMillis());
    record.setPlayerName(playerName);
    record.setReservationType(reservationType);
    record.save();
  }
  
}
