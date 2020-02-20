package de.niroyt.nnc.manager;

import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import de.niroyt.nnc.enums.Check;
import de.niroyt.nnc.utils.PacketReader;
import de.niroyt.nnc.utils.TimeHelper;

public class PlayerData {

	Player player;
	
	Location LastTeleportPos = null;
	
	Location GroundUtilsLastOnGround = null;
	Location GroundUtilsLastTimePos = null;
	long GroundUtilsWasFlying = 0;
	boolean GroundUtilsWasOnSlime = false;
	int GroundUtilsWasOnIce = 0;
    TimeHelper GroundUtilsPosResetTimer;

	PacketReader packetReader = null;
	Object LastPacket = null;
	long LastMovePacketTime = 0;
	
	Check TestingCheck = null;	
	int ping = 0;	
	long PingFirstChangeSlot = -1;
	long PingTeleportTime = -1;
	int FlagLevel = 0;	
	TimeHelper LastMessage;
	TimeHelper LastLog;
	boolean NormalyOnline = true;
	long LastFlag = 0;
	int PacketPreviousItemHeld = -1;	
	
	int ChestStealerFlags = 0;
	TimeHelper ChestStealerResetTime;
	Material ChestStealerLastItem;
	long ChestStealerLastChest = 0;
	
	HashMap<String, Long> EspHidden;
	HashMap<String, Long> EspHideEquip;
	
	long FastBowTime = 0;
	
	long FastBreakLastInteract = 0;
	long FastBreakStartDestroyTime = 0;
	Location FastBreakStartDestroyBlock;	
	Location FastBreakBlockToFlagPlayer;
	
	long FastHealTime = 0;
	double FastHealAmount = 0;
	
	TimeHelper FastPlaceResetTime;
	int FastPlaceCounter = 0;
	
	long FastUseTime = 0;
	
	int FlyMoves = 0;
	double FlyLastPosDiff = -1337;
	boolean FlyTouchedLadder = false; 
	int FlyMovesSinceTeleport = 0;
	double FlySlimeJumpMotion = 0;
	
	HashMap<Long, Vector> FlyVelocityLog;
	HashMap<Long, Location> FlyVelocityLocation;
	HashMap<Long, Vector> MovementVelocityLog;
	HashMap<Long, Location> MovementVelocityLocation;
	
	TimeHelper GhostHandTimer;
	int GhostHandClicks = 0;	
	TimeHelper GhostHandKillauraTimer;
	int GhostHandKillauraClickCount = 0;
	boolean GhostHandPlayersInBreakBlock = false;
	
	long InventoryOpen = -1337;
	boolean InventoryOpenPacket = false;
	double InventorySpeed = 0;
	int InventoryMoveFlags = 0;
	long InventoryLastItemHoldChange = 0;
		
	long KillauraHitLock = 0;
	long KillauraLastHit = 0;
	int KillauraLastHitEntity = 0;
	long KillauraLastMove = 0;
	long KillauraMoveTimeLastFlag = 0;
	int KillauraHitSuccessful = 0;
	int KillauraHitFailure = 0;
	TimeHelper KillauraHitDiffReset;
	int KillauraSamePitch = 0;
	double KillauraLastYawSpeed = 0;
	
	float NoFallFallHeight = 0;
	
	int NoSlowAntiSword = 0;
	long NoSlowUseItem = 0;
	double NoSlowOldSpeed = 0;
	int NoSlowFlagCounter = 0;
	
	double NoSwingFlags = 0; 
	long NoSwingLastSwing = 0;
		
	float PacketOldFromPitch = 0;
	float PacketOldFromYaw = 0;	
	long PacketAntiMoreMovePacketTime;
	double PacketLastMoreMovePacketCount = 0;
	long PacketLastOverMoreMovePacket = 0;
	double PacketAntiMoreMovePacketCount = -15;
	TimeHelper PacketUpdatePingTimer;
	float PacketOldPitch = 0;
	float PacketOldYaw = 0;
	int PacketDoNotCheck = 0;
	TimeHelper PacketAntiCrashResetTimer;
	int PacketAntiCrashCount = 0;
	int PacketsLagCount = 0;
	
	Vector ReachOldMovements = null;
	
	int SafewalkCounter = 0;
	double SafewalkSpeedValue = 0;
	long SafewalkLastFlag = 0;
	
