package de.niroyt.nnc.modules.fly;

import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;

import de.niroyt.nnc.manager.Manager;
import de.niroyt.nnc.utils.BlockChecks;

public class OnGround {

	public static HashMap<String, String> getValues(final Player player, final PlayerMoveEvent e, final boolean onGroundFrom) {
		final HashMap<String, String> Values = new HashMap<String, String>(); 
		
		final double oldValue = Manager.getPlayerData(player).getFlyLastPosDiff();
		
		Values.put("AA", "=0.0");
		
		if(BlockChecks.inBlock(e.getFrom().clone().add(0, -1, 0), Material.SLIME_BLOCK) && oldValue < 0.1) {
			Values.put("CH", "<<-0.12&0");
       	}
		
		if(BlockChecks.onSteps(player) && (oldValue == 0 || oldValue == 0.5)) {
			Values.put("AE", "=0.5");
		}
		         	
		if(onGroundFrom) {
			Values.put("AC", "<=0.0&0.625");
		} else {
			double value = oldValue - oldValue * 0.02;
	    	double min = value - 0.0785;
			Values.put("AH", "<<" + min + "&0");
			
			if(BlockChecks.onSteps(e.getTo()) && oldValue < 0) {
				Values.put("AK", "<<0&0.5");
			}
			
			if(BlockChecks.BlockOverPlayer(e.getTo()) || BlockChecks.BlockOverPlayer(e.getFrom())) {
				Values.put("BB", "=-0.11760000228882461");
	    	} 
			
			if(oldValue < -0.08 && oldValue > -0.12) {
				Values.put("BU", "<<0&0.5");
			}
		}
		
		if(BlockChecks.inBlock(e.getTo().clone().add(0, -1, 0), Material.LADDER) || BlockChecks.inBlock(e.getTo().clone().add(0, -1, 0), Material.VINE)) {    			
			Values.put("BI", "<<-0.1&0.1");
		} else {
			Manager.getPlayerData(player).setFlyTouchedLadder(false);
		}
		
		return Values;
	}
}
