package com.robosoft.flickrapp.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.robosoft.flickrapp.R;

/**
 * Created by rahul on 26/5/16.
 */
public class SplashScreenActivity extends Activity {

    /** Duration of wait **/
    private final int SPLASH_DISPLAY_LENGTH = 2000;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle bundle) {

        super.onCreate(bundle);
        setContentView(R.layout.activity_splash_screen);

        /* New Handler to start the Main-Activity
         * and close this Splash-Screen after some seconds.*/
        new Handler().postDelayed(new Runnable(){

            @Override
            public void run() {

                /* Create an Intent that will start the Main-Activity. */
                Intent intent = new Intent(SplashScreenActivity.this,HomeActivity.class);
                startActivity(intent);
                finish();
            }

        }, SPLASH_DISPLAY_LENGTH);
    }
}
