package com.narahentai.app;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TextView tv = new TextView(this);
        tv.setText("Narahentai ✅ (minimal mode)");
        tv.setTextSize(18);
        tv.setPadding(40, 80, 40, 40);
        setContentView(tv);
    }
}
