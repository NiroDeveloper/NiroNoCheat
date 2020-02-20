package de.niroyt.nnc.utils;


import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;

import de.niroyt.nnc.enums.BlockHeight;
import de.niroyt.nnc.manager.Manager;

public class BlockChecks {

	public static boolean inWeb(final Player player) {
		final Location loc = player.getLocation();
		if(inBlock(loc, Material.WEB)) {
			return true;
		}
		loc.setY(loc.getY() + 1);
		return inBlock(loc, Material.WEB);
	}
	
	public static boolean inBlock(final Location loc, final Material m) {
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
    	
    	Location toCheckLoc1 = new Location(loc.getWorld(), loc.getX(), loc.getY(), loc.getZ() + onOtherBlockZ);
    	Location toCheckLoc2 = new Location(loc.getWorld(), loc.getX() + onOtherBlockX, loc.getY(), loc.getZ() + onOtherBlockZ);
    	Location toCheckLoc3 = new Location(loc.getWorld(), loc.getX() + onOtherBlockX, loc.getY(), loc.getZ());
    	    	
    	if(loc.getBlock().getType() == m) {
    		return true;
    	} 
    	if(loc.getBlock().getRelative(0, 1, 0).getType() == m && loc.getY() % 1 != 0) {
    		return true;
    	}   
    	
    	if(toCheckLoc1.getBlock().getRelative(0, 1, 0).getType() == m && loc.getY() % 1 != 0) {
    		return true;
    	}    	
    	if(toCheckLoc2.getBlock().getRelative(0, 1, 0).getType() == m && loc.getY() % 1 != 0) {
    		return true;
    	}    	
    	if(toCheckLoc3.getBlock().getRelative(0, 1, 0).getType() == m && loc.getY() % 1 != 0) {
    		return true;
    	}
    	
    	if(toCheckLoc1.getBlock().getType() == m) {
    		return true;
    	}    	
    	if(toCheckLoc2.getBlock().getType() == m) {
    		return true;
    	}    	
    	if(toCheckLoc3.getBlock().getType() == m) {
    		return true;
    	}    	
    	return false;
    }
		
	public static boolean BlockOverPlayer(Location loc) {
		int onOtherBlockX = 0;
    	int onOtherBlockZ = 0;
    	
    	loc = loc.clone().add(0, 2, 0);
    	
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
    	
    	Location toCheckLoc1 = new Location(loc.getWorld(), loc.getX(), loc.getY(), loc.getZ() + onOtherBlockZ);
    	Location toCheckLoc2 = new Location(loc.getWorld(), loc.getX() + onOtherBlockX, loc.getY(), loc.getZ() + onOtherBlockZ);
    	Location toCheckLoc3 = new Location(loc.getWorld(), loc.getX() + onOtherBlockX, loc.getY(), loc.getZ());
    	    	
    	if(loc.getBlock().getType() != Material.AIR) {
    		return true;
    	}     	
    	if(toCheckLoc1.getBlock().getType() != Material.AIR) {
    		return true;
    	}    	
    	if(toCheckLoc2.getBlock().getType() != Material.AIR) {
    		return true;
    	}    	
    	if(toCheckLoc3.getBlock().getType() != Material.AIR) {
    		return true;
    	}    	
    	return false;
    }
	
	public static boolean canCheckFly(final Player player) {
		if(inBlock(player.getLocation(), Material.LADDER) || inBlock(player.getLocation(), Material.VINE)) {
			return false;
		}
		
        int x = player.getLocation().getBlockX() - 1;
        double y = player.getLocation().getY() + 0.1 - 1;
        int z = player.getLocation().getBlockZ() - 1;
                
        while(x <= player.getLocation().getBlockX() + 1) {
        	while(y <= player.getLocation().getY() + 2.5) {
        		while(z <= player.getLocation().getBlockZ() + 1) {                	
        			Location BlockPos = new Location(player.getWorld(), x, y, z);
        			
        			if(BlockPos.getBlock().getType() == Material.WATER_LILY) {            				
        				return false;
        			}
        			if((BlockPos.getBlock().getType() == Material.WATER_LILY
        	        	|| BlockPos.getBlock().getType() == Material.WATER
        	            || BlockPos.getBlock().getType() == Material.STATIONARY_WATER
        	            || BlockPos.getBlock().getType() == Material.LAVA
        	            || BlockPos.getBlock().getType() == Material.STATIONARY_LAVA
        	            || BlockPos.getBlock().getType() == Material.TRAP_DOOR
        	            || BlockPos.getBlock().getType() == Material.IRON_TRAPDOOR) && y >= player.getLocation().getY() + 0.4) {
        				
        				return false;
        			}
                	z++;
                }
        		z = player.getLocation().getBlockZ() - 1;
            	y++;
            }
        	y = player.getLocation().getY() + 0.4 - 1;
        	x++;
        }
        
        return true;        	
    }
		    
