package com.narahentai.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private MaterialToolbar topAppBar;
    private BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        topAppBar = findViewById(R.id.topAppBar);
        bottomNav = findViewById(R.id.bottomNav);

        // pastiin menu toolbar ada (search doang)
        topAppBar.getMenu().clear();
        topAppBar.inflateMenu(R.menu.top_appbar_menu);

        topAppBar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.action_search) {
                startActivity(new Intent(this, SearchActivity.class));
                return true;
            }
            return false;
        });

        // default page
        if (savedInstanceState == null) {
            bottomNav.setSelectedItemId(R.id.nav_home);
            openTab(R.id.nav_home);
        }

        bottomNav.setOnItemSelectedListener(item -> {
            openTab(item.getItemId());
            return true;
        });
    }

    private void openTab(int menuId) {
        Fragment fragment;
        String title;

        if (menuId == R.id.nav_home) {
            title = "Narahentai";
            fragment = new HomeFragment();
        } else if (menuId == R.id.nav_update) {
            title = "Update";
            fragment = new UpdateFragment();
        } else if (menuId == R.id.nav_history) {
            title = "Riwayat";
            fragment = new HistoryFragment();
        } else { // nav_profile
            title = "Profil";
            fragment = new ProfileFragment();
        }

        topAppBar.setTitle(title);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .commit();
    }
}
