package de.niroyt.nnc.modules;

import org.bukkit.entity.*;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import de.niroyt.nnc.enums.Check;
import de.niroyt.nnc.manager.*;
import de.niroyt.nnc.utils.ConfigManager;
import de.niroyt.nnc.utils.Flag;

import org.bukkit.*;

public class Safewalk extends Module {
	        
    public void onMove(final PlayerMoveEvent e) {    	
    	final Player player = e.getPlayer();
    	
    	if(Manager.isOP(player)) {
        	return;
        }
    	
    	if(player.isSneaking()) {
    		return;
    	}
    	
    	if(e.getFrom().getZ() == e.getTo().getZ() && e.getFrom().getX() == e.getTo().getX()) {
    		return;
    	}
    	
    	if(!player.isOnGround()) {
    		return;
    	}
    	
    	if(!onBridge(player)) {
    		return;
    	}
    	
    	if(posOnBridge(player, e.getTo()) 
    			&& e.getFrom().distance(e.getTo()) > 0.08
    			&& e.getFrom().distance(e.getTo()) < 0.22
    			&& e.getFrom().getY() == e.getTo().getY()) {        		
    		if(System.currentTimeMillis() - Manager.getPlayerData(player).getSafewalkLastFlag() < 2000
    				&& System.currentTimeMillis() - Manager.getPlayerData(player).getSafewalkLastFlag() > 250
    				&& round(e.getFrom().distance(e.getTo())) == round(Manager.getPlayerData(player).getSafewalkSpeedValue())) {
    			if(Manager.debug(player, Check.msw2, "")) {
    				e.setCancelled(true);
    				
    				player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 100, 255, false, false));
		    		player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 100, 128, false, false));  
		    			
