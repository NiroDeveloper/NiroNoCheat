package de.niroyt.nnc.utils;

import java.io.File;
import java.io.IOException;
import java.util.Map.Entry;

import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

import de.niroyt.nnc.Main;
import de.niroyt.nnc.enums.Check;
import de.niroyt.nnc.enums.Messages;
import de.niroyt.nnc.enums.Status;
import de.niroyt.nnc.manager.Commands;

public class ConfigManager {

	public static YamlConfiguration cfg;
	
	public static boolean debugMode() {			
		return getConfigBoolean("Debugmode");
	}

	public static boolean getConfigBoolean(final String s) {		
		if(!cfg.contains(s)) {
			setConfigValue(s, true);
		}
		
		return cfg.getBoolean(s);
	}
	
	public static boolean isCheckEnabled(final Check c) {
		return getConfigBoolean("Checks." + c.showCheckCategory() + "." + c + ".Enabled");
	}
	
	public static void setConfig(String s) {
		final File file = new File("plugins//NiroNoCheat//config.yml");
		s = s.replaceAll("\t", "    ");
		try {
			FileUtils.writeStringToFile(file, s);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		loadSettings();
				
		Commands.sendMessage(Bukkit.getConsoleSender(), "§2Config loaded!");
	}
	
	public static void loadSettings() {
		final File ordner = new File("plugins//NiroNoCheat");
		final File file = new File("plugins//NiroNoCheat//config.yml");
			
		if(!(ordner.exists())) {
			ordner.mkdir();
		}
			
		if(!(file.exists())) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		cfg = YamlConfiguration.loadConfiguration(file);
				
		for(final Check c : Check.values()) {
			getConfigBoolean("Checks." + c.showCheckCategory() + "." + c + ".Enabled");
			getCheckConfigFlagLevel(c);
			getMeasure(c);
			
			for(Entry<String, Double> entry : c.showValueList().entrySet()) {
				getCheckConfigValue(c, entry.getKey());
			}			
		}
	}	
	
	public static Status getMeasure(Check check) {
		final String ValuePos = "Checks." + check.showCheckCategory() + "." + check + ".Measure";
		
		if(!cfg.contains(ValuePos)) {
			final Status DefaultValue = check.showMeasure();
			cfg.set(ValuePos, DefaultValue.toString());
			saveConfig();
		}
		
		return Status.valueOf(cfg.getString(ValuePos));
	}

	public static String getMessage(final Messages m) {
		if(!Main.isVip()) {
			return m.getDefaultMessage().replaceAll("&", "§");
		}
		if(getConfigString("Messages." + m.toString().toLowerCase(), m.getDefaultMessage()).length() == 0) {
			return m.getDefaultMessage().replaceAll("&", "§");
		}
		return getConfigString("Messages." + m.toString().toLowerCase(), m.getDefaultMessage()).replaceAll("&", "§");
	}
	
	public static String getConfigString(final String s, final String Default) {
		if(!cfg.contains(s)) {
			cfg.set(s, Default);
			
			saveConfig();
		}
		
		return cfg.getString(s);
	}
		
	public static void setConfigValue(final String s, final Object value) {
		cfg.set(s, value);
		saveConfig();
	}	
	
	public static void saveConfig() {
		final File file = new File("plugins//NiroNoCheat//config.yml");
		try {
			cfg.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static double getCheckConfigValue(final Check check, final String value) {
		final String ValuePos = "Checks." + check.showCheckCategory() + "." + check + "." + value;
		
		if(!cfg.contains(ValuePos)) {
			try {
				final double DefaultValue = check.showValueList().get(value);
				cfg.set(ValuePos, DefaultValue);
				saveConfig();
			} catch (Exception e) {
				Commands.sendMessage(Bukkit.getConsoleSender(), "§cError while reading data: " + e.getMessage() + "(Searching " + value + " in " + check + ")");
				e.printStackTrace();
			}			
		}
		
		return cfg.getDouble(ValuePos);
	}
	
	public static void setCheckConfigValue(final Check check, final String name, final double value) {
		final String ValuePos = "Checks." + check.showCheckCategory() + "." + check + "." + name;
		
		cfg.set(ValuePos, value);
		saveConfig();
	}
	
	public static int getCheckConfigFlagLevel(final Check check) {
		final String ValuePos = "Checks." + check.showCheckCategory() + "." + check + ".Weight";
		
		if(!cfg.contains(ValuePos)) {
			final int DefaultValue = check.showFlagLevel();
			cfg.set(ValuePos, DefaultValue);
			saveConfig();
		}
		
		return cfg.getInt(ValuePos);
	}
}