    public static boolean isMassiv(final Location loc) {
    	final Material b = loc.getBlock().getType();
    	    	
    	if(b.isSolid()) {
    		return true;
    	} 
    	
    	return false;
    }
    
    public static boolean inAir(final Player player) {
    	int x = player.getLocation().getBlockX() - 1;
        double y = player.getLocation().getY() + (Manager.getPlayerData(player).getFlyLastPosDiff() > 0 ? -1 : 0);
        int z = player.getLocation().getBlockZ() - 1;
                
        while(x <= player.getLocation().getBlockX() + 1) {
        	while(y <= player.getLocation().getBlockY() + 1) {
        		while(z <= player.getLocation().getBlockZ() + 1) {                	
        			Location BlockPos = new Location(player.getWorld(), x, y, z);
        			
        			if(canStandOnBlock(BlockPos.getBlock().getType())) {   
        				return false;
        			}
                	z++;
                }
        		z = player.getLocation().getBlockZ() - 1;
            	y++;
            }
        	y = player.getLocation().getY() + (Manager.getPlayerData(player).getFlyLastPosDiff() > 0 ? -1 : 0);
        	x++;
        }
        
        return true;   
    }
    
    public static boolean isStep(final Location loc) {
    	Material m = loc.getWorld().getBlockAt(loc).getType();
    	
    	if(m == Material.ANVIL) {
    		return true;
    	}
    	if(m == Material.SANDSTONE_STAIRS) {
    		return true;
    	}
    	if(m == Material.ACACIA_STAIRS) {
    		return true;
    	}
    	if(m == Material.BIRCH_WOOD_STAIRS) {
    		return true;
    	}
    	if(m == Material.BRICK_STAIRS) {
    		return true;
    	}
    	if(m == Material.COBBLESTONE_STAIRS) {
    		return true;
    	}
    	if(m == Material.DARK_OAK_STAIRS) {
    		return true;
    	}
    	if(m == Material.JUNGLE_WOOD_STAIRS) {
    		return true;
    	}
    	if(m == Material.NETHER_BRICK_STAIRS) {
    		return true;
    	}
    	if(m == Material.QUARTZ_STAIRS) {
    		return true;
    	}
    	if(m == Material.RED_SANDSTONE_STAIRS) {
    		return true;
    	}
    	if(m == Material.SPRUCE_WOOD_STAIRS) {
    		return true;
    	}
    	if(m == Material.WOOD_STAIRS) {
    		return true;
    	}
    	if(m == Material.SMOOTH_STAIRS) {
    		return true;
    	}
    	if(m == Material.SANDSTONE_STAIRS) {
    		return true;
    	}
    	if(m == Material.ACACIA_STAIRS) {
    		return true;
    	}
    	if(m == Material.BIRCH_WOOD_STAIRS) {
    		return true;
    	}
    	if(m == Material.BRICK_STAIRS) {
    		return true;
    	}
    	if(m == Material.COBBLESTONE_STAIRS) {
    		return true;
    	}
    	if(m == Material.DARK_OAK_STAIRS) {
    		return true;
    	}
    	if(m == Material.JUNGLE_WOOD_STAIRS) {
    		return true;
    	}
    	if(m == Material.NETHER_BRICK_STAIRS) {
    		return true;
    	}
    	if(m == Material.QUARTZ_STAIRS) {
    		return true;
    	}
    	if(m == Material.RED_SANDSTONE_STAIRS) {
    		return true;
    	}
    	if(m == Material.SPRUCE_WOOD_STAIRS) {
    		return true;
    	}
    	if(m == Material.WOOD_STAIRS) {
    		return true;
    	}
    	if(m == Material.SMOOTH_STAIRS) {
    		return true;
    	}
    	if(m == Material.STONE_SLAB2) {
    		return true;
    	}
    	if(m == Material.WOOD_STEP) {
    		return true;
    	}
    	if(m == Material.STEP) {
    		return true;
    	}
    	if(m == Material.CARPET) {
    		return true;
    	}
    	if(m == Material.SNOW) {
    		return true;
    	}
    	    	
    	return false;
    }
    
