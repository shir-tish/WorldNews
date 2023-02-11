package shirtish.worldnews.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
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
        void onArticleClicked(int position);

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

        if(article.getImage() != null) {
            Glide.with(context).load(article.getImage()).into(holder.image);
            holder.noImageTextView.setVisibility(View.GONE);
        } else {
            holder.noImageTextView.setVisibility(View.VISIBLE);
        }

        checkIfFavoriteAndRemoveOrAddToList(holder.favoriteImageView);

        holder.itemView.setOnClickListener(view -> articleAdapterListener.onArticleClicked(holder.getAdapterPosition()));

        holder.favoriteImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                articleAdapterListener.onFavoriteBtnClicked(holder.getAdapterPosition());

                checkIfFavoriteAndRemoveOrAddToList(holder.favoriteImageView);
            }
        });
    }

    private void checkIfFavoriteAndRemoveOrAddToList(ImageView favoriteImageView){
        //TODO: if article is in favorite list then
        /*if () {
            favoriteImageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_mark));
        } else {
            favoriteImageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_mark_border));
        }*/
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
