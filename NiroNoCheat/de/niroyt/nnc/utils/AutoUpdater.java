package de.niroyt.nnc.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import org.bukkit.Bukkit;

import de.niroyt.nnc.Main;
import de.niroyt.nnc.manager.Commands;

public class AutoUpdater {
	
	public static void download() {		
		try {
			final URL url = new URL("http://www.niroyt.de/NiroNoCheat/NNC.jar");
			final long bytesTotal = url.openConnection().getContentLengthLong();
			final URLConnection conn = url.openConnection();
			final InputStream is = new BufferedInputStream(conn.getInputStream());
			final OutputStream os = new BufferedOutputStream(new FileOutputStream(new File(Main.class.getProtectionDomain().getCodeSource().getLocation().getFile().replace("%20", " "))));
			final byte[] chunk = new byte[1024];
			int chunkSize;
			long bytesDownloaded = 0;
			
			Commands.sendMessage(Bukkit.getConsoleSender(), "Start downloading Update...");
			
			long LastReset = 0;
			
			while ((chunkSize = is.read(chunk)) != -1) {
				os.write(chunk, 0, chunkSize);
				bytesDownloaded += chunkSize;
				
				if(System.currentTimeMillis() - LastReset > 250) {
					LastReset = System.currentTimeMillis();
					
					String percent = String.valueOf((short)((bytesDownloaded * 100.0) / bytesTotal));
					Commands.sendMessage(Bukkit.getConsoleSender(), "Downloading Update: " + percent + "%");
				}				
			}
			os.flush(); 
			os.close();
			is.close();
			
			Commands.sendMessage(Bukkit.getConsoleSender(), "Downloading Update: 100%");
			
			Bukkit.reload();	
			
			Commands.sendMessage(Bukkit.getConsoleSender(), "Update succesfully loaded!");					
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}	
}
