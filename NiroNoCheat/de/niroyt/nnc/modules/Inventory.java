package de.niroyt.nnc.modules;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import de.niroyt.nnc.enums.Check;
import de.niroyt.nnc.manager.Manager;
import de.niroyt.nnc.manager.Module;
import de.niroyt.nnc.utils.ConfigManager;
import de.niroyt.nnc.utils.Flag;
import de.niroyt.nnc.utils.GroundUtils;

public class Inventory extends Module {

	public static void InventoryOpen(final Player player) {
		Manager.getPlayerData(player).setInventoryOpen(System.currentTimeMillis());	
	}
	
	public static void InventoryClose(final Player player) {
		Manager.getPlayerData(player).setInventoryOpen(-1337);	
	}
	
	public static boolean IsInventoryOpen(final Player player) {
		return Manager.getPlayerData(player).getInventoryOpen() != -1337;
	}
	
	public static Long getInventoryOpenTime(final Player player) {
		if(!IsInventoryOpen(player)) {
			return (long) 0;
		}
		
		return Manager.getPlayerData(player).getInventoryOpen();
	}
			
	public void onMove(final PlayerMoveEvent e) {
		final Player player = e.getPlayer();
		
		if(Manager.isOP(player)) {
			return;
		}
		
		if((e.getFrom().getYaw() != e.getTo().getYaw() || e.getFrom().getPitch() != e.getTo().getPitch()) && IsInventoryOpen(player) && getInventoryOpenTime(player) + 800 < System.currentTimeMillis()) {
			if(Manager.debug(player, Check.im4, "")) {
				InventoryClose(player);
				player.closeInventory();
			}
		}
		
		if(player.isBlocking() && IsInventoryOpen(player) && getInventoryOpenTime(player) + 800 < System.currentTimeMillis()) {
			if(Manager.debug(player, Check.im5, "")) {
				InventoryClose(player);
				player.closeInventory();
			}
		}
		
		final double mX = e.getTo().getX() - e.getFrom().getX();
		final double mZ = e.getTo().getZ() - e.getFrom().getZ();
		final double speed = Math.sqrt(mX * mX + mZ * mZ);
		
		if(IsInventoryOpen(player) && !GroundUtils.wasOnIce(player) && !player.isFlying()) {
			if(Manager.getPlayerData(player).getInventorySpeed() == 0) {
				Manager.getPlayerData(player).setInventorySpeed(speed);
			} else {
				if(Manager.getPlayerData(player).getInventorySpeed() - 0.005 < speed && speed > ConfigManager.getCheckConfigValue(Check.im1, "maxSpeed")) {
					int newV = Manager.getPlayerData(player).getInventoryMoveFlags() + 1;
					if(Manager.getPlayerData(player).getInventorySpeed() + 0.03 <= speed) {
						newV += 1;
					}
					Manager.getPlayerData(player).setInventoryMoveFlags(newV);
					
					if(Manager.getPlayerData(player).getInventoryMoveFlags() >= ConfigManager.getCheckConfigValue(Check.im1, "maxFlags")) {
						if(Manager.debug(player, Check.im1, Double.valueOf(Math.round((Manager.getPlayerData(player).getInventorySpeed() - 0.005) * 1000)) / 1000 + " < " + Double.valueOf(Math.round(speed * 1000)) / 1000)) {
							InventoryClose(player);
							player.closeInventory();
							Flag.backPort(player);
						}
					}					
				}
				Manager.getPlayerData(player).setInventorySpeed(speed);
			}
		} else {
			Manager.getPlayerData(player).setInventorySpeed(0);
			Manager.getPlayerData(player).setInventoryMoveFlags(0);
		}
		
		final double mY = e.getTo().getY() - e.getFrom().getY();		
		if(IsInventoryOpen(player)) {
			if(mY == 0.41999998688697815) {
				if(Manager.debug(player, Check.im6, "")) {
					InventoryClose(player);
					player.closeInventory();
				}
			}
		} 
	}	
	
	public void onDeath(final PlayerDeathEvent e) {
		if(IsInventoryOpen(e.getEntity())) {
			InventoryClose(e.getEntity());
		}
	}
	
	public void onInventoryClick(final InventoryClickEvent e) {
		if(!(e.getWhoClicked() instanceof Player)) {
			return;
		}
		final Player player = (Player) e.getWhoClicked();
		if(Manager.isOP(player)) {
			return;
		}
		if(e.getInventory().getType() == InventoryType.CRAFTING && !IsInventoryOpen(player)) {
			if(e.getSlot() >= 9 && Manager.getPlayerData(player).isInventoryOpenPacket()) {
				if(Manager.debug(player, Check.im2, "")) {
					e.setCancelled(true);
					player.closeInventory();
					InventoryClose(player);
				}			
			}
		}
		if(player.isSprinting()) {
			if(Manager.debug(player, Check.sp3, "")) {
				e.setCancelled(true);
				player.closeInventory();
			}
		}
	}
	
	public void onDamageByEntityEvent(final EntityDamageByEntityEvent e) {
		if(e.getDamager() instanceof Player) {			
			final Player player = (Player) e.getDamager();
			if(Manager.isOP(player)) {
				return;
			}
			if(IsInventoryOpen(player)) {
				if(Manager.debug(player, Check.im3, "")) {
					e.setCancelled(true);
					Killaura.setHitLock(player, 10000);
				}
			}
		}
	}
	
	public void onInventoryOpen(final InventoryOpenEvent e) {
		if(!e.isCancelled()) {
			InventoryOpen((Player) e.getPlayer());
		}		
	}
	
	public void onInventoryClose(final InventoryCloseEvent e) {
		InventoryClose((Player) e.getPlayer());
	}
	
	public void onItemHeld(final PlayerItemHeldEvent e) {
		final Player player = e.getPlayer();
		
		if(Manager.isOP(player)) {
			return;
		}
		
		if(Manager.ping(player) > 100) {
			return;
		}
		
		final double value = (System.nanoTime() - Manager.getPlayerData(player).getInventoryLastItemHoldChange()) / 100000;
		if(value < ConfigManager.getCheckConfigValue(Check.bp5, "minValue") && value != 0) {
			if(Manager.debug(player, Check.bp5, "Value: " + (Double.valueOf(Math.round(value * 100)) / 100))) {
				Killaura.setHitLock(player, 1000);
				Manager.getPlayerData(player).setWorldPlaceBlockLock(System.currentTimeMillis() + 1000);
			}
		}		
		
		Manager.getPlayerData(player).setInventoryLastItemHoldChange(System.nanoTime());
	}
}
