package com.narahentai.app;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ApiClient {

    private static final OkHttpClient client = new OkHttpClient();
    private static final Gson gson = new Gson();

    public static List<VideoItem> fetchPosts(String apiUrl) throws IOException {
        Request req = new Request.Builder().url(apiUrl).build();
        try (Response res = client.newCall(req).execute()) {
            if (!res.isSuccessful() || res.body() == null) throw new IOException("HTTP " + res.code());
            String body = res.body().string();

            JsonObject root = gson.fromJson(body, JsonObject.class);
            JsonArray items = root.getAsJsonArray("items");

            List<VideoItem> out = new ArrayList<>();
            for (int i = 0; i < items.size(); i++) {
                VideoItem v = gson.fromJson(items.get(i), VideoItem.class);
                out.add(v);
            }
            return out;
        }
    }
}
