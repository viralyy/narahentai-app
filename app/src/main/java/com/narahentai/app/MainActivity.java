package com.narahentai.app;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import androidx.media3.common.MediaItem;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.ui.PlayerView;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private WebView webView;

    private final String HOME = "https://narahentai.pages.dev/";
    private final String UPDATES = "https://narahentai.pages.dev/?sort=new";
    private final String HISTORY = "https://narahentai.pages.dev/?history=1";
    private final String PROFILE = "https://narahentai.pages.dev/?profile=1";

    private ProgressBar pageProgress;
    private ImageButton btnRefresh;

    private ExoPlayer player;
    private PlayerView playerView;
    private BottomSheetBehavior<View> sheet;

    private ImageView miniThumb;
    private TextView miniTitle;
    private TextView miniSub;
    private ImageButton btnPlayPause;
    private ImageButton btnClose;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pageProgress = findViewById(R.id.pageProgress);
        btnRefresh = findViewById(R.id.btnRefresh);

        // --- WebView setup
        webView = findViewById(R.id.webView);
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);

        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setSupportZoom(false);
        settings.setBuiltInZoomControls(false);
        settings.setDisplayZoomControls(false);
        settings.setMediaPlaybackRequiresUserGesture(false);

        // app mode
        settings.setUserAgentString(settings.getUserAgentString() + " NarahentaiApp");

        webView.setOverScrollMode(WebView.OVER_SCROLL_NEVER);

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress >= 100) {
                    pageProgress.setVisibility(View.GONE);
                } else {
                    if (pageProgress.getVisibility() != View.VISIBLE) pageProgress.setVisibility(View.VISIBLE);
                    pageProgress.setProgress(newProgress);
                }
            }
        });

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                Uri uri = request.getUrl();
                String url = uri.toString();

                // ✅ tangkap watch.html?slug=xxx -> play native
                if (url.contains("/watch.html") && url.contains("slug=")) {
                    String slug = uri.getQueryParameter("slug");
                    if (slug != null && !slug.isEmpty()) {
                        openNativePlayerBySlug(slug);
                        return true;
                    }
                }

                if (url.startsWith("http://") || url.startsWith("https://")) return false;
                return false;
            }
        });

        btnRefresh.setOnClickListener(v -> webView.reload());

        // --- Player setup
        playerView = findViewById(R.id.playerView);
        miniThumb = findViewById(R.id.miniThumb);
        miniTitle = findViewById(R.id.miniTitle);
        miniSub = findViewById(R.id.miniSub);
        btnPlayPause = findViewById(R.id.btnPlayPause);
        btnClose = findViewById(R.id.btnClose);

        player = new ExoPlayer.Builder(this).build();
        playerView.setPlayer(player);

        View sheetView = findViewById(R.id.playerSheet);
        sheet = BottomSheetBehavior.from(sheetView);
        sheet.setHideable(true);
        sheet.setPeekHeight(dp(64));
        sheet.setState(BottomSheetBehavior.STATE_HIDDEN);

        // tap mini bar -> expand
        findViewById(R.id.miniBar).setOnClickListener(v -> {
            if (sheet.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
                sheet.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });

        btnPlayPause.setOnClickListener(v -> {
            if (player == null) return;
            if (player.isPlaying()) {
                player.pause();
                btnPlayPause.setImageResource(android.R.drawable.ic_media_play);
            } else {
                player.play();
                btnPlayPause.setImageResource(android.R.drawable.ic_media_pause);
            }
        });

        btnClose.setOnClickListener(v -> stopAndHidePlayer());

        // --- Bottom nav
        BottomNavigationView bottomNav = findViewById(R.id.bottomNav);
        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                webView.loadUrl(HOME);
                return true;
            } else if (id == R.id.nav_updates) {
                webView.loadUrl(UPDATES);
                return true;
            } else if (id == R.id.nav_history) {
                webView.loadUrl(HISTORY);
                return true;
            } else if (id == R.id.nav_profile) {
                webView.loadUrl(PROFILE);
                return true;
            }
            return false;
        });

        webView.loadUrl(HOME);
    }

    private int dp(int v) {
        float d = getResources().getDisplayMetrics().density;
        return Math.round(v * d);
    }

    private void stopAndHidePlayer() {
        try { if (player != null) player.stop(); } catch (Exception ignored) {}
        sheet.setState(BottomSheetBehavior.STATE_HIDDEN);
        miniTitle.setText("Video");
        miniSub.setText("Narahentai");
        miniThumb.setImageBitmap(null);
        btnPlayPause.setImageResource(android.R.drawable.ic_media_pause);
    }

    private void openNativePlayerBySlug(String slug) {
        new Thread(() -> {
            try {
                String api = "https://narahentai.pages.dev/api/post?slug=" + Uri.encode(slug);
                HttpURLConnection conn = (HttpURLConnection) new URL(api).openConnection();
                conn.setConnectTimeout(15000);
                conn.setReadTimeout(15000);

                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) sb.append(line);
                br.close();

                JSONObject obj = new JSONObject(sb.toString());
                String title = obj.optString("title", "Video");
                String videoUrl = obj.optString("video_url", "");
                String thumbUrl = obj.optString("thumbnail_url", "");
                long views = obj.optLong("views", 0);

                runOnUiThread(() -> {
                    miniTitle.setText(title);
                    miniSub.setText(views + " views");
                    btnPlayPause.setImageResource(android.R.drawable.ic_media_pause);
                    loadThumbAsync(thumbUrl);

                    // ✅ Play + langsung gede (YouTube)
                    playUrl(videoUrl);
                });

            } catch (Exception e) {
                runOnUiThread(() -> {
                    miniTitle.setText("Gagal load video");
                    miniSub.setText("Coba lagi");
                    sheet.setState(BottomSheetBehavior.STATE_COLLAPSED);
                });
            }
        }).start();
    }

    private void playUrl(String url) {
        if (url == null || url.isEmpty()) return;

        MediaItem item = MediaItem.fromUri(Uri.parse(url));
        player.setMediaItem(item);
        player.prepare();
        player.play();

        // ✅ INI KUNCI: langsung expanded
        sheet.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    private void loadThumbAsync(String thumbUrl) {
        if (thumbUrl == null || thumbUrl.trim().isEmpty()) {
            miniThumb.setImageBitmap(null);
            return;
        }
        new Thread(() -> {
            try {
                HttpURLConnection conn = (HttpURLConnection) new URL(thumbUrl).openConnection();
                conn.setConnectTimeout(12000);
                conn.setReadTimeout(12000);
                conn.connect();
                BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());
                Bitmap bmp = BitmapFactory.decodeStream(bis);
                bis.close();
                runOnUiThread(() -> miniThumb.setImageBitmap(bmp));
            } catch (Exception e) {
                runOnUiThread(() -> miniThumb.setImageBitmap(null));
            }
        }).start();
    }

    @Override
    public void onBackPressed() {
        // YouTube feel:
        // expanded -> mini
        if (sheet != null && sheet.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            sheet.setState(BottomSheetBehavior.STATE_COLLAPSED);
            return;
        }
        // mini -> close
        if (sheet != null && sheet.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
            stopAndHidePlayer();
            return;
        }

        if (webView.canGoBack()) webView.goBack();
        else super.onBackPressed();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (player != null && player.isPlaying()) {
            player.pause();
            btnPlayPause.setImageResource(android.R.drawable.ic_media_play);
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
