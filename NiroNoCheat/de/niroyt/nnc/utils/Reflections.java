package de.niroyt.nnc.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import de.niroyt.nnc.Main;

public class Reflections {

	public static void setValue(final Object obj, final String name, final Object value){
        try{
        	final Field field = obj.getClass().getDeclaredField(name);
	        field.setAccessible(true);
	        field.set(obj, value);
	    } catch(Exception e) {}
	}
	
	public static Object getValue(final Object obj, final String name){
		try{
			final Field field = obj.getClass().getDeclaredField(name);
	        field.setAccessible(true);
	        
	        return field.get(obj);
		} catch(Exception e) {}
		
		return null;
	}

	public static void sendPacket(final Object packet, final Player BukkitPlayer) {		
		try {
			final Object player = Class.forName("org.bukkit.craftbukkit." + Main.MinecraftVersion + ".entity.CraftPlayer").cast(BukkitPlayer);
			final Method handle = player.getClass().getMethod("getHandle", new Class[0]);
			final Object entityPlayer = handle.invoke(player, new Object[0]);
            
			final Field playerConnection = entityPlayer.getClass().getField("playerConnection");
			final Object playerConnection2 = playerConnection.get(entityPlayer);

            playerConnection2.getClass()
            	.getMethod("sendPacket", Class.forName("net.minecraft.server." + Main.MinecraftVersion + ".Packet"))
            	.invoke(playerConnection2, packet);            	            
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchFieldException | SecurityException | NoSuchMethodException | ClassNotFoundException e1) {
			e1.printStackTrace();
		}
	}
	
	public static void sendPacket(final Object packet){
		for(final Player player : Bukkit.getOnlinePlayers()){
			sendPacket(packet, player);
	    }
	}
	
	public static void sendPacketWithoutPlayer(final Object packet, final Player p){
		for(final Player player : Bukkit.getOnlinePlayers()){
			if(player != p) {
				sendPacket(packet, player);
			}
	    }
	}
}
