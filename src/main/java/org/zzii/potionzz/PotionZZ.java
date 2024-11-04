package org.zzii.potionzz;

import org.bukkit.plugin.java.JavaPlugin;
import org.zzii.potionzz.utils.GivePotion;

public class PotionZZ extends JavaPlugin {
    public static PotionZZ instance;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        this.getCommand("zpotion").setExecutor(new GivePotion());
        this.getCommand("zpotion").setTabCompleter(new PotionTabCompleter());
    }
}
