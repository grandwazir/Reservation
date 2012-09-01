
package name.richardson.james.bukkit.reservation;

import java.io.IOException;
import java.util.Map;

import name.richardson.james.bukkit.utilities.configuration.PluginConfiguration;
import name.richardson.james.bukkit.utilities.plugin.Plugin;

public class ReservationConfiguration extends PluginConfiguration {
  
  public enum ReservationType {
    KICK,
    FULL
  }
  
  public ReservationConfiguration(Plugin plugin) throws IOException {
    super(plugin);
  }
  
  public int getReservedSlots() {
    return getConfiguration().getInt("reserved-slots", 0);
  }

  public boolean isDebugging() {
    return getConfiguration().getBoolean("debugging");
  }

  public boolean isHideReservedSlots() {
    return getConfiguration().getBoolean("hide-reserved-slots", true);
  }
  
  @SuppressWarnings("unchecked")
  public Map<String, ReservationType> getPlayers() {
    return (Map<String, ReservationType>) getConfiguration().getMapList("reserved-players");
  }
  
  public void setPlayers(Map<String, ReservationType> players) {
    getConfiguration().createSection("reserved-players", players);
    this.save();
  }

}
