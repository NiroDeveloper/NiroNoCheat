package de.niroyt.nnc.modules;

import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import de.niroyt.nnc.Main;
import de.niroyt.nnc.enums.Check;
import de.niroyt.nnc.manager.*;
import de.niroyt.nnc.utils.ConfigManager;
import de.niroyt.nnc.utils.Flag;
import de.niroyt.nnc.utils.PacketReader;

import org.bukkit.*;
import org.bukkit.entity.Player;

public class Packets extends Module {
		            
    public void onMove(final PlayerMoveEvent e) {    	
    	final Player player = e.getPlayer();
    		
        if(Manager.isOP(player)) {
        	return;
        }
        
        if(Manager.getPlayerData(player).getPacketOldFromPitch() != -1337 && Manager.getPlayerData(player).getPacketOldFromYaw() != -1337) {
        	if(Manager.getPlayerData(player).getPacketOldFromPitch() == e.getTo().getPitch() 
        			&& Manager.getPlayerData(player).getPacketOldFromYaw() == e.getTo().getYaw()
        			&& Math.max(e.getFrom().getYaw(), e.getTo().getYaw()) - Math.min(e.getFrom().getYaw(), e.getTo().getYaw()) > 20
        			&& Math.max(e.getFrom().getPitch(), e.getTo().getPitch()) - Math.min(e.getFrom().getPitch(), e.getTo().getPitch()) > 20) {
        		if(Manager.debug(player, Check.bp11, "Weird Behaviour")) {
        			Flag.backPort(player);
        		}
        	}
        }
        
        Manager.getPlayerData(player).setPacketOldFromPitch(e.getFrom().getPitch());
        Manager.getPlayerData(player).setPacketOldFromYaw(e.getFrom().getYaw());            
    }
    
    public void onTeleport(final PlayerTeleportEvent e) {
    	final Player player = e.getPlayer();
    	
    	Manager.getPlayerData(player).setPacketOldFromPitch(-1337);
        Manager.getPlayerData(player).setPacketOldFromYaw(-1337);  
    	
        Manager.getPlayerData(player).setPacketOldPitch(0);
		Manager.getPlayerData(player).setPacketOldYaw(0);
    	
		Manager.getPlayerData(player).setPacketDoNotCheck(Manager.getPlayerData(player).getPacketDoNotCheck() + 2);
    	
    	Manager.getPlayerData(player).setPacketAntiMoreMovePacketCount(Manager.getPlayerData(player).getPacketAntiMoreMovePacketCount() - 2);
    }
            
    public void onJoin(final PlayerJoinEvent e){
    	final Player player = e.getPlayer();
    	
    	Manager.getPlayerData(player).setPacketReader(new PacketReader(player));
    }
    
    public void onQuit(final PlayerQuitEvent e) {
    	final Player player = e.getPlayer();
    	    	
    	Manager.getPlayerData(player).getPacketReader().uninject();
    }
    
    public void onItemHeld(PlayerItemHeldEvent e) {
    	final Player player = e.getPlayer();
    	Manager.getPlayerData(player).setPacketPreviousItemHeld(e.getPreviousSlot());
    }
    
