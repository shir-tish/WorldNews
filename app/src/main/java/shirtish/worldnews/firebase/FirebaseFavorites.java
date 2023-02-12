package shirtish.worldnews.firebase;

import static shirtish.worldnews.MainActivity.favoritesArticlesKeysUrlsMap;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import shirtish.worldnews.models.Article;
import shirtish.worldnews.viewmodels.ArticlesViewModel;

public class FirebaseFavorites {

    private final DatabaseReference reference;
    private final ArticlesViewModel articlesViewModel;

    public FirebaseFavorites(ArticlesViewModel articlesViewModel) {
        this.articlesViewModel = articlesViewModel;
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        reference = database.getReference();
    }

    public void deleteArticleFromFavorites(String key) {
        reference.child(key).removeValue();
    }

    public String addArticleToFavoritesListStorage(Article article) {
        String key = reference.push().getKey();
        if (key != null) {
            reference.child(key).setValue(article);
        }
        return key;
    }

    public void getAllFavoriteArticles() {
        List<Article> favoriteArticles = new ArrayList<>();
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    if (dataSnapshot.exists()) {
                        Article article = dataSnapshot.getValue(Article.class);
                        favoriteArticles.add(article);
                    }
                }
                articlesViewModel.setArticlesList(favoriteArticles);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void addFavoriteToMap() {
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    if (dataSnapshot.exists()) {
                        Article article = dataSnapshot.getValue(Article.class);
                        if (article != null) {
                            favoritesArticlesKeysUrlsMap.put(dataSnapshot.getKey(), article.getUrl());
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}
