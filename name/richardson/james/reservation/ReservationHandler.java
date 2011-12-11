package name.richardson.james.reservation;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import name.richardson.james.reservation.util.Handler;

import org.bukkit.OfflinePlayer;


public final class ReservationHandler extends Handler {

  protected final static Map<String, ReservationRecord.Type> reservations = new HashMap<String, ReservationRecord.Type>();
  
  public ReservationHandler(Class<?> owner) {
    super(owner);
    if (reservations.isEmpty() && ReservationRecord.list().size() != 0) loadReservations();
  }

  private void loadReservations() {
    logger.debug("Reservation map empty, assuming it needs to be refreshed.");
    for (ReservationRecord record : ReservationRecord.list()) {
      reservations.put(record.getPlayerName(), record.getReservationType());
    }
    logger.info(Integer.toString(reservations.size()) + " reservation(s) loaded.");
  }

  public boolean addReservation(OfflinePlayer player, ReservationRecord.Type reservationType) {
    if (player == null ) throw new IllegalArgumentException("You must provide a OfflinePlayer.");
    if (isPlayerReserved(player)) return false;
    // check to see if the player already is reserved.
    logger.debug(this.owner + " is creating a Reservation.");
    ReservationRecord.create(player.getName(), reservationType);
    reservations.put(player.getName().toLowerCase(), reservationType);
    logger.info("A new reservation has been created for " + player.getName() + ".");
    return true; 
  }
  
  public Map<String, ReservationRecord.Type> listReservations() {
    Map<String, ReservationRecord.Type> m = new HashMap<String, ReservationRecord.Type>();
    for (Entry<String, ReservationRecord.Type> entry : reservations.entrySet()) {
      m.put(entry.getKey(), entry.getValue());
    }
    return m;
  }
  
  public boolean removeReservation(OfflinePlayer player) {
    if (player == null ) throw new IllegalArgumentException("You must provide a OfflinePlayer.");
    if (!isPlayerReserved(player)) return false;
    // check to see if the player already is reserved.
    logger.debug(this.owner + " is attempting to remove a Reservation.");
    ReservationRecord example = new ReservationRecord();
    example.setPlayerName(player.getName());
    ReservationRecord.findFirst(example).delete();
    reservations.remove(player.getName().toLowerCase());
    logger.info("Reservation for " + player.getName() + " has been deleted.");
    return true;
  }
  
  public boolean isPlayerReserved(OfflinePlayer player) {
    ReservationRecord example = new ReservationRecord();
    example.setPlayerName(player.getName());
    if (ReservationRecord.count(example) == 1) {
      return true;
    } else {
      return false;
    }
  }
  
}
