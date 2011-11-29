/*******************************************************************************
 * Copyright (c) 2011 James Richardson.
 * 
 * ReservationRecord.java is part of Reservation.
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

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.ExampleExpression;
import com.avaje.ebean.LikeType;
import com.avaje.ebean.validation.NotNull;


@Entity()
@Table(name = "reservation_list")
public class ReservationRecord {
  
  public static enum Type {
    RESERVED,
    KICK
  }
  
  @Id
  private String playerName;

  @NotNull
  private Type reservationType;
  
  private static EbeanServer database;
 
  protected static ReservationRecord find(String playerName) {
    // create the example
    ReservationRecord example = new ReservationRecord();
    example.setPlayerName(playerName);
    // create the example expression
    ExampleExpression expression = ReservationRecord.database.getExpressionFactory().exampleLike(example, true, LikeType.EQUAL_TO);
    // find and return all bans that match the expression
    return ReservationRecord.database.find(ReservationRecord.class).where().add(expression).findList().get(0);
  }
  
  protected static List<ReservationRecord> list() {
    return ReservationRecord.database.find(ReservationRecord.class).findList();
  }

  protected static void initalise(EbeanServer database) {
    if (ReservationRecord.database == null) {
      ReservationRecord.database = database;
    } 
  }

  public String getPlayerName() {
    return playerName;
  }

  public Type getReservationType() {
    return reservationType;
  }

  public void setPlayerName(String playerName) {
    this.playerName = playerName;
  }

  public void setReservationType(Type reservationType) {
    this.reservationType = reservationType;
  }
  
  protected void destroy() {
    ReservationRecord.database.delete(this);
  }
  
  protected void save() {
    ReservationRecord.database.save(this);
  }
  
}
