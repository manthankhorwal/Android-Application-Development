package com.example.ecommerce;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;

public class Front extends AppCompatActivity {
LottieAnimationView front;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_front);
        front=findViewById(R.id.front);
        front.addAnimatorListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationCancel(Animator animation) {
                super.onAnimationCancel(animation);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
              //  Toast.makeText(Front.this, "end", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
              //  Toast.makeText(Front.this, "Start", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAnimationStart(Animator animation, boolean isReverse) {
               // Toast.makeText(Front.this, "Start", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAnimationEnd(Animator animation, boolean isReverse) {
                startActivity(new Intent(Front.this,MainActivity.class));
            }
        });
    front.playAnimation();
    }

}