	int SneakToggles = 0;
	TimeHelper SneakResetTimer;
	
	double Speed1OldJumpValue = 0;
   	double Speed1OldSpeed = 0;
   	double Speed1AfterJump = 0;    
   	double Speed1JumpSpeed = 0;
   	boolean Speed1DoNotCheckAfterJump = false;
   	double Speed1NoWebSpeed = 0; 
	
    double Speed2LastDetected = 0;
    double Speed2OldSpeed = 0;
   	
    double Speed3LastSpeedOnLadder = 0;    
   	double Speed3LastSpeedInWater = 0;
    
   	int SprintValue = 0;
    boolean Sprinting = false;
    long SprintLastToggle = 0;
    long SprintLastToggleFlag = 0;
    
    double StrafeValueX = 0;
    double StrafeValueZ = 0;
   	HashMap<Long, Double[]> StrafeFlags;
   	long StrafeLastFlag = 0;
    
    long WorldPlaceBlockLock = 0;
    int WorldPlaceBlockTimeFlags = 0;
    
    long WorldLastAngleFlag = 0;
    
    int WorldScaffoldPosX = 0;
    int WorldScaffoldPosZ = 0;    
    TimeHelper WorldScaffoldCountReset;
    int WorldScaffoldBlockCount = 0;
   	
   	int WorldTowerPosX = 0;
    int WorldTowerPosZ = 0;    
    TimeHelper WorldTowerCountReset;
    int WorldTowerBlockCount = 0;
    
    long ZootTime = 0;
    int ZootTicks = 0;
    
	public PlayerData(Player player) {
		this.player = player;
		this.LastMessage = new TimeHelper();
		this.LastLog = new TimeHelper();
		this.ChestStealerResetTime = new TimeHelper();
		this.EspHidden = new HashMap<String, Long>();
		this.EspHideEquip = new HashMap<String, Long>();
		this.FastPlaceResetTime = new TimeHelper();
		this.GhostHandTimer = new TimeHelper();
		this.GhostHandKillauraTimer = new TimeHelper();
		this.KillauraHitDiffReset = new TimeHelper();
		this.PacketUpdatePingTimer = new TimeHelper();
		this.PacketAntiCrashResetTimer = new TimeHelper();
		this.SneakResetTimer = new TimeHelper();
		this.WorldScaffoldCountReset = new TimeHelper();
		this.WorldTowerCountReset = new TimeHelper();
		this.GroundUtilsPosResetTimer = new TimeHelper();
		this.StrafeFlags = new HashMap<Long, Double[]>();
		this.FlyVelocityLog = new HashMap<Long, Vector>();
		this.FlyVelocityLocation = new HashMap<Long, Location>();
		this.MovementVelocityLog = new HashMap<Long, Vector>();
		this.MovementVelocityLocation = new HashMap<Long, Location>();
	}
	
	
	public boolean isFlyTouchedLadder() {
		return FlyTouchedLadder;
	}


	public void setFlyTouchedLadder(boolean flyTouchedLadder) {
		FlyTouchedLadder = flyTouchedLadder;
	}


	public Location getLastTeleportPos() {
		return LastTeleportPos;
	}

	public void setLastTeleportPos(Location lastTeleportPos) {
		LastTeleportPos = lastTeleportPos;
	}

	public Player getPlayer() {
		return player;
	}

	public Location getGroundUtilsLastOnGround() {
		return GroundUtilsLastOnGround;
	}

	public void setGroundUtilsLastOnGround(Location groundUtilsLastOnGround) {
		GroundUtilsLastOnGround = groundUtilsLastOnGround;
	}

	public Location getGroundUtilsLastTimePos() {
		return GroundUtilsLastTimePos;
	}

	public void setGroundUtilsLastTimePos(Location groundUtilsLastTimePos) {
		GroundUtilsLastTimePos = groundUtilsLastTimePos;
	}

	public long getGroundUtilsWasFlying() {
		return GroundUtilsWasFlying;
	}

	public void setGroundUtilsWasFlying(long groundUtilsWasFlying) {
		GroundUtilsWasFlying = groundUtilsWasFlying;
	}

	public boolean isGroundUtilsWasOnSlime() {
		return GroundUtilsWasOnSlime;
	}

	public void setGroundUtilsWasOnSlime(boolean groundUtilsWasOnSlime) {
		GroundUtilsWasOnSlime = groundUtilsWasOnSlime;
	}

