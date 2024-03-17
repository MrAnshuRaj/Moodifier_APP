package com.anshu.moodifier;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;

import java.util.Locale;
import java.util.Objects;

public class SplashScreen extends AppCompatActivity {
    TextToSpeech ts;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences sharedPreferences=getSharedPreferences("Moodifier",MODE_PRIVATE);
                boolean loggedIn=sharedPreferences.getBoolean("Logged",false);
                if(loggedIn)
                {
                    String name=sharedPreferences.getString("Name","XYZ");
                    if(!name.equals("XYZ")) {
                        ts = new TextToSpeech(SplashScreen.this, i -> {
                            if (i != TextToSpeech.ERROR)
                                ts.setLanguage(Locale.ENGLISH);
                        });
                        ts.speak("Hi "+name+"!",TextToSpeech.QUEUE_FLUSH,null);
                    }
                    startActivity(new Intent(SplashScreen.this,Dashboard.class));
                }
                else
                    startActivity(new Intent(SplashScreen.this,MainActivity.class));
                finish();
            }
        },2000);
    }
}