package de.niroyt.nnc.gui;

import java.lang.reflect.InvocationTargetException;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryAction;

import de.niroyt.nnc.Main;
import de.niroyt.nnc.enums.Check;
import de.niroyt.nnc.enums.EventTypes;
import de.niroyt.nnc.enums.Status;
import de.niroyt.nnc.gui.taps.MainTab;
import de.niroyt.nnc.gui.taps.ManageChecks;
import de.niroyt.nnc.gui.taps.ModuleConfiguration;
import de.niroyt.nnc.gui.taps.Settings;
import de.niroyt.nnc.utils.ConfigManager;

public class ClickEvent {

	EventTypes EventTyp;
	
	String Value;
	
	public ClickEvent(EventTypes e) {
		EventTyp = e;
	}
	
	public ClickEvent(EventTypes e, String value) {
		EventTyp = e;
		Value = value;
	}

	public void start(Player player, InventoryAction inventory_action) {
		if(EventTypes.CHANGE_DISABLED == EventTyp) {
			Main.setDISABLED(!Main.isDisabled());
		} else if(EventTypes.GO_TO_CHECKS == EventTyp) {
			if(Main.OptionGui.get(player.getUniqueId()).getGuiTab() instanceof MainTab) {
				Main.OptionGui.get(player.getUniqueId()).LastTab += "MainTab##";
			} else {
				Main.OptionGui.get(player.getUniqueId()).LastTab += "ManageChecks__" + ((ManageChecks) Main.OptionGui.get(player.getUniqueId()).getGuiTab()).getValue() + "##";
			}
			
			Main.OptionGui.get(player.getUniqueId()).setGuiTab(new ManageChecks(Value));
		} else if(EventTypes.CHANGE_CHECK_ENABLED == EventTyp) {
			String variable = "Checks." + Check.valueOf(Value).showCheckCategory() + "." + Value + ".Enabled";
			
			ConfigManager.setConfigValue(variable, !ConfigManager.getConfigBoolean(variable));
		} else if(EventTypes.GO_TO_SETTINGS == EventTyp) {
			if(Main.OptionGui.get(player.getUniqueId()).getGuiTab() instanceof MainTab) {
				Main.OptionGui.get(player.getUniqueId()).LastTab += "MainTab##";
			} else {
				Main.OptionGui.get(player.getUniqueId()).LastTab += "Settings__" + ((Settings) Main.OptionGui.get(player.getUniqueId()).getGuiTab()).getValue() + "##";
			}
			
			Main.OptionGui.get(player.getUniqueId()).setGuiTab(new Settings(Value));
		} else if(EventTypes.CHANGE_CONFIG_BOOLEAN == EventTyp) {
			ConfigManager.setConfigValue(Value, !ConfigManager.getConfigBoolean(Value));
		} else if(EventTypes.GO_TO_MODULE_CONFIGURATION == EventTyp) {
			if(Main.OptionGui.get(player.getUniqueId()).getGuiTab() instanceof MainTab) {
				Main.OptionGui.get(player.getUniqueId()).LastTab += "MainTab##";
			} else {
				Main.OptionGui.get(player.getUniqueId()).LastTab += "ManageChecks__" + ((ManageChecks) Main.OptionGui.get(player.getUniqueId()).getGuiTab()).getValue() + "##";
			}
			
			Main.OptionGui.get(player.getUniqueId()).setGuiTab(new ModuleConfiguration(Value));
		} else if(EventTypes.CHANGE_CHECK_WEIGHT == EventTyp) {
			Check check = Check.valueOf(Value);
			
			if(inventory_action == InventoryAction.PICKUP_ALL) {
				ConfigManager.setConfigValue("Checks." + check.showCheckCategory() + "." + check + ".Weight", ConfigManager.getCheckConfigFlagLevel(check) - 1);
			} else if(inventory_action == InventoryAction.PICKUP_HALF) {
				ConfigManager.setConfigValue("Checks." + check.showCheckCategory() + "." + check + ".Weight", ConfigManager.getCheckConfigFlagLevel(check) + 1);
			}
		} else if(EventTypes.CHANGE_CHECK_MEASURE == EventTyp) {
			Check check = Check.valueOf(Value);
			
			if(inventory_action == InventoryAction.PICKUP_ALL) {
				ConfigManager.setConfigValue("Checks." + check.showCheckCategory() + "." + check + ".Measure", getNewMeasure(ConfigManager.getMeasure(check), -1).toString());
			} else if(inventory_action == InventoryAction.PICKUP_HALF) {
				ConfigManager.setConfigValue("Checks." + check.showCheckCategory() + "." + check + ".Measure", getNewMeasure(ConfigManager.getMeasure(check), 1).toString());
			}
		} else if(EventTypes.SET_GUI_VALUE == EventTyp) {
			try {
				try {
					Main.OptionGui.get(player.getUniqueId()).setGuiTab(Main.OptionGui.get(player.getUniqueId()).getGuiTab().getClass().getDeclaredConstructor(String.class).newInstance(this.Value));
				} catch (IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
					e.printStackTrace();
				}
			} catch (InstantiationException | IllegalAccessException e) {
				e.printStackTrace();
			}
		} else if(EventTypes.CHANGE_CHECK_VARIABLE == EventTyp) {
			Check check = Check.valueOf(Value.split(":")[0]);
			String VarName = Value.split(":")[1];
		
			String defaultValStr = Double.toString(check.showValueList().get(VarName));
			double increaseValue = defaultValStr.substring(defaultValStr.indexOf(".") + 1).length();
			
			for(double i = increaseValue; i > 0; i--) {
				if(i == increaseValue) {
					increaseValue = 1.0;
				}
				
				increaseValue *= 0.1;
			}
												
			if(inventory_action == InventoryAction.PICKUP_ALL) {
				ConfigManager.setCheckConfigValue(check, VarName, round(ConfigManager.getCheckConfigValue(check, VarName) - increaseValue));
			} else if(inventory_action == InventoryAction.PICKUP_HALF) {
				ConfigManager.setCheckConfigValue(check, VarName, round(ConfigManager.getCheckConfigValue(check, VarName) + increaseValue));
			} else if(inventory_action == InventoryAction.CLONE_STACK) {
				ConfigManager.setCheckConfigValue(check, VarName, check.showValueList().get(VarName));
			}
		}
	}	
	
	private double round(double val) {
		return Double.valueOf(Math.round(val * 10000)) / 10000;
	}
	
	private Status getNewMeasure(Status m, int diff) {
		int a = m.getAggresivity() + diff;
		for(Status s : Status.values()) {
			if(a == s.getAggresivity()) {
				return s;
			}
		}
		return m;
	}
}