	public int getGroundUtilsWasOnIce() {
		return GroundUtilsWasOnIce;
	}

	public void setGroundUtilsWasOnIce(int groundUtilsWasOnIce) {
		GroundUtilsWasOnIce = groundUtilsWasOnIce;
	}

	public TimeHelper getGroundUtilsPosResetTimer() {
		return GroundUtilsPosResetTimer;
	}

	public void setGroundUtilsPosResetTimer(TimeHelper groundUtilsPosResetTimer) {
		GroundUtilsPosResetTimer = groundUtilsPosResetTimer;
	}

	public PacketReader getPacketReader() {
		return packetReader;
	}

	public void setPacketReader(PacketReader packetReader) {
		this.packetReader = packetReader;
	}

	public Object getLastPacket() {
		return LastPacket;
	}

	public void setLastPacket(Object lastPacket) {
		LastPacket = lastPacket;
	}

	public long getLastMovePacketTime() {
		return LastMovePacketTime;
	}

	public void setLastMovePacketTime(long lastMovePacketTime) {
		LastMovePacketTime = lastMovePacketTime;
	}

	public Check getTestingCheck() {
		return TestingCheck;
	}

	public void setTestingCheck(Check testingCheck) {
		TestingCheck = testingCheck;
	}

	public int getPing() {
		return ping;
	}

	public void setPing(int ping) {
		this.ping = ping;
	}
	
	public long getPingFirstChangeSlot() {
		return PingFirstChangeSlot;
	}


	public void setPingFirstChangeSlot(long pingFirstChangeSlot) {
		PingFirstChangeSlot = pingFirstChangeSlot;
	}


	public long getPingTeleportTime() {
		return PingTeleportTime;
	}


	public void setPingTeleportTime(long pingTeleportTime) {
		PingTeleportTime = pingTeleportTime;
	}

	public int getFlagLevel() {
		return FlagLevel;
	}

	public void setFlagLevel(int flagLevel) {
		FlagLevel = flagLevel;
	}

	public TimeHelper getLastMessage() {
		return LastMessage;
	}

	public void setLastMessage(TimeHelper lastMessage) {
		LastMessage = lastMessage;
	}

	public boolean isNormalyOnline() {
		return NormalyOnline;
	}

	public void setNormalyOnline(boolean normalyOnline) {
		NormalyOnline = normalyOnline;
	}

	public TimeHelper getLastLog() {
		return LastLog;
	}

	public void setLastLog(TimeHelper lastLog) {
		LastLog = lastLog;
	}

	public long getLastFlag() {
		return LastFlag;
	}

	public void setLastFlag(long lastFlag) {
		LastFlag = lastFlag;
	}

	public int getPacketPreviousItemHeld() {
		return PacketPreviousItemHeld;
	}


	public void setPacketPreviousItemHeld(int packetPreviousItemHeld) {
		PacketPreviousItemHeld = packetPreviousItemHeld;
	}


	public int getChestStealerFlags() {
		return ChestStealerFlags;
	}

	public void setChestStealerFlags(int chestStealerFlags) {
		ChestStealerFlags = chestStealerFlags;
	}

	public TimeHelper getChestStealerResetTime() {
		return ChestStealerResetTime;
	}

	public void setChestStealerResetTime(TimeHelper chestStealerResetTime) {
		ChestStealerResetTime = chestStealerResetTime;
	}

	public Material getChestStealerLastItem() {
		return ChestStealerLastItem;
	}

	public void setChestStealerLastItem(Material chestStealerLastItem) {
		ChestStealerLastItem = chestStealerLastItem;
	}

	public long getChestStealerLastChest() {
		return ChestStealerLastChest;
	}

	public void setChestStealerLastChest(long chestStealerLastChest) {
		ChestStealerLastChest = chestStealerLastChest;
	}

	public HashMap<String, Long> getEspHidden() {
		return EspHidden;
	}

	public void setEspHidden(HashMap<String, Long> espHidden) {
		EspHidden = espHidden;
	}

	public HashMap<String, Long> getEspHideEquip() {
		return EspHideEquip;
	}

	public void setEspHideEquip(HashMap<String, Long> espHideEquip) {
		EspHideEquip = espHideEquip;
	}

	public long getFastBowTime() {
		return FastBowTime;
	}

	public void setFastBowTime(long fastBowTime) {
		FastBowTime = fastBowTime;
	}

