package shirtish.worldnews;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.Objects;

import shirtish.worldnews.adapters.CategoryAdapter;
import shirtish.worldnews.firebase.FirebaseAuthenticate;
import shirtish.worldnews.firebase.FirebaseFavorites;
import shirtish.worldnews.models.Category;
import shirtish.worldnews.viewmodels.ArticlesViewModel;
import shirtish.worldnews.viewmodels.CategoriesViewModel;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuthenticate firebaseAuthenticate;
    public static FirebaseFavorites firebaseFavorites;

    public static ArticlesViewModel articlesViewModel;
    private CategoriesViewModel categoriesViewModel;

    private TextView loginLogoutTextView;
    private TextView noLikedArticleTextView;
    private RecyclerView categoriesRecycleView;

    private boolean isFavoritesViewMode = false;

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

            String TAG = "MainActivity";
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.e(TAG, "account: " + account.getIdToken());

                firebaseAuthWithGoogle(account.getIdToken());

            } catch (ApiException e) {
                Log.e(TAG, e.getStatus().toString());
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
        firebaseAuthenticate = new FirebaseAuthenticate(this, this);
        firebaseFavorites = new FirebaseFavorites();

        articlesViewModel = new ViewModelProvider(this).get(ArticlesViewModel.class);
        categoriesViewModel = new ViewModelProvider(this).get(CategoriesViewModel.class);

        noLikedArticleTextView = findViewById(R.id.no_liked_article_text_view);

        setLoginTextView();
        setFavoriteTextView();

        if (firebaseAuthenticate.isLoggedIn()) {
            loginLogoutTextView.setText(R.string.logout);
        } else {
            loginLogoutTextView.setText(R.string.login);
        }

        setCategoriesRecyclerView();
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
                if (isFavoritesViewMode){
                    favoriteTextView.setText(R.string.favorites);
                    isFavoritesViewMode = false;
                    noLikedArticleTextView.setVisibility(View.GONE);
                    categoriesRecycleView.setVisibility(View.VISIBLE);
                    articlesViewModel.loadArticles();
                }else{
                    favoriteTextView.setText(R.string.back_to_list);
                    isFavoritesViewMode = true;
                    categoriesRecycleView.setVisibility(View.GONE);
                    showAllFavorites();
                }
            } else {
                showUserThatNeedsToLogin();
            }
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

    private void showAllFavorites() {
        firebaseFavorites.getAllFavoriteArticles();
        if(articlesViewModel.getArticleListSize() > 0){
            noLikedArticleTextView.setVisibility(View.GONE);
        } else {
            noLikedArticleTextView.setVisibility(View.VISIBLE);
        }
    }

    private void showUserThatNeedsToLogin() {
        Toast.makeText(this, R.string.login_required, Toast.LENGTH_SHORT).show();
    }

}