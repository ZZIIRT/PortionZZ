package org.zzii.potionzz;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public class PotionTabCompleter implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            completions.add("give");
        } else if (args.length == 3) {
            completions.addAll(PotionZZ.instance.getConfig().getConfigurationSection("potions").getKeys(false));
        }

        return completions;
    }
}
