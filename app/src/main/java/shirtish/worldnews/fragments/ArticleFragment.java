package shirtish.worldnews.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;

import java.util.List;

import shirtish.worldnews.Constants;
import shirtish.worldnews.R;
import shirtish.worldnews.models.Article;
import shirtish.worldnews.viewmodels.ArticlesViewModel;

public class ArticleFragment extends Fragment {

    private int articlePosition = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.article_fragment, container, false);
        init(view);
        return view;
    }

    private void init(View view) {
        checkWhichArticleWasClickedOn();
        provideArticleViewModelAndObserveIt(view);
    }

    private void checkWhichArticleWasClickedOn() {
        if (getArguments() != null) {
            articlePosition = getArguments().getInt(Constants.BUNDLE_KEY_POSITION);
        }
    }

    private void provideArticleViewModelAndObserveIt(View view) {
        ArticlesViewModel articlesViewModel = new ViewModelProvider(this).get(ArticlesViewModel.class);
        articlesViewModel.getArticlesList().observe(getViewLifecycleOwner(), new Observer<List<Article>>() {
            @Override
            public void onChanged(List<Article> articleList) {
                setFragmentElements(view, articleList.get(articlePosition));
            }
        });
    }

    private void setFragmentElements(View view, Article article) {
        TextView articleTitle = view.findViewById(R.id.article_title);
        TextView articleDescription = view.findViewById(R.id.article_description);
        ImageView articleImage = view.findViewById(R.id.article_image);

        articleTitle.setText(article.getTitle());
        articleDescription.setText(article.getDescription());

        Glide.with(getContext()).load(article.getImage()).into(articleImage);
    }
}