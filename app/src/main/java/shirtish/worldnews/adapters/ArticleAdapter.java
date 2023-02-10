package shirtish.worldnews.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import shirtish.worldnews.R;
import shirtish.worldnews.models.Article;

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ArticleViewHolder> {

    private final Context context;
    private ArticleAdapterListener articleAdapterListener;
    private final List<Article> articleList;

    public interface ArticleAdapterListener {
        void onItemClicked(int position);

        void onFavoriteBtnClicked(int position);
    }

    public void setArticleAdapterListener(ArticleAdapterListener articleAdapterListener) {
        this.articleAdapterListener = articleAdapterListener;
    }

    public ArticleAdapter(Context context, List<Article> articleList) {
        this.context = context;
        this.articleList = articleList;
    }

    @NonNull
    @Override
    public ArticleAdapter.ArticleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.article_card, parent, false);
        return new ArticleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ArticleAdapter.ArticleViewHolder holder, int position) {
        Article article = articleList.get(position);

        holder.title.setText(article.getTitle());
        holder.title.setText(article.getPublishedDate());
        Glide.with(context).load(article.getImage()).into(holder.image);

        holder.itemView.setOnClickListener(view -> articleAdapterListener.onItemClicked(position));

        holder.favoriteBtn.setOnClickListener(view -> articleAdapterListener.onFavoriteBtnClicked(position));
    }

    @Override
    public int getItemCount() {
        return articleList.size();
    }

    public static class ArticleViewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        TextView title;
        TextView publishDate;
        ImageView favoriteBtn;

        public ArticleViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.article_image);
            title = itemView.findViewById(R.id.article_title);
            publishDate = itemView.findViewById(R.id.article_publish_date);
            favoriteBtn = itemView.findViewById(R.id.article_favorite);
        }
    }
}
