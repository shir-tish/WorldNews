package shirtish.worldnews.adapters;

import static shirtish.worldnews.MainActivity.favoritesArticlesKeysUrlsMap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;
import java.util.Map;

import shirtish.worldnews.R;
import shirtish.worldnews.firebase.FirebaseFavorites;
import shirtish.worldnews.models.Article;

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ArticleViewHolder> {
    private final Context context;
    private final List<Article> articleList;
    private final FirebaseFavorites firebaseFavorites;

    public ArticleAdapter(Context context, List<Article> articleList, FirebaseFavorites firebaseFavorites) {
        this.context = context;
        this.articleList = articleList;
        this.firebaseFavorites = firebaseFavorites;
    }

    @NonNull
    @Override
    public ArticleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.article_card, parent, false);
        return new ArticleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ArticleAdapter.ArticleViewHolder holder, int position) {
        Article article = articleList.get(holder.getAdapterPosition());

        holder.title.setText(article.getTitle());
        holder.category.setText(article.getCategory());
        holder.publishDate.setText(article.getPublishedDate());

        if (article.getImage() != null) {
            Glide.with(context).load(article.getImage()).into(holder.image);
            holder.noImageTextView.setVisibility(View.GONE);
        } else {
            holder.noImageTextView.setVisibility(View.VISIBLE);
        }

        boolean isFavorite = false;
        for (Map.Entry<String, String> entry : favoritesArticlesKeysUrlsMap.entrySet()) {
            if (entry.getValue().equals(article.getUrl())) {
                holder.favoriteImageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_mark));
                isFavorite = true;
            }
        }

        if (!isFavorite) {
            holder.favoriteImageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_mark_border));
        }

        holder.favoriteImageView.setOnClickListener(view -> {
            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
            if (firebaseAuth.getCurrentUser() != null) {
                if (favoritesArticlesKeysUrlsMap.isEmpty()) {
                    turnToFavorite(article, holder.favoriteImageView);
                } else {
                    handleOnClickArticleIfMapIsNotEmpty(article, holder.favoriteImageView);
                }
            } else {
                Toast.makeText(context, context.getString(R.string.login_required), Toast.LENGTH_SHORT).show();
            }

        });
    }

    private void handleOnClickArticleIfMapIsNotEmpty(Article article, ImageView imageView) {
        boolean isAlreadyFavorite = false;
        for (Map.Entry<String, String> entry : favoritesArticlesKeysUrlsMap.entrySet()) {
            if (entry.getValue().equals(article.getUrl())) {
                isAlreadyFavorite = true;
                imageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_mark_border));
                firebaseFavorites.deleteArticleFromFavorites(entry.getKey());
                favoritesArticlesKeysUrlsMap.remove(entry.getKey());
                break;
            }
        }

        if (!isAlreadyFavorite) {
            turnToFavorite(article, imageView);
        }
    }

    private void turnToFavorite(Article article, ImageView imageView) {
        imageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_mark));
        String key = firebaseFavorites.addArticleToFavoritesListStorage(article);
        favoritesArticlesKeysUrlsMap.put(key, article.getUrl());
    }

    @Override
    public int getItemCount() {
        return articleList.size();
    }

    public static class ArticleViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView title;
        TextView category;
        TextView publishDate;
        TextView noImageTextView;
        ImageView favoriteImageView;

        public ArticleViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.article_image);
            title = itemView.findViewById(R.id.article_title);
            category = itemView.findViewById(R.id.article_category);
            publishDate = itemView.findViewById(R.id.article_publish_date);
            noImageTextView = itemView.findViewById(R.id.no_image_text_view);
            favoriteImageView = itemView.findViewById(R.id.article_favorite);
        }
    }
}
