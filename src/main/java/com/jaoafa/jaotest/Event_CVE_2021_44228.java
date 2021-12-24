package com.jaoafa.jaotest;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class Event_CVE_2021_44228 implements Listener {
    @EventHandler
    public void OnJoin(PlayerJoinEvent event) {
        new Task_CVE_2021_44228(event.getPlayer()).runTaskAsynchronously(Main.getMain());
    }
}
