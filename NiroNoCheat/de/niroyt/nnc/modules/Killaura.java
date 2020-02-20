package de.niroyt.nnc.modules;

import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

import de.niroyt.nnc.Main;
import de.niroyt.nnc.enums.Check;
import de.niroyt.nnc.manager.*;
import de.niroyt.nnc.utils.BlockChecks;
import de.niroyt.nnc.utils.ConfigManager;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;

import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
public class Killaura extends Module {
					
	public static void setHitLock(final Player player, final long time) {		
		if(System.currentTimeMillis() + time < Manager.getPlayerData(player).getKillauraHitLock()) {
			return;
		}
		
		Manager.getPlayerData(player).setKillauraHitLock(System.currentTimeMillis() + time);
	}
	
	public void onDamageByEntityEventHighest(final EntityDamageByEntityEvent e) {
		final Entity damager = e.getDamager();
	        
		if(damager instanceof Player) {
			final Player player = (Player) damager;
			final String playername = player.getName();
			final Entity entity = e.getEntity();
	        	
			if(e.isCancelled()) {
				Manager.getPlayerData(player).setKillauraLastHit(System.nanoTime());
		        Manager.getPlayerData(player).setKillauraLastHitEntity(entity.getEntityId());
				return;
			}
			
			if(Manager.isOP(player)) {
	        	return;
	        }
						
			if(Manager.getPlayerData(player).getKillauraHitFailure() == 0) {
				Manager.getPlayerData(player).setKillauraHitFailure(1);
			}
			
	        final double prozentWert = Double.valueOf(Manager.getPlayerData(player).getKillauraHitSuccessful()) / Double.valueOf(Manager.getPlayerData(player).getKillauraHitFailure()) * 100;
	         	       	
	       	if(prozentWert >= ConfigManager.getCheckConfigValue(Check.ka8, "maxHitAccuracy") 
	       			&& Manager.getPlayerData(player).getKillauraHitSuccessful() > ConfigManager.getCheckConfigValue(Check.ka8, "maxHitSpeed") 
	       			&& Manager.ping(player) < 200) {
	       		if(Manager.debug(player, Check.ka8, "Weird Behaviour")) {
	       			e.setDamage(0);
	       		}
	       	}
	        
	       	if(!Main.isDisabled()) {
	       		if(Manager.getPlayerData(player).getKillauraHitLock() > System.currentTimeMillis()) {
	       			e.setCancelled(true);
	       		} 
	       	}
	       				    					
			if(player.getFallDistance() > 0.0f && !player.isOnGround() && !player.isInsideVehicle() && !BlockChecks.inWeb(player) && BlockChecks.canCheckFly(player)) {
				if(player.getLocation().getY() % 1.0 == 0.0) {					
					if(Manager.debug(player, Check.bp6, "")) {
						setHitLock(player, 3000);
						e.setCancelled(true);
					}	               
				}
			}
			
			Location entityLoc = entity.getLocation().clone();
			Location playerLoc = player.getLocation().clone();
			entityLoc.setY(50);
			playerLoc.setY(50);
			
			if(entityLoc.distance(playerLoc) > 1.5) {
				Class<?> craftPlayer;
				Object converted;
	            Method handle;
	            Object handle2;
	            Method BoundingBox;
	            Object BoundingBox2;
	            Field FieldA;
	            Field FieldC;
	            Field FieldD;
	            Field FieldF;
				double BoxA = 0;
				double BoxC = 0;
				double BoxD = 0;
				double BoxF = 0;
				try {
					craftPlayer = Class.forName("org.bukkit.craftbukkit." + Main.MinecraftVersion + ".entity.CraftEntity");
					converted = craftPlayer.cast(entity);
		            handle = converted.getClass().getMethod("getHandle", new Class[0]);
		            handle2 = handle.invoke(converted, new Object[0]);
		            BoundingBox = handle2.getClass().getMethod("getBoundingBox", new Class[0]);
		            BoundingBox2 = BoundingBox.invoke(handle2, new Object[0]);
		            
		            FieldA = BoundingBox2.getClass().getField("a");
		            BoxA = FieldA.getDouble(BoundingBox2);
		            FieldC = BoundingBox2.getClass().getField("c");
		            BoxC = FieldC.getDouble(BoundingBox2);
		            FieldD = BoundingBox2.getClass().getField("d");
		            BoxD = FieldD.getDouble(BoundingBox2);
		            FieldF = BoundingBox2.getClass().getField("f");
		            BoxF = FieldF.getDouble(BoundingBox2);		            
				} catch (ClassNotFoundException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchFieldException | SecurityException | NoSuchMethodException e1) {
					e1.printStackTrace();
				}
												
				final double width = BoxD - BoxA;
				final double length = BoxF - BoxC;
				
				final double EntitySize = Math.sqrt(width * width + length * length);
							
				final double d1 = playerLoc.distance(entityLoc);
	    		final double d2 = a(playerLoc, entityLoc);
	    		double erkennungsrate = ConfigManager.getCheckConfigValue(Check.ka2, "minValue") - EntitySize / 2;
	
	    		double PlayerSpeed = Manager.getPlayerData(player).getSpeed2OldSpeed() * 4;
	    		if(PlayerSpeed > 1.5) {
	    			PlayerSpeed = 1.5;
	    		}
	    		if(PlayerSpeed < 1) {
	    			PlayerSpeed = 1;
	    		}
	    		    		
	    		erkennungsrate /= PlayerSpeed;
	    		
	    		final double value = d2 - (0.25 / d1);
	    		
				if(value < erkennungsrate) {
					if(Manager.debug(player, Check.ka2, "Entity size: " + Double.valueOf(Math.round(EntitySize * 100)) / 100 + ", Detection rate: " + Double.valueOf(Math.round(erkennungsrate * 100)) / 100 + ", Angle Value: " + Double.valueOf(Math.round(value * 100)) / 100)) {
						setHitLock(player, 1000);
						e.setCancelled(true);
						Manager.getPlayerData(player).setKillauraLastHit(System.nanoTime());
				        Manager.getPlayerData(player).setKillauraLastHitEntity(entity.getEntityId());
	        			return;
	        		}	
				}
			}
			
	       	if(player.getTargetBlock((Set)null, 3) != null) {	      
	       		if(player.getTargetBlock((Set)null, 3).getLocation().distance(player.getEyeLocation()) + 1 < player.getEyeLocation().distance(entity.getLocation()) 
	       				&& BlockChecks.isFullBlock(player.getTargetBlock((Set)null, 3).getType())
	       				&& player.getTargetBlock((Set)null, 3).getType() != Material.AIR) {
	        		if(Manager.debug(player, Check.ka1, "Beat through wall: " + player.getTargetBlock((Set)null, 3).getType())) {
	        			setHitLock(player, 2500);
	        			e.setCancelled(true);
	        			Manager.getPlayerData(player).setKillauraLastHit(System.nanoTime());
	        	        Manager.getPlayerData(player).setKillauraLastHitEntity(entity.getEntityId());
	        			return;
	        		}		            	
	        	}
	        }
	       		        			        
	        if((System.nanoTime() - Manager.getPlayerData(player).getKillauraLastHit()) / 1000000 <= ConfigManager.getCheckConfigValue(Check.ka6, "minValue")
	        		&& Manager.ping(player) < 60
	        		&& Main.getServerLag() < 50) {
	        	if(Manager.getPlayerData(player).getKillauraLastHitEntity() != entity.getEntityId()) {
	        		if(Manager.debug(player, Check.ka6, "Weird Behaviour")) {
	        			e.setCancelled(true);
	        			setHitLock(player, 1000);
	        			Manager.getPlayerData(player).setKillauraLastHit(System.nanoTime());
	        	        Manager.getPlayerData(player).setKillauraLastHitEntity(entity.getEntityId());
	        			return;
	        		}
	        	}
	        }
	        
	        Manager.getPlayerData(player).setKillauraLastHit(System.nanoTime());
	        Manager.getPlayerData(player).setKillauraLastHitEntity(entity.getEntityId());
	        
	        if(Manager.ping(player) + Main.getServerLag() < 60) {
	        	if((System.nanoTime() - Manager.getPlayerData(player).getKillauraLastMove()) / 1000000 < ConfigManager.getCheckConfigValue(Check.ka7, "minValue")) {	   	        		
	        		if(System.currentTimeMillis() - Manager.getPlayerData(player).getKillauraMoveTimeLastFlag() < 1000) {
	        			if(Manager.debug(player, Check.ka7, "Weird Behaviour")) {
	        				setHitLock(player, 1500);
	        				e.setCancelled(true);
	        			}
	        		}
	        		
	        		Manager.getPlayerData(player).setKillauraMoveTimeLastFlag(System.currentTimeMillis());
	        	} 
	        }
		}	
	}
		    
