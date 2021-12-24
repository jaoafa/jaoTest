package com.jaoafa.jaotest;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Locale;
import java.util.Objects;

public final class Main extends JavaPlugin {
    static Main main;
    @Override
    public void onEnable() {
        main = this;

        getServer().getPluginManager().registerEvents(new Event_JoinVerifiedCheck(), this);
        getServer().getPluginManager().registerEvents(new Event_CommandNotify(), this);
        getServer().getPluginManager().registerEvents(new Event_CVE_2021_44228(), this);

        Objects.requireNonNull(getCommand("brb")).setExecutor(new Cmd_Brb());
        Objects.requireNonNull(getCommand("cmdb")).setExecutor(new Cmd_Cmdb());
        Objects.requireNonNull(getCommand("debstick")).setExecutor(new Cmd_DebStick());
        Objects.requireNonNull(getCommand("elytra")).setExecutor(new Cmd_Elytra());
        Objects.requireNonNull(getCommand("g")).setExecutor(new Cmd_G());
    }

    public static void SendMessage(CommandSender sender, Command cmd, String s) {
        sender.sendMessage(Component.join(JoinConfiguration.noSeparators(),
            Component.text("[" + cmd.getName().toLowerCase(Locale.ROOT) + "]"),
            Component.space(),
            Component.text("指定されたプレイヤー「" + s + "」は見つかりませんでした。", NamedTextColor.GREEN)
        ));
    }

    public static Main getMain() {
        return main;
    }
}
