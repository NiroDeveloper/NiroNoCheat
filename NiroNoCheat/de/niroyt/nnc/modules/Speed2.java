package de.niroyt.nnc.modules;

import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffectType;

import de.niroyt.nnc.Main;
import de.niroyt.nnc.enums.Check;
import de.niroyt.nnc.manager.*;
import de.niroyt.nnc.utils.BlockChecks;
import de.niroyt.nnc.utils.ConfigManager;
import de.niroyt.nnc.utils.Flag;
import de.niroyt.nnc.utils.GroundUtils;

import org.bukkit.entity.*;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

public class Speed2 extends Module {
	               
    public void onMove(final PlayerMoveEvent e) {    	
	    final Player player = e.getPlayer();
	    
	    if(Manager.AllowedSpeedFly(player)
	    		|| ((BlockChecks.BlockOverPlayer(e.getTo()) && !BlockChecks.onBlock(e.getTo())) || e.getFrom().getBlock().getRelative(0, 2, 0).getType().isSolid())) {
	    	Manager.getPlayerData(player).setSpeed2OldSpeed(0);
	    	return;
	    }
	    	    				
		if(player.hasPotionEffect(PotionEffectType.SPEED)) {
			return;
		}
		
		if(GroundUtils.wasOnIce(player) && !player.isOnGround()) {
			return;
		}
					    
		final double dX = e.getFrom().getX() - e.getTo().getX();
		final double dZ = e.getFrom().getZ() - e.getTo().getZ();

		final double speed = Math.sqrt(dZ * dZ + dX * dX);				
		double maxSpeed = ConfigManager.getCheckConfigValue(Check.sg1, "maxSpeed");
		String info = "";
		float yaw = player.getLocation().getYaw();		
		if(yaw < 0) {
			yaw += 360;
		}		
		double mx = dX;
		double mz = dZ;    			
		if(mz < 0) {
			mz *= -1;
		}
		if(mx < 0) {
			mx *= -1;
		}			
		if(yaw > 180) {
			yaw -= 180;
		}		
		double xValue = yaw - (yaw > 90 ? (yaw - 90) * 2 : 0);
		double x = mx * xValue;
		yaw += 90;
		double zYaw = yaw - (yaw > 90 ? (yaw - 90) * 2 : 0);
		double zValue = mz * zYaw;
		double z = zValue < 0 ? -zValue : zValue;		
		double forward = x + z;
						
		if(BlockChecks.onSteps(player)) { 
			info = " (on Steps)";
			
			maxSpeed *= ConfigManager.getCheckConfigValue(Check.sg1, "stepMultiply");
		}if(GroundUtils.wasOnIce(player)) { 
			info = " (on ice)";
		} else if(player.isSneaking()) {
			info = " (sneaking)";
			
			maxSpeed *= ConfigManager.getCheckConfigValue(Check.sg1, "sneakMultiply");
		} else if(Sprint.DoNotSprint(player) && !player.isSprinting()) {
			info = " (not sprinting)";
			
			maxSpeed *= ConfigManager.getCheckConfigValue(Check.sg1, "notSprintMultiply");
		} else if(forward < ConfigManager.getCheckConfigValue(Check.sg1, "minSidewayValue")) {
			info = " (run sideway, value: " + round(forward) + ")";

			maxSpeed *= ConfigManager.getCheckConfigValue(Check.sg1, "runSidewayMultiply");
		} else {
			Location l = behindPlayer(e.getFrom());
			if(l.getBlockX() == e.getTo().getBlock().getLocation().getBlockX() 
					&& l.getBlockZ() == e.getTo().getBlock().getLocation().getBlockZ()) {	
				info = " (run backward)";
				
				maxSpeed *= ConfigManager.getCheckConfigValue(Check.sg1, "runBackwardMultiply");
			}
		}
		
		if(!Main.MinecraftVersion.equalsIgnoreCase("v1_8_R3")) {
			double nearestEntity = Double.MAX_VALUE;
			
			for(final LivingEntity entity : player.getWorld().getLivingEntities()) {
				if(entity.getLocation().distance(e.getTo()) < nearestEntity) {
					nearestEntity = entity.getLocation().distance(e.getTo());
				}
			}
			
			if(nearestEntity < 1.8) {
				maxSpeed = ConfigManager.getCheckConfigValue(Check.sg1, "maxSpeed") * 1.12;
			}
		}
				
		if(player.isOnGround()
				&& (e.getFrom().getY() - e.getTo().getY()) % 0.5 == 0) {		
			if(Manager.getPlayerData(player).getSpeed2LastDetected() != -1
					&& (!BlockChecks.onSteps(player) || (speed > maxSpeed && Manager.getPlayerData(player).getSpeed2LastDetected() > maxSpeed))
					&& Manager.getPlayerData(player).getSpeed2LastDetected() <= speed + (speed - maxSpeed) * 0.5
					&& !BlockChecks.inBlock(e.getTo().clone().add(0, 1, 0), Material.TRAP_DOOR) && !BlockChecks.inBlock(e.getTo().clone().add(0, 1, 0), Material.IRON_TRAPDOOR)
					&& ((speed > maxSpeed && !GroundUtils.wasOnIce(player)) 
							|| (speed > maxSpeed && Manager.getPlayerData(player).getSpeed2LastDetected() > maxSpeed))
					&& (!info.contains("sneak") || Manager.getPlayerData(player).getSpeed2LastDetected() > maxSpeed)) {				
				if(Manager.debug(player, Check.sg1, round(speed) + " > " + round(maxSpeed) + ", LastSpeed: " + round(Manager.getPlayerData(player).getSpeed2LastDetected()) + info)) {
					Flag.backPort(player);
				}
			}
			
			Manager.getPlayerData(player).setSpeed2LastDetected(speed);
		} else {
			Manager.getPlayerData(player).setSpeed2LastDetected(-1);
		}
		
		if(Fly.getFlyMoves(player) != 0 && !GroundUtils.wasOnSlime(player) && BlockChecks.NotInLiquid(player) && BlockChecks.canCheckFly(player)
				&& (Main.MinecraftVersion.equalsIgnoreCase("v1_8_R3") || Fly.getFlyMoves(player) != 1)
				&& !GroundUtils.wasFlying(player)
				&& Manager.getPlayerData(player).getSpeed2OldSpeed() > 0) {		
			final double OldSpeedValue = Manager.getPlayerData(player).getSpeed2OldSpeed();
				
			if((OldSpeedValue - speed) < ConfigManager.getCheckConfigValue(Check.st2, "minValue") && !GroundUtils.wasFlying(player)) {
				if(Manager.debug(player, Check.st2, "")) {
					Flag.backPort(player);
				}
			}
						
			final double NormalOldSpeedValue = -0.062 * Math.pow(speed, 3) + 0.062 * Math.pow(speed, 2) + 1.079 * speed - 0.027;
					
			if(OldSpeedValue - NormalOldSpeedValue < 0 && !player.isOnGround()) {
				final double FlyValue = -0.005 * Math.pow(Fly.getFlyMoves(player), 3) + 0.283 * Math.pow(Fly.getFlyMoves(player), 2) - 7.2 * Fly.getFlyMoves(player); 
				double maxValue = ConfigManager.getCheckConfigValue(Check.sj4, "maxSpeed") * 1000;
					
				if(Main.MinecraftVersion.equalsIgnoreCase("v1_8_R3")) {
					maxValue *= 0.982;
				}
				
				maxValue += FlyValue;
									
				maxValue = maxValue / 1000;
				
				if(maxValue < 0.1) {
					maxValue = 0.1;
				}
				
				if(maxValue - speed < 0) {
					if(Manager.debug(player, Check.sj4, round(speed) + " > " + round(maxValue))) {
						Flag.backPort(player);
						
						if(speed - maxValue > 0.05) {
							Flag.kick(player, Check.sj4.showCheckCategory());
						}
					}
				} 
				
				if(Manager.getPlayerData(player).getTestingCheck() == Check.sj4) {
					Commands.sendMessage(player, "§7Speed-Debug: [" + (speed - maxValue > 0 ? "§c" : "§a") + "Overlimit: " + (speed - maxValue) + "§7]");
				}
			} 
		} 
		Manager.getPlayerData(player).setSpeed2OldSpeed(speed);
    } 
    
    public double round(final double value) {
    	return Double.valueOf(Math.round(value * 10000)) / 10000;
    }
        
    public Location behindPlayer(final Location loc) {       
    	double rotation = loc.getYaw() % 360;       
    	final Block b = loc.getBlock();       
    	if (rotation < 0) {
    		rotation += 360.0;       
    	}  
    	if (0 <= rotation && rotation < 45) {
    		return b.getRelative(BlockFace.NORTH).getLocation();       
    	} else if (45 <= rotation && rotation < 135) {
    		return b.getRelative(BlockFace.EAST).getLocation();       
    	} else if (135 <= rotation && rotation < 225) {
    		return b.getRelative(BlockFace.SOUTH).getLocation();       
    	} else if (225 <= rotation && rotation < 315) {
    		return b.getRelative(BlockFace.WEST).getLocation();       
    	} else if (315 <= rotation && rotation < 360.0) {
    		return b.getRelative(BlockFace.NORTH).getLocation();       
    	} else {
    		return null;       
    	}   
	}
}