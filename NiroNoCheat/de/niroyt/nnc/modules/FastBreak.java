package de.niroyt.nnc.modules;

import java.util.Set;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import de.niroyt.nnc.enums.Check;
import de.niroyt.nnc.manager.Manager;
import de.niroyt.nnc.manager.Module;
import de.niroyt.nnc.utils.BlockChecks;

public class FastBreak extends Module {
	        
    public static void startDestroyBlock(final Player player, final Location l) {
    	Manager.getPlayerData(player).setFastBreakStartDestroyBlock(l);
    	
    	Manager.getPlayerData(player).setFastBreakStartDestroyTime(System.currentTimeMillis());
    }
    
    public static void stopDestroyBlock(final Player player, final Location l) {    	
    	if(Manager.getPlayerData(player).getFastBreakStartDestroyBlock() != null) {
    		if(Manager.getPlayerData(player).getFastBreakStartDestroyTime() == System.currentTimeMillis()) {
    			Manager.getPlayerData(player).setFastBreakBlockToFlagPlayer(l);
    		}
    	} 
    }
    
    public void onInteract(final PlayerInteractEvent e) {
    	final Player player = e.getPlayer();
    	
    	if(Manager.isOP(player)) {
        	return;
        }
    	
    	if(e.getAction() == Action.LEFT_CLICK_BLOCK) {
    		Manager.getPlayerData(player).setFastBreakLastInteract(System.currentTimeMillis());
    	}
    }
        
    public void onBlockBreak(final BlockBreakEvent e) {
    	final Player player = e.getPlayer();
    	
    	if(Manager.isOP(player)) {
    		return;
    	}	        
    			
    	if(e.getBlock().getLocation() == Manager.getPlayerData(player).getFastBreakBlockToFlagPlayer()) {
    		if(Manager.debug(player, Check.fbr2, "")) {
    			e.setCancelled(true);
    		}    			
    		Manager.getPlayerData(player).setFastBreakBlockToFlagPlayer(null);
    	}
    	
    	if(System.currentTimeMillis() - Manager.getPlayerData(player).getFastBreakLastInteract() <= 1)  {
    		return;
    	}	     
    	
        if(!player.getTargetBlock((Set)null, 5).getType().equals((Object)e.getBlock().getType()) 
        		&& BlockChecks.isFullBlock(player.getTargetBlock((Set)null, 5).getType())
        		&& player.getGameMode() != GameMode.CREATIVE) {
        	if(e.getBlock().getType() == Material.BED_BLOCK) {
        		boolean action = Manager.debug(player, Check.fbr3, "");
        	} else if(Manager.debug(player, Check.fbr1, "Blocktype: " + player.getTargetBlock((Set)null, 5).getType())) {
        		e.setCancelled(true);
        		Manager.getPlayerData(player).setWorldPlaceBlockLock(System.currentTimeMillis() + 5000);
        	}       		
        }        
    }
}