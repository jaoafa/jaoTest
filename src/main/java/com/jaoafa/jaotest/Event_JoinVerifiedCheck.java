package com.jaoafa.jaotest;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent.Result;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.UUID;

public class Event_JoinVerifiedCheck implements Listener {
    // 参加時にAPIを使ってVerified権限以上かどうかを調べる。違ったらdisallow
    @EventHandler
    public void OnLogin(AsyncPlayerPreLoginEvent event) {
        UUID uuid = event.getUniqueId();
        JSONObject json = getHttpJson("https://api.jaoafa.com/users/" + uuid);
        if (json == null) {
            event.disallow(Result.KICK_OTHER, ChatColor.GREEN + "[jaoTest]\n\n"
                + ChatColor.RESET + "権限の取得に失敗しました。時間をおいてからもう一回お試しください。(0)");
            return;
        }
        if (!json.has("status") || !json.getBoolean("status")) {
            event.disallow(Result.KICK_OTHER, ChatColor.GREEN + "[jaoTest]\n\n"
                + ChatColor.RESET + "権限の取得に失敗しました。時間をおいてからもう一回お試しください。(1)");
            return;
        }
        if (!json.has("data")) {
            event.disallow(Result.KICK_OTHER, ChatColor.GREEN + "[jaoTest]\n"
                + ChatColor.RESET + "権限の取得に失敗しました。時間をおいてからもう一回お試しください。(2)");
            return;
        }
        JSONObject data = json.getJSONObject("data");
        if (!data.has("permission")) {
            event.disallow(Result.KICK_OTHER, ChatColor.GREEN + "[jaoTest]\n"
                + ChatColor.RESET + "権限の取得に失敗しました。時間をおいてからもう一回お試しください。(3)");
            return;
        }
        String permission = data.getString("permission");

        Calendar startCal = Calendar.getInstance(TimeZone.getTimeZone(ZoneId.of("Asia/Tokyo")));
        startCal.set(2021, Calendar.JUNE, 19, 0, 0, 0); // 2021/07/19 00:00:00 JST
        Date start = startCal.getTime();

        Date now = new Date();

        Calendar endCal = Calendar.getInstance(TimeZone.getTimeZone(ZoneId.of("Asia/Tokyo")));
        endCal.set(2021, Calendar.JUNE, 30, 23, 59, 59); // 2021/06/30 23:59:59 JST
        Date end = endCal.getTime();

        if (now.before(start) &&
            !permission.equalsIgnoreCase("Admin") &&
            !permission.equalsIgnoreCase("Moderator")) {
            // 期間前 & AM以外
            event.disallow(Result.KICK_OTHER, ChatColor.GREEN + "[jaoTest]\n"
                + ChatColor.RESET + "イベントはまだ開始されていません。イベント開始までしばらくお待ちください。 (" + permission + ")");
            return;
        }
        if (now.after(end) &&
            !permission.equalsIgnoreCase("Admin") &&
            !permission.equalsIgnoreCase("Moderator")) {
            // 期間後 & AM以外
            event.disallow(Result.KICK_OTHER, ChatColor.GREEN + "[jaoTest]\n"
                + ChatColor.RESET + "イベントは終了いたしました。ご参加ありがとうございました。 (" + permission + ")");
            return;
        }

        if (!permission.equalsIgnoreCase("Admin") &&
            !permission.equalsIgnoreCase("Moderator") &&
            !permission.equalsIgnoreCase("Regular") &&
            !permission.equalsIgnoreCase("Verified")) {
            event.disallow(Result.KICK_OTHER, ChatColor.GREEN + "[jaoTest]\n"
                + ChatColor.RESET + "あなたにはイベントサーバに参加するための権限がありません。(" + permission + ")");
        }
    }
    private static JSONObject getHttpJson(String address) {
        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(address).get().build();
            Response response = client.newCall(request).execute();
            if (response.code() != 200) {
                System.out.println("[jaoTest] URLGetConnected(Error): " + address);
                System.out.println("[jaoTest] ResponseCode: " + response.code());
                if (response.body() != null) {
                    System.out.println("[jaoTest] Response: " + response.body().string());
                }
                return null;
            }
            if (response.body() == null) {
                return null;
            }
            JSONObject obj = new JSONObject(response.body().string());
            response.close();
            return obj;
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
