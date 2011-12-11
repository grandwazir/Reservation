/*******************************************************************************
 * Copyright (c) 2011 James Richardson.
 * 
 * ListCommand.java is part of Reservation.
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

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import name.richardson.james.reservation.Reservation;
import name.richardson.james.reservation.database.ReservationRecord;
import name.richardson.james.reservation.database.ReservationRecord.Type;
import name.richardson.james.reservation.util.Command;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.PermissionDefault;

public class ListCommand extends Command {

  public ListCommand(final Reservation plugin) {
    super(plugin);
    this.name = "list";
    this.description = "list all players that have reservations";
    this.usage = "/reserve list";
    this.permission = "reservation." + this.name;
    this.registerPermission(this.permission, "list players on the reservation list", PermissionDefault.OP);
  }

  @Override
  public void execute(final CommandSender sender, final Map<String, Object> arguments) {
    Map<String, Type> reservations = this.handler.listReservations();
    if (reservations.size() == 0) {
      sender.sendMessage(ChatColor.YELLOW + "There are no players on the list.");
    } else {
      final String list = this.buildList(reservations);
      final String numberOfPlayers = Integer.toString(reservations.size());
      sender.sendMessage(ChatColor.YELLOW + numberOfPlayers + " player(s): " + ChatColor.WHITE + list);
    }
  }

  private String buildList(final Map<String, Type> reservations) {
    final StringBuilder list = new StringBuilder();
    for (Entry<String, ReservationRecord.Type> entry : reservations.entrySet()) {
      list.append(entry.getKey());
      list.append(" (");
      list.append(entry.getValue());
      list.append(")");
      list.append(", ");
    }
    list.delete(list.length() - 2, list.length());
    return list.toString();
  }

  @Override
  protected Map<String, Object> parseArguments(final List<String> arguments) throws IllegalArgumentException {
    return null;
  }

}
