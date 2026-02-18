package com.narahentai.app;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class CrashActivity extends AppCompatActivity {

    public static final String EXTRA_ERROR = "extra_error";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crash);

        TextView txtError = findViewById(R.id.txtError);
        Button btnCopy = findViewById(R.id.btnCopy);
        Button btnRestart = findViewById(R.id.btnRestart);

        String err = getIntent().getStringExtra(EXTRA_ERROR);
        if (err == null) err = "Unknown crash (no stacktrace).";
        txtError.setText(err);

        String finalErr = err;

        btnCopy.setOnClickListener(v -> {
            ClipboardManager cb = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            if (cb != null) {
                cb.setPrimaryClip(ClipData.newPlainText("crash_log", finalErr));
            }
        });

        btnRestart.setOnClickListener(v -> {
            Intent i = new Intent(this, MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
            finish();
        });
    }
}
