package com.example.carrerarace;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Debug;
import android.os.Handler;
import android.util.Log;

public class StartActivity extends AppCompatActivity {

    //////////////////////////////////////////////////////////starts App with pretty loading screen and waits
    // for two seconds
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        Handler handler = new Handler();
        Log.e("StartActivity" , "executed");
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(StartActivity.this, MainActivity.class);
                startActivity(intent);
            }
        }, 3000);

    }
    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}