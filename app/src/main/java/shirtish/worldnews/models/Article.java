package shirtish.worldnews.models;

import com.google.gson.annotations.SerializedName;

public class Article {
    private String title;
    private String url;
    private String image;
    private String category;

    @SerializedName("published_at")
    private String publishedAt;

    public Article() {
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    public String getImage() {
        return image;
    }

    public String getCategory() {
        return category;
    }

    public String getPublishedDate() {
        if (this.publishedAt != null) {
            String[] dateTime = this.publishedAt.split("T");
            if (dateTime.length > 1) {
                String date = dateTime[0];
                String[] dateArray = date.split("-");
                return dateArray[2] + "/" + dateArray[1] + "/" + dateArray[2] + " " + dateTime[1].substring(0, 5);
            }
        }

        return "";
    }
}
