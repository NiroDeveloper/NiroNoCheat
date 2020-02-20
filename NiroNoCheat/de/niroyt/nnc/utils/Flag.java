package de.niroyt.nnc.utils;

import org.bukkit.BanList.Type;


import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import de.niroyt.nnc.Main;
import de.niroyt.nnc.enums.CheckCategory;
import de.niroyt.nnc.enums.Messages;
import de.niroyt.nnc.manager.Commands;

public class Flag {

	public static void backPort(final Player player) {		
		if(!player.isDead()) {	
			Location l;
    		if(GroundUtils.isPlayerPositionOnGround(player.getLocation())) {
	    		l = GroundUtils.LastLocation(player);
	    	} else {	
	    		l = GroundUtils.LastOnGround(player);
	    	}	
    		    	
    		Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), new Runnable() {				
				@Override
				public void run() {
					player.teleport(l);
				}
			});    	
    	} 	
    }
    
    public static void kick(final Player player, final CheckCategory m) {        
    	final String KickMessage = ConfigManager.getMessage(Messages.PREFIX) + ConfigManager.getMessage(Messages.KICK_MESSAGE);
    	    	
    	player.sendMessage(KickMessage);
    	
    	Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), new Runnable() {
    		public void run() {
    			try {
					player.kickPlayer(KickMessage);
				} catch (Exception e) {
					Commands.sendMessage(Bukkit.getConsoleSender(), "§cError: Can not kick " + player.getName() + ": " + e.getMessage());
				}    			
    		}
    	});    	
    }
    
    public static void ban(final Player player) { 
    	final String normalCommand = "ban [player] Cheating";
    	final String ConfigVar = "Ban-Command";
    	
    	if(normalCommand.equalsIgnoreCase(ConfigManager.cfg.getString(ConfigVar))) {
    		Bukkit.getBanList(Type.NAME).addBan(player.getName(), "Cheating", null, "NiroNoCheat");    
    	} else {
    		final String command = ConfigManager.cfg.getString(ConfigVar).replace("[player]", player.getName());
    		if(!Bukkit.getServer().dispatchCommand(Main.getInstance().getServer().getConsoleSender(), command)) {
    			Commands.sendMessage(Bukkit.getConsoleSender(), "§c\"" + command + "\" could not be executed!");
    			ConfigManager.cfg.set(ConfigVar, normalCommand);
    			
    			ConfigManager.saveConfig();
    		}
    	}
    	
    	Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), new Runnable() {
    		public void run() {
    			player.kickPlayer(ConfigManager.getMessage(Messages.PREFIX) + ConfigManager.getMessage(Messages.BAN_MESSAGE));
    		}
    	});
    }	
}
