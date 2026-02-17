package com.narahentai.app;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private WebView webView;

    private final String HOME = "https://narahentai.pages.dev/";
    private final String UPDATES = "https://narahentai.pages.dev/?sort=new";
    private final String HISTORY = "https://narahentai.pages.dev/?history=1";
    private final String PROFILE = "https://narahentai.pages.dev/?profile=1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // ✅ Fullscreen immersive (biar terasa app banget)
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
        );

        webView = findViewById(R.id.webView);

        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);

        // ✅ Disable zoom (biar gak terasa web)
        settings.setBuiltInZoomControls(false);
        settings.setDisplayZoomControls(false);
        settings.setSupportZoom(false);

        // Biar semua link tetap di dalam app
        webView.setWebViewClient(new WebViewClient());

        // Load halaman awal
        webView.loadUrl(HOME);

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
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}
