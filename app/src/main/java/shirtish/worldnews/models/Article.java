package shirtish.worldnews.models;

import com.google.gson.annotations.SerializedName;

public class Article {
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

    public Article() {
    }

    public String getAuthor() {
        return author;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getUrl() {
        return url;
    }

    public String getSource() {
        return source;
    }

    public String getImage() {
        return image;
    }

    public String getCategory() {
        return category;
    }

    public String getLanguage() {
        return language;
    }

    public String getCountry() {
        return country;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public String getPublishedDate() {
        String[] dateTime = this.publishedAt.split("T");
        String date = dateTime[0];

        String[] dateArray = date.split("-");

        return dateArray[2]+"/"+dateArray[1]+"/"+dateArray[2]+ " " + dateTime[1].substring(0, 4);
    }
}
