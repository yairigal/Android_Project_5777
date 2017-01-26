package project.android.com.second_app.controller;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import project.android.com.second_app.R;

/**
 * this activity sets splashscreen before the first activity
 */
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
