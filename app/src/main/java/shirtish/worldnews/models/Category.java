package shirtish.worldnews.models;

public class Category {
    private final String title;
    private boolean isActive;

    public Category(String title) {
        this.title = title;
        isActive = false;
    }

    public String getTitle() {
        return title;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
