package shirtish.worldnews.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import shirtish.worldnews.R;
import shirtish.worldnews.models.Category;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {
    private final Context context;
    private CategoryAdapterListener categoryAdapterListener;
    private final List<Category> categoryList;

    public interface CategoryAdapterListener {
        void onCategoryClicked(int position);
    }

    public void setCategoryAdapterListener(CategoryAdapterListener categoryAdapterListener) {
        this.categoryAdapterListener = categoryAdapterListener;
    }

    public CategoryAdapter(Context context, List<Category> categoryList) {
        this.context = context;
        this.categoryList = categoryList;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.category_card, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.CategoryViewHolder holder, int position) {
        Category category = categoryList.get(holder.getAdapterPosition());
        changeCategoryColorAccordingToItsIsActive(category, holder);

        holder.textView.setText(category.getTitle());
        holder.layout.setOnClickListener(view -> {
            categoryAdapterListener.onCategoryClicked(holder.getAdapterPosition());
            changeCategoryColorAccordingToItsIsActive(category, holder);
        });
    }

    private void changeCategoryColorAccordingToItsIsActive(Category category, @NonNull CategoryViewHolder holder) {
        if (category.isActive()) {
            holder.layout.setBackground(ContextCompat.getDrawable(context, R.drawable.basic_red_background));
            holder.textView.setTextColor(ContextCompat.getColor(context, R.color.white));
        } else {
            holder.layout.setBackground(ContextCompat.getDrawable(context, R.drawable.basic_white_background));
            holder.textView.setTextColor(ContextCompat.getColor(context, R.color.black));
        }
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public static class CategoryViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout layout;
        TextView textView;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            layout = itemView.findViewById(R.id.category_layout);
            textView = itemView.findViewById(R.id.category_text_view);
        }
    }
}
