package de.niroyt.nnc.modules.fly;

import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;

import de.niroyt.nnc.manager.Manager;
import de.niroyt.nnc.utils.BlockChecks;

public class Web {

	public static boolean inWeb(final Location loc1, final Location loc2) {
    	if(BlockChecks.inBlock(loc1, Material.WEB) && BlockChecks.inBlock(loc2, Material.WEB)) {
			return true;
		}
				
		if(BlockChecks.inBlock(new Location(loc1.getWorld(), loc1.getX(), loc1.getY() + 1, loc1.getZ()), Material.WEB) 
				&& BlockChecks.inBlock(new Location(loc2.getWorld(), loc2.getX(), loc2.getY() + 1, loc2.getZ()), Material.WEB)) {
			return true;
		}
		
		return false;
    }
	
	public static HashMap<String, String> getValues(final Player player, final PlayerMoveEvent e, final boolean onGroundFrom) {
		final HashMap<String, String> Values = new HashMap<String, String>();
		
		final double oldValue = Manager.getPlayerData(player).getFlyLastPosDiff();
		
		if(onGroundFrom) {
			Values.put("CA", "<=0&0.020999999657277613");
		} else if(oldValue > 0 && oldValue <= 0.020999999657277613) {
			Values.put("CB", "<=-0.16&0.0");
		} else {
			Values.put("CC", "<=-0.1&0.0");
		}
		
		return Values;
	}
	
}
