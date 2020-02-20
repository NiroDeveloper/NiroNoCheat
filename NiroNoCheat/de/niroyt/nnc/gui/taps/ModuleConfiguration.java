package de.niroyt.nnc.gui.taps;

import java.util.ArrayList;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

import de.niroyt.nnc.Main;
import de.niroyt.nnc.enums.Check;
import de.niroyt.nnc.enums.EventTypes;
import de.niroyt.nnc.gui.ClickEvent;
import de.niroyt.nnc.gui.GuiTab;
import de.niroyt.nnc.utils.ConfigManager;


public class ModuleConfiguration extends GuiTab {

	String value;
	
	public ModuleConfiguration(String value) {
		this.value = value;
		
		setName(Check.valueOf(getCheckString()).showCheckCategory() + " - [" + getCheckString() + "]");
		setInvTyp(InventoryType.CHEST);
		setSlots(27);
		
		setInv(Bukkit.createInventory(null, getInvTyp(), getName()));
		
		setItems();
	}
	
	public String getValue() {
		return this.value;
	}
	
	public String getCheckString() {
		return getValue().split(":")[0];
	}
	
	public int getScrollValue() {
		return Integer.valueOf(getValue().split(":")[1]);
	}
	
	public void setItems() {
		Check check = Check.valueOf(getCheckString());
		boolean enabled = ConfigManager.isCheckEnabled(check);
		
		setItem(9, getItemStack(Material.INK_SACK, (short) (enabled ? 10 : 7), "§" + (enabled? "2" : "8") + Check.valueOf(getCheckString()).showCheckCategory() + " - [" + check.toString() + "]", 
				new ArrayList<String>() {{ add(""); add("§7Click to " + (enabled ? "DISABLE" : "ENABLE")); }}), 
				new ClickEvent(EventTypes.CHANGE_CHECK_ENABLED, check.toString()));			
		
		
		setItem(11, getItemStack(Material.ANVIL, (short) 0, "§8Weight: §9" + ConfigManager.getCheckConfigFlagLevel(check), 
				new ArrayList<String>() {{ add("§7Leftclick to belittle the weight!"); add(""); add("§7Rightclick to increase the weight!"); add(""); add("§7Default: " + check.showFlagLevel()); }}), 
				new ClickEvent(EventTypes.CHANGE_CHECK_WEIGHT, check.toString()));	
		
		setItem(12, getItemStack(Material.WORKBENCH, (short) 0, "§8Measure: §9" + ConfigManager.getMeasure(check), 
				new ArrayList<String>() {{ add("§7Leftclick to belittle the aggressivity!"); add(""); add("§7Rightclick to increase the aggressivity!"); add(""); add("§7Sequence:"); add("§7NOTHING - CANCEL - KICK - BAN"); add(""); add("§7Default: " + check.showMeasure()); }}), 
				new ClickEvent(EventTypes.CHANGE_CHECK_MEASURE, check.toString()));	
		
		
		int sites = (int) (Math.floor(check.showValueList().size() / 3) + 1);
		
		boolean canScrollUp = getScrollValue() > 0;
		boolean canScrollDown = getScrollValue() + 1 < sites;
		setItem(8, getItemStack(canScrollUp ? Material.NETHER_STAR : Material.CLAY_BALL, (short) 0, (canScrollUp ? "§6" : "§8") + "Last page", 
				new ArrayList<String>() {{ if(canScrollUp) { add(""); add("§7Click to see the last page"); } }}),  
				new ClickEvent(canScrollUp ? EventTypes.SET_GUI_VALUE : null, getCheckString() + ":" + (getScrollValue() - 1)));
		setItem(26, getItemStack(canScrollDown ? Material.NETHER_STAR : Material.CLAY_BALL, (short) 0, (canScrollDown ? "§6" : "§8") + "Next page", 
				new ArrayList<String>() {{ if(canScrollDown) { add(""); add("§7Click to see the next page"); } }}),  
				new ClickEvent(canScrollDown ? EventTypes.SET_GUI_VALUE : null, getCheckString() + ":" + (getScrollValue() + 1)));
		setItem(17, getItemStack(Material.PAPER, getScrollValue() + 1, (short) 0, "§7" + (getScrollValue() + 1) + "/" + sites, new ArrayList<String>()), 
				new ClickEvent(null));
		
		
		ItemStack white_glass = getItemStack(Material.STAINED_GLASS_PANE, 
				Main.MinecraftVersion.equalsIgnoreCase("v1_8_R3") ? DyeColor.WHITE.getData() : DyeColor.WHITE.getDyeData(), " ", 
				new ArrayList<String>());
	
		setItem(5, white_glass, new ClickEvent(null));
		setItem(14, white_glass, new ClickEvent(null));
		setItem(23, white_glass, new ClickEvent(null));
				
		setItem(7, white_glass, new ClickEvent(null));
		setItem(16, white_glass, new ClickEvent(null));
		setItem(25, white_glass, new ClickEvent(null));
		
		String varName1 = null;
		String varName2 = null;
		String varName3 = null;
		
		int count = 1 - getScrollValue() * 3;
		for(Entry<String, Double> entry : check.showValueList().entrySet()) {
			if(count == 1) {
				varName1 = entry.getKey();
			} else if(count == 2) {
				varName2 = entry.getKey();
			} else if(count == 3) {
				varName3 = entry.getKey();
			}
			
			count ++;
		}
		
		final String varName1Fin = varName1;
		final String varName2Fin = varName2;
		final String varName3Fin = varName3;
		
		if(varName1 != null) {
			setItem(6, getItemStack(Material.SIGN, (short) 0, "§8" + varName1 + ": §3" + ConfigManager.getCheckConfigValue(check, varName1), 
					new ArrayList<String>() {{ add("§7Leftclick to belittle the value!"); add(""); add("§7Rightclick to increase the value!"); add(""); add("§7Midclick to reset the value!"); add(""); add("§7Default: " + check.showValueList().get(varName1Fin)); }}), 
					new ClickEvent(EventTypes.CHANGE_CHECK_VARIABLE, getCheckString() + ":" + varName1));
		}
		
		if(varName2 != null) {
			setItem(15, getItemStack(Material.SIGN, (short) 0, "§8" + varName2 + ": §3" + ConfigManager.getCheckConfigValue(check, varName2), 
					new ArrayList<String>() {{ add("§7Leftclick to belittle the value!"); add(""); add("§7Rightclick to increase the value!"); add(""); add("§7Midclick to reset the value!"); add(""); add("§7Default: " + check.showValueList().get(varName2Fin)); }}), 
					new ClickEvent(EventTypes.CHANGE_CHECK_VARIABLE, getCheckString() + ":" + varName2));
		}
		
		if(varName3 != null) {
			setItem(24, getItemStack(Material.SIGN, (short) 0, "§8" + varName3 + ": §3" + ConfigManager.getCheckConfigValue(check, varName3), 
					new ArrayList<String>() {{ add("§7Leftclick to belittle the value!"); add(""); add("§7Rightclick to increase the value!"); add(""); add("§7Midclick to reset the value!"); add(""); add("§7Default: " + check.showValueList().get(varName3Fin)); }}), 
					new ClickEvent(EventTypes.CHANGE_CHECK_VARIABLE, getCheckString() + ":" + varName3));
		}
	}	
	
	
}
