package com.example.yang.fabapp;


import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.view.View;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.google.gson.JsonObject;
import com.google.gson.*;

import java.util.HashMap;
import java.util.Map;
import java.math.*;

public class MovieList extends AppCompatActivity {

    public JsonArray ja = new JsonArray();
    public Context context = this;
    public int page_num;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);
        Bundle bd = getIntent().getExtras();
        String url = bd.getString("url");
        System.out.println(url);
        final Map<String, String> params = new HashMap<String, String>();
        page_num = 0;
        RequestQueue queue = Volley.newRequestQueue(this);



        StringRequest postRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>()
                {

                    @Override
                    public void onResponse(String response) {





                        Log.d("response", response);
                        System.out.println("yyr's res"+response);

                        JsonParser jp = new JsonParser();
                        JsonElement je = jp.parse(response);
                        ja = je.getAsJsonArray();


                        for(int i=0;i<Math.min(4,ja.size());i++)
                        {
                            String tx = "tx"+i;
                            String result = "";
                            JsonElement obj = ja.get(i);
                            JsonObject jobj = obj.getAsJsonObject();
                            int id = getResources().getIdentifier(tx, "id", context.getPackageName());

                            result+=jobj.get("title")+"\n";
                            result+="Year: "+jobj.get("year") +"\n";
                            result+="director: " + jobj.get("dir") + "\n";
                            result+="Genres: "+jobj.get("genres") + "\n";
                            result+="Stars" + jobj.get("stars") + "\n";
                            ((TextView)findViewById(id)).setText(result);


                        }


                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("security.error", error.toString());

                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                return params;
            }
        };



        queue.add(postRequest);
    }


    public void prev(View view)
    {


        if(page_num*4>=4) {
            page_num--;




            for (int i = 0 + page_num * 4; i < Math.min(4 + page_num * 4, ja.size()); i++) {
                String tx = "tx" + i%4;
                String result = "";
                JsonElement obj = ja.get(i);
                JsonObject jobj = obj.getAsJsonObject();
                int id = getResources().getIdentifier(tx, "id", context.getPackageName());

                result += jobj.get("title") + "\n";
                result += "Year: " + jobj.get("year") + "\n";
                result += "director: " + jobj.get("dir") + "\n";
                result += "Genres: " + jobj.get("genres") + "\n";
                result += "Stars" + jobj.get("stars") + "\n";
                ((TextView) findViewById(id)).setText(result);


            }
        }

    }

    public void next(View view)
    {
        if(page_num*4<ja.size()) {
            page_num++;
            for (int i = 0 + page_num * 4; i < Math.min(4 + page_num * 4, ja.size()); i++) {
                String tx = "tx" + i%4;
                String result = "";
                JsonElement obj = ja.get(i);
                JsonObject jobj = obj.getAsJsonObject();
                int id = getResources().getIdentifier(tx, "id", context.getPackageName());

                result += jobj.get("title") + "\n";
                result += "Year: " + jobj.get("year") + "\n";
                result += "director: " + jobj.get("dir") + "\n";
                result += "Genres: " + jobj.get("genres") + "\n";
                result += "Stars" + jobj.get("stars") + "\n";
                ((TextView) findViewById(id)).setText(result);


            }
        }


    }
}
