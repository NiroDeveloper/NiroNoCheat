package de.niroyt.nnc.gui.taps;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.niroyt.nnc.Main;
import de.niroyt.nnc.enums.EventTypes;
import de.niroyt.nnc.gui.ClickEvent;
import de.niroyt.nnc.gui.GuiTab;
import de.niroyt.nnc.utils.ConfigManager;


public class Settings extends GuiTab {

	String value;
	
	public Settings(String value) {
		this.value = value;
		
		setName("Settings");
		setInvTyp(InventoryType.CHEST);
		setSlots(27);
		
		setInv(Bukkit.createInventory(null, getInvTyp(), getName()));
		
		setItems();
	}
	
	public String getValue() {
		return this.value;
	}
	
	public void setItems() {
		ItemStack i1 = new ItemStack(Material.FIREWORK, 1);
		ItemMeta m1 = i1.getItemMeta();
		m1.setDisplayName("§6Kick at violation level: " + ConfigManager.cfg.getInt("Kick-at-Violation-Level"));
		ArrayList<String> l1 = new ArrayList<String>();
		l1.add("§7Change variable \"Kick-at-Violation-Level\" in the config.yml");
		m1.setLore(l1);
		i1.setItemMeta(m1);
		setItem(10, i1, null);	
		
		boolean value2 = ConfigManager.getConfigBoolean("Debugmode");			
		ItemStack i2 = new ItemStack(Material.COMMAND, 1);
		ItemMeta m2 = i2.getItemMeta();
		m2.setDisplayName((value2 ? "§2" : "§c") + "Debugmode");
		ArrayList<String> l2 = new ArrayList<String>();
		l2.add("§7Click to " + (!value2 ? "Enable" : "Disable"));
		m2.setLore(l2);
		i2.setItemMeta(m2);
		setItem(11, i2, new ClickEvent(EventTypes.CHANGE_CONFIG_BOOLEAN, "Debugmode"));	
		
		boolean value3 = ConfigManager.getConfigBoolean("Write-Log");			
		ItemStack i3 = new ItemStack(Material.BOOK_AND_QUILL, 1);
		ItemMeta m3 = i3.getItemMeta();
		m3.setDisplayName((value3 ? "§2" : "§c") + "Write Logfiles");
		ArrayList<String> l3 = new ArrayList<String>();
		l3.add("§7Click to " + (!value3 ? "Enable" : "Disable"));
		m3.setLore(l3);
		i3.setItemMeta(m3);
		setItem(12, i3, new ClickEvent(EventTypes.CHANGE_CONFIG_BOOLEAN, "Write-Log"));	
		
		boolean value4 = ConfigManager.getConfigBoolean("Violation-Messages");			
		ItemStack i4 = new ItemStack(Material.PAPER, 1);
		ItemMeta m4 = i4.getItemMeta();
		m4.setDisplayName((value4 ? "§2" : "§c") + "Violation Messages");
		ArrayList<String> l4 = new ArrayList<String>();
		l4.add("§7Click to " + (!value4 ? "Enable" : "Disable"));
		m4.setLore(l4);
		i4.setItemMeta(m4);
		setItem(13, i4, new ClickEvent(EventTypes.CHANGE_CONFIG_BOOLEAN, "Violation-Messages"));	
		
		if(Main.isVip()) {
			setItem(14, getItemStack(Material.ENCHANTMENT_TABLE, (short) 0, 
						(ConfigManager.getConfigBoolean("Auto-Updater") ? "§2" : "§c") + "Auto Updater", 
					new ArrayList<String>() {{ add("§7Click to " + (!value4 ? "Enable" : "Disable")); }}),  
					new ClickEvent(EventTypes.CHANGE_CONFIG_BOOLEAN, "Auto-Updater"));
		}
	}	
}
