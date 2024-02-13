package org.chamchi.usershop.Listener;

import net.kyori.adventure.text.Component;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.chamchi.usershop.Instance.UserShopInstance;
import org.chamchi.usershop.Instance.UserShopSlotData;
import org.chamchi.usershop.UserShop;
public class UserShopListener implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (e.getView().getOriginalTitle().contains("§8§l[UserShop]")) {
            e.setCancelled(true);
            Player p = (Player) e.getWhoClicked();
            p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 0.5F, 1F);
            int slot = e.getSlot();
            if (e.getClickedInventory().getItem(slot) == null) {
                return;
            }
            if (slot == 53) {
                if (e.getClickedInventory().getItem(slot).getType() == Material.OAK_HANGING_SIGN) {
                    String[] strings = ChatColor.stripColor(e.getView().getOriginalTitle()).split(" ");
                    int page = Integer.parseInt(strings[1].replace("페이지", "")) + 1;
                    UserShopInstance userShopInstance = UserShop.getUserShopInstance();
                    p.openInventory(userShopInstance.open(page));
                }
                return;
            }
            if (slot == 45) {
                if (e.getClickedInventory().getItem(slot).getType() == Material.OAK_HANGING_SIGN) {
                    String[] strings = ChatColor.stripColor(e.getView().getOriginalTitle()).split(" ");
                    int page = Integer.parseInt(strings[1].replace("페이지", "")) - 1;
                    UserShopInstance userShopInstance = UserShop.getUserShopInstance();
                    p.openInventory(userShopInstance.open(page));
                }
                return;
            }
            if (slot < 45) {
                String[] strings = ChatColor.stripColor(e.getView().getOriginalTitle()).split(" ");
                int page = Integer.parseInt(strings[1].replace("페이지", ""));
                UserShopInstance userShopInstance = UserShop.getUserShopInstance();

                int count = (page-1)*45 + slot;
                UserShopSlotData data = userShopInstance.userShopSlotDataArrayList.get(count);

                if (p.getName().equalsIgnoreCase(data.getPlayer().getName())) {
                    send(p, "본인이 등록한 아이템을 구매하실 수 없습니다.");
                    return;
                }
                Economy economy = UserShop.getEconomy();
                if(economy.getBalance(p) < data.getPrice()) {
                    send(p, "보유 금액이 부족하여 해당 아이템을 구매하실 수 없습니다.");
                    return;
                }
                economy.withdrawPlayer(p, data.getPrice());
                economy.depositPlayer(data.getPlayer(), data.getPrice());
                p.getInventory().addItem(data.getItemStack());
                userShopInstance.userShopSlotDataArrayList.remove(data);
                p.sendActionBar(Component.text("§e§l[유저상점] §f유저상점에서 물건을 구입하셨습니다."));
                data.getPlayer().sendActionBar(Component.text("§e§l[유저상점] §f" + p.getName() + "님께서 당신의 물건을 구입하셨습니다."));

                for(Player player : Bukkit.getOnlinePlayers()) {
                    if (player.getOpenInventory().getOriginalTitle().contains("§8§l[UserShop]")) {
                        String[] splits = ChatColor.stripColor(player.getOpenInventory().getOriginalTitle()).split(" ");
                        int _current = Integer.parseInt(strings[1].replace("페이지", ""));
                        player.openInventory(userShopInstance.open(_current));
                    }
                }
            }

        }
    }

    public void send(Player p, String s) {
        p.sendMessage("§e§l[유저상점] §f" + s);
    }
}
