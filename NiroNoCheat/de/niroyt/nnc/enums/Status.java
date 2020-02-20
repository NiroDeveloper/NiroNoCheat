package de.niroyt.nnc.enums;


public enum Status {

	KICK(2), BAN(3), CANCEL(1), NOTHING(0);
	
	int aggresivity;	
	Status(int aggresivity) {
		this.aggresivity = aggresivity;
	}
	
	public int getAggresivity() {
		return this.aggresivity;
	}
	
}
