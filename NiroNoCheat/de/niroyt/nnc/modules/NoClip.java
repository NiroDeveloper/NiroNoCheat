package de.niroyt.nnc.modules;

import org.bukkit.event.player.PlayerMoveEvent;

import de.niroyt.nnc.enums.Check;
import de.niroyt.nnc.manager.*;
import de.niroyt.nnc.utils.BlockChecks;
import de.niroyt.nnc.utils.GroundUtils;

import org.bukkit.*;
import org.bukkit.entity.Player;

public class NoClip extends Module {
	        
	public void onMove(final PlayerMoveEvent e) {
		final Player player = e.getPlayer();
		
		if(Manager.AllowedSpeedFly(player)) {
			return;
		}				
		if(e.getFrom().getBlock().getLocation() == e.getTo().getBlock().getLocation()) {
			return;
		}		
		if(BlockChecks.onSteps(player)) {
			return;
		}
		if(GroundUtils.LastLocation(player) == e.getFrom())	{
			return;
		}
		
		final Location loc = e.getTo().getBlock().getLocation();
		final Location loc2 = e.getFrom().getBlock().getLocation();
		
		Material b = null;
		
		if(BlockChecks.isFullBlock(loc.getBlock().getType()) && BlockChecks.isMassiv(loc)
				&& !(BlockChecks.isFullBlock(loc2.getBlock().getType()) && BlockChecks.isMassiv(loc2))) {
			b = loc.getBlock().getType();	 
		}
		if(BlockChecks.isFullBlock(loc.getBlock().getRelative(0, 1, 0).getType()) && BlockChecks.isMassiv(loc.add(0, 1, 0))
				&& !(BlockChecks.isFullBlock(loc2.getBlock().getRelative(0, 1, 0).getType()) && BlockChecks.isMassiv(loc2.add(0, 1, 0)))) {	
			b = loc.add(0, 1, 0).getBlock().getType();
		}
		
		if(b != null) {
			if(Manager.debug(player, Check.ph1, "Material: " + b)) {
				e.setCancelled(true);
			}
    	}
    }
}
