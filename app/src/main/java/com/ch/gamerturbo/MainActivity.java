package com.ch.gamerturbo;

import android.app.*;
import android.os.*;
import android.content.*;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.view.*;
import android.view.animation.*;
import android.widget.*;
import java.io.*;
import java.util.Locale;

public class MainActivity extends Activity {
    LinearLayout root;
    TextView stats, status;
    int purple = Color.rgb(213,0,249);
    int bg = Color.rgb(8,5,16);
    int panel = Color.rgb(23,13,36);

    GradientDrawable gradient(int a, int b, int radius) {
        GradientDrawable d = new GradientDrawable(
            GradientDrawable.Orientation.TL_BR,
            new int[]{a,b});
        d.setCornerRadius(radius);
        return d;
    }

    TextView label(String s, int size, int color) {
        TextView t = new TextView(this);
        t.setText(s);
        t.setTextSize(size);
        t.setTextColor(color);
        t.setPadding(12,8,12,8);
        return t;
    }

    void heading(String s) {
        TextView t = label(s,17,Color.WHITE);
        t.setTypeface(null,Typeface.BOLD);
        root.addView(t);
    }

    void card(View v) {
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(-1,-2);
        p.setMargins(0,7,0,12);
        v.setBackground(gradient(panel,Color.rgb(35,15,52),22));
        v.setPadding(14,14,14,14);
        root.addView(v,p);
    }

    void button(String name, String subtitle, Runnable action) {
        LinearLayout box = new LinearLayout(this);
        box.setOrientation(1);
        box.setPadding(14,12,14,12);
        box.setBackground(gradient(Color.rgb(43,17,62),
            Color.rgb(19,10,30),18));
        GradientDrawable border = gradient(Color.rgb(43,17,62),
            Color.rgb(19,10,30),18);
        border.setStroke(1,purple);
        box.setBackground(border);

        TextView title = label(name,16,Color.WHITE);
        title.setTypeface(null,Typeface.BOLD);
        TextView desc = label(subtitle,12,Color.LTGRAY);
        box.addView(title);
        box.addView(desc);

        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(-1,-2);
        p.setMargins(0,5,0,7);
        root.addView(box,p);

        box.setOnClickListener(v -> {
            v.animate().scaleX(.97f).scaleY(.97f).setDuration(80)
                .withEndAction(() -> v.animate().scaleX(1f).scaleY(1f)
                .setDuration(100).start()).start();
            v.setBackground(gradient(purple,Color.rgb(105,0,180),18));
            v.postDelayed(() -> {
                GradientDrawable d = gradient(Color.rgb(43,17,62),
                    Color.rgb(19,10,30),18);
                d.setStroke(1,purple);
                v.setBackground(d);
            },220);
            action.run();
        });
    }

    void launchGame(String pkg) {
        Intent i = getPackageManager().getLaunchIntentForPackage(pkg);
        if (i == null) {
            status.setText("No se encontró el juego instalado.");
        } else {
            startActivity(i);
        }
    }

