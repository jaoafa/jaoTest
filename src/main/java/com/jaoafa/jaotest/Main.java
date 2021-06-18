package com.jaoafa.jaotest;

import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {
    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new Event_JoinVerifiedCheck(), this);
        getServer().getPluginManager().registerEvents(new Event_CommandNotify(), this);
    }
}
