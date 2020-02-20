package de.niroyt.nnc.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;


import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import de.niroyt.nnc.Main;
import de.niroyt.nnc.enums.Check;
import de.niroyt.nnc.manager.Manager;
import de.niroyt.nnc.manager.Module;
import de.niroyt.nnc.modules.FastBreak;
import de.niroyt.nnc.modules.Fly;
import de.niroyt.nnc.modules.Inventory;
import de.niroyt.nnc.modules.NoSlowdown;
import de.niroyt.nnc.modules.Packets;
import io.netty.channel.Channel;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;

public class PacketReader {
	 
	Player player;
    Channel channel;
        
    public PacketReader(Player player) {
    	this.player = player;
    	
    	try {
    		final Object CraftPlayer = Class.forName("org.bukkit.craftbukkit." + Main.MinecraftVersion + ".entity.CraftPlayer").cast(this.player);
    		final  Method handle = player.getClass().getMethod("getHandle", new Class[0]);
    		final Object entityPlayer = handle.invoke(player, new Object[0]);
            
    		final Field playerConnection = entityPlayer.getClass().getField("playerConnection");
    		final Object playerConnection2 = playerConnection.get(entityPlayer);
            
    		final  Field networkManager = playerConnection2.getClass().getField("networkManager");
            final Object networkManager2 = networkManager.get(playerConnection2);
            
            final Field channelField = networkManager2.getClass().getField("channel");
            channel = (Channel) channelField.get(networkManager2);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchFieldException | SecurityException | NoSuchMethodException | ClassNotFoundException e1) {
			e1.printStackTrace();
		}
    	
    	inject();
    }

    public void inject() {  	
    	final ChannelDuplexHandler CDH = new ChannelDuplexHandler() {
    		@Override
    		public void channelRead(ChannelHandlerContext chc, Object packet) throws Exception {
    			readInPackets(packet);
    			super.channelRead(chc, packet);
    		}
    		
    		@Override
    		public void write(ChannelHandlerContext chc, Object packet, ChannelPromise CP) throws Exception {
    			readOutPackets(packet);
    			super.write(chc, packet, CP);
    		}
    	};
    	
    	channel.pipeline().addBefore("packet_handler", player.getName(), CDH);
    }

    public void uninject() {
    	try {
    		channel.eventLoop().submit(() -> {
    			channel.pipeline().remove(player.getName());
    			return null;
    		});
		} catch (Exception e) {}        	
    }

