package de.niroyt.nnc.modules;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;

import de.niroyt.nnc.enums.Check;
import de.niroyt.nnc.manager.Manager;
import de.niroyt.nnc.manager.Module;
import de.niroyt.nnc.utils.BlockChecks;
import de.niroyt.nnc.utils.ConfigManager;
import de.niroyt.nnc.utils.GroundUtils;

public class NoFallDamage extends Module {
	   	    
    public static float getFallHeigth(final Player player) {
		return Manager.getPlayerData(player).getNoFallFallHeight();
    }
    
    public static void addFallHeigth(final Player player, final double value) {
    	Manager.getPlayerData(player).setNoFallFallHeight((float) (Manager.getPlayerData(player).getNoFallFallHeight() + value));
    }
     
    public void onMove(final PlayerMoveEvent e) {
    	final Player player = e.getPlayer();
    	
    	if(Manager.AllowedSpeedFly(player)) {
        	return;
        }
    	
    	final double difY = e.getFrom().getY() - e.getTo().getY();
    	if(difY >= 0 && !player.isDead() 
    			&& BlockChecks.canCheckFly(player) 
    			&& BlockChecks.NotInLiquid(player)
    			&& !BlockChecks.inWeb(player) 
    			&& !BlockChecks.onSteps(player)
    			&& !GroundUtils.isPlayerPositionOnGround(e.getTo())
    			&& !GroundUtils.isPlayerPositionOnGround(e.getFrom())
    			&& !(difY == 0.07840000152587834 && BlockChecks.isSaveOnGround(e.getFrom()))) {
    		
    		addFallHeigth(player, difY);
    		    	
    		final double value = Manager.getPlayerData(player).getNoFallFallHeight() - player.getFallDistance() - player.getFallDistance() * 0.06;
    		if(value >= ConfigManager.getCheckConfigValue(Check.nf1, "maxValue")) {
    			if(Manager.debug(player, Check.nf1, "Value: " + Double.valueOf(Math.round(value * 1000)) / 1000)) {
    				player.setFallDistance(getFallHeigth(player));
    			}    			
    		}
    	} else {
    		Manager.getPlayerData(player).setNoFallFallHeight(0);
    	}
    }    
}