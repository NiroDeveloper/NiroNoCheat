package de.niroyt.nnc;

import de.niroyt.nnc.enums.Messages;
import de.niroyt.nnc.gui.EventsForGui;
import de.niroyt.nnc.gui.GuiMain;
import de.niroyt.nnc.manager.*;
import de.niroyt.nnc.modules.*;
import de.niroyt.nnc.modules.World;
import de.niroyt.nnc.utils.AutoUpdater;
import de.niroyt.nnc.utils.ConfigManager;
import de.niroyt.nnc.utils.GroundUtils;
import de.niroyt.nnc.utils.LoginSystem;
import de.niroyt.nnc.utils.PacketReader;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.*;
import org.bukkit.plugin.*;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.command.*;

public class Main extends JavaPlugin implements Listener {
	
	// DEVELOPED BY NIROYT 
	
	private static Plugin plugin;
	public static String version = null;
	
	public static HashMap<UUID, GuiMain> OptionGui = new HashMap<UUID, GuiMain>();
	public static String MinecraftVersion = "";	
	
	public static boolean DISABLED = false;
	
	private static LoginSystem LS = new LoginSystem();
	
	public static List<Module> moduleList = Arrays.asList(new Module[] {});
	public static EventCaller events;
		
	public static void setDISABLED(final boolean dISABLED) {
		DISABLED = dISABLED;
		ConfigManager.setConfigValue("Disabled", dISABLED);
	}
	
	public static boolean isDisabled() {
		return DISABLED;
	}
	
	public void onEnable() {
		plugin = this;	
		
		version = "v" + getInstance().getDescription().getVersion();
		
		MinecraftVersion = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];
			
		registerEvent();
		loadConfig();
		registerModules();	
		
		Thread start = new Thread(new Runnable() {			
			@Override
			public void run() {
				if(!LS.Login()) {
					return;
				}
				
				if(isVip()) {
					String config = LS.getOnlineConfig();
					if(config != null) {
						ConfigManager.setConfig(config);
					}					
				}
				
				final boolean isUsingAutoUpdater = ConfigManager.getConfigBoolean("Auto-Updater");
					
				if(LS.existUpdate() && LS.isLogin() && LS.isVIP() && isUsingAutoUpdater) {
					AutoUpdater.download();
					return;
				} else if(!LS.isVIP() && LS.existUpdate() && isUsingAutoUpdater) {
					Commands.sendMessage(Bukkit.getConsoleSender(), "§cAuto Updater is a VIP feature!");
				}
				
				if(LS.isVIP()) {
					for(Messages m : Messages.values()) {
						ConfigManager.getMessage(m);
					}
				}
								
				events = new EventCaller();
				
				for(final Player player : Bukkit.getOnlinePlayers()) {
					try {
						Manager.getPlayerData(player).setPacketReader(new PacketReader(player));
					} catch (Exception e) {
						e.printStackTrace();
					}					
				}
				
				Commands.sendMessage(Bukkit.getConsoleSender(), "Minecraft version: " + MinecraftVersion + (OptimizedVersion() ? "" : "! This version is not optimized! Some Checks will be work not correctly! Please update NNC or change to another spigot version, more information on the spigot site of NNC!"));
			}
		});		
		
