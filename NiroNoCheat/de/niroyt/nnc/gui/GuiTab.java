package de.niroyt.nnc.gui;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public abstract class GuiTab {

	InventoryType InvTyp;
	int slots = -1;
	String name;
	Inventory inv;
	HashMap<Integer, ClickEvent> ItemClickEvent = new HashMap<Integer, ClickEvent>();
	
	public abstract void setItems();
	
	public void setInvTyp(InventoryType invTyp) {
		this.InvTyp = invTyp;
	}
	
	public InventoryType getInvTyp() {
		return this.InvTyp;
	}
	
	public void setSlots(int slots) {
		this.slots = slots;
	}
	
	public int getSlots() {
		return this.slots;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public void setInv(Inventory i) {
		this.inv = i;
		if(InventoryType.CHEST == InvTyp || InventoryType.ENDER_CHEST == InvTyp) {
			this.inv.setMaxStackSize(getSlots());
		}		
	}
	
	public void setItem(int pos, ItemStack item, ClickEvent e) {
		inv.setItem(pos, item);
				
		ItemClickEvent.put(pos, e);
	}
	
	public ClickEvent getClickEvent(int id) {
		return ItemClickEvent.get(id);
	}
	
	public Inventory getInv() {
		return inv;
	}	
	
	public ItemStack getItemStack(Material material, int material_count, short material_data, String name, ArrayList<String> text_list) {
		ItemStack item_stack = new ItemStack(material, material_count, material_data);
		ItemMeta meta = item_stack.getItemMeta();
		meta.setDisplayName(name);		
		meta.setLore(text_list);		
		item_stack.setItemMeta(meta);
		
		return item_stack;
	}
	
	public ItemStack getItemStack(Material material, short material_data, String name, ArrayList<String> text_list) {
		return getItemStack(material, 1, material_data, name, text_list);
	}
}
