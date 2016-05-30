package com.example.nursul.myapplication;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class StartActivity extends AppCompatActivity {
    String jsonResponse="";
    String urlJson_news_by_category_1 = "https://api.i-news.kz/news/search?query[cat_id]=";
    String urlJson_news_by_category_2 = "&limit=100&appId=Ozaa5nic5oeph7eethok&appKey=ushoh4ahpe8Aghahreel&version=1";
    String urlJson_news_by_category_="";
    public static ArrayList<News> al;
    static  int category =0;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //makeJsonObjectRequest();
        al = new ArrayList<>();
        /*
        TextView tv =(TextView)findViewById(R.id.tv);
        Linkify.addLinks(tv,Linkify.WEB_URLS);*/
    }

    public void makeJsonArrayRequest(View view) {
        JsonArrayRequest req = new JsonArrayRequest(urlJson_news_by_category_, new Response.Listener<JSONArray>() {
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

    public void makeJsonObjectRequest (int a){


         urlJson_news_by_category_= urlJson_news_by_category_1+a+urlJson_news_by_category_2;
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,urlJson_news_by_category_, null, new Response.Listener<JSONObject>() {
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
                        String feed = jo.getString("feed");
                        News news = new News(title,desc,date,url);
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
        Button b =(Button)view;
        String st = b.getText().toString().toLowerCase();
        int a=0;

        switch (st){
            case "политика" : a=1;break;
            case "в мире": a=2;break;
            case "общество": a=3;break;
            case "экономика": a=4;break;
            case "спорт": a=5;break;
            case "проишествия": a=6;break;
            case "культура": a=7;break;
            default: a=0;
        }


        makeJsonObjectRequest(a);
        pd = new ProgressDialog(this);
        pd.setTitle("Title");
        pd.setMessage("Message");

      MyTask  mt = new MyTask(pd);
        mt.execute();
      //  pd.setCancelable(false);
        // добавляем кнопку
        pd.setButton(Dialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        pd.show();


/*

        Log.i("the baton",b.getText().toString()+"  "+a);
        */
        if (!al.isEmpty())
        {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        }

    }

}
 class MyTask extends AsyncTask<Void, Void, Void> {
ProgressDialog pd=null;
     MyTask(ProgressDialog pd){
       this.  pd =pd;
     }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (!isNewsLoaded()){pd.dismiss();}
    }

    @Override
    protected Void doInBackground(Void... params) {
        try {
            TimeUnit.SECONDS.sleep(1);
            Log.i("mytask","*******************");
            if (!isNewsLoaded()){pd.dismiss();}
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
        if (!isNewsLoaded()){pd.dismiss();}
        Log.i("mytask","Post***********************");

    }

     static  boolean isNewsLoaded(){
         return  (StartActivity.al.isEmpty());
     }
}
