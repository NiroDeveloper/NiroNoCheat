package de.niroyt.nnc.modules;

import java.util.HashMap;
import java.util.Map.Entry;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

import de.niroyt.nnc.enums.Check;
import de.niroyt.nnc.manager.Manager;
import de.niroyt.nnc.manager.Module;
import de.niroyt.nnc.utils.GroundUtils;

public class Velocity extends Module {

	public void onMove(final PlayerMoveEvent e) {
		final Player player = e.getPlayer();
		
		if(Manager.isOP(player)) {
			return;
		}
		
		final boolean onGround = GroundUtils.isPlayerPositionOnGround(e.getTo());
		final boolean onGroundFrom = GroundUtils.isPlayerPositionOnGround(e.getFrom());
		final double motionX = e.getTo().getX() - e.getFrom().getX();
		final double motionY = e.getTo().getY() - e.getFrom().getY();
		final double motionZ = e.getTo().getZ() - e.getFrom().getZ();
		
		boolean FlagAble = true;
		HashMap<Long, Vector> sort1 = new HashMap<Long, Vector>();
		HashMap<Long, Location> sort2 = new HashMap<Long, Location>();
		for(Entry<Long, Vector> entry : Manager.getPlayerData(player).getMovementVelocityLog().entrySet()) {
			double differenceX = Math.abs(motionX - entry.getValue().getX());
			double differenceZ = Math.abs(motionZ - entry.getValue().getZ());
			boolean isVelocityMove = (differenceX < 0.06 && differenceZ < 0.06) || (onGroundFrom && differenceX < 0.15 && differenceZ < 0.15);
						
			if(isVelocityMove) {
				FlagAble = false;
			} else {
				if(entry.getKey() + 100 + Math.floor(Manager.ping(player) / 50) * 100 > System.currentTimeMillis()) {
					if(motionY != 0.41999998688697815 || !onGroundFrom) {
						sort1.put(entry.getKey(), entry.getValue());
						sort2.put(entry.getKey(), Manager.getPlayerData(player).getMovementVelocityLocation().get(entry.getKey()));
					}
				} else {
					if(Manager.debug(player, Check.v2, "Move: " + motionX + "/" + motionZ + ", Dictation: " + entry.getValue().getX() + "/" + entry.getValue().getZ())
							&& Manager.getPlayerData(player).getMovementVelocityLocation().containsKey(entry.getKey())) {
						Velocity.FlagVelocity(player, Manager.getPlayerData(player).getMovementVelocityLocation().get(entry.getKey()), entry.getValue());
		        		       		
		        		Manager.getPlayerData(player).setMovementVelocityLog(new HashMap<Long, Vector>());
		        		Manager.getPlayerData(player).setMovementVelocityLocation(new HashMap<Long, Location>());
		        		
		        		return;
					}
				}
			}			
		}
		
		Manager.getPlayerData(player).setMovementVelocityLog(sort1);
		Manager.getPlayerData(player).setMovementVelocityLocation(sort2);
	}
	
	public static void FlagVelocity(final Player player, final Location l, final Vector vec) {
		final Location loc = l.add(vec);
		
		if(!Fly.inBlock(loc)) {
			final double value = vec.getY() * 0.98 - 0.0784;
			
			player.teleport(loc);
			
			final double falldist = player.getFallDistance();
			player.setFallDistance((float) (falldist - value));
			
			player.setVelocity(new Vector(vec.getX() * 0.8, value * 0.98 - 0.0784, vec.getZ() * 0.8));
			Manager.getPlayerData(player).setFlyLastPosDiff(value);  	
		}
	}
	
}
