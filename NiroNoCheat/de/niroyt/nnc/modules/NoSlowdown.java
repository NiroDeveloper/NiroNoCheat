package de.niroyt.nnc.modules;


import de.niroyt.nnc.enums.Check;
import de.niroyt.nnc.manager.Manager;
import de.niroyt.nnc.manager.Module;
import de.niroyt.nnc.utils.ConfigManager;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public class NoSlowdown extends Module {
	    	
	public static void setUseItem(final Player player) {		
		if((player.getItemInHand().getType() == Material.BOW && player.getInventory().contains(Material.ARROW))
				|| player.getItemInHand().getType() == Material.APPLE
	   			|| player.getItemInHand().getType() == Material.GOLDEN_APPLE
	   			|| player.getItemInHand().getType() == Material.GOLDEN_CARROT
	   			|| player.getItemInHand().getType() == Material.COOKED_BEEF
	   			|| player.getItemInHand().getType() == Material.RAW_BEEF
	   			|| player.getItemInHand().getType() == Material.COOKED_CHICKEN
	   			|| player.getItemInHand().getType() == Material.RAW_CHICKEN
	   			|| player.getItemInHand().getType() == Material.RAW_FISH
	   			|| player.getItemInHand().getType() == Material.COOKED_FISH
	   			|| player.getItemInHand().getType() == Material.BREAD
	   			|| player.getItemInHand().getType() == Material.BAKED_POTATO
	   			|| player.getItemInHand().getType() == Material.POISONOUS_POTATO
	   			|| player.getItemInHand().getType() == Material.POTATO_ITEM) {
			Manager.getPlayerData(player).setNoSlowUseItem(System.currentTimeMillis());		
		}		
	}
	
	public static void setNotUsingItem(final Player player) {
		Manager.getPlayerData(player).setNoSlowUseItem(0);
	}
	
	public static boolean isUsingItem(final Player player) {
		return Manager.getPlayerData(player).getNoSlowUseItem() != 0;
	}
    
    public void onDamageByEntityEvent(final EntityDamageByEntityEvent e) {
    	if(e.getDamager() instanceof Player) {
    		final Player player = (Player) e.getDamager();
    		
    		if(Manager.isOP(player)) {
        		return;
        	}
    		    		
    		if(isUsingItem(player)) {
    			if(System.currentTimeMillis() - Manager.getPlayerData(player).getNoSlowUseItem() < 99) {
    				return;
    			}
    			if(Manager.debug(player, Check.ns5, "")) {
    				Killaura.setHitLock(player, 5000);
    				e.setCancelled(true);
    			}
    		}
    	}
    }
    
    public void onMove(final PlayerMoveEvent e) {
    	final Player player = e.getPlayer();
    	
    	if(Manager.isOP(player)) {
    		return;
    	}
    	
		if(!((player.getItemInHand().getType() == Material.BOW && player.getInventory().contains(Material.ARROW))
			|| player.getItemInHand().getType() == Material.APPLE
	    	|| player.getItemInHand().getType() == Material.GOLDEN_APPLE
	    	|| player.getItemInHand().getType() == Material.GOLDEN_CARROT
	    	|| player.getItemInHand().getType() == Material.COOKED_BEEF
	    	|| player.getItemInHand().getType() == Material.RAW_BEEF
	    	|| player.getItemInHand().getType() == Material.COOKED_CHICKEN
	    	|| player.getItemInHand().getType() == Material.RAW_CHICKEN
	    	|| player.getItemInHand().getType() == Material.RAW_FISH
	    	|| player.getItemInHand().getType() == Material.COOKED_FISH
	    	|| player.getItemInHand().getType() == Material.BREAD
	    	|| player.getItemInHand().getType() == Material.BAKED_POTATO
	    	|| player.getItemInHand().getType() == Material.POISONOUS_POTATO
	    	|| player.getItemInHand().getType() == Material.POTATO_ITEM)) {
			
			setNotUsingItem(player);
		}
		
		if(isUsingItem(player) && (player.getGameMode() == GameMode.CREATIVE || player.getGameMode() == GameMode.SPECTATOR)) {
			setNotUsingItem(player);
		}
    	
    	boolean Blocking = player.isBlocking();
    	
    	final boolean canCheckSprint = (System.nanoTime() - Manager.getPlayerData(player).getKillauraLastHit()) / 1000000 > 1000;
    	
    	if(Blocking && player.isSprinting() && canCheckSprint) {   
    		Manager.getPlayerData(player).setNoSlowFlagCounter(Manager.getPlayerData(player).getNoSlowFlagCounter() + 1);
    		    	    		
    		if(Manager.getPlayerData(player).getNoSlowFlagCounter() >= ConfigManager.getCheckConfigValue(Check.ns4, "maxValue")) {
    			if(Manager.debug(player, Check.ns4, "")) {
    				player.getInventory().setHeldItemSlot((player.getInventory().getHeldItemSlot() + 9 - 1) % 9);
					return;
    			}
    			Manager.getPlayerData(player).setNoSlowFlagCounter(0);  			
    		}						
    	}
    	
   		if(!Blocking) {
   			Blocking = Manager.getPlayerData(player).getNoSlowAntiSword() > ConfigManager.getCheckConfigValue(Check.ns3, "maxValue");
   		}
   		   		
   		if(Manager.getPlayerData(player).getNoSlowAntiSword() > ConfigManager.getCheckConfigValue(Check.ns3, "maxValue") * 1.2) {
   			if(Manager.debug(player, Check.ns3, "Value: " + Manager.getPlayerData(player).getNoSlowAntiSword() * 1.2)) {
   				player.getInventory().setHeldItemSlot((player.getInventory().getHeldItemSlot() + 9 - 1) % 9);
   				return;
   			}
       	}
    	  	   
    	if(isUsingItem(player) && player.isSprinting() 
    			&& System.currentTimeMillis() - Manager.getPlayerData(player).getNoSlowUseItem() < 1200
    			&& System.currentTimeMillis() - Manager.getPlayerData(player).getNoSlowUseItem() > 200) {
    		Manager.getPlayerData(player).setNoSlowFlagCounter(Manager.getPlayerData(player).getNoSlowFlagCounter() + 1);
    		    	    		
    		if(Manager.getPlayerData(player).getNoSlowFlagCounter() >= ConfigManager.getCheckConfigValue(Check.ns1, "maxValue")) {
    			if(Manager.debug(player, Check.ns1, "")) {
    				setNotUsingItem(player);
    				player.getInventory().setHeldItemSlot((player.getInventory().getHeldItemSlot() + 9 - 1) % 9);
    				return;
    			}
    			Manager.getPlayerData(player).setNoSlowFlagCounter(0);   			
    		}
    	}
    	
    	final double dX = e.getTo().getX() - e.getFrom().getX();
    	final double dZ = e.getTo().getZ() - e.getFrom().getZ();
    	final double speed = Math.sqrt(dZ*dZ + dX*dX);
    	
    	double maxSpeed = ConfigManager.getCheckConfigValue(Check.ns2, "maxSpeed") * 1.55;
    	if(player.isSneaking()) {
    		maxSpeed *= 0.75;
    	} 
    	if(Blocking && !player.isBlocking()) {
    		maxSpeed *= 2;
    	}
    	
    	if((isUsingItem(player) && (System.currentTimeMillis() - Manager.getPlayerData(player).getNoSlowUseItem() < 1100 || player.getItemInHand().getType() == Material.BOW)) || Blocking) {
    		if(Manager.getPlayerData(player).getNoSlowOldSpeed() - 0.005 <= speed && speed > maxSpeed && (e.getFrom().getY() % 0.5 != 0 || e.getFrom().getY() - e.getTo().getY() == 0)) {
    			Manager.getPlayerData(player).setNoSlowFlagCounter(Manager.getPlayerData(player).getNoSlowFlagCounter() + 2);
    			
    			if(Manager.getPlayerData(player).getNoSlowFlagCounter() >= ConfigManager.getCheckConfigValue(Check.ns2, "maxValue")) {
    				if(Manager.debug(player, Check.ns2, (Double.valueOf(Math.round(speed / 1.55 * 1000)) / 1000) + " > " + (Double.valueOf(Math.round((maxSpeed / 1.55) * 1000)) / 1000) + ", Blocktogglespeed: " + Manager.getPlayerData(player).getNoSlowAntiSword())) {
    					setNotUsingItem(player);  
    					player.getInventory().setHeldItemSlot((player.getInventory().getHeldItemSlot() + 9 - 1) % 9);
    				}
    				Manager.getPlayerData(player).setNoSlowFlagCounter(0);   
    			}    				
    		}
    		Manager.getPlayerData(player).setNoSlowOldSpeed(speed);
    	} else {
    		Manager.getPlayerData(player).setNoSlowFlagCounter(0);   
    		Manager.getPlayerData(player).setNoSlowFlagCounter(0);   
    	}
	}    
    
    public void onTick() { 
    	for(final Player player : Bukkit.getOnlinePlayers()) {
    		Manager.getPlayerData(player).setNoSlowAntiSword((int) (Manager.getPlayerData(player).getNoSlowAntiSword() / 1.2));
    	}
    }
    
    public void onRespawn(PlayerRespawnEvent e) {
    	final Player player = e.getPlayer();
    	
    	if(isUsingItem(player)) {
    		setNotUsingItem(player);
    	}
    }
}
