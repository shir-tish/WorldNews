package shirtish.worldnews.viewmodels;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import shirtish.worldnews.Constants;
import shirtish.worldnews.models.Article;
import shirtish.worldnews.retrofit.MediaStackApi;
import shirtish.worldnews.retrofit.MediaStackApiUtil;

public class ArticleViewModel extends ViewModel {
    private MutableLiveData<List<Article>> articlesList;

    public LiveData<List<Article>> getArticlesList() {
        if (articlesList == null) {
            articlesList = new MutableLiveData<>();
            loadArticles();
        }
        return articlesList;
    }

    private void loadArticles() {
        MediaStackApi mediaStackApi = MediaStackApiUtil.getMediaStackRetrofitApi();

        Call<ArrayList<Article>> call = mediaStackApi.getArticle(Constants.MEDIA_STACK_API_KEY);
        call.enqueue(new Callback<ArrayList<Article>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<Article>> call, @NonNull Response<ArrayList<Article>> response) {
                articlesList.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<Article>> call, @NonNull Throwable t) {

            }
        });
    }
}