    void refresh() {
        try {
            android.app.ActivityManager am =
                (android.app.ActivityManager)getSystemService(ACTIVITY_SERVICE);
            android.app.ActivityManager.MemoryInfo mi =
                new android.app.ActivityManager.MemoryInfo();
            am.getMemoryInfo(mi);

            Intent battery = registerReceiver(null,
                new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
            int level = battery == null ? -1 :
                battery.getIntExtra("level",-1);
            int scale = battery == null ? 100 :
                battery.getIntExtra("scale",100);
            int temp = battery == null ? -1 :
                battery.getIntExtra("temperature",-1);

            android.os.StatFs fs = new android.os.StatFs(
                android.os.Environment.getDataDirectory().getPath());
            long free = fs.getAvailableBytes()/1048576;
            String bat = level < 0 ? "N/D" :
                (level*100/Math.max(scale,1))+"%";
            String heat = temp < 0 ? "N/D" :
                String.format(Locale.US,"%.1f °C",temp/10.0);

            stats.setText("🔋  BATERÍA       "+bat+
                "\n\n🌡  TEMP. BATERÍA    "+heat+
                "\n\n🧠  RAM DISPONIBLE   "+(mi.availMem/1048576)+" MB"+
                "\n\n💾  ESPACIO LIBRE    "+free+" MB");
        } catch(Exception e) {
            stats.setText("No se pudieron leer las estadísticas.");
        }
    }

    boolean remove(File f) {
        if(f.isDirectory()) {
            File[] children=f.listFiles();
            if(children!=null) for(File c:children) remove(c);
        }
        return f.delete();
    }

    @Override public void onCreate(Bundle b) {
        super.onCreate(b);
        getWindow().setStatusBarColor(bg);
        getWindow().setNavigationBarColor(bg);

        ScrollView scroll = new ScrollView(this);
        scroll.setFillViewport(true);
        root = new LinearLayout(this);
        root.setOrientation(1);
        root.setPadding(18,16,18,28);
        root.setBackgroundColor(bg);
        scroll.addView(root);
        setContentView(scroll);

        LinearLayout hero = new LinearLayout(this);
        hero.setGravity(Gravity.CENTER);
        hero.setOrientation(1);
        hero.setPadding(12,28,12,26);
        hero.setBackground(gradient(Color.rgb(69,0,105),
            Color.rgb(12,6,25),28));

        TextView logo = label("◈  CH  ◈",30,Color.WHITE);
        logo.setGravity(Gravity.CENTER);
        logo.setTypeface(null,Typeface.BOLD);
        hero.addView(logo);

        TextView title = label("GAMER TURBO",28,Color.WHITE);
        title.setGravity(Gravity.CENTER);
        title.setTypeface(null,Typeface.BOLD);
        hero.addView(title);

        TextView sub = label("POWER UP YOUR GAME  •  V1.1",11,
            Color.rgb(235,150,255));
        sub.setGravity(Gravity.CENTER);
        hero.addView(sub);
        card(hero);

        heading("◈  PANEL DE RENDIMIENTO");
        stats = label("",15,Color.WHITE);
        card(stats);

        button("⚡  ACTUALIZAR MONITOR",
            "Consultar batería, memoria y temperatura",() -> {
                refresh();
                status.setText("Monitor actualizado.");
            });

        heading("🎮  CENTRO GAMER");
        button("🔥  FREE FIRE","Abrir el juego instalado",() ->
            launchGame("com.dts.freefireth"));
        button("💜  FREE FIRE MAX","Abrir la versión MAX instalada",() ->
            launchGame("com.dts.freefiremax"));

        heading("🛠  HERRAMIENTAS");
        button("🧹  LIMPIAR CACHÉ PROPIA",
            "Elimina archivos de caché de esta app, no los juegos",() -> {
                File[] files=getCacheDir().listFiles();
                int count=0;
                if(files!=null) for(File f:files) if(remove(f)) count++;
                status.setText("Limpieza propia completada. Elementos: "+count);
            });

        button("🔋  AHORRO DE BATERÍA",
            "Abrir los ajustes de ahorro de Android",() -> {
                try {
                    startActivity(new Intent(
                        android.provider.Settings.ACTION_BATTERY_SAVER_SETTINGS));
                } catch(Exception e) {
                    status.setText("Abre Ahorro de batería desde Ajustes.");
                }
            });

        button("❄️  CUIDADO TÉRMICO",
            "Revisar temperatura y recomendaciones",() -> {
                refresh();
                status.setText("Reduce brillo y gráficos si el teléfono se calienta. Evita jugar mientras cargas.");
            });

        button("🔐  SHIZUKU",
            "Abrir Shizuku si está instalado",() -> {
                Intent i=getPackageManager().getLaunchIntentForPackage(
                    "moe.shizuku.privileged.api");
                if(i!=null) startActivity(i);
                else status.setText("Instala y activa Shizuku por separado. Aún no se aplican cambios avanzados.");
            });

        button("⚙️  AJUSTES DEL TELÉFONO",
            "Administrar el dispositivo",() ->
            startActivity(new Intent(android.provider.Settings.ACTION_SETTINGS)));

        status = label("●  SISTEMA LISTO",14,
            Color.rgb(220,150,255));
        card(status);

        TextView footer=label("GAMER TURBO CH  •  JUEGA INTELIGENTE",
            10,Color.GRAY);
        footer.setGravity(Gravity.CENTER);
        root.addView(footer);

        refresh();
    }

    @Override protected void onResume() {
        super.onResume();
        if(stats!=null) refresh();
    }
}
