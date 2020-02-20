package de.niroyt.nnc.modules;


import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import de.niroyt.nnc.Main;
import de.niroyt.nnc.enums.Check;
import de.niroyt.nnc.manager.Manager;
import de.niroyt.nnc.manager.Module;
import de.niroyt.nnc.utils.ConfigManager;
import de.niroyt.nnc.utils.Flag;

public class World extends Module {

    public void onTick() {
    	for(final Player player : Bukkit.getOnlinePlayers()) {
    		if(Manager.getPlayerData(player).getWorldScaffoldCountReset().isDelayComplete(200)) {
    			Manager.getPlayerData(player).getWorldScaffoldCountReset().setLastMS();
    			
    			Manager.getPlayerData(player).setWorldScaffoldBlockCount(0);
    		}
    		
    		if(Manager.getPlayerData(player).getWorldTowerCountReset().isDelayComplete(800)) {
    			Manager.getPlayerData(player).getWorldTowerCountReset().setLastMS();
    			
    			Manager.getPlayerData(player).setWorldTowerBlockCount(0);
    		}
    	}
    }
    
    public void onBlockPlace(final BlockPlaceEvent e) {
    	final Player player = e.getPlayer();
    	 	
    	if(Manager.ping(player) < 50) {
    		int oldValue = Manager.getPlayerData(player).getWorldPlaceBlockTimeFlags();    		
        	if((System.nanoTime() - Manager.getPlayerData(player).getKillauraLastMove()) / 1000000 < ConfigManager.getCheckConfigValue(Check.fa7, "minValue")) {
        		oldValue++;
        		if(oldValue >= 3) {
        			if(Manager.debug(player, Check.fa7, "Weird Behaviour")) {
            			e.setCancelled(true);    
            			Manager.getPlayerData(player).setWorldPlaceBlockLock(System.currentTimeMillis() + 1000);
            		} 
            		oldValue--;
        		}
        		
        	} else {
        		oldValue--;
        	}
        	if(oldValue < 0) {
        		oldValue = 0;
        	}
        	
        	Manager.getPlayerData(player).setWorldPlaceBlockTimeFlags(oldValue);
        }
    	
    	if(Manager.getPlayerData(player).getWorldPlaceBlockLock() > System.currentTimeMillis() && !Main.isDisabled()) {
    		e.setCancelled(true);
    	} 
    	    	
    	if(e.isCancelled()) {
    		return;
    	}
    	
    	if(Manager.isOP(player)) {
    		return;
    	}
    	
    	final Location loc1 = new Location(e.getBlock().getWorld(), e.getBlock().getX() + 0.5, e.getBlock().getY() + 0.5, e.getBlock().getZ() + 0.5);
    	final Location loc2 = new Location(e.getBlockAgainst().getWorld(), e.getBlockAgainst().getX() + 0.5, e.getBlockAgainst().getY() + 0.5, e.getBlockAgainst().getZ() + 0.5);
	    
    	if(player.getLocation().getBlock().getY() < e.getBlock().getY()) {
	    	if(player.getEyeLocation().distance(loc1) > player.getEyeLocation().distance(loc2)
	    			&& player.getEyeLocation().distance(e.getBlock().getLocation()) > 2) {
		   		if(Manager.debug(player, Check.fa1, "")) {
		   			e.setCancelled(true);
		   		}
	    	}
    	} else {	    	
    		if(player.getEyeLocation().distance(loc1) > player.getEyeLocation().distance(loc2)
		   			&& player.getEyeLocation().distance(e.getBlock().getLocation()) > 1.8
		   			&& player.getLocation().getBlockY() - 1 == e.getBlock().getLocation().getBlockY()
		   			&& player.getLocation().getBlockY() - 1 == e.getBlockAgainst().getLocation().getBlockY()
		   			&& player.isOnGround()) {
	    		
	    		if(Manager.debug(player, Check.fa2, "")) {
	    			e.setCancelled(true);
	    		}
		    }
	    }
	    
    	int x = player.getLocation().getBlockX();
    	int z = player.getLocation().getBlockZ();
   		
    	if(e.getBlock().getY() + 1 == player.getLocation().getY()
  			&& e.getBlockAgainst().getY() + 1 == player.getLocation().getY()
   			&& e.getBlock().getLocation().getBlockX() == player.getLocation().getBlockX()
   			&& e.getBlock().getLocation().getBlockZ() == player.getLocation().getBlockZ()) {
    				    		
    		if(player.getLocation().getPitch() == 90) {
    			if(Manager.debug(player, Check.fa6, "")) {
    				e.setCancelled(true);
    				return;
    			}    			
    		}
    		   			   			
   			boolean SameLine = false;
   			
   			if(Manager.getPlayerData(player).getWorldScaffoldPosX() != 0) {
   				if(Manager.getPlayerData(player).getWorldScaffoldPosX() == player.getLocation().getBlockX()
   						&& e.getBlock().getLocation().getBlockX() == e.getBlockAgainst().getLocation().getBlockX()) {
   					SameLine = true;
   				} else {
   					Manager.getPlayerData(player).setWorldScaffoldPosX(player.getLocation().getBlockX());
   				}
   			} else {
   				Manager.getPlayerData(player).setWorldScaffoldPosX(player.getLocation().getBlockX());
   			}
    				
   			if(Manager.getPlayerData(player).getWorldScaffoldPosZ() != 0) {
    			if(Manager.getPlayerData(player).getWorldScaffoldPosZ() == player.getLocation().getBlockZ() 
    					&& e.getBlock().getLocation().getBlockZ() == e.getBlockAgainst().getLocation().getBlockZ()) {
    				if(!SameLine) {
    					SameLine = true;
    				} else {
    					Manager.getPlayerData(player).setWorldScaffoldPosZ(player.getLocation().getBlockZ());
    				}
    			} else {
    				Manager.getPlayerData(player).setWorldScaffoldPosZ(player.getLocation().getBlockZ());
    			}
   			} else {
   				Manager.getPlayerData(player).setWorldScaffoldPosZ(player.getLocation().getBlockZ());
   			}
    				
   			if(SameLine && Manager.ping(player) < 250) {
   				Manager.getPlayerData(player).setWorldScaffoldBlockCount(Manager.getPlayerData(player).getWorldScaffoldBlockCount() + 1);
    			
    	    	if(Manager.getPlayerData(player).getWorldScaffoldBlockCount() >= 2) {
    	    		Manager.getPlayerData(player).setWorldScaffoldBlockCount(0);
    		    		
    		    	if(Manager.debug(player, Check.ws1, "")) {
    		    		player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 80, 255, false, false));
    		    		player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 80, 128, false, false));  
    		    			
    		    		Manager.getPlayerData(player).setWorldPlaceBlockLock(System.currentTimeMillis() + 4000);
    		    		
    		    		Flag.backPort(player);
    		    	}   
    			}
    		}    			
    	}
    	
    	if(e.getBlock().getY() + 1 == player.getLocation().getBlockY()) {
    		if(e.getBlockAgainst().getY() + 2 == player.getLocation().getBlockY()) {
    			boolean gleich1 = false;
    			boolean gleich2 = false;
    				
    			if(Manager.getPlayerData(player).getWorldTowerPosX() != 0) {
    				x = Manager.getPlayerData(player).getWorldTowerPosX();
    				if(x == e.getBlock().getLocation().getX()) {
    					gleich1 = true;
    				} else {
    					Manager.getPlayerData(player).setWorldTowerPosX(player.getLocation().getBlockX());
    				}
    			} else {
    				Manager.getPlayerData(player).setWorldTowerPosX(player.getLocation().getBlockX());
    			}
    				
    			if(Manager.getPlayerData(player).getWorldTowerPosZ() != 0) {
    				z = Manager.getPlayerData(player).getWorldTowerPosZ();
    				if(z == e.getBlock().getLocation().getZ()) {
    					gleich2 = true;
    				} else {
    					Manager.getPlayerData(player).setWorldTowerPosZ(player.getLocation().getBlockZ());
    				}
    			} else {
    				Manager.getPlayerData(player).setWorldTowerPosZ(player.getLocation().getBlockZ());
    			}
    				
   				if(gleich1 && gleich2 && Manager.ping(player) < 120) {
   					Manager.getPlayerData(player).setWorldScaffoldBlockCount(Manager.getPlayerData(player).getWorldScaffoldBlockCount() + 1);
   	    			
   	    	    	if(Manager.getPlayerData(player).getWorldScaffoldBlockCount() > 2) {
   	    	    		Manager.getPlayerData(player).setWorldScaffoldBlockCount(0);

    			    	if(player.getLocation().getPitch() < 60) {
   			    			if(Manager.debug(player, Check.wt1, "")) {
   			    				e.setCancelled(true);
   			    				return;
   			    			}    		    			
    		    		}
    			    		
    		    		if(Manager.debug(player, Check.wt2, "")) {
    		    			player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 80, 255, false, false));
    		    			player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 80, 128, false, false));
    			    		
    		    			e.setCancelled(true);
    		    			
    		    			Manager.getPlayerData(player).setWorldPlaceBlockLock(System.currentTimeMillis() + 5000);
    		    		}
    				}
    			}
    		}
    	}
    	    	
    	final BlockFace bf = e.getBlockAgainst().getFace(e.getBlockPlaced());
		double z1 = e.getBlockAgainst().getZ();
    	double x1 = e.getBlockAgainst().getX();
    	double z2 = e.getBlockAgainst().getZ();
    	double x2 = e.getBlockAgainst().getX();
    	    	
		if(bf == BlockFace.NORTH) {
			x2 += 1;
		} else if(bf == BlockFace.EAST) {
			x1 += 1;
			x2 += 1;
			z2 += 1;
		} else if(bf == BlockFace.SOUTH) {
			x2 += 1;
			z2 += 1;
			z1 += 1;
		} else if(bf == BlockFace.WEST) {
			z1 += 1;				
		} else {
			return;
		}
		
		final double p2Oben[] = getRotationToPosition(player, x2, e.getBlockAgainst().getY()+1, z2);
		final double p2Unten[] = getRotationToPosition(player, x2, e.getBlockAgainst().getY(), z2);
		
		final double p1Oben[] = getRotationToPosition(player, x1, e.getBlockAgainst().getY()+1, z1);
		final double p1Unten[] = getRotationToPosition(player, x1, e.getBlockAgainst().getY(), z1);
		
		double dif = (p1Oben[1] - p1Unten[1]) - (p2Oben[1] - p2Unten[1]);
		if(dif < 0) {
			dif *= -1;
		}
						
		double pos2 = (p2Oben[0] < p2Unten[0] ? p2Oben[0] : p2Unten[0]);    
		double pos1 = (p1Oben[0] < p1Unten[0] ? p1Unten[0] : p1Oben[0]) - (pos2 < 0 ? pos2 : 0);    	
		double yaw = player.getLocation().getYaw() - (pos2 < 0 ? pos2 : 0);
    	if(yaw >= 360) {
    		yaw %= 360;
    	}
    	if(pos2 < 0) {
    		pos2 = 0;    		
    		if(pos1 < 0) {
    			pos1 *= -1;
    			yaw = 360 - yaw;
    		}
    	}
    	if(yaw < 0) {
    		yaw += 360;
    	}
    	if(pos1 < yaw) {
    		pos2 += 360;
    	}    	 
    	if(pos2 == 360 && yaw > 360) {
    		yaw -= 360;
    		pos2 = 0;
    	}
    	double dif1 = pos1 - pos2;
    	if(dif1 < 0) {
    		dif1 *= -1;
    	}
    	final double dif2 = ((pos1 > pos2 ? pos1 : pos2) - yaw) / dif1;
    	
    	if(dif2 > 1 || dif2 < 0) {
    		if(Manager.debug(player, Check.fa3, "")) {
    			if(System.currentTimeMillis() - Manager.getPlayerData(player).getWorldLastAngleFlag() < 3000) {
    				e.setCancelled(true);
    				Manager.getPlayerData(player).setWorldPlaceBlockLock(System.currentTimeMillis() + 1500);  
    			}    			
    			
    			Manager.getPlayerData(player).setWorldLastAngleFlag(System.currentTimeMillis());
    			return;
    		}    		
    	}
    	
    	double z3 = e.getBlockAgainst().getZ();
    	double x3 = e.getBlockAgainst().getX();
    	if(bf == BlockFace.NORTH) {
			x3 += dif2;
		} else if(bf == BlockFace.EAST) {
			x3 += 1;
			z3 += dif2;			
		} else if(bf == BlockFace.SOUTH) {
			z3 += 1;
			x3 += 1 - dif2;
		} else if(bf == BlockFace.WEST) {
			z3 += 1 - dif2;				
		} 
    	    	    	
    	if(dif < 0.12) {
    		dif = 0.12;
    	}
    	
    	final double a = getRotationToPosition(player, x3, e.getBlockAgainst().getY(), z3)[1] + dif * 10;
    	if(a < player.getLocation().getPitch()) {
    		if(Manager.debug(player, Check.fa4, Double.valueOf(Math.round(a * 100)) / 100 + " < " + Double.valueOf(Math.round(player.getLocation().getPitch() * 100)) / 100 + ", Diff: " + Double.valueOf(Math.round(dif * 100)) / 100)) {
    			if(System.currentTimeMillis() - Manager.getPlayerData(player).getWorldLastAngleFlag() < 3000) {
    				e.setCancelled(true);
    				Manager.getPlayerData(player).setWorldPlaceBlockLock(System.currentTimeMillis() + 500);
    			}    			
    			
    			Manager.getPlayerData(player).setWorldLastAngleFlag(System.currentTimeMillis());
    		}
    	}
    	
    	final double b = getRotationToPosition(player, x3, e.getBlockAgainst().getY() + 1, z3)[1] - dif * 10;
    	if(b > player.getLocation().getPitch()) {
    		if(Manager.debug(player, Check.fa5, Double.valueOf(Math.round(b * 100)) / 100 + " > " + Double.valueOf(Math.round(player.getLocation().getPitch() * 100)) / 100 + ", Diff: " + Double.valueOf(Math.round(dif * 100)) / 100)) {
    			if(System.currentTimeMillis() - Manager.getPlayerData(player).getWorldLastAngleFlag() < 3000) {
    				e.setCancelled(true);
    				Manager.getPlayerData(player).setWorldPlaceBlockLock(System.currentTimeMillis() + 500);
    			}    			
    			
    			Manager.getPlayerData(player).setWorldLastAngleFlag(System.currentTimeMillis());
    		}
    	}
    }
        
	public static double[] getRotationToPosition(final Player p, final double xx, final double yy, final double zz) {
		final Location e = p.getLocation();
		final double x = xx - e.getX();
		final double y = yy - (e.getY() + 1.62);
		final double z = zz - e.getZ();
		final double helper = Math.sqrt(x * x + z * z);
		float newYaw = (float)Math.toDegrees(-Math.atan(x / z));
		final float newPitch = (float)-Math.toDegrees(Math.atan(y / helper));
		
     	if ((z < 0.0D) && (x < 0.0D)) {
     		newYaw = (float)(90.0D + Math.toDegrees(Math.atan(z / x)));
     	} else if ((z < 0.0D) && (x > 0.0D)) {
     		newYaw = (float)(-90.0D + Math.toDegrees(Math.atan(z / x)));
     	}
     	     	
     	return new double[] { Double.valueOf(newYaw), Double.valueOf(newPitch) };
    }
}