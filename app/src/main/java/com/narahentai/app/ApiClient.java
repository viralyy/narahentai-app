package com.narahentai.app;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ApiClient {

    public static List<VideoItem> fetchPosts(String apiUrl) throws Exception {
        String json = httpGet(apiUrl);
        JSONObject root = new JSONObject(json);
        JSONArray items = root.getJSONArray("items");

        List<VideoItem> list = new ArrayList<>();

        for (int i = 0; i < items.length(); i++) {
            JSONObject o = items.getJSONObject(i);

            String title = o.optString("title", "Untitled");
            String thumb = o.optString("thumbnail_url", "");
            int duration = o.optInt("duration_minutes", 0);
            int views = o.optInt("views", 0);

            // ambil field yang mungkin ada di API lu
            String videoUrl = o.optString("video_url", "");
            String videoId  = o.optString("video_id", "");     // saran: simpan ini di backend kalau gak mau simpan url full
            String slug     = o.optString("slug", "");

            list.add(new VideoItem(
                    title,
                    thumb,
                    videoUrl,
                    videoId,
                    slug,
                    duration,
                    views
            ));
        }

        return list;
    }

    private static String httpGet(String urlStr) throws Exception {
        HttpURLConnection conn = (HttpURLConnection) new URL(urlStr).openConnection();
        conn.setConnectTimeout(15000);
        conn.setReadTimeout(20000);
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");

        InputStream is = (conn.getResponseCode() >= 200 && conn.getResponseCode() < 300)
                ? conn.getInputStream()
                : conn.getErrorStream();

        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) sb.append(line);
        br.close();
        conn.disconnect();
        return sb.toString();
    }
}
