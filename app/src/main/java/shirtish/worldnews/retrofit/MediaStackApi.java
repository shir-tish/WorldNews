package shirtish.worldnews.retrofit;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import shirtish.worldnews.models.New;

public interface MediaStackApi {
    @GET("news")
    Call<ArrayList<New>> getNew(
            @Query("access_key") String accessKey
    );
}
