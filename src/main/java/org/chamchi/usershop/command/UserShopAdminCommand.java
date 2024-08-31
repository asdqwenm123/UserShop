package org.chamchi.usershop.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.chamchi.usershop.UserShop;
import org.chamchi.usershop.instance.UserShopInstance;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class UserShopAdminCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§c해당 명령어는 콘솔에서 사용 불가능합니다.");
            return false;
        }
        Player p = (Player) sender;
        if (!(p.isOp())) {
            send(p, "권한이 없어 해당 명령어를 사용하실 수 없습니다.");
            return false;
        }
        if (args.length == 0) {
            send(p, "/유저상점관리 초기화 - 유저상점 아이템들을 모두 초기화 합니다.");
            return false;
        }
        if (args[0].equalsIgnoreCase("초기화")) {
            UserShopInstance userShopInstance = UserShop.getUserShopInstance();
            userShopInstance.userShopSlotDataArrayList = new ArrayList<>();
            send(p, "유저상점 아이템들을 모두 초기화 하였습니다.");
            return false;
        }
        return false;
    }

    public void send(Player p, String s) {
        p.sendMessage("§e§l[유저상점] §f" + s);
    }
}
