package de.niroyt.nnc.modules;


import java.util.HashMap;
import java.util.Map.Entry;


import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import de.niroyt.nnc.Main;
import de.niroyt.nnc.enums.Check;
import de.niroyt.nnc.manager.Manager;
import de.niroyt.nnc.manager.Module;
import de.niroyt.nnc.modules.fly.Web;
import de.niroyt.nnc.utils.BlockChecks;
import de.niroyt.nnc.utils.ConfigManager;
import de.niroyt.nnc.utils.Flag;
import de.niroyt.nnc.utils.GroundUtils;

public class Strafe extends Module {
	    	    	
	public void onMove(final PlayerMoveEvent e) {		
		final Player player = e.getPlayer();
		
		if(!BlockChecks.inAir(player)
				|| player.isOnGround() 
				|| Manager.AllowedSpeedFly(player) 
				|| !BlockChecks.canCheckFly(player)
				|| BlockChecks.BlockOverPlayer(e.getTo())
				|| NoSlowdown.isUsingItem(player)
				|| BlockChecks.onSteps(player)
				|| GroundUtils.isPlayerPositionOnGround(e.getTo())
				|| GroundUtils.isPlayerPositionOnGround(e.getFrom())
				|| BlockChecks.onBlock(e.getTo())
				|| Manager.getPlayerData(player).getMovementVelocityLog().size() != 0) {
			Manager.getPlayerData(player).setStrafeValueX(0);
			Manager.getPlayerData(player).setStrafeValueZ(0);
			return;
		}
		
		final double diffX = e.getTo().getX() - e.getFrom().getX();
		final double diffZ = e.getTo().getZ() - e.getFrom().getZ();
		
		if(!Main.MinecraftVersion.equalsIgnoreCase("v1_8_R3") && Fly.getFlyMoves(player) <= 3) {
			Manager.getPlayerData(player).setStrafeValueX(0);
			Manager.getPlayerData(player).setStrafeValueZ(0);			
			return;
		}
		
		if(Manager.getPlayerData(player).getStrafeValueX() == 0 || Manager.getPlayerData(player).getStrafeValueZ() == 0) {
			Manager.getPlayerData(player).setStrafeValueX(diffX);
			Manager.getPlayerData(player).setStrafeValueZ(diffZ);
			return;
		}
				
		final double speed = Math.sqrt(diffX * diffX + diffZ * diffZ);
		final double oldSpeed = Math.sqrt(Manager.getPlayerData(player).getStrafeValueX() * Manager.getPlayerData(player).getStrafeValueX() + Manager.getPlayerData(player).getStrafeValueZ() * Manager.getPlayerData(player).getStrafeValueZ());
		
		double SpeedMultiply = speed / oldSpeed;
				
		boolean OnBoat = false;
		for(Entity entity : player.getWorld().getEntities()) {
        	if(entity.getLocation().distance(e.getTo()) < 4 && entity.getType() == EntityType.BOAT) {
        		OnBoat = true;
        	}
        }
		
		if(SpeedMultiply > ConfigManager.getCheckConfigValue(Check.st3, "maxValue") 
				&& speed > ConfigManager.getCheckConfigValue(Check.st3, "requiredSpeed")
				&& (e.getTo().getY() - e.getFrom().getY() > -0.07 || e.getTo().getY() - e.getFrom().getY() < -0.08)
				&& (System.nanoTime() - Manager.getPlayerData(player).getSprintLastToggle()) / 1000000 - Manager.ping(player) - Main.getServerLag() > 500
				&& !OnBoat
				&& !GroundUtils.wasOnSlime(player)
				&& !Web.inWeb(e.getFrom(), e.getFrom())) {
			if(Manager.debug(player, Check.st3, "Value: " + round(SpeedMultiply) + ", Speed: " + round(speed))) {
				Flag.backPort(player);
			}
		}		
		
		if(SpeedMultiply > 1) {
			SpeedMultiply = 1 - SpeedMultiply;
		}
		
		if(SpeedMultiply < 0) {
			SpeedMultiply = 0;
		}
		
		double DirectionDiffX = (diffX - Manager.getPlayerData(player).getStrafeValueX()) * SpeedMultiply;
		double DirectionDiffZ = (diffZ - Manager.getPlayerData(player).getStrafeValueZ()) * SpeedMultiply;
				
		if(DirectionDiffX < 0) {
			DirectionDiffX = 0;
		}
		if(DirectionDiffZ < 0) {
			DirectionDiffZ = 0;
		}
		
		if(DirectionDiffX == Double.POSITIVE_INFINITY) {
			DirectionDiffX = 0;
		}
		if(DirectionDiffZ == Double.POSITIVE_INFINITY) {
			DirectionDiffZ = 0;
		}
	
		final double directionChanging = Math.sqrt(DirectionDiffZ * DirectionDiffZ + DirectionDiffX * DirectionDiffX);
				
		double value = ConfigManager.getCheckConfigValue(Check.st1, "maxValue");
						
		if(Main.MinecraftVersion.equalsIgnoreCase("v1_8_R3")) {
			value *= 0.95;
		} else {
			double nearestEntity = Double.MAX_VALUE;
			
			for(LivingEntity entity : player.getWorld().getLivingEntities()) {
				if(entity.getLocation().distance(e.getTo()) < nearestEntity) {
					nearestEntity = entity.getLocation().distance(e.getTo());
				}
			}
			
			if(nearestEntity < 1.8) {
				value *= 1.5;
			}
		}
		
		if((System.nanoTime() - Manager.getPlayerData(player).getSprintLastToggle()) / 1000000 - Manager.ping(player) - Main.getServerLag() < 500) {
			value *= 3.1;
		}
		
		if(player.isSneaking()) {
			value *= 2;
		}
				
		if(directionChanging > value) {			
			Manager.getPlayerData(player).getStrafeFlags().put(System.currentTimeMillis(), new Double[] {directionChanging, value});
		} 
		
		Manager.getPlayerData(player).setStrafeValueX(diffX);
		Manager.getPlayerData(player).setStrafeValueZ(diffZ);
	}
	
