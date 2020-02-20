package de.niroyt.nnc.modules;

import org.bukkit.event.entity.EntityDamageByEntityEvent;

import de.niroyt.nnc.Main;
import de.niroyt.nnc.enums.Check;
import de.niroyt.nnc.manager.*;

import org.bukkit.Bukkit;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.block.BlockBreakEvent;

public class NoSwing extends Module {
	    
    public void onDamageByEntityEvent(final EntityDamageByEntityEvent e) {
        if (!(e.getDamager() instanceof Player)) {
            return;
        }
        
        final Player player = (Player)e.getDamager();

        if(Manager.isOP(player)) {
        	return;
        }
        
        if(Manager.getPlayerData(player).getNoSwingLastSwing() == 0) {        	
        	if(onFlag(player, Check.nsw1)) {
        		e.setCancelled(true);        		
        	} 
        	return;     	
        }
        
        final Long LastSwing = Manager.getPlayerData(player).getNoSwingLastSwing();        
        final int Dauer = (int) (System.currentTimeMillis() - LastSwing);
                
        if(Dauer > Manager.ping(player) + 51 + Main.getServerLag()) {
        	e.setCancelled(true);       	
        	onFlag(player, Check.nsw2);
        }
    }
    
    public void onBlockBreak(final BlockBreakEvent e) {        
        final Player player = e.getPlayer();
        
        if(Manager.isOP(player)) {
        	return;
        }
        
        if(Manager.getPlayerData(player).getNoSwingLastSwing() == 0) {        	
        	if(onFlag(player, Check.nsw3)) {
        		e.setCancelled(true);
        		return;
        	}        	
        }
        
        final Long LastSwing = Manager.getPlayerData(player).getNoSwingLastSwing();
     
        try {
        	long Dauer = System.currentTimeMillis() - LastSwing;
            
            if(Dauer > Manager.ping(player) + 51 + Main.getServerLag()) {        	
            	if(onFlag(player, Check.nsw4)) {
            		e.setCancelled(true);
            	}
            }
		} catch (Exception e2) {}
    }
    
    public void onSwingArm(final Player player) {      	
    	if(Manager.isOP(player)) {
    		return;
    	}
    	    	
    	Manager.getPlayerData(player).setNoSwingLastSwing(System.currentTimeMillis());
        
        int entities = 0;
    	for(Entity entity : player.getLocation().getWorld().getEntities()) {
    		if(entity.getLocation().distance(player.getLocation()) < 5 
    				&& (entity instanceof Animals || entity instanceof Zombie ||  entity instanceof Player)) {  
    			entities += 1;
    		}
    	}
    	if(entities == 0) {
    		return;
    	}
    	
    	Manager.getPlayerData(player).setKillauraHitFailure(Manager.getPlayerData(player).getKillauraHitFailure() + 1);
    }
    
    public boolean onFlag(final Player player, final Check c) {    	
    	Manager.getPlayerData(player).setNoSwingFlags(Manager.getPlayerData(player).getNoSwingFlags() + 10);
    	    	
    	if(Manager.getPlayerData(player).getNoSwingFlags() >= ((c == Check.nsw1 || c == Check.nsw2) ? 16 : 11)) {    		
    		if(Manager.debug(player, c, "")) {
    			Killaura.setHitLock(player, 4000);
    			
    			return true;
    		}    		
    	}
    	return false;
    }
    
    public void onTick() {
    	for(final Player player : Bukkit.getOnlinePlayers()) {
    		Manager.getPlayerData(player).setNoSwingFlags(Manager.getPlayerData(player).getNoSwingFlags() * 0.97);
    	}
    }
}
