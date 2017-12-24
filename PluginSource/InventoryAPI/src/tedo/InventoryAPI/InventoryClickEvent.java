package tedo.InventoryAPI;

import cn.nukkit.Player;
import cn.nukkit.event.Cancellable;
import cn.nukkit.event.Event;
import cn.nukkit.event.HandlerList;
import cn.nukkit.item.Item;

public class InventoryClickEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();

    public static HandlerList getHandlers() {
        return handlers;
    }

    public Player player;
    public Item item;
    public int slot;

    public InventoryClickEvent(Player player, Item item, int slot) {
    	this.player = player;
    	this.item = item;
    	this.slot = slot;
    }
}
