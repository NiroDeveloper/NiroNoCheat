package de.niroyt.nnc.gui;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import de.niroyt.nnc.Main;

public class EventsForGui implements Listener {

	@EventHandler
	public void onClick(InventoryClickEvent e) {
		if(Main.OptionGui.containsKey(e.getWhoClicked().getUniqueId())) {
			e.setCancelled(true);

			if(e.getClickedInventory() != null) {
				if(e.getClickedInventory().equals(e.getWhoClicked().getInventory())) {
					return;
				}
			}
			
			if(Main.OptionGui.get(e.getWhoClicked().getUniqueId()).getGuiTab().getClickEvent(e.getSlot()) != null) {
				Main.OptionGui.get(e.getWhoClicked().getUniqueId()).getGuiTab().getClickEvent(e.getSlot()).start((Player) e.getWhoClicked(), e.getAction());
								
				Main.OptionGui.get(e.getWhoClicked().getUniqueId()).update();
			}
		}
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		if(Main.OptionGui.containsKey(e.getPlayer().getUniqueId())) {
			Bukkit.getScheduler().cancelTask(Main.OptionGui.get(e.getPlayer().getUniqueId()).id);
			Main.OptionGui.remove(e.getPlayer().getUniqueId());
		}
	}	
}