    public static boolean onSteps(final Player player) {
    	return onSteps(player.getLocation());
    }
    
    public static boolean onSteps(final Location loc) {
    	int y = loc.getBlockY();
    	
    	World w = loc.getWorld();
    	int x = loc.getBlockX();
    	int z = loc.getBlockZ();
    	
    	while(y > loc.getBlockY() - 2) {
    		if(isStep(new Location(w, x, y, z))) {
    			return true;
    		}
    		if(isStep(new Location(w, x + 1, y, z))) {
    			return true;
    		}
    		if(isStep(new Location(w, x + 1, y, z - 1))) {
    			return true;
    		}
    		if(isStep(new Location(w, x + 1, y, z + 1))) {
    			return true;
    		}
    		if(isStep(new Location(w, x, y, z + 1))) {
    			return true;
    		}
    		if(isStep(new Location(w, x, y, z - 1))) {
    			return true;
    		}
    		if(isStep(new Location(w, x - 1, y, z - 1))) {
    			return true;
    		}
    		if(isStep(new Location(w, x - 1, y, z))) {
    			return true;
    		}
    		if(isStep(new Location(w, x - 1, y, z + 1))) {
    			return true;
    		}
    		
    		y--;
    	}
    	
    	return false;
    }
    
    public static boolean isFullBlock(final Material m) {
    	if(m == Material.STONE) return true;
    	if(m == Material.GRASS) return true;
    	if(m == Material.DIRT) return true;
    	if(m == Material.COBBLESTONE) return true;
    	if(m == Material.WOOD) return true;
    	if(m == Material.BEDROCK) return true;
    	if(m == Material.SAND) return true;
    	if(m == Material.SANDSTONE) return true;
    	if(m == Material.GRAVEL) return true;
    	if(m == Material.COAL_ORE) return true;
    	if(m == Material.DIAMOND_ORE) return true;
    	if(m == Material.EMERALD_ORE) return true;
    	if(m == Material.GOLD_ORE) return true;
    	if(m == Material.IRON_ORE) return true;
    	if(m == Material.LAPIS_ORE) return true;
    	if(m == Material.QUARTZ_ORE) return true;
    	if(m == Material.REDSTONE_ORE) return true;
    	if(m == Material.LOG) return true;
    	if(m == Material.LOG_2) return true;
    	if(m == Material.LEAVES) return true;
    	if(m == Material.LEAVES_2) return true;
    	if(m == Material.SPONGE) return true;
    	if(m == Material.GLASS) return true;
    	if(m == Material.LAPIS_BLOCK) return true;
    	if(m == Material.DISPENSER) return true;
    	if(m == Material.NOTE_BLOCK) return true;
    	if(m == Material.WOOL) return true;
    	if(m == Material.GOLD_BLOCK) return true;
    	if(m == Material.IRON_BLOCK) return true;
    	if(m == Material.WOOD_DOUBLE_STEP) return true;
    	if(m == Material.DOUBLE_STONE_SLAB2) return true;
    	if(m == Material.DOUBLE_STEP) return true;
    	if(m == Material.BRICK) return true;
    	if(m == Material.TNT) return true;
    	if(m == Material.BOOKSHELF) return true;
    	if(m == Material.OBSIDIAN) return true;
    	if(m == Material.MOSSY_COBBLESTONE) return true;
    	if(m == Material.DIAMOND_BLOCK) return true;
    	if(m == Material.SNOW) return true;
    	if(m == Material.GLOWSTONE) return true;
    	if(m == Material.ENDER_STONE) return true;
    	if(m == Material.CLAY) return true;
    	if(m == Material.HARD_CLAY) return true;
    	if(m == Material.STAINED_CLAY) return true;
    	if(m == Material.PRISMARINE) return true;
    	if(m == Material.HAY_BLOCK) return true;
    	if(m == Material.MELON_BLOCK) return true;
    	if(m == Material.PUMPKIN) return true;
		
		return false;
    }
        
