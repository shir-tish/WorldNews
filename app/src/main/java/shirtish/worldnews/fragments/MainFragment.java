package shirtish.worldnews.fragments;

import static shirtish.worldnews.MainActivity.articlesViewModel;
import static shirtish.worldnews.MainActivity.firebaseFavorites;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import shirtish.worldnews.models.Article;

public class MainFragment extends Fragment {

    private ArticleAdapter articleAdapter;
    private RecyclerView articlesRecyclerView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_fragment, container, false);
        init(view);
        return view;
    }

    private void init(View view) {
        setArticlesRecyclerView(view);
        observeChangesOfArticleList();
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
                articleAdapter = new ArticleAdapter(getContext(), articleList, firebaseFavorites);
                articlesRecyclerView.setAdapter(articleAdapter);
                articleAdapter.setArticleAdapterListener(new ArticleAdapter.ArticleAdapterListener() {
                    @Override
                    public void onArticleClicked(int position) {
                        Bundle bundle = new Bundle();
                        bundle.putInt(Constants.BUNDLE_KEY_POSITION, position);
                        Navigation.findNavController(articlesRecyclerView).navigate(R.id.action_mainFragment_to_articleFragment, bundle);
                    }
                });
            }
        });
    }
}