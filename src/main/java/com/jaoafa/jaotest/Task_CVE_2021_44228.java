package com.jaoafa.jaotest;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.title.Title;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.json.JSONObject;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

public class Task_CVE_2021_44228 extends BukkitRunnable {
    Player player;

    public Task_CVE_2021_44228(Player player) {
        this.player = player;
    }

    @Override
    public void run() {
        if (!player.isOnline()) {
            return;
        }
        InetSocketAddress isa = player.getAddress();
        if (isa == null) {
            return;
        }
        int beforeFoundCount = getFoundCount(isa.getAddress());
        Main.getMain().getLogger().info(player.getName() + " beforeFoundCount: " + beforeFoundCount);
        player.sendMessage(Component.join(
            JoinConfiguration.noSeparators(),
            Component.text("[脆弱性テスト] "),
            Component.text("${jndi:ldap://zakuro.jaoafa.com:39602}", NamedTextColor.DARK_GRAY, TextDecoration.ITALIC)
                .hoverEvent(HoverEvent.showText(
                    Component.text("これはなんですか: Javaライブラリ「log4j2」にあった脆弱性 CVE-2021-44228 に対策されているかを確認するものです。")
                ))
        ));
        int afterFoundCount = getFoundCount(isa.getAddress());
        Main.getMain().getLogger().info(player.getName() + " afterFoundCount: " + afterFoundCount);

        if (afterFoundCount == 0 || afterFoundCount - beforeFoundCount == 0) {
            player.sendMessage(Component.join(
                JoinConfiguration.noSeparators(),
                Component.text("[脆弱性テスト] "),
                Component.text("ご協力いただきありがとうございます。あなたのクライアントは脆弱性対策がなされているようです。", NamedTextColor.GREEN)
                    .hoverEvent(HoverEvent.showText(
                        Component.text("これはなんですか: Javaライブラリ「log4j2」にあった脆弱性 CVE-2021-44228 に対策されているかを確認するものです。")
                    ))
            ));
        } else {
            player.sendMessage(Component.join(
                JoinConfiguration.noSeparators(),
                Component.text("[脆弱性テスト] "),
                Component.text("ご注意ください！あなたのクライアントは脆弱性対策がなされていないようです！", NamedTextColor.RED, TextDecoration.BOLD)
                    .hoverEvent(HoverEvent.showText(
                        Component.text("これはなんですか: Javaライブラリ「log4j2」にあった脆弱性 CVE-2021-44228 に対策されているかを確認するものです。")
                    ))
            ));
            player.showTitle(Title.title(
                Component.text("脆弱性テストに失敗", NamedTextColor.RED),
                Component.join(
                    JoinConfiguration.noSeparators(),
                    Component.text("ご注意ください！あなたのクライアントは", NamedTextColor.RED),
                    Component.newline(),
                    Component.text("脆弱性対策がなされていないようです！", NamedTextColor.RED)
                ),
                Title.Times.of(Duration.ofSeconds(1), Duration.ofSeconds(5), Duration.ofSeconds(1))
            ));
        }
    }

    int getFoundCount(InetAddress ia) {
        try {
            String url = "http://127.0.0.1:39602/api/cve-2021-44228.php?ip=" + ia.getHostAddress();
            OkHttpClient client = new OkHttpClient().newBuilder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .build();
            Request request = new Request.Builder().url(url).build();

            Response response = client.newCall(request).execute();
            ResponseBody body = response.body();
            if (body == null) {
                return -1;
            }
            JSONObject object = new JSONObject(body.string());
            if (!object.has("count")) {
                return 0;
            }
            return object.getInt("count");
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        }
    }
}