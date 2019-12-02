package com.pgdit101.iit.notifee;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;

public class SplashScreenActivity extends AppCompatActivity {

    ImageView logoRotate, logoText;
    ProgressBar mProgressBar;

    Animation rotateAnim, textAnim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        logoRotate = (ImageView) findViewById(R.id.rotate);
        logoText = (ImageView) findViewById(R.id.logoArt);
        mProgressBar = findViewById(R.id.splash_screen_progress_bar);


        rotateAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_anim);
        logoRotate.startAnimation(rotateAnim);

        textAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.text_anim);
        logoText.startAnimation(textAnim);


        Thread myThread = new Thread(new Runnable() {
            @Override
            public void run() {

                doWork();
                startApp();
                finish();
            }
        });

        myThread.start();

    }


    private void  doWork()
    {
        for (int progress = 0; progress <= 100; progress += 1){

            try {
                Thread.sleep(20);
                mProgressBar.setProgress(progress);

            } catch (InterruptedException e) {

                e.printStackTrace();

            }

        }
    }


    private void startApp()
    {
        Intent intent = new Intent(SplashScreenActivity.this, ListViewActivity.class);
        startActivity(intent);
    }
}
