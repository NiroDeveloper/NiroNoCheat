package de.niroyt.nnc.modules;

import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

import de.niroyt.nnc.Main;
import de.niroyt.nnc.enums.Check;
import de.niroyt.nnc.manager.*;
import de.niroyt.nnc.utils.ConfigManager;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.*;

public class Reach extends Module {
	    
	public void onMove(final PlayerMoveEvent e) {
		final Player player = e.getPlayer();
		
		final double motionX = e.getTo().getX() - e.getFrom().getX();
		final double motionY = e.getTo().getY() - e.getFrom().getY();
		final double motionZ = e.getTo().getZ() - e.getFrom().getZ();
		
		Vector MovementSpeed = new Vector(motionX, motionY, motionZ);
		
		if(Manager.getPlayerData(player).getReachOldMovements() != null) {
			MovementSpeed = new Vector((MovementSpeed.getX() + Manager.getPlayerData(player).getReachOldMovements().getX()) / 2, 
				(MovementSpeed.getY() + Manager.getPlayerData(player).getReachOldMovements().getY()) / 2, 
				(MovementSpeed.getZ() + Manager.getPlayerData(player).getReachOldMovements().getZ()) / 2);
		}
							
		Manager.getPlayerData(player).setReachOldMovements(MovementSpeed);
	}
	
    public void onDamageByEntityEvent(final EntityDamageByEntityEvent event) {
    	if(event.getDamager() instanceof Player) {    		
    		final Player player = (Player) event.getDamager();
    		
    		if(Manager.isOP(player)) {
            	return;
            }
    		
    		final Entity entity = event.getEntity();    		
    		
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
            Field FieldB;
            Field FieldE;
			double BoxA = 0;
			double BoxC = 0;
			double BoxD = 0;
			double BoxF = 0;
			double BoxB = 0;
			double BoxE = 0;
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
	            FieldB = BoundingBox2.getClass().getField("b");
	            BoxB = FieldB.getDouble(BoundingBox2);
	            FieldE = BoundingBox2.getClass().getField("e");
	            BoxE = FieldE.getDouble(BoundingBox2);	
			} catch (ClassNotFoundException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchFieldException | SecurityException | NoSuchMethodException e1) {
				e1.printStackTrace();
			}
											
			final double width = BoxD - BoxA;
			final double length = BoxF - BoxC;
			final double height = BoxE - BoxB;
			
			final double EntitySize = Math.sqrt(width * width + length * length);    		
			double value = entity.getLocation().distance(player.getEyeLocation()) - EntitySize / 2;
			
			double maxdifferenz = ConfigManager.getCheckConfigValue(Check.r1, "maxValue");
						
			if(entity instanceof Player && Manager.getPlayerData(player).getReachOldMovements() != null)  {
				final Player victim = (Player) entity;
				
				if(Manager.getPlayerData(victim).getReachOldMovements() != null) {
					double DiffX = Manager.getPlayerData(player).getReachOldMovements().getX() - Manager.getPlayerData(victim).getReachOldMovements().getX();
					double DiffY = Manager.getPlayerData(player).getReachOldMovements().getY() - Manager.getPlayerData(victim).getReachOldMovements().getY();
					double DiffZ = Manager.getPlayerData(player).getReachOldMovements().getZ() - Manager.getPlayerData(victim).getReachOldMovements().getZ();
					
					double distance = Math.sqrt(Math.sqrt(DiffX * DiffX + DiffZ * DiffZ) * Math.sqrt(DiffX * DiffX + DiffZ * DiffZ) + DiffY * DiffY);
					
					maxdifferenz += distance * 3;
					
					maxdifferenz += Manager.getPlayerData(player).getReachOldMovements().length() * 2;
				} else {
					maxdifferenz += 1;
				}
			} else {
				maxdifferenz += 1;
			}
			
			if(player.getGameMode() == GameMode.CREATIVE) {
    			maxdifferenz *= 1.25;
    		}
			
			value -= height * ((player.getLocation().getY() - entity.getLocation().getY()) / maxdifferenz) / 2;			
    		maxdifferenz += Manager.ping(player) / 1000;    		
    		String message = "Reach: " + round(entity.getLocation().distance(player.getEyeLocation())) + ", Max: " + round(maxdifferenz) + ", Value: " + round(value);
    		
    		if(value > maxdifferenz) {
    			if(Manager.debug(player, Check.r1, message)) {
    				if(value > maxdifferenz + 0.3) {
    					Killaura.setHitLock(player, 4000);
    				}
    				
    				event.setCancelled(true);
    			}
    		} 
    	}
    }
    
    public double round(final double value) {
    	return Double.valueOf(Math.round(value * 1000)) / 1000;
    }
    
	public void onInteract(final PlayerInteractEvent e) {
		if(Manager.isOP(e.getPlayer())) {
			return;
		}
		
		if(e.getClickedBlock() == null) {
			return;
		}
		
		if(Manager.getPlayerData(e.getPlayer()).getLastTeleportPos() != null) {
			final Location playerLoc = e.getPlayer().getLocation().clone();
			playerLoc.setYaw(0);
			playerLoc.setPitch(0);
			
			final Location TeleportLoc = Manager.getPlayerData(e.getPlayer()).getLastTeleportPos().clone();
			TeleportLoc.setYaw(0);
			TeleportLoc.setPitch(0);
			
			if(TeleportLoc.toVector().toString().equalsIgnoreCase(playerLoc.toVector().toString())) {
				return;
			}
		}
				 		
		final Block b = e.getClickedBlock();
		final Player player = e.getPlayer();
		final Location blockloc = b.getLocation();
		final Location playerloc = player.getEyeLocation();
		
		if(playerloc.distance(blockloc) >= ConfigManager.getCheckConfigValue(Check.r2, "maxValue") + ((player.getGameMode() == GameMode.CREATIVE) ? 1.8 : 0)) {
			if(Manager.debug(player, Check.r2, "Reach: " + round(playerloc.distance(blockloc)))) {
				e.setCancelled(true);
			}				
		}
	}
}
