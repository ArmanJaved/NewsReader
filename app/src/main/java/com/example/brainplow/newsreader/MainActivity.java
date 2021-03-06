package com.example.brainplow.newsreader;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.brainplow.newsreader.Adapter.ListSourceAdapter;
import com.example.brainplow.newsreader.Common.Common;
import com.example.brainplow.newsreader.Interface.NewsService;
import com.example.brainplow.newsreader.Model.WebSite;
import com.google.gson.Gson;

import dmax.dialog.SpotsDialog;
import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {


    RecyclerView listWebsite;
    RecyclerView.LayoutManager layoutManager;
    NewsService mService;

    ListSourceAdapter adapter;
    SpotsDialog dialog;
    SwipeRefreshLayout swipeRefreshLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Paper.init(this);
        mService = Common.getNewsService();


        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swiperefreshlyt);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadWebsiteSource(true);
            }
        });
        listWebsite = (RecyclerView)findViewById(R.id.list_source);
        listWebsite.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        listWebsite.setLayoutManager(layoutManager);

        dialog = new SpotsDialog(this);
        dialog.show();
        loadWebsiteSource(false);
        


    }

    private void loadWebsiteSource(boolean isRefreshed) {
        if (!isRefreshed)
        {
            String cache = Paper.book().read("cache");
            if (cache != null && !cache.isEmpty() )
            {
                dialog.dismiss();
                WebSite webSite = new Gson().fromJson(cache, WebSite.class);
                adapter = new ListSourceAdapter(getApplicationContext(),webSite);
                adapter.notifyDataSetChanged();
                listWebsite.setAdapter(adapter);
            }
            else
            {
                mService.getSources().enqueue(new Callback<WebSite>() {
                    @Override
                    public void onResponse(Call<WebSite> call, Response<WebSite> response) {
                        dialog.dismiss();
                        adapter = new ListSourceAdapter(getApplicationContext(), response.body());
                        adapter.notifyDataSetChanged();
                        listWebsite.setAdapter(adapter);
                        Paper.book().write("cache", new Gson().toJson(response.body()));

                    }

                    @Override
                    public void onFailure(Call<WebSite> call, Throwable t) {

                    }
                });
            }
        }
        else {

            mService.getSources().enqueue(new Callback<WebSite>() {
                @Override
                public void onResponse(Call<WebSite> call, Response<WebSite> response) {
                    dialog.dismiss();
                    adapter = new ListSourceAdapter(getApplicationContext(), response.body());
                    adapter.notifyDataSetChanged();
                    listWebsite.setAdapter(adapter);
                    Paper.book().write("cache", new Gson().toJson(response.body()));
                    swipeRefreshLayout.setRefreshing(false);


                }

                @Override
                public void onFailure(Call<WebSite> call, Throwable t) {

                }
            });
        }
    }
}
