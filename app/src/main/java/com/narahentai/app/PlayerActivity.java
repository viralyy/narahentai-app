package com.narahentai.app;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import androidx.media3.common.MediaItem;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.ui.PlayerView;

public class PlayerActivity extends AppCompatActivity {

    public static final String EXTRA_URL = "url";
    public static final String EXTRA_TITLE = "title";
    public static final String EXTRA_VIEWS = "views";
    public static final String EXTRA_DURATION = "duration";

    private ExoPlayer player;
    private PlayerView playerView;

    // ✅ Ini yang bikin HomeFragment lu gak error lagi
    public static void open(Context context, String url, String title, int views, String durationText) {
        Intent i = new Intent(context, PlayerActivity.class);
        i.putExtra(EXTRA_URL, url);
        i.putExtra(EXTRA_TITLE, title);
        i.putExtra(EXTRA_VIEWS, views);
        i.putExtra(EXTRA_DURATION, durationText);
        context.startActivity(i);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Biar gak tiba-tiba landscape pas masuk (kayak keluhan lu)
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setContentView(R.layout.activity_player);

        playerView = findViewById(R.id.playerView);

        String url = getIntent().getStringExtra(EXTRA_URL);
        if (url == null) url = "";

        player = new ExoPlayer.Builder(this).build();
        playerView.setPlayer(player);

        MediaItem mediaItem = MediaItem.fromUri(Uri.parse(url));
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
}
