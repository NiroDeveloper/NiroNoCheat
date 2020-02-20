package de.niroyt.nnc.manager;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
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

import de.niroyt.nnc.Main;
import de.niroyt.nnc.utils.ConfigManager;

public class EventCaller implements Listener {

	public EventCaller() {
		Bukkit.getPluginManager().registerEvents((Listener)this, Main.getInstance());
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onMove(final PlayerMoveEvent e) {
		final Player player = e.getPlayer();
		
		if(e.isCancelled() || Manager.getPlayerData(player).getLastTeleportPos() == e.getFrom()) {
			Manager.getPlayerData(e.getPlayer()).setLastTeleportPos(null);
			return;
		}
								
		for(final Module m : Main.moduleList) {
			m.onMove(e);
		}
		
		if(e.isCancelled() && !ConfigManager.getConfigBoolean("Allowed-as-Flag.setBack")) {
			e.setCancelled(false);
		}
	}
	
	@EventHandler
	public void onBlockBreak(final BlockBreakEvent e) {
		for(final Module m : Main.moduleList) {
			m.onBlockBreak(e);
		}
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onBlockBreakLowest(final BlockBreakEvent e) {
		for(final Module m : Main.moduleList) {
			m.onBlockBreakLowest(e);
		}
	}
	
	@EventHandler
	public void onInventoryOpen(final InventoryOpenEvent e) {
		for(final Module m : Main.moduleList) {
			m.onInventoryOpen(e);
		}
	}
	
	@EventHandler
	public void onInventoryClose(final InventoryCloseEvent e) {
		for(final Module m : Main.moduleList) {
			m.onInventoryClose(e);
		}
	}
	
	@EventHandler
	public void onInventoryClick(final InventoryClickEvent e) {
		for(final Module m : Main.moduleList) {
			m.onInventoryClick(e);
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onInventoryClickHighest(final InventoryClickEvent e) {
		for(final Module m : Main.moduleList) {
			m.onInventoryClickHighest(e);
		}
	}
	
	@EventHandler
	public void onDamageByEntityEvent(final EntityDamageByEntityEvent e) {
		for(final Module m : Main.moduleList) {
			m.onDamageByEntityEvent(e);
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onDamageByEntityEventHighest(final EntityDamageByEntityEvent e) {
		for(final Module m : Main.moduleList) {
			m.onDamageByEntityEventHighest(e);
		}
	}
			
	@EventHandler
	public void onSignChange(final SignChangeEvent e) {
		for(final Module m : Main.moduleList) {
			m.onSignChange(e);
		}
	}
	
	@EventHandler
	public void onCreatureSpawn(final CreatureSpawnEvent e) {
		for(final Module m : Main.moduleList) {
			m.onCreatureSpawn(e);
		}
	}
	
	@EventHandler
	public void onInteract(final PlayerInteractEvent e) {
		for(final Module m : Main.moduleList) {
			m.onInteract(e);
		}
	}
	
	@EventHandler
	public void onEntityShootBow(final EntityShootBowEvent e) {
		for(final Module m : Main.moduleList) {
			m.onEntityShootBow(e);
		}
	}
	
	@EventHandler
	public void onEntityRegainHealth(final EntityRegainHealthEvent e) {
		for(final Module m : Main.moduleList) {
			m.onEntityRegainHealth(e);
		}
	}
	
	@EventHandler
	public void onBlockPlace(final BlockPlaceEvent e) {
		for(final Module m : Main.moduleList) {
			m.onBlockPlace(e);
		}
	}
	
	@EventHandler
	public void onItemConsume(final PlayerItemConsumeEvent e) {
		for(final Module m : Main.moduleList) {
			m.onItemConsume(e);
		}
	}
		
	@EventHandler(priority = EventPriority.HIGH)
	public void onTeleport(final PlayerTeleportEvent e) {
		final Player player = e.getPlayer();
		
		Manager.getPlayerData(e.getPlayer()).setLastTeleportPos(e.getTo());
			
		for(final Module m : Main.moduleList) {
			m.onTeleport(e);
		}
	}
	
	@EventHandler
	public void onDeath(final PlayerDeathEvent e) {
		for(final Module m : Main.moduleList) {
			m.onDeath(e);
		}
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onQuit(final PlayerQuitEvent e) {
		for(final Module m : Main.moduleList) {
			m.onQuit(e);
		}
	}
	
	@EventHandler
	public void onRespawn(final PlayerRespawnEvent e) {
		for(final Module m : Main.moduleList) {
			m.onRespawn(e);
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onVelocity(final PlayerVelocityEvent e) {
		for(final Module m : Main.moduleList) {
			m.onVelocity(e);
		}
	}
	
	@EventHandler
	public void onJoin(final PlayerJoinEvent e) {
		for(final Module m : Main.moduleList) {
			m.onJoin(e);
		}
	}
	
	@EventHandler
	public void onToggleSneak(final PlayerToggleSneakEvent e) {
		for(final Module m : Main.moduleList) {
			m.onToggleSneak(e);
		}
	}
	
	@EventHandler
	public void onToggleSprint(final PlayerToggleSprintEvent e) {
		for(final Module m : Main.moduleList) {
			m.onToggleSprint(e);
		}
	}
	
	@EventHandler
	public void onToggleFlight(final PlayerToggleFlightEvent e) {
		for(final Module m : Main.moduleList) {
			m.onToggleFlight(e);
		}
	}
	
	@EventHandler
	public void onDropItem(final PlayerDropItemEvent e) {
		for(final Module m : Main.moduleList) {
			m.onDropItem(e);
		}
	}
	
	@EventHandler
	public void onPlayerItemHeld(final PlayerItemHeldEvent e) {
		for(final Module m : Main.moduleList) {
			m.onItemHeld(e);
		}
	}
}
