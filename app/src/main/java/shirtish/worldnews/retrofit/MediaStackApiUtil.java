package shirtish.worldnews.retrofit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import shirtish.worldnews.Constants;

public class MediaStackApiUtil {
    public static MediaStackApi getMediaStackRetrofitApi() {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(ArrayList.class, new MediaStackJsonDeserializer())
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.MEDIA_STACK_API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        return retrofit.create(MediaStackApi.class);
    }
}
