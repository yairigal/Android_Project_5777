package project.android.com.android5777_9254_6826.controller;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import project.android.com.android5777_9254_6826.R;
import project.android.com.android5777_9254_6826.model.entities.Properties;

public class SplashScreen extends AppCompatActivity {

    private static final int SPLASH_DISPLAY_LENGTH = 3000;
    static Activity ctx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        ((TextView) findViewById(R.id.loadingTV)).setText(getIntent().getStringExtra("text").toString());
        ctx = this;
        //Properties.animateView(progressOverlay, View.VISIBLE, 0.4f, 200);
        /* New Handler to start the Menu-Activity
         * and close this Splash-Screen after some seconds.*/
        /*new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {

                *//* Create an Intent that will start the Menu-Activity. *//*
*//*                Intent mainIntent = new Intent(SplashScreen.this,BusinessActivity.class);
                SplashScreen.this.startActivity(mainIntent);*//*
                SplashScreen.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);*/
    }
    public void setSplashScreenText(String text){
        ((TextView)  ctx.findViewById(R.id.loadingTV)).setText(text);
    }

    public static void hideSplashScreen(){
        ctx.finish();
    }
}
