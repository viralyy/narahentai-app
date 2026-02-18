package com.narahentai.app;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TextView tv = new TextView(this);
        tv.setText("Narahentai ✅ (super minimal, no AndroidX)");
        tv.setTextSize(18);
        tv.setPadding(40, 80, 40, 40);
        setContentView(tv);
    }
}
