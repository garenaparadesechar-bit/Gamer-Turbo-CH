package com.gamerturbo.ch;

import android.app.*;
import android.os.*;
import android.content.*;
import android.content.pm.*;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.view.*;
import android.widget.*;
import java.util.Locale;

public class MainActivity extends Activity {
    int purple = Color.rgb(207, 40, 255);
    int white = Color.WHITE;
    LinearLayout root, content;
    TextView batteryView, ramView, tempView, statusView;

    GradientDrawable shape(int color, int stroke) {
        GradientDrawable d = new GradientDrawable();
        d.setColor(color);
        d.setCornerRadius(24);
        d.setStroke(2, stroke);
        return d;
    }

    TextView text(String s, int size, int color, boolean bold) {
        TextView t = new TextView(this);
        t.setText(s);
        t.setTextSize(size);
        t.setTextColor(color);
        t.setGravity(Gravity.CENTER);
        if (bold) t.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        t.setPadding(12, 12, 12, 12);
        return t;
    }

    void addCard(String title, String detail) {
        LinearLayout card = new LinearLayout(this);
        card.setOrientation(LinearLayout.VERTICAL);
        card.setPadding(12, 10, 12, 10);
        card.setBackground(shape(Color.rgb(24, 15, 37), purple));
        TextView a = text(title, 17, purple, true);
        TextView b = text(detail, 13, white, false);
        card.addView(a);
        card.addView(b);
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(
            -1, -2);
        p.setMargins(0, 7, 0, 7);
        content.addView(card, p);
    }

    Button button(String label, Runnable action) {
        Button b = new Button(this);
        b.setText(label);
        b.setTextColor(white);
        b.setAllCaps(false);
        b.setTextSize(16);
        b.setBackground(shape(Color.rgb(61, 15, 87), purple));
        b.setOnClickListener(v -> {
            b.animate().scaleX(0.96f).scaleY(0.96f).setDuration(80)
             .withEndAction(() -> b.animate().scaleX(1f).scaleY(1f)
             .setDuration(100).start()).start();
            action.run();
        });
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(-1, -2);
        p.setMargins(0, 6, 0, 6);
        content.addView(b, p);
        return b;
    }

    @Override public void onCreate(Bundle state) {
        super.onCreate(state);
        getWindow().setStatusBarColor(Color.rgb(9, 5, 17));
        getWindow().setNavigationBarColor(Color.rgb(9, 5, 17));

        ScrollView scroll = new ScrollView(this);
        scroll.setBackgroundColor(Color.rgb(9, 5, 17));
        root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(18, 18, 18, 22);
        scroll.addView(root);

        TextView logo = text("♛", 52, purple, true);
        root.addView(logo);
        root.addView(text("GAMER TURBO", 30, white, true));
        root.addView(text("✨ CH  •  SUPER v1.4", 16, purple, true));
        root.addView(text("POTENCIA TU EXPERIENCIA GAMER", 12, white, true));

        content = new LinearLayout(this);
        content.setOrientation(LinearLayout.VERTICAL);
        content.setPadding(0, 12, 0, 0);
        root.addView(content);

        batteryView = text("BATERÍA  •  --%", 16, white, true);
        ramView = text("RAM DISPONIBLE  •  --", 16, white, true);
        tempView = text("TEMPERATURA  •  --", 16, white, true);
        addCard("⚡ ESTADO DEL DISPOSITIVO", "Indicadores del sistema");
        content.addView(batteryView);
        content.addView(ramView);
        content.addView(tempView);

        statusView = text("SISTEMA LISTO", 14, purple, true);
        content.addView(statusView);

        button("⚡ BOOST PÚRPURA", () -> {
            updateStats();
            statusView.setText("✓ ESTADO ACTUALIZADO");
            Toast.makeText(this,
                "Se actualizaron los indicadores del dispositivo",
                Toast.LENGTH_SHORT).show();
        });

        addCard("🎮 GAME CENTER", "Abre tus juegos instalados");
        button("FREE FIRE", () -> launchGame(
            new String[]{"com.dts.freefireth", "com.dts.freefiremax"},
            "Free Fire"));
        button("FREE FIRE MAX", () -> launchGame(
            new String[]{"com.dts.freefiremax", "com.dts.freefireth"},
            "Free Fire MAX"));

        addCard("🧹 LIMPIEZA DE CACHÉ", "Limpia solamente la caché propia de esta app");
        button("LIMPIAR CACHÉ PROPIA", () -> {
            long before = folderSize(getCacheDir());
            clearFolder(getCacheDir());
            long after = folderSize(getCacheDir());
            statusView.setText("Caché propia revisada");
            Toast.makeText(this,
                "Caché propia: " + formatSize(Math.max(0, before-after)),
                Toast.LENGTH_LONG).show();
        });

        addCard("❄️ CONTROL TÉRMICO", 
            "Monitorización informativa. Android no permite enfriar físicamente el teléfono desde una app normal.");
        button("ACTUALIZAR TEMPERATURA", () -> {
            updateStats();
            statusView.setText("Indicadores térmicos actualizados");
        });

        addCard("🛡️ SHIZUKU", "Comprueba si Shizuku está instalado");
        button("COMPROBAR SHIZUKU", () -> {
            boolean installed = packageInstalled("moe.shizuku.privileged.api");
            statusView.setText(installed
                ? "Shizuku instalado; verifica que el servicio esté iniciado"
                : "Shizuku no detectado");
            Toast.makeText(this, statusView.getText(), Toast.LENGTH_LONG).show();
        });

        addCard("CH • TURBO ENGINE", 
            "Sin root. No modifica archivos de Free Fire ni promete FPS adicionales.");
        root.addView(text("Diseñado para jugar con responsabilidad 💜",
            12, Color.LTGRAY, false));
        setContentView(scroll);
        updateStats();
    }

