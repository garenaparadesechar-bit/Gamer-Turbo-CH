package com.ch.gamerturbo;

import android.app.*;
import android.os.*;
import android.content.*;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.view.*;
import android.widget.*;
import java.io.*;
import java.util.Locale;

public class MainActivity extends Activity {
    LinearLayout root;
    TextView stats, status;
    int purple = Color.rgb(213,0,249);

    GradientDrawable shape(int color) {
        GradientDrawable d = new GradientDrawable();
        d.setColor(color);
        d.setCornerRadius(24);
        d.setStroke(2, purple);
        return d;
    }

    TextView text(String s, int size) {
        TextView t = new TextView(this);
        t.setText(s);
        t.setTextColor(Color.WHITE);
        t.setTextSize(size);
        t.setPadding(16, 12, 16, 12);
        return t;
    }

    void button(String label, Runnable action) {
        TextView b = text(label, 16);
        b.setTypeface(null, Typeface.BOLD);
        b.setGravity(Gravity.CENTER);
        b.setBackground(shape(Color.rgb(35,15,48)));
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
            -1, -2);
        lp.setMargins(0, 7, 0, 7);
        root.addView(b, lp);
        b.setOnClickListener(v -> {
            b.setBackground(shape(purple));
            b.postDelayed(() -> b.setBackground(
                shape(Color.rgb(35,15,48))), 250);
            action.run();
        });
    }

    void launchGame(String pkg) {
        try {
            Intent i = getPackageManager().getLaunchIntentForPackage(pkg);
            if (i == null) {
                status.setText("El juego no está instalado o Android no permite abrirlo.");
            } else {
                startActivity(i);
            }
        } catch (Exception e) {
            status.setText("No se pudo abrir el juego.");
        }
    }

    void refresh() {
        try {
            android.os.StatFs fs = new android.os.StatFs(
                android.os.Environment.getDataDirectory().getPath());
            long free = fs.getAvailableBytes() / (1024 * 1024);
            android.app.ActivityManager am =
                (android.app.ActivityManager)getSystemService(ACTIVITY_SERVICE);
            android.app.ActivityManager.MemoryInfo mi =
                new android.app.ActivityManager.MemoryInfo();
            am.getMemoryInfo(mi);
            android.content.IntentFilter f =
                new android.content.IntentFilter(Intent.ACTION_BATTERY_CHANGED);
            Intent battery = registerReceiver(null, f);
            int temp = battery == null ? -1 :
                battery.getIntExtra("temperature", -1);
            int level = battery == null ? -1 :
                battery.getIntExtra("level", -1);
            int scale = battery == null ? 100 :
                battery.getIntExtra("scale", 100);
            String batteryText = level < 0 ? "N/D" :
                (level * 100 / Math.max(1, scale)) + "%";
            String tempText = temp < 0 ? "N/D" :
                String.format(Locale.US, "%.1f °C", temp / 10.0);
            stats.setText("🔋 Batería: " + batteryText +
                "\n🌡 Temperatura de batería: " + tempText +
                "\n🧠 RAM disponible: " + (mi.availMem / 1048576) +
                " MB\n💾 Almacenamiento libre: " + free + " MB");
        } catch (Exception e) {
            stats.setText("No se pudieron leer todos los datos del dispositivo.");
        }
    }

    @Override public void onCreate(Bundle b) {
        super.onCreate(b);
        getWindow().setStatusBarColor(Color.rgb(9,5,16));
        getWindow().setNavigationBarColor(Color.rgb(9,5,16));

        ScrollView scroll = new ScrollView(this);
        root = new LinearLayout(this);
        root.setOrientation(1);
        root.setPadding(20, 24, 20, 24);
        root.setBackgroundColor(Color.rgb(9,5,16));
        scroll.addView(root);
        setContentView(scroll);

        TextView logo = text("⚡ CH ⚡", 38);
        logo.setTextColor(purple);
        logo.setGravity(Gravity.CENTER);
        logo.setTypeface(null, Typeface.BOLD);
        root.addView(logo);

        TextView title = text("GAMER TURBO", 27);
        title.setGravity(Gravity.CENTER);
        title.setTypeface(null, Typeface.BOLD);
        root.addView(title);

        TextView sub = text("CENTRO GAMER • REDMI 12C", 13);
        sub.setGravity(Gravity.CENTER);
        root.addView(sub);

        stats = text("", 17);
        stats.setBackground(shape(Color.rgb(23,12,32)));
        root.addView(stats);

        status = text("Sistema listo. Elige una función.", 14);
        root.addView(status);

        button("⚡ ACTUALIZAR MONITOR", () -> refresh());
        button("🎮 ABRIR FREE FIRE", () ->
            launchGame("com.dts.freefireth"));
        button("🔥 ABRIR FREE FIRE MAX", () ->
            launchGame("com.dts.freefiremax"));

        button("🧹 LIMPIAR CACHÉ PROPIA", () -> {
            try {
                File dir = getCacheDir();
                File[] files = dir.listFiles();
                int count = 0;
                if (files != null) {
                    for (File file : files) {
                        if (remove(file)) count++;
                    }
                }
                status.setText("Caché propia limpiada. Elementos eliminados: " + count +
                    ". No se borraron los datos de tus juegos.");
            } catch (Exception e) {
                status.setText("No fue posible completar la limpieza.");
            }
        });

        button("🔋 MODO AHORRO", () -> {
            status.setText("Activa el ahorro de batería de Android desde Ajustes. " +
                "Esta app no puede cambiarlo silenciosamente.");
            try {
                startActivity(new Intent(
                    android.provider.Settings.ACTION_BATTERY_SAVER_SETTINGS));
            } catch (Exception e) {}
        });

        button("❄️ CONTROL DE TEMPERATURA", () -> {
            refresh();
            status.setText("Monitor actualizado. Reduce gráficos, brillo y evita jugar mientras cargas si el teléfono está caliente.");
        });

        button("🔐 CONFIGURAR SHIZUKU", () -> {
            try {
                startActivity(getPackageManager().getLaunchIntentForPackage(
                    "moe.shizuku.privileged.api"));
            } catch (Exception e) {
                status.setText("Instala Shizuku desde su fuente oficial y " +
                    "configúralo manualmente. Aún no se aplican cambios por ADB.");
            }
        });

        button("📱 AJUSTES DEL TELÉFONO", () -> {
            startActivity(new Intent(
                android.provider.Settings.ACTION_SETTINGS));
        });

        refresh();
    }

    boolean remove(File f) {
        if (f.isDirectory()) {
            File[] children = f.listFiles();
            if (children != null) for (File c : children) remove(c);
        }
        return f.delete();
    }

    @Override protected void onResume() {
        super.onResume();
        if (stats != null) refresh();
    }
}
