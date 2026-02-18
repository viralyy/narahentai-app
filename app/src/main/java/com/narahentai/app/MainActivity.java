package com.narahentai.app;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private MaterialToolbar topAppBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // TOP BAR
        topAppBar = findViewById(R.id.topAppBar);

        // Set title
        topAppBar.setTitle("Narahentai");
        topAppBar.setTitleTextAppearance(
                this,
                com.google.android.material.R.style.TextAppearance_Material3_TitleLarge
        );

        // Hapus semua menu dulu (biar gak dobel)
        topAppBar.getMenu().clear();

        // Inflate search menu
        topAppBar.inflateMenu(R.menu.top_appbar_menu);

        topAppBar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.action_search) {
                startActivity(new Intent(this, SearchActivity.class));
                return true;
            }
            return false;
        });

        // BOTTOM NAV
        BottomNavigationView bottomNav = findViewById(R.id.bottomNav);

        if (savedInstanceState == null) {
            switchFragment(new HomeFragment());
            bottomNav.setSelectedItemId(R.id.nav_home);
        }

        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_home) {
                topAppBar.setTitle("Narahentai");
                switchFragment(new HomeFragment());
                return true;

            } else if (id == R.id.nav_update) {
                topAppBar.setTitle("Update");
                switchFragment(new UpdateFragment());
                return true;

            } else if (id == R.id.nav_profile) {
                topAppBar.setTitle("Profil");
                switchFragment(new ProfileFragment());
                return true;
            }

            return false;
        });
    }

    private void switchFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .commit();
    }
}