	public long getFastBreakLastInteract() {
		return FastBreakLastInteract;
	}

	public void setFastBreakLastInteract(long fastBreakLastInteract) {
		FastBreakLastInteract = fastBreakLastInteract;
	}

	public long getFastBreakStartDestroyTime() {
		return FastBreakStartDestroyTime;
	}

	public void setFastBreakStartDestroyTime(long fastBreakStartDestroyTime) {
		FastBreakStartDestroyTime = fastBreakStartDestroyTime;
	}

	public Location getFastBreakStartDestroyBlock() {
		return FastBreakStartDestroyBlock;
	}

	public void setFastBreakStartDestroyBlock(Location fastBreakStartDestroyBlock) {
		FastBreakStartDestroyBlock = fastBreakStartDestroyBlock;
	}

	public Location getFastBreakBlockToFlagPlayer() {
		return FastBreakBlockToFlagPlayer;
	}

	public void setFastBreakBlockToFlagPlayer(Location fastBreakBlockToFlagPlayer) {
		FastBreakBlockToFlagPlayer = fastBreakBlockToFlagPlayer;
	}

	public long getFastHealTime() {
		return FastHealTime;
	}

	public void setFastHealTime(long fastHealTime) {
		FastHealTime = fastHealTime;
	}

	public double getFastHealAmount() {
		return FastHealAmount;
	}

	public void setFastHealAmount(double fastHealAmount) {
		FastHealAmount = fastHealAmount;
	}

	public TimeHelper getFastPlaceResetTime() {
		return FastPlaceResetTime;
	}

	public void setFastPlaceResetTime(TimeHelper fastPlaceResetTime) {
		FastPlaceResetTime = fastPlaceResetTime;
	}

	public int getFastPlaceCounter() {
		return FastPlaceCounter;
	}

	public void setFastPlaceCounter(int fastPlaceCounter) {
		FastPlaceCounter = fastPlaceCounter;
	}

	public long getFastUseTime() {
		return FastUseTime;
	}

	public void setFastUseTime(long fastUseTime) {
		FastUseTime = fastUseTime;
	}

	public int getFlyMoves() {
		return FlyMoves;
	}

	public void setFlyMoves(int flyMoves) {
		FlyMoves = flyMoves;
	}

	public double getFlyLastPosDiff() {
		return FlyLastPosDiff;
	}

	public void setFlyLastPosDiff(double flyLastPosDiff) {
		FlyLastPosDiff = flyLastPosDiff;
	}

	
	public int getFlyMovesSinceTeleport() {
		return FlyMovesSinceTeleport;
	}


	public void setFlyMovesSinceTeleport(int flyMovesSinceTeleport) {
		FlyMovesSinceTeleport = flyMovesSinceTeleport;
	}


	public HashMap<Long, Vector> getFlyVelocityLog() {
		return FlyVelocityLog;
	}


	public void setFlyVelocityLog(HashMap<Long, Vector> velocityLog) {
		FlyVelocityLog = velocityLog;
	}


	public HashMap<Long, Location> getFlyVelocityLocation() {
		return FlyVelocityLocation;
	}


	public void setFlyVelocityLocation(HashMap<Long, Location> velocityLocation) {
		FlyVelocityLocation = velocityLocation;
	}

	public double getFlySlimeJumpMotion() {
		return FlySlimeJumpMotion;
	}


	public void setFlySlimeJumpMotion(double flySlimeJumpMotion) {
		FlySlimeJumpMotion = flySlimeJumpMotion;
	}


	public HashMap<Long, Vector> getMovementVelocityLog() {
		return MovementVelocityLog;
	}


	public void setMovementVelocityLog(HashMap<Long, Vector> movementVelocityLog) {
		MovementVelocityLog = movementVelocityLog;
	}


	public HashMap<Long, Location> getMovementVelocityLocation() {
		return MovementVelocityLocation;
	}


	public void setMovementVelocityLocation(HashMap<Long, Location> movementVelocityLocation) {
		MovementVelocityLocation = movementVelocityLocation;
	}


	public TimeHelper getGhostHandTimer() {
		return GhostHandTimer;
	}

	public void setGhostHandTimer(TimeHelper ghostHandTimer) {
		GhostHandTimer = ghostHandTimer;
	}

	public int getGhostHandClicks() {
		return GhostHandClicks;
	}

