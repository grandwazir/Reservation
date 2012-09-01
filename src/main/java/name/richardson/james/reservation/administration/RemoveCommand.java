/*******************************************************************************
 * Copyright (c) 2011 James Richardson.
 * 
 * RemoveCommand.java is part of Reservation.
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

import name.richardson.james.bukkit.utilities.command.AbstractCommand;
import name.richardson.james.bukkit.utilities.command.CommandArgumentException;
import name.richardson.james.bukkit.utilities.command.CommandPermissionException;
import name.richardson.james.bukkit.utilities.command.CommandUsageException;
import name.richardson.james.reservation.Reservation;

import org.bukkit.command.CommandSender;

public class RemoveCommand extends AbstractCommand {

  private String player;
  
  private final  Reservation plugin;

  public RemoveCommand(final Reservation plugin) {
    super(plugin, false);
    this.plugin = plugin;
  }

  public void execute(CommandSender sender) throws CommandArgumentException, CommandPermissionException, CommandUsageException {
    this.plugin.removePlayer(player);
    sender.sendMessage(this.getLocalisation().getMessage(this, "player-removed", player));
  }

  public void parseArguments(String[] arguments, CommandSender sender) throws CommandArgumentException {
    if (arguments.length != 1) throw new CommandArgumentException(this.getUsage(), null);
    this.player = arguments[0];
  }


}
