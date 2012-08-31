/*******************************************************************************
 * Copyright (c) 2011 James Richardson.
 * 
 * Database.java is part of Reservation.
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

package name.richardson.james.reservation.util;

import java.util.ArrayList;
import java.util.List;

import name.richardson.james.reservation.Reservation;
import name.richardson.james.reservation.database.Record;
import name.richardson.james.reservation.database.ReservationRecord;

import com.avaje.ebean.EbeanServer;

public class Database {

  private static final int SCHEMA = 1;

  private static Database instance;
  private static EbeanServer pluginDatabase;

  private final static Logger logger = new Logger(Database.class);

  /**
   * Creates a new instance if one does not already exist.
   * 
   * @throws IllegalStateException
   * thrown when an instance already exists for this class.
   */
  public Database(final Reservation plugin) {
    if (Database.instance != null) throw new IllegalStateException();
    Database.pluginDatabase = plugin.getDatabase();
    logger.config("Database configured.");
    this.validate();
    Database.instance = this;
  }

  public static Class<?> getDatabaseClass(final Record record) {
    if (record instanceof ReservationRecord)
      return ReservationRecord.class;
    else
      throw new IllegalArgumentException();
  }

  public static List<Class<?>> getDatabaseClasses() {
    final List<Class<?>> list = new ArrayList<Class<?>>();
    list.add(ReservationRecord.class);
    return list;
  }

  public static EbeanServer getInstance() {
    return Database.pluginDatabase;
  }

  /**
   * Validates the current database schema and updates it if necessary.
   */
  private void validate() {
    logger.config("Current schema version " + Integer.toString(Database.SCHEMA));
  }

}