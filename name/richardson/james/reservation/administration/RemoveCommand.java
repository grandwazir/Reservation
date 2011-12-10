/*******************************************************************************
 * Copyright (c) 2011 James Richardson.
 * 
 * RemoveCommand.java is part of Reservation.
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
package name.richardson.james.reservation.administration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import name.richardson.james.reservation.Reservation;
import name.richardson.james.reservation.util.Command;
import name.richardson.james.reservation.util.PluginLogger;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.PermissionDefault;

public class RemoveCommand extends Command {

  public RemoveCommand(final Reservation plugin) {
    super(plugin);
    this.name = "remove";
    this.description = "remove a player from the reservation list";
    this.usage = "/reserve remove [playerName]";
    this.permission = "reservation." + this.name;
    registerPermission(permission, "remove people to the reservation list", PermissionDefault.OP);
  }

  @Override
  public void execute(final CommandSender sender, Map<String, String> arguments) {
    String playerName = arguments.get("playerName"); 
    if (plugin.removeReservation(playerName)) {
      sender.sendMessage(String.format(ChatColor.GREEN + "%s has been removed from the reserved list.", playerName));
      // Logger.info(String.format("%s removed %s from the reserved list.", this.getSenderName(sender), playerName));
    } else {
      sender.sendMessage(String.format(ChatColor.YELLOW + "%s is not on the reserved list.", playerName));
    }
  }

  @Override
  protected Map<String, String> parseArguments(List<String> arguments) throws IllegalArgumentException {
    Map<String, String> m = new HashMap<String, String>();
    arguments.remove(0);

    try {
      m.put("playerName", arguments.get(0));
    } catch (IndexOutOfBoundsException exception) {
      throw new IllegalArgumentException("You must specify a player name.");
    }

    return m;
  }

}
