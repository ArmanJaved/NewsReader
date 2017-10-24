package com.example.brainplow.newsreader;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.brainplow.newsreader.Adapter.ListNewsAdapter;
import com.example.brainplow.newsreader.Common.Common;
import com.example.brainplow.newsreader.Interface.NewsService;
import com.example.brainplow.newsreader.Model.Article;
import com.example.brainplow.newsreader.Model.News;
import com.flaviofaria.kenburnsview.KenBurnsView;
import com.github.florent37.diagonallayout.DiagonalLayout;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.util.List;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListNews extends AppCompatActivity {


    KenBurnsView kbv;
    DiagonalLayout diagonalLayout;
    SpotsDialog dialog;
    NewsService mservice;
    TextView top_author, top_title;
    SwipeRefreshLayout swipeRefreshLayout;
    String source="", sortby="", webHotUrl="";

    ListNewsAdapter adapter;
    RecyclerView lstNews;
    RecyclerView.LayoutManager layoutManager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_news);


        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        //service
        mservice = Common.getNewsService();
        dialog = new SpotsDialog(this);
        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swiperefesh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                try {
                    loadNews(source, true);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

        diagonalLayout= (DiagonalLayout)findViewById(R.id.diagonallayout);
        diagonalLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // click to read the hot news


                Intent detail = new Intent( getBaseContext(), DetailArticle.class);
                detail.putExtra("webURL", webHotUrl);
                startActivity(detail);

            }
        });
        kbv = (KenBurnsView)findViewById(R.id.top_image);
        top_author = (TextView)findViewById(R.id.top_author);
        top_title = (TextView)findViewById(R.id.top_title);

        lstNews = (RecyclerView)findViewById(R.id.lstNews);
        lstNews.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        lstNews.setLayoutManager(layoutManager);

        if (getIntent()!= null)
        {
            source = getIntent().getStringExtra("source");
            sortby = getIntent().getStringExtra("sortBy");
            if (!source.isEmpty() )
            {
                try {
                    loadNews(source, false);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }

    }

    private void loadNews(String source, boolean isRefreshed) throws JSONException {

        if (!isRefreshed)
        {
            dialog.show();

            String url = Common.getAPIUrl(source, sortby, Common.API_KEY);

            mservice.getNewsArticles(Common.getAPIUrl(source, sortby, Common.API_KEY))
                    .enqueue(new Callback<News>() {
                        @Override
                        public void onResponse(Call<News> call, final Response<News> response) {
                            dialog.dismiss();

                            List<Article> ar = response.body().getArticles();
                            Picasso.with(getApplicationContext())
                                    .load(response.body().getArticles().get(0).getUrlToImage())
                                    .into(kbv);
                            top_title.setText(response.body().getArticles().get(0).getTitle());
                            String authsize = response.body().getArticles().get(0).getAuthor();
                            try {
                            if (authsize.length() < 25)
                            {

                                    top_author.setText(response.body().getArticles().get(0).getAuthor());
                            }

                            }
                                catch (Exception e)
                            {
                            }
                            webHotUrl = response.body().getArticles().get(0).getUrl();


                            List<Article> removeFirstItem = response.body().getArticles();
                            removeFirstItem.remove(0);

                            adapter = new ListNewsAdapter( removeFirstItem, getBaseContext());
                            adapter.notifyDataSetChanged();
                            lstNews.setAdapter(adapter);

                        }



                        @Override
                        public void onFailure(Call<News> call, Throwable t) {

                            Toast.makeText(getApplicationContext(), "Status Error ", Toast.LENGTH_LONG).show();
                        }
                    });

        }
        else
        {
            dialog.show();


            mservice.getNewsArticles(Common.getAPIUrl(source, sortby, Common.API_KEY))
                    .enqueue(new Callback<News>() {
                        @Override
                        public void onResponse(Call<News> call, final Response<News> response) {
                            dialog.dismiss();

                            List<Article> ar = response.body().getArticles();
                            Picasso.with(getApplicationContext())
                                    .load(response.body().getArticles().get(0).getUrlToImage())
                                    .into(kbv);
                            top_title.setText(response.body().getArticles().get(0).getTitle());
                            top_author.setText(response.body().getArticles().get(0).getAuthor());
                            webHotUrl = response.body().getArticles().get(0).getUrl();


                            List<Article> removeFirstItem = response.body().getArticles();
                            removeFirstItem.remove(0);

                            adapter = new ListNewsAdapter( removeFirstItem, getBaseContext());
                            adapter.notifyDataSetChanged();
                            lstNews.setAdapter(adapter);

                        }



                        @Override
                        public void onFailure(Call<News> call, Throwable t) {

                            Toast.makeText(getApplicationContext(), "Status Error ", Toast.LENGTH_LONG).show();
                        }
                    });

            swipeRefreshLayout.setRefreshing(false);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            startActivity(new Intent(ListNews.this, MainActivity.class));
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }

}
