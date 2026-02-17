package com.narahentai.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private MaterialToolbar topBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        topBar = findViewById(R.id.topBar);
        setSupportActionBar(topBar);

        // Hilangin tombol refresh (poin 4) => kita gak pake menu refresh sama sekali
        // Kita cuma pake icon search dari menu.

        BottomNavigationView bottomNav = findViewById(R.id.bottomNav);

        // default: Home
        if (savedInstanceState == null) {
            switchFragment(new HomeFragment(), "Beranda");
            bottomNav.setSelectedItemId(R.id.nav_home);
        }

        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                switchFragment(new HomeFragment(), "Beranda");
                return true;
            } else if (id == R.id.nav_update) {
                switchFragment(new UpdateFragment(), "Update");
                return true;
            } else if (id == R.id.nav_history) {
                switchFragment(new HistoryFragment(), "Riwayat");
                return true;
            } else if (id == R.id.nav_profile) {
                switchFragment(new ProfileFragment(), "Profil");
                return true;
            }
            return false;
        });
    }

    private void switchFragment(Fragment fragment, String title) {
        topBar.setTitle("Narahentai");
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }

    // Search aktif (poin 3)
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.topbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_search) {
            startActivity(new Intent(this, SearchActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
