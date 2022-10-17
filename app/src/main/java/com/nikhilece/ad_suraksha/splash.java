package com.nikhilece.ad_suraksha;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

public class splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash);
        LogoLancher logoLancher = new LogoLancher();
        logoLancher.start();
    }

    private class LogoLancher extends Thread {
        public void run() {
            try {
                sleep(3500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Intent intent = new Intent(splash.this, login.class);
            startActivity(intent);
            splash.this.finish();
        }
    }
}