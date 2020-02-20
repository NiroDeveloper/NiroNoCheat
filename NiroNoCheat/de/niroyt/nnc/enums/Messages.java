package de.niroyt.nnc.enums;


public enum Messages {

	PREFIX("&3[NNC] &7"),
	
	PLUGIN_RELOAD("&2Plugin reloaded!"),
	PLUGIN_DISABLED("&cPlugin disabled!"),
	PLUGIN_ENABLED("&aPlugin enabled!"),
	
	PLAYER_NO_PERMISSION("You haven't any permission to do this!"),
	PLAYER_PING("Your Ping: &3[ping]ms"),
	
	FLAG_MESSAGE("&7[player]&f was detected with a &7[cheat]&f Hack! [info]"),
	KICK_MESSAGE("&cYou were kicked for Cheating!"),
	BAN_MESSAGE("&cYou were banned for Cheating!");
	
	String DefaultMessage;
	Messages(String DefaultMessage) {
		this.DefaultMessage = DefaultMessage;
	}
	public String getDefaultMessage() {
		return DefaultMessage;
	}	
}
