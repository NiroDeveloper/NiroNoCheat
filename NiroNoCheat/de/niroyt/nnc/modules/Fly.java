package de.niroyt.nnc.modules;

import java.util.HashMap;
import java.util.Map.Entry;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.player.*;
import org.bukkit.util.Vector;

import de.niroyt.nnc.enums.Check;
import de.niroyt.nnc.manager.*;
import de.niroyt.nnc.modules.fly.Ladder;
import de.niroyt.nnc.modules.fly.Normal;
import de.niroyt.nnc.modules.fly.OnGround;
import de.niroyt.nnc.modules.fly.Water;
import de.niroyt.nnc.modules.fly.Web;
import de.niroyt.nnc.utils.BlockChecks;
import de.niroyt.nnc.utils.Flag;
import de.niroyt.nnc.utils.GroundUtils;

public class Fly extends Module {
        	      
    public static void setFlyMoves(final Player player, final int i) {
    	Manager.getPlayerData(player).setFlyMoves(i);
    }
    
    public static int getFlyMoves(final Player player) {
    	return Manager.getPlayerData(player).getFlyMoves();
    }
            	  
    public void onMove(final PlayerMoveEvent e) {
        final Player player = e.getPlayer();
              
        if(player.isOnGround()) {
        	setFlyMoves(player, 0);
        } else {
        	setFlyMoves(player, getFlyMoves(player) + 1);
        }
              
        Manager.getPlayerData(player).setFlyMovesSinceTeleport(Manager.getPlayerData(player).getFlyMovesSinceTeleport() + 1);
         
		if(Manager.isOP(player)) {
			return;
		}
		        
		final float pitch = e.getTo().getPitch();		
		if(pitch > 90) {
			if(Manager.debug(player, Check.nmr1, "Pitch: " + Double.valueOf(Math.round(pitch * 100)) / 100)) {
				e.setCancelled(true);
			}
		}
	        	
		if(pitch < -90) {
			if(Manager.debug(player, Check.nmr2, "Pitch: " + Double.valueOf(Math.round(pitch * 100)) / 100)) {
				e.setCancelled(true);
			}
		}  	
		
        final boolean onGround = GroundUtils.isPlayerPositionOnGround(e.getTo());
        final boolean onGroundFrom = GroundUtils.isPlayerPositionOnGround(e.getFrom());
        
        final HashMap<String, String> Values = new HashMap<String, String>(); 
        
        if(onGround) {
        	Values.putAll(OnGround.getValues(player, e, onGroundFrom));
        } else {    
        	if(BlockChecks.inLadder(e.getTo())) {
        		Values.putAll(Ladder.getValues(player, e, onGroundFrom));
        	} else {
        		if(BlockChecks.InLiquid(player)) {
        			Values.putAll(Water.getValues(player, e, onGroundFrom));
        		} else {
        			if(Web.inWeb(e.getFrom(), e.getTo())) {
        				Values.putAll(Web.getValues(player, e, onGroundFrom));
        			} else {
        				Values.putAll(Normal.getValues(player, e, onGroundFrom));
        			}        			
        		}
        	}
        }
        
        final double motionY = e.getTo().getY() - e.getFrom().getY();
		String ValidMoveID = "";		
		String MoveCode = "";
		String debugCode = "";
		for(final Entry<String, String> entry : Values.entrySet()) {
			if(MoveCode.length() != 0) {
				MoveCode += ":";
			}			
			MoveCode += entry.getKey();
						
			if(debugCode.length() != 0) {
				debugCode += "§7:";
			}
			
			boolean IsValidMove = false;
			
			if(entry.getValue().startsWith("=") && motionY == Double.valueOf(entry.getValue().replace("=", ""))) {				
				ValidMoveID += entry.getKey();
				IsValidMove = true;
			}
			
			if(entry.getValue().contains("<<") 
					&& motionY > Double.valueOf(entry.getValue().replace("<<", "").split("&")[0])
					&& motionY < Double.valueOf(entry.getValue().replace("<<", "").split("&")[1])) {
				ValidMoveID += entry.getKey();
				IsValidMove = true;
			}
			
			if(entry.getValue().contains("<=") 
					&& motionY >= Double.valueOf(entry.getValue().replace("<=", "").split("&")[0])
					&& motionY <= Double.valueOf(entry.getValue().replace("<=", "").split("&")[1])) {
				ValidMoveID += entry.getKey();
				IsValidMove = true;
			}
			
			if(IsValidMove) {
				debugCode += "§a";
			} else {
				debugCode += "§c";
			}
			
			debugCode += entry.getKey();
		}
		
		if(Manager.getPlayerData(player).getTestingCheck() == Check.f) {
			Commands.sendMessage(player, "§7Fly-Debug: [" + debugCode + "§7]");
		}
		
		boolean FlagAble = true;
		final HashMap<Long, Vector> sort1 = new HashMap<Long, Vector>();
		final HashMap<Long, Location> sort2 = new HashMap<Long, Location>();
		for(Entry<Long, Vector> entry : Manager.getPlayerData(player).getFlyVelocityLog().entrySet()) {
			final double difference = Math.abs(motionY - entry.getValue().getY());
			final boolean isVelocityMove = difference < 0.01;
			
			if(isVelocityMove) {
				FlagAble = false;
			} else {
				if(entry.getKey() + 100 + Math.floor(Manager.ping(player) / 50) * 100 > System.currentTimeMillis()) {
					if(motionY != 0.41999998688697815 || !onGroundFrom) {
						sort1.put(entry.getKey(), entry.getValue());
						sort2.put(entry.getKey(), Manager.getPlayerData(player).getFlyVelocityLocation().get(entry.getKey()));
					}
				} else {
					if(Manager.debug(player, Check.v1, "Move: " + motionY + ", Dictation: " + entry.getValue().getY())
							&& Manager.getPlayerData(player).getFlyVelocityLocation().containsKey(entry.getKey())) {
						Velocity.FlagVelocity(player, Manager.getPlayerData(player).getFlyVelocityLocation().get(entry.getKey()), entry.getValue());
		        		       		
		        		Manager.getPlayerData(player).setFlyVelocityLog(new HashMap<Long, Vector>());
		        		Manager.getPlayerData(player).setFlyVelocityLocation(new HashMap<Long, Location>());
		        		
		        		return;
					}
				}
			}			
		}
		Manager.getPlayerData(player).setFlyVelocityLog(sort1);
		Manager.getPlayerData(player).setFlyVelocityLocation(sort2);
				
		if(Manager.AllowedSpeedFly(player)) {
        	Manager.getPlayerData(player).setFlyLastPosDiff(-1337);
        	return;
        }
		
		if(ValidMoveID.length() == 0 && Manager.getPlayerData(player).getFlyLastPosDiff() != -1337 && FlagAble) {			
			if(Manager.debug(player, Check.f, "Type: [" + MoveCode + "], Move: " + (motionY) + ", LastMove: " + (Manager.getPlayerData(player).getFlyLastPosDiff()))) {
				if(!onGround && !BlockChecks.inLadder(e.getTo()) && !BlockChecks.InLiquid(player) && !Web.inWeb(e.getFrom(), e.getTo()) && !GroundUtils.onSlime(e.getFrom()) && !GroundUtils.onSlime(e.getTo())) {
					final double value = Manager.getPlayerData(player).getFlyLastPosDiff() - Manager.getPlayerData(player).getFlyLastPosDiff() * 0.02;
		        	final double min = value - 0.0784;
		        	
		        	final Location newLoc = new Location(e.getFrom().getWorld(), e.getFrom().getX(), e.getFrom().getY() + min, e.getFrom().getZ(), e.getTo().getYaw(), e.getTo().getPitch());
		        	
		        	if(Manager.getPlayerData(player).getFlyVelocityLog().size() != 0) {
		        		return;
		        	}
		        	
		        	if(!blockOnWay(e.getFrom(), newLoc)) {
			        	double falldist = player.getFallDistance();
			        	player.teleport(newLoc);
			        	player.setFallDistance((float) (falldist - min));
			        		
			        	player.setVelocity(new Vector(0, min * 0.98 - 0.0784, 0));
			        	Manager.getPlayerData(player).setFlyLastPosDiff(min);  	
			        } else {
			        	Location correctedLoc = new Location(e.getFrom().getWorld(), e.getFrom().getX(), Math.floor(e.getFrom().getY()), e.getFrom().getZ(), e.getTo().getYaw(), e.getTo().getPitch());
			        	if(inBlock(correctedLoc)) {
			        		player.teleport(new Location(e.getFrom().getWorld(), e.getFrom().getX(), e.getFrom().getY(), e.getFrom().getZ(), e.getTo().getYaw(), e.getTo().getPitch()));
			        	} else {
			        		player.teleport(correctedLoc);
			        	}		        		
			        	player.setVelocity(new Vector(0, 0, 0));
			        	Manager.getPlayerData(player).setFlyLastPosDiff(0);  
			        }		        	 
		        	
		        	return;
				} else {
					Flag.backPort(player);
				}
			}
		}
        
        Manager.getPlayerData(player).setFlyLastPosDiff(motionY);     
        
        if(!GroundUtils.onSlime(e.getTo())) {
        	Manager.getPlayerData(player).setFlySlimeJumpMotion(motionY);
        }        
    }
    
