package de.niroyt.nnc.modules.fly;

import java.util.HashMap;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;

import de.niroyt.nnc.manager.Manager;
import de.niroyt.nnc.utils.BlockChecks;

public class Ladder {

	public static HashMap<String, String> getValues(final Player player, final PlayerMoveEvent e, final boolean onGroundFrom) {
		final HashMap<String, String> Values = new HashMap<String, String>(); 
		
		final double oldValue = Manager.getPlayerData(player).getFlyLastPosDiff();
		
		if(onGroundFrom) {
    		Values.put("AU", "<<0.41&0.42"); 
    		Values.put("AV", "=-0.07840000152589255");
    		Values.put("AW", "=0.33319999363422426");
    		Values.put("AX", "=0.11760000228882461");
    		
    		if(BlockChecks.BlockOverPlayer(e.getTo()) || BlockChecks.BlockOverPlayer(e.getFrom())) {
        		Values.put("AT", "=0.20000004768371582");
        	} 
    		
    		Values.put("BK", "=-0.07840000152587834");
    	} else {            		
    		Values.put("AY", "<=" + "-0.156&0.12"); 
    		
    		if(oldValue < -0.156) {
    			Values.put("BC", "<<" + oldValue * 0.75 + "&-0.156"); 
    			
    			if(oldValue == 0.11760000228882461) {
    				Values.put("BT", "=-0.19155199856568572");
    			}
    		}
    		
    		if(oldValue < 0.12 && oldValue > 0.07) {
    			Values.put("BD", "=0.1544480052490229"); 
    		}
    		
    		if(oldValue == 0.1544480052490229) {
    			Values.put("BE", "<<-0.17&-0.156"); 
    		}
    		
    		if(oldValue > 0.12) {
    			Values.put("BF", "<<" + "0.12&" + oldValue * 0.8); 
    		}
    		
    		if(Manager.getPlayerData(player).isFlyTouchedLadder() == false) {
    			Values.put("BJ", "<<" + (oldValue - 0.1) + "&-0.156"); 
    			
    			Manager.getPlayerData(player).setFlyTouchedLadder(true);
    		}            		
    	}
		
		return Values;
	}
	
}
