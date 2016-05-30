package com.example.nursul.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CheckActivity extends AppCompatActivity {
    String jsonResponse="";
    String urlJson_news_by_category="https://api.i-news.kz/news/search?query[cat_id]=2&limit=100&appId=Ozaa5nic5oeph7eethok&appKey=ushoh4ahpe8Aghahreel&version=1";
   static ArrayList<News> al;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //makeJsonObjectRequest();
        al = new ArrayList<>();
        /*
        TextView tv =(TextView)findViewById(R.id.tv);
        Linkify.addLinks(tv,Linkify.WEB_URLS);*/
    }

    public void makeJsonArrayRequest(View view) {
        JsonArrayRequest req = new JsonArrayRequest(urlJson_news_by_category, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    // Parsing json array response
                    // loop through each json object
                    jsonResponse = "";
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject person = (JSONObject) response.get(i);/*
                                String title = person.getString("title");
                                String site = person.getString("site");
                                jsonResponse += "site: " + site + "  ";
                                jsonResponse += "title: " + title + "  ";*/
                        String id = person.getString("status");
                        jsonResponse = "";
                        jsonResponse +=id;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(req);
    }

    public void makeJsonObjectRequest (){
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,urlJson_news_by_category, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray ar = response.getJSONArray("news");
                    for (int i=0;i<ar.length();i++) {
                        JSONObject jo = (JSONObject) ar.get(i);
                        String id = jo.getString("id");
                        String date = jo.getString("date");
                        String url = jo.getString("url");
                        String desc = jo.getString("description");
                        String title = jo.getString("title");
                        //String desc = jo.getString("date");
                        News news = new News(title,url);
                        al.add(news);

                        //jsonResponse += title +" "+desc;
                    }
                } catch (JSONException e) {e.printStackTrace();}
/*
  "id" : "7845511",
    "url" : "*ссылка на первоисточник*",
    "title" : "*заголовок новости*",
    "date" : "2014-12-10T15:14:17+06:00",
    "description" : "*анонс новости*",
    "text" : "*полный текст новости*",
    "feed" : "1",
    "category" : "14",
    "persons" : [],
    "cities" : [],
    "allowComments" : "1"
 */
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {}
        });
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);

    }

    public void viewNewsByCategory(View view){
        makeJsonObjectRequest();


        if (!al.isEmpty()){
        Intent intent = new Intent(this, MainActivity.class);

        //String message = editText.getText().toString();

        startActivity(intent);}
    }

}