    public double round(final double i) {
    	return Double.valueOf(Math.round(i * 1000)) / 1000;
    }
    
    private boolean blockOnWay(final Location loc1, final Location loc2) {
    	if(loc1.getWorld() != loc2.getWorld()) {
    		return true;
    	}
    	
    	double x = Math.abs(loc1.getX() - loc2.getX()) + 1;
    	double y = Math.abs(loc1.getY() - loc2.getY()) + 1;
    	double z = Math.abs(loc1.getZ() - loc2.getZ()) + 1;
    	
    	while(x >= -Math.abs(loc1.getX() - loc2.getX()) - 1) {
    		while(y >= -Math.abs(loc1.getY() - loc2.getY()) - 1) {
    			while(z >= -Math.abs(loc1.getZ() - loc2.getZ()) - 1) {
    				Location loc = new Location(loc1.getWorld(), loc1.getX() + (x / Math.abs(loc1.getX() - loc2.getX())) * (loc1.getX() - loc2.getX()), 
    						loc1.getY() + (y / Math.abs(loc1.getY() - loc2.getY())) * (loc1.getY() - loc2.getY()),
    						loc1.getZ() + (z / Math.abs(loc1.getZ() - loc2.getZ())) * (loc1.getZ() - loc2.getZ()));
    				    				
    				if(Double.isNaN(loc.getX())) {
    					loc.setX(loc1.getX());
    				}
    				
    				if(Double.isNaN(loc.getY())) {    	    					
    					loc.setY(loc1.getY());
    				}
    				
    				if(Double.isNaN(loc.getZ())) {
    					loc.setZ(loc1.getZ());
    				}
    				
    				if(inBlock(loc)) {
    					return true;
    				}
    				
    				z--;
    			}
    			z = Math.abs(loc1.getZ() - loc2.getZ()) + 1;
    			y--;
    		}
    		y = Math.abs(loc1.getY() - loc2.getY()) + 1;
    		x--;
    	}

    	return false;
    }
        
