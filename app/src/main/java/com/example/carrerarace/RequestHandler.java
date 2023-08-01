package com.example.carrerarace;

import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;


public abstract class RequestHandler extends AppCompatActivity {
    ////////////////////////////////////////does a request with headers, data etc.
    public static void volleyRequest(int method, String postUrl, JSONObject postData, RequestQueue requestQueue, String key, final Callback callback) {
        String headerKey;
        String headerValue;
        RetryPolicy mRetryPolicy = new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

        if(!key.equals("")){
            headerKey = "Authorization";
            headerValue = "Token " + key;
        }
        else {
            headerKey = "Content-Type";
            headerValue = "application/json";
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(method, postUrl, postData, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("worked", "lol");
                callback.myCallback(response);
            }


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }

        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                final Map<String, String> headers = new HashMap<>();
                headers.put(headerKey, headerValue);
                return headers;
            }

        };

        Log.d("Request", method + " " + postUrl + " " + headerKey + ":" + headerValue);
        if(postData != null) {
            Log.d("Request", postData.toString());
        }
        jsonObjectRequest.setRetryPolicy(mRetryPolicy);
        requestQueue.add(jsonObjectRequest);


    }
}
