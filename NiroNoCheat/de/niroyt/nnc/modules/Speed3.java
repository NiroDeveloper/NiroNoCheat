package de.niroyt.nnc.modules;

import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.potion.PotionEffectType;

import de.niroyt.nnc.enums.Check;
import de.niroyt.nnc.manager.*;
import de.niroyt.nnc.modules.fly.Web;
import de.niroyt.nnc.utils.BlockChecks;
import de.niroyt.nnc.utils.ConfigManager;
import de.niroyt.nnc.utils.Flag;
import de.niroyt.nnc.utils.GroundUtils;

import org.bukkit.entity.*;


import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;

public class Speed3 extends Module {
	        
    public void onMove(final PlayerMoveEvent e) {    	
	    final Player player = e.getPlayer(); 	
	    
	    if(Manager.AllowedSpeedFly(player)) {
	    	return;
	    }
	    
	    final double dX = e.getFrom().getX() - e.getTo().getX();
		final double dZ = e.getFrom().getZ() - e.getTo().getZ();
	    final double speed = Math.sqrt(dZ * dZ + dX * dX);	
	    
	    final double ns7maxSpeed = ConfigManager.getCheckConfigValue(Check.ns7, "maxSpeed") + (GroundUtils.isPlayerPositionOnGround(e.getFrom()) == false ? 0 : 0.06);
	    if(Web.inWeb(e.getTo(), e.getFrom()) 
    			&& speed > ns7maxSpeed
    			&& Manager.getPlayerData(player).getSpeed1NoWebSpeed() - 0.01 < speed) {
    		if(Manager.debug(player, Check.ns7, ns7maxSpeed + " < " + Double.valueOf(Math.round(speed * 1000)) / 1000)) {
    			Flag.backPort(player);
    		}
    	}
    	
    	Manager.getPlayerData(player).setSpeed1NoWebSpeed(speed);
	    
	    for(final Entity entity : player.getWorld().getEntities()) {
        	if(entity.getLocation().distance(e.getTo()) < 4 && entity.getType() == EntityType.BOAT) {
        		return;
        	}
        }
	    
	    if(!player.hasPotionEffect(PotionEffectType.SPEED) 
	    		&& BlockChecks.isOnWater(player.getLocation())
	    		&& !BlockChecks.onBlock(e.getTo())
	    		&& !BlockChecks.onBlock(e.getFrom())) {
	    	
	    	double FromY = e.getFrom().getY();
	    	
	    	if(FromY > e.getTo().getY()) {
	    		FromY = e.getTo().getY();
	    	}
	    	
	    	Location locold = new Location(player.getWorld(), e.getFrom().getX(), FromY, e.getFrom().getZ());
			Location locnew = new Location(player.getWorld(), e.getTo().getX(), e.getTo().getY(), e.getTo().getZ());
				
			if(Manager.getPlayerData(player).getSpeed3LastSpeedInWater() != 0	
					&& locnew.distance(locold) > ConfigManager.getCheckConfigValue(Check.sw1, "maxSpeed")
					&& Manager.getPlayerData(player).getSpeed3LastSpeedInWater() - 0.003 <= locnew.distance(locold)) {
				if(player.getInventory().getBoots() != null) {
					if(player.getInventory().getBoots().containsEnchantment(Enchantment.DEPTH_STRIDER)) {
						if(locnew.distance(locold) < ConfigManager.getCheckConfigValue(Check.sw1, "maxSpeed") + 0.2 * player.getInventory().getBoots().getEnchantmentLevel(Enchantment.DEPTH_STRIDER) / 3) {
							Manager.getPlayerData(player).setSpeed3LastSpeedInWater(locnew.distance(locold));	
							return;
						}
					}
				}
				
				double value = Double.valueOf(Math.round(locnew.distance(locold) * 100)) / 100;
				if(Manager.debug(player, Check.sw1, "Value: " + value)) {
					Flag.backPort(player);
				}
			}
			Manager.getPlayerData(player).setSpeed3LastSpeedInWater(locnew.distance(locold));				
	    } else {
	    	Manager.getPlayerData(player).setSpeed3LastSpeedInWater(0);
	    }	
    }
    
    public void onToggleSneak(final PlayerToggleSneakEvent e) {    	
    	final Player player = e.getPlayer();
    	
    	if(Manager.isOP(player)) {
            return;
        }
    	
    	Manager.getPlayerData(player).setSneakToggles(Manager.getPlayerData(player).getSneakToggles() + 1);    	    
    }
    
    public void onTick() {    
    	for(final Player player : Bukkit.getOnlinePlayers()) {
    		if(Manager.getPlayerData(player).getSneakResetTimer().isDelayComplete(1000L)) {
    			Manager.getPlayerData(player).getSneakResetTimer().setLastMS();
    			
    			if(Manager.getPlayerData(player).getSneakToggles() >= ConfigManager.getCheckConfigValue(Check.sn1, "maxValue")) {
            		Manager.debug(player, Check.sn1, "Sneakechanges: " + Manager.getPlayerData(player).getSneakToggles());
    		    }
    			
    			Manager.getPlayerData(player).setSneakToggles(0);
    		}
    	}    			
    }
}
