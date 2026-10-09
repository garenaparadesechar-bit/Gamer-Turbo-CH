package com.ch.gamerturbo;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.BatteryManager;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Locale;

public class MainActivity extends Activity {
    private final int bg = Color.rgb(10, 8, 18);
    private final int purple = Color.rgb(170, 65, 255);
    private final int white = Color.WHITE;
    private TextView battery, temp, ram, status;
    private LinearLayout root;

    @Override
    public void onCreate(Bundle b) {
        super.onCreate(b);
        getWindow().setStatusBarColor(bg);
        getWindow().setNavigationBarColor(bg);
        getWindow().getDecorView().setSystemUiVisibility(0);

        ScrollView scroll = new ScrollView(this);
        scroll.setFillViewport(true);
        scroll.setBackgroundColor(bg);

        root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(dp(18), dp(20), dp(18), dp(30));
        scroll.addView(root);
        setContentView(scroll);

        TextView brand = text("CH  //  GAMING SYSTEM", 13, purple, true);
        brand.setLetterSpacing(.12f);
        root.addView(brand);

        TextView hero = text("🎮", 62, white, true);
        hero.setGravity(Gravity.CENTER);
        hero.setPadding(0, dp(10), 0, 0);
        root.addView(hero);

        TextView title = text("GAMER TURBO", 30, white, true);
        title.setGravity(Gravity.CENTER);
        root.addView(title);

        TextView subtitle = text("CH  •  PERFORMANCE CENTER", 12, purple, true);
        subtitle.setGravity(Gravity.CENTER);
        subtitle.setPadding(0, 0, 0, dp(18));
        root.addView(subtitle);

        LinearLayout banner = card();
        banner.setOrientation(LinearLayout.VERTICAL);
        TextView b1 = text("DOMINA CADA PARTIDA", 21, white, true);
        banner.addView(b1);
        TextView b2 = text("Tu centro de juegos y monitor del dispositivo", 13,
                Color.LTGRAY, false);
        b2.setPadding(0, dp(7), 0, 0);
        banner.addView(b2);
        root.addView(banner);

        TextView monitorTitle = text("MONITOR DEL DISPOSITIVO", 16, white, true);
        monitorTitle.setPadding(dp(2), dp(20), 0, dp(8));
        root.addView(monitorTitle);

        LinearLayout stats = new LinearLayout(this);
        stats.setOrientation(LinearLayout.VERTICAL);
        stats.setPadding(dp(14), dp(12), dp(14), dp(12));
        stats.setBackground(shape(Color.rgb(25, 18, 38), purple, 18));
        battery = text("🔋 BATERÍA  •  --", 15, white, true);
        temp = text("🌡️ TEMPERATURA  •  --", 15, white, true);
        ram = text("🧠 RAM DISPONIBLE  •  --", 15, white, true);
        stats.addView(battery);
        stats.addView(temp);
        stats.addView(ram);
        root.addView(stats);
        space(8);

        addButton("⚡  ACTUALIZAR MONITOR", v -> refreshMonitor());
        addButton("🚀  MODO DE RENDIMIENTO", v -> {
            refreshMonitor();
            message("Monitor actualizado. El rendimiento también depende del sistema y la temperatura.");
        });

        TextView games = text("CENTRO DE JUEGOS", 17, white, true);
        games.setPadding(dp(2), dp(22), 0, dp(8));
        root.addView(games);

        addButton("🔥  ABRIR FREE FIRE", v ->
                launchGame("com.dts.freefireth"));
        addButton("💜  ABRIR FREE FIRE MAX", v ->
                launchGame("com.dts.freefiremax"));

        TextView tools = text("HERRAMIENTAS", 17, white, true);
        tools.setPadding(dp(2), dp(20), 0, dp(8));
        root.addView(tools);

        addButton("🧹  LIMPIAR CACHÉ DE ESTA APP", v -> {
            deleteCache(getCacheDir());
            message("Caché propia limpiada. No se borraron datos de tus juegos.");
            refreshMonitor();
        });

        addButton("🔐  COMPROBAR SHIZUKU", v -> {
            if (isInstalled("moe.shizuku.privileged.api")) {
                Intent i = getPackageManager()
                        .getLaunchIntentForPackage("moe.shizuku.privileged.api");
                if (i != null) startActivity(i);
                else message("Abre Shizuku y comprueba su estado.");
            } else {
                message("Shizuku no está instalado. Esta app no obtiene permisos por sí sola.");
            }
        });

        addButton("⚙️  INFORMACIÓN DE LA APLICACIÓN", v -> {
            Intent i = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            i.setData(android.net.Uri.parse("package:" + getPackageName()));
            startActivity(i);
        });

        status = text("CH TURBO  •  LISTO PARA JUGAR", 11, purple, true);
        status.setGravity(Gravity.CENTER);
        status.setPadding(0, dp(22), 0, 0);
        root.addView(status);

        refreshMonitor();
    }

