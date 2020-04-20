package com.brandon.ecoboosters;

import com.brandon.ecoboosters.utils.cc;
import com.qrakn.phoenix.lang.file.type.BasicConfigurationFile;
import lombok.Getter;
import lombok.Setter;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Main extends JavaPlugin {

    @Getter @Setter public static BasicConfigurationFile mainConfig;
    @Getter @Setter public static BasicConfigurationFile dataConfig;

    public static Economy economy = null;

    public String moneyDuration;
    public String xpDuration;

    public boolean hasMoney2;
    public boolean hasMoney3;
    public boolean hasMoney4;
    public boolean hasXP2;
    public boolean hasXP3;
    public boolean hasXP4;

    public HashMap<Player, Integer> activeMoneyBoosters = new HashMap<>();
    public HashMap<Player, Integer> activeXpBoosters = new HashMap<>();

    @Override
    public void onEnable() {

        registerConfigs();
        registerCommands();
        registerEvents();
        parseDuration();

        if (!(setupEconomy())) {
            getLogger().severe("Vault not found!\n"+getDescription().getName());
            getServer().getPluginManager().disablePlugin(this);
        }

        List<String> test = new ArrayList<>(dataConfig.getStringList("player-data.pending-boosters.e61d49d2-395b-4e2c-987c-2ac697560daa.boosters"));
        for (String item : test) {
            getLogger().severe(item);
        }

        List<String> test2 = new ArrayList<>(getDataConfig().getStringList("player-data.pending-boosters.e61d49d2-395b-4e2c-987c-2ac697560daa.boosters"));
        for (String item : test2) {
            getLogger().severe(item);
        }

        getLogger().info("Plugin has loaded successfully!");

    }

    @Override
    public void onDisable() {
        try {
            mainConfig.getConfiguration().save(mainConfig.getFile());
            dataConfig.getConfiguration().save(dataConfig.getFile());
        } catch (IOException ignored) {}
    }

    public void registerConfigs() {
        mainConfig = new BasicConfigurationFile(this, "config");
        dataConfig = new BasicConfigurationFile(this, "data");
    }

    private boolean setupEconomy() {

        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> provider = getServer().getServicesManager().getRegistration(Economy.class);
        economy = provider.getProvider();
        return economy != null;

    }

    public void parseDuration() {

        int moneyD = mainConfig.getConfiguration().getInt("duration.money-booster");
        int xpD = mainConfig.getConfiguration().getInt("duration.xp-booster");

        if (moneyD % 60 == 0) {
            moneyDuration = moneyD / 60 + " hours";
        } else {
            moneyDuration = moneyD + " minutes";
        }
        if (xpD % 60 == 0) {
            xpDuration = xpD / 60 + " hours";
        } else {
            xpDuration = xpD + " minutes";
        }

    }

    public void registerCommands() { getCommand("boosters").setExecutor(new boosterCommand(this)); }

    public void registerEvents() { Bukkit.getPluginManager().registerEvents(new playerListener(this), this); }

    // GUI

    public void checkBoosters(Player player) {

        String uuid = player.getUniqueId().toString();
        hasMoney2 = false; hasMoney3 = false; hasMoney4 = false;
        hasXP2 = false; hasXP3 = false; hasXP4 = false;

        if (dataConfig.getConfiguration().getConfigurationSection("player-data.pending-boosters").getKeys(false).contains(uuid)) {

            ArrayList<String> playerBoosters = new ArrayList<>(dataConfig.getConfiguration().getStringList("player-data.pending-boosters."+uuid+".boosters"));

            for (String booster : playerBoosters) {
                String[] boosterData = booster.split("_");

                if (boosterData[0].equalsIgnoreCase("money")) {

                    switch (boosterData[1]) {
                        case "2":
                            hasMoney2 = true;
                            break;
                        case "3":
                            hasMoney3 = true;
                            break;
                        case "4":
                            hasMoney4 = true;
                            break;
                        default:
                            getLogger().severe("Cannot resolve booster data in data.yml file!");
                            hasMoney2 = false; hasMoney3 = false; hasMoney4 = false;
                            hasXP2 = false; hasXP3 = false; hasXP4 = false;
                    }

                } else if (boosterData[0].equalsIgnoreCase("xp")) {

                    switch (boosterData[1]) {
                        case "2":
                            hasXP2 = true;
                            break;
                        case "3":
                            hasXP3 = true;
                            break;
                        case "4":
                            hasXP4 = true;
                            break;
                        default:
                            getLogger().severe("Cannot resolve booster data in data.yml file!");
                            hasMoney2 = false; hasMoney3 = false; hasMoney4 = false;
                            hasXP2 = false; hasXP3 = false; hasXP4 = false;
                    }

                } else {

                    getLogger().severe("Cannot resolve booster data in data.yml file!");
                    hasMoney2 = false; hasMoney3 = false; hasMoney4 = false;
                    hasXP2 = false; hasXP3 = false; hasXP4 = false;

                }
            }

        }

    }

    public void applyMainGUI(Player player) {

        Inventory main_gui = Bukkit.createInventory(player, 27, cc.translate(mainConfig.getString("main-gui.name")));

        List<String> moneyLore = new ArrayList<>(cc.translate(mainConfig.getStringList("main-gui.money-booster.lore")));
        List<String> xpLore = new ArrayList<>(cc.translate(mainConfig.getStringList("main-gui.xp-booster.lore")));

        ItemStack moneyBooster = new ItemStack(Material.EMERALD);
        ItemStack xpBooster = new ItemStack(Material.DIAMOND);
        ItemMeta moneyBoosterMeta = moneyBooster.getItemMeta();
        ItemMeta xpBoosterMeta = xpBooster.getItemMeta();

        moneyBoosterMeta.setDisplayName(cc.translate(mainConfig.getString("main-gui.money-booster.name")));
        xpBoosterMeta.setDisplayName(cc.translate(mainConfig.getString("main-gui.xp-booster.name")));

        moneyBoosterMeta.setLore(moneyLore);
        xpBoosterMeta.setLore(xpLore);

        moneyBooster.setItemMeta(moneyBoosterMeta);
        xpBooster.setItemMeta(xpBoosterMeta);

        main_gui.setItem(12, moneyBooster);
        main_gui.setItem(14, xpBooster);

        ItemStack other = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 7);
        ItemMeta otherMeta = other.getItemMeta();
        otherMeta.setDisplayName(" ");
        other.setItemMeta(otherMeta);

        for (int i = 0; i < 27; i++) {
            if (main_gui.getItem(i) == null) {
                main_gui.setItem(i, other);
            }
        }

        player.openInventory(main_gui);

    }

    public void applyMoneyGUI(Player player) {

        checkBoosters(player);
        Inventory money_gui = Bukkit.createInventory(player, 54, cc.translate(mainConfig.getString("money-gui.name")));

        List<String> x2lore = new ArrayList<>(mainConfig.getStringList("money-gui.x2-lore"));
        List<String> x3lore = new ArrayList<>(mainConfig.getStringList("money-gui.x3-lore"));
        List<String> x4lore = new ArrayList<>(mainConfig.getStringList("money-gui.x4-lore"));

        ItemStack x2Booster = new ItemStack(Material.EMERALD);
        ItemStack x3Booster = new ItemStack(Material.EMERALD);
        ItemStack x4Booster = new ItemStack(Material.EMERALD);
        ItemStack x2Perm;
        ItemStack x3Perm;
        ItemStack x4Perm;

        if (hasMoney2) {
            x2Perm = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 5);
            ItemMeta meta = x2Perm.getItemMeta();
            meta.setDisplayName(cc.translate(mainConfig.getString("permission.true")));
            x2Perm.setItemMeta(meta);
        } else {
            x2Perm = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 14);
            ItemMeta meta = x2Perm.getItemMeta();
            meta.setDisplayName(cc.translate(mainConfig.getString("permission.false")));
            x2Perm.setItemMeta(meta);
        }
        if (hasMoney3) {
            x3Perm = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 5);
            ItemMeta meta = x3Perm.getItemMeta();
            meta.setDisplayName(cc.translate(mainConfig.getString("permission.true")));
            x3Perm.setItemMeta(meta);
        } else {
            x3Perm = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 14);
            ItemMeta meta = x3Perm.getItemMeta();
            meta.setDisplayName(cc.translate(mainConfig.getString("permission.false")));
            x3Perm.setItemMeta(meta);
        }
        if (hasMoney4) {
            x4Perm = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 5);
            ItemMeta meta = x4Perm.getItemMeta();
            meta.setDisplayName(cc.translate(mainConfig.getString("permission.true")));
            x4Perm.setItemMeta(meta);
        } else {
            x4Perm = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 14);
            ItemMeta meta = x4Perm.getItemMeta();
            meta.setDisplayName(cc.translate(mainConfig.getString("permission.false")));
            x4Perm.setItemMeta(meta);
        }

        ItemMeta x2BoosterMeta = x2Booster.getItemMeta();
        ItemMeta x3BoosterMeta = x3Booster.getItemMeta();
        ItemMeta x4BoosterMeta = x4Booster.getItemMeta();

        x2BoosterMeta.setDisplayName(cc.translate(mainConfig.getString("money-gui.x2-name").replace("{duration}", moneyDuration)));
        x3BoosterMeta.setDisplayName(cc.translate(mainConfig.getString("money-gui.x3-name").replace("{duration}", moneyDuration)));
        x4BoosterMeta.setDisplayName(cc.translate(mainConfig.getString("money-gui.x4-name").replace("{duration}", moneyDuration)));

        List<String> newx2Lore = new ArrayList<>();
        List<String> newx3Lore = new ArrayList<>();
        List<String> newx4Lore = new ArrayList<>();

        for (String lore : x2lore) {
            newx2Lore.add(cc.translate(lore.replace("{duration}", moneyDuration)));
        }
        for (String lore : x3lore) {
            newx3Lore.add(cc.translate(lore.replace("{duration}", moneyDuration)));
        }
        for (String lore : x4lore) {
            newx4Lore.add(cc.translate(lore.replace("{duration}", moneyDuration)));
        }

        x2BoosterMeta.setLore(newx2Lore);
        x3BoosterMeta.setLore(newx3Lore);
        x4BoosterMeta.setLore(newx4Lore);

        x2Booster.setItemMeta(x2BoosterMeta);
        x3Booster.setItemMeta(x3BoosterMeta);
        x4Booster.setItemMeta(x4BoosterMeta);

        ItemStack cancel = new ItemStack(Material.ARROW);
        ItemMeta cancelMeta = cancel.getItemMeta();
        cancelMeta.setDisplayName(cc.translate(mainConfig.getString("money-gui.cancel")));
        cancel.setItemMeta(cancelMeta);

        money_gui.setItem(20, x2Booster);
        money_gui.setItem(22, x3Booster);
        money_gui.setItem(24, x4Booster);
        money_gui.setItem(29, x2Perm);
        money_gui.setItem(31, x3Perm);
        money_gui.setItem(33, x4Perm);
        money_gui.setItem(45, cancel);

        ItemStack other = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 7);
        ItemMeta otherMeta = other.getItemMeta();
        otherMeta.setDisplayName(" ");
        other.setItemMeta(otherMeta);

        for (int i = 0; i < 54; i++) {
            if (money_gui.getItem(i) == null) {
                money_gui.setItem(i, other);
            }
        }

        player.openInventory(money_gui);

    }

    public void applyXPGUI(Player player) {

        checkBoosters(player);
        Inventory xp_gui = Bukkit.createInventory(player, 54, cc.translate(mainConfig.getString("xp-gui.name")));

        List<String> x2lore = new ArrayList<>(mainConfig.getStringList("xp-gui.x2-lore"));
        List<String> x3lore = new ArrayList<>(mainConfig.getStringList("xp-gui.x3-lore"));
        List<String> x4lore = new ArrayList<>(mainConfig.getStringList("xp-gui.x4-lore"));

        ItemStack x2Booster = new ItemStack(Material.DIAMOND);
        ItemStack x3Booster = new ItemStack(Material.DIAMOND);
        ItemStack x4Booster = new ItemStack(Material.DIAMOND);
        ItemStack x2Perm;
        ItemStack x3Perm;
        ItemStack x4Perm;

        if (hasXP2) {
            x2Perm = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 5);
            ItemMeta meta = x2Perm.getItemMeta();
            meta.setDisplayName(cc.translate(mainConfig.getString("permission.true")));
            x2Perm.setItemMeta(meta);
        } else {
            x2Perm = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 14);
            ItemMeta meta = x2Perm.getItemMeta();
            meta.setDisplayName(cc.translate(mainConfig.getString("permission.false")));
            x2Perm.setItemMeta(meta);
        }
        if (hasXP3) {
            x3Perm = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 5);
            ItemMeta meta = x3Perm.getItemMeta();
            meta.setDisplayName(cc.translate(mainConfig.getString("permission.true")));
            x3Perm.setItemMeta(meta);
        } else {
            x3Perm = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 14);
            ItemMeta meta = x3Perm.getItemMeta();
            meta.setDisplayName(cc.translate(mainConfig.getString("permission.false")));
            x3Perm.setItemMeta(meta);
        }
        if (hasXP4) {
            x4Perm = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 5);
            ItemMeta meta = x4Perm.getItemMeta();
            meta.setDisplayName(cc.translate(mainConfig.getString("permission.true")));
            x4Perm.setItemMeta(meta);
        } else {
            x4Perm = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 14);
            ItemMeta meta = x4Perm.getItemMeta();
            meta.setDisplayName(cc.translate(mainConfig.getString("permission.false")));
            x4Perm.setItemMeta(meta);
        }

        ItemMeta x2BoosterMeta = x2Booster.getItemMeta();
        ItemMeta x3BoosterMeta = x3Booster.getItemMeta();
        ItemMeta x4BoosterMeta = x4Booster.getItemMeta();

        x2BoosterMeta.setDisplayName(cc.translate(mainConfig.getString("xp-gui.x2-name").replace("{duration}", xpDuration)));
        x3BoosterMeta.setDisplayName(cc.translate(mainConfig.getString("xp-gui.x3-name").replace("{duration}", xpDuration)));
        x4BoosterMeta.setDisplayName(cc.translate(mainConfig.getString("xp-gui.x4-name").replace("{duration}", xpDuration)));

        List<String> newx2Lore = new ArrayList<>();
        List<String> newx3Lore = new ArrayList<>();
        List<String> newx4Lore = new ArrayList<>();

        for (String lore : x2lore) {
            newx2Lore.add(cc.translate(lore.replace("{duration}", xpDuration)));
        }
        for (String lore : x3lore) {
            newx3Lore.add(cc.translate(lore.replace("{duration}", xpDuration)));
        }
        for (String lore : x4lore) {
            newx4Lore.add(cc.translate(lore.replace("{duration}", xpDuration)));
        }

        x2BoosterMeta.setLore(newx2Lore);
        x3BoosterMeta.setLore(newx3Lore);
        x4BoosterMeta.setLore(newx4Lore);

        x2Booster.setItemMeta(x2BoosterMeta);
        x3Booster.setItemMeta(x3BoosterMeta);
        x4Booster.setItemMeta(x4BoosterMeta);

        ItemStack cancel = new ItemStack(Material.ARROW);
        ItemMeta cancelMeta = cancel.getItemMeta();
        cancelMeta.setDisplayName(cc.translate(mainConfig.getString("xp-gui.cancel")));
        cancel.setItemMeta(cancelMeta);

        xp_gui.setItem(20, x2Booster);
        xp_gui.setItem(22, x3Booster);
        xp_gui.setItem(24, x4Booster);
        xp_gui.setItem(29, x2Perm);
        xp_gui.setItem(31, x3Perm);
        xp_gui.setItem(33, x4Perm);
        xp_gui.setItem(45, cancel);

        ItemStack other = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 7);
        ItemMeta otherMeta = other.getItemMeta();
        otherMeta.setDisplayName(" ");
        other.setItemMeta(otherMeta);

        for (int i = 0; i < 54; i++) {
            if (xp_gui.getItem(i) == null) {
                xp_gui.setItem(i, other);
            }
        }

        player.openInventory(xp_gui);

    }

}
