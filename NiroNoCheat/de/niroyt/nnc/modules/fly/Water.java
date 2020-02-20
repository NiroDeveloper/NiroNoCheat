package de.niroyt.nnc.modules.fly;

import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;

import de.niroyt.nnc.enums.Check;
import de.niroyt.nnc.manager.Manager;
import de.niroyt.nnc.utils.BlockChecks;
import de.niroyt.nnc.utils.Flag;
import de.niroyt.nnc.utils.GroundUtils;

public class Water {
	
	public static HashMap<String, String> getValues(final Player player, final PlayerMoveEvent e, final boolean onGroundFrom) {
		final HashMap<String, String> Values = new HashMap<String, String>();
				
		final double oldValue = Manager.getPlayerData(player).getFlyLastPosDiff();
		
		if(oldValue < 0.35 && oldValue > -0.16
				&& (BlockChecks.onBlock(e.getFrom()) || BlockChecks.onBlock(e.getTo()))) {
			Values.put("BP", "<<0.04&0.35");
		}
		
		Values.put("BO", "<<" + (oldValue - 0.2) + "&" 
				+ (oldValue > 0.10 ? oldValue - 0.002 : oldValue + 0.072));
		
		if(oldValue < 0) {
			Values.put("BQ", "<<" + oldValue + "&" + (oldValue + 0.2));
		}
		
		Location loc1 = e.getFrom().clone();
		loc1.setY(Math.floor(loc1.getY()));
		
		Location loc2 = e.getTo().clone();
		loc2.setY(Math.floor(loc2.getY()));
		if(GroundUtils.isPlayerPositionOnGround(loc1) || GroundUtils.isPlayerPositionOnGround(loc2)) {
			Values.put("BS", "<<0.06&0.14");
		}
		
		if(onGroundFrom) {
			Values.put("BR", "<<-0.05&0.16");
		} 
		
		if(e.getTo().getY() - e.getFrom().getY() == oldValue && e.getFrom().distance(e.getTo()) != 0
				&& !onGroundFrom && !BlockChecks.BlockOverPlayer(e.getTo()) && !BlockChecks.BlockOverPlayer(e.getFrom())) {
			if(Manager.debug(player, Check.j1, "Move: " + (e.getTo().getY() - e.getFrom().getY()) + ", LastMove: " + (oldValue))) {
				Flag.backPort(player);
			}
		}
				
		return Values;
	}
	
}
