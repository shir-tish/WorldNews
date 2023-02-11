package shirtish.worldnews.retrofit;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import shirtish.worldnews.models.Article;

public interface MediaStackApi {
    @GET("news")
    Call<ArrayList<Article>> getAllArticles(
            @Query("access_key") String access_key
    );

    @GET("news")
    Call<ArrayList<Article>> getArticleByCategory(
            @Query("access_key") String access_key,
            @Query("categories") String categories
    );
}
