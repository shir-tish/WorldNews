package shirtish.worldnews.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import shirtish.worldnews.Constants;
import shirtish.worldnews.models.Category;

public class CategoriesViewModel extends ViewModel {
    private MutableLiveData<List<Category>> categoriesList;

    public LiveData<List<Category>> getCategories() {
        if (categoriesList == null) {
            categoriesList = new MutableLiveData<>();
            setCategoriesList();
        }

        return categoriesList;
    }

    private void setCategoriesList() {
        List<Category> categories = new ArrayList<>();

        for (String category : Constants.CATEGORIES) {
            categories.add(new Category(category));
        }

        categoriesList.setValue(categories);
    }

    public String getActiveCategoriesAsStringList() {
        boolean noCategoryIsActive = true;

        StringBuilder categoriesAsStringList = new StringBuilder();

        for (Category category : Objects.requireNonNull(getCategories().getValue())) {
            if (category.isActive()) {
                noCategoryIsActive = false;
                categoriesAsStringList.append(category.getTitle()).append(",");
            }
        }

        if (noCategoryIsActive) {
            for (Category category : getCategories().getValue()) {
                categoriesAsStringList.append(category.getTitle()).append(",");
            }
        }

        categoriesAsStringList.deleteCharAt(categoriesAsStringList.length() - 1);

        return categoriesAsStringList.toString();
    }
}
