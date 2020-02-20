package de.niroyt.nnc.manager;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;
import org.bukkit.event.player.PlayerQuitEvent;
import de.niroyt.nnc.Main;
import de.niroyt.nnc.PlayerCheatEvent;
import de.niroyt.nnc.enums.Check;
import de.niroyt.nnc.enums.CheckCategory;
import de.niroyt.nnc.enums.Messages;
import de.niroyt.nnc.enums.Status;
import de.niroyt.nnc.utils.ConfigManager;
import de.niroyt.nnc.utils.Flag;
import de.niroyt.nnc.utils.GroundUtils;
import de.niroyt.nnc.utils.LogFile;

public class Manager implements Listener {
  
	private static HashMap<String, PlayerData> PlayerDatas = new HashMap<String, PlayerData>();
	
	public static PlayerData getPlayerData(final Player player) {
		if(!PlayerDatas.containsKey(player.getName())) {
			PlayerDatas.put(player.getName(), new PlayerData(player));
		}
		
		return PlayerDatas.get(player.getName());
	}
	    
    public static boolean debug(final Player player, final Check check, final String value) {       	    	
    	if(!getPlayerData(player).isNormalyOnline()) {
    		return false;
    	}
    	
    	if(!player.isOnline()) {
    		return false;
    	}
    	
    	if(!ConfigManager.isCheckEnabled(check)) {
    		return false;
    	}
    	
    	if(Manager.getPlayerData(player).getTestingCheck() != check && Manager.getPlayerData(player).getTestingCheck() != null) {
    		return false;
   		}
   	
    	getPlayerData(player).setFlagLevel(getPlayerData(player).getFlagLevel() + ConfigManager.getCheckConfigFlagLevel(check));
    	
    	CheckCategory m = check.showCheckCategory();
    	
    	String checkString = "Check: [" + check.toString() + "]";
    	String ping = "Ping: " + pingString(player);
    	
    	String status = "Status: NONE";
    	String level = "VL: " + getPlayerData(player).getFlagLevel();
    	    	    	
    	final PlayerCheatEvent Event = new PlayerCheatEvent(player, check, ConfigManager.getMeasure(check), ConfigManager.getConfigBoolean("Write-Log") && getPlayerData(player).getFlagLevel() >= 20);
    	
    	if(Main.isVip()) {
    		Bukkit.getPluginManager().callEvent(Event);
    	}    	
    	
    	if(Event.isCancelled()) {
    		return false;
    	}
    	
    	Status s = Event.getStatus();
    	    	
    	if(getPlayerData(player).getFlagLevel() >= ConfigManager.cfg.getInt("Kick-at-Violation-Level") 
    			&& s != Status.BAN 
    			&& ConfigManager.cfg.getInt("Kick-at-Violation-Level") > 0) {
    		s = Status.KICK;
    	}
    	    	
    	if(s == Status.BAN) {
    		status = "Status: Banned";
    	} else if(Status.KICK == s) {    			
	       	status = "Status: Kicked";
    	} else if(s == Status.CANCEL) {
	       	status = "Status: CANCEL";
    	}  
    	
    	String info = "";    	
    	if(ConfigManager.debugMode()) {
    		info = value + (value.length() > 1 ? ", " : "") + checkString + ", " + status + ", " + level + ", " + ping;
    	}
    	
    	final String message = ConfigManager.getMessage(Messages.PREFIX) + ConfigManager.getMessage(Messages.FLAG_MESSAGE).replace("[info]", info).replace("[player]", player.getName()).replace("[cheat]", m.toString());
    	boolean sendMessage = false;    	
    	
    	for(Player p : Bukkit.getOnlinePlayers()) {
    		if(((p.hasPermission("nnc.debug") || p.isOp()) 
    				&& (getPlayerData(player).getFlagLevel() >= 25|| ConfigManager.debugMode())
    				&& (getPlayerData(player).getLastMessage().isDelayComplete(1000L) || ConfigManager.debugMode())
    				&& ConfigManager.getConfigBoolean("Violation-Messages")) ||
    				(ConfigManager.debugMode() && player == p)) {
    			p.sendMessage(message);
    			sendMessage = true;
    		}
    	}    
    	
    	if(sendMessage) {
    		getPlayerData(player).getLastMessage().setLastMS();
    	}
    	
    	if(Main.isDisabled()) {
    		return false;
    	}
    	
    	getPlayerData(player).setLastFlag(System.currentTimeMillis());
    	
    	if((Status.KICK == s || Status.BAN == s) && ConfigManager.getConfigBoolean("Violation-Messages")) {
    		Bukkit.getConsoleSender().sendMessage(message);
    	}
    	
    	if(s == Status.BAN) {	
    		getPlayerData(player).setNormalyOnline(false);
    		
    		Flag.ban(player); 
		} else if(s == Status.KICK) {
			getPlayerData(player).setNormalyOnline(false);
			
			Flag.kick(player, m);
		} 
    	
    	if(Event.WriteInLogFile()) {    		
    		if(getPlayerData(player).getLastLog().isDelayComplete(60L)) {
    			getPlayerData(player).getLastLog().setLastMS();
    			
    			final String Value = value;
    			final Status Status = s;
    			final String Ping = ping;
    			Thread t = new Thread() {
    				public void run() {
    					try {
							LogFile.addLine(player, m + ", " + (Value.length() == 0 ? "" : Value + ", ") + checkString + ", " + Status + ", FL: " + getPlayerData(player).getFlagLevel() + ", " + Ping);
						} catch (IOException e) {
							e.printStackTrace();
						}
    				}
				};
				t.setName("NiroNoCheatLog-" + System.currentTimeMillis() + "-" + System.nanoTime());
				t.start();
    		}    		
    	}
    	    	
    	return true;
    }
    
