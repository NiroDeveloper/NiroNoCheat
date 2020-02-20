package de.niroyt.nnc.modules;


import org.bukkit.event.entity.*;
import org.bukkit.potion.PotionEffectType;

import de.niroyt.nnc.Main;
import de.niroyt.nnc.enums.Check;
import de.niroyt.nnc.manager.*;

import org.bukkit.*;
import org.bukkit.entity.*;

public class FastHeal extends Module {
	        
    public void onEntityRegainHealth(final EntityRegainHealthEvent event) {        
        if(event.getRegainReason() != EntityRegainHealthEvent.RegainReason.EATING && event.getRegainReason() != EntityRegainHealthEvent.RegainReason.REGEN && event.getRegainReason() != EntityRegainHealthEvent.RegainReason.SATIATED) {
            return;
        }
        
        if(event.getEntity().getWorld().getDifficulty() != Difficulty.PEACEFUL) {
        	 if (event.getEntity() instanceof Player) { 	        	
 	            final Player player = (Player)event.getEntity();
 	            
 	            if(Manager.isOP(player)) {
 	            	return;
 	            }
 	            
 	            if(player.hasPotionEffect(PotionEffectType.REGENERATION)) {
 	            	return;
 	            }
 	            
 	            if (Manager.getPlayerData(player).getFastHealTime() == 0) {
 	            	Manager.getPlayerData(player).setFastHealAmount(player.getHealth());
 	            	Manager.getPlayerData(player).setFastHealTime(System.currentTimeMillis());
 	            } else if(System.currentTimeMillis() - Manager.getPlayerData(player).getFastHealTime() < (Main.MinecraftVersion.equalsIgnoreCase("v1_8_R3") ? 1000 : 400)) { 	            	
 	                if(Manager.getPlayerData(player).getFastHealAmount() < player.getHealth() + event.getAmount()) { 	                    
 	                	if(Manager.debug(player, Check.fh1, "Delay: " + (System.currentTimeMillis() - Manager.getPlayerData(player).getFastHealTime()) + "ms")) {
 	                		event.setCancelled(true);   
 	                	}         	
 	                }
 	                Manager.getPlayerData(player).setFastHealAmount(0);
	            	Manager.getPlayerData(player).setFastHealTime(0);
 	            } else {
 	            	Manager.getPlayerData(player).setFastHealAmount(0);
	            	Manager.getPlayerData(player).setFastHealTime(0);
 	            }
 	        }
        }
    }
}
