package de.niroyt.nnc.modules;

import org.bukkit.event.*;
import org.bukkit.event.player.PlayerMoveEvent;

import de.niroyt.nnc.enums.Check;
import de.niroyt.nnc.manager.*;
import de.niroyt.nnc.utils.BlockChecks;
import de.niroyt.nnc.utils.Flag;
import de.niroyt.nnc.utils.GroundUtils;

import org.bukkit.*;
import org.bukkit.entity.Player;

public class Phase extends Module {
	        
    @EventHandler
	public void onMove(final PlayerMoveEvent e) {
		final Player player = e.getPlayer();
		
		if(Manager.AllowedSpeedFly(player)) {
			return;
		}	
			
		if(e.getTo().distance(e.getFrom()) > 2) {
			return;
		}
		
		if(GroundUtils.LastLocation(player) == e.getFrom())	{
			return;
		}
						
		if(inBlock(e.getTo()) && !inBlock(e.getFrom())) {
			if(Manager.debug(player, Check.ph2, "")) {
				Flag.backPort(player);
			}
		}
	}
    
    public static boolean inBlock(final Location loc) {
    	int onOtherBlockX = 0;
    	int onOtherBlockZ = 0;    	
    	
    	if(loc.getX() % 1.0 > 0.7 || (loc.getX() % 1.0 > -0.3 && loc.getX() % 1.0 < 0)) {
    		onOtherBlockX = 1;
    	} else if((loc.getX() % 1.0 < 0.3 && loc.getX() % 1.0 > 0) || loc.getX() % 1.0 < -0.7) {
    		onOtherBlockX = -1;
    	}
    	
    	if(loc.getZ() % 1.0 > 0.7 || (loc.getZ() % 1.0 > -0.3 && loc.getZ() % 1.0 < 0)) {
    		onOtherBlockZ = 1;
    	} else if((loc.getZ() % 1.0 < 0.3 && loc.getZ() % 1.0 > 0) || loc.getZ() % 1.0 < -0.7) {
    		onOtherBlockZ = -1;
    	}
    	
    	Location toCheckLoc1 = new Location(loc.getWorld(), loc.getX(), loc.getY(), loc.getZ() + onOtherBlockZ);
    	Location toCheckLoc2 = new Location(loc.getWorld(), loc.getX() + onOtherBlockX, loc.getY(), loc.getZ() + onOtherBlockZ);
    	Location toCheckLoc3 = new Location(loc.getWorld(), loc.getX() + onOtherBlockX, loc.getY(), loc.getZ());
		
		boolean Cheating = false;
		Material block = null;
		
		if(BlockChecks.isFullBlock(loc.getBlock().getType()) 
				&& BlockChecks.isMassiv(loc)
				&& !BlockChecks.isStep(loc)) {	
			Cheating = true;	
			block = loc.getBlock().getType();
		}
		
		if(BlockChecks.isFullBlock(toCheckLoc1.getBlock().getType()) 
				&& BlockChecks.isMassiv(toCheckLoc1)
				&& !BlockChecks.isStep(toCheckLoc1)) {	
			Cheating = true;	
			block = toCheckLoc1.getBlock().getType();
		}
		
		if(BlockChecks.isFullBlock(toCheckLoc2.getBlock().getType()) 
				&& BlockChecks.isMassiv(toCheckLoc2)
				&& !BlockChecks.isStep(toCheckLoc2)) {	
			Cheating = true;	
			block = toCheckLoc2.getBlock().getType();
		}
		
		if(BlockChecks.isFullBlock(toCheckLoc3.getBlock().getType()) 
				&& BlockChecks.isMassiv(toCheckLoc3)
				&& !BlockChecks.isStep(toCheckLoc3)) {	
			Cheating = true;	
			block = toCheckLoc3.getBlock().getType();
		}
		
		if(BlockChecks.isFullBlock(loc.getBlock().getRelative(0, 1, 0).getType()) 
				&& BlockChecks.isMassiv(loc.getBlock().getRelative(0, 1, 0).getLocation())
				&& !BlockChecks.isStep(loc.getBlock().getRelative(0, 1, 0).getLocation())) {	
			Cheating = true;	
			block = loc.getBlock().getRelative(0, 1, 0).getType();
		}
		
		if(BlockChecks.isFullBlock(toCheckLoc1.getBlock().getRelative(0, 1, 0).getType()) 
				&& BlockChecks.isMassiv(toCheckLoc1.getBlock().getRelative(0, 1, 0).getLocation())
				&& !BlockChecks.isStep(toCheckLoc1.getBlock().getRelative(0, 1, 0).getLocation())) {	
			Cheating = true;	
			block = toCheckLoc1.getBlock().getRelative(0, 1, 0).getType();
		}
		
		if(BlockChecks.isFullBlock(toCheckLoc2.getBlock().getRelative(0, 1, 0).getType()) 
				&& BlockChecks.isMassiv(toCheckLoc2.getBlock().getRelative(0, 1, 0).getLocation())
				&& !BlockChecks.isStep(toCheckLoc2.getBlock().getRelative(0, 1, 0).getLocation())) {	
			Cheating = true;	
			block = toCheckLoc2.getBlock().getRelative(0, 1, 0).getType();
		}
		
		if(BlockChecks.isFullBlock(toCheckLoc3.getBlock().getRelative(0, 1, 0).getType()) 
				&& BlockChecks.isMassiv(toCheckLoc3.getBlock().getRelative(0, 1, 0).getLocation())
				&& !BlockChecks.isStep(toCheckLoc3.getBlock().getRelative(0, 1, 0).getLocation())) {	
			Cheating = true;	
			block = toCheckLoc3.getBlock().getRelative(0, 1, 0).getType();
		}
		
		return Cheating;
    }
}
