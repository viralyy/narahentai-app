package com.narahentai.app;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.MaterialToolbar;

import androidx.media3.common.MediaItem;
import androidx.media3.common.PlaybackException;
import androidx.media3.common.Player;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.ui.PlayerView;

public class PlayerActivity extends AppCompatActivity {

    private static final String EXTRA_URL = "url";
    private static final String EXTRA_TITLE = "title";
    private static final String EXTRA_META = "meta";

    private ExoPlayer player;

    public static void open(Context ctx, String url, String title, String meta) {
        Intent i = new Intent(ctx, PlayerActivity.class);
        i.putExtra(EXTRA_URL, url);
        i.putExtra(EXTRA_TITLE, title);
        i.putExtra(EXTRA_META, meta);
        ctx.startActivity(i);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        String url = getIntent().getStringExtra(EXTRA_URL);
        String title = getIntent().getStringExtra(EXTRA_TITLE);
        String meta = getIntent().getStringExtra(EXTRA_META);

        MaterialToolbar tb = findViewById(R.id.playerToolbar);
        tb.setTitle(title != null ? title : "Player");
        tb.setNavigationOnClickListener(v -> finish());

        TextView txtTitle = findViewById(R.id.txtTitle);
        TextView txtMeta = findViewById(R.id.txtMeta);
        txtTitle.setText(title != null ? title : "");
        txtMeta.setText(meta != null ? meta : "");

        PlayerView pv = findViewById(R.id.playerView);

        player = new ExoPlayer.Builder(this).build();
        pv.setPlayer(player);

        player.addListener(new Player.Listener() {
            @Override
            public void onPlayerError(PlaybackException error) {
                txtMeta.setText("Gagal play: " + error.getMessage());
            }
        });

        if (url != null && !url.trim().isEmpty()) {
            player.setMediaItem(MediaItem.fromUri(Uri.parse(url)));
            player.prepare();
            player.play();
        } else {
            txtMeta.setText("URL kosong (cek API video_url)");
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (player != null) player.pause();
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
