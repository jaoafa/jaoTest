package com.jaoafa.jaotest;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.jetbrains.annotations.NotNull;

import static com.jaoafa.jaotest.Main.SendMessage;

public class Cmd_Cmdb  implements CommandExecutor {
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String commandLabel, String[] args) {
        if (args.length == 0) {
            if (!(sender instanceof Player player)) {
                SendMessage(sender, cmd, "このコマンドはゲーム内から実行してください。");
                return true;
            }

            ItemStack is = new ItemStack(Material.COMMAND_BLOCK);

            PlayerInventory inv = player.getInventory();
            ItemStack main = inv.getItemInMainHand();

            inv.setItemInMainHand(is);
            SendMessage(sender, cmd, "コマンドブロックをメインハンドのアイテムと置きかえました。");

            if (main.getType() != Material.AIR) {
                if (player.getInventory().firstEmpty() == -1) {
                    player.getLocation().getWorld().dropItem(player.getLocation(), main);
                    SendMessage(sender, cmd, "インベントリがいっぱいだったため、既に持っていたアイテムはあなたの足元にドロップしました。");
                } else {
                    inv.addItem(main);
                }
            }
            return true;
        } else if (args.length == 1) {
            Player player = Bukkit.getPlayerExact(args[0]);
            if (player == null) {
                SendMessage(sender, cmd, "指定されたプレイヤー「" + args[0] + "」は見つかりませんでした。");

                Player any_chance_player = Bukkit.getPlayer(args[0]);
                if (any_chance_player != null) {
                    SendMessage(sender, cmd, "もしかして: " + any_chance_player.getName());
                }
                return true;
            }

            ItemStack is = new ItemStack(Material.BARRIER);

            PlayerInventory inv = player.getInventory();
            ItemStack main = inv.getItemInMainHand();

            inv.setItemInMainHand(is);
            SendMessage(sender, cmd, "コマンドブロックをプレイヤー「" + player.getName() + "」のメインハンドのアイテムと置きかえました。");

            if (main.getType() != Material.AIR) {
                if (player.getInventory().firstEmpty() == -1) {
                    player.getLocation().getWorld().dropItem(player.getLocation(), main);
                    SendMessage(player, cmd, "インベントリがいっぱいだったため、既に持っていたアイテムはあなたの足元にドロップしました。");
                } else {
                    inv.addItem(main);
                }
            }
            return true;
        }
        SendMessage(sender, cmd, "Usage: /cmdb");
        return true;
    }
}
