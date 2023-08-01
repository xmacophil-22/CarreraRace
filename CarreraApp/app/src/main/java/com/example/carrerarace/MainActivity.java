package com.example.carrerarace;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    public static final String USERNAME_MESSAGE = "usernameMessage";

    private EditText editText;
    private String username;
    private ProgressBar progressBar;
    private TextView textView;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText = (EditText) findViewById(R.id.Username);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);
        textView = (TextView) findViewById(R.id.textView);
        textView.setVisibility(View.INVISIBLE);
        requestQueue = Volley.newRequestQueue(this);
    }

    ///////////////////////////////////////On Click participate Upload userdata and login
    public void participate(View view){
        username = editText.getText().toString();
        if(username != "") {
            JSONObject myJSON = new JSONObject();
            try {
                myJSON.put("username", username);
                myJSON.put("password1", "philippStinkt");
                myJSON.put("password2", "philippStinkt");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            RequestHandler.volleyRequest(Request.Method.POST, "http://"+Winner.IP+":8000/registration/", myJSON, requestQueue, "", new Callback(){
                @Override
                public void myCallback(JSONObject result){
                    Log.e("registered", "");
                    JSONObject my2JSON = new JSONObject();
                    try {
                        my2JSON.put("username", username);
                        my2JSON.put("password", "philippStinkt");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    RequestHandler.volleyRequest(Request.Method.POST, "http://"+Winner.IP+":8000/login/", my2JSON, requestQueue, "", new Callback() {
                        @Override
                        public void myCallback(JSONObject result) {
                            Log.e("loggedIn", "");
                            editText.setVisibility(View.GONE);
                            view.setVisibility(View.GONE);
                            progressBar.setVisibility(View.VISIBLE);
                            textView.setVisibility(View.VISIBLE);
                            waitForStart();
                        }
                    });
                }
            });
        }
    }

    ////////////////////////////////////////////////Waits For the Game to Start while there is no decent result
    // from ?currentRound
    public void waitForStart(){
        Log.e("Maya", "does Request");
        RequestHandler.volleyRequest(Request.Method.GET, "http://"+Winner.IP+":8000/game/?currentRound", null, requestQueue, "", new Callback() {
            @Override
            public void myCallback(JSONObject result) {
                try {
                    Log.e("Maya", result.toString());
                    result.get("player");
                    Log.e("Maya", "player tag exists");
                    Intent intent = new Intent(getApplicationContext(), RaceRunning.class);
                    intent.putExtra(USERNAME_MESSAGE, username);
                    startActivity(intent);
                } catch (JSONException e) {
                    try {
                        Log.e("Maya", "player tag does not exist");
                        Thread.sleep(1000);
                        waitForStart();
                    } catch (InterruptedException interruptedException) {
                        interruptedException.printStackTrace();
                    }
                }
            }
        });
    }
    ////////////////////////////////////////////provides app from going Back
    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}