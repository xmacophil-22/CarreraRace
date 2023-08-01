package com.example.carrerarace;

import static com.example.carrerarace.MainActivity.USERNAME_MESSAGE;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class RaceRunning extends AppCompatActivity {

    private TextView usernameDisplay;
    private TextView player1, player2, wins1, wins2, losses1, losses2;
    private String username;
    private Button button;
    private RequestQueue requestQueue;
    private Handler handler = new Handler();
    private Runnable runnable;
    int delay = 20000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_race_running);

        Intent intent = getIntent();
        username = intent.getStringExtra(USERNAME_MESSAGE);

        requestQueue = Volley.newRequestQueue(this);

        usernameDisplay = (TextView) findViewById(R.id.usernameTV);
        usernameDisplay.setText(username);

        player1 = (TextView) findViewById(R.id.currentPlayer1TV);
        player2 = (TextView) findViewById(R.id.currentPlayer2TV);
        wins1 = (TextView) findViewById(R.id.wins1TV);
        wins2 = (TextView) findViewById(R.id.wins2TV);
        losses1 = (TextView) findViewById(R.id.losses1TV);
        losses2 = (TextView) findViewById(R.id.losses2TV);
        button = (Button) findViewById(R.id.readyBtn);
        button.setVisibility(View.INVISIBLE);
        refresh();
    }

    //////////////////////////////////////////////////refreshes every 20 seconds
    @Override
    protected void onResume(){
        handler.postDelayed(runnable = new Runnable() {
            @Override
            public void run() {
                handler.postDelayed(runnable, delay);
                refresh();
            }
        }, delay);
        super.onResume();
    }
    @Override
    protected  void onPause(){
        handler.removeCallbacks(runnable);
        super.onPause();
    }
    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    //////////////////////////////////////////////manually Refresh current enemys

    public void refreshBtnPressed(View view){
        refresh();
    }

    /////////////////////////////////////////////On Ready button click sets player ready on Server
    public void ready(View view){
        RequestHandler.volleyRequest(Request.Method.GET, "http://"+Winner.IP+":8000/game/?ready", null, requestQueue, "", new Callback() {
            @Override
            public void myCallback(JSONObject result) {
                button.setVisibility(View.INVISIBLE);
            }
        });
        //request
    }

    //////////////////////////////////////////reloads current enemys and their stats
    // if the user is a part of enemys, ready Button appears

    private void refresh(){
        //Request and set all data
        RequestHandler.volleyRequest(Request.Method.GET, "http://"+Winner.IP+":8000/game/?currentRound", null, requestQueue, "", new Callback() {
            @Override
            public void myCallback(JSONObject result) {
                    try {
                        if((result.get("player").toString().equals(username) || result.get("enemy").toString().equals(username)) && (!result.get("player").toString().equals(player1.getText()) || !result.get("enemy").toString().equals(player2.getText()))){ //wenn Player dran
                            button.setVisibility(View.VISIBLE);
                        }
                        if(!(result.get("player").toString().equals(username) || result.get("enemy").toString().equals(username))){
                            button.setVisibility(View.INVISIBLE);
                        }
                        player1.setText(result.get("player").toString());
                        player2.setText(result.get("enemy").toString());
                        wins1.setText("Wins: " + result.get("Awins").toString());
                        losses1.setText("Losses: " + result.get("Alosses").toString());
                        wins2.setText("Wins: " + result.get("Bwins").toString());
                        losses2.setText("Losses: " + result.get("Blosses").toString());
                    } catch (JSONException e) {
                        Intent intent = new Intent(getApplicationContext(), Winner.class);
                        intent.putExtra(USERNAME_MESSAGE, username);
                        startActivity(intent);
                        e.printStackTrace();
                    }

            }
        });
    }
}