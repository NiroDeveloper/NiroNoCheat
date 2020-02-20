package de.niroyt.nnc.modules.fly;

import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import de.niroyt.nnc.manager.Manager;
import de.niroyt.nnc.modules.Phase;
import de.niroyt.nnc.utils.BlockChecks;
import de.niroyt.nnc.utils.GroundUtils;

public class Normal {
	
	public static HashMap<String, String> getValues(final Player player, final PlayerMoveEvent e, final boolean onGroundFrom) {
		final HashMap<String, String> Values = new HashMap<String, String>();
		
		final double oldValue = Manager.getPlayerData(player).getFlyLastPosDiff();
		
		final double value = oldValue * 0.98;
    	final double min = value - 0.0785;
       	final double max = value - 0.07835;
        	            
       	if(BlockChecks.inBlock(e.getFrom().clone().add(0, -1, 0), Material.SLIME_BLOCK) && oldValue > -0.2) {
			Values.put("CF", "<<0&" + (oldValue * -0.95));
			Values.put("CG", "<<0&0.15");
       	}
       	
        if(onGroundFrom) {
    		Values.put("AI", "=0.0");
    		Values.put("AB", "=0.41999998688697815"); 
    		Values.put("AJ", "=-0.07840000152587834");
    		
    		if(BlockChecks.BlockOverPlayer(e.getTo()) || BlockChecks.BlockOverPlayer(e.getFrom())) {
    			Values.put("AZ", "=0.20000004768371582");
        	} 
    		
    		if(GroundUtils.onSlime(e.getFrom())) {
    			double JumpValue = Manager.getPlayerData(player).getFlySlimeJumpMotion() * -0.96;
    			
    			Values.put("CD", "<<" + (JumpValue - 0.01) + "&" + (JumpValue + 0.01));
    		}
    	} else {    		
    		Values.put("AF", "<<" + min + "&" + max);
    		
    		if(Manager.getPlayerData(player).getFlyMovesSinceTeleport() <= 1) {
    			Values.put("BV", "<<" + (min - 0.001) + "&" + (max + 0.001));
    		} 
    		       		
    		if(oldValue <= -0.15522 && oldValue >= -0.15525 && Phase.inBlock(e.getFrom().clone().add(0, -1, 0))) {
    			Values.put("BW", "=0.40444491418477924");
    			Values.put("BY", "=0.0");
    		}
    		
    		if(oldValue == 0.40444491418477924) {
    			Values.put("BX", "=0.33319999363422426");
    		}
    		
    		if(oldValue == 0.41999998688697815 && Phase.inBlock(e.getFrom().clone().add(0, -1, 0))) {
    			Values.put("BZ", "=-0.07837499999999409");
    		}
    		
    		if(!BlockChecks.NotInLiquid(player) 
    				&& ((oldValue > 0.34 && oldValue < 0.35)
    						|| (oldValue > 0.08 && oldValue < 0.1))) {
    			Values.put("BL", "<<0.04&0.31");
    		}
    		
    		if(!BlockChecks.NotInLiquid(player)) {
    			Values.put("BM", "<<-0.3&" + (oldValue - 0.01));    			
    		}
    		
    		if(!BlockChecks.NotInLiquid(player) 
    				&& oldValue > 0.04 
    				&& oldValue < 0.1
    				&& e.getTo().getY() % 1 < 0.62 && e.getTo().getY() % 1 > 0.48) {
    			Values.put("BN", "<<-0.16&0.04"); 
    		}
    		
    		if(BlockChecks.inBlock(e.getTo().clone().add(0, -1, 0), Material.LADDER) || BlockChecks.inBlock(e.getTo().clone().add(0, -1, 0), Material.VINE)) {
    			Values.put("BA", "=0.1176000022888104");             			
    		}
    		
    		if(BlockChecks.isSaveOnGround(e.getTo())) {
    			Values.put("AL", "<<" + min + "&0");
    		}
    		
    		if(oldValue > -0.1 && oldValue < 0.16) {
    			Values.put("AG", "<<" + (value - 0.3) + "&" + (oldValue - 0.03));
    		}  
    		
    		if(oldValue > -0.2 && oldValue <= -0.1) {
    			Values.put("AD", "<<" + (value - 0.08) + "&" + (oldValue - 0.02));
    		}  
    		
    		if(oldValue > -0.24 && oldValue <= -0.2) {
    			Values.put("CE", "<<" + (value - 0.08) + "&" + (oldValue - 0.01));
    		}  
    		
    		if(oldValue <= 0 && BlockChecks.isSaveOnGround(e.getFrom())) {
    			Values.put("AQ", "=0.41999998688697815"); 
    			
    			Values.put("AS", "=-0.07840000152587834");
    		}
    		
    		if(oldValue == 0.20000004768371582) {
    			Values.put("AM", "=-0.07840000152587834");
    		}   
    		
    		if(BlockChecks.BlockOverPlayer(e.getTo().clone().add(0, 1, 0)) || BlockChecks.BlockOverPlayer(e.getFrom().clone().add(0, 1, 0))
    				|| BlockChecks.BlockOverPlayer(e.getTo()) || BlockChecks.BlockOverPlayer(e.getFrom())) {
    			Values.put("AN", "<<-0.24&0");
    			        			
    			if(oldValue > 0) {
    				Values.put("AO", "<<0&" + oldValue);
    			} else if(oldValue == -0.37663049823865435) {
    				Values.put("AP", "<<0&0.37663049823865435");
    			}
    			
    			if(oldValue == 0) {
    				Values.put("AR", "=0.20000004768371582");
    			}
    		}
    		
    		if((BlockChecks.inBlock(e.getTo().clone().add(0, -1, 0), Material.LADDER) || BlockChecks.inBlock(e.getTo().clone().add(0, -1, 0), Material.VINE))
    				&& oldValue > -0.156 && oldValue < 0.12) {
    			Values.put("BG", "=" + oldValue);
    			
    			Values.put("BH", "<<-0.1&"+oldValue);
    		} else {
    			Manager.getPlayerData(player).setFlyTouchedLadder(false);
    		}
    	} 
		
		return Values;
	}
	
	public void onTeleport(PlayerTeleportEvent e) {
		Manager.getPlayerData(e.getPlayer()).setFlyMovesSinceTeleport(0);
	}
}
