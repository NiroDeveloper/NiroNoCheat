package de.niroyt.nnc.enums;

import java.util.HashMap;

public enum Check {
		
	bp2(CheckCategory.FALSE_PACKETS, 12, Status.CANCEL, new HashMap<String, Double>()),
	bp3(CheckCategory.MORE_PACKETS, 100, Status.KICK, new HashMap<String, Double>() {{ put("maxValue", 150.0); }}),
	bp5(CheckCategory.FALSE_PACKETS, 25, Status.CANCEL, new HashMap<String, Double>() {{ put("minValue", 4.95); }}),
	bp6(CheckCategory.FALSE_PACKETS, 20, Status.KICK, new HashMap<String, Double>()),
	bp7(CheckCategory.MORE_PACKETS, 16, Status.CANCEL, new HashMap<String, Double>() {{ put("maxValue", 0.88); }}),
	bp8(CheckCategory.PACKETS_PING_SPOOF, 4, Status.CANCEL, new HashMap<String, Double>() {{ put("maxDifference", 100.0); put("1-8-x-Multiply", 0.4); }}),
	bp9(CheckCategory.PACKETS_PING_SPOOF, 4, Status.CANCEL, new HashMap<String, Double>() {{ put("maxDifference", 100.0); put("1-8-x-Multiply", 0.4);}}),
	bp11(CheckCategory.FALSE_PACKETS, 12, Status.CANCEL, new HashMap<String, Double>()),
	bp13(CheckCategory.FALSE_PACKETS, 10, Status.NOTHING, new HashMap<String, Double>()),
	
	cs1(CheckCategory.PLAYER_CHESTSTEALER, 15, Status.CANCEL, new HashMap<String, Double>() {{ put("maxValue", 3.0); }}),
	cs2(CheckCategory.PLAYER_CHESTSTEALER, 12, Status.CANCEL, new HashMap<String, Double>() {{ put("minValue", 120.0); }}),
			
	fa1(CheckCategory.FALSE_ANGLE, 22, Status.KICK, new HashMap<String, Double>()),
	fa2(CheckCategory.FALSE_ANGLE, 22, Status.KICK, new HashMap<String, Double>()),
	fa3(CheckCategory.FALSE_ANGLE, 10, Status.CANCEL, new HashMap<String, Double>()),
	fa4(CheckCategory.FALSE_ANGLE, 14, Status.CANCEL, new HashMap<String, Double>()),
	fa5(CheckCategory.FALSE_ANGLE, 14, Status.CANCEL, new HashMap<String, Double>()),	
	fa6(CheckCategory.FALSE_ANGLE, 30, Status.KICK, new HashMap<String, Double>()),
	fa7(CheckCategory.FALSE_PACKETS, 12, Status.CANCEL, new HashMap<String, Double>() {{ put("minValue", 1.0); }}),
	
	fb1(CheckCategory.PLAYER_FASTBOW, 14, Status.CANCEL, new HashMap<String, Double>() {{ put("minValue", 950.0); }}),
	fb2(CheckCategory.PLAYER_FASTBOW, 14, Status.CANCEL, new HashMap<String, Double>() {{ put("minValue", 700.0); }}),
	
	fbr1(CheckCategory.PLAYER_INTERACT, 12, Status.CANCEL, new HashMap<String, Double>()),
	fbr2(CheckCategory.PLAYER_FASTBREAK, 18, Status.CANCEL, new HashMap<String, Double>()),
	fbr3(CheckCategory.PLAYER_FASTBREAK, 30, Status.KICK, new HashMap<String, Double>()),
	
	fbp1(CheckCategory.PLAYER_FASTPLACE, 10, Status.CANCEL,  new HashMap<String, Double>() {{ put("maxValue", 4.0); }}),
	
	fh1(CheckCategory.PLAYER_FASTHEAL, 20, Status.KICK, new HashMap<String, Double>()),
	
	f(CheckCategory.MOVING_FLY, 10, Status.CANCEL, new HashMap<String, Double>()),
	
	fu1(CheckCategory.PLAYER_FASTUSE, 15, Status.CANCEL, new HashMap<String, Double>() {{ put("minValue", 1490.0); }}),
	
	gh1(CheckCategory.PLAYER_GHOSTHAND, 24, Status.KICK, new HashMap<String, Double>() {{ put("maxValue", 7.0); }}),
	gh2(CheckCategory.PLAYER_GHOSTHAND, 18, Status.CANCEL, new HashMap<String, Double>() {{ put("maxValue", 4.0); }}),
		
