package com.jaoafa.jaotest;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Locale;

public final class Main extends JavaPlugin {
    static Main main;
    @Override
    public void onEnable() {
        main = this;

        getServer().getPluginManager().registerEvents(new Event_JoinVerifiedCheck(), this);
    }

    public static void SendMessage(CommandSender sender, Command cmd, String s) {
        sender.sendMessage(Component.join(JoinConfiguration.noSeparators(),
            Component.text("[" + cmd.getName().toLowerCase(Locale.ROOT) + "]"),
            Component.space(),
            Component.text(s, NamedTextColor.GREEN)
        ));
    }

    public static Main getMain() {
        return main;
    }
}
