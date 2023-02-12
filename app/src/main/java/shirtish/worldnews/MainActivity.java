package shirtish.worldnews;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.HashMap;
import java.util.Objects;

import shirtish.worldnews.adapters.ArticleAdapter;
import shirtish.worldnews.adapters.CategoryAdapter;
import shirtish.worldnews.firebase.FirebaseAuthenticate;
import shirtish.worldnews.firebase.FirebaseFavorites;
import shirtish.worldnews.models.Category;
import shirtish.worldnews.viewmodels.ArticlesViewModel;
import shirtish.worldnews.viewmodels.CategoriesViewModel;

public class MainActivity extends AppCompatActivity {

    /*Firebase*/
    private FirebaseAuthenticate firebaseAuthenticate;
    private FirebaseFavorites firebaseFavorites;

    /*View Model*/
    private ArticlesViewModel articlesViewModel;
    private CategoriesViewModel categoriesViewModel;

    /*UI elements*/
    private TextView loginLogoutTextView;
    private TextView noLikedArticleTextView;
    private RecyclerView categoriesRecycleView;
    private RecyclerView articlesRecyclerView;

    /*Utils*/
    private ArticleAdapter articleAdapter;

    /*Data*/
    private boolean isFavoritesViewMode = false;
    public static HashMap<String, String> favoritesArticlesKeysUrlsMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constants.REQUEST_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                e.printStackTrace();
            }
        }
    }

    public void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        firebaseAuthenticate.getFirebaseAuth().signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        loginLogoutTextView.setText(R.string.logout);
                    } else {
                        Toast.makeText(this, getText(R.string.authentication_failed), Toast.LENGTH_SHORT).show();
                        loginLogoutTextView.setText(R.string.login);
                    }
                });
    }

    private void init() {
        noLikedArticleTextView = findViewById(R.id.no_liked_article_text_view);

        articlesViewModel = new ViewModelProvider(this).get(ArticlesViewModel.class);
        categoriesViewModel = new ViewModelProvider(this).get(CategoriesViewModel.class);

        firebaseAuthenticate = new FirebaseAuthenticate(this, this);
        firebaseFavorites = new FirebaseFavorites(articlesViewModel);

        setFavoriteHashMap();
        setLoginTextView();
        setFavoriteTextView();
        setArticlesRecyclerView();
        observeChangesOfArticleList();

        if (firebaseAuthenticate.isLoggedIn()) {
            loginLogoutTextView.setText(R.string.logout);
        } else {
            loginLogoutTextView.setText(R.string.login);
        }

        setCategoriesRecyclerView();
    }

    private void setFavoriteHashMap(){
        favoritesArticlesKeysUrlsMap = new HashMap<>();
        firebaseFavorites.addFavoriteToMap();
    }

    private void setLoginTextView() {
        loginLogoutTextView = findViewById(R.id.login_logout_text_view);
        loginLogoutTextView.setOnClickListener(view -> {
            if (!firebaseAuthenticate.isLoggedIn()) {
                firebaseAuthenticate.signIn();
            } else {
                firebaseAuthenticate.signOut();
                loginLogoutTextView.setText(R.string.login);
            }
        });
    }

    private void setFavoriteTextView() {
        TextView favoriteTextView = findViewById(R.id.favorites);

        favoriteTextView.setOnClickListener(view -> {
            if (firebaseAuthenticate.isLoggedIn()) {
                if (isFavoritesViewMode) {
                    favoriteTextView.setText(R.string.favorites);
                    isFavoritesViewMode = false;
                    noLikedArticleTextView.setVisibility(View.GONE);
                    categoriesRecycleView.setVisibility(View.VISIBLE);
                    articlesViewModel.loadArticles();
                } else {
                    favoriteTextView.setText(R.string.back_to_list);
                    isFavoritesViewMode = true;
                    categoriesRecycleView.setVisibility(View.GONE);
                    showAllFavorites();
                }
            } else {
                Toast.makeText(this, R.string.login_required, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showAllFavorites() {
        firebaseFavorites.getAllFavoriteArticles();
        if (articlesViewModel.getArticleListSize() > 0) {
            noLikedArticleTextView.setVisibility(View.GONE);
        } else {
            noLikedArticleTextView.setVisibility(View.VISIBLE);
        }
    }

    private void setArticlesRecyclerView() {
        articlesRecyclerView = findViewById(R.id.articles_recycle_view);
        articlesRecyclerView.setHasFixedSize(true);
        articlesRecyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
    }

    private void observeChangesOfArticleList() {
        articlesViewModel.getArticlesList().observe(this, articleList -> {
            articleAdapter = new ArticleAdapter(MainActivity.this, articleList, firebaseFavorites);
            articlesRecyclerView.setAdapter(articleAdapter);
        });
    }

    private void setCategoriesRecyclerView() {
        categoriesRecycleView = findViewById(R.id.categories_recycle_view);
        categoriesRecycleView.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        categoriesRecycleView.setLayoutManager(linearLayoutManager);

        categoriesViewModel.getCategories().observe(this, categories -> {
            CategoryAdapter categoryAdapter = new CategoryAdapter(this, categories);
            categoriesRecycleView.setAdapter(categoryAdapter);
            categoryAdapter.setCategoryAdapterListener(position -> {
                Category category = Objects.requireNonNull(categoriesViewModel.getCategories().getValue()).get(position);
                category.setActive(!category.isActive());

                articlesViewModel.loadArticlesWithCategories(categoriesViewModel);
            });
        });

    }
}