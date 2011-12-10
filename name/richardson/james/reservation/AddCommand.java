/*******************************************************************************
 * Copyright (c) 2011 James Richardson.
 * 
 * AddCommand.java is part of Reservation.
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import name.richardson.james.reservation.util.PluginLogger;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.PermissionDefault;

public class AddCommand extends Command {

  public AddCommand(final Reservation plugin) {
    super(plugin);
    this.logger.setDebugging(true);
    this.name = "add";
    this.description = "add a player to the reservation list";
    this.usage = "/reserve add [name] [type]";
    this.permission = "reservation." + this.name;
    registerPermission(permission, "add people to the reservation list", PermissionDefault.OP);
  }

  @Override
  public void execute(final CommandSender sender, Map<String, String> arguments) {
    String playerName = arguments.get("playerName"); 
    if (plugin.addReservation(playerName, arguments.get("reservationType"))) {
      sender.sendMessage(String.format(ChatColor.GREEN + "%s added to reserved list.", playerName));
      logger.debug(String.format("%s added %s to reserved list.", this.getSenderName(sender), playerName));
    } else {
      sender.sendMessage(String.format(ChatColor.YELLOW + "%s is already on the reserved list.", playerName));
    }
  }

  @Override
  protected Map<String, String> parseArguments(List<String> arguments) throws IllegalArgumentException {
    Map<String, String> m = new HashMap<String, String>();
    arguments.remove(0);

    try {
      m.put("playerName", arguments.get(0));
      ReservationRecord.Type.valueOf(arguments.get(1));
      m.put("reservationType", arguments.get(1));
    } catch (IndexOutOfBoundsException exception) {
      throw new IllegalArgumentException("You must specify both a name and a reservation type.");
    } catch (IllegalArgumentException exception) {
      throw new IllegalArgumentException("Use RESERVED or KICK as the type.");
    }

    return m;
  }

}