	public void setGhostHandClicks(int ghostHandClicks) {
		GhostHandClicks = ghostHandClicks;
	}

	public TimeHelper getGhostHandKillauraTimer() {
		return GhostHandKillauraTimer;
	}

	public void setGhostHandKillauraTimer(TimeHelper ghostHandKillauraTimer) {
		GhostHandKillauraTimer = ghostHandKillauraTimer;
	}

	public int getGhostHandKillauraClickCount() {
		return GhostHandKillauraClickCount;
	}

	public void setGhostHandKillauraClickCount(int ghostHandKillauraClickCount) {
		GhostHandKillauraClickCount = ghostHandKillauraClickCount;
	}

	public boolean isGhostHandPlayersInBreakBlock() {
		return GhostHandPlayersInBreakBlock;
	}

	public void setGhostHandPlayersInBreakBlock(boolean ghostHandPlayersInBreakBlock) {
		GhostHandPlayersInBreakBlock = ghostHandPlayersInBreakBlock;
	}

	public long getInventoryOpen() {
		return InventoryOpen;
	}

	public void setInventoryOpen(long inventoryOpen) {
		InventoryOpen = inventoryOpen;
	}

	public boolean isInventoryOpenPacket() {
		return InventoryOpenPacket;
	}

	public void setInventoryOpenPacket(boolean inventoryOpenPacket) {
		InventoryOpenPacket = inventoryOpenPacket;
	}

	public double getInventorySpeed() {
		return InventorySpeed;
	}

	public void setInventorySpeed(double inventorySpeed) {
		InventorySpeed = inventorySpeed;
	}

	public int getInventoryMoveFlags() {
		return InventoryMoveFlags;
	}

	public void setInventoryMoveFlags(int inventoryMoveFlags) {
		InventoryMoveFlags = inventoryMoveFlags;
	}

	public long getInventoryLastItemHoldChange() {
		return InventoryLastItemHoldChange;
	}


	public void setInventoryLastItemHoldChange(long inventoryLastItemHoldChange) {
		InventoryLastItemHoldChange = inventoryLastItemHoldChange;
	}

	public long getKillauraHitLock() {
		return KillauraHitLock;
	}

	public void setKillauraHitLock(long killauraHitLock) {
		KillauraHitLock = killauraHitLock;
	}

	public long getKillauraLastHit() {
		return KillauraLastHit;
	}

	public void setKillauraLastHit(long killauraLastHit) {
		KillauraLastHit = killauraLastHit;
	}

	public int getKillauraLastHitEntity() {
		return KillauraLastHitEntity;
	}

	public void setKillauraLastHitEntity(int killauraLastHitEntity) {
		KillauraLastHitEntity = killauraLastHitEntity;
	}

	public long getKillauraLastMove() {
		return KillauraLastMove;
	}

	public void setKillauraLastMove(long killauraLastMove) {
		KillauraLastMove = killauraLastMove;
	}

	public long getKillauraMoveTimeLastFlag() {
		return KillauraMoveTimeLastFlag;
	}

	public void setKillauraMoveTimeLastFlag(long killauraMoveTimeFlags) {
		KillauraMoveTimeLastFlag = killauraMoveTimeFlags;
	}

	public int getKillauraHitSuccessful() {
		return KillauraHitSuccessful;
	}

	public void setKillauraHitSuccessful(int killauraHitSuccessful) {
		KillauraHitSuccessful = killauraHitSuccessful;
	}

	public int getKillauraHitFailure() {
		return KillauraHitFailure;
	}

	public void setKillauraHitFailure(int killauraHitFailure) {
		KillauraHitFailure = killauraHitFailure;
	}

	public TimeHelper getKillauraHitDiffReset() {
		return KillauraHitDiffReset;
	}

	public void setKillauraHitDiffReset(TimeHelper killauraHitDiffReset) {
		KillauraHitDiffReset = killauraHitDiffReset;
	}
	
	public int getKillauraSamePitch() {
		return KillauraSamePitch;
	}


	public void setKillauraSamePitch(int killauraSamePitch) {
		KillauraSamePitch = killauraSamePitch;
	}


	public double getKillauraLastYawSpeed() {
		return KillauraLastYawSpeed;
	}


	public void setKillauraLastYawSpeed(double d) {
		KillauraLastYawSpeed = d;
	}


	public float getNoFallFallHeight() {
		return NoFallFallHeight;
	}

