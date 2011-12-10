/*******************************************************************************
 * Copyright (c) 2011 James Richardson.
 * 
 * Command.java is part of Reservation.
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

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import name.richardson.james.reservation.util.PluginLogger;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

public abstract class Command implements CommandExecutor {

  protected static String consoleName = "Console";
  
  protected String description;
  protected String name;
  protected String[] optionalArgumentKeys;
  protected String permission;
  protected Reservation plugin;
  protected Integer requiredArgumentCount;
  protected String usage;
  protected PluginLogger logger;

  public Command(Reservation plugin) {
    this.plugin = plugin;
    this.logger = new PluginLogger(this.toString());
  }
  
  public abstract void execute(CommandSender sender, Map<String, String> arguments);

  @Override
  public boolean onCommand(final CommandSender sender, final org.bukkit.command.Command command, final String label, final String[] args) {
    if (!this.authorisePlayer(sender)) {
      sender.sendMessage(ChatColor.RED + "You do not have permission to do that.");
      return true;
    }
    
    try {
      LinkedList<String> arguments = new LinkedList<String>();
      arguments.addAll(Arrays.asList(args));
      final Map<String, String> parsedArguments = this.parseArguments(arguments);  
      this.execute(sender, parsedArguments);
    } catch (final IllegalArgumentException e) {
      sender.sendMessage(ChatColor.RED + this.usage);
      sender.sendMessage(ChatColor.YELLOW + e.getMessage());
    }
    return true;
  }

  /**
   * Check to see if a player has permission to use this command.
   * 
   * A console user is permitted to use all commands by default.
   * 
   * @param sender The player/console that is attempting to use the command
   * @return true if the player has permission; false otherwise.
   */
  protected boolean authorisePlayer(CommandSender sender) {
    if (sender instanceof ConsoleCommandSender) {
      return true;
    } else if (sender instanceof Player) {
      final Player player = (Player) sender;
      if (player.hasPermission(this.permission) || player.hasPermission("reservation.*")) {
        return true;
      }
    } 
    return false;
  }
  
  
  /**
   * Get the name of a CommandSender.
   * 
   * By default a CommandSender which is not a Player has no name. In this case
   * the method will return the value of consoleName.
   * 
   * @param sender The CommandSender that you wish to resolve the name of.
   * @return name Return the name of the Player or "Console" if no name available.
   */
  protected String getSenderName(CommandSender sender) {
    if (sender instanceof ConsoleCommandSender) {
      return Command.consoleName;
    } else {
      final Player player = (Player) sender;
      return player.getName();
    }
  }    

  protected abstract Map<String, String> parseArguments(List<String> arguments);
  
  protected void registerPermission(final String name, final String description, final PermissionDefault defaultValue) {
    final Permission permission = new Permission(name, description, defaultValue);
    plugin.getServer().getPluginManager().addPermission(permission);
  }

}
