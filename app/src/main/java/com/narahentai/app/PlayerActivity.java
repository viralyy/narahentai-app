package com.narahentai.app;

import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import androidx.media3.common.MediaItem;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.ui.PlayerView;

public class PlayerActivity extends AppCompatActivity {

    public static final String EXTRA_URL = "url";
    public static final String EXTRA_TITLE = "title";

    private ExoPlayer player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        String url = getIntent().getStringExtra(EXTRA_URL);
        String title = getIntent().getStringExtra(EXTRA_TITLE);

        TextView tvTitle = findViewById(R.id.playerTitle);
        tvTitle.setText(title != null ? title : "");

        PlayerView playerView = findViewById(R.id.playerView);

        player = new ExoPlayer.Builder(this).build();
        playerView.setPlayer(player);

        if (url != null && !url.isEmpty()) {
            MediaItem mediaItem = MediaItem.fromUri(Uri.parse(url));
            player.setMediaItem(mediaItem);
            player.prepare();
            player.play();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (player != null) {
            player.pause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (player != null) {
            player.release();
            player = null;
        }
    }
}