    public void onTeleport(final PlayerTeleportEvent e) {
    	final Player player = e.getPlayer();
    	
    	Manager.getPlayerData(player).setFlyMovesSinceTeleport(0);
    	Manager.getPlayerData(player).setFlyLastPosDiff(-1337);
    	
    	Manager.getPlayerData(player).setFlyVelocityLog(new HashMap<Long, Vector>());
		Manager.getPlayerData(player).setFlyVelocityLocation(new HashMap<Long, Location>());
		
		Manager.getPlayerData(player).setMovementVelocityLog(new HashMap<Long, Vector>());
		Manager.getPlayerData(player).setMovementVelocityLocation(new HashMap<Long, Location>());
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
    	
    	final Location toCheckLoc1 = new Location(loc.getWorld(), loc.getX(), loc.getY(), loc.getZ() + onOtherBlockZ);
    	final Location toCheckLoc2 = new Location(loc.getWorld(), loc.getX() + onOtherBlockX, loc.getY(), loc.getZ() + onOtherBlockZ);
    	final Location toCheckLoc3 = new Location(loc.getWorld(), loc.getX() + onOtherBlockX, loc.getY(), loc.getZ());
		
		boolean Cheating = false;
		Material block = null;
		
		if(BlockChecks.isMassiv(loc.getBlock().getLocation())) {	
			Cheating = true;	
			block = loc.getBlock().getType();
		}
		
		if(BlockChecks.isMassiv(toCheckLoc1.getBlock().getLocation())) {	
			Cheating = true;	
			block = toCheckLoc1.getBlock().getType();
		}
		
		if(BlockChecks.isMassiv(toCheckLoc2.getBlock().getLocation())) {	
			Cheating = true;	
			block = toCheckLoc2.getBlock().getType();
		}
		
		if(BlockChecks.isMassiv(toCheckLoc3.getBlock().getLocation())) {	
			Cheating = true;	
			block = toCheckLoc3.getBlock().getType();
		}
		
		if(BlockChecks.isMassiv(loc.getBlock().getRelative(0, 1, 0).getLocation())) {	
			Cheating = true;	
			block = loc.getBlock().getRelative(0, 1, 0).getType();
		}
		
		if(BlockChecks.isMassiv(toCheckLoc1.getBlock().getRelative(0, 1, 0).getLocation())) {	
			Cheating = true;	
			block = toCheckLoc1.getBlock().getRelative(0, 1, 0).getType();
		}
		
		if(BlockChecks.isMassiv(toCheckLoc2.getBlock().getRelative(0, 1, 0).getLocation())) {	
			Cheating = true;	
			block = toCheckLoc2.getBlock().getRelative(0, 1, 0).getType();
		}
		
		if(BlockChecks.isMassiv(toCheckLoc3.getBlock().getRelative(0, 1, 0).getLocation())) {	
			Cheating = true;	
			block = toCheckLoc3.getBlock().getRelative(0, 1, 0).getType();
		}
		
		return Cheating;
    }
}						