package com.example.brainplow.newsreader.Interface;

import com.example.brainplow.newsreader.Model.IconBetterIdea;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

/**
 * Created by BrainPlow on 10/19/2017.
 */

public interface IconBetterIdeaService {

    @GET
    Call<IconBetterIdea> getIconUrl (@Url String url);

}
