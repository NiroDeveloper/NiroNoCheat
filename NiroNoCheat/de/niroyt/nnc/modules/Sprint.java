package de.niroyt.nnc.modules;

import org.bukkit.entity.*;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleSprintEvent;

import de.niroyt.nnc.enums.Check;
import de.niroyt.nnc.manager.*;
import de.niroyt.nnc.utils.ConfigManager;
import de.niroyt.nnc.utils.Flag;


public class Sprint extends Module {
        
    public static boolean DoSprint(final Player player) {
    	if(Manager.getPlayerData(player).getSprintValue() == 0) {
    		return false;
    	}
    	return true;
    }
    
    public static boolean DoNotSprint(final Player player) {
    	return !DoSprint(player);
    }
    
    public void onDamageByEntityEvent(final EntityDamageByEntityEvent e) {
    	if(e.getDamager() instanceof Player) {
    		final Player player = (Player) e.getDamager();
    		
    		Manager.getPlayerData(player).setSprinting(true);
    	}
    }
    
    public void onToggleSprint(final PlayerToggleSprintEvent e) {
    	final Player player = e.getPlayer();
    	
    	Manager.getPlayerData(player).setSprinting(e.isSprinting());
    	
    	if(Manager.isOP(player)) {
    		return;
    	}
    	    	
    	final long time = (System.nanoTime() - Manager.getPlayerData(player).getSprintLastToggle()) / 100000;
    	if(time < ConfigManager.getCheckConfigValue(Check.sp2, "minValue")) {    		
    		if(System.currentTimeMillis() - Manager.getPlayerData(player).getSprintLastToggleFlag() < 2000) {
    			if(Manager.debug(player, Check.sp2, "Delay: " + time + "ns")) {
    				Flag.backPort(player);
    				Killaura.setHitLock(player, 4000);
    			}
    		}    		
    		
    		Manager.getPlayerData(player).setSprintLastToggleFlag(System.currentTimeMillis());
		}
		
    	Manager.getPlayerData(player).setSprintLastToggle(System.nanoTime());
    }
    
    public void onMove(final PlayerMoveEvent e) {    	
    	final Player player = e.getPlayer();
    	
	    if(Manager.getPlayerData(player).isSprinting()) {
	    	Manager.getPlayerData(player).setSprintValue(7);  		
	    } else {
	    	if(Manager.getPlayerData(player).getSprintValue() != 0) {
	    		Manager.getPlayerData(player).setSprintValue(Manager.getPlayerData(player).getSprintValue() - 1);
	    	}	    		
	    }
	    
    	if(Manager.isOP(player)) {
        	return;
        }
    	
    	if(player.getFoodLevel() <= 5 && player.isSprinting()) {
    		if(Manager.debug(player, Check.sp1, "")) {
    			Flag.backPort(player);
    		}
    	}
    }
}
