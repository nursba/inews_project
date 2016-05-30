package com.example.nursul.myapplication;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private Toolbar toolbar;

    private TextView tvEmptyView;
    private RecyclerView mRecyclerView;
    private DataAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private List<Student> studentList;
    protected Handler handler;
    String urlJson_news_by_category = "https://api.i-news.kz/news/search?query[cat_id]=2&limit=100&appId=Ozaa5nic5oeph7eethok&appKey=ushoh4ahpe8Aghahreel&version=1";
    ArrayList<News> arl ;
    String jsonResponse;
    boolean yes_no =false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        tvEmptyView = (TextView) findViewById(R.id.empty_view);
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        studentList = new ArrayList<Student>();
        handler = new Handler();
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle("Android Students");

        }
         arl = new ArrayList<>();
        if (arl.isEmpty()) Log.i("**************","///////////////////////////////////////////////////////////////");
       // makeJsonObjectRequest();

        loadData();



        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);

        // use a linear layout manager
        mRecyclerView.setLayoutManager(mLayoutManager);

        // create an Object for Adapter
        mAdapter = new DataAdapter(studentList, mRecyclerView,this);

        // set the adapter object to the Recyclerview
        mRecyclerView.setAdapter(mAdapter);
        //  mAdapter.notifyDataSetChanged();


        if (studentList.isEmpty()) {
            mRecyclerView.setVisibility(View.GONE);
            tvEmptyView.setVisibility(View.VISIBLE);

        } else {
            mRecyclerView.setVisibility(View.VISIBLE);
            tvEmptyView.setVisibility(View.GONE);
        }

        mAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                //add null , so the adapter will check view_type and show progress bar at bottom
                studentList.add(null);
                mAdapter.notifyItemInserted(studentList.size() - 1);

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //   remove progress item
                        studentList.remove(studentList.size() - 1);
                        mAdapter.notifyItemRemoved(studentList.size());
                        //add items one by one
                        int start = studentList.size();
                        int end=0;
                       if (studentList.size()<50) { end = start + 20;}

                        for (int i = start + 1; i <= end; i++) {
                          // studentList.add(new Student("Student " + i, "google" +  ".com"));
                            News n = CheckActivity.al.get(i);
                            studentList.add(new Student(n.title,n.desc));
                            mAdapter.notifyItemInserted(studentList.size());
                        }
                        mAdapter.setLoaded();
                        //or you can add all at once but do not forget to call mAdapter.notifyDataSetChanged();
                    }
                }, 2000);

            }
        });

    }


    // load initial data
    private void loadData() {
          for (int i = 1; i <= 21; i++) {
              //studentList.add(new Student("Student " + i, "google" + ".com"));
              //Log.i("***************", arl.size() + "");
              News news = CheckActivity.al.get(i);
                studentList.add(new Student(news.title, news.desc));
          }
    }

    public void makeJsonObjectRequest (){
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,urlJson_news_by_category, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    /*
                    String name = response.getString("name");
                    JSONObject phone = response.getJSONObject("phone");
                    String id = response.getString("id");
  */
                    /*
                    String desc = response.getString("description");
                    String title = response.getString("title");
                    String ddate = response.getString("date");
                    String feed = response.getString("feed");
                    String url = response.getString("url");

                    jsonResponse = "";
                    jsonResponse +=desc+"\n\n";
                    jsonResponse +=ddate+"*************";
                    jsonResponse +=title;
                    jsonResponse +=feed;
                    jsonResponse +=url;
                    */

                    String status = response.getString("status");
                     jsonResponse = "";

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
                        arl.add(news);

                        //jsonResponse += title +" "+desc;
                    }
                 Log.i("id",arl.size()+"");
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



    }
class News {
    String title;
    String desc;

    News(String title,String desc){
    this.title = title;
    this.desc = desc;
    }
}



