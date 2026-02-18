package com.narahentai.app;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class CrashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crash);

        TextView txt = findViewById(R.id.txtCrash);
        Button btnCopy = findViewById(R.id.btnCopy);
        Button btnClear = findViewById(R.id.btnClear);

        String crash = CrashHandler.getLastCrash(this);
        if (crash == null || crash.trim().isEmpty()) crash = "No crash log saved.";

        txt.setText(crash);

        String finalCrash = crash;
        btnCopy.setOnClickListener(v -> {
            ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            cm.setPrimaryClip(ClipData.newPlainText("crash", finalCrash));
            Toast.makeText(this, "Crash log copied", Toast.LENGTH_SHORT).show();
        });

        btnClear.setOnClickListener(v -> {
            CrashHandler.clear(this);
            Toast.makeText(this, "Cleared. Open app again.", Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}
