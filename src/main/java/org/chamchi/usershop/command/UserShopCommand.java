package org.chamchi.usershop.command;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.chamchi.usershop.UserShop;
import org.chamchi.usershop.instance.UserShopInstance;
import org.chamchi.usershop.instance.UserShopSlotData;
import org.jetbrains.annotations.NotNull;

public class UserShopCommand implements CommandExecutor {
    private static final UserShopInstance userShopInstance = UserShop.getUserShopInstance();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§c콘솔에서는 해당 기능을 이용하실 수 없습니다.");
            return false;
        }
        Player p = (Player) sender;
        if (args.length == 0) {
            Inventory inventory = userShopInstance.open(1);
            p.openInventory(inventory);
            return false;
        }
        if (args[0].equalsIgnoreCase("등록")) {
            if (args.length == 1) {
                send(p, "가격을 입력하여주세요.");
                return false;
            }
            if (p.getInventory().getItemInMainHand().isEmpty()) {
                send(p, "손에 아이템을 들어주신 후 다시 시도하여주세요.");
                return false;
            }
            int price = Integer.parseInt(args[1]);
            if (price <= 0) {
                send(p, "가격이 잘못 입력되었습니다. 다시 입력하여주세요.");
                return false;
            }
            UserShopSlotData userShopSlotData = new UserShopSlotData();
            userShopSlotData.setItemStack(p.getInventory().getItemInMainHand());
            userShopSlotData.setPrice(price);
            userShopSlotData.setPlayer(p);
            userShopInstance.addItem(userShopSlotData);
            send(p, "유저상점에 해당 아이템을 등록하였습니다.");
            p.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
        }
        return false;
    }

    public void send(Player p, String s) {
        p.sendMessage("§e§l[유저상점] §f" + s);
    }
}
