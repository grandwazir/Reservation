
package name.richardson.james.reservation;

import java.io.IOException;
import java.io.InputStream;

import name.richardson.james.reservation.util.Configuration;
import name.richardson.james.reservation.util.Logger;

public class ReservationConfiguration extends Configuration {

  protected final static Logger logger = new Logger(ReservationConfiguration.class);
  protected final static String fileName = "config.yml";

  protected final InputStream defaults = Reservation.getInstance().getResource(fileName);

  public ReservationConfiguration() throws IOException {
    super();
  }

  public static ReservationConfiguration getInstance() {
    return (ReservationConfiguration) instance;
  }

  public int getReservedSlots() {
    return configuration.getInt("reserved-slots");
  }

  public boolean isDebugging() {
    return configuration.getBoolean("debugging");
  }

  public boolean isHideReservedSlots() {
    return configuration.getBoolean("hide-reserved-slots");
  }

  public void logValues() {
    logger.config(String.format("debugging: %b", isDebugging()));
    logger.config(String.format("hide-reserved-slots: %b", isHideReservedSlots()));
    logger.config(String.format("reserved-slots: %d", getReservedSlots()));
  }

}
