package com.narahentai.app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import androidx.media3.common.MediaItem;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.ui.PlayerView;

public class PlayerActivity extends AppCompatActivity {

    private static final String EXTRA_URL = "url";
    private static final String EXTRA_TITLE = "title";
    private static final String EXTRA_VIEWS = "views";
    private static final String EXTRA_DURATION = "duration";

    private ExoPlayer player;
    private PlayerView playerView;

    public static void open(Context ctx, String url, String title, int views, String duration) {
        Intent i = new Intent(ctx, PlayerActivity.class);
        i.putExtra(EXTRA_URL, url);
        i.putExtra(EXTRA_TITLE, title);
        i.putExtra(EXTRA_VIEWS, views);
        i.putExtra(EXTRA_DURATION, duration);
        ctx.startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        String url = getIntent().getStringExtra(EXTRA_URL);
        String title = getIntent().getStringExtra(EXTRA_TITLE);
        int views = getIntent().getIntExtra(EXTRA_VIEWS, 0);
        String duration = getIntent().getStringExtra(EXTRA_DURATION);

        playerView = findViewById(R.id.playerView);

        TextView tvTitle = findViewById(R.id.tvTitle);
        TextView tvMeta = findViewById(R.id.tvMeta);
        ImageButton btnBack = findViewById(R.id.btnBack);

        tvTitle.setText(title);
        tvMeta.setText(formatViews(views) + " • " + duration);

        btnBack.setOnClickListener(v -> finish());

        setupPlayer(url);
    }

    private void setupPlayer(String url) {
        player = new ExoPlayer.Builder(this).build();
        playerView.setPlayer(player);
        playerView.setUseController(true);

        MediaItem mediaItem = MediaItem.fromUri(url);
        player.setMediaItem(mediaItem);
        player.prepare();
        player.play();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (player != null) {
            player.release();
            player = null;
        }
    }

    private String formatViews(int v) {
        if (v >= 1_000_000) return String.format("%.1fM views", v / 1_000_000f);
        if (v >= 1_000) return String.format("%.1fK views", v / 1_000f);
        return v + " views";
    }
}