	public void onMove(final PlayerMoveEvent e) {
		final Player player = e.getPlayer();
		
		if(e.isCancelled()) {
			return;
		}
		
		if(Manager.isOP(player)) {
			return;
		}
				
		if(e.getTo().getPitch() == e.getFrom().getPitch() 
				&& !player.isInsideVehicle()
				&& (calculateDifferenceBetweenAngles(e.getTo().getYaw(), e.getFrom().getYaw()) > 5 || calculateDifferenceBetweenAngles(e.getTo().getYaw(), e.getFrom().getYaw()) < -5)
				&& e.getTo().getPitch() != 90 && e.getFrom().getPitch() != -90) {
			Manager.getPlayerData(player).setKillauraSamePitch(Manager.getPlayerData(player).getKillauraSamePitch() + 1);
			
			if(Manager.getPlayerData(player).getKillauraSamePitch() > ConfigManager.getCheckConfigValue(Check.ka4, "maxValue")
					&& (calculateDifferenceBetweenAngles(e.getTo().getYaw(), e.getFrom().getYaw()) > 10 || calculateDifferenceBetweenAngles(e.getTo().getYaw(), e.getFrom().getYaw()) < -10)) {
				if(Manager.debug(player, Check.ka4, "Weird aiming")) {
					setHitLock(player, 2000);
				}
			}
		} else if(e.getTo().getPitch() != e.getFrom().getPitch()) {
			Manager.getPlayerData(player).setKillauraSamePitch(0);
		}
		
		Manager.getPlayerData(player).setKillauraLastYawSpeed(calculateDifferenceBetweenAngles(e.getTo().getYaw(), e.getFrom().getYaw()));
		Manager.getPlayerData(player).setKillauraLastMove(System.nanoTime());
	}
	
	private double calculateDifferenceBetweenAngles(final double firstAngle, final double secondAngle) {
		double difference = secondAngle - firstAngle;
		while (difference < -180) difference += 360;
		while (difference > 180) difference -= 360;
		return difference;
	}
	    
	public void onTick() {	    			
		for(final Player player : Bukkit.getOnlinePlayers()) {				
			if(Manager.getPlayerData(player).getKillauraHitDiffReset().isDelayComplete(750L)) {
				Manager.getPlayerData(player).getKillauraHitDiffReset().setLastMS();
				
				Manager.getPlayerData(player).setKillauraHitSuccessful((int) (Manager.getPlayerData(player).getKillauraHitSuccessful() * 0.8));
		        
				Manager.getPlayerData(player).setKillauraHitFailure((int) (Manager.getPlayerData(player).getKillauraHitFailure() * 0.8));
			}
			
		}
	}		
	
	public static double a(final Location location, final Location location2) {
		location.setY(20.0);
		location2.setY(20.0);
		final Vector subtract = location2.toVector().subtract(location.toVector());
		final Location clone = location.clone();
		clone.setPitch(0.0f);
		return subtract.normalize().dot(clone.getDirection());
	}
}
