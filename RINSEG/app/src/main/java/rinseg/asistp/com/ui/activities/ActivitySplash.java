package rinseg.asistp.com.ui.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.preference.PreferenceManager;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import rinseg.asistp.com.models.User;
import rinseg.asistp.com.rinseg.R;
import rinseg.asistp.com.utils.Constants;
import rinseg.asistp.com.utils.Generic;
import rinseg.asistp.com.utils.RinsegModule;

public class ActivitySplash extends AppCompatActivity {

    boolean logoVisible;
    ViewGroup viewRoot;
    ImageView logo;
    RealmConfiguration myConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        viewRoot = (ViewGroup) findViewById(R.id.linear_root_splash);
        logo = (ImageView) viewRoot.findViewById(R.id.logo_splash);


        //configuramos Realm
        Realm.init(this.getApplicationContext());
        myConfig = new RealmConfiguration.Builder()
                .name("rinseg.realm")
                .schemaVersion(2)
                .modules(new RinsegModule())
                .deleteRealmIfMigrationNeeded()
                .build();

        // Creamos la tarea de Splash
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                launchActivity();
            }
        };

        // Mostramos el Splash
        Timer timer = new Timer();
        timer.schedule(task, Constants.SPLASH_SCREEN_DELAY);
    }


    // sobreescribimos vacio el boton "Atras" para bloquearlo
    @Override
    public void onBackPressed() {
    }

    @Override
    protected void onResume(){
        super.onResume();
        AnimarLogo();
    }

    private void AnimarLogo() {
        Animation fadeInDown = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.enter_from_down_logo);
        fadeInDown.setDuration(600);
        logo.startAnimation(fadeInDown);
    }


    /**
     * Created on 28/09/16
     * Módulo encargado de lanzar la actividad Login
     */
    private void launchActivity() {
        Realm realm = Realm.getInstance(myConfig);
        try {

            realm.beginTransaction();
            User usuario = realm.where(User.class).findFirst();
            realm.commitTransaction();

            Intent mainIntent;
            if (usuario != null) {
                mainIntent = new Intent().setClass(ActivitySplash.this, ActivityMain.class);
            } else {
                mainIntent = new Intent().setClass(ActivitySplash.this, ActivityLogin.class);
            }
            startActivity(mainIntent);
            finish();

        } catch (Exception e) {

        } finally {
            realm.close();
        }


    }

}

