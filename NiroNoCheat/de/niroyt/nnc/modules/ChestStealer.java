package de.niroyt.nnc.modules;


import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;

import de.niroyt.nnc.Main;
import de.niroyt.nnc.enums.Check;
import de.niroyt.nnc.manager.Manager;
import de.niroyt.nnc.manager.Module;
import de.niroyt.nnc.utils.ConfigManager;

public class ChestStealer extends Module {
			    
    public void onInventoryOpen(final InventoryOpenEvent e) {    	
    	final Player player = (Player) e.getPlayer();

    	if(player.getOpenInventory() != null) {
    		return;
    	}
    	    	
    	if(e.getInventory().getType() == InventoryType.CHEST || e.getInventory().getType() == InventoryType.ENDER_CHEST) {
    		Manager.getPlayerData(player).setChestStealerLastChest(System.nanoTime());
    	}
    }
    
    public void onInventoryClickHighest(final InventoryClickEvent e) {
    	if(!(e.getWhoClicked() instanceof Player)) {
    		return;
    	}
    	
    	final Player player = (Player) e.getWhoClicked();
    	
    	if(Manager.isOP(player)) {
    		return;
    	}
    	
    	if(Manager.ping(player) + Main.getServerLag() < 250) {
    		if((System.nanoTime() - Manager.getPlayerData(player).getChestStealerLastChest()) / 1000000 < ConfigManager.getCheckConfigValue(Check.cs2, "minValue")) {
    			if(Manager.debug(player, Check.cs2, "")) {
    				e.setCancelled(true);
    	    			
    				player.closeInventory();
    				
    				Inventory.InventoryClose(player);
    			} 
    		}
    	}
    	    
    	if(e.isCancelled()) {
    		return;
    	}
    	if(player.getGameMode() == GameMode.CREATIVE) {
    		return;
    	}
    	if(e.getInventory().getType() == InventoryType.PLAYER) {
    		return;
    	}    	
    	if(e.getAction() == InventoryAction.NOTHING 
    			|| e.getAction() == InventoryAction.PLACE_ALL 
    			|| e.getAction() == InventoryAction.PLACE_ONE
    			|| e.getAction() == InventoryAction.PLACE_SOME) {
    		return;
    	}
    	
    	if(e.getCurrentItem().getType() == Manager.getPlayerData(player).getChestStealerLastItem()) {
    		return;
    	}
    	
    	Manager.getPlayerData(player).setChestStealerLastItem(e.getCurrentItem().getType());
    	    	
    	if(Manager.getPlayerData(player).getChestStealerResetTime().isDelayComplete(300L)) {
    		Manager.getPlayerData(player).getChestStealerResetTime().setLastMS();
    		Manager.getPlayerData(player).setChestStealerFlags(0);
    	}
    	
    	if(Manager.getPlayerData(player).getChestStealerFlags() == 0) {
    		Manager.getPlayerData(player).getChestStealerResetTime().setLastMS();
    	}
    	    
    	if (Manager.getPlayerData(player).getChestStealerFlags() >= ConfigManager.getCheckConfigValue(Check.cs1, "maxValue") + Math.floor(Manager.ping(player) / 40)) { 
    		if(Manager.debug(player, Check.cs1, "")) {
    			e.setCancelled(true);
    	    			
    			player.closeInventory();
    				
    			Inventory.InventoryClose(player);
    		}    			
    	} else {
    		Manager.getPlayerData(player).setChestStealerFlags(Manager.getPlayerData(player).getChestStealerFlags() + 1);
    	}
    } 
}
