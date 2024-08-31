package org.chamchi.usershop.instance;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class UserShopSlotData {
    private Player player;
    private int price;
    private ItemStack itemStack;

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public void setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
    }
}
