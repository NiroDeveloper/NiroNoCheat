package de.niroyt.nnc.modules;

import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import de.niroyt.nnc.Main;
import de.niroyt.nnc.enums.Check;
import de.niroyt.nnc.manager.Manager;
import de.niroyt.nnc.manager.Module;
import de.niroyt.nnc.utils.BlockChecks;
import de.niroyt.nnc.utils.ConfigManager;

public class GhostHand extends Module {
	          
    public void onTick() {
    	for(final Player player : Bukkit.getOnlinePlayers()) {
    		if(Manager.getPlayerData(player).getGhostHandKillauraTimer().isDelayComplete(2500L)) {
    			Manager.getPlayerData(player).getGhostHandKillauraTimer().setLastMS();
        		
    			Manager.getPlayerData(player).setGhostHandKillauraClickCount((int) ((Manager.getPlayerData(player).getGhostHandKillauraClickCount() - 1) * 0.8));
        	}
    		if(Manager.getPlayerData(player).getGhostHandTimer().isDelayComplete(300L)) {
    			Manager.getPlayerData(player).getGhostHandTimer().setLastMS();
        		
    			if(Manager.getPlayerData(player).getGhostHandClicks() > 0) {
    				Manager.getPlayerData(player).setGhostHandClicks(Manager.getPlayerData(player).getGhostHandClicks() - 1);
    			}  
        	}
    	}
    }
    
    public void onSwingArm(final Player player) {    	
    	if(Manager.isOP(player)) {
        	return;
        }
    	
    	if(Manager.getPlayerData(player).getGhostHandKillauraClickCount() < 0) {
    		Manager.getPlayerData(player).setGhostHandKillauraClickCount(0);
    	}
    	
    	if(Manager.getPlayerData(player).getGhostHandKillauraClickCount() >= ConfigManager.getCheckConfigValue(Check.gh2, "maxValue")) {
    		if(Manager.debug(player, Check.gh2, "Weird Behaviour")) {
    			Killaura.setHitLock(player, 2500);
    		}
    		Manager.getPlayerData(player).setGhostHandKillauraClickCount(Manager.getPlayerData(player).getGhostHandKillauraClickCount() - 2);
    	}
    	
    	if(player.getTargetBlock((Set)null, 4) != null 
    			&& !Manager.getPlayerData(player).isGhostHandPlayersInBreakBlock()
    			&& player.getGameMode() != GameMode.ADVENTURE) {
    		if(player.getTargetBlock((Set)null, 4).getType() == Material.AIR) {
    			return;
    		}
    		if(!BlockChecks.isFullBlock(player.getTargetBlock((Set)null, 4).getType())) {
    			return;
    		}
    		
    		Manager.getPlayerData(player).setGhostHandKillauraClickCount(Manager.getPlayerData(player).getGhostHandKillauraClickCount() + 1);
    	}
    }
    
    public void onInteract(final PlayerInteractEvent e) {
    	final Player player = e.getPlayer();
    	
    	if((player.getTargetBlock((Set)null, 4).getType() != Material.AIR && e.getAction() == Action.LEFT_CLICK_AIR)
    			|| (e.getAction() == Action.LEFT_CLICK_BLOCK || e.getAction() == Action.RIGHT_CLICK_BLOCK)) {
    		Manager.getPlayerData(player).setGhostHandKillauraClickCount(Manager.getPlayerData(player).getGhostHandKillauraClickCount() - 1);
    	}
    	    	
    	if(e.getClickedBlock() != null) {
    		if(e.getClickedBlock().getLocation().add(0.5, 0.5, 0.5).distance(player.getLocation()) < 0.75) {
    			Manager.getPlayerData(player).setGhostHandKillauraClickCount(Manager.getPlayerData(player).getGhostHandKillauraClickCount() - 1);
    		}
    	}
    	
    	if(e.getAction() != Action.RIGHT_CLICK_BLOCK) {
    		return;
    	}
    	
    	if(Manager.isOP(player)) {
        	return;
        }
    	
    	if(BlockChecks.isFullBlock(player.getTargetBlock((Set)null, 5).getType())) {
    		if(player.getTargetBlock((Set)null, 5).getType() != e.getClickedBlock().getType()) {
    			Manager.getPlayerData(player).setGhostHandClicks(Manager.getPlayerData(player).getGhostHandClicks() + 1);
    			
    			if(Manager.getPlayerData(player).getGhostHandClicks() >= ConfigManager.getCheckConfigValue(Check.gh1, "maxValue")) {
    				Manager.debug(player, Check.gh1, "");
    				Manager.getPlayerData(player).setGhostHandClicks(0);
    			}
    			
    			if(!Main.isDisabled()) {
    				e.setCancelled(true);
    			}    			
    		}
    	}
    	
    	if(e.isCancelled()) {
    		Manager.getPlayerData(player).setGhostHandKillauraClickCount(Manager.getPlayerData(player).getGhostHandKillauraClickCount() - 1);
    	}
    }    
}