    private void addButton(String label, View.OnClickListener action) {
        Button b = new Button(this);
        b.setText(label);
        b.setTextColor(white);
        b.setTextSize(13);
        b.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        b.setAllCaps(false);
        b.setGravity(Gravity.CENTER);
        b.setPadding(dp(12), dp(8), dp(12), dp(8));
        b.setBackground(shape(Color.rgb(38, 20, 58), purple, 16));
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(
                -1, dp(58));
        p.setMargins(0, dp(5), 0, dp(5));
        root.addView(b, p);
        b.setOnClickListener(v -> {
            b.setScaleX(.97f);
            b.setScaleY(.97f);
            b.animate().scaleX(1f).scaleY(1f).setDuration(140).start();
            action.onClick(v);
        });
    }

    private LinearLayout card() {
        LinearLayout box = new LinearLayout(this);
        box.setOrientation(LinearLayout.VERTICAL);
        box.setPadding(dp(16), dp(17), dp(16), dp(17));
        box.setBackground(shape(
                new int[]{Color.rgb(49, 20, 78), Color.rgb(19, 13, 31)},
                purple, 20));
        return box;
    }

    private GradientDrawable shape(int color, int stroke, int radius) {
        GradientDrawable d = new GradientDrawable();
        d.setColor(color);
        d.setCornerRadius(dp(radius));
        d.setStroke(dp(1), stroke);
        return d;
    }

    private GradientDrawable shape(int[] colors, int stroke, int radius) {
        GradientDrawable d = new GradientDrawable(
                GradientDrawable.Orientation.TL_BR, colors);
        d.setCornerRadius(dp(radius));
        d.setStroke(dp(1), stroke);
        return d;
    }

    private TextView text(String s, int size, int color, boolean bold) {
        TextView t = new TextView(this);
        t.setText(s);
        t.setTextSize(size);
        t.setTextColor(color);
        if (bold) t.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        return t;
    }

    private void refreshMonitor() {
        Intent i = registerReceiver(null,
                new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        if (i != null) {
            int level = i.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            int scale = i.getIntExtra(BatteryManager.EXTRA_SCALE, 100);
            int t = i.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, -1);
            battery.setText("🔋 BATERÍA  •  " +
                    (level >= 0 ? (level * 100 / Math.max(1, scale)) + "%" : "N/D"));
            temp.setText("🌡️ TEMPERATURA  •  " +
                    (t >= 0 ? String.format(Locale.US, "%.1f °C", t / 10f) : "N/D"));
        }
        ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(mi);
        ram.setText("🧠 RAM DISPONIBLE  •  " +
                formatSize(mi.availMem) + " / " + formatSize(mi.totalMem));
        if (status != null) status.setText("MONITOR ACTUALIZADO  •  CH TURBO");
    }

    private String formatSize(long n) {
        if (n < 1024L * 1024) return (n / 1024) + " KB";
        return String.format(Locale.US, "%.1f GB", n / (1024.0 * 1024 * 1024));
    }

    private void launchGame(String pkg) {
        Intent i = getPackageManager().getLaunchIntentForPackage(pkg);
        if (i != null) startActivity(i);
        else message("Ese juego no está instalado o no se encontró.");
    }

    private boolean isInstalled(String pkg) {
        try {
            getPackageManager().getPackageInfo(pkg, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    private void deleteCache(java.io.File f) {
        java.io.File[] files = f.listFiles();
        if (files == null) return;
        for (java.io.File file : files) {
            if (file.isDirectory()) deleteCache(file);
            try { file.delete(); } catch (Exception ignored) {}
        }
    }

    private void space(int h) {
        View v = new View(this);
        root.addView(v, new LinearLayout.LayoutParams(1, dp(h)));
    }

    private int dp(int n) {
        return (int) (n * getResources().getDisplayMetrics().density + .5f);
    }

    private void message(String s) {
        Toast.makeText(this, s, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (battery != null) refreshMonitor();
    }
}