	im1(CheckCategory.INVENTORY_MOVE, 18, Status.CANCEL, new HashMap<String, Double>() {{ put("maxSpeed", 0.05); put("maxFlags", 3.0); }}),
	im2(CheckCategory.INVENTORY_INTERACT, 20, Status.BAN, new HashMap<String, Double>()),
	im3(CheckCategory.INVENTORY_ATTACK, 30, Status.KICK,  new HashMap<String, Double>()),
	im4(CheckCategory.INVENTORY_MOVE, 25, Status.BAN, new HashMap<String, Double>()),
	im5(CheckCategory.INVENTORY_ATTACK, 20, Status.KICK, new HashMap<String, Double>()),
	im6(CheckCategory.INVENTORY_MOVE, 16, Status.KICK, new HashMap<String, Double>()),
	
	j1(CheckCategory.MOVING_WATERWALK, 20, Status.CANCEL, new HashMap<String, Double>()),
	
	ka1(CheckCategory.KILLAURA_WALLHACK, 12, Status.CANCEL, new HashMap<String, Double>()),
	ka2(CheckCategory.COMBAT_ANGLE, 16, Status.CANCEL, new HashMap<String, Double>() {{ put("minValue", 0.75); }}),
	ka4(CheckCategory.KILLAURA, 20, Status.CANCEL, new HashMap<String, Double>()  {{ put("maxValue", 6.0); }}),
	ka6(CheckCategory.KILLAURA, 24, Status.CANCEL, new HashMap<String, Double>() {{ put("minValue", 0.8); }}),
	ka7(CheckCategory.KILLAURA, 18, Status.CANCEL, new HashMap<String, Double>() {{ put("minValue", 1.2); }}),
	ka8(CheckCategory.KILLAURA, 5, Status.CANCEL, new HashMap<String, Double>() {{ put("maxHitAccuracy", 98.0); put("maxHitSpeed", 14.0); }}),
	
	nf1(CheckCategory.PLAYER_NOFALL, 16, Status.CANCEL, new HashMap<String, Double>() {{ put("maxValue", 0.7); }}),
	
	v1(CheckCategory.INVALID_VELOCITY, 12, Status.CANCEL, new HashMap<String, Double>()),
	v2(CheckCategory.INVALID_VELOCITY, 12, Status.CANCEL, new HashMap<String, Double>()),
	
	ns1(CheckCategory.MOVING_NOSLOWDOWN, 20, Status.CANCEL, new HashMap<String, Double>() {{ put("maxValue", 14.0); }}),
	ns2(CheckCategory.MOVING_NOSLOWDOWN, 15, Status.CANCEL, new HashMap<String, Double>() {{ put("maxValue", 12.0); put("maxSpeed", 0.08); }}),
	ns3(CheckCategory.MOVING_NOSLOWDOWN, 10, Status.CANCEL, new HashMap<String, Double>() {{ put("maxValue", 30.0); }}),
	ns4(CheckCategory.MOVING_NOSLOWDOWN, 20, Status.CANCEL, new HashMap<String, Double>() {{ put("maxValue", 10.0); }}),
	ns5(CheckCategory.MOVING_NOSLOWDOWN, 25, Status.CANCEL, new HashMap<String, Double>()),
	ns7(CheckCategory.MOVING_NOWEB, 18, Status.CANCEL, new HashMap<String, Double>() {{ put("maxSpeed", 0.121); }}),
	
	nsw1(CheckCategory.PLAYER_NOSWING, 30, Status.CANCEL, new HashMap<String, Double>()),
	nsw2(CheckCategory.PLAYER_NOSWING, 25, Status.CANCEL, new HashMap<String, Double>()),
	nsw3(CheckCategory.PLAYER_NOSWING, 20, Status.CANCEL, new HashMap<String, Double>()),
	nsw4(CheckCategory.PLAYER_NOSWING, 20, Status.CANCEL, new HashMap<String, Double>()),
	
	nmr1(CheckCategory.FALSE_ANGLE, 50, Status.BAN, new HashMap<String, Double>()),
	nmr2(CheckCategory.FALSE_ANGLE, 50, Status.BAN, new HashMap<String, Double>()),
	
	ph1(CheckCategory.MOVING_NOCLIP, 25, Status.CANCEL, new HashMap<String, Double>()), 
	ph2(CheckCategory.MOVING_PHASE, 10, Status.CANCEL, new HashMap<String, Double>()), 
	
