package shirtish.worldnews.models;

import com.google.gson.annotations.SerializedName;

public class New{
    private String author;
    private String title;
    private String description;
    private String url;
    private String source;
    private String image;
    private String category;
    private String language;
    private String country;

    @SerializedName("published_at")
    private String publishedAt;

    public New() {
    }
}
