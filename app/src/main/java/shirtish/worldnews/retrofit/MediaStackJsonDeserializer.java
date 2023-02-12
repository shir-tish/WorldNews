package shirtish.worldnews.retrofit;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.ArrayList;

import shirtish.worldnews.Constants;
import shirtish.worldnews.models.Article;

public class MediaStackJsonDeserializer implements JsonDeserializer {

    @Override
    public Object deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        ArrayList<Article> articleArrayList = null;

        try {
            JsonObject jsonObject = json.getAsJsonObject();
            JsonArray jsonArray = jsonObject.getAsJsonArray(Constants.MEDIA_STACK_API_MEMBER_NAME);
            articleArrayList = new ArrayList<>(jsonArray.size());

            for (JsonElement i : jsonArray) {
                articleArrayList.add(context.deserialize(i, Article.class));
            }
        } catch (JsonParseException ignored) {
        }
        return articleArrayList;
    }
}
