package de.niroyt.nnc.modules;


import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;

import de.niroyt.nnc.enums.Check;
import de.niroyt.nnc.manager.Manager;
import de.niroyt.nnc.manager.Module;
import de.niroyt.nnc.utils.ConfigManager;

public class FastPlace extends Module {
		
	public void onBlockPlace(final BlockPlaceEvent e) {
		final Player player = e.getPlayer();
		
		if(Manager.isOP(player)) {
			return;
		}
		
		if(e.getBlock().getType() == Material.FIRE) {
			return;
		}
						
		if(Manager.getPlayerData(player).getFastPlaceResetTime().isDelayComplete(180L)) {
			Manager.getPlayerData(player).getFastPlaceResetTime().setLastMS();
			Manager.getPlayerData(player).setFastPlaceCounter(0);
		}
	
		Manager.getPlayerData(player).setFastPlaceCounter(Manager.getPlayerData(player).getFastPlaceCounter() + 1);
					
		if(Manager.getPlayerData(player).getFastPlaceCounter() >= ConfigManager.getCheckConfigValue(Check.fbp1, "maxValue")) {
			if(Manager.debug(player, Check.fbp1, "Value: " + Manager.getPlayerData(player).getFastPlaceCounter() + " in 180ms")) {
				e.setCancelled(true);
				Manager.getPlayerData(player).setWorldPlaceBlockLock(System.currentTimeMillis() + 3000);
			}			
		}
	}
}
