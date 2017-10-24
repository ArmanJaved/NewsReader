package com.example.brainplow.newsreader.Common;

import com.example.brainplow.newsreader.Interface.IconBetterIdeaService;
import com.example.brainplow.newsreader.Interface.NewsService;
import com.example.brainplow.newsreader.Remote.IconBetterIdeaClient;
import com.example.brainplow.newsreader.Remote.RetrofitClient;

/**
 * Created by BrainPlow on 10/19/2017.
 */

public class Common {

    private static final  String BASE_URL = "https://newsapi.org/";
    public static final String API_KEY = "d8a1805b8c4b4bdd8faad032c9327fef";

    public static NewsService getNewsService()
    {
        return RetrofitClient.getClient(BASE_URL)
                .create(NewsService.class);

    }

    public static IconBetterIdeaService getIconService()
    {
        return IconBetterIdeaClient.getClient()
                .create(IconBetterIdeaService.class);

    }


    // https://newsapi.org/v1/articles?source=the-next-web&sortBy=latest&apiKey=d8a1805b8c4b4bdd8faad032c9327fef
    public static String getAPIUrl (String source, String sortBy, String apikEY)
    {
        StringBuilder apiUrl = new StringBuilder( "https://newsapi.org/v1/articles?source=");
        return apiUrl.append(source)
                .append("&sortBy=")
                .append(sortBy)
                .append("&apiKey=")
                .append(apikEY)
                .toString();
    }
}