    public static boolean NotInLiquid(final Player player) {
    	int x = player.getLocation().getBlockX() - 1;
        int y = player.getLocation().getBlockY() - 1;
        int z = player.getLocation().getBlockZ() - 1;
                
        while(x <= player.getLocation().getBlockX() + 1) {
        	while(y <= player.getLocation().getBlockY() + 1) {
        		while(z <= player.getLocation().getBlockZ() + 1) {                	
        			Location BlockPos = new Location(player.getWorld(), x, y, z);
        			
        			if(BlockPos.getBlock().getType() == Material.WATER
        					|| BlockPos.getBlock().getType() == Material.STATIONARY_WATER
        					|| BlockPos.getBlock().getType() == Material.LAVA
        					|| BlockPos.getBlock().getType() == Material.STATIONARY_LAVA) {        				
        				return false;
        			}
                	z++;
                }
        		z = player.getLocation().getBlockZ() - 1;
            	y++;
            }
        	y = player.getLocation().getBlockY() - 1;
        	x++;
        }
        
        return true;   
    }
    
    public static boolean InLiquid(final Player player) {
    	int x = player.getLocation().getBlockX() - 1;
        double y = player.getLocation().getY() + 0.1 - 1;
        int z = player.getLocation().getBlockZ() - 1;
                
        while(x <= player.getLocation().getBlockX() + 1) {
        	while(y <= player.getLocation().getY() + 2) {
        		while(z <= player.getLocation().getBlockZ() + 1) {                	
        			Location BlockPos = new Location(player.getWorld(), x, y, z);
        			
        			if((BlockPos.getBlock().getType() == Material.WATER
        	            || BlockPos.getBlock().getType() == Material.STATIONARY_WATER
        	            || BlockPos.getBlock().getType() == Material.LAVA
        	            || BlockPos.getBlock().getType() == Material.STATIONARY_LAVA) && y >= player.getLocation().getY() + 0.4) {
        				
        				return true;
        			}
                	z++;
                }
        		z = player.getLocation().getBlockZ() - 1;
            	y++;
            }
        	y = player.getLocation().getY() + 0.4 - 1;
        	x++;
        }
        
        return false;   
    }
    
    public static boolean canStandOnBlock(final Material m) {    	
    	if (m == Material.AIR) 
			return false;
    	if (m == Material.FIRE)
			return false;
    	if (m == Material.LEVER)
			return false;
    	if (m == Material.REDSTONE_WIRE)
			return false;
		if (m == Material.SIGN_POST)
			return false;
		if (m == Material.SIGN)
			return false;
		if (m == Material.WATER)
			return false;
		if (m == Material.STATIONARY_WATER)
			return false;
		if (m == Material.LAVA)
			return false;
		if (m == Material.STATIONARY_LAVA)
			return false;
		if (m == Material.DOUBLE_PLANT)
			return false;
		if (m == Material.LONG_GRASS)
			return false;
		if (m == Material.YELLOW_FLOWER)
			return false;
		if (m == Material.RED_ROSE)
			return false;
		if (m == Material.DEAD_BUSH)
			return false;
		if (m == Material.WEB)
			return false;
		if (m == Material.STRING)
			return false;
		if (m == Material.TORCH)
			return false;
		if (m == Material.REDSTONE_TORCH_OFF)
			return false;
		if (m == Material.REDSTONE_TORCH_ON)
			return false;
		if (m == Material.STONE_BUTTON)
			return false;
		if (m == Material.WOOD_BUTTON)
			return false;
		if (m == Material.WALL_SIGN)
			return false;
		if(m == Material.SEEDS) 
			return false;
		if(m == Material.MELON_SEEDS) 
			return false;
		if(m == Material.PUMPKIN_SEEDS) 
			return false;
		if(m == Material.CROPS)
			return false;
		return true;
    }
    