		    		Manager.getPlayerData(player).setWorldPlaceBlockLock(System.currentTimeMillis() + 5000);
    			}
    		}
    		
    		Manager.getPlayerData(player).setSafewalkLastFlag(System.currentTimeMillis());
    		Manager.getPlayerData(player).setSafewalkSpeedValue(e.getFrom().distance(e.getTo()));
    	} 
    	
    	double yaw = player.getLocation().getYaw();    	
    	if(!((yaw < 190 && yaw > 170)|| (yaw > -190 && yaw < -170) 
    			|| ((yaw < -350 && yaw >= -360) || (yaw > -10 && yaw <= 0))
    			|| ((yaw > 350 && yaw <= 360) || (yaw < 10 && yaw >= 0))
    					
    			|| (yaw < 280 && yaw > 260)
    			|| (yaw > 80 && yaw < 100)
    			|| (yaw > -280 && yaw < -260)
    			|| (yaw > -100 && yaw < -80))) {
    		
    		if(posOnBridge(player, e.getTo())) {
    			onWalkSafe(player, true);
    			return;
    		}
    	}
    	
    	onWalkSafe(player, false);
    }
    
    public boolean onBridge(final Player player) {
    	final Location loc = player.getLocation();
    
    	int x = loc.getBlockX() - 1;
        int y = loc.getBlockY() - 1;
        int z = loc.getBlockZ() - 1;
        
        int Blocks = 0;
        
        while(x <= loc.getBlockX() + 1) {
        	while(z <= loc.getBlockZ() + 1) {                	
        		Location BlockPos = new Location(loc.getWorld(), x, y, z);
        			
        		if(BlockPos.getBlock().getType() != Material.AIR) {        				
        			Blocks++;
        		}
                z++;        		
            }
        	z = loc.getBlockZ() - 1;
        	x++;
        }
                
        if(Blocks == 1 || Blocks == 2 || Blocks == 3) {
        	return true;
        }
            	
		return false;
	}

	public double round(final double value) {
    	return Double.valueOf(Math.round(value * 100)) / 100;
    }
    
    public boolean posOnBridge(final Player player, final Location loc) {
    	if(player.getLocation().clone().add(0, -1, 0).getBlock().getType() != Material.AIR) {
    		return false;
    	}
    	
    	if(loc.getX() % 1.0 >= 0.690 && loc.getX() % 1.0 <= 0.708
				&& NoWall(player, "X+")) {
    		return true;
    	}
    	
    	if(loc.getX() % 1.0 >= 0.292 && loc.getX() % 1.0 <= 0.310
    			&& NoWall(player, "X-")) {
    		onWalkSafe(player, true);
    		return true;
    	}
    	
    	if(loc.getX() % 1.0 <= -0.690 && loc.getX() % 1.0 >= -0.708
    			&& NoWall(player, "X-")) {
    		onWalkSafe(player, true);
    		return true;
    	}
    	
    	if(loc.getX() % 1.0 <= -0.292 && loc.getX() % 1.0 >= -0.310
    			&& NoWall(player, "X+")) {
    		onWalkSafe(player, true);
    		return true;
    	}
    	
    	if(loc.getZ() % 1.0 >= 0.690 && loc.getZ() % 1.0 <= 0.708
    			&& NoWall(player, "Z+")) {
    		onWalkSafe(player, true);
    		return true;
    	}
    	
    	if(loc.getZ() % 1.0 >= 0.292 && loc.getZ() % 1.0 <= 0.310
    			&& NoWall(player, "Z-")) {
    		onWalkSafe(player, true);
    		return true;
    	}
    	
    	if(loc.getZ() % 1.0 <= -0.690 && loc.getZ() % 1.0 >= -0.708
    			&& NoWall(player, "Z-")) {
    		onWalkSafe(player, true);
    		return true;
    	}
    	
    	if(loc.getZ() % 1.0 <= -0.292 && loc.getZ() % 1.0 >= -0.310
    			&& NoWall(player, "Z+")) {
    		onWalkSafe(player, true);
    		return true;
    	}
    	
    	return false;
    }
    
    public boolean NoWall(final Player player, final String f) {
    	if(f == "Z+") {
    		return player.getLocation().getBlock().getRelative(0, 0, 1).getType() == Material.AIR
        			&& player.getLocation().getBlock().getRelative(0, 1, 1).getType() == Material.AIR
        			&& player.getLocation().getBlock().getRelative(1, 0, 1).getType() == Material.AIR
        			&& player.getLocation().getBlock().getRelative(1, 1, 1).getType() == Material.AIR
        			&& player.getLocation().getBlock().getRelative(-1, 0, 1).getType() == Material.AIR
        			&& player.getLocation().getBlock().getRelative(-1, 1, 1).getType() == Material.AIR;
    	} 
    	
    	if(f == "Z-") {
    		return player.getLocation().getBlock().getRelative(0, 0, -1).getType() == Material.AIR
        			&& player.getLocation().getBlock().getRelative(0, 1, -1).getType() == Material.AIR
        			&& player.getLocation().getBlock().getRelative(1, 0, -1).getType() == Material.AIR
        			&& player.getLocation().getBlock().getRelative(1, 1, -1).getType() == Material.AIR
        			&& player.getLocation().getBlock().getRelative(-1, 0, -1).getType() == Material.AIR
        			&& player.getLocation().getBlock().getRelative(-1, 1, -1).getType() == Material.AIR;
    	}
    	
    	if(f == "X+") {
    		return player.getLocation().getBlock().getRelative(1, 0, 0).getType() == Material.AIR
        				&& player.getLocation().getBlock().getRelative(1, 1, 0).getType() == Material.AIR
        				&& player.getLocation().getBlock().getRelative(1, 0, 1).getType() == Material.AIR
                		&& player.getLocation().getBlock().getRelative(1, 1, 1).getType() == Material.AIR
                		&& player.getLocation().getBlock().getRelative(1, 0, -1).getType() == Material.AIR
                		&& player.getLocation().getBlock().getRelative(1, 1, -1).getType() == Material.AIR;
    	}
    	
    	if(f == "X-") {
    		return player.getLocation().getBlock().getRelative(-1, 0, 0).getType() == Material.AIR
        				&& player.getLocation().getBlock().getRelative(-1, 1, 0).getType() == Material.AIR
        				&& player.getLocation().getBlock().getRelative(-1, 0, 1).getType() == Material.AIR
                		&& player.getLocation().getBlock().getRelative(-1, 1, 1).getType() == Material.AIR
                		&& player.getLocation().getBlock().getRelative(-1, 0, -1).getType() == Material.AIR
                		&& player.getLocation().getBlock().getRelative(-1, 1, -1).getType() == Material.AIR;
    	}
    	
    	return false;
    }
    
    public void onWalkSafe(final Player player, final boolean value) {    	
		if(!value) {
			if(Manager.getPlayerData(player).getSafewalkCounter() > 0) {
				Manager.getPlayerData(player).setSafewalkCounter(Manager.getPlayerData(player).getSafewalkCounter() - 4);
			}
			return;
		}
		
		Manager.getPlayerData(player).setSafewalkCounter(Manager.getPlayerData(player).getSafewalkCounter() + 1);
		
		if(Manager.getPlayerData(player).getSafewalkCounter() >= ConfigManager.getCheckConfigValue(Check.msw1, "maxValue")) {
			Manager.getPlayerData(player).setSafewalkCounter(Manager.getPlayerData(player).getSafewalkCounter() - 2);
			
			if(Manager.debug(player, Check.msw1, "Value: " + Manager.getPlayerData(player).getSafewalkCounter())) {
				Flag.backPort(player);
			}
		}
    }
}
