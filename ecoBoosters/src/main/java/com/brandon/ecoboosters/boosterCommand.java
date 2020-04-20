package com.brandon.ecoboosters;

import com.brandon.ecoboosters.utils.cc;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class boosterCommand implements CommandExecutor {

    private Main main;
    public boosterCommand(Main main) { this.main = main; }

    public static YamlConfiguration config = Main.getMainConfig().getConfiguration();
    public static YamlConfiguration dataConfig = Main.getDataConfig().getConfiguration();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        /*

        /boosters - boosters.use
        /boosters giveall <money,xp> <2,3,4> - boosters.admin.giveall
        /boosters give <player> <money,xp> <2,3,4> - boosters.admin.give
        /boosters remove <player> <money,xp> <2,3,4> - boosters.admin.remove
        /boosters <rl,reload> - boosters.admin.reload

         */

        if (args.length == 0) {
            if (!(sender instanceof Player)) {
                main.getLogger().severe("You cannot run this command from console!");
            } else {
                Player player = (Player) sender;
                if (player.hasPermission("boosters.use")) {
                    main.applyMainGUI(player);
                } else {
                    player.sendMessage(cc.translate(config.getString("messages.no-permission")));
                }
            }
        } else if (args.length == 1) {
            if (args[0].equalsIgnoreCase("rl") || args[0].equalsIgnoreCase("reload")) {
                if (!(sender instanceof Player)) {
                    main.getLogger().info("Plugin has been reloaded!");
                } else {
                    if (sender.hasPermission("boosters.admin.reload")) {
                        sender.sendMessage(cc.translate(config.getString("messages.on-reload")));
                    } else {
                        sender.sendMessage(cc.translate(config.getString("messages.no-permission")));
                    }
                }
                reloadConfig();
            } else {
                if (!(sender instanceof Player)) {
                    main.getLogger().severe("Invalid usage! Do /boosters!");
                } else {
                    sender.sendMessage(cc.translate(config.getString("messages.invalid-usage")));
                }
            }
        } else if (args.length == 3) {
            if (args[0].equalsIgnoreCase("giveall")) {
                if (args[1].equalsIgnoreCase("money") || args[1].equalsIgnoreCase("xp")) {
                    if (args[2].equalsIgnoreCase("2") || args[2].equalsIgnoreCase("3") || args[2].equalsIgnoreCase("4")) {

                        String booster;
                        String message;

                        if (args[1].equalsIgnoreCase("money")) {
                            if (args[2].equalsIgnoreCase("2")) {
                                booster = "money_2";
                                message = "x2 Money Booster";
                            } else if (args[2].equalsIgnoreCase("3")) {
                                booster = "money_3";
                                message = "x3 Money Booster";
                            } else {
                                booster = "money_4";
                                message = "x4 Money Booster";
                            }
                        } else {
                            if (args[2].equalsIgnoreCase("2")) {
                                booster = "xp_2";
                                message = "x2 Xp Booster";
                            } else if (args[2].equalsIgnoreCase("3")) {
                                booster = "xp_3";
                                message = "x3 Xp Booster";
                            } else {
                                booster = "xp_4";
                                message = "x4 Xp Booster";
                            }
                        }

                        if (!(sender instanceof Player)) {
                            for (Player target : Bukkit.getOnlinePlayers()) {
                                addBooster(target, booster);
                            }
                            main.getLogger().info("All players have been rewarded a "+message);
                        } else {
                            if (sender.hasPermission("boosters.admin.giveall")) {
                                for (Player target : Bukkit.getOnlinePlayers()) {
                                    addBooster(target, booster);
                                }
                                sender.sendMessage(cc.translate(config.getString("messages.on-giveall").replace("{booster}", message)));
                            } else {
                                sender.sendMessage(cc.translate(config.getString("messages.no-permission")));
                            }
                        }

                    } else {
                        if (!(sender instanceof Player)) {
                            main.getLogger().severe("Invalid usage! Do /boosters!");
                        } else {
                            sender.sendMessage(cc.translate(config.getString("messages.invalid-usage")));
                        }
                    }
                } else {
                    if (!(sender instanceof Player)) {
                        main.getLogger().severe("Invalid usage! Do /boosters!");
                    } else {
                        sender.sendMessage(cc.translate(config.getString("messages.invalid-usage")));
                    }
                }
            } else {
                if (!(sender instanceof Player)) {
                    main.getLogger().severe("Invalid usage! Do /boosters!");
                } else {
                    sender.sendMessage(cc.translate(config.getString("messages.invalid-usage")));
                }
            }
        } else if (args.length == 4) {

            if (args[0].equalsIgnoreCase("give")) {

                if (Bukkit.getPlayerExact(args[1]) != null) {
                    Player target = Bukkit.getPlayerExact(args[1]);
                    if (args[2].equalsIgnoreCase("money") || args[2].equalsIgnoreCase("xp")) {
                        if (args[3].equalsIgnoreCase("2") || args[3].equalsIgnoreCase("3") || args[3].equalsIgnoreCase("4")) {

                            String booster;
                            String message;

                            if (args[2].equalsIgnoreCase("money")) {
                                if (args[3].equalsIgnoreCase("2")) {
                                    booster = "money_2";
                                    message = "x2 Money Booster";
                                } else if (args[3].equalsIgnoreCase("3")) {
                                    booster = "money_3";
                                    message = "x3 Money Booster";
                                } else {
                                    booster = "money_4";
                                    message = "x4 Money Booster";
                                }
                            } else {
                                if (args[3].equalsIgnoreCase("2")) {
                                    booster = "xp_2";
                                    message = "x2 Xp Booster";
                                } else if (args[3].equalsIgnoreCase("3")) {
                                    booster = "xp_3";
                                    message = "x3 Xp Booster";
                                } else {
                                    booster = "xp_4";
                                    message = "x4 Xp Booster";
                                }
                            }

                            if (!(sender instanceof Player)) {

                                addBooster(target, booster);
                                main.getLogger().info(target.getName() + " has been rewared a "+message);

                            } else {
                                if (sender.hasPermission("boosters.admin.give")) {
                                    addBooster(target, booster);
                                    sender.sendMessage(cc.translate(config.getString("messages.on-give").replace("{player}", target.getName()).replace("{booster}", message)));
                                } else {
                                    sender.sendMessage(cc.translate(config.getString("messages.no-permission")));
                                }
                            }

                        } else {
                            if (!(sender instanceof Player)) {
                                main.getLogger().severe("Invalid usage! Do /boosters!");
                            } else {
                                sender.sendMessage(cc.translate(config.getString("messages.invalid-usage")));
                            }
                        }

                    } else {
                        if (!(sender instanceof Player)) {
                            main.getLogger().severe("Invalid usage! Do /boosters!");
                        } else {
                            sender.sendMessage(cc.translate(config.getString("messages.invalid-usage")));
                        }
                    }

                } else {
                    if (!(sender instanceof Player)) {
                        main.getLogger().severe("Player not found!");
                    } else {
                        sender.sendMessage(cc.translate(config.getString("messages.invalid-player")));
                    }
                }

            } else if (args[0].equalsIgnoreCase("remove")) {

                if (Bukkit.getPlayerExact(args[1]) != null) {
                    Player target = Bukkit.getPlayerExact(args[1]);
                    if (args[2].equalsIgnoreCase("money") || args[2].equalsIgnoreCase("xp")) {
                        if (args[3].equalsIgnoreCase("2") || args[3].equalsIgnoreCase("3") || args[3].equalsIgnoreCase("4")) {

                            String booster;

                            if (args[2].equalsIgnoreCase("money")) {
                                if (args[3].equalsIgnoreCase("2")) {
                                    booster = "money_2";
                                } else if (args[3].equalsIgnoreCase("3")) {
                                    booster = "money_3";
                                } else {
                                    booster = "money_4";
                                }
                            } else {
                                if (args[3].equalsIgnoreCase("2")) {
                                    booster = "xp_2";
                                } else if (args[3].equalsIgnoreCase("3")) {
                                    booster = "xp_3";
                                } else {
                                    booster = "xp_4";
                                }
                            }

                            if (!(sender instanceof Player)) {
                                removeBoosterViaConsole(target, booster);
                            } else {
                                if (sender.hasPermission("boosters.admin.give")) {
                                    removeBoosterViaPlayer(target, booster, ((Player)sender));
                                } else {
                                    sender.sendMessage(cc.translate(config.getString("messages.no-permission")));
                                }
                            }

                        } else {
                            if (!(sender instanceof Player)) {
                                main.getLogger().severe("Invalid usage! Do /boosters!");
                            } else {
                                sender.sendMessage(cc.translate(config.getString("messages.invalid-usage")));
                            }
                        }

                    } else {
                        if (!(sender instanceof Player)) {
                            main.getLogger().severe("Invalid usage! Do /boosters!");
                        } else {
                            sender.sendMessage(cc.translate(config.getString("messages.invalid-usage")));
                        }
                    }

                } else {
                    if (!(sender instanceof Player)) {
                        main.getLogger().severe("Player not found!");
                    } else {
                        sender.sendMessage(cc.translate(config.getString("messages.invalid-player")));
                    }
                }

            } else {
                if (!(sender instanceof Player)) {
                    main.getLogger().severe("Invalid usage! Do /boosters!");
                } else {
                    sender.sendMessage(cc.translate(config.getString("messages.invalid-usage")));
                }
            }

        } else {
            if (!(sender instanceof Player)) {
                main.getLogger().severe("Invalid usage! Do /boosters!");
            } else {
                sender.sendMessage(cc.translate(config.getString("messages.invalid-usage")));
            }
        }

        return false;
    }

    public void addBooster(Player player, String booster) {

        String uuid = player.getUniqueId().toString();
        String bm;

        if (booster.equalsIgnoreCase("money_2")) {

            bm = "x2 Money Booster";
            List<String> lore = new ArrayList<>(config.getStringList("money-gui.x2-lore"));
            ItemStack Booster = new ItemStack(Material.EMERALD);
            ItemMeta BoosterMeta = Booster.getItemMeta();
            BoosterMeta.setDisplayName(cc.translate(config.getString("money-gui.x2-name").replace("{duration}", main.moneyDuration)));

            List<String> newLore = new ArrayList<>();
            for (String item : lore) {
                newLore.add(cc.translate(item.replace("{duration}", main.moneyDuration)));
            }

            BoosterMeta.setLore(newLore);
            Booster.setItemMeta(BoosterMeta);

            player.getInventory().addItem(Booster);
            player.sendMessage(cc.translate(config.getString("messages.on-receive").replace("{booster}", bm)));

        } else if (booster.equalsIgnoreCase("money_3")) {
            bm = "x3 Money Booster";
            List<String> lore = new ArrayList<>(config.getStringList("money-gui.x3-lore"));
            ItemStack Booster = new ItemStack(Material.EMERALD);
            ItemMeta BoosterMeta = Booster.getItemMeta();
            BoosterMeta.setDisplayName(cc.translate(config.getString("money-gui.x3-name").replace("{duration}", main.moneyDuration)));

            List<String> newLore = new ArrayList<>();
            for (String item : lore) {
                newLore.add(cc.translate(item.replace("{duration}", main.moneyDuration)));
            }

            BoosterMeta.setLore(newLore);
            Booster.setItemMeta(BoosterMeta);

            player.getInventory().addItem(Booster);
            player.sendMessage(cc.translate(config.getString("messages.on-receive").replace("{booster}", bm)));

        } else if (booster.equalsIgnoreCase("money_4")) {

            bm = "x4 Money Booster";
            List<String> lore = new ArrayList<>(config.getStringList("money-gui.x4-lore"));
            ItemStack Booster = new ItemStack(Material.EMERALD);
            ItemMeta BoosterMeta = Booster.getItemMeta();
            BoosterMeta.setDisplayName(cc.translate(config.getString("money-gui.x4-name").replace("{duration}", main.moneyDuration)));

            List<String> newLore = new ArrayList<>();
            for (String item : lore) {
                newLore.add(cc.translate(item.replace("{duration}", main.moneyDuration)));
            }

            BoosterMeta.setLore(newLore);
            Booster.setItemMeta(BoosterMeta);

            player.getInventory().addItem(Booster);
            player.sendMessage(cc.translate(config.getString("messages.on-receive").replace("{booster}", bm)));

        } else if (booster.equalsIgnoreCase("xp_2")) {

            bm = "x2 XP Booster";
            List<String> lore = new ArrayList<>(config.getStringList("xp-gui.x2-lore"));
            ItemStack Booster = new ItemStack(Material.DIAMOND);
            ItemMeta BoosterMeta = Booster.getItemMeta();
            BoosterMeta.setDisplayName(cc.translate(config.getString("xp-gui.x2-name").replace("{duration}", main.moneyDuration)));

            List<String> newLore = new ArrayList<>();
            for (String item : lore) {
                newLore.add(cc.translate(item.replace("{duration}", main.moneyDuration)));
            }

            BoosterMeta.setLore(newLore);
            Booster.setItemMeta(BoosterMeta);

            player.getInventory().addItem(Booster);
            player.sendMessage(cc.translate(config.getString("messages.on-receive").replace("{booster}", bm)));

        } else if (booster.equalsIgnoreCase("xp_3")) {

            bm = "x3 XP Booster";
            List<String> lore = new ArrayList<>(config.getStringList("xp-gui.x3-lore"));
            ItemStack Booster = new ItemStack(Material.DIAMOND);
            ItemMeta BoosterMeta = Booster.getItemMeta();
            BoosterMeta.setDisplayName(cc.translate(config.getString("xp-gui.x3-name").replace("{duration}", main.moneyDuration)));

            List<String> newLore = new ArrayList<>();
            for (String item : lore) {
                newLore.add(cc.translate(item.replace("{duration}", main.moneyDuration)));
            }

            BoosterMeta.setLore(newLore);
            Booster.setItemMeta(BoosterMeta);

            player.getInventory().addItem(Booster);
            player.sendMessage(cc.translate(config.getString("messages.on-receive").replace("{booster}", bm)));

        } else {

            bm = "x4 XP Booster";
            List<String> lore = new ArrayList<>(config.getStringList("xp-gui.x4-lore"));
            ItemStack Booster = new ItemStack(Material.DIAMOND);
            ItemMeta BoosterMeta = Booster.getItemMeta();
            BoosterMeta.setDisplayName(cc.translate(config.getString("xp-gui.x4-name").replace("{duration}", main.moneyDuration)));

            List<String> newLore = new ArrayList<>();
            for (String item : lore) {
                newLore.add(cc.translate(item.replace("{duration}", main.moneyDuration)));
            }

            BoosterMeta.setLore(newLore);
            Booster.setItemMeta(BoosterMeta);

            player.getInventory().addItem(Booster);
            player.sendMessage(cc.translate(config.getString("messages.on-receive").replace("{booster}", bm)));

        }

        saveConfig();
        reloadConfig();

    }

    public void removeBoosterViaPlayer (Player player, String booster, Player remover) {

        String uuid = player.getUniqueId().toString();

        if (dataConfig.getConfigurationSection("player-data.pending-boosters").getKeys(false).contains(uuid)) {

            List<String> boosterList = new ArrayList<>(dataConfig.getStringList("player-data.pending-boosters." + uuid + ".boosters"));
            boosterList.remove(booster);
            dataConfig.set("player-data.pending-boosters." + uuid + ".boosters", boosterList);
            remover.sendMessage(cc.translate(config.getString("messages.on-remove")));

        } else {
            remover.sendMessage(cc.translate(config.getString("messages.no-boosters")));
        }

        saveConfig();
        reloadConfig();

    }

    public void removeBoosterViaConsole (Player player, String booster) {

        String uuid = player.getUniqueId().toString();

        if (dataConfig.getConfigurationSection("player-data.pending-boosters").getKeys(false).contains(uuid)) {

            List<String> boosterList = new ArrayList<>(dataConfig.getStringList("player-data.pending-boosters." + uuid + ".boosters"));
            boosterList.remove(booster);
            dataConfig.set("player-data.pending-boosters." + uuid + ".boosters", boosterList);
            main.getLogger().info("Booster removed successfully!");

        } else {
            main.getLogger().severe("That player has no boosters to remove!");
        }

        saveConfig();
        reloadConfig();

    }

    public void reloadConfig() {
        try {
            Main.getMainConfig().getConfiguration().load(Main.getMainConfig().getFile());
            Main.getDataConfig().getConfiguration().load(Main.getDataConfig().getFile());
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }

    }

    public void saveConfig() {
        try {
            config.save(Main.mainConfig.getFile());
            dataConfig.save(Main.dataConfig.getFile());
        } catch (IOException ignored) {}
    }

}
