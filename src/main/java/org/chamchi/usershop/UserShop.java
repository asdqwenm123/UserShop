package org.chamchi.usershop;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.chamchi.usershop.command.UserShopAdminCommand;
import org.chamchi.usershop.command.UserShopCommand;
import org.chamchi.usershop.instance.UserShopInstance;
import org.chamchi.usershop.instance.UserShopSlotData;
import org.chamchi.usershop.listener.InventoryClickListener;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public final class UserShop extends JavaPlugin {
    private static UserShopInstance userShopInstance;
    private static RegisteredServiceProvider<Economy> rsp;
    private final File dataFile = new File(this.getDataFolder(),"data.yml");

    public static UserShopInstance getUserShopInstance() {
        return userShopInstance;
    }

    public static Economy getEconomy() {
        return rsp.getProvider();
    }

    @Override
    public void onEnable() {
        Objects.requireNonNull(getCommand("유저상점")).setExecutor(new UserShopCommand());
        getCommand("유저상점관리").setExecutor(new UserShopAdminCommand());
        getServer().getPluginManager().registerEvents(new InventoryClickListener(), this);
        getLogger().info("유저상점 플러그인이 활성화 되었습니다. 제작자: 참치");
        rsp = Bukkit.getServicesManager().getRegistration(Economy.class);
    }

    @Override
    public void onLoad() {
        UserShop instance = this;
        if(!instance.getDataFolder().exists()) {
            instance.getDataFolder().mkdir();
        }
        if(!dataFile.exists()) {
            try {
                dataFile.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        userShopInstance = new UserShopInstance();
        userShopDataLoad();
    }

    private void userShopDataLoad() {
        FileConfiguration fileConfiguration = YamlConfiguration.loadConfiguration(dataFile);
        ConfigurationSection configurationSection = fileConfiguration.getConfigurationSection("아이템들");
        ConfigurationSection configurationSection2;
        for(int i = 0; i < configurationSection.getKeys(false).size(); i++) {
            configurationSection2 = configurationSection.getConfigurationSection(String.valueOf(i));
            UserShopSlotData userShopSlotData = new UserShopSlotData();
            userShopSlotData.setPlayer((Player) configurationSection2.get("주인"));
            userShopSlotData.setPrice((Integer) configurationSection2.get("가격"));
            userShopSlotData.setItemStack((ItemStack) configurationSection2.get("아이템"));
            userShopInstance.addItem(userShopSlotData);
        }
        getLogger().info("유저상점 데이터들을 정상적으로 불러왔습니다.");
    }

    private void userShopDataSave() {
        FileConfiguration fileConfiguration = YamlConfiguration.loadConfiguration(dataFile);
        fileConfiguration.set("아이템들", null);
        fileConfiguration.createSection("아이템들");
        ConfigurationSection configurationSection = fileConfiguration.getConfigurationSection("아이템들");
        if (userShopInstance.userShopSlotDataArrayList == null) {
            return;
        }
        for(int i = 0; i < userShopInstance.userShopSlotDataArrayList.size(); i++) {
            configurationSection.createSection(String.valueOf(i));
            ConfigurationSection configurationSection2 = configurationSection.getConfigurationSection(String.valueOf(i));
            UserShopSlotData userShopSlotData = userShopInstance.userShopSlotDataArrayList.get(i);
            configurationSection2.set("가격", userShopSlotData.getPrice());
            configurationSection2.set("주인", userShopSlotData.getPlayer());
            configurationSection2.set("아이템", userShopSlotData.getItemStack());
        }
        try {
            fileConfiguration.save(dataFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        getLogger().info("유저상점 데이터들을 정상적으로 저장하였습니다.");
    }

    @Override
    public void onDisable() {
        userShopDataSave();
        getLogger().info("유저상점 플러그인이 비활성화 되었습니다. 제작자: 참치");
    }
}