    public static boolean onIce(final Location location) {
    	Location loc = new Location(location.getWorld(), location.getX(), location.getY() - 1, location.getZ());
    	
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
    	
    	Location toCheckLoc1 = new Location(loc.getWorld(), loc.getX(), loc.getY(), loc.getZ() + onOtherBlockZ);
    	Location toCheckLoc2 = new Location(loc.getWorld(), loc.getX() + onOtherBlockX, loc.getY(), loc.getZ() + onOtherBlockZ);
    	Location toCheckLoc3 = new Location(loc.getWorld(), loc.getX() + onOtherBlockX, loc.getY(), loc.getZ());
    	    	
    	if(loc.getBlock().getType() == Material.ICE) {
    		return true;
    	}     	
    	if(toCheckLoc1.getBlock().getType() == Material.ICE) {
    		return true;
    	}    	
    	if(toCheckLoc2.getBlock().getType() == Material.ICE) {
    		return true;
    	}    	
    	if(toCheckLoc3.getBlock().getType() == Material.ICE) {
    		return true;
    	}
    	if(loc.getBlock().getType() == Material.PACKED_ICE) {
    		return true;
    	}     	
    	if(toCheckLoc1.getBlock().getType() == Material.PACKED_ICE) {
    		return true;
    	}    	
    	if(toCheckLoc2.getBlock().getType() == Material.PACKED_ICE) {
    		return true;
    	}    	
    	if(toCheckLoc3.getBlock().getType() == Material.PACKED_ICE) {
    		return true;
    	}   	
    	return false;
    }
    
    public static boolean inLadder(final Location location) {
        double x = location.getX();
        double y = location.getY();
        double z = location.getZ();
        
        final Location BlockOver1 = new Location(location.getWorld(), x, y, z);
        final Location BlockOver2 = new Location(location.getWorld(), x, y + 0.2, z);
        
        if(BlockOver1.getBlock().getType() == Material.LADDER || BlockOver1.getBlock().getType() == Material.VINE
        		|| BlockOver2.getBlock().getType() == Material.LADDER || BlockOver2.getBlock().getType() == Material.VINE) {
        	
        	return true;
        } else {
        	return false;
        }
    }
    
    public static boolean isOnWater(Location location) {
    	location = location.clone();
    	location.setY(location.getY() + 0.5);
    	
    	HashMap<Integer, Material> locations = new HashMap<Integer, Material>();
    	
        final Location l = location.getBlock().getLocation();
        final Location l2 = location.getBlock().getRelative(1, 0, 0).getLocation();
        final Location l3 = location.getBlock().getRelative(-1, 0, 0).getLocation();
        final Location l4 = location.getBlock().getRelative(0, 0, 1).getLocation();
        final Location l5 = location.getBlock().getRelative(0, 0, -1).getLocation();
        final Location l6 = location.getBlock().getRelative(1, 0, 1).getLocation();
        final Location l7 = location.getBlock().getRelative(-1, 0, -1).getLocation();
        final Location l8 = location.getBlock().getRelative(-1, 0, 1).getLocation();
        final Location l9 = location.getBlock().getRelative(1, 0, -1).getLocation();
        locations.clear();
        locations.put(0, l.getBlock().getType());
        locations.put(1, l2.getBlock().getType());
        locations.put(2, l3.getBlock().getType());
        locations.put(3, l4.getBlock().getType());
        locations.put(4, l5.getBlock().getType());
        locations.put(5, l6.getBlock().getType());
        locations.put(6, l7.getBlock().getType());
        locations.put(7, l8.getBlock().getType());
        locations.put(8, l9.getBlock().getType());
        int j = 0;
        for (final int i : locations.keySet()) {
            if (locations.get(i) == Material.WATER || locations.get(i) == Material.STATIONARY_WATER || locations.get(i) == Material.LAVA || locations.get(i) == Material.STATIONARY_LAVA) {
                ++j;
            }
        }
        return j >= 6;
    }
    
