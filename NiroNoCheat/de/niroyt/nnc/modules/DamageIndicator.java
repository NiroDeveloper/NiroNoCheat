package de.niroyt.nnc.modules;

import java.util.List;

import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wither;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.reflect.StructureModifier;
import com.comphenix.protocol.wrappers.WrappedWatchableObject;

import de.niroyt.nnc.Main;
import de.niroyt.nnc.manager.Manager;
import de.niroyt.nnc.manager.Module;
import de.niroyt.nnc.utils.ConfigManager;

public class DamageIndicator extends Module {
    
    public DamageIndicator() {
        start();
    }
        
    public void start() {
    	final PacketAdapter pa1 = new PacketAdapter(Main.getInstance(), ListenerPriority.NORMAL, PacketType.Play.Server.ENTITY_METADATA) {
    		public void onPacketSending(PacketEvent event) {
    			try {
    				Player observer = event.getPlayer();
    				StructureModifier<Entity> entityModifer = event.getPacket().getEntityModifier(observer.getWorld());
    				org.bukkit.entity.Entity entity = entityModifer.read(0);
    				if (entity != null && observer != entity && entity instanceof LivingEntity
    						&& !(entity instanceof EnderDragon && entity instanceof Wither)
    						&& (entity.getPassenger() == null || entity.getPassenger() != observer)
    						&& !Manager.isOP(observer) 
    						&& ConfigManager.getConfigBoolean("Checks.AntiDamageIndicator.Enabled")) {
    					event.setPacket(event.getPacket().deepClone());
    					StructureModifier<List<WrappedWatchableObject>> watcher = event.getPacket().getWatchableCollectionModifier();
    					for(WrappedWatchableObject watch : watcher.read(0)) {
    						if(watch.getIndex() == 6) {    							
    							if(watch.getValue().getClass().getSimpleName().equalsIgnoreCase("Float")) {
    								if((float) watch.getValue() > 0) {
        								watch.setValue((float) Double.NaN);
        							}
    							} else {
    								if(((Byte) watch.getValue()).floatValue() > 0) {
        								watch.setValue(Byte.valueOf((byte)Double.NaN));
        							}
    							}
    						}
    					}
    				}
    			} catch (Exception e) {
    				e.printStackTrace();
    			}
    		}
    	};
    
    	if(!ProtocolLibrary.getProtocolManager().getPacketListeners().contains(pa1)) {
    		ProtocolLibrary.getProtocolManager().addPacketListener(pa1);
    	}  
    }
}
