package com.narahentai.app;

import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import androidx.media3.common.MediaItem;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.ui.PlayerView;

public class PlayerActivity extends AppCompatActivity {

    private ExoPlayer player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        PlayerView playerView = findViewById(R.id.playerView);
        TextView txtTitle = findViewById(R.id.txtTitle);

        String slug = getIntent().getStringExtra("slug");
        String title = getIntent().getStringExtra("title");
        if (title != null) txtTitle.setText(title);

        player = new ExoPlayer.Builder(this).build();
        playerView.setPlayer(player);

        if (slug == null || slug.isEmpty()) {
            txtTitle.setText("Video tidak ditemukan");
            return;
        }

        String detailUrl = "https://narahentai.pages.dev/api/post?slug=" + Uri.encode(slug);

        ExecutorService pool = Executors.newSingleThreadExecutor();
        pool.submit(() -> {
            try {
                JSONObject obj = ApiClient.fetchPostDetail(detailUrl);

                final String videoUrl = obj.optString("video_url", "");
                final String fixedTitle = obj.optString("title", title != null ? title : "Video");

                runOnUiThread(() -> {
                    txtTitle.setText(fixedTitle);

                    if (videoUrl.isEmpty()) {
                        txtTitle.setText("Video URL belum ada di API");
                        return;
                    }

                    MediaItem mediaItem = new MediaItem.Builder()
                            .setUri(Uri.parse(videoUrl))
                            .build();

                    player.setMediaItem(mediaItem);
                    player.prepare();
                    player.play();
                });
            } catch (Exception e) {
                runOnUiThread(() -> txtTitle.setText("Gagal load video"));
            }
        });
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
