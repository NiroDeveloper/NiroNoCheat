package de.niroyt.nnc.gui;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.niroyt.nnc.Main;
import de.niroyt.nnc.gui.taps.MainTab;
import de.niroyt.nnc.gui.taps.ManageChecks;
import de.niroyt.nnc.gui.taps.ModuleConfiguration;
import de.niroyt.nnc.gui.taps.Settings;

public class GuiMain {

	String player;
	GuiTab GuiTab;
	int id;
	String LastTab = "";
	
	public GuiMain(Player player) {		
		this.player = player.getName();		
		GuiTab = new MainTab();
				
		start();
	}
	
	public boolean closeInv() {
		if(LastTab.length() == 0) {
			Bukkit.getScheduler().cancelTask(id);
			
			return true;
		}
				
		if(LastTab.split("##")[LastTab.split("##").length - 1].equals("MainTab")) {
			GuiTab = new MainTab();
			
			LastTab = "";
		} else if(LastTab.split("##")[LastTab.split("##").length - 1].contains("ManageChecks")) {
			GuiTab = new ManageChecks(LastTab.split("##")[LastTab.split("##").length - 1].split("__")[1]);
			
			LastTab = LastTab.replace(LastTab.split("##")[LastTab.split("##").length - 1], "");
		} else if(LastTab.split("##")[LastTab.split("##").length - 1].contains("Settings")) {
			GuiTab = new Settings(LastTab.split("##")[LastTab.split("##").length - 1].split("__")[1]);
			
			LastTab = LastTab.replace(LastTab.split("##")[LastTab.split("##").length - 1], "");
		} else if(LastTab.split("##")[LastTab.split("##").length - 1].contains("ModuleConfiguration")) {
			GuiTab = new ModuleConfiguration(LastTab.split("##")[LastTab.split("##").length - 1].split("__")[1]);
			
			LastTab = LastTab.replace(LastTab.split("##")[LastTab.split("##").length - 1], "");
		}
				
		Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), new Runnable() {			
			@Override
			public void run() {
				update();
			}
		});
		
		return false;
	}
	
	public void start() {
		fillUpGlass();
		
		Bukkit.getPlayer(player).openInventory(GuiTab.getInv());
		
		id = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getInstance(), new Runnable() {			
			@Override
			public void run() {
				update();
			}
		}, 50L, 50L);
	}
	
	public void update() {		
		GuiTab.setItems();
		
		fillUpGlass();
		
		Bukkit.getPlayer(player).openInventory(GuiTab.getInv());
	}
	
	public void setGuiTab(GuiTab GuiTab) {
		this.GuiTab = GuiTab;
		
		Bukkit.getPlayer(player).openInventory(GuiTab.getInv());
	}
	
	public GuiTab getGuiTab() {
		return this.GuiTab;
	}
	
	private void fillUpGlass() {
		int i = 0;		
		while(i < GuiTab.getSlots()) {
			ItemStack item = null;
			if(Main.MinecraftVersion.equalsIgnoreCase("v1_8_R3")) {
				item = new ItemStack(Material.STAINED_GLASS_PANE, 1, DyeColor.BLACK.getData());
			} else {
				item = new ItemStack(Material.STAINED_GLASS_PANE, 1, DyeColor.BLACK.getDyeData());
			}
			ItemMeta meta = item.getItemMeta();
			meta.setDisplayName(" ");
			meta.setLore(new ArrayList<String>());
			item.setItemMeta(meta);
					
			if(GuiTab.getInv().getItem(i) == null) {
				GuiTab.setItem(i, item, null);
			}		
			
			i++;
		}		
	}	
}
