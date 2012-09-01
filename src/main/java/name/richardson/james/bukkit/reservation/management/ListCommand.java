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

package name.richardson.james.bukkit.reservation.management;

import java.util.Map;
import java.util.Map.Entry;

import name.richardson.james.bukkit.reservation.Reservation;
import name.richardson.james.bukkit.reservation.ReservationConfiguration.ReservationType;
import name.richardson.james.bukkit.utilities.command.AbstractCommand;
import name.richardson.james.bukkit.utilities.command.CommandArgumentException;
import name.richardson.james.bukkit.utilities.command.CommandPermissionException;
import name.richardson.james.bukkit.utilities.command.CommandUsageException;
import name.richardson.james.bukkit.utilities.formatters.ChoiceFormatter;

import org.bukkit.command.CommandSender;

public class ListCommand extends AbstractCommand {

  private final Map<String, ReservationType> reservedPlayers;
  
  private final ChoiceFormatter formatter;

  public ListCommand(final Reservation plugin, Map<String, ReservationType> players) {
    super(plugin, false);
    this.reservedPlayers = players;
    this.formatter = new ChoiceFormatter(this.getLocalisation());
    this.formatter.setLimits(0, 1, 2);
    this.formatter.setMessage(this, "header");
    this.formatter.setArguments(reservedPlayers.size());
    this.formatter.setFormats(
        this.getLocalisation().getMessage(this, "no-players"), 
        this.getLocalisation().getMessage(this, "one-player"), 
        this.getLocalisation().getMessage(this, "many-players")
    );
  }

  private String buildList() {
    final StringBuilder list = new StringBuilder();
    for (Entry<String, ReservationType> entry : reservedPlayers.entrySet()) {
      list.append(entry.getKey());
      list.append(" (");
      list.append(entry.getValue());
      list.append(")");
      list.append(", ");
    }
    list.delete(list.length() - 2, list.length());
    return list.toString();
  }


  public void execute(CommandSender sender) throws CommandArgumentException, CommandPermissionException, CommandUsageException {
    if (reservedPlayers.isEmpty()) {
      sender.sendMessage(this.getLocalisation().getMessage(this, "no-players"));
    } else {
      this.formatter.setArguments(reservedPlayers.size());
      final String list = this.buildList();
      sender.sendMessage(this.formatter.getMessage());
      sender.sendMessage(list);
    }
  }

  public void parseArguments(String[] arguments, CommandSender sender) throws CommandArgumentException {
    return;
  }

}
