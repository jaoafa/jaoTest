package com.jaoafa.jaotest;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

import static com.jaoafa.jaotest.Main.SendMessage;

public class Cmd_Elytra implements CommandExecutor {
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String commandLabel, String[] args) {
        if (!(sender instanceof Player player)) {
            SendMessage(sender, cmd, "このコマンドはゲーム内から実行してください。");
            return true;
        }
        ItemStack elytra = new ItemStack(Material.ELYTRA);
        ItemStack fireworks = new ItemStack(Material.FIREWORK_ROCKET, 64);

        PlayerInventory inv = player.getInventory();
        ItemStack offhand = inv.getItemInOffHand();

        inv.setItemInOffHand(fireworks);
        SendMessage(player, cmd, "花火をオフハンドのアイテムと置きかえました。");

        if (offhand.getType() != Material.AIR) {
            if (player.getInventory().firstEmpty() == -1) {
                player.getLocation().getWorld().dropItem(player.getLocation(), offhand);
                SendMessage(player, cmd, "インベントリがいっぱいだったため、既にオフハンドに持っていたアイテムはあなたの足元にドロップしました。");
            } else {
                inv.addItem(offhand);
            }
        }

        ItemStack chestplate = inv.getChestplate();

        inv.setChestplate(elytra);
        SendMessage(player, cmd, "エリトラを装備しました。");

        if (chestplate != null && chestplate.getType() != Material.AIR) {
            if (player.getInventory().firstEmpty() == -1) {
                player.getLocation().getWorld().dropItem(player.getLocation(), chestplate);
                SendMessage(player, cmd, "インベントリがいっぱいだったため、既に胴体につけていたアイテムはあなたの足元にドロップしました。");
            } else {
                inv.addItem(chestplate);
            }
        }
        return true;
    }
}