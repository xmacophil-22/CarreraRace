package com.example.carrerarace;

import static com.example.carrerarace.MainActivity.USERNAME_MESSAGE;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Winner extends AppCompatActivity {

    private String username;
    private RequestQueue requestQueue;
    private TextView player1TV, player2TV, player3TV;
    public static String IP = "192.168.53.64";

    //////////////////////////////////////////////Displays winners from ?score
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_winner);

        Intent intent = getIntent();
        username = intent.getStringExtra(USERNAME_MESSAGE);
        requestQueue = Volley.newRequestQueue(this);
        player1TV = (TextView) findViewById(R.id.player1TV);
        player2TV = (TextView) findViewById(R.id.player2TV);
        player3TV = (TextView) findViewById(R.id.player3TV);

        RequestHandler.volleyRequest(Request.Method.GET, "http://" + IP +":8000/game/?score", null, requestQueue, "", new Callback() {
            @Override
            public void myCallback(JSONObject result) {
                try {
                    JSONArray array = result.getJSONArray("wins");
                    player1TV.setText(array.getString(0));
                    player2TV.setText(array.getString(1));
                    player3TV.setText(array.getString(2));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void back(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}