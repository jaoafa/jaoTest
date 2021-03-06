package com.jaoafa.jaotest;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;
import net.kyori.adventure.text.format.NamedTextColor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent.Result;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.time.ZoneId;
import java.util.*;

public class Event_JoinVerifiedCheck implements Listener {
    private static JSONObject getHttpJson(String address) {
        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(address).get().build();
            Response response = client.newCall(request).execute();
            if (response.code() != 200) {
                System.out.println("[jaoTest] URLGetConnected(Error): " + address);
                System.out.println("[jaoTest] ResponseCode: " + response.code());
                if (response.body() != null)
                    System.out.println("[jaoTest] Response: " + Objects.requireNonNull(response.body()).string());
                return null;
            }
            if (response.body() == null) return null;
            JSONObject obj = new JSONObject(Objects.requireNonNull(response.body()).string());
            response.close();
            return obj;
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    // 参加時にAPIを使ってVerified権限以上かどうかを調べる。違ったらdisallow
    @EventHandler
    public static void OnLogin(AsyncPlayerPreLoginEvent event) {
        UUID uuid = event.getUniqueId();
        JSONObject json = Event_JoinVerifiedCheck.getHttpJson("https://api.jaoafa.com/users/" + uuid);
        if (json == null) {
            event.disallow(Result.KICK_OTHER, Component.join(
                JoinConfiguration.noSeparators(),
                Component.text("[jaoTest]", NamedTextColor.GREEN),
                Component.newline(),
                Component.newline(),
                Component.text("権限グループの取得に失敗しました。"),
                Component.newline(),
                Component.text("時間をおいてからもう一回お試しください。(0)")
            ));
            return;
        }
        if (!json.has("status") || !json.getBoolean("status")) {
            event.disallow(Result.KICK_OTHER, Component.join(
                JoinConfiguration.noSeparators(),
                Component.text("[jaoTest]", NamedTextColor.GREEN),
                Component.newline(),
                Component.newline(),
                Component.text("権限グループの取得に失敗しました。"),
                Component.newline(),
                Component.text("時間をおいてからもう一回お試しください。(1)")
            ));
            return;
        }
        if (!json.has("data")) {
            event.disallow(Result.KICK_OTHER, Component.join(
                JoinConfiguration.noSeparators(),
                Component.text("[jaoTest]", NamedTextColor.GREEN),
                Component.newline(),
                Component.newline(),
                Component.text("権限グループの取得に失敗しました。"),
                Component.newline(),
                Component.text("時間をおいてからもう一回お試しください。(2)")
            ));
            return;
        }
        JSONObject data = json.getJSONObject("data");
        if (!data.has("permission")) {
            event.disallow(Result.KICK_OTHER, Component.join(
                JoinConfiguration.noSeparators(),
                Component.text("[jaoTest]", NamedTextColor.GREEN),
                Component.newline(),
                Component.newline(),
                Component.text("権限グループの取得に失敗しました。"),
                Component.newline(),
                Component.text("時間をおいてからもう一回お試しください。(3)")
            ));
            return;
        }
        String permission = data.getString("permission");

        Calendar startCal = Calendar.getInstance(TimeZone.getTimeZone(ZoneId.of("Asia/Tokyo")));
        if (permission.equalsIgnoreCase("Regular"))
            startCal.set(2022, Calendar.JANUARY, 22, 17, 30, 0); // Regular - 2022/01/22 17:30:00 JST
        else if (permission.equalsIgnoreCase("Verified"))
            startCal.set(2022, Calendar.JANUARY, 26, 0, 0, 0); // Verified - 2022/01/26 00:00:00 JST
        else
            startCal.set(2022, Calendar.FEBRUARY, 19, 0, 0, 0); // Default - 2022/02/19 00:00:00 JST
        Date start = startCal.getTime();

        Date now = new Date();

        // 期間前 & AM以外
        if (now.before(start) &&
            !permission.equalsIgnoreCase("Admin") &&
            !permission.equalsIgnoreCase("Moderator")) event.disallow(Result.KICK_OTHER, Component.join(
            JoinConfiguration.noSeparators(),
            Component.text("[jaoTest]", NamedTextColor.GREEN),
            Component.newline(),
            Component.newline(),
            Component.text("あなたの権限グループではまだ参加できません。"),
            Component.newline(),
            Component.text("参加可能になるまでしばらくお待ちください。(" + permission + ")")
        ));
    }
}