	public void setNoFallFallHeight(float noFallFallHeight) {
		NoFallFallHeight = noFallFallHeight;
	}


	public int getNoSlowAntiSword() {
		return NoSlowAntiSword;
	}

	public void setNoSlowAntiSword(int noSlowAntiSword) {
		NoSlowAntiSword = noSlowAntiSword;
	}

	public long getNoSlowUseItem() {
		return NoSlowUseItem;
	}

	public void setNoSlowUseItem(long noSlowUseItem) {
		NoSlowUseItem = noSlowUseItem;
	}

	public double getNoSlowOldSpeed() {
		return NoSlowOldSpeed;
	}

	public void setNoSlowOldSpeed(double noSlowOldSpeed) {
		NoSlowOldSpeed = noSlowOldSpeed;
	}

	public int getNoSlowFlagCounter() {
		return NoSlowFlagCounter;
	}

	public void setNoSlowFlagCounter(int noSlowFlagCounter) {
		NoSlowFlagCounter = noSlowFlagCounter;
	}

	public double getNoSwingFlags() {
		return NoSwingFlags;
	}

	public void setNoSwingFlags(double noSwingFlags) {
		NoSwingFlags = noSwingFlags;
	}

	public long getNoSwingLastSwing() {
		return NoSwingLastSwing;
	}

	public void setNoSwingLastSwing(long noSwingLastSwing) {
		NoSwingLastSwing = noSwingLastSwing;
	}

	public float getPacketOldFromPitch() {
		return PacketOldFromPitch;
	}

	public void setPacketOldFromPitch(float packetOldFromPitch) {
		PacketOldFromPitch = packetOldFromPitch;
	}

	public float getPacketOldFromYaw() {
		return PacketOldFromYaw;
	}

	public void setPacketOldFromYaw(float packetOldFromYaw) {
		PacketOldFromYaw = packetOldFromYaw;
	}

	public long getPacketAntiMoreMovePacketTime() {
		return PacketAntiMoreMovePacketTime;
	}

	public void setPacketAntiMoreMovePacketTime(long packetAntiMoreMovePacketTime) {
		PacketAntiMoreMovePacketTime = packetAntiMoreMovePacketTime;
	}

	public long getPacketLastOverMoreMovePacket() {
		return PacketLastOverMoreMovePacket;
	}

	public void setPacketLastOverMoreMovePacket(long packetLastOverMoreMovePacket) {
		PacketLastOverMoreMovePacket = packetLastOverMoreMovePacket;
	}

	public double getPacketAntiMoreMovePacketCount() {
		return PacketAntiMoreMovePacketCount;
	}

	public void setPacketAntiMoreMovePacketCount(double packetAntiMoreMovePacketCount) {
		PacketAntiMoreMovePacketCount = packetAntiMoreMovePacketCount;
	}

	public TimeHelper getPacketUpdatePingTimer() {
		return PacketUpdatePingTimer;
	}

	public void setPacketUpdatePingTimer(TimeHelper packetUpdatePingTimer) {
		PacketUpdatePingTimer = packetUpdatePingTimer;
	}

	public float getPacketOldPitch() {
		return PacketOldPitch;
	}

	public void setPacketOldPitch(float packetOldPitch) {
		PacketOldPitch = packetOldPitch;
	}

	public float getPacketOldYaw() {
		return PacketOldYaw;
	}

	public void setPacketOldYaw(float packetOldYaw) {
		PacketOldYaw = packetOldYaw;
	}

	public int getPacketDoNotCheck() {
		return PacketDoNotCheck;
	}

	public void setPacketDoNotCheck(int packetDoNotCheck) {
		PacketDoNotCheck = packetDoNotCheck;
	}

	public TimeHelper getPacketAntiCrashResetTimer() {
		return PacketAntiCrashResetTimer;
	}

	public void setPacketAntiCrashResetTimer(TimeHelper packetAntiCrashResetTimer) {
		PacketAntiCrashResetTimer = packetAntiCrashResetTimer;
	}

	public int getPacketAntiCrashCount() {
		return PacketAntiCrashCount;
	}

	public void setPacketAntiCrashCount(int packetAntiCrashCount) {
		PacketAntiCrashCount = packetAntiCrashCount;
	}

	public int getPacketsLagCount() {
		return PacketsLagCount;
	}