	r1(CheckCategory.COMBAT_REACH, 8, Status.CANCEL, new HashMap<String, Double>() {{ put("maxValue", 3.25); }}), 
	r2(CheckCategory.PLAYER_REACH, 12, Status.CANCEL, new HashMap<String, Double>() {{ put("maxValue", 6.0); }}),
		
	msw1(CheckCategory.MOVING_SAFEWALK, 24, Status.CANCEL, new HashMap<String, Double>() {{ put("maxValue", 10.0); }}), 
	msw2(CheckCategory.MOVING_SAFEWALK, 12, Status.CANCEL, new HashMap<String, Double>()), 
	
	sn1(CheckCategory.MORE_PACKETS, 12, Status.KICK, new HashMap<String, Double>()  {{ put("maxValue", 15.0); }}), 
	
	sg1(CheckCategory.SPEED_GROUND, 16, Status.CANCEL, new HashMap<String, Double>()  {{ put("maxSpeed", 0.29); put("sneakMultiply", 0.46); put("notSprintMultiply", 0.84); put("stepMultiply", 1.5); put("runBackwardMultiply", 0.82); put("runSidewayMultiply", 0.88); put("minSidewayValue", 7.0); }}),
	sj1(CheckCategory.SPEED_JUMP, 12, Status.CANCEL, new HashMap<String, Double>()),
	sj2(CheckCategory.SPEED_JUMP, 12, Status.CANCEL, new HashMap<String, Double>() {{ put("maxSpeed", 0.2); put("minValue", 1.5); }}),
	sj3(CheckCategory.SPEED_JUMP, 12, Status.CANCEL, new HashMap<String, Double>() {{ put("maxSpeed", 0.18); put("maxValue", 1.8); }}),
	sj4(CheckCategory.SPEED_JUMP, 14, Status.CANCEL, new HashMap<String, Double>() {{ put("maxSpeed", 0.375); }}),
	sj5(CheckCategory.SPEED_JUMP, 8, Status.CANCEL, new HashMap<String, Double>() {{ put("maxValue", 0.62); }}), 
	sj6(CheckCategory.SPEED_JUMP, 12, Status.KICK, new HashMap<String, Double>()), 
	sw1(CheckCategory.SPEED_WATER, 8, Status.CANCEL, new HashMap<String, Double>() {{ put("maxSpeed", 0.16); }}), 
	
	sp1(CheckCategory.MOVING_SPRINT, 16, Status.KICK, new HashMap<String, Double>()), 
	sp2(CheckCategory.MOVING_SPRINT, 10, Status.CANCEL, new HashMap<String, Double>() {{ put("minValue", 6.5); }}),
	sp3(CheckCategory.MOVING_SPRINT, 20, Status.BAN, new HashMap<String, Double>()),
	
	st1(CheckCategory.MOVING_STRAFE, 15, Status.CANCEL, new HashMap<String, Double>() {{ put("maxValue", 0.042); }}), 
	st2(CheckCategory.MOVING_STRAFE, 14, Status.CANCEL, new HashMap<String, Double>() {{ put("minValue", -0.4); }}), 
	st3(CheckCategory.MOVING_STRAFE, 10, Status.CANCEL, new HashMap<String, Double>() {{ put("maxValue", 1.95); put("requiredSpeed", 0.06); }}), 
			
	ws1(CheckCategory.WORLD_SCAFFOLD, 25, Status.CANCEL, new HashMap<String, Double>()), 
	
	wt1(CheckCategory.WORLD_TOWER, 25, Status.KICK, new HashMap<String, Double>()), 
	wt2(CheckCategory.WORLD_TOWER, 25, Status.CANCEL, new HashMap<String, Double>()),
	
	z1(CheckCategory.PLAYER_ZOOT, 25, Status.CANCEL, new HashMap<String, Double>() {{ put("maxValue", 3.0); }});
	
	CheckCategory check;	
	HashMap<String, Double> ValueList;	
	int FlagLevel;
	Status Measure;
	Check(CheckCategory check, int FlagLevel, Status Measure, HashMap<String, Double> ValueList) {
		this.check = check;
		this.ValueList = ValueList;
		this.FlagLevel = FlagLevel;
		this.Measure = Measure;
	}
	public CheckCategory showCheckCategory() {
		return check;
	}
	public HashMap<String, Double> showValueList() {
		return ValueList;
	}
	public int showFlagLevel() {
		return FlagLevel;
	}
	public Status showMeasure() {
		return Measure;
	}
}
