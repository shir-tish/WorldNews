package shirtish.worldnews.viewmodels;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import shirtish.worldnews.Constants;
import shirtish.worldnews.firebase.FirebaseFavorites;
import shirtish.worldnews.models.Article;
import shirtish.worldnews.retrofit.MediaStackApi;
import shirtish.worldnews.retrofit.MediaStackApiUtil;

public class ArticlesViewModel extends ViewModel {
    private static final String TAG = "ArticlesViewModel";

    private MutableLiveData<List<Article>> articlesList;

    public LiveData<List<Article>> getArticlesList() {
        if (articlesList == null) {
            articlesList = new MutableLiveData<>();
            loadArticles();
        }
        return articlesList;
    }

    public void setArticlesList(List<Article> articlesList) {
        this.articlesList.setValue(articlesList);
    }

    public int getArticleListSize(){
        if(articlesList.getValue() != null) {
            return articlesList.getValue().size();
        }
        return 0;
    }

    public void loadArticles() {
        MediaStackApi mediaStackApi = MediaStackApiUtil.getMediaStackRetrofitApi();

        Call<ArrayList<Article>> call = mediaStackApi.getAllArticles(Constants.MEDIA_STACK_API_KEY);
        call.enqueue(new Callback<ArrayList<Article>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<Article>> call, @NonNull Response<ArrayList<Article>> response) {
                if (response.body() != null){
                    articlesList.setValue(response.body());
                    Log.d(TAG, response.body().toString());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<Article>> call, @NonNull Throwable t) {
            }
        });
    }

    public void loadArticlesWithCategories(CategoriesViewModel categoriesViewModel) {
        String categoriesAsStringList = categoriesViewModel.getActiveCategoriesAsStringList();

        MediaStackApi mediaStackApi = MediaStackApiUtil.getMediaStackRetrofitApi();

        Call<ArrayList<Article>> call = mediaStackApi.getArticleByCategory(Constants.MEDIA_STACK_API_KEY, categoriesAsStringList);
        call.enqueue(new Callback<ArrayList<Article>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<Article>> call, @NonNull Response<ArrayList<Article>> response) {
                if (response.body() != null){
                    articlesList.setValue(response.body());
                    Log.d(TAG, response.body().toString());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<Article>> call, @NonNull Throwable t) {
            }
        });
    }

}