package org.chamchi.usershop.Instance;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.chamchi.usershop.UserShop;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.text.DecimalFormat;
import java.util.*;

public class UserShopInstance {

    private int _maxPage;
    public ArrayList<UserShopSlotData> userShopSlotDataArrayList = new ArrayList<UserShopSlotData>();
    private final File saveFile = new File(UserShop.getInstance().getDataFolder() + "\\" + "data.yml");

    public UserShopInstance() {
        this._maxPage = (int) (1 + Math.floor(userShopSlotDataArrayList.size() / 45));
    }

    public Inventory open(int page) {
        this._maxPage = 1;
        if(userShopSlotDataArrayList != null) {
            this._maxPage = (int) (1 + Math.floor(userShopSlotDataArrayList.size() / 45));
            if (page != 1 && (userShopSlotDataArrayList.size() % 45) == 0) {
                this._maxPage -= 1;
            }
        }
        Inventory inventory = Bukkit.createInventory(null, 54, Component.text("§8§l[UserShop] §8" + page + "페이지"));
        if (userShopSlotDataArrayList != null) {
            if (page > 1 && userShopSlotDataArrayList.size() <= (page-1)*45) {
                return open(page-1);
            }
            int _slot;
            for(int count = 0; count <45; count++) {
                if(userShopSlotDataArrayList.size()-1 < ((page-1)*45) + count) {
                    break;
                }
                _slot = ((page - 1) * 45) + count;
                UserShopSlotData slot = userShopSlotDataArrayList.get(_slot);
                inventory.setItem(count, invItem(slot));
            }
        }
        for(int i = 45; i<54; i++) {
            inventory.setItem(i, new ItemStack(Material.BLACK_STAINED_GLASS_PANE));
        }
        if (page > 1) {
            inventory.setItem(45, Button("이전"));
        }
        if (_maxPage > page) {
            inventory.setItem(53, Button("다음"));
        }
        return inventory;
    }

    public ItemStack invItem(UserShopSlotData userShopSlotData) {
        ItemStack itemStack = new ItemStack(userShopSlotData.getItemStack());
        ItemMeta itemMeta = itemStack.getItemMeta();
        @Nullable List<Component> components = new ArrayList<Component>() {
        };
        if(itemMeta.lore() != null) {
            components = itemMeta.lore();
        }
        components.add(Component.text(""));
        components.add(Component.text(" §6§l[!] §f아이템 판매자: " + userShopSlotData.getPlayer().getName()));
        components.add(Component.text(" §6§l[!] §f아이템 구매가격: " + Comma(userShopSlotData.getPrice()) + "원"));
        components.add(Component.text(""));
        components.add(Component.text("§fCLICK(클릭) §7시 해당 아이템을 구매합니다!"));
        itemMeta.lore(components);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public ItemStack Button(String type) {
        if (type.equalsIgnoreCase("이전") || type.equalsIgnoreCase("다음")) {
            ItemStack itemStack = new ItemStack(Material.OAK_HANGING_SIGN);
            ItemMeta itemMeta = itemStack.getItemMeta();
            Component component = Component.text("§6§l[ " + "§f" + type + "§f페이지로 이동하기 " + "§6§l]");
            itemMeta.displayName(component);
            List<Component> components = new ArrayList<>();
            if (itemMeta.lore() != null) {
                components = itemMeta.lore();
            }
            components.add(Component.text("§7아이템 클릭 시 " + type + "§7페이지로 이동합니다."));
            itemMeta.lore(components);
            itemStack.setItemMeta(itemMeta);
            return itemStack;
        }
        return null;
    }

    private String Comma(int price) {
        DecimalFormat decimalFormat = new DecimalFormat("###,###");
        return decimalFormat.format(price);
    }

    public void addItem(UserShopSlotData userShopSlotData) {
        userShopSlotDataArrayList.add(userShopSlotData);
    }
}