    void updateStats() {
        Intent i = registerReceiver(null,
            new android.content.IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        if (i != null) {
            int level = i.getIntExtra("level", -1);
            int scale = i.getIntExtra("scale", 100);
            int t = i.getIntExtra("temperature", -1);
            batteryView.setText("BATERÍA  •  " +
                (level >= 0 ? (level * 100 / Math.max(1, scale)) + "%" : "N/D"));
            tempView.setText("BATERÍA  •  " +
                (t >= 0 ? String.format(Locale.US, "%.1f °C", t / 10f) : "N/D"));
        }
        ActivityManager am = (ActivityManager)getSystemService(ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(mi);
        ramView.setText("RAM DISPONIBLE  •  " +
            formatSize(mi.availMem) + " / " + formatSize(mi.totalMem));
    }

    String formatSize(long n) {
        if (n < 1024) return n + " B";
        if (n < 1024L*1024) return String.format(Locale.US, "%.1f KB", n/1024.0);
        if (n < 1024L*1024*1024)
            return String.format(Locale.US, "%.1f MB", n/(1024.0*1024));
        return String.format(Locale.US, "%.2f GB", n/(1024.0*1024*1024));
    }

    long folderSize(java.io.File f) {
        if (f == null || !f.exists()) return 0;
        if (f.isFile()) return f.length();
        long total = 0;
        java.io.File[] files = f.listFiles();
        if (files != null) for (java.io.File x : files) total += folderSize(x);
        return total;
    }

    void clearFolder(java.io.File f) {
        if (f == null || !f.exists()) return;
        java.io.File[] files = f.listFiles();
        if (files == null) return;
        for (java.io.File x : files) {
            if (x.isDirectory()) clearFolder(x);
            try { x.delete(); } catch (Exception ignored) {}
        }
    }

    boolean packageInstalled(String name) {
        try {
            getPackageManager().getPackageInfo(name, 0);
            return true;
        } catch (Exception e) { return false; }
    }

    void launchGame(String[] packages, String name) {
        for (String p : packages) {
            try {
                Intent i = getPackageManager().getLaunchIntentForPackage(p);
                if (i != null) {
                    startActivity(i);
                    return;
                }
            } catch (Exception ignored) {}
        }
        Toast.makeText(this, name + " no está instalado o no se encontró",
            Toast.LENGTH_LONG).show();
    }
}
