package com.narahentai.app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.io.PrintWriter;
import java.io.StringWriter;

public class CrashHandler implements Thread.UncaughtExceptionHandler {

    private static final String PREF = "crash_pref";
    private static final String KEY = "last_crash";
    private final Context appCtx;
    private final Thread.UncaughtExceptionHandler defaultHandler;

    private CrashHandler(Context ctx) {
        this.appCtx = ctx.getApplicationContext();
        this.defaultHandler = Thread.getDefaultUncaughtExceptionHandler();
    }

    public static void install(Context ctx) {
        Thread.setDefaultUncaughtExceptionHandler(new CrashHandler(ctx));
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw));
        String stack = sw.toString();

        SharedPreferences sp = appCtx.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        sp.edit().putString(KEY, stack).apply();

        Intent i = new Intent(appCtx, CrashActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        appCtx.startActivity(i);

        // biar activity sempet kebuka
        try { Thread.sleep(350); } catch (InterruptedException ignored) {}

        // tetep panggil default handler biar proses mati bersih
        if (defaultHandler != null) {
            defaultHandler.uncaughtException(t, e);
        }
    }

    public static String getLastCrash(Context ctx) {
        SharedPreferences sp = ctx.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        return sp.getString(KEY, "");
    }

    public static void clear(Context ctx) {
        SharedPreferences sp = ctx.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        sp.edit().remove(KEY).apply();
    }
}