    public static String pingString(final Player player) {
    	return ping(player) + "ms";
    }
    
    public static int ping(final Player player) {
    	return getPlayerData(player).getPing();
    }
    
    public static boolean AllowedSpeedFly(final Player player) {        	
    	if(player.isFlying() || isOP(player) || GroundUtils.wasFlying(player) || player.isInsideVehicle()) {
            return true;
        }
    	
    	return false;
    }
    
    public static boolean isOP(final Player player) {
    	if(Manager.getPlayerData(player).getTestingCheck() != null && Manager.getPlayerData(player).getLastMovePacketTime() != 0) {
    		return false;
    	}
    	
    	if(player.isOp() || player.hasPermission("nnc.checknot") || Manager.getPlayerData(player).getLastMovePacketTime() == 0
    			|| player.getLocation().getPitch() == 88.1337) {
            return true;
        }
    	
    	return false;
    }
        
    @EventHandler
    public void onJoin(final PlayerJoinEvent e) {
    	final Player player = e.getPlayer();
    	final String playername = player.getName();
    	
    	if(!PlayerDatas.containsKey(playername)) {
			PlayerDatas.put(playername, new PlayerData(player));
		}
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onQuit(final PlayerQuitEvent e) {
    	final Player player = e.getPlayer();
    	final String playername = player.getName();
    	
    	if(PlayerDatas.containsKey(playername)) {
			PlayerDatas.remove(playername);
		}
    }
    
    @EventHandler (priority = EventPriority.HIGHEST)
    public void Login(PlayerLoginEvent e){
        String bm = ConfigManager.getMessage(Messages.PREFIX) + ConfigManager.getMessage(Messages.BAN_MESSAGE);
        if(e.getResult() == Result.KICK_BANNED && e.getKickMessage().contains("Cheating")) {
        	e.setKickMessage(bm);
        }
    }
    
    public static int getPing(Player player) {
    	int pingAtTheMoment = 0;
		try {
			final Class<?> craftPlayer = Class.forName("org.bukkit.craftbukkit." + Main.MinecraftVersion + ".entity.CraftPlayer");
			final Object converted = craftPlayer.cast(player);
			final Method handle = converted.getClass().getMethod("getHandle", new Class[0]);
			final Object entityPlayer = handle.invoke(converted, new Object[0]);
			final Field pingField = entityPlayer.getClass().getField("ping");
			pingAtTheMoment = pingField.getInt(entityPlayer);
		} catch (ClassNotFoundException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchFieldException | SecurityException | NoSuchMethodException e1) {
			e1.printStackTrace();
		}
		
		return pingAtTheMoment;
    }
}
