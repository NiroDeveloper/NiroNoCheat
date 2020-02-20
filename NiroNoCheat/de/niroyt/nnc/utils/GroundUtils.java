package de.niroyt.nnc.utils;


import org.bukkit.entity.*;
import org.bukkit.*;
import org.bukkit.event.player.*;

import de.niroyt.nnc.enums.BlockHeight;
import de.niroyt.nnc.manager.Manager;
import de.niroyt.nnc.manager.Module;
import de.niroyt.nnc.modules.Phase;


public class GroundUtils extends Module {    
	
    public static boolean wasOnSlime(final Player player) {
    	return Manager.getPlayerData(player).isGroundUtilsWasOnSlime();
    }
    
    public static boolean wasOnIce(final Player player) {
    	return Manager.getPlayerData(player).getGroundUtilsWasOnIce() > 6;
    }
    
    public static boolean wasFlying(final Player player) {
    	return System.currentTimeMillis() - Manager.getPlayerData(player).getGroundUtilsWasFlying() < 1000;
    }
        
    public void onToggleFlight(final PlayerToggleFlightEvent e) {
    	final Player player = e.getPlayer();
    	if(e.isFlying()) {
    		Manager.getPlayerData(player).setGroundUtilsWasFlying(System.currentTimeMillis());
    	}
    }
    
    public void onMove(final PlayerMoveEvent e) {
    	final Player player = e.getPlayer();

        if(!player.isFlying() && player.isOnGround()) {
        	Manager.getPlayerData(player).setGroundUtilsWasFlying(0);
        } else if(player.isFlying()) {
        	Manager.getPlayerData(player).setGroundUtilsWasFlying(System.currentTimeMillis());
        }
        
        if(onSlime(e.getFrom())) {
        	Manager.getPlayerData(player).setGroundUtilsWasOnSlime(true);
        }
        
        if(isPlayerPositionOnGround(player.getLocation()) && !onSlime(e.getFrom())) {
        	Manager.getPlayerData(player).setGroundUtilsWasOnSlime(false);
        }
        
        if(BlockChecks.onIce(e.getTo())) {
        	if(!wasOnIce(player)) {
        		Manager.getPlayerData(player).setGroundUtilsWasOnIce(10);
        	}
        } else {
        	if(wasOnIce(player) && player.isOnGround()) {
        		int i = Manager.getPlayerData(player).getGroundUtilsWasOnIce();
        		if(i > 1) {
        			Manager.getPlayerData(player).setGroundUtilsWasOnIce(i - 1);
        		}        		
        	}
        }
        
    	if(System.currentTimeMillis() - Manager.getPlayerData(player).getLastFlag() < 1500) {
    		return;
    	}
                        
        if((isPlayerPositionOnGround(e.getTo()) && !onSlime(e.getFrom())) || player.isFlying()) {   
        	Location pos = e.getTo().clone();
        	pos.setY(Math.floor(pos.getY()));
        	Manager.getPlayerData(player).setGroundUtilsLastOnGround(pos);
        }        
    }
    
    public static Location LastOnGround(final Player player) {         	
    	double x = (int) player.getLocation().getX();
    	double y = (int) player.getLocation().getY();
    	double z = (int) player.getLocation().getZ();
    	    	
    	if(Manager.getPlayerData(player).getGroundUtilsLastOnGround() != null) {
    		x = Manager.getPlayerData(player).getGroundUtilsLastOnGround().getX();
    		z = Manager.getPlayerData(player).getGroundUtilsLastOnGround().getZ();
	        y = Manager.getPlayerData(player).getGroundUtilsLastOnGround().getY();
    	}
	    
    	Location loc = new Location(player.getWorld(), x, y, z, player.getLocation().getYaw(), player.getLocation().getPitch());
    	
    	if(Phase.inBlock(loc)) {
    		loc = player.getLocation();
    	}
    	    	
    	return loc;
    }
        
    public void onTick() {
    	for(final Player player : Bukkit.getOnlinePlayers()) {    		
    		if(Manager.getPlayerData(player).getGroundUtilsPosResetTimer().isDelayComplete(5000) && System.currentTimeMillis() - Manager.getPlayerData(player).getLastFlag() > 1000) {
    			Manager.getPlayerData(player).getGroundUtilsPosResetTimer().setLastMS();

    			Manager.getPlayerData(player).setGroundUtilsLastTimePos(player.getLocation());
    		}
    	}
    }
    
    public static Location LastLocation(final Player player) {      	    	
    	double x = (int) player.getLocation().getX();
    	double y = (int) player.getLocation().getY();
    	double z = (int) player.getLocation().getZ();
    	
    	if(Manager.getPlayerData(player).getGroundUtilsLastTimePos() != null) {
	        z = Manager.getPlayerData(player).getGroundUtilsLastTimePos().getZ();
	        y = Manager.getPlayerData(player).getGroundUtilsLastTimePos().getY();
	        x = Manager.getPlayerData(player).getGroundUtilsLastTimePos().getX();
    	}
    	
    	Location loc = new Location(player.getWorld(), x, y, z, player.getLocation().getYaw(), player.getLocation().getPitch());
    	
    	if(Phase.inBlock(loc)) {
    		loc = player.getLocation();
    	}
    	
    	return loc;
    }
    
    public void onRespawn(final PlayerRespawnEvent e) {
    	final Player player = e.getPlayer();
    	
    	ResetLocations(player, e.getRespawnLocation());
    }
    
    public void ResetLocations(final Player player, final Location e) {
    	Manager.getPlayerData(player).setGroundUtilsLastTimePos(e.clone());
		
    	Manager.getPlayerData(player).setGroundUtilsLastOnGround(e.clone());
    }
    
    public void onTeleport(final PlayerTeleportEvent e) {    	
    	final Player player = e.getPlayer();	
    	    	
    	ResetLocations(player, e.getTo());
    }
    
    public static boolean onSlime(final Location loc) {
    	if(loc.getY() % 1 == 0 && BlockChecks.inBlock(loc.clone().add(0, -1, 0), Material.SLIME_BLOCK)) {
    		return true;
    	}
    	    	    	   	
    	return false;
    }
    
    public static boolean isPlayerPositionOnGround(final Location loc) {	
    	if(loc.getY() % 1 == 0 && (Phase.inBlock(loc.clone().add(0, -1, 0)) || BlockChecks.inBlock(loc.clone().add(0, -1, 0), Material.SLIME_BLOCK) || BlockChecks.onIce(loc))) {
    		return true;
    	}
    	
    	if(loc.getY() % 0.5 == 0 && BlockChecks.onSteps(loc)) {
    		return true;
    	}
    	
    	if(BlockChecks.inBlock(loc, Material.SNOW) && loc.getY() % 0.125 == 0) {
    		return true;
    	}
    	    	
    	for(final BlockHeight h : BlockHeight.values()) {
    		if(loc.getY() % 1 != h.getHeight() && loc.getY() % 2 != h.getHeight() && (loc.getY() + 1) % 2 != h.getHeight()) {
    			continue;
    		}
    		
    		Location l = loc.clone();
    		
    		if(h.getHeight() >= 1) {
    			l.add(0, -1, 0);
    		}
    		
    		if(BlockChecks.inBlock(l, h.getMaterial())) {
    			return true;
    		}
    	}
    	   	
    	return false;
    }
    
}