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
 * Reservation is distributed in the hope that it will be useful, but WITHOUT
 * ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Reservation. If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/

package name.richardson.james.reservation.util;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PluginLogger {

  private static boolean currentlyDebugging = false;
  private static final Level debugLevel = Level.ALL;
  private static final Logger parentLogger = Logger.getLogger("Minecraft");
  private static final String prefix = "[Reservation] ";
  private static final Set<PluginLogger> registeredLoggers = new HashSet<PluginLogger>();

  private final java.util.logging.Logger logger;

  /**
   * Create a new logger with the specified name.
   * 
   * @param className
   * The name of the logger, should be the class it belongs to.
   */
  public PluginLogger(final String className) {
    this.logger = java.util.logging.Logger.getLogger(className);
    this.logger.setParent(PluginLogger.parentLogger);
    PluginLogger.registeredLoggers.add(this);
    if (PluginLogger.currentlyDebugging) {
      this.setDebugging(true);
    }
  }

  /**
   * Enable debugging for all loggers.
   * 
   * This basically sets all parentHandlers to a lower log level to ensure
   * that messages are correctly logged. All newly created and existing loggers
   * will also have debugging enabled.
   */
  public static void enableDebugging() {
    PluginLogger.currentlyDebugging = true;
    for (final Handler handler : PluginLogger.parentLogger.getHandlers()) {
      handler.setLevel(PluginLogger.debugLevel);
    }
    for (final PluginLogger logger : PluginLogger.registeredLoggers) {
      logger.setDebugging(true);
    }
  }

  /**
   * Log a debug message with this logger.
   * 
   * @param message
   * The string that you wish to log.
   */
  public void debug(final String message) {
    this.logger.fine(PluginLogger.prefix + this.logger.getName() + ": " + message);
  }

  /**
   * Log a general message with this logger.
   * 
   * @param message
   * The string that you wish to log.
   */
  public void info(final String message) {
    this.logger.info(PluginLogger.prefix + message);
  }

  /**
   * Check to see if the logger is logging debug messages.
   * 
   * @return isDebugging true if it is logging debug messages, false otherwise.
   */
  public boolean isDebugging() {
    return this.logger.isLoggable(PluginLogger.debugLevel);
  }

  /**
   * Set if a logger should be logging debug messages or not.
   * 
   * @param setDebugging
   * true if it is should log messages, false otherwise.
   */
  public void setDebugging(final Boolean value) {
    this.logger.setLevel(PluginLogger.debugLevel);
  }

  /**
   * Log a severe (fatal) message with this logger.
   * 
   * @param message
   * The string that you wish to log.
   */
  public void severe(final String message) {
    this.logger.severe(PluginLogger.prefix + message);
  }

  /**
   * Log a warning message with this logger.
   * 
   * @param message
   * The string that you wish to log.
   */
  public void warning(final String message) {
    this.logger.warning(PluginLogger.prefix + message);
  }

}