	public void setPacketsLagCount(int packetsLagCount) {
		PacketsLagCount = packetsLagCount;
	}


	public double getPacketLastMoreMovePacketCount() {
		return PacketLastMoreMovePacketCount;
	}


	public void setPacketLastMoreMovePacketCount(double packetLastMoreMovePacketCount) {
		PacketLastMoreMovePacketCount = packetLastMoreMovePacketCount;
	}


	public Vector getReachOldMovements() {
		return ReachOldMovements;
	}


	public void setReachOldMovements(Vector reachOldMovements) {
		ReachOldMovements = reachOldMovements;
	}


	public int getSafewalkCounter() {
		return SafewalkCounter;
	}

	public void setSafewalkCounter(int safewalkCounter) {
		SafewalkCounter = safewalkCounter;
	}

	public double getSafewalkSpeedValue() {
		return SafewalkSpeedValue;
	}


	public void setSafewalkSpeedValue(double safewalkSpeedValue) {
		SafewalkSpeedValue = safewalkSpeedValue;
	}


	public long getSafewalkLastFlag() {
		return SafewalkLastFlag;
	}


	public void setSafewalkLastFlag(long safewalkLastFlag) {
		SafewalkLastFlag = safewalkLastFlag;
	}


	public int getSneakToggles() {
		return SneakToggles;
	}

	public void setSneakToggles(int sneakToggles) {
		SneakToggles = sneakToggles;
	}

	public TimeHelper getSneakResetTimer() {
		return SneakResetTimer;
	}

	public void setSneakResetTimer(TimeHelper sneakResetTimer) {
		SneakResetTimer = sneakResetTimer;
	}

	public double getSpeed1OldSpeed() {
		return Speed1OldSpeed;
	}

	public void setSpeed1OldSpeed(double speed1OldSpeed) {
		Speed1OldSpeed = speed1OldSpeed;
	}

	public double getSpeed1AfterJump() {
		return Speed1AfterJump;
	}

	public void setSpeed1AfterJump(double speed1AfterJump) {
		Speed1AfterJump = speed1AfterJump;
	}

	public double getSpeed1JumpSpeed() {
		return Speed1JumpSpeed;
	}

	public void setSpeed1JumpSpeed(double speed1JumpSpeed) {
		Speed1JumpSpeed = speed1JumpSpeed;
	}

	public boolean isSpeed1DoNotCheckAfterJump() {
		return Speed1DoNotCheckAfterJump;
	}

	public void setSpeed1DoNotCheckAfterJump(boolean speed1DoNotCheckAfterJump) {
		Speed1DoNotCheckAfterJump = speed1DoNotCheckAfterJump;
	}

	public double getSpeed1OldJumpValue() {
		return Speed1OldJumpValue;
	}


	public void setSpeed1OldJumpValue(double speed1OldJumpValue) {
		Speed1OldJumpValue = speed1OldJumpValue;
	}


	public double getSpeed1NoWebSpeed() {
		return Speed1NoWebSpeed;
	}


	public void setSpeed1NoWebSpeed(double speed1NoWebSpeed) {
		Speed1NoWebSpeed = speed1NoWebSpeed;
	}


	public double getSpeed2LastDetected() {
		return Speed2LastDetected;
	}

	public void setSpeed2LastDetected(double speed2LastDetected) {
		Speed2LastDetected = speed2LastDetected;
	}

	public double getSpeed2OldSpeed() {
		return Speed2OldSpeed;
	}

	public void setSpeed2OldSpeed(double speed2OldSpeed) {
		Speed2OldSpeed = speed2OldSpeed;
	}

	public double getSpeed3LastSpeedOnLadder() {
		return Speed3LastSpeedOnLadder;
	}

	public void setSpeed3LastSpeedOnLadder(double speed3LastSpeedOnLadder) {
		Speed3LastSpeedOnLadder = speed3LastSpeedOnLadder;
	}

	public double getSpeed3LastSpeedInWater() {
		return Speed3LastSpeedInWater;
	}

	public void setSpeed3LastSpeedInWater(double speed3LastSpeedInWater) {
		Speed3LastSpeedInWater = speed3LastSpeedInWater;
	}

	public int getSprintValue() {
		return SprintValue;
	}

	public void setSprintValue(int sprintValue) {
		SprintValue = sprintValue;
	}

	public boolean isSprinting() {
		return Sprinting;
	}

