package de.niroyt.nnc.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;

import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

import de.niroyt.nnc.Main;
import de.niroyt.nnc.manager.Commands;

public class LoginSystem {

	private boolean login = false;
	private boolean VIP = false;
	private boolean update = false;
	
	private String username = null;
	
	public LoginSystem() {}
	
	public boolean Login() {
		final File file = new File(Main.getInstance().getDataFolder() + "//login.yml");
		if(!file.exists()) {
			try {
				file.createNewFile();
				
				FileUtils.writeStringToFile(file, "username: \"\"\npassword: \"\"");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		final YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);		
		username = cfg.getString("username");
		final String password = cfg.getString("password");
		final String ip = null;
		String line = null;
		
		try {
			// okay, that's secret
			
			line = response.toString();
		} catch (IOException e1) {
			Commands.sendMessage(Bukkit.getConsoleSender(), "No connection to the Webspace! Error: " + e1.getMessage());
			login = true;
			VIP = false;
			return true;
		} catch (Exception e2) {	
			Commands.sendMessage(Bukkit.getConsoleSender(), "No connection to the Webspace! Error: " + e2.getMessage());
			login = true;
			VIP = false;
			return true;
		}
		if(line == null) {
			Commands.sendMessage(Bukkit.getConsoleSender(), "No connection to the Webspace!");
			login = true;
			VIP = false;
			return true;
		}
		
		if(line.equalsIgnoreCase("No Access")) {
			Commands.sendMessage(Bukkit.getConsoleSender(), "Too many login trys! Please try again later!");
			login = false;
			return false;
		}
		
		if(line.split("-").length != 3 && line.split("-").length != 4) {
			Commands.sendMessage(Bukkit.getConsoleSender(), "Invalid answer: " + line);
			login = false;
			VIP = false;
			return false;
		}
		
		if(line.split("-")[0].equalsIgnoreCase("true")) {
			Commands.sendMessage(Bukkit.getConsoleSender(), "Successfully logged in!");
			login = true;
		} else {
			Commands.sendMessage(Bukkit.getConsoleSender(), "Login failed! More information on www.niroyt.de/NiroNoCheat/Main.php");
			login = false;
		}
		
		if(line.split("-")[1].equalsIgnoreCase("true")) {
			Commands.sendMessage(Bukkit.getConsoleSender(), "§2VIP version activated! §7Thanks for your purchase!");
			VIP = true;
		} else {
			VIP = false;
		}
		
		if(line.split("-")[2].equalsIgnoreCase("true")) {
			Commands.sendMessage(Bukkit.getConsoleSender(), "§cA new version is available!");
			
			update = true;
		}
		
		if(line.split("-").length == 4) {
			String message = line.split("-")[3].split(":")[1];
			if(message.length() != 0) {
				Commands.sendMessage(Bukkit.getConsoleSender(), message);
			}
			
			return !Boolean.valueOf(line.split("-")[3].split(":")[0]);
		}
		
		return true;
	}
	
	public static String getHWID() throws Exception  {        
		final String original = System.getProperty("os.name") + System.getProperty("os.arch") + System.getenv("PROCESSOR_IDENTIFIER");
		
		final  MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(original.getBytes());
		final byte[] digest = md.digest();
		final StringBuffer sb = new StringBuffer();
		for (byte b : digest) {
			sb.append(String.format("%02x", b & 0xff));
		}
		
		return sb.toString();
    }

	public boolean isLogin() {
		return login;
	}

	public boolean isVIP() {
		return VIP;
	}
	
	public boolean existUpdate() {
		return update;
	}
	
	public String getOnlineConfig() {
		if(!isVIP()) {
			return null;
		}
			
		Commands.sendMessage(Bukkit.getConsoleSender(), "Downloading online config...");
		
		String line = null;
		try {
			final URL url = new URL("http://niroyt.de/NiroNoCheat/ConfigDownload.php");
			
			final HttpURLConnection con = (HttpURLConnection) url.openConnection();

			con.setRequestMethod("GET");	
			con.addRequestProperty("Referer", Bukkit.getIp() + ":" + Bukkit.getPort());
			con.setRequestProperty("User-Agent", username);
					
			final int responseCode = con.getResponseCode();

			final BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			final StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
			
			line = response.toString();
		} catch (IOException e1) {
			Commands.sendMessage(Bukkit.getConsoleSender(), "No connection to the Webspace! Error: " + e1.getMessage());
			return null;
		} catch (Exception e2) {	
			Commands.sendMessage(Bukkit.getConsoleSender(), "No connection to the Webspace! Error: " + e2.getMessage());
			return null;
		}
		if(line == null) {
			Commands.sendMessage(Bukkit.getConsoleSender(), "No connection to the Webspace!");
			return null;
		}	
			
		if(line.equalsIgnoreCase("OFF")) {
			Commands.sendMessage(Bukkit.getConsoleSender(), "Online config is deactivated!");
			return null;
		}
				
		line = new String(Base64.getDecoder().decode(line), StandardCharsets.UTF_8);
				
		return line;
	}
	
}
