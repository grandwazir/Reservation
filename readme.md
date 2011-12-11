Reservation - Manage player slots on your server.
====================================

## Description

Reservation is plugin for the Minecraft wrapper [Bukkit](http://bukkit.org/) that provides a method for administrators to manage player slots on their server. 

There are currently two supported modes:

- Allow specified players to join a server using a reserved slot.
- Kick a player to make room for a joining player.

## Installation

### Ensure you are using the latest recommended build.

Before you installing Reservation, you need to make sure you are running at least the latest [recommended build](http://ci.bukkit.org/job/dev-CraftBukkit/Recommended/) for Bukkit. 

Reservation will also warn you if you attempt to use it with an untested build of Bukkit. This is usually safe to ignore but if you encounter problems upgrade Bukkit first before reporting a bug against Reservation.

### Getting Reservation

The best way to install Reservation is to use the direct download link to the latest version:

    http://downloads.james.richardson.name/public/binaries/reservation/Reservation.jar
    
Older versions are available as well, however they are not supported.

    http://downloads.james.richardson.name/public/binaries/reservation/

## Configuration

Configure your settings in config.yml. The configuration file can be found in the datafolder for the plugin which is usually: 

    plugins/Reservation/config.yml

Next manage the plugin by using the in game commands: 

    /reserve add [name] [type]
    /reserve list
    /reserve remove

Further [instructions](https://github.com/grandwazir/DynamicMOTD/wiki/instructions) are available on wiki. 