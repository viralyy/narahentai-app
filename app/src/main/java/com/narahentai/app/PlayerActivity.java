package com.narahentai.app;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import androidx.media3.common.MediaItem;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.ui.PlayerView;

public class PlayerActivity extends AppCompatActivity {

    private PlayerView playerView;
    private ExoPlayer player;

    private boolean isFullscreen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        playerView = findViewById(R.id.playerView);

        TextView txtTitle = findViewById(R.id.txtTitle);
        TextView txtMeta = findViewById(R.id.txtMeta);

        Button btnShare = findViewById(R.id.btnShare);
        Button btnLike = findViewById(R.id.btnLike);
        Button btnDownload = findViewById(R.id.btnDownload);

        String title = getIntent().getStringExtra("title");
        String videoUrl = getIntent().getStringExtra("videoUrl");

        if (title == null) title = "Video";
        if (videoUrl == null) videoUrl = "";

        txtTitle.setText(title);
        txtMeta.setText("Streaming • MP4");

        // Player
        player = new ExoPlayer.Builder(this).build();
        playerView.setPlayer(player);

        // load media
        if (!videoUrl.isEmpty()) {
            MediaItem item = new MediaItem.Builder()
                    .setUri(Uri.parse(videoUrl))
                    .build();
            player.setMediaItem(item);
            player.prepare();
            player.play();
        }

        // tombol share
        String finalTitle = title;
        String finalVideoUrl = videoUrl;
        btnShare.setOnClickListener(v -> {
            Intent share = new Intent(Intent.ACTION_SEND);
            share.setType("text/plain");
            share.putExtra(Intent.EXTRA_SUBJECT, finalTitle);
            share.putExtra(Intent.EXTRA_TEXT, finalTitle + "\n" + finalVideoUrl);
            startActivity(Intent.createChooser(share, "Bagikan"));
        });

        // placeholder (nanti lu isi)
        btnLike.setOnClickListener(v -> {});
        btnDownload.setOnClickListener(v -> {});

        // fullscreen button dari controller
        View fsBtn = playerView.findViewById(androidx.media3.ui.R.id.exo_fullscreen);
        if (fsBtn != null) {
            fsBtn.setOnClickListener(v -> toggleFullscreen());
        }
    }

    private void toggleFullscreen() {
        if (!isFullscreen) {
            isFullscreen = true;
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            );
        } else {
            isFullscreen = false;
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
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
