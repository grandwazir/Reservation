/*******************************************************************************
 * Copyright (c) 2011 James Richardson.
 * 
 * AddCommand.java is part of Reservation.
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

package name.richardson.james.reservation.administration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import name.richardson.james.reservation.Reservation;
import name.richardson.james.reservation.ReservationRecord;
import name.richardson.james.reservation.ReservationRecord.Type;
import name.richardson.james.reservation.util.Command;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.PermissionDefault;

public class AddCommand extends Command {

  public AddCommand(final Reservation plugin) {
    super(plugin);
    this.name = "add";
    this.description = "add a player to the reservation list";
    this.usage = "/reserve add [name] [type]";
    this.permission = "reservation." + this.name;
    this.registerPermission(this.permission, "add people to the reservation list", PermissionDefault.OP);
  }

  @Override
  public void execute(final CommandSender sender, final Map<String, Object> arguments) {
    final OfflinePlayer player = (OfflinePlayer) arguments.get("player");
    final Type reservationType = (Type) arguments.get("reservationType");
    
    if (this.handler.addReservation(player, reservationType)) {
      sender.sendMessage(String.format(ChatColor.GREEN + "%s added to reserved list.", player.getName()));
      this.logger.info(String.format("%s added %s to reserved list.", sender.getName(), player.getName()));
    } else {
      sender.sendMessage(String.format(ChatColor.YELLOW + "%s is already on the reserved list.", player.getName()));
    }
  }

  @Override
  protected Map<String, Object> parseArguments(final List<String> arguments) throws IllegalArgumentException {
    final Map<String, Object> m = new HashMap<String, Object>();
    arguments.remove(0);

    try {
      m.put("player", this.plugin.getServer().getOfflinePlayer(arguments.get(0)));
      m.put("reservationType", ReservationRecord.Type.valueOf(arguments.get(1)));
    } catch (final IndexOutOfBoundsException exception) {
      throw new IllegalArgumentException("You must specify both a name and a reservation type.");
    } catch (final IllegalArgumentException exception) {
      throw new IllegalArgumentException("Use RESERVED or KICK as the type.");
    }

    return m;
  }

}