    public static boolean onBlock(final Location loc) {
		if(loc.getX() % 1.0 >= 0.690 && loc.getX() % 1.0 <= 0.708) {
    		return true;
    	}
    	
    	if(loc.getX() % 1.0 >= 0.292 && loc.getX() % 1.0 <= 0.310) {
    		return true;
    	}
    	
    	if(loc.getX() % 1.0 <= -0.690 && loc.getX() % 1.0 >= -0.708) {
    		return true;
    	}
    	
    	if(loc.getX() % 1.0 <= -0.292 && loc.getX() % 1.0 >= -0.310) {
    		return true;
    	}
    	
    	if(loc.getZ() % 1.0 >= 0.690 && loc.getZ() % 1.0 <= 0.708) {
    		return true;
    	}
    	
    	if(loc.getZ() % 1.0 >= 0.292 && loc.getZ() % 1.0 <= 0.310) {
    		return true;
    	}
    	
    	if(loc.getZ() % 1.0 <= -0.690 && loc.getZ() % 1.0 >= -0.708) {
    		return true;
    	}
    	
    	if(loc.getZ() % 1.0 <= -0.292 && loc.getZ() % 1.0 >= -0.310) {
    		return true;
    	}
    	
    	return false;
	}
    
    public static boolean isSaveOnGround(final Location loc) {	
    	if(loc.getY() % 1 == 0 || BlockChecks.inBlock(loc.clone().add(0, -1, 0), Material.SLIME_BLOCK)) {
    		final Location l = loc.clone().add(0, -1, 0);
    		
    		if(l.getBlock().getRelative(1, 0, 0).getType().isSolid() || 
	    		l.getBlock().getRelative(1, 0, 1).getType().isSolid() || 
	    		l.getBlock().getRelative(1, 0, -1).getType().isSolid() ||
	    		l.getBlock().getRelative(-1, 0, 0).getType().isSolid() ||
	    		l.getBlock().getRelative(-1, 0, 1).getType().isSolid() ||
	    		l.getBlock().getRelative(-1, 0, -1).getType().isSolid() ||
	    		l.getBlock().getRelative(0, 0, 0).getType().isSolid() || 
	    		l.getBlock().getRelative(0, 0, 1).getType().isSolid() ||
	    		l.getBlock().getRelative(0, 0, -1).getType().isSolid()) {
    			return true;
    		}
    	}
    	
    	if(loc.getY() % 0.5 == 0 && BlockChecks.onSteps(loc)) {
    		return true;
    	}
    	
    	if(BlockChecks.inBlock(loc, Material.SNOW) && loc.getY() % 0.125 == 0) {
    		return true;
    	}
    	    	
    	for(final BlockHeight h : BlockHeight.values()) {
    		if(loc.getY() % 1 != h.getHeight() && loc.getY() % 2 != h.getHeight() && (loc.getY() + 1) % 2 != h.getHeight()) {
    			continue;
    		}
    		
    		Location l = loc.clone();
    		
    		if(h.getHeight() >= 1) {
    			l.add(0, -1, 0);
    		}
    		
    		if(l.getBlock().getType().isSolid()) {
    			return true;
    		}
    		
    		if(l.getBlock().getRelative(1, 0, 0).getType().isSolid()) {
    			return true;
    		}
    		if(l.getBlock().getRelative(1, 0, 1).getType().isSolid()) {
    			return true;
    		}
    		if(l.getBlock().getRelative(1, 0, -1).getType().isSolid()) {
    			return true;
    		}
    		if(l.getBlock().getRelative(-1, 0, 0).getType().isSolid()) {
    			return true;
    		}
    		if(l.getBlock().getRelative(-1, 0, 1).getType().isSolid()) {
    			return true;
    		}
    		if(l.getBlock().getRelative(-1, 0, -1).getType().isSolid()) {
    			return true;
    		}
    		if(l.getBlock().getRelative(0, 0, 0).getType().isSolid()) {
    			return true;
    		}
    		if(l.getBlock().getRelative(0, 0, 1).getType().isSolid()) {
    			return true;
    		}
    		if(l.getBlock().getRelative(0, 0, -1).getType().isSolid()) {
    			return true;
    		}
    	}
    	   	
    	return false;
    }
}