    private void readInPackets(final Object packet) {         	
    	Packets.PacketCount(player);
    	
    	if(packet.getClass().getSimpleName().equals("PacketPlayInPositionLook") || packet.getClass().getSimpleName().equals("PacketPlayInLook")
    			 || packet.getClass().getSimpleName().equals("PacketPlayInFlying") || packet.getClass().getSimpleName().equals("PacketPlayInPosition")) {    		
    		try {
    			final Object PacketPlayInFlying = Class.forName("net.minecraft.server." + Main.MinecraftVersion + ".PacketPlayInFlying").cast(packet);
    			
    			float ValueYaw = -1337;
    			float ValuePitch = -1337;
    			double ValueX = -1337;
    			double ValueY = -1337;
    			double ValueZ = -1337;
    			
    			if(Main.MinecraftVersion.equalsIgnoreCase("v1_8_R3")) {
    				ValueYaw = (float) PacketPlayInFlying.getClass().getMethod("d").invoke(PacketPlayInFlying);
    				ValuePitch = (float) PacketPlayInFlying.getClass().getMethod("e").invoke(PacketPlayInFlying);
    				
    				ValueX = (double) PacketPlayInFlying.getClass().getMethod("a").invoke(PacketPlayInFlying);
    				ValueY = (double) PacketPlayInFlying.getClass().getMethod("b").invoke(PacketPlayInFlying);
    				ValueZ = (double) PacketPlayInFlying.getClass().getMethod("c").invoke(PacketPlayInFlying);
    			} else {
    				ValueYaw = (float) PacketPlayInFlying.getClass().getMethod("a", float.class).invoke(PacketPlayInFlying, -1337);
    				ValuePitch = (float) PacketPlayInFlying.getClass().getMethod("b", float.class).invoke(PacketPlayInFlying, -1337);
    				
    				ValueX = (double) PacketPlayInFlying.getClass().getMethod("a", double.class).invoke(PacketPlayInFlying, -1337.0);
    				ValueY = (double) PacketPlayInFlying.getClass().getMethod("b", double.class).invoke(PacketPlayInFlying, -1337.0);
    				ValueZ = (double) PacketPlayInFlying.getClass().getMethod("c", double.class).invoke(PacketPlayInFlying, -1337.0);
    			}
    			
    			if((ValueYaw != -1337 || ValuePitch != -1337) 
    					&& (packet.getClass().getSimpleName().equals("PacketPlayInPositionLook") || packet.getClass().getSimpleName().equals("PacketPlayInLook"))) {
    				Packets.LookingPacket(player, ValueYaw, ValuePitch);   
    			}
    			
    			Location NewPosition = new Location(player.getWorld(), ValueX, ValueY, ValueZ);
    	    	        		
        		if(Manager.getPlayerData(player).getLastTeleportPos() != null) {        			
        			if(Manager.getPlayerData(player).getPingTeleportTime() != -1 && Manager.getPlayerData(player).getLastTeleportPos().distance(NewPosition) == 0) {
        	        	int delay = ((int) (System.nanoTime() - Manager.getPlayerData(player).getPingTeleportTime()) / 2000000) - Main.getServerLag();    			
        				if(delay < 0) {
        					delay = 0;
        				}
        				
        				final int diff = Math.abs(Manager.getPing(player) - delay);
        				
        				if(diff > ConfigManager.getCheckConfigValue(Check.bp8, "maxDifference") * (Main.MinecraftVersion.equalsIgnoreCase("v1_8_R3") ? ConfigManager.getCheckConfigValue(Check.bp8, "1-8-x-Multiply") : 1)
        						 && !Manager.isOP(player)) {
        					if(Manager.debug(player, Check.bp8, "Showed: " + Manager.getPing(player) + ", Real: " + delay)) {        				
        						Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), new Runnable() {									
									@Override
									public void run() {
										player.getInventory().setHeldItemSlot((player.getInventory().getHeldItemSlot() + 1) % 9);
										player.teleport(player.getLocation().add(0, 0.000001, 0));
									}
								}, 20L);
        						        						
        						Manager.getPlayerData(player).setPing(delay);	
        					}
        				} 
        				
        				Manager.getPlayerData(player).setPingTeleportTime(-1);
        	        }
        		}
    		} catch (Exception e) {
    			e.printStackTrace();
    		}
    	} 
    	
    	if(packet.getClass().getSimpleName().equals("PacketPlayInHeldItemSlot")) {    		
    		if(NoSlowdown.isUsingItem(player)) {
    			NoSlowdown.setNotUsingItem(player);    			
    		}
    		
    		if(Manager.getPlayerData(player).getPingFirstChangeSlot() != -1) {
    			int delay = ((int) (System.nanoTime() - Manager.getPlayerData(player).getPingFirstChangeSlot()) / 2000000) - Main.getServerLag();    			
    			if(delay < 0) {
    				delay = 0;
    			}
    			
				final int diff = Math.abs(Manager.getPing(player) - delay);
				
				if(diff > ConfigManager.getCheckConfigValue(Check.bp9, "maxDifference") * (Main.MinecraftVersion.equalsIgnoreCase("v1_8_R3") ? ConfigManager.getCheckConfigValue(Check.bp9, "1-8-x-Multiply") : 1)
						 && !Manager.isOP(player)) {
					if(Manager.debug(player, Check.bp9, "Showed: " + Manager.getPing(player) + ", Real: " + delay)) {
						Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), new Runnable() {									
							@Override
							public void run() {
								player.getInventory().setHeldItemSlot((player.getInventory().getHeldItemSlot() + 1) % 9);
								player.teleport(player.getLocation().add(0, 0.000001, 0));
							}
						}, 20L);
						
						Manager.getPlayerData(player).setPing(delay);	
					}
				} 
    			    			
    			Manager.getPlayerData(player).setPingFirstChangeSlot(-1);
    		}
    	}
    	
    	if(packet.getClass().getSimpleName().equals("PacketPlayInClientCommand") 
    			&& getValue(packet, "a").toString().equals("OPEN_INVENTORY_ACHIEVEMENT")) {
    		Manager.getPlayerData(player).setInventoryOpenPacket(true);
    		
    		Inventory.InventoryOpen(player);
    	}
    	
    	if(packet.getClass().getSimpleName().equals("PacketPlayInCloseWindow")) {    		
    		if(getValue(packet, "id").toString().equals("0")) {
    			Inventory.InventoryClose(player);
    		}
    		if(Main.OptionGui.containsKey(player.getUniqueId())) {
    			if(Main.OptionGui.get(player.getUniqueId()).closeInv()) {
    				Main.OptionGui.remove(player.getUniqueId());
    			}    			
    		}
    	}
    	
    	if(packet.getClass().getSimpleName().equals("PacketPlayInBlockPlace")) {
    		if(Main.MinecraftVersion.equalsIgnoreCase("v1_8_R3")) {
    			Location BlockLocation = null;
    			try {
    				final Object BlockPosClass = Class.forName("net.minecraft.server." + Main.MinecraftVersion + ".BlockPosition").cast(getValue(packet, "a"));
    				BlockLocation = new Location(player.getWorld(), 
    						(int) BlockPosClass.getClass().getMethod("getX").invoke(BlockPosClass), 
    						(int) BlockPosClass.getClass().getMethod("getY").invoke(BlockPosClass), 
    						(int) BlockPosClass.getClass().getMethod("getZ").invoke(BlockPosClass));
    			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
    					| NoSuchMethodException | SecurityException | ClassNotFoundException e) {
    				e.printStackTrace();
    			}
        		
        		if(BlockLocation.getX() == -1 && BlockLocation.getY() == -1 && BlockLocation.getZ() == -1 && (int) getValue(packet, "c") == 255) {
        			NoSlowdown.setUseItem(player);
        		}
    		} else {
    			NoSlowdown.setUseItem(player);
    		}
    	}
    	
    	if(packet.getClass().getSimpleName().equals("PacketPlayInBlockDig")) {
    		Location BlockPos = null;
			try {
				final Object BlockPosClass = Class.forName("net.minecraft.server." + Main.MinecraftVersion + ".BlockPosition").cast(getValue(packet, "a"));
				BlockPos = new Location(player.getWorld(), 
						(int) BlockPosClass.getClass().getMethod("getX").invoke(BlockPosClass), 
						(int) BlockPosClass.getClass().getMethod("getY").invoke(BlockPosClass), 
						(int) BlockPosClass.getClass().getMethod("getZ").invoke(BlockPosClass));
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
					| NoSuchMethodException | SecurityException | ClassNotFoundException e) {
				e.printStackTrace();
			}
    		
    		if(getValue(packet, "c").toString().equals("START_DESTROY_BLOCK")) {
    			FastBreak.startDestroyBlock(player, new Location(player.getWorld(), BlockPos.getX(), BlockPos.getY(), BlockPos.getZ()));
    			
    	    	if(player.getTargetBlock((Set)null, 3).getType() != null 
    	    			&& player.getTargetBlock((Set)null, 3).getType() != Material.AIR
    	    			&& Manager.getPlayerData(player).getLastPacket().getClass().getSimpleName().equals("PacketPlayInArmAnimation")) {
    	        	Manager.getPlayerData(player).setGhostHandKillauraClickCount(Manager.getPlayerData(player).getGhostHandKillauraClickCount() - 1);
    	    	}
    	    	Manager.getPlayerData(player).setGhostHandPlayersInBreakBlock(true);
    		}
    		if(getValue(packet, "c").toString().equals("STOP_DESTROY_BLOCK")) {
    			FastBreak.stopDestroyBlock(player, new Location(player.getWorld(), BlockPos.getX(), BlockPos.getY(), BlockPos.getZ()));
    		}    		
    		if(getValue(packet, "c").toString().equals("ABORT_DESTROY_BLOCK")) {
    			Manager.getPlayerData(player).setGhostHandPlayersInBreakBlock(false);
    			
    			if(player.getTargetBlock((Set)null, 3).getType() != null 
    	    			&& player.getTargetBlock((Set)null, 3).getType() != Material.AIR) {
    				Manager.getPlayerData(player).setGhostHandKillauraClickCount(Manager.getPlayerData(player).getGhostHandKillauraClickCount() + 1);
    	    	}
    		}
    		    		
    		if(BlockPos.getX() == 0.0 && BlockPos.getY() == 0.0 && BlockPos.getZ() == 0.0
    				&& getValue(packet, "c").toString().equalsIgnoreCase("RELEASE_USE_ITEM")) {
    			NoSlowdown.setNotUsingItem(player);
    			    			
    			if(player.getItemInHand() != null) {    				
    				final Material m = player.getItemInHand().getType();
    				
    				if(m.toString().toUpperCase().contains("SWORD")) {
    					Manager.getPlayerData(player).setNoSlowAntiSword(Manager.getPlayerData(player).getNoSlowAntiSword() + 10);
    				}
    			}    			
    		}
    	}
    	    	
    	if(packet.getClass().getSimpleName().equalsIgnoreCase("PacketPlayInUseEntity")){
    		int id = (Integer)getValue(packet, "a");      
                        
            if(getValue(packet, "action").toString().equalsIgnoreCase("ATTACK")){
            	Manager.getPlayerData(player).setGhostHandPlayersInBreakBlock(false);

            	if(player.getTargetBlock((Set)null, 5).getType() != null 
        				&& player.getTargetBlock((Set)null, 5).getType() != Material.AIR) {
            		Manager.getPlayerData(player).setGhostHandKillauraClickCount(Manager.getPlayerData(player).getGhostHandKillauraClickCount() - 1);
            	}
            	
            	Manager.getPlayerData(player).setKillauraHitSuccessful(Manager.getPlayerData(player).getKillauraHitSuccessful() + 1);
            }
    	}
    	
    	if(packet.getClass().getSimpleName().equalsIgnoreCase("PacketPlayInArmAnimation")) {
    		for(Module m : Main.moduleList) {
    			m.onSwingArm(player);
    		}
    		
    		if(Manager.getPlayerData(player).getLastPacket().equals("PacketPlayInBlockPlace")) {
    			if(player.getTargetBlock((Set)null, 3).getType() != null 
    	    			&& player.getTargetBlock((Set)null, 3).getType() != Material.AIR) {
    				Manager.getPlayerData(player).setGhostHandKillauraClickCount(Manager.getPlayerData(player).getGhostHandKillauraClickCount() - 1);
    	    	}
    		}
        } 
    	
    	if(packet.getClass().getSimpleName().equalsIgnoreCase("PacketPlayInPositionLook")
    			|| packet.getClass().getSimpleName().equalsIgnoreCase("PacketPlayInPosition")
    			|| packet.getClass().getSimpleName().equalsIgnoreCase("PacketPlayInLook")
    			|| packet.getClass().getSimpleName().equalsIgnoreCase("PacketPlayInFlying")) {     
    		if(!Manager.isOP(player)) { 	        		
        		int ping = Manager.ping(player);
        		if(ping < 40) {
        			ping = 0;
        		}       
        		
        		if(player.getFireTicks() != -20 && Manager.getPlayerData(player).getZootTime() == 0) {
        			Manager.getPlayerData(player).setZootTime(System.currentTimeMillis());
        			Manager.getPlayerData(player).setZootTicks(player.getFireTicks());
        		}
        		
        		if(player.getFireTicks() > Manager.getPlayerData(player).getZootTicks())  {
        			Manager.getPlayerData(player).setZootTime(System.currentTimeMillis());
        			Manager.getPlayerData(player).setZootTicks(player.getFireTicks());
        		}
        		        		
        		if(!BlockChecks.NotInLiquid(player) || player.getGameMode() == GameMode.CREATIVE || player.getGameMode() == GameMode.SPECTATOR) {
        			Manager.getPlayerData(player).setZootTime(0);
    				Manager.getPlayerData(player).setZootTicks(0);
        		}
        		
        		if(Manager.getPlayerData(player).getZootTime() != 0) {
        			double value = (Manager.getPlayerData(player).getZootTicks() - player.getFireTicks()) - (System.currentTimeMillis() - Manager.getPlayerData(player).getZootTime()) / 50;
        			
        			if(player.getFireTicks() == -20) {
        				value += player.getFireTicks();
        			}
        			        			
        			if(value > ConfigManager.getCheckConfigValue(Check.z1, "maxValue")) {
        				if(Manager.debug(player, Check.z1, "Value: " + value)) {
        					player.setFireTicks((int) (Manager.getPlayerData(player).getZootTicks() - (System.currentTimeMillis() - Manager.getPlayerData(player).getZootTime()) / 50));
        				}
        			}
        			
        			if(player.getFireTicks() == -20) {
            			Manager.getPlayerData(player).setZootTime(0);
        				Manager.getPlayerData(player).setZootTicks(0);
            		}
        		}
        		        		
        		final double newValue = Manager.getPlayerData(player).getPacketAntiMoreMovePacketCount() + 1;
        		        		
        		if(System.currentTimeMillis() - Main.LastTimeReset > 250) {
        			Manager.getPlayerData(player).setPacketAntiMoreMovePacketCount(newValue);
        		}        		
        	}
    		    		    		
    		Manager.getPlayerData(player).setPacketsLagCount(Manager.getPlayerData(player).getPacketsLagCount() - 1);
    		
    		if(Manager.getPlayerData(player).getPacketsLagCount() < 0) {
    			Manager.getPlayerData(player).setPacketsLagCount(0);
    		}
    		
    		Manager.getPlayerData(player).setLastMovePacketTime(System.nanoTime());
        } else {            	
        	if(Manager.getPlayerData(player).getLastPacket() != null && !Manager.isOP(player) && !player.isDead() && !player.isInsideVehicle()) {    
	        	if((System.nanoTime() - Manager.getPlayerData(player).getLastMovePacketTime()) / 1000000 > 250 + Manager.ping(player) + Main.getServerLag()) {
	        		Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), new Runnable() {						
						@Override
						public void run() {
							player.teleport(player.getLocation());
						}
					});
	        		
	        		Manager.getPlayerData(player).setPacketsLagCount(2);
	        	}
	        }
        } 	
    	    	
    	Manager.getPlayerData(player).setLastPacket(packet);
    }
         
    private void readOutPackets(final Object packet) {
    	if(packet.getClass().getSimpleName().equalsIgnoreCase("PacketPlayOutHeldItemSlot")) {    	    		
    		if(Manager.getPlayerData(player).getPingFirstChangeSlot() == -1 
    				&& Manager.getPlayerData(player).getPacketPreviousItemHeld() != player.getInventory().getHeldItemSlot()) {
    			Manager.getPlayerData(player).setPingFirstChangeSlot(System.nanoTime());
    		}
    	}
    	
    	if(packet.getClass().getSimpleName().equalsIgnoreCase("PacketPlayOutPosition")) {    		
    		if(Manager.getPlayerData(player).getPingTeleportTime() == -1) {    			
    			Manager.getPlayerData(player).setPingTeleportTime(System.nanoTime());    			
    		}    		
    	}
    	
    	if(packet.getClass().getSimpleName().equalsIgnoreCase("PacketPlayOutEntityVelocity")) {
    		if(Integer.valueOf(getValue(packet, "a").toString()) == player.getEntityId()) {
    			double x = Double.valueOf(getValue(packet, "b").toString()) / 8000;
    			double y = Double.valueOf(getValue(packet, "c").toString()) / 8000;
    			double z = Double.valueOf(getValue(packet, "d").toString()) / 8000;
    	    	
    	    	Manager.getPlayerData(player).setSpeed1AfterJump(Double.NEGATIVE_INFINITY);
    	    	Manager.getPlayerData(player).setSpeed2OldSpeed(Double.POSITIVE_INFINITY);
    	    	Manager.getPlayerData(player).setStrafeValueX(0);
    			Manager.getPlayerData(player).setStrafeValueZ(0);	
    	    	
    			boolean isLegal = true;
    			
    			if(Fly.inBlock(player.getLocation().add(0, y, 0))) {
    				isLegal = false;
    			}
    			
    			if(Fly.inBlock(player.getLocation().add(x, 0, 0))) {
    				isLegal = false;
    			}
    			
    			if(Fly.inBlock(player.getLocation().add(0, 0, z))) {
    				isLegal = false;
    			}
    			
    			if(Fly.inBlock(player.getLocation().add(x, y, z))) {
    				isLegal = false;
    			}
    			
    			if(Fly.inBlock(player.getLocation().add(x, 0, z))) {
    				isLegal = false;
    			}
    			
    			if(Fly.inBlock(player.getLocation().add(x, y, 0))) {
    				isLegal = false;
    			}
    			
    			if(Fly.inBlock(player.getLocation().add(0, y, z))) {
    				isLegal = false;
    			}
    			
    			if(y < 0 && GroundUtils.isPlayerPositionOnGround(player.getLocation())) {
    				isLegal = false;
    			}
    			
    	    	if(isLegal) {
    	    		while(Manager.getPlayerData(player).getFlyVelocityLog().containsKey(System.currentTimeMillis())) {
        	    		Manager.getPlayerData(player).getFlyVelocityLog().remove(System.currentTimeMillis());
        	    	}
        	    	while(Manager.getPlayerData(player).getFlyVelocityLocation().containsKey(System.currentTimeMillis())) {
        	    		Manager.getPlayerData(player).getFlyVelocityLocation().remove(System.currentTimeMillis());
        	    	}
        	    	
        	    	Manager.getPlayerData(player).getFlyVelocityLog().put(System.currentTimeMillis(), new Vector(x, y, z));
        	    	Manager.getPlayerData(player).getFlyVelocityLocation().put(System.currentTimeMillis(), player.getLocation());    
        	    	
        	    	while(Manager.getPlayerData(player).getMovementVelocityLog().containsKey(System.currentTimeMillis())) {
        	    		Manager.getPlayerData(player).getMovementVelocityLog().remove(System.currentTimeMillis());
        	    	}
        	    	while(Manager.getPlayerData(player).getMovementVelocityLocation().containsKey(System.currentTimeMillis())) {
        	    		Manager.getPlayerData(player).getMovementVelocityLocation().remove(System.currentTimeMillis());
        	    	}
        	    	
        	    	Manager.getPlayerData(player).getMovementVelocityLog().put(System.currentTimeMillis(), new Vector(x, y, z));
        	    	Manager.getPlayerData(player).getMovementVelocityLocation().put(System.currentTimeMillis(), player.getLocation());   
    	    	}
    		}    		
    	}
    }
    
    public void setValue(final Object obj, final String name, final Object value){
        try {
        	Field field = obj.getClass().getDeclaredField(name);
        	field.setAccessible(true);
        	field.set(obj, value);
        } catch(Exception e) {}
    }

    public static Object getValue(final Object obj, final String name){
        try{
        	Field field = obj.getClass().getDeclaredField(name);
        	field.setAccessible(true);
        	return field.get(obj);
        } catch(Exception e) {
        	e.printStackTrace();
        }
        return null;
    }
}