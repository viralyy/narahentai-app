package com.narahentai.app;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNav = findViewById(R.id.bottomNav);

        // default: Home
        if (savedInstanceState == null) {
            switchTo(new HomeFragment());
        }

        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                switchTo(new HomeFragment());
                return true;
            } else if (id == R.id.nav_update) {
                // TODO: fragment update lu
                switchTo(new HomeFragment()); // sementara
                return true;
            } else if (id == R.id.nav_history) {
                // TODO
                switchTo(new HomeFragment()); // sementara
                return true;
            } else if (id == R.id.nav_profile) {
                // TODO
                switchTo(new HomeFragment()); // sementara
                return true;
            }
            return false;
        });
    }

    private void switchTo(Fragment f) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, f)
                .commit();
    }
}
