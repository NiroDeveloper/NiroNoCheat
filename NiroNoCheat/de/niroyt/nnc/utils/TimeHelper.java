package de.niroyt.nnc.utils;

public class TimeHelper {
	long lastMS = 0;

	public TimeHelper() {
		lastMS = System.currentTimeMillis();
	}
	
	public boolean isDelayComplete(final long delay) {
		if(0 > this.lastMS) {
			lastMS = System.currentTimeMillis();
		}
		
		if(delayAtTheMoment() >= delay) {
			return true;
		}
		return false;
	}

	public void setLastMS() {
		this.lastMS = System.currentTimeMillis();
	}

	public long getLastMS() {
		return lastMS;
	}
	
	public long delayAtTheMoment() {
		return System.currentTimeMillis() - this.lastMS;
	}
}
