package com.brandon.ecoboosters;

import com.brandon.ecoboosters.utils.cc;
import net.ess3.api.events.UserBalanceUpdateEvent;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class playerListener implements Listener {

    private Main main;
    public playerListener(Main main) { this.main = main; }

    public static YamlConfiguration config = Main.getMainConfig().getConfiguration();
    public static YamlConfiguration dataConfig = Main.getDataConfig().getConfiguration();

    String booster;
    String message;
    int multiplier;
    int duration;

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();

        if (e.getView().getTitle().equals(cc.translate(config.getString("main-gui.name")))) {

            if (e.getCurrentItem() != null) {
                e.setCancelled(true);
                reset(player);
                switch (e.getCurrentItem().getType()) {
                    case STAINED_GLASS_PANE:
                        break;
                    case EMERALD:
                        main.applyMoneyGUI(player);
                        break;
                    case DIAMOND:
                        main.applyXPGUI(player);
                        break;
                    default:
                        return;
                }
            }

        } else if (e.getView().getTitle().equals(cc.translate(config.getString("money-gui.name")))) {

            if (e.getCurrentItem().getType() != null) {
                e.setCancelled(true);
                reset(player);
                switch (e.getCurrentItem().getType()) {
                    case STAINED_GLASS_PANE:
                        break;
                    case EMERALD:

                        if (!main.activeMoneyBoosters.containsKey(player)) {

                            if (e.getSlot() == 20) { // x2 MONEY BOOSTER

                                if (main.hasMoney2) {

                                    removeBooster(player, "money_2");

                                    booster = "x2 Money Booster";
                                    multiplier = 2;
                                    duration = config.getInt("duration.money-booster");
                                    duration = duration*60;
                                    duration = duration*20;

                                    message = config.getString("messages.on-activate");
                                    message = message.replace("{booster}", booster).replace("{duration}", main.moneyDuration);

                                    main.activeMoneyBoosters.put(player, multiplier);

                                    Bukkit.getScheduler().runTaskLater(main, new Runnable() {
                                        public void run() {
                                            main.activeMoneyBoosters.remove(player);
                                            player.sendMessage(cc.translate(config.getString("messages.on-expire")));
                                        }
                                    }, duration);

                                    player.sendMessage(cc.translate(message));
                                } else {
                                    player.sendMessage(cc.translate(config.getString("messages.no-permission")));
                                }
                                player.closeInventory();

                            } else if (e.getSlot() == 22) { // x3

                                if (main.hasMoney3) {

                                    removeBooster(player, "money_3");

                                    booster = "x3 Money Booster";
                                    multiplier = 3;
                                    duration = config.getInt("duration.money-booster");
                                    duration = duration*60;
                                    duration = duration*20;

                                    message = config.getString("messages.on-activate");
                                    message = message.replace("{booster}", booster).replace("{duration}", main.moneyDuration);

                                    main.activeMoneyBoosters.put(player, multiplier);

                                    Bukkit.getScheduler().runTaskLater(main, new Runnable() {
                                        public void run() {
                                            main.activeMoneyBoosters.remove(player);
                                            player.sendMessage(cc.translate(config.getString("messages.on-expire")));
                                        }
                                    }, duration);

                                    player.sendMessage(cc.translate(message));
                                } else {
                                    player.sendMessage(cc.translate(config.getString("messages.no-permission")));
                                }
                                player.closeInventory();

                            } else if (e.getSlot() == 24) { // x4

                                if (main.hasMoney4) {

                                    removeBooster(player, "money_4");

                                    booster = "x4 Money Booster";
                                    multiplier = 4;
                                    duration = config.getInt("duration.money-booster");
                                    duration = duration*60;
                                    duration = duration*20;

                                    message = config.getString("messages.on-activate");
                                    message = message.replace("{booster}", booster).replace("{duration}", main.moneyDuration);

                                    main.activeMoneyBoosters.put(player, multiplier);

                                    Bukkit.getScheduler().runTaskLater(main, new Runnable() {
                                        public void run() {
                                            main.activeMoneyBoosters.remove(player);
                                            player.sendMessage(cc.translate(config.getString("messages.on-expire")));
                                        }
                                    }, duration);

                                    player.sendMessage(cc.translate(message));
                                } else {
                                    player.sendMessage(cc.translate(config.getString("messages.no-permission")));
                                }
                                player.closeInventory();

                            }
                        } else {
                            player.sendMessage(cc.translate(config.getString("messages.already-active-booster")));
                        }

                    case ARROW:
                        main.applyMainGUI(player);
                        break;
                    default:
                        return;
                }
            }

        } else if (e.getView().getTitle().equals(cc.translate(config.getString("xp-gui.name")))) {

            if (e.getCurrentItem().getType() != null) {
                e.setCancelled(true);
                reset(player);
                switch (e.getCurrentItem().getType()) {
                    case STAINED_GLASS_PANE:
                        break;
                    case DIAMOND:

                        if (!main.activeXpBoosters.containsKey(player)) {
                            if (e.getSlot() == 20) { // x2 XP BOOSTER

                                if (main.hasXP2) {

                                    removeBooster(player, "xp_2");

                                    booster = "x2 Xp Booster";
                                    multiplier = 2;
                                    duration = config.getInt("duration.xp-booster");
                                    duration = duration*60;
                                    duration = duration*20;

                                    message = config.getString("messages.on-activate");
                                    message = message.replace("{booster}", booster).replace("{duration}", main.xpDuration);

                                    main.activeXpBoosters.put(player, multiplier);

                                    Bukkit.getScheduler().runTaskLater(main, new Runnable() {
                                        public void run() {
                                            main.activeXpBoosters.remove(player);
                                            player.sendMessage(cc.translate(config.getString("messages.on-expire")));
                                        }
                                    }, duration);

                                    player.sendMessage(cc.translate(message));
                                } else {
                                    player.sendMessage(cc.translate(config.getString("messages.no-permission")));
                                }
                                player.closeInventory();

                            } else if (e.getSlot() == 22) { // x3

                                if (main.hasXP3) {

                                    removeBooster(player, "xp_3");

                                    booster = "x3 Xp Booster";
                                    multiplier = 3;
                                    duration = config.getInt("duration.xp-booster");
                                    duration = duration*60;
                                    duration = duration*20;

                                    message = config.getString("messages.on-activate");
                                    message = message.replace("{booster}", booster).replace("{duration}", main.xpDuration);

                                    main.activeXpBoosters.put(player, multiplier);

                                    Bukkit.getScheduler().runTaskLater(main, new Runnable() {
                                        public void run() {
                                            main.activeXpBoosters.remove(player);
                                            player.sendMessage(cc.translate(config.getString("messages.on-expire")));
                                        }
                                    }, duration);

                                    player.sendMessage(cc.translate(message));
                                } else {
                                    player.sendMessage(cc.translate(config.getString("messages.no-permission")));
                                }
                                player.closeInventory();

                            } else if (e.getSlot() == 24) { // x4

                                if (main.hasXP4) {

                                    removeBooster(player, "xp_4");

                                    booster = "x4 Xp Booster";
                                    multiplier = 4;
                                    duration = config.getInt("duration.xp-booster");
                                    duration = duration*60;
                                    duration = duration*20;

                                    message = config.getString("messages.on-activate");
                                    message = message.replace("{booster}", booster).replace("{duration}", main.xpDuration);

                                    main.activeXpBoosters.put(player, multiplier);

                                    Bukkit.getScheduler().runTaskLater(main, new Runnable() {
                                        public void run() {
                                            main.activeXpBoosters.remove(player);
                                            player.sendMessage(cc.translate(config.getString("messages.on-expire")));
                                        }
                                    }, duration);

                                    player.sendMessage(cc.translate(message));
                                } else {
                                    player.sendMessage(cc.translate(config.getString("messages.no-permission")));
                                }
                                player.closeInventory();

                            }
                        } else {
                            player.sendMessage(cc.translate(config.getString("messages.already-active-booster")));
                        }

                        break;
                    case ARROW:
                        main.applyMainGUI(player);
                        break;
                    default:
                        return;
                }
            }

        }

    }

    public void reset(Player player) {
        String booster = null;
        String message = null;
        int multiplier = 0;
        int duration = 0;
        main.checkBoosters(player);
    }

    public void removeBooster(Player player, String booster) {
        String uuid = player.getUniqueId().toString();

        List<String> boosterList = new ArrayList<>(dataConfig.getStringList("player-data.pending-boosters." + uuid + ".boosters"));

        boosterList.remove(booster);
        dataConfig.set("player-data.pending-boosters." + uuid + ".boosters", boosterList);

        try {
            dataConfig.save(Main.dataConfig.getFile());
        } catch (IOException ignored) {}

        reloadConfig();
        reset(player);

    }

    public void reloadConfig() {
        try {
            Main.getMainConfig().getConfiguration().load(Main.getMainConfig().getFile());
            Main.getDataConfig().getConfiguration().load(Main.getDataConfig().getFile());
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }

    }

    public void addBooster(Player player, String booster) {

        String uuid = player.getUniqueId().toString();

        if (dataConfig.getConfigurationSection("player-data.pending-boosters").getKeys(false).contains(uuid)) {

            List<String> boosterList = new ArrayList<>(dataConfig.getStringList("player-data.pending-boosters." + uuid + ".boosters"));
            boosterList.add(booster);
            dataConfig.set("player-data.pending-boosters." + uuid + ".boosters", boosterList);

        } else {

            String name = player.getName();
            dataConfig.set("player-data.pending-boosters." + uuid + ".name", name);
            List<String> boosterList = new ArrayList<>();
            boosterList.add(booster);
            dataConfig.set("player-data.pending-boosters." + uuid + ".boosters", boosterList);

        }

        saveConfig();
        reloadConfig();

    }

    public void saveConfig() {
        try {
            config.save(Main.mainConfig.getFile());
            dataConfig.save(Main.dataConfig.getFile());
        } catch (IOException ignored) {}
    }

    @EventHandler
    public void onClick(PlayerInteractEvent e) {
        if (e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            Player player = e.getPlayer();
            if (player.getItemInHand().getType().equals(Material.EMERALD)) {

                List<String> x2lore = new ArrayList<>(config.getStringList("money-gui.x2-lore"));
                List<String> x3lore = new ArrayList<>(config.getStringList("money-gui.x3-lore"));
                List<String> x4lore = new ArrayList<>(config.getStringList("money-gui.x4-lore"));
                List<String> newx2Lore = new ArrayList<>();
                for (String item : x2lore) {
                    newx2Lore.add(cc.translate(item.replace("{duration}", main.moneyDuration)));
                }
                List<String> newx3Lore = new ArrayList<>();
                for (String item : x3lore) {
                    newx3Lore.add(cc.translate(item.replace("{duration}", main.moneyDuration)));
                }
                List<String> newx4Lore = new ArrayList<>();
                for (String item : x4lore) {
                    newx4Lore.add(cc.translate(item.replace("{duration}", main.moneyDuration)));
                }

                ItemMeta meta = player.getItemInHand().getItemMeta();
                if (meta.getDisplayName().equalsIgnoreCase(cc.translate(config.getString("money-gui.x2-name").replace("{duration}", main.moneyDuration)))) {
                    if (meta.getLore().equals(newx2Lore)) {
                        addBooster(player, "money_2");
                        player.getInventory().setItemInHand(new ItemStack(Material.AIR));
                        player.sendMessage(cc.translate(config.getString("messages.on-redeem").replace("{booster}", "x2 Money Booster")));
                    }
                } else if (meta.getDisplayName().equalsIgnoreCase(cc.translate(config.getString("money-gui.x3-name").replace("{duration}", main.moneyDuration)))) {
                    if (meta.getLore().equals(newx3Lore)) {
                        addBooster(player, "money_3");
                        player.getInventory().setItemInHand(new ItemStack(Material.AIR));
                        player.sendMessage(cc.translate(config.getString("messages.on-redeem").replace("{booster}", "x3 Money Booster")));
                    }
                } else if (meta.getDisplayName().equalsIgnoreCase(cc.translate(config.getString("money-gui.x4-name").replace("{duration}", main.moneyDuration)))) {
                    if (meta.getLore().equals(newx4Lore)) {
                        addBooster(player, "money_4");
                        player.getInventory().setItemInHand(new ItemStack(Material.AIR));
                        player.sendMessage(cc.translate(config.getString("messages.on-redeem").replace("{booster}", "x4 Money Booster")));
                    }
                }

            } else if (player.getItemInHand().getType().equals(Material.DIAMOND)) {

                List<String> x2lore = new ArrayList<>(config.getStringList("xp-gui.x2-lore"));
                List<String> x3lore = new ArrayList<>(config.getStringList("xp-gui.x3-lore"));
                List<String> x4lore = new ArrayList<>(config.getStringList("xp-gui.x4-lore"));
                List<String> newx2Lore = new ArrayList<>();
                for (String item : x2lore) {
                    newx2Lore.add(cc.translate(item.replace("{duration}", main.xpDuration)));
                }
                List<String> newx3Lore = new ArrayList<>();
                for (String item : x3lore) {
                    newx3Lore.add(cc.translate(item.replace("{duration}", main.xpDuration)));
                }
                List<String> newx4Lore = new ArrayList<>();
                for (String item : x4lore) {
                    newx4Lore.add(cc.translate(item.replace("{duration}", main.xpDuration)));
                }

                ItemMeta meta = player.getItemInHand().getItemMeta();
                if (meta.getDisplayName().equalsIgnoreCase(cc.translate(config.getString("xp-gui.x2-name").replace("{duration}", main.xpDuration)))) {
                    if (meta.getLore().equals(newx2Lore)) {
                        addBooster(player, "xp_2");
                        player.getInventory().setItemInHand(new ItemStack(Material.AIR));
                        player.sendMessage(cc.translate(config.getString("messages.on-redeem").replace("{booster}", "x2 Xp Booster")));
                    }
                } else if (meta.getDisplayName().equalsIgnoreCase(cc.translate(config.getString("xp-gui.x3-name").replace("{duration}", main.xpDuration)))) {
                    if (meta.getLore().equals(newx3Lore)) {
                        addBooster(player, "xp_3");
                        player.getInventory().setItemInHand(new ItemStack(Material.AIR));
                        player.sendMessage(cc.translate(config.getString("messages.on-redeem").replace("{booster}", "x3 Xp Booster")));
                    }
                } else if (meta.getDisplayName().equalsIgnoreCase(cc.translate(config.getString("xp-gui.x4-name").replace("{duration}", main.xpDuration)))) {
                    if (meta.getLore().equals(newx4Lore)) {
                        addBooster(player, "xp_4");
                        player.getInventory().setItemInHand(new ItemStack(Material.AIR));
                        player.sendMessage(cc.translate(config.getString("messages.on-redeem").replace("{booster}", "x4 Xp Booster")));
                    }
                }

            }
        }
    }

    /* BOOSTERS EVENTS */

    @EventHandler
    public void onBalanceUpdate(UserBalanceUpdateEvent e) {

        Player player = e.getPlayer();
        if (main.activeMoneyBoosters.containsKey(player)) {
            if (e.getCause() != UserBalanceUpdateEvent.Cause.COMMAND_ECO && e.getCause() != UserBalanceUpdateEvent.Cause.COMMAND_PAY) {
                BigDecimal difference = e.getNewBalance().subtract(e.getOldBalance());
                e.setNewBalance(e.getOldBalance().add(difference.multiply(BigDecimal.valueOf(main.activeMoneyBoosters.get(player)))));
            }
        }
    }

    @EventHandler
    public void onXpGet(PlayerExpChangeEvent e) {

        Player player = e.getPlayer();
        if (main.activeXpBoosters.containsKey(player)) {
            e.setAmount(e.getAmount() * (main.activeXpBoosters.get(player)));
        }

    }

}