	public void setSprinting(boolean sprinting) {
		Sprinting = sprinting;
	}

	public long getSprintLastToggle() {
		return SprintLastToggle;
	}

	public void setSprintLastToggle(long sprintLastToggle) {
		SprintLastToggle = sprintLastToggle;
	}

	public long getSprintLastToggleFlag() {
		return SprintLastToggleFlag;
	}

	public void setSprintLastToggleFlag(long sprintLastToggleFlag) {
		SprintLastToggleFlag = sprintLastToggleFlag;
	}

	public double getStrafeValueX() {
		return StrafeValueX;
	}

	public void setStrafeValueX(double strafeValueX) {
		StrafeValueX = strafeValueX;
	}

	public double getStrafeValueZ() {
		return StrafeValueZ;
	}

	public void setStrafeValueZ(double strafeValueZ) {
		StrafeValueZ = strafeValueZ;
	}

	public HashMap<Long, Double[]> getStrafeFlags() {
		return StrafeFlags;
	}

	public void setStrafeFlags(HashMap<Long, Double[]> strafeFlags) {
		StrafeFlags = strafeFlags;
	}

	public long getStrafeLastFlag() {
		return StrafeLastFlag;
	}


	public void setStrafeLastFlag(long strafeLastFlag) {
		StrafeLastFlag = strafeLastFlag;
	}


	public long getWorldPlaceBlockLock() {
		return WorldPlaceBlockLock;
	}

	public void setWorldPlaceBlockLock(long worldPlaceBlockLock) {
		WorldPlaceBlockLock = worldPlaceBlockLock;
	}

	public int getWorldPlaceBlockTimeFlags() {
		return WorldPlaceBlockTimeFlags;
	}

	public void setWorldPlaceBlockTimeFlags(int worldPlaceBlockTimeFlags) {
		WorldPlaceBlockTimeFlags = worldPlaceBlockTimeFlags;
	}

	public int getWorldScaffoldPosX() {
		return WorldScaffoldPosX;
	}

	public void setWorldScaffoldPosX(int worldScaffoldPosX) {
		WorldScaffoldPosX = worldScaffoldPosX;
	}

	public int getWorldScaffoldPosZ() {
		return WorldScaffoldPosZ;
	}

	public void setWorldScaffoldPosZ(int worldScaffoldPosZ) {
		WorldScaffoldPosZ = worldScaffoldPosZ;
	}

	public TimeHelper getWorldScaffoldCountReset() {
		return WorldScaffoldCountReset;
	}

	public void setWorldScaffoldCountReset(TimeHelper worldScaffoldCountReset) {
		WorldScaffoldCountReset = worldScaffoldCountReset;
	}

	public int getWorldScaffoldBlockCount() {
		return WorldScaffoldBlockCount;
	}

	public void setWorldScaffoldBlockCount(int worldScaffoldBlockCount) {
		WorldScaffoldBlockCount = worldScaffoldBlockCount;
	}

	public int getWorldTowerPosX() {
		return WorldTowerPosX;
	}

	public void setWorldTowerPosX(int worldTowerPosX) {
		WorldTowerPosX = worldTowerPosX;
	}

	public int getWorldTowerPosZ() {
		return WorldTowerPosZ;
	}

	public void setWorldTowerPosZ(int worldTowerPosZ) {
		WorldTowerPosZ = worldTowerPosZ;
	}

	public TimeHelper getWorldTowerCountReset() {
		return WorldTowerCountReset;
	}

	public void setWorldTowerCountReset(TimeHelper worldTowerCountReset) {
		WorldTowerCountReset = worldTowerCountReset;
	}

	public int getWorldTowerBlockCount() {
		return WorldTowerBlockCount;
	}

	public void setWorldTowerBlockCount(int worldTowerBlockCount) {
		WorldTowerBlockCount = worldTowerBlockCount;
	}


	public long getWorldLastAngleFlag() {
		return WorldLastAngleFlag;
	}


	public void setWorldLastAngleFlag(long worldLastAngleFlag) {
		WorldLastAngleFlag = worldLastAngleFlag;
	}


	public long getZootTime() {
		return ZootTime;
	}


	public void setZootTime(long zootTime) {
		ZootTime = zootTime;
	}


	public int getZootTicks() {
		return ZootTicks;
	}


	public void setZootTicks(int zootTicks) {
		ZootTicks = zootTicks;
	}

}