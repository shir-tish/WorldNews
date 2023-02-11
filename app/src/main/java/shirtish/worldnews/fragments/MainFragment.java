package shirtish.worldnews.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Objects;

import shirtish.worldnews.Constants;
import shirtish.worldnews.R;
import shirtish.worldnews.adapters.ArticleAdapter;
import shirtish.worldnews.adapters.CategoryAdapter;
import shirtish.worldnews.models.Article;
import shirtish.worldnews.models.Category;
import shirtish.worldnews.viewmodels.ArticlesViewModel;
import shirtish.worldnews.viewmodels.CategoriesViewModel;

public class MainFragment extends Fragment {

    private ArticleAdapter articleAdapter;

    public ArticlesViewModel articlesViewModel;
    public CategoriesViewModel categoriesViewModel;

    /*visuals*/
    private SearchView searchView;
    private RecyclerView articlesRecyclerView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        articlesViewModel = new ViewModelProvider(this).get(ArticlesViewModel.class);
        categoriesViewModel = new ViewModelProvider(this).get(CategoriesViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_fragment, container, false);
        init(view);
        return view;
    }

    private void init(View view) {
        TextView noLikedArticleTextView = view.findViewById(R.id.no_liked_article_text_view);
        noLikedArticleTextView.setVisibility(View.GONE);

        //TODO: search view

        setCategoriesRecyclerView(view);
        setArticlesRecyclerView(view);
        observeChangesOfArticleList();

        setFavoriteTextViewAndSetItsOnClickListener(view);

    }

    private void setCategoriesRecyclerView(View view) {
        RecyclerView categoriesRecycleView = view.findViewById(R.id.categories_recycle_view);
        categoriesRecycleView.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        categoriesRecycleView.setLayoutManager(linearLayoutManager);

        categoriesViewModel.getCategories().observe(getViewLifecycleOwner(), new Observer<List<Category>>() {
            @Override
            public void onChanged(List<Category> categories) {
                CategoryAdapter categoryAdapter = new CategoryAdapter(getContext(), categories);
                categoriesRecycleView.setAdapter(categoryAdapter);
                categoryAdapter.setCategoryAdapterListener(new CategoryAdapter.CategoryAdapterListener() {
                    @Override
                    public void onCategoryClicked(int position) {
                        Category category = Objects.requireNonNull(categoriesViewModel.getCategories().getValue()).get(position);
                        category.setActive(!category.isActive());

                        articlesViewModel.loadArticlesWithCategories(categoriesViewModel);
                    }
                });
            }
        });

    }

    private void setArticlesRecyclerView(View view) {
        articlesRecyclerView = view.findViewById(R.id.articles_recycle_view);
        articlesRecyclerView.setHasFixedSize(true);
        articlesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void observeChangesOfArticleList() {
        articlesViewModel.getArticlesList().observe(getViewLifecycleOwner(), new Observer<List<Article>>() {
            @Override
            public void onChanged(List<Article> articleList) {
                articleAdapter = new ArticleAdapter(getContext(), articleList);
                articlesRecyclerView.setAdapter(articleAdapter);
                articleAdapter.setArticleAdapterListener(new ArticleAdapter.ArticleAdapterListener() {
                    @Override
                    public void onArticleClicked(int position) {
                        Bundle bundle = new Bundle();
                        bundle.putInt(Constants.BUNDLE_KEY_POSITION, position);
                        Navigation.findNavController(articlesRecyclerView).navigate(R.id.action_mainFragment_to_articleFragment, bundle);
                    }

                    @Override
                    public void onFavoriteBtnClicked(int position) {
                        //TODO: add/remove position to/from favorites if login else offer to login
                    }
                });
            }
        });
    }

    private void setFavoriteTextViewAndSetItsOnClickListener(View view) {
        TextView favoritesTextView = view.findViewById(R.id.favorites);
        favoritesTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: if login then show all favorites article. else offer to login
            }
        });
    }
}