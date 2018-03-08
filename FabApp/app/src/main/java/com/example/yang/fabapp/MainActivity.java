package com.example.yang.fabapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.JsonReader;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;



import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }


    public void goSearch()
    {

        Intent goToIntent = new Intent(this, Search.class);
        startActivity(goToIntent);

    }

    public void login(View view){


        final Map<String, String> params = new HashMap<String, String>();

        System.out.println("login intent!");

        RequestQueue queue = Volley.newRequestQueue(this);

        final Context context = this;
        String pwd = ((EditText)findViewById(R.id.password)).getText().toString();
        String uname = ((EditText)findViewById(R.id.Username)).getText().toString();
        System.out.println(pwd);
        System.out.println(uname);
        String url = "http://13.58.92.109:8080/Fablix/AndroidLogin?username=";
        url+= uname+"&password="+pwd;




        StringRequest postRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>()
                {

                    @Override
                    public void onResponse(String response) {



                        Log.d("response", response);
                        System.out.println("yyr's res"+response);
                        ((TextView)findViewById(R.id.http_response)).setText(response);
                        if(response.equals("true"))
                            goSearch();
                        else
                            ((TextView)findViewById(R.id.http_response)).setText("Login error");

                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("security.error", error.toString());
                        ((TextView)findViewById(R.id.http_response)).setText("Network error");
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                return params;
            }
        };

        // Add the request to the RequestQueue.
        queue.add(postRequest);


        return ;
    }

}
