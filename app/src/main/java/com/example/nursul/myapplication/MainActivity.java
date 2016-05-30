package com.example.nursul.myapplication;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import java.util.List;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private Toolbar toolbar;

    private TextView tvEmptyView;
    private RecyclerView mRecyclerView;
    private DataAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private List<NewsBean> newsList;
    protected Handler handler;
    ArrayList<News> arl ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        tvEmptyView = (TextView) findViewById(R.id.empty_view);
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        newsList = new ArrayList<NewsBean>();

        handler = new Handler();
        if (toolbar != null)
        {
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle("Новости");

        }
         arl = new ArrayList<>();
       // makeJsonObjectRequest();
         loadData();

        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new DataAdapter(newsList, mRecyclerView,this);
        mRecyclerView.setAdapter(mAdapter);

        if (newsList.isEmpty()) {
            mRecyclerView.setVisibility(View.GONE);
            tvEmptyView.setVisibility(View.VISIBLE);

        } else {
            mRecyclerView.setVisibility(View.VISIBLE);
            tvEmptyView.setVisibility(View.GONE);
        }

        mAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                newsList.add(null);
                mAdapter.notifyItemInserted(newsList.size() - 1);

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //   remove progress item
                        newsList.remove(newsList.size() - 1);
                        mAdapter.notifyItemRemoved(newsList.size());

                        int start = newsList.size();
                        int end= start + 20;

                        for (int i = start + 1; i <= end; i++) {
                          // studentList.add(new Student("Student " + i, "google" +  ".com"));
                            News n = StartActivity.al.get(i);
                            newsList.add(new NewsBean(n.title,n.desc,n.date,n.feed));
                            mAdapter.notifyItemInserted(newsList.size());
                        }
                        mAdapter.setLoaded();
                    }
                }, 2000);

            }
        });

    }


    // начальная загрузка списка новостей
    private void loadData() {
          for (int i = 1; i <= 21; i++) {
              //Log.i("***************", arl.size() + "");
              News news = StartActivity.al.get(i);
              newsList.add(new NewsBean(news.title, news.desc, news.date, news.feed));
          }
    }


    }
class News {
    String title;
    String desc;
    String date;
    String feed;

    News(String title,String desc,String date,String feed){
        this.title = title;
        this.desc = desc;
        this.date = date;
        this.feed = feed;
    }
}