    public void onTick() {
    	for(final Player player : Bukkit.getOnlinePlayers()) {	
    		if(Manager.getPlayerData(player).getPacketsLagCount() > 0 && Manager.isOP(player)) {
    			//TODO player.teleport(player.getLocation());
    			
    			Manager.getPlayerData(player).setPacketsLagCount(Manager.getPlayerData(player).getPacketsLagCount() + 1);
    			
    			if(Manager.getPlayerData(player).getPacketsLagCount() == 2) {
	    			if(Manager.debug(player, Check.bp13, "")) {
	    				player.teleport(player.getLocation());
	    			}
    			}
    		}
    		
    		if(Manager.getPlayerData(player).getPacketUpdatePingTimer().isDelayComplete(1000L)) {
    			Manager.getPlayerData(player).getPacketUpdatePingTimer().setLastMS();
						    	  				
    			int pingOld = Manager.getPlayerData(player).getPing();				    	
    		    	        				
    			Manager.getPlayerData(player).setPing((Manager.getPing(player) + pingOld) / 2);	
			}	
    		
           	if(System.currentTimeMillis() - Manager.getPlayerData(player).getPacketAntiMoreMovePacketTime() > 49) {     
           		double newValue = (Manager.getPlayerData(player).getPacketAntiMoreMovePacketCount() - Double.valueOf(System.currentTimeMillis() - Manager.getPlayerData(player).getPacketAntiMoreMovePacketTime()) / 50) * 0.98;
           		           		
           		Manager.getPlayerData(player).setPacketAntiMoreMovePacketCount(newValue);
           		
           		if(Manager.getPlayerData(player).getPacketAntiMoreMovePacketCount() < -15) {
           			Manager.getPlayerData(player).setPacketAntiMoreMovePacketCount(-15);
           		}
           		
           		if(Manager.getPlayerData(player).getPacketAntiMoreMovePacketCount() > 0) {
           			Manager.getPlayerData(player).setPacketAntiMoreMovePacketCount(Manager.getPlayerData(player).getPacketAntiMoreMovePacketCount() * 0.96);
           		}
           		
           		double Value = Manager.getPlayerData(player).getPacketAntiMoreMovePacketCount();
           		int ping = Manager.ping(player);
        		
        		double maxValue = ConfigManager.getCheckConfigValue(Check.bp7, "maxValue") + (ping / 50 + Main.getServerLag() / 25) * Double.valueOf(System.currentTimeMillis() - Manager.getPlayerData(player).getPacketAntiMoreMovePacketTime()) / 50;
        		double oldMoreMovePacketCount = Manager.getPlayerData(player).getPacketAntiMoreMovePacketCount();
        		
        		if(maxValue > 10) {
        			maxValue = 10;
        		}
        		
        		if(Value >= maxValue + 0.5
        				|| (Value >= maxValue && Manager.getPlayerData(player).getPacketLastMoreMovePacketCount() + newValue >= maxValue * 2)
        				|| (Value >= maxValue && System.currentTimeMillis() - Manager.getPlayerData(player).getPacketLastOverMoreMovePacket() < 2500)) {        		
                	if(Manager.debug(player, Check.bp7, "Value: " + Double.valueOf(Math.round(newValue * 10)) / 10 + ", Serverlag: " + Main.getServerLag() + "ms, Last Value: " + Double.valueOf(Math.round(Manager.getPlayerData(player).getPacketLastMoreMovePacketCount() * 10)) / 10)) {
                		Flag.backPort(player);
                	};
                
                	Manager.getPlayerData(player).setPacketAntiMoreMovePacketCount(maxValue - 1.01);
        		}
        		
        		if(Value >= maxValue) {
        			Manager.getPlayerData(player).setPacketLastOverMoreMovePacket(System.currentTimeMillis());
        		}
        		
           		Manager.getPlayerData(player).setPacketAntiMoreMovePacketTime(System.currentTimeMillis());
           		Manager.getPlayerData(player).setPacketLastMoreMovePacketCount(oldMoreMovePacketCount);
        	}
		}  
    }
        
    public static void LookingPacket(final Player player, final float yaw, final float pitch) {    	    	
    	if(Manager.isOP(player) || player.isInsideVehicle()) {
    		return;
    	}
    	    	
    	if(Manager.getPlayerData(player).getPacketDoNotCheck() > 0) {
    		Manager.getPlayerData(player).setPacketDoNotCheck(Manager.getPlayerData(player).getPacketDoNotCheck() - 1);
    		return;
    	}
    	
    	if(Manager.getPlayerData(player).getPacketOldPitch() != 0 && Manager.getPlayerData(player).getPacketOldYaw() != 0) {
    		if(yaw == Manager.getPlayerData(player).getPacketOldYaw() && pitch == Manager.getPlayerData(player).getPacketOldPitch()) {
    			if(Manager.debug(player, Check.bp2, "")) {
    				Flag.backPort(player);
    			}
    			
    			Manager.getPlayerData(player).setPacketOldPitch(0);
    			Manager.getPlayerData(player).setPacketOldYaw(0);
    			return;
    		}
    	}
    		
    	Manager.getPlayerData(player).setPacketOldPitch(pitch);
		Manager.getPlayerData(player).setPacketOldYaw(yaw);
    }
        
    public static void PacketCount(final Player player) {
    	if(Manager.isOP(player)) {
    		return;
    	}
    	
    	int newValue = Manager.getPlayerData(player).getPacketAntiCrashCount() + 1;
    	
    	if(Manager.getPlayerData(player).getPacketAntiCrashResetTimer().isDelayComplete(1000)) {
    		Manager.getPlayerData(player).getPacketAntiCrashResetTimer().setLastMS();
    		    		
    		if(newValue > ConfigManager.getCheckConfigValue(Check.bp3, "maxValue")) {        		
        		if(Manager.debug(player, Check.bp3, newValue + " packets in 1000ms")) {
        			Flag.kick(player, Check.bp3.showCheckCategory());
        		}
        	}
    		
    		newValue = 1;
    	}
    	
    	Manager.getPlayerData(player).setPacketAntiCrashCount(newValue);
    }
}
