package com.example.mp2_weatherapp_final;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.vectordrawable.graphics.drawable.ArgbEvaluator;

import com.tomer.fadingtextview.FadingTextView;

public class SplashScreen extends Activity {


    ImageView imageView; //Splash
    FadingTextView fadingTextView; //Splash

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* Splash Screen    */
        fadingTextView = findViewById(R.id.FadTxt);

        imageView = findViewById(R.id.logo);

        imageView.animate().alpha(0.5f).setDuration(2000);
        imageView.animate().translationX(1000).setDuration(5000);
        imageView.animate().translationY(1000).setDuration(5000);
        imageView.animate().scaleX(10).setDuration(5000);
        imageView.animate().scaleY(10).setDuration(5000);

        fadingTextView.animate().alpha(0.5f).setDuration(1000);
        /*
        ObjectAnimator txtanim = ObjectAnimator.ofInt(fadingTextView,"TextColor", Color.MAGENTA, Color.GREEN, Color.WHITE, Color.GREEN, Color.RED, Color.YELLOW, Color.WHITE);
        txtanim.setDuration(4000);
        txtanim.setEvaluator(new ArgbEvaluator());
        txtanim.setRepeatMode(Animation.RESTART);
        //txtanim.setRepeatMode(Animation.INFINITE);
        txtanim.start();

        ObjectAnimator animator1 = ObjectAnimator.ofFloat(imageView,"rotationY",0.0f,180.0f);

        animator1.setDuration(4900);
        animator1.setRepeatCount(Animation.RESTART);
        animator1.start();*/

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }
        },3000);
        /* Splash End   */
    }
}
