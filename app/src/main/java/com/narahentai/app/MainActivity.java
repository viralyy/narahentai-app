package com.narahentai.app;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
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

        topAppBar = findViewById(R.id.topAppBar);
        topAppBar.setTitle("App");

        // Icon kiri dihapus (sesuai request: cuma judul)
        topAppBar.setNavigationIcon(null);

        // Menu kanan: search icon
        topAppBar.getMenu().clear();
        topAppBar.inflateMenu(R.menu.top_appbar_menu);
        topAppBar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.action_search) {
                // kalau SearchActivity belum ada, comment dulu biar ga crash
                // startActivity(new Intent(this, SearchActivity.class));
                return true;
            }
            return false;
        });

        BottomNavigationView bottomNav = findViewById(R.id.bottomNav);

        if (savedInstanceState == null) {
            bottomNav.setSelectedItemId(R.id.nav_home);
            switchFragment(new HomeFragment());
        }

        bottomNav.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.nav_home) {
                    topAppBar.setTitle("Beranda");
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
            }
        });
    }

    private void switchFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .commit();
    }
}
