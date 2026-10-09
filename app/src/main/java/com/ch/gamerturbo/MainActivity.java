package com.ch.gamerturbo;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.provider.Settings;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.graphics.drawable.RippleDrawable;
import android.util.TypedValue;
import java.io.File;
import java.text.DecimalFormat;

public class MainActivity extends Activity {

    private final int BG = Color.rgb(8, 7, 15);
    private final int PANEL = Color.rgb(20, 16, 34);
    private final int PURPLE = Color.rgb(157, 64, 255);
    private final int PINK = Color.rgb(218, 65, 255);
    private final int WHITE = Color.rgb(247, 243, 255);
    private final int MUTED = Color.rgb(171, 160, 194);

    private LinearLayout root;
    private TextView batteryValue, tempValue, ramValue, storageValue;
    private TextView statusValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(Color.rgb(12, 8, 24));
        getWindow().setNavigationBarColor(BG);
        getWindow().getDecorView().setSystemUiVisibility(0);

        ScrollView scroll = new ScrollView(this);
        scroll.setFillViewport(true);
        scroll.setBackgroundColor(BG);

        root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(dp(17), dp(15), dp(17), dp(28));
        scroll.addView(root);
        setContentView(scroll);

        buildUI();
        refreshStats();
    }

    private int dp(float value) {
        return (int) (value * getResources().getDisplayMetrics().density + 0.5f);
    }

    private GradientDrawable shape(int[] colors, int radius, int stroke) {
        GradientDrawable d = new GradientDrawable(
                GradientDrawable.Orientation.TL_BR, colors);
        d.setCornerRadius(dp(radius));
        if (stroke != 0) d.setStroke(dp(1), stroke);
        return d;
    }

    private TextView text(String value, int size, int color, boolean bold) {
        TextView t = new TextView(this);
        t.setText(value);
        t.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
        t.setTextColor(color);
        t.setGravity(Gravity.CENTER_VERTICAL);
        if (bold) t.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        return t;
    }

    private void gap(int height) {
        View v = new View(this);
        root.addView(v, new LinearLayout.LayoutParams(1, dp(height)));
    }

    private void addSection(String title, String subtitle) {
        TextView t = text(title, 17, WHITE, true);
        root.addView(t);
        TextView s = text(subtitle, 11, MUTED, false);
        s.setPadding(0, dp(3), 0, 0);
        root.addView(s);
        gap(12);
    }

    private LinearLayout panel() {
        LinearLayout box = new LinearLayout(this);
        box.setOrientation(LinearLayout.VERTICAL);
        box.setPadding(dp(14), dp(13), dp(14), dp(13));
        box.setBackground(shape(new int[]{Color.rgb(29, 21, 47),
                Color.rgb(15, 12, 25)}, 18, Color.rgb(74, 43, 108)));
        return box;
    }

    private void buildUI() {
        // Encabezado CH
        LinearLayout header = new LinearLayout(this);
        header.setGravity(Gravity.CENTER_VERTICAL);

        TextView logo = text("CH", 23, WHITE, true);
        logo.setGravity(Gravity.CENTER);
        logo.setBackground(shape(new int[]{PINK, PURPLE,
                Color.rgb(88, 36, 214)}, 16, Color.rgb(222, 153, 255)));
        header.addView(logo, new LinearLayout.LayoutParams(dp(57), dp(57)));

        LinearLayout titles = new LinearLayout(this);
        titles.setOrientation(LinearLayout.VERTICAL);
        titles.setPadding(dp(12), 0, 0, 0);
        TextView name = text("GAMER TURBO", 19, WHITE, true);
        titles.addView(name);
        TextView sub = text("CH  /  CONTROL CENTER", 10, MUTED, true);
        titles.addView(sub);
        header.addView(titles, new LinearLayout.LayoutParams(
                0, -2, 1));

        TextView live = text("● READY", 10, Color.rgb(199, 255, 218), true);
        live.setGravity(Gravity.CENTER);
        live.setPadding(dp(9), dp(8), dp(9), dp(8));
        live.setBackground(shape(new int[]{Color.rgb(21, 54, 43),
                Color.rgb(17, 34, 30)}, 20, Color.rgb(42, 108, 77)));
        header.addView(live);
        root.addView(header);

        gap(19);

        // Portada gráfica construida con degradados
        LinearLayout hero = new LinearLayout(this);
        hero.setOrientation(LinearLayout.VERTICAL);
        hero.setPadding(dp(19), dp(20), dp(19), dp(19));
        hero.setBackground(shape(new int[]{
                Color.rgb(75, 28, 131),
                Color.rgb(37, 17, 67),
                Color.rgb(19, 13, 32)}, 24, Color.rgb(174, 89, 255)));

        TextView tag = text("✦  NEXT LEVEL GAMING", 10,
                Color.rgb(230, 192, 255), true);
        hero.addView(tag);
        gapIn(hero, 9);

        LinearLayout heroRow = new LinearLayout(this);
        heroRow.setGravity(Gravity.CENTER_VERTICAL);

        LinearLayout heroWords = new LinearLayout(this);
        heroWords.setOrientation(LinearLayout.VERTICAL);

        TextView headline = text("DOMINA", 30, WHITE, true);
        heroWords.addView(headline);
        TextView headline2 = text("CADA PARTIDA", 22,
                Color.rgb(220, 151, 255), true);
        heroWords.addView(headline2);

        TextView desc = text("Tu centro gamer en un solo lugar", 11,
                Color.rgb(213, 197, 234), false);
        desc.setPadding(0, dp(8), 0, 0);
        heroWords.addView(desc);
        heroRow.addView(heroWords, new LinearLayout.LayoutParams(0, -2, 1));

        TextView orb = text("⚡", 42, WHITE, true);
        orb.setGravity(Gravity.CENTER);
        orb.setBackground(shape(new int[]{Color.rgb(191, 83, 255),
                Color.rgb(103, 34, 218)}, 50, Color.rgb(231, 176, 255)));
        heroRow.addView(orb, new LinearLayout.LayoutParams(dp(78), dp(78)));
        hero.addView(heroRow);

        gapIn(hero, 17);
        TextView safe = text("◈  MODO SEGURO  •  SIN ROOT", 10,
                Color.rgb(237, 219, 255), true);
        safe.setGravity(Gravity.CENTER);
        safe.setPadding(dp(10), dp(11), dp(10), dp(11));
        safe.setBackground(shape(new int[]{Color.rgb(61, 30, 94),
                Color.rgb(43, 23, 69)}, 13, Color.rgb(126, 72, 171)));
        hero.addView(safe);
        root.addView(hero);

        gap(23);
        addSection("◈  MONITOR DEL DISPOSITIVO",
                "Datos actuales del teléfono");

        LinearLayout row1 = new LinearLayout(this);
        row1.addView(statCard("BATERÍA", "Leyendo...", "🔋", 0),
                weightParams());
        spacer(row1);
        row1.addView(statCard("TEMPERATURA", "Leyendo...", "🌡", 1),
                weightParams());
        root.addView(row1);

        gap(10);

        LinearLayout row2 = new LinearLayout(this);
        row2.addView(statCard("RAM DISPONIBLE", "Leyendo...", "◫", 2),
                weightParams());
        spacer(row2);
        row2.addView(statCard("ALMACENAMIENTO", "Leyendo...", "▣", 3),
                weightParams());
        root.addView(row2);

        gap(23);
        addSection("◈  CENTRO DE JUEGOS",
                "Abre tu juego con un toque");

        action("🎮   FREE FIRE", "Abrir Free Fire",
                () -> launchGame("com.dts.freefireth"), true);
        action("⚔   FREE FIRE MAX", "Abrir la versión MAX",
                () -> launchGame("com.dts.freefiremax"), false);

        gap(23);
        addSection("◈  HERRAMIENTAS TURBO",
                "Utilidades para preparar tu sesión");

        action("✦   ACTUALIZAR MONITOR", "Actualizar batería, temperatura y memoria",
                this::refreshStats, true);
        action("♧   LIMPIAR CACHÉ PROPIA", "Solo archivos temporales de esta aplicación",
                this::clearOwnCache, false);
        action("❄   CUIDADO TÉRMICO", "Consejos para reducir el calentamiento",
                this::thermalTips, false);
        action("◉   SHIZUKU", "Abrir Shizuku si está instalado",
                this::openShizuku, true);
        action("⚙   AJUSTES DEL TELÉFONO", "Opciones de batería y sistema",
                this::openSettings, false);

        gap(17);
        LinearLayout foot = panel();
        TextView footTitle = text("CH  •  GAMER TURBO", 12,
                Color.rgb(217, 164, 255), true);
        foot.addView(footTitle);
        TextView footText = text(
                "Las estadísticas son informativas. La app no cambia por sí sola la resolución ni la frecuencia de Free Fire. Las funciones avanzadas requieren permisos compatibles.",
                10, MUTED, false);
        footText.setPadding(0, dp(7), 0, 0);
        footText.setLineSpacing(dp(3), 1f);
        foot.addView(footText);
        root.addView(foot);
    }

    private void gapIn(LinearLayout parent, int height) {
        View v = new View(this);
        parent.addView(v, new LinearLayout.LayoutParams(1, dp(height)));
    }

    private LinearLayout.LayoutParams weightParams() {
        return new LinearLayout.LayoutParams(0, dp(105), 1);
    }

    private void spacer(LinearLayout row) {
        View v = new View(this);
        row.addView(v, new LinearLayout.LayoutParams(dp(10), 1));
    }

    private View statCard(String title, String value, String symbol, int id) {
        LinearLayout c = new LinearLayout(this);
        c.setOrientation(LinearLayout.VERTICAL);
        c.setPadding(dp(12), dp(12), dp(10), dp(10));
        c.setBackground(shape(new int[]{Color.rgb(34, 24, 53),
                Color.rgb(19, 15, 30)}, 17, Color.rgb(86, 48, 124)));

        TextView top = text(symbol + "  " + title, 9,
                Color.rgb(201, 177, 225), true);
        c.addView(top);

        TextView val = text(value, 18, WHITE, true);
        val.setPadding(0, dp(9), 0, 0);
        c.addView(val);

        TextView note = text("EN VIVO", 9,
                Color.rgb(194, 132, 255), true);
        note.setPadding(0, dp(4), 0, 0);
        c.addView(note);

        if (id == 0) batteryValue = val;
        if (id == 1) tempValue = val;
        if (id == 2) ramValue = val;
        if (id == 3) storageValue = val;
        return c;
    }

    private void action(String title, String subtitle, Runnable click,
                        boolean highlight) {
        LinearLayout card = new LinearLayout(this);
        card.setOrientation(LinearLayout.VERTICAL);
        card.setPadding(dp(15), dp(14), dp(15), dp(14));
        int[] colors = highlight
                ? new int[]{Color.rgb(74, 35, 111), Color.rgb(35, 20, 54)}
                : new int[]{Color.rgb(27, 21, 40), Color.rgb(17, 14, 26)};
        card.setBackground(shape(colors, 16,
                highlight ? Color.rgb(152, 74, 220) : Color.rgb(66, 43, 89)));
        card.setClickable(true);
        card.setFocusable(true);

        TextView t = text(title, 14, WHITE, true);
        card.addView(t);
        TextView s = text(subtitle, 10, MUTED, false);
        s.setPadding(0, dp(5), 0, 0);
        card.addView(s);

        card.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                v.setScaleX(0.985f);
                v.setScaleY(0.985f);
                v.setAlpha(0.85f);
            } else if (event.getAction() == MotionEvent.ACTION_UP
                    || event.getAction() == MotionEvent.ACTION_CANCEL) {
                v.setScaleX(1f);
                v.setScaleY(1f);
                v.setAlpha(1f);
            }
            return false;
        });
        card.setOnClickListener(v -> {
            try {
                click.run();
            } catch (Exception e) {
                toast("No se pudo completar esta acción");
            }
        });

        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, -2);
        p.bottomMargin = dp(9);
        root.addView(card, p);
    }

    private void refreshStats() {
        try {
            Intent battery = registerReceiver(null,
                    new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
            if (battery != null) {
                int level = battery.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
                int scale = battery.getIntExtra(BatteryManager.EXTRA_SCALE, 100);
                int percent = scale > 0 ? level * 100 / scale : level;
                if (batteryValue != null) {
                    batteryValue.setText(percent >= 0 ? percent + "%" : "N/D");
                }

                int temp = battery.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, -1);
                if (tempValue != null) {
                    tempValue.setText(temp >= 0
                            ? new DecimalFormat("0.0").format(temp / 10.0) + "°C"
                            : "N/D");
                }
            }

            ActivityManager am = (ActivityManager)
                    getSystemService(ACTIVITY_SERVICE);
            ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
            if (am != null) {
                am.getMemoryInfo(mi);
                if (ramValue != null) {
                    ramValue.setText(android.text.format.Formatter.formatFileSize(
                            this, mi.availMem));
                }
            }

            StatFs fs = new StatFs(Environment.getDataDirectory().getPath());
            long free = fs.getAvailableBytes();
            if (storageValue != null) {
                storageValue.setText(android.text.format.Formatter.formatFileSize(
                        this, free));
            }
            toast("Monitor actualizado");
        } catch (Exception e) {
            toast("No se pudieron leer todos los datos");
        }
    }

    private void launchGame(String packageName) {
        Intent i = getPackageManager().getLaunchIntentForPackage(packageName);
        if (i != null) {
            startActivity(i);
        } else {
            toast("Ese juego no está instalado");
        }
    }

    private void clearOwnCache() {
        long before = folderSize(getCacheDir());
        deleteContents(getCacheDir());
        File external = getExternalCacheDir();
        if (external != null) deleteContents(external);
        toast("Caché propia limpiada: "
                + android.text.format.Formatter.formatFileSize(this, before));
    }

    private long folderSize(File f) {
        if (f == null || !f.exists()) return 0;
        if (f.isFile()) return f.length();
        long sum = 0;
        File[] files = f.listFiles();
        if (files != null) {
            for (File child : files) sum += folderSize(child);
        }
        return sum;
    }

    private void deleteContents(File f) {
        if (f == null || !f.exists()) return;
        File[] files = f.listFiles();
        if (files != null) {
            for (File child : files) {
                if (child.isDirectory()) deleteContents(child);
                child.delete();
            }
        }
    }

    private void thermalTips() {
        new android.app.AlertDialog.Builder(this)
                .setTitle("❄ Cuidado térmico")
                .setMessage("• Evita jugar mientras cargas el teléfono.\n\n"
                        + "• Reduce el brillo y los gráficos si se calienta.\n\n"
                        + "• Cierra juegos y apps que no estés usando.\n\n"
                        + "• Quita la funda durante sesiones exigentes y deja descansar el equipo.\n\n"
                        + "Esta herramienta ofrece consejos; no puede enfriar físicamente el teléfono.")
                .setPositiveButton("Entendido", null)
                .show();
    }

    private void openShizuku() {
        Intent i = getPackageManager().getLaunchIntentForPackage(
                "moe.shizuku.privileged.api");
        if (i != null) {
            startActivity(i);
        } else {
            toast("Shizuku no está instalado");
        }
    }

    private void openSettings() {
        try {
            startActivity(new Intent(Settings.ACTION_SETTINGS));
        } catch (Exception e) {
            toast("No se pudieron abrir los ajustes");
        }
    }

    private void toast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
