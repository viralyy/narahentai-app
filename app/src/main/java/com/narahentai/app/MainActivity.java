package com.narahentai.app;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private static final String API_LIST = "https://narahentai.pages.dev/api/posts?page=1&q=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MaterialToolbar topBar = findViewById(R.id.topBar);
        topBar.setTitle("Beranda");

        RecyclerView rv = findViewById(R.id.recycler);
        rv.setLayoutManager(new LinearLayoutManager(this));

        List<VideoItem> list = new ArrayList<>();
        VideoAdapter adapter = new VideoAdapter(list);
        rv.setAdapter(adapter);

        adapter.setOnVideoClick(item -> {
            Intent i = new Intent(MainActivity.this, PlayerActivity.class);
            i.putExtra("slug", item.slug);
            i.putExtra("title", item.title);
            startActivity(i);
        });

        ExecutorService pool = Executors.newSingleThreadExecutor();
        pool.submit(() -> {
            try {
                List<VideoItem> items = ApiClient.fetchPosts(API_LIST);
                runOnUiThread(() -> {
                    list.clear();
                    list.addAll(items);
                    adapter.notifyDataSetChanged();
                });
            } catch (Exception ignored) {}
        });
    }
}
