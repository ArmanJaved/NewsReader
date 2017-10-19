package com.example.brainplow.newsreader.Interface;

import com.example.brainplow.newsreader.Model.WebSite;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by BrainPlow on 10/19/2017.
 */

public interface NewsService {


    @GET("v1/sources?language=en")
    Call<WebSite> getSources();
}