		start.setName("NNC-Start-Thread");
		start.start();
					
		Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
			@Override
			public void run() {
				for(final Module m : Main.moduleList) {
					m.onTick();
				}
				
				Tick();
			}
		}, 20L, 1L);
	}
		
	public static boolean OptimizedVersion() {
		if(MinecraftVersion.equals("v1_8_R3") 
				|| MinecraftVersion.equals("v1_9_R2")
				|| MinecraftVersion.equals("v1_11_R1")
				|| MinecraftVersion.equals("v1_12_R1")) {
			return true;
		}
		
		return false;
	}
	
	public void onDisable() {		
		if(!LS.isLogin()) {
			return;
		}
		
		for(final Player player : Bukkit.getOnlinePlayers()) {
			if(OptionGui.containsKey(player.getUniqueId())) {
				player.closeInventory();
			}
			
			try {
				Manager.getPlayerData(player).getPacketReader().uninject();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
	}
	
	public void registerModules() {
		moduleList = new ArrayList<Module>(Arrays.asList(
			new FastBow(),
			new FastHeal(),
			new Fly(),
			new Speed1(),
			new Speed2(),
			new Speed3(),
			new Sprint(),
			new Reach(),
			new Killaura(),
			new GroundUtils(),
			new NoSlowdown(),
			new Packets(),
			new ChestStealer(),
			new GhostHand(),
			new FastBreak(),
			new World(),
			new FastUse(),
			new Strafe(),
			new FastPlace(),
			new Safewalk(),
			new NoSwing(),		
			new NoFallDamage(),
			new Inventory(),
			new NoClip(),
			new Phase(),
			new ESP(),
			new Velocity()
		));
		
		if(getServer().getPluginManager().getPlugin("ProtocolLib") != null) {
			moduleList.add(new DamageIndicator());
		} else {
			Commands.sendMessage(Bukkit.getConsoleSender(), "ProtocolLib is not installed, AntiDamageIndicator/AntiEntityESP/AntiItemESP will not work!");
		}
	}
	
	public static Plugin getInstance() {
		return plugin;
	}
	
	public void registerEvent() {
		Bukkit.getPluginManager().registerEvents(this, plugin);
		Bukkit.getPluginManager().registerEvents(new EventsForGui(), plugin);		
		Bukkit.getPluginManager().registerEvents(new Manager(), plugin);
	}

	public static String getVersion() {
		return version;
	}
	
	public static void openInv(final Player player) {		
		if(OptionGui.containsKey(player.getUniqueId())) {
			while(!OptionGui.get(player.getUniqueId()).closeInv()) {}
			
			OptionGui.remove(player.getUniqueId());
		}
		
		OptionGui.put(player.getUniqueId(), new GuiMain(player));
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {		
		return Commands.onCommand(sender, cmd, label, args);
	}
	
	public void loadConfig() {	
		getConfig().options().copyDefaults(true);
		saveConfig();
		
		ConfigManager.loadSettings();
		
		setDISABLED(ConfigManager.getConfigBoolean("Disabled"));
	}
	
	private static long TimeBehind = 0;			
	private static long LastTimeBehind = 0;
	public static long LastTimeReset = 0;
	
	public static void Tick() {	
		long newTimeBehind = (System.nanoTime() - LastTimeBehind - 50000000) / 1000000;
		
		if(newTimeBehind < 0) {
			newTimeBehind = 0;
		}
		
		if(LastTimeBehind == 0) {
			newTimeBehind = 0;
		}
			
		if(ConfigManager.debugMode() 
				&& newTimeBehind > 75
				&& newTimeBehind >= TimeBehind) {
			for(Player p : Bukkit.getOnlinePlayers()) {
				Commands.sendMessage(p, "Server is lagging! " + (newTimeBehind) + "ms behind");
			}
			
			LastTimeReset = System.currentTimeMillis();
		}
		
		if(newTimeBehind > TimeBehind) {
			TimeBehind = newTimeBehind;
			
			if(newTimeBehind > 75) {
				LastTimeReset = System.currentTimeMillis();
			}
		} else if(System.currentTimeMillis() - LastTimeReset > 1000) {
			TimeBehind = (TimeBehind * 5 + newTimeBehind) / 6;
		}	

		LastTimeBehind = System.nanoTime();
		
		for(final Player player : Bukkit.getOnlinePlayers()) {
    		if(System.currentTimeMillis() - Manager.getPlayerData(player).getLastFlag() > 2500) {
    			Manager.getPlayerData(player).setFlagLevel((int) (Manager.getPlayerData(player).getFlagLevel() / 1.03)); 
			}	
    	}
    	
    	for(final Player player : Bukkit.getOnlinePlayers()) {
    		if(player.isOnline() && !Manager.getPlayerData(player).isNormalyOnline()) {
    			player.kickPlayer("");
    		}
    	}
	}
	
	public static int getServerLag() {
		long newTimeBehind = (System.nanoTime() - LastTimeBehind - 50000000) / 1000000;
				
		if(newTimeBehind > TimeBehind) {
			TimeBehind = newTimeBehind;
			
			if(newTimeBehind > 75) {
				LastTimeReset = System.currentTimeMillis();
			}
		}
		
		return (int) TimeBehind;
	}
	
	public static boolean isVip() {
		return LS.isVIP();
	}
	
	public static LoginSystem getLoginSystem() {
		return LS;
	}
}

