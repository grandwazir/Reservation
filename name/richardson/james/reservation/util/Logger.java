/*******************************************************************************
 * Copyright (c) 2011 James Richardson.
 * 
 * Logger.java is part of Reservation.
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
package name.richardson.james.reservation.util;


import java.util.logging.Level;

public class Logger {
  
  static private Level debugLevel = Level.FINE;
  static private java.util.logging.Logger logger;
  static private String prefix = "[Reservation] ";
  
  public static void debug(String message) {
    logger.fine(prefix + message);
  }
  
  public static void info(String message) {
    logger.info(prefix + message);
  }
  
  public static void initalise() {
    if (Logger.logger == null) {
      java.util.logging.Logger parentLogger = java.util.logging.Logger.getLogger("Minecraft");
      java.util.logging.Logger logger = java.util.logging.Logger.getLogger("name.richardson.james.reservation");
      logger.setParent(parentLogger);
      Logger.logger = logger;
    }
  }
  
  public static boolean isDebugging() {
    return logger.isLoggable(debugLevel);
  }
  
  public static void setDebugging(Boolean value) {
    logger.setLevel(debugLevel);
  }
  
  public static void severe(String message) {
    logger.severe(prefix + message);
  }
  
  public static void warning(String message) {
    logger.warning(prefix + message);
  }
  
}
