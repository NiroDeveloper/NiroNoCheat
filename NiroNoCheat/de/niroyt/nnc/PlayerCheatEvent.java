package de.niroyt.nnc;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import de.niroyt.nnc.enums.Check;
import de.niroyt.nnc.enums.Status;

public class PlayerCheatEvent extends Event implements Cancellable {
	 
	public static HandlerList handlers = new HandlerList();
   
    Player player;
    boolean cancelled = false;
    Check check;    
    Status status;
    boolean write_in_logfile;
   
    public PlayerCheatEvent(Player player, Check check, Status status, boolean write_in_logfile){
    	this.player = player;
    	this.check = check;
    	this.status = status;
    	this.write_in_logfile = write_in_logfile;
    }
   
    public Player getPlayer() {
    	return player;
    }
   
    public static HandlerList getHandlerList(){
    	return handlers;
    }
   
    @Override
    public HandlerList getHandlers() {
    	return handlers;
    }

	@Override
	public boolean isCancelled() {
		return this.cancelled;
	}

	@Override
	public void setCancelled(boolean arg0) {
		this.cancelled = arg0;
	}
	
	public Check getCheck() {
		return check;
	}
	
	public Status getStatus() {
		return this.status;
	}
	
	public void setStatus(Status status) {
		this.status = status;
	}
	
	public boolean WriteInLogFile() {
		return this.write_in_logfile;
	}
	
	public void setWriteInLogFile(boolean write_in_logfile) {
		this.write_in_logfile = write_in_logfile;
	}
   
}