package de.niroyt.nnc.modules;

import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffectType;

import de.niroyt.nnc.Main;
import de.niroyt.nnc.enums.Check;
import de.niroyt.nnc.manager.*;
import de.niroyt.nnc.utils.BlockChecks;
import de.niroyt.nnc.utils.ConfigManager;
import de.niroyt.nnc.utils.Flag;
import de.niroyt.nnc.utils.GroundUtils;

import org.bukkit.entity.*;

import org.bukkit.*;

public class Speed1 extends Module {
       	
    public void onMove(final PlayerMoveEvent e) {    	
	    final Player player = e.getPlayer();
	       
	    if(Manager.AllowedSpeedFly(player) 
	    		|| BlockChecks.BlockOverPlayer(e.getTo())
	    		|| (GroundUtils.wasOnIce(player) && !player.isOnGround())
	    		|| player.hasPotionEffect(PotionEffectType.SPEED)) {
	    	Manager.getPlayerData(player).setSpeed1DoNotCheckAfterJump(true);
	    	Manager.getPlayerData(player).setSpeed1JumpSpeed(0);
	    	return;
	    }
	    	    	    
	    final Location oldPos = new Location(player.getWorld(), e.getFrom().getX(), 1, e.getFrom().getZ());
	    final Location newPos = new Location(player.getWorld(), e.getTo().getX(), 1, e.getTo().getZ());
	    
    	if(player.isOnGround() && e.getTo().getY() == e.getFrom().getY() && oldPos.distance(newPos) < 0.35) {
    		Manager.getPlayerData(player).setSpeed1DoNotCheckAfterJump(false);
	    }
	    
    	if(Manager.getPlayerData(player).isSpeed1DoNotCheckAfterJump()) {
    		return;
    	}
    	
	    if(!player.isOnGround() && !GroundUtils.onSlime(e.getFrom()) && !GroundUtils.wasOnIce(player)) {	    	
	    	double JumpValue = ConfigManager.getCheckConfigValue(Check.sj5, "maxValue");
			
	    	if(Main.MinecraftVersion.equalsIgnoreCase("v1_8_R3")) {
	    		JumpValue *= 0.5935;
	    	}
	    	
	    	if(Manager.getPlayerData(player).getSpeed1AfterJump() > 0) {
	    		Manager.getPlayerData(player).setSpeed1AfterJump(oldPos.distance(newPos));
	    			 				    		
	    		if(Manager.getPlayerData(player).getSpeed1AfterJump() > JumpValue) {
		    		if(Manager.debug(player, Check.sj5, "Jumpspeed: " + round(Manager.getPlayerData(player).getSpeed1AfterJump()))) {
		    			Flag.backPort(player);
		    			return;
		    		}		    	
	    		}
	    	}
	    			    
	    	if(oldPos.distance(newPos) > JumpValue && oldPos.distance(newPos) > 0 && Manager.getPlayerData(player).getSpeed1AfterJump() > 0) {
	    		if(Manager.debug(player, Check.sj5, "")) {
	    			Flag.backPort(player);
	    		}
	    	}	    	
	    } else {
	    	if(Manager.getPlayerData(player).getSpeed1AfterJump() == Double.NEGATIVE_INFINITY) {
	    		Manager.getPlayerData(player).setSpeed1AfterJump(-1);
	    	} else {
	    		Manager.getPlayerData(player).setSpeed1AfterJump(0);
	    	}
	    }	    	 
	    	    
	    if(player.isOnGround()) {
	    	Manager.getPlayerData(player).setSpeed1JumpSpeed(oldPos.distance(newPos));
	    } else {
	    	if(!GroundUtils.wasOnSlime(player)) {
	    		if(Manager.getPlayerData(player).getSpeed1JumpSpeed() != 0 
	    				&& e.getTo().getY() - e.getFrom().getY() > 0.2
	    				&& !BlockChecks.BlockOverPlayer(e.getTo()) 
	    				&& BlockChecks.canCheckFly(player)
	    				&& BlockChecks.inAir(player)) {
		    		if(Math.round((Manager.getPlayerData(player).getSpeed1JumpSpeed() / oldPos.distance(newPos)) * 1000) / 1000 == 0 
		    				&& player.isSprinting()
		    				&& (System.nanoTime() - Manager.getPlayerData(player).getSprintLastToggle()) / 1000000 - Manager.ping(player) - Main.getServerLag() > 750
		    				&& Main.MinecraftVersion.equalsIgnoreCase("v1_8_R3")) {
		    			if(Manager.debug(player, Check.sj1, "Invalid Movment")) {
		    				Flag.backPort(player);
		    			}
		    		}
		    		if(Manager.getPlayerData(player).getSpeed1JumpSpeed() / oldPos.distance(newPos) < ConfigManager.getCheckConfigValue(Check.sj2, "minValue") 
		    				&& oldPos.distance(newPos) > ConfigManager.getCheckConfigValue(Check.sj2, "maxSpeed")
		    				&& Main.MinecraftVersion.equalsIgnoreCase("v1_8_R3")) {
		    			if(Manager.debug(player, Check.sj2, "Invalid Movment")) {
		    				Flag.backPort(player);
		    			}
		    		}
		    		if(Manager.getPlayerData(player).getSpeed1OldJumpValue() != 0) {
		    			if(Manager.getPlayerData(player).getSpeed1OldJumpValue() == Manager.getPlayerData(player).getSpeed1JumpSpeed() / oldPos.distance(newPos)
		    					&& oldPos.distance(newPos) > 0.12) {
		    				Manager.debug(player, Check.sj6, "Invalid Movment");
		    			}
		    			if(Manager.getPlayerData(player).getSpeed1JumpSpeed() / oldPos.distance(newPos) > ConfigManager.getCheckConfigValue(Check.sj3, "maxValue")
		    					&& Manager.getPlayerData(player).getSpeed1OldJumpValue() > ConfigManager.getCheckConfigValue(Check.sj3, "maxValue")
		    					&& Manager.getPlayerData(player).getSpeed1JumpSpeed() > 0.35 
		    					&& oldPos.distance(newPos) > ConfigManager.getCheckConfigValue(Check.sj3, "maxSpeed")
		    					&& (System.nanoTime() - Manager.getPlayerData(player).getSprintLastToggle()) / 1000000 - Manager.ping(player) - Main.getServerLag() > 750) {
			    			if(Manager.debug(player, Check.sj3, "Invalid Movment")) {
			    				Flag.backPort(player);
			    			}
			    		}
		    		}
		    		Manager.getPlayerData(player).setSpeed1OldJumpValue(Manager.getPlayerData(player).getSpeed1JumpSpeed() / oldPos.distance(newPos));
		    	}
	    		Manager.getPlayerData(player).setSpeed1JumpSpeed(0);
	    	}
	    }
    }
        
    public double round(final double a) {
		return Double.valueOf(Math.round(a * 10000)) / 10000;
	}
}