	public void onTeleport(final PlayerTeleportEvent e) {
		final Player player = e.getPlayer();
		
		Manager.getPlayerData(player).setStrafeValueX(0);
		Manager.getPlayerData(player).setStrafeValueZ(0);
	}
	
	public void onTick() {
		for(final Player player : Bukkit.getOnlinePlayers()) {
			if(Manager.AllowedSpeedFly(player)) {
				continue;
			}

			final HashMap<Long, Double[]> newList = new HashMap<Long, Double[]>();
			for(Entry<Long, Double[]> entry : Manager.getPlayerData(player).getStrafeFlags().entrySet()) {
				final Long time = entry.getKey();
				
				if(System.currentTimeMillis() - time > Manager.ping(player) + Main.getServerLag() + 50) {					
					final Double[] value = entry.getValue();
										
					if((System.nanoTime() - Manager.getPlayerData(player).getKillauraLastHit()) / 1000000 < 500 + Manager.ping(player) + Main.getServerLag()) {
						value[1] *= 2.25;
					}
					
					if(value[0] > value[1] * 1.16 || (value[0] > value[1] && System.currentTimeMillis() - Manager.getPlayerData(player).getStrafeLastFlag() < 1500)) {			
						if(Manager.debug(player, Check.st1, "Value: " + round(value[0]) + ", Trigger value: " + round(value[1]))) {
							Flag.backPort(player);
						}
					}
					
					if(value[0] > value[1]) {
						Manager.getPlayerData(player).setStrafeLastFlag(System.currentTimeMillis());
					}
				} else {
					newList.put(time, entry.getValue());
				}
			}
			
			Manager.getPlayerData(player).getStrafeFlags().clear();
			Manager.getPlayerData(player).setStrafeFlags(newList);
		}
	}
	
	public double round(final double i) {
    	return Double.valueOf(Math.round(i * 10000)) / 10000;
    }
}
