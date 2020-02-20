package de.niroyt.nnc.manager;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import de.niroyt.nnc.Main;
import de.niroyt.nnc.enums.Check;
import de.niroyt.nnc.enums.Messages;
import de.niroyt.nnc.utils.AutoUpdater;
import de.niroyt.nnc.utils.ConfigManager;

public class Commands {

	public static boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
		if(!cmd.getName().equalsIgnoreCase("nnc")) {		
			return false;
		}
		
		if(args.length == 0) {
			if((sender.isOp() || sender.hasPermission("nnc.options")) && !(sender instanceof ConsoleCommandSender)) {
				Main.openInv((Player) sender);
			} else {
				sendMessage(sender, "§3NiroNoCheat §7" + Main.version + " is here!");
			}				
			return true;		
		} else if(args[0].equalsIgnoreCase("reload")) {
			if(sender instanceof ConsoleCommandSender || sender.isOp() || sender.hasPermission("nnc.reload")) {
				new Thread(new Runnable() {						
					@Override
					public void run() {						
						ConfigManager.loadSettings();
						
						if(!Main.getLoginSystem().Login()) {
							System.exit(1);
						}
						
						if(Main.isVip()) {
							String config = Main.getLoginSystem().getOnlineConfig();
							if(config != null) {
								ConfigManager.setConfig(config);
							}					
						}
						
						final boolean isUsingAutoUpdater = ConfigManager.getConfigBoolean("Auto-Updater");
							
						if(Main.getLoginSystem().existUpdate() && Main.getLoginSystem().isLogin() && Main.getLoginSystem().isVIP() && isUsingAutoUpdater) {
							
							AutoUpdater.download();
							return;
						} else if(!Main.getLoginSystem().isVIP() && Main.getLoginSystem().existUpdate() && isUsingAutoUpdater) {
							Commands.sendMessage(Bukkit.getConsoleSender(), "§cAuto Updater is a VIP feature!");
						}
						
						if(Main.getLoginSystem().isVIP()) {
							for(Messages m : Messages.values()) {
								ConfigManager.getMessage(m);
							}
						}
						
						sendMessage(sender, ConfigManager.getMessage(Messages.PLUGIN_RELOAD));
					}
				}).start();
				
				return true;
			} else {
				sendMessage(sender, ConfigManager.getMessage(Messages.PLAYER_NO_PERMISSION));
			}
		} else if(args[0].equalsIgnoreCase("velocity")) {
			if(sender.isOp() || sender.hasPermission("nnc.options")) {
				if(args.length == 4) {
					try {
						((Player)sender).setVelocity(new Vector(Double.valueOf(args[1]), Double.valueOf(args[2]), Double.valueOf(args[3])));
					} catch (Exception e) {
						sendMessage(sender, "Error while executing!");
					}
				}				
				
				return true;
			} else {
				sendMessage(sender, ConfigManager.getMessage(Messages.PLAYER_NO_PERMISSION));
			}
		} else if(args[0].equalsIgnoreCase("disable")) {
			if(sender instanceof ConsoleCommandSender || sender.isOp() || sender.hasPermission("nnc.options")) {
				Main.setDISABLED(true);
				sendMessage(sender, ConfigManager.getMessage(Messages.PLUGIN_DISABLED));						
				
				return true;
			} else {
				sendMessage(sender, ConfigManager.getMessage(Messages.PLAYER_NO_PERMISSION));
			}
		} else if(args[0].equalsIgnoreCase("enable")) {
			if(sender instanceof ConsoleCommandSender || sender.isOp() || sender.hasPermission("nnc.options")) {
				Main.setDISABLED(false);
				sendMessage(sender, ConfigManager.getMessage(Messages.PLUGIN_ENABLED));
				
				return true;
			} else {
				sendMessage(sender, ConfigManager.getMessage(Messages.PLAYER_NO_PERMISSION));
			}
		} else if(args[0].equalsIgnoreCase("ping")) {
			if(sender instanceof ConsoleCommandSender) {
				sendMessage(sender, "Your Ping ... this is the console!?");
			} else {
				Player player = (Player) sender;
				
				sendMessage(sender, ConfigManager.getMessage(Messages.PLAYER_PING).replace("[ping]", String.valueOf(Manager.ping(player))));
			}	
			return true;
		} else if(args[0].equalsIgnoreCase("test") && ((args.length == 2 && !(sender instanceof ConsoleCommandSender)) || args.length == 3)) {
			Player player = null;
			
			if(args.length == 2) {
				player = ((Player)sender);
			}
				
			if(args.length == 3) {
				if(Bukkit.getPlayer(args[2]) == null) {
					sendMessage(sender, "This player could not be found!");
				} else if(!Bukkit.getPlayer(args[2]).isOnline()) {
					sendMessage(sender, "This player is not online!");
				} else {
					player = Bukkit.getPlayer(args[2]);
				}
			}
			
			if(player == null) {
				return true;
			}
						
			if(!(sender instanceof ConsoleCommandSender)) {
				if(!((Player)sender).hasPermission("nnc.options")) {
					sendMessage(sender, ConfigManager.getMessage(Messages.PLAYER_NO_PERMISSION));
					return true;
				}
			}
				
			if(args[1].equalsIgnoreCase("stop")) {
				if(Manager.getPlayerData(player).getTestingCheck() != null) {
					Check check = Manager.getPlayerData(player).getTestingCheck();
					
					Manager.getPlayerData(player).setTestingCheck(null);
					
					sendMessage(sender, "Testing §3[" + check + "]§7 for §3" + player.getName() + "§7 stopped!");
				} else {
					sendMessage(sender, "§3" + player.getName() + "§7 are not testing any check!");
				}
			} else {
				if(Manager.getPlayerData(player).getTestingCheck() != null) {
					sendMessage(sender, "§3" + player.getName() + "§7 are already testing §3[" + Manager.getPlayerData(player).getTestingCheck() + "]§7! '/nnc test stop' to cancel!");
						
					return true;
				}
				
				Check check = null;				
				for(final Check cc : Check.values()) {
					if(cc.toString().equalsIgnoreCase(args[1])) {
						check = Check.valueOf(args[1]);
					}
				}
					
				if(check == null) {
					sendMessage(sender, "This Check is not existing!");
				} else {
					Manager.getPlayerData(player).setTestingCheck(check);
						
					sendMessage(sender, "§3" + player.getName() + "§7 is now testing §3[" + Manager.getPlayerData(player).getTestingCheck() + "]§7!");
				}
			}

			return true;
		} else {
			if(sender instanceof ConsoleCommandSender || sender.isOp() || sender.hasPermission("nnc.options")) {
				sendCommandInfo(sender);
			} else {
				sendMessage(sender, "§3NiroNoCheat §7" + Main.version + " is here!");
			}
		}
		return false;
	}
	
	public static void sendCommandInfo(final CommandSender sender) {
		sendMessage(sender, "§7------------------------");
		sendMessage(sender, " §3NiroNoCheat §7" + Main.version);	
		sendMessage(sender, "");	
		sendMessage(sender, " §3/nnc");
		sendMessage(sender, " §7Open the settings GUI");
		sendMessage(sender, "");	
		sendMessage(sender, " §3/nnc enable");
		sendMessage(sender, " §7Enable NNC");
		sendMessage(sender, "");	
		sendMessage(sender, " §3/nnc disable");
		sendMessage(sender, " §7Disable NNC");
		sendMessage(sender, "");	
		sendMessage(sender, " §3/nnc ping");
		sendMessage(sender, " §7Current ping");
		sendMessage(sender, "");	
		sendMessage(sender, " §3/nnc reload");
		sendMessage(sender, " §7Reload the NNC config");
		sendMessage(sender, "");	
		sendMessage(sender, " §3/nnc test ([Check]/stop) [Player]");
		sendMessage(sender, " §7Only this check will flag you");
		sendMessage(sender, "");	
		sendMessage(sender, "§7------------------------");
	}
	
	public static void sendMessage(final CommandSender sender, String message) {
		message = ConfigManager.getMessage(Messages.PREFIX) + message;
		if(sender instanceof ConsoleCommandSender) {
			Bukkit.getConsoleSender().sendMessage(message);
		} else {
			((Player) sender).sendMessage(message);
		}
	}	
}
