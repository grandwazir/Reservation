/*******************************************************************************
 * Copyright (c) 2012 James Richardson.
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
package name.richardson.james.bukkit.reservation.management;

import org.bukkit.command.CommandSender;

import name.richardson.james.bukkit.reservation.Reservation;
import name.richardson.james.bukkit.reservation.ReservationConfiguration.ReservationType;
import name.richardson.james.bukkit.utilities.command.AbstractCommand;
import name.richardson.james.bukkit.utilities.command.CommandArgumentException;
import name.richardson.james.bukkit.utilities.command.CommandPermissionException;
import name.richardson.james.bukkit.utilities.command.CommandUsageException;

public class AddCommand extends AbstractCommand {

  private String player;

  private final Reservation plugin;

  private ReservationType reservation;

  public AddCommand(final Reservation plugin) {
    super(plugin, false);
    this.plugin = plugin;
  }

  public void execute(final CommandSender sender) throws CommandArgumentException, CommandPermissionException, CommandUsageException {
    this.plugin.addPlayer(this.player, this.reservation);
    sender.sendMessage(this.getLocalisation().getMessage(this, "player-added", this.player));
  }

  public void parseArguments(final String[] arguments, final CommandSender sender) throws CommandArgumentException {
    if (arguments.length != 2) {
      throw new CommandArgumentException(this.getUsage(), null);
    }
    this.player = arguments[0];
    try {
      this.reservation = ReservationType.valueOf(arguments[1]);
    } catch (final IndexOutOfBoundsException exception) {
      throw new CommandArgumentException(this.getLocalisation().getMessage(this, "must-specify-valid-reservation"), this.getLocalisation().getMessage(this, "valid-reservations"));
    } catch (final IllegalArgumentException exception) {
      throw new CommandArgumentException(this.getLocalisation().getMessage(this, "must-specify-valid-reservation"), this.getLocalisation().getMessage(this, "valid-reservations"));
    }
  }

}
