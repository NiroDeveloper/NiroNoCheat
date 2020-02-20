package de.niroyt.nnc.modules;


import de.niroyt.nnc.Main;
import de.niroyt.nnc.manager.Manager;
import de.niroyt.nnc.manager.Module;
import de.niroyt.nnc.utils.BlockChecks;
import de.niroyt.nnc.utils.ConfigManager;
import de.niroyt.nnc.utils.EntityHider;
import de.niroyt.nnc.utils.Reflections;

import java.lang.reflect.InvocationTargetException;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ESP extends Module {
	                 
    public void onTick() {
    	if(!ConfigManager.getConfigBoolean("Checks.AntiESP.Enabled")) {
    		return;
    	}
    	
    	if(System.currentTimeMillis() % 500 > 50) {
    		return;
    	}
    	
    	for(final Player player : Bukkit.getOnlinePlayers()) {    	
    		if(Manager.isOP(player)) {
    			continue;
    		}
    		    		
    		if(ConfigManager.getConfigBoolean("Checks.AntiESP.Players")) {
    			for(Player p : player.getLocation().getWorld().getPlayers()) {
            		if(p != player) {  
            			boolean canSeePlayer = canSeeEntity(player, p);        			
            			        			
            			if(!canSeePlayer && p.isSneaking()) {
            				if(!Manager.getPlayerData(player).getEspHidden().containsKey(p.getName())) {
            					Manager.getPlayerData(player).getEspHidden().put(p.getName(), System.currentTimeMillis());
            				}
            				
            				if(Manager.getPlayerData(player).getEspHidden().containsKey(p.getName())) {
            					if(System.currentTimeMillis() - Manager.getPlayerData(player).getEspHidden().get(p.getName()) > 1500) {
            						player.hidePlayer(p);
            					}
            				}
            			} else {
            				if(Manager.getPlayerData(player).getEspHidden().containsKey(p.getName())) {
            					if(System.currentTimeMillis() - Manager.getPlayerData(player).getEspHidden().get(p.getName()) > 1500) {
            						player.showPlayer(p);
            					}
            					     
            					Manager.getPlayerData(player).getEspHidden().remove(p.getName());
            				}        				
            			}
            			
            			if(!canSeePlayer) {
            				if(!Manager.getPlayerData(player).getEspHideEquip().containsKey(p.getName())) {
            					Manager.getPlayerData(player).getEspHideEquip().put(p.getName(), System.currentTimeMillis());
            				} else {
            					if(System.currentTimeMillis() - Manager.getPlayerData(player).getEspHideEquip().get(p.getName()) > 1500) {
            						equip(p.getEntityId(), 0, null, player);
                					equip(p.getEntityId(), 1, null, player);
                					equip(p.getEntityId(), 2, null, player);
                					equip(p.getEntityId(), 3, null, player);
                					equip(p.getEntityId(), 4, null, player);
            					}
            				}
            			} else {
            				if(Manager.getPlayerData(player).getEspHideEquip().containsKey(p.getName())) {
            					Manager.getPlayerData(player).getEspHideEquip().remove(p.getName());
            					        					
            					equip(p.getEntityId(), 0, p.getItemInHand(), player);
            					equip(p.getEntityId(), 1, p.getInventory().getBoots(), player);
            					equip(p.getEntityId(), 2, p.getInventory().getLeggings(), player);
            					equip(p.getEntityId(), 3, p.getInventory().getChestplate(), player);
            					equip(p.getEntityId(), 4, p.getInventory().getHelmet(), player);
            				}
            			}
            		}
            	}
    		}
    		
    		if(Main.getInstance().getServer().getPluginManager().getPlugin("ProtocolLib") == null) {
    			continue;
    		}
    		
    		if(ConfigManager.getConfigBoolean("Checks.AntiESP.Entities")) {
	    		for(Entity entity : player.getWorld().getLivingEntities()) {	    		
		    		if(entity instanceof Player) {
		    			continue;
		    		}
		    		
		        	boolean canSeePlayer = canSeeEntity(player, entity);        			
		        			        			
		        	if(!canSeePlayer) {
		        		if(!Manager.getPlayerData(player).getEspHidden().containsKey(String.valueOf(entity.getEntityId()))) {
		        			Manager.getPlayerData(player).getEspHidden().put(String.valueOf(entity.getEntityId()), System.currentTimeMillis());
		        		}
		        				
		        		if(Manager.getPlayerData(player).getEspHidden().containsKey(String.valueOf(entity.getEntityId()))) {
		        			if(System.currentTimeMillis() - Manager.getPlayerData(player).getEspHidden().get(String.valueOf(entity.getEntityId())) > 1500) {
		        				EntityHider.hideEntity(player, entity);
		        			}
		        		}
		        	} else {
		        		if(Manager.getPlayerData(player).getEspHidden().containsKey(String.valueOf(entity.getEntityId()))) {
		        			if(System.currentTimeMillis() - Manager.getPlayerData(player).getEspHidden().get(String.valueOf(entity.getEntityId())) > 1500) {
		        				EntityHider.showEntity(player, entity);
		        			}
		        					     
		        			Manager.getPlayerData(player).getEspHidden().remove(String.valueOf(entity.getEntityId()));
		        		}
		        	}
		    	}
    		}
    		
    		if(ConfigManager.getConfigBoolean("Checks.AntiESP.Items")) {
	    		for(Entity entity : player.getWorld().getEntities()) {	    		
		    		if(!entity.getClass().getSimpleName().equalsIgnoreCase("CraftItem")) {
		    			continue;
		    		}
		    				    		
		        	boolean canSeePlayer = canSeeEntity(player, entity);        			
		        			        			
		        	if(!canSeePlayer) {
		        		if(!Manager.getPlayerData(player).getEspHidden().containsKey(String.valueOf(entity.getEntityId()))) {
		        			Manager.getPlayerData(player).getEspHidden().put(String.valueOf(entity.getEntityId()), System.currentTimeMillis());
		        		}
		        				
		        		if(Manager.getPlayerData(player).getEspHidden().containsKey(String.valueOf(entity.getEntityId()))) {
		        			if(System.currentTimeMillis() - Manager.getPlayerData(player).getEspHidden().get(String.valueOf(entity.getEntityId())) > 1500) {
		        				EntityHider.hideEntity(player, entity);
		        			}
		        		}
		        	} else {
		        		if(Manager.getPlayerData(player).getEspHidden().containsKey(String.valueOf(entity.getEntityId()))) {
		        			if(System.currentTimeMillis() - Manager.getPlayerData(player).getEspHidden().get(String.valueOf(entity.getEntityId())) > 1500) {
		        				EntityHider.showEntity(player, entity);
		        			}
		        					     
		        			Manager.getPlayerData(player).getEspHidden().remove(String.valueOf(entity.getEntityId()));
		        		}
		        	}
		    	}
    		}
    	}
    }  
    
    public void equip(int id, int slot, ItemStack itemStack, Player player) {
    	Object packet = null;
		try {
			packet = ClassLoader.getSystemClassLoader()
				.loadClass("net.minecraft.server." + Main.MinecraftVersion + ".PacketPlayOutEntityEquipment").newInstance();
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | IllegalArgumentException | SecurityException e1) {
			e1.printStackTrace();
		}
    	
		Reflections.setValue(packet, "a", id);
		Reflections.setValue(packet, "b", slot);
		try {
			Reflections.setValue(packet, "c", 
					ClassLoader.getSystemClassLoader()
					.loadClass("org.bukkit.craftbukkit." + Main.MinecraftVersion + ".inventory.CraftItemStack")
					.getMethod("asNMSCopy", ItemStack.class)
					.invoke((Object)null, itemStack));
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
				| SecurityException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		Reflections.sendPacket(packet, player);
	}
        
    public boolean canSeeEntity(Player player, Entity p) {
    	boolean canSeePlayerPos1 = true; 
    	boolean canSeePlayerPos2 = true; 
    	boolean canSeePlayerPos3 = true; 
    	
    	double distance = player.getEyeLocation().distance(p.getLocation());
		double count = distance;
		while(count > 0) {
			double x = player.getEyeLocation().getX() - p.getLocation().getX();
			double y = player.getEyeLocation().getY() - p.getLocation().getY(); 
			double z = player.getEyeLocation().getZ() - p.getLocation().getZ();
			
			Location loc = new Location(player.getWorld(), 
					player.getEyeLocation().getX() - (x / distance) * count, 
					player.getEyeLocation().getY() - (y / distance) * count,
					player.getEyeLocation().getZ() - (z / distance) * count);
							
			if(BlockChecks.isFullBlock(loc.getBlock().getType()) && BlockChecks.isMassiv(loc)
					 && BlockChecks.isFullBlock(loc.getBlock().getRelative(0, 1, 0).getType()) && BlockChecks.isMassiv(loc.add(0, 1, 0))
					&& loc.distance(player.getEyeLocation()) > 2
					&& loc.distance(p.getLocation()) > 2) {
				canSeePlayerPos1 = false;
			}
			
			count--;
		}
		
		if(!canSeePlayerPos1) {
			Location PlayerPos = player.getLocation().clone();
			PlayerPos.setY(player.getEyeLocation().getY());
			if(PlayerPos.getDirection().multiply(2).toLocation(player.getWorld()).getBlock().getType() != Material.AIR) {
				PlayerPos = PlayerPos.getDirection().multiply(1).toLocation(player.getWorld());
			} else if(PlayerPos.getDirection().multiply(3).toLocation(player.getWorld()).getBlock().getType() != Material.AIR) {
				PlayerPos = PlayerPos.getDirection().multiply(2).toLocation(player.getWorld());
			} else if(PlayerPos.getDirection().multiply(4).toLocation(player.getWorld()).getBlock().getType() != Material.AIR) {
				PlayerPos = PlayerPos.getDirection().multiply(3).toLocation(player.getWorld());
			} else if(PlayerPos.getDirection().multiply(5).toLocation(player.getWorld()).getBlock().getType() != Material.AIR) {
				PlayerPos = PlayerPos.getDirection().multiply(4).toLocation(player.getWorld());
			} else {
				PlayerPos = PlayerPos.getDirection().multiply(5).toLocation(player.getWorld());
			}
			
			PlayerPos = player.getEyeLocation().add(PlayerPos);
									
			count = distance;
			while(count > 0) {
				double x = PlayerPos.getX() - p.getLocation().getX();
				double y = PlayerPos.getY() - p.getLocation().getY(); 
				double z = PlayerPos.getZ() - p.getLocation().getZ();
				
				Location loc = new Location(player.getWorld(), 
						PlayerPos.getX() - (x / distance) * count, 
						PlayerPos.getY() - (y / distance) * count,
						PlayerPos.getZ() - (z / distance) * count);
								
				if(BlockChecks.isFullBlock(loc.getBlock().getType()) && BlockChecks.isMassiv(loc)
						 && BlockChecks.isFullBlock(loc.getBlock().getRelative(0, 1, 0).getType()) && BlockChecks.isMassiv(loc.add(0, 1, 0))
						&& loc.distance(p.getLocation()) > 2) {
					canSeePlayerPos2 = false;
				}
				
				count--;
			}
		}
		
		if(!canSeePlayerPos2) {
			Location PlayerPos = player.getLocation().clone();
			PlayerPos.setY(player.getEyeLocation().getY());
			if(PlayerPos.getDirection().multiply(-2).toLocation(player.getWorld()).getBlock().getType() != Material.AIR) {
				PlayerPos = PlayerPos.getDirection().multiply(-1).toLocation(player.getWorld());
			} else if(PlayerPos.getDirection().multiply(-3).toLocation(player.getWorld()).getBlock().getType() != Material.AIR) {
				PlayerPos = PlayerPos.getDirection().multiply(-2).toLocation(player.getWorld());
			} else if(PlayerPos.getDirection().multiply(-4).toLocation(player.getWorld()).getBlock().getType() != Material.AIR) {
				PlayerPos = PlayerPos.getDirection().multiply(-3).toLocation(player.getWorld());
			} else if(PlayerPos.getDirection().multiply(-5).toLocation(player.getWorld()).getBlock().getType() != Material.AIR) {
				PlayerPos = PlayerPos.getDirection().multiply(-4).toLocation(player.getWorld());
			} else {
				PlayerPos = PlayerPos.getDirection().multiply(-5).toLocation(player.getWorld());
			}
			
			PlayerPos = player.getEyeLocation().add(PlayerPos);
			
			count = distance;
			while(count > 0) {
				double x = PlayerPos.getX() - p.getLocation().getX();
				double y = PlayerPos.getY() - p.getLocation().getY(); 
				double z = PlayerPos.getZ() - p.getLocation().getZ();
				
				Location loc = new Location(player.getWorld(), 
						PlayerPos.getX() - (x / distance) * count, 
						PlayerPos.getY() - (y / distance) * count,
						PlayerPos.getZ() - (z / distance) * count);
								
				if(BlockChecks.isFullBlock(loc.getBlock().getType()) && BlockChecks.isMassiv(loc)
						 && BlockChecks.isFullBlock(loc.getBlock().getRelative(0, 1, 0).getType()) && BlockChecks.isMassiv(loc.add(0, 1, 0))
						&& loc.distance(p.getLocation()) > 2) {
					canSeePlayerPos3 = false;
				}
				
				count--;
			}
		}
		
		return canSeePlayerPos1 || canSeePlayerPos2 || canSeePlayerPos3;
    }
}
