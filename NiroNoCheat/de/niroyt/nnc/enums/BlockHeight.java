package de.niroyt.nnc.enums;

import org.bukkit.Material;

public enum BlockHeight {

	SOUL_SAND(Material.SOUL_SAND, 0.875),
	COCOA(Material.COCOA, 0.75),
	CAULDRON1(Material.CAULDRON, 0.3125),
	CAULDRON2(Material.CAULDRON, 1.0),
	BREWING_STAND1(Material.BREWING_STAND, 0.125),
	BREWING_STAND2(Material.BREWING_STAND, 0.875),
	DIODE_BLOCK_OFF(Material.DIODE_BLOCK_OFF, 0.125),
	DIODE_BLOCK_ON(Material.DIODE_BLOCK_ON, 0.125),
	REDSTONE_COMPARATOR(Material.REDSTONE_COMPARATOR, 0.125),
	HOPPER1(Material.HOPPER, 0.625),
	HOPPER2(Material.HOPPER, 1.0),
	DAYLIGHT_DETECTOR_INVERTED(Material.DAYLIGHT_DETECTOR_INVERTED, 0.375),
	DAYLIGHT_DETECTOR(Material.DAYLIGHT_DETECTOR, 0.375),
	IRON_TRAPDOOR(Material.IRON_TRAPDOOR, 0.1875),
	TRAP_DOOR(Material.TRAP_DOOR, 0.1875),
	SKULL(Material.SKULL, 0.5),
	BED_BLOCK(Material.BED_BLOCK, 0.5625),
	CARPET(Material.CARPET, 0.0625),
	CHEST(Material.CHEST, 0.875),
	TRAPPED_CHEST(Material.TRAPPED_CHEST, 0.875),
	ENDER_CHEST(Material.ENDER_CHEST, 0.875),
	ENDER_PORTAL_FRAME1(Material.ENDER_PORTAL_FRAME, 0.8125),
	ENDER_PORTAL_FRAME2(Material.ENDER_PORTAL_FRAME, 1.0),
	ENCHANTMENT_TABLE(Material.ENCHANTMENT_TABLE, 0.75),
	SNOW(Material.SNOW, 0.125),
	WATER_LILY(Material.WATER_LILY, 0.015625),
	CACTUS(Material.CACTUS, 0.9375),
	FENCE(Material.FENCE, 1.5),
	FENCE_GATE(Material.FENCE_GATE, 1.5),
	ACACIA_FENCE(Material.ACACIA_FENCE, 1.5),
	ACACIA_FENCE_GATE(Material.ACACIA_FENCE_GATE, 1.5),
	BIRCH_FENCE(Material.BIRCH_FENCE, 1.5),
	BIRCH_FENCE_GATE(Material.BIRCH_FENCE_GATE, 1.5),
	DARK_OAK_FENCE(Material.DARK_OAK_FENCE, 1.5),
	DARK_OAK_FENCE_GATE(Material.DARK_OAK_FENCE_GATE, 1.5),
	IRON_FENCE(Material.IRON_FENCE, 1.5),
	JUNGLE_FENCE(Material.JUNGLE_FENCE, 1.5),
	JUNGLE_FENCE_GATE(Material.JUNGLE_FENCE_GATE, 1.5),
	NETHER_FENCE(Material.NETHER_FENCE, 1.5),
	SPRUCE_FENCE(Material.SPRUCE_FENCE, 1.5),
	ENCE_GATE(Material.SPRUCE_FENCE_GATE, 1.5),
	LADDER(Material.LADDER, 1.0),
	COBBLE_WALL(Material.COBBLE_WALL, 1.5);
	
	Material m;	
	double h;	
	BlockHeight(Material material, double height) {
		this.m = material;
		this.h = height;
	}
	
	public Material getMaterial() {
		return m;
	}
	public double getHeight() {
		return h;
	}
	
}
