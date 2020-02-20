package de.niroyt.nnc.modules;


import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;

import de.niroyt.nnc.Main;
import de.niroyt.nnc.enums.Check;
import de.niroyt.nnc.manager.Manager;
import de.niroyt.nnc.manager.Module;
import de.niroyt.nnc.utils.ConfigManager;

public class FastUse extends Module {
        
    public void onInteract(final PlayerInteractEvent e) {    	
    	Player player = e.getPlayer();
    	
    	if((e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) 
    			&& (player.getItemInHand().getType() == Material.APPLE
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
    		
    		if(System.currentTimeMillis() - Manager.getPlayerData(player).getFastUseTime() > 0) {
    			Manager.getPlayerData(player).setFastUseTime(System.currentTimeMillis());
    		}    		
    	}
    }
    
    public void onTick() {
    	for(final Player player : Bukkit.getOnlinePlayers()) {	
    		if((System.currentTimeMillis()) - Manager.getPlayerData(player).getFastUseTime() >= 1800) {
    			Manager.getPlayerData(player).setFastUseTime(0);
    		}
    	}
    }
    
    public void onDropItem(final PlayerDropItemEvent e) {
    	final Player player = e.getPlayer();
    	
    	Manager.getPlayerData(player).setFastUseTime(System.currentTimeMillis() + 2000 - (System.currentTimeMillis() - Manager.getPlayerData(player).getFastUseTime()));
    }
    
    public void onItemConsume(final PlayerItemConsumeEvent e) {
    	final Player player = e.getPlayer();  	
    	
    	if(Manager.isOP(player)) {
        	return;
        }
    	
    	if(System.currentTimeMillis() - Manager.getPlayerData(player).getFastUseTime() + Manager.ping(player) + Main.getServerLag() < ConfigManager.getCheckConfigValue(Check.fu1, "minValue")
    			&& System.currentTimeMillis() - Manager.getPlayerData(player).getFastUseTime() > 0) {
    		if(Manager.debug(player, Check.fu1, "Value: " + (System.currentTimeMillis() - Manager.getPlayerData(player).getFastUseTime()) + "ms")) {
    			e.setCancelled(true);
    			e.getPlayer().getInventory().setHeldItemSlot((player.getInventory().getHeldItemSlot() + 9 - 1) % 9);
    		}
    	}   		
    	Manager.getPlayerData(player).setFastUseTime(0);
    }
}