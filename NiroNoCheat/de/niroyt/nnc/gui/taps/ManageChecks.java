package de.niroyt.nnc.gui.taps;

import java.util.ArrayList;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.niroyt.nnc.enums.Check;
import de.niroyt.nnc.enums.CheckCategory;
import de.niroyt.nnc.enums.EventTypes;
import de.niroyt.nnc.gui.ClickEvent;
import de.niroyt.nnc.gui.GuiTab;
import de.niroyt.nnc.utils.ConfigManager;

public class ManageChecks extends GuiTab {

	String value;
	
	public ManageChecks(String value) {
		this.value = value;
		
		setName("");
		setInvTyp(InventoryType.CHEST);
		setSlots(27);
		
		setInv(Bukkit.createInventory(null, getInvTyp(), getName()));
		
		setItems();
	}
	
	public String getValue() {
		return this.value;
	}
	
	public void setItems() {
		if(value == null || value.equalsIgnoreCase("null")) {
			int SlotPosition = 0;
			
			setName("Categories");
			setInv(Bukkit.createInventory(null, getInvTyp(), getName()));
			
			ArrayList<String> categories = new ArrayList<String>();
			categories.add("Moving");
			categories.add("Combat");
			categories.add("Killaura");
			categories.add("Speed");
			categories.add("Player");
			categories.add("Inventory");
			categories.add("World");
			categories.add("Packets");
			categories.add("Angle");
			categories.add("Velocity");
			
			categories.sort(String::compareToIgnoreCase);
			
			for(String cc : categories) {				
				ItemStack i = new ItemStack(Material.CHEST, 1);
				ItemMeta m = i.getItemMeta();
				m.setDisplayName("§6" + cc);
				m.setLore(new ArrayList<String>());
				i.setItemMeta(m);
				setItem(SlotPosition, i, new ClickEvent(EventTypes.GO_TO_CHECKS, "CheckTypes:" + cc));	
				
				SlotPosition += 1;
			}
									
			boolean value2 = ConfigManager.getConfigBoolean("Checks.AntiDamageIndicator.Enabled");			
			ItemStack i2 = new ItemStack(Material.PAPER, 1);
			ItemMeta m2 = i2.getItemMeta();
			m2.setDisplayName((value2 ? "§2" : "§c") + "AntiDamageIndicator");
			ArrayList<String> l2 = new ArrayList<String>();
			l2.add("§7Click to " + (!value2 ? "Enable" : "Disable"));
			m2.setLore(l2);
			i2.setItemMeta(m2);
			setItem(SlotPosition, i2, new ClickEvent(EventTypes.CHANGE_CONFIG_BOOLEAN, "Checks.AntiDamageIndicator.Enabled"));	
			
			SlotPosition += 1;
			
			boolean value3 = ConfigManager.getConfigBoolean("Checks.AntiESP.Enabled");			
			ItemStack i3 = new ItemStack(Material.PAPER, 1);
			ItemMeta m3 = i3.getItemMeta();
			m3.setDisplayName((value3 ? "§2" : "§c") + "AntiESP");
			ArrayList<String> l3 = new ArrayList<String>();
			l3.add("§7Click to " + (!value3 ? "Enable" : "Disable"));
			m3.setLore(l3);
			i3.setItemMeta(m3);
			setItem(SlotPosition, i3, new ClickEvent(EventTypes.CHANGE_CONFIG_BOOLEAN, "Checks.AntiESP.Enabled"));	
			
		} else if(value.split(":")[0].equals("CheckTypes")) {
			setName("Subcategories - " + value.split(":")[1]);
			setInv(Bukkit.createInventory(null, getInvTyp(), getName()));
			
			int SlotPosition = 0;
						
			for(CheckCategory cc : CheckCategory.class.getEnumConstants()) {
				if(!cc.toString().contains(value.split(":")[1].toUpperCase())) {
					continue;
				}
				
				ItemStack i = new ItemStack(Material.ENDER_CHEST, 1);
				ItemMeta m = i.getItemMeta();
				m.setDisplayName("§6" + cc.toString());
				m.setLore(new ArrayList<String>());
				i.setItemMeta(m);
				setItem(SlotPosition, i, new ClickEvent(EventTypes.GO_TO_CHECKS, "Checks:" + cc));	
				
				SlotPosition += 1;
			}
		} else if(value.split(":")[0].equals("Checks")) {
			setName("Checks - " + value.split(":")[1]);
			setInv(Bukkit.createInventory(null, getInvTyp(), getName()));
			
			int SlotPosition = 0;
						
			for(Check cc : Check.values()) {				
				if(!cc.showCheckCategory().toString().equals(value.split(":")[1])) {
					continue;
				}
								
				boolean enabled = ConfigManager.isCheckEnabled(cc);
				
				ItemStack i = new ItemStack(enabled ? Material.ENCHANTED_BOOK : Material.BOOK, 1);
				ItemMeta m = i.getItemMeta();
				m.setDisplayName("§" + (enabled? "d" : "8") + "[" + cc.toString() + "]");
				
				ArrayList<String> l1 = new ArrayList<String>();
				l1.add("");
				l1.add("§7Click to change settings.");
				l1.add("");
				l1.add("§7Checking: §8" + value.split(":")[1]);	
				l1.add("");
				l1.add("§7Weight: §8" + ConfigManager.getCheckConfigFlagLevel(cc));	
				l1.add("§7Measure: §8" + ConfigManager.getMeasure(cc));	
				if(!cc.showValueList().isEmpty()) {
					l1.add("");
					l1.add("§7Values:");
					
					for(Entry<String, Double> entry : cc.showValueList().entrySet()) {
						l1.add("§7- " + entry.getKey() + ": §8" + ConfigManager.getCheckConfigValue(cc, entry.getKey()));
					}
				}
				m.setLore(l1);
				
				i.setItemMeta(m);
				setItem(SlotPosition, i, new ClickEvent(EventTypes.GO_TO_MODULE_CONFIGURATION, cc.toString() + ":0"));	
				
				SlotPosition += 1;
			}
		}
	}	
}
