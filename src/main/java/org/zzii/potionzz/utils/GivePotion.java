package org.zzii.potionzz.utils;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.zzii.potionzz.PotionZZ;

import java.util.ArrayList;
import java.util.List;

public class GivePotion implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!sender.hasPermission("zpotion.give")) {
            sender.sendMessage(colorize(PotionZZ.instance.getConfig().getString("messages.no_permission")));
            return true;
        }

        if (args.length != 3 || !args[0].equalsIgnoreCase("give")) {
            sender.sendMessage(colorize(PotionZZ.instance.getConfig().getString("messages.usage")));
            return false;
        }

        Player target = Bukkit.getPlayer(args[1]);
        if (target == null) {
            sender.sendMessage(colorize(PotionZZ.instance.getConfig().getString("messages.player_not_found")));
            return false;
        }

        String potionKey = args[2];
        if (!PotionZZ.instance.getConfig().getConfigurationSection("potions").contains(potionKey)) {
            sender.sendMessage(colorize(PotionZZ.instance.getConfig().getString("messages.potion_not_found")));
            return false;
        }

        String name = colorize(PotionZZ.instance.getConfig().getString("potions." + potionKey + ".name"));
        List<String> lore = PotionZZ.instance.getConfig().getStringList("potions." + potionKey + ".lore");
        String color = PotionZZ.instance.getConfig().getString("potions." + potionKey + ".color");
        boolean hideAttributes = PotionZZ.instance.getConfig().getBoolean("potions." + potionKey + ".hideAttributes");
        List<String> effects = PotionZZ.instance.getConfig().getStringList("potions." + potionKey + ".effects");
        boolean isSplashPotion = PotionZZ.instance.getConfig().getBoolean("potions." + potionKey + ".splash_potion");

        List<String> colorizedLore = new ArrayList<>();
        for (String line : lore) {
            colorizedLore.add(colorize(line));
        }

        ItemStack potion = new ItemStack(isSplashPotion ? Material.SPLASH_POTION : Material.POTION, 1);
        PotionMeta meta = (PotionMeta) potion.getItemMeta();

        if (meta != null) {
            meta.setDisplayName(name);
            if (!colorizedLore.isEmpty()) {
                meta.setLore(colorizedLore);
            }
            if (color != null) {
                meta.setColor(rgbToColor(color));
            }
            if (hideAttributes) {
                meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_POTION_EFFECTS);
            }

            for (String effectString : effects) {
                String[] effectData = effectString.split(":");
                if (effectData.length == 3) {
                    PotionEffectType type = PotionEffectType.getByName(effectData[0]);
                    if (type != null) {
                        int amplifier = Integer.parseInt(effectData[1]);
                        int duration = Integer.parseInt(effectData[2]);
                        meta.addCustomEffect(new PotionEffect(type, duration, amplifier), true);
                    }
                }
            }

            potion.setItemMeta(meta);
        }

        target.getInventory().addItem(potion);
        target.sendMessage(colorize(PotionZZ.instance.getConfig().getString("messages.potion_received").replace("%potion%", name)));

        return true;
    }

    private String colorize(String text) {
        return text == null ? null : text.replace('&', '§');
    }

    private Color rgbToColor(String rgb) {
        String[] rgbValues = rgb.split(",");
        int r = Integer.parseInt(rgbValues[0]);
        int g = Integer.parseInt(rgbValues[1]);
        int b = Integer.parseInt(rgbValues[2]);
        return Color.fromRGB(r, g, b);
    }
}
