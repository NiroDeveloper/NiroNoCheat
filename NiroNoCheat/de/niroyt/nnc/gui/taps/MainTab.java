package de.niroyt.nnc.gui.taps;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.niroyt.nnc.Main;
import de.niroyt.nnc.enums.EventTypes;
import de.niroyt.nnc.gui.ClickEvent;
import de.niroyt.nnc.gui.GuiTab;

public class MainTab extends GuiTab {

	public MainTab() {
		setName("");
		setInvTyp(InventoryType.HOPPER);
		
		setSlots(5);
		
		setInv(Bukkit.createInventory(null, getInvTyp(), getName()));
		
		setItems();
	}
	
	public void setItems() {
		byte NNCcolor = 10;
		String NNCtext = "§2";
		if(Main.isDisabled()) {
			NNCcolor = 1;
			NNCtext = "§c";
		}
		ItemStack i1 = new ItemStack(Material.INK_SACK, 1, NNCcolor);
		ItemMeta m1 = i1.getItemMeta();
		m1.setDisplayName(NNCtext + "NiroNoCheat " + Main.version);
		ArrayList<String> l1 = new ArrayList<String>();
		l1.add("§7Click to " + (Main.isDisabled() ? "enable" : "disable"));
		l1.add(" ");
		l1.add("§7" + Bukkit.getIp() + ":" + Bukkit.getPort());
		l1.add(" ");
		l1.add("§7Player: " + Bukkit.getOnlinePlayers().size() + "/" + Bukkit.getMaxPlayers());
		long var1 = Runtime.getRuntime().maxMemory();
        long var2 = Runtime.getRuntime().totalMemory();
        long var3 = Runtime.getRuntime().freeMemory();
        long var4 = var2 - var3;    
		l1.add("§7Ram: " + (var4 / 1024 / 1024) + "/" + (var1 / 1024 / 1024) + "MB");	
		l1.add("§7Java: " + System.getProperty("java.version") + " " + Integer.valueOf(isJvm64bit() ? 64 : 32) + "bit");
		l1.add(" ");
		l1.add("§7Plugin by NiroYT");
		m1.setLore(l1);
		i1.setItemMeta(m1);
		setItem(2, i1, new ClickEvent(EventTypes.CHANGE_DISABLED));
		
		ItemStack i3 = new ItemStack(Material.BOOK_AND_QUILL, 1);
		ItemMeta m3 = i3.getItemMeta();
		m3.setDisplayName("§6Checks");
		ArrayList<String> l3 = new ArrayList<String>();
		l3.add("§7Edit check settings");
		m3.setLore(l3);
		i3.setItemMeta(m3);
		setItem(0, i3, new ClickEvent(EventTypes.GO_TO_CHECKS));
		
		ItemStack i4 = new ItemStack(Material.PAPER, 1);
		ItemMeta m4 = i4.getItemMeta();
		m4.setDisplayName("§4Settings");
		ArrayList<String> l4 = new ArrayList<String>();
		l4.add("§7Edit NiroNoCheat settings");
		m4.setLore(l4);
		i4.setItemMeta(m4);
		setItem(4, i4, new ClickEvent(EventTypes.GO_TO_SETTINGS));
	}
	
	private static boolean isJvm64bit() {
        String[] var0 = new String[] {"sun.arch.data.model", "com.ibm.vm.bitmode", "os.arch"};
        String[] var1 = var0;
        int var2 = var0.length;

        for (int var3 = 0; var3 < var2; ++var3) {
            String var4 = var1[var3];
            String var5 = System.getProperty(var4);

            if (var5 != null && var5.contains("64"))
            {
                return true;
            }
        }
        return false;
    }
	
}
