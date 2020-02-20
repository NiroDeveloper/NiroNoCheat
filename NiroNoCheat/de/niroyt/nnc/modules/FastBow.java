package de.niroyt.nnc.modules;

import org.bukkit.*;
import org.bukkit.event.player.PlayerInteractEvent;

import de.niroyt.nnc.enums.Check;
import de.niroyt.nnc.manager.*;
import de.niroyt.nnc.utils.ConfigManager;

import org.bukkit.entity.*;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityShootBowEvent;

public class FastBow extends Module {
	    
    public void onInteract(final PlayerInteractEvent e) {    	
    	final Player player = e.getPlayer();
    	
    	if((e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) 
    			&& (player.getItemInHand().getType() == Material.BOW)) {
    		
    		Manager.getPlayerData(player).setFastBowTime(System.currentTimeMillis());
    	}
    }
    
    public void onEntityShootBow(final EntityShootBowEvent e) {    	
    	if(e.getEntity() instanceof Player) {
        	final Player player = (Player) e.getEntity();  
        	final double force = e.getForce();
        	        	
        	if(Manager.isOP(player)) {
	        	return;
	        }
        	  		
        	if((System.currentTimeMillis() - Manager.getPlayerData(player).getFastBowTime()) / force < ConfigManager.getCheckConfigValue(Check.fb1, "minValue") && force > 0.85) {
        		if(Manager.debug(player, Check.fb1, "Value: " + ((System.currentTimeMillis() - Manager.getPlayerData(player).getFastBowTime()) / force))) {        			
        			e.setCancelled(true);
        			player.getInventory().setHeldItemSlot((player.getInventory().getHeldItemSlot() + 9 - 1) % 9);
        		}
        	} else if((System.currentTimeMillis() - Manager.getPlayerData(player).getFastBowTime()) / force < ConfigManager.getCheckConfigValue(Check.fb2, "minValue") && force > 0.42) {
            	if(Manager.debug(player, Check.fb2, "Value: " + ((System.currentTimeMillis() - Manager.getPlayerData(player).getFastBowTime()) / force))) {            			
            		e.setCancelled(true);
            		player.getInventory().setHeldItemSlot((player.getInventory().getHeldItemSlot() + 9 - 1) % 9);
            	}
            }
        	Manager.getPlayerData(player).setFastBowTime(0);
    	}
    }
}
