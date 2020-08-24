package com.rku.tutorial04;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);

        textView = findViewById(R.id.SplashTitle);
        int unicode = 0x1F64F;
        String emoji = getEmojiByUnicode(unicode);
        String text = "Welcome!! ";
        textView.setText(text + emoji);


        ImageView imageView = findViewById(R.id.aboutUSimageview);
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade);
        imageView.startAnimation(animation);

        Thread timer = new Thread(){
            @Override
            public void run() {
                try{
                    sleep(2000);
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                    super.run();
                }
                catch (InterruptedException e){
                    e.printStackTrace();
                }
                super.run();
            }
        };
        timer.start();

    }
    public String getEmojiByUnicode(int unicode){
        return new String(Character.toChars(unicode));
    }

}