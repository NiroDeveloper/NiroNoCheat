package de.niroyt.nnc.manager;

import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.event.player.PlayerToggleSprintEvent;
import org.bukkit.event.player.PlayerVelocityEvent;

public abstract class Module {

	public Module() {}
	
	public void onMove(PlayerMoveEvent e) {}
	
	public void onDamageByEntityEvent(EntityDamageByEntityEvent e) {}
	public void onDamageByEntityEventHighest(EntityDamageByEntityEvent e) {}
	
	public void onTick() {}
	
	public void onBlockBreak(BlockBreakEvent e) {}
	public void onBlockBreakLowest(BlockBreakEvent e) {}
	
	public void onInventoryOpen(InventoryOpenEvent e) {}
	
	public void onInventoryClose(InventoryCloseEvent e) {}
	
	public void onInventoryClick(InventoryClickEvent e) {}
	public void onInventoryClickHighest(InventoryClickEvent e) {}
	
	public void onSignChange(SignChangeEvent e) {}
	
	public void onCreatureSpawn(CreatureSpawnEvent e) {}
	
	public void onInteract(PlayerInteractEvent e) {}
	
	public void onEntityShootBow(EntityShootBowEvent e) {}
	
	public void onEntityRegainHealth(EntityRegainHealthEvent e) {}
	
	public void onBlockPlace(BlockPlaceEvent e) {}
	
	public void onItemConsume(PlayerItemConsumeEvent e) {}
	
	public void onSwingArm(Player player) {}
	
	public void onTeleport(PlayerTeleportEvent e) {}
	
	public void onDeath(PlayerDeathEvent e) {}
	
	public void onQuit(PlayerQuitEvent e) {}
	
	public void onRespawn(PlayerRespawnEvent e) {}
	
	public void onVelocity(PlayerVelocityEvent e) {}
	
	public void onJoin(PlayerJoinEvent e) {}
	
	public void onToggleSneak(PlayerToggleSneakEvent e) {}
	
	public void onToggleSprint(PlayerToggleSprintEvent e) {}
	
	public void onToggleFlight(PlayerToggleFlightEvent e) {}

	public void onDropItem(PlayerDropItemEvent e) {}

	public void onItemHeld(PlayerItemHeldEvent e) {}
	
}
