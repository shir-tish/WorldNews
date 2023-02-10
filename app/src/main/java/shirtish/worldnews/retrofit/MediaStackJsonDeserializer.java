package shirtish.worldnews.retrofit;

import android.util.Log;

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
    private static final String TAG = "MediaStackJsonDeserializer";

    @Override
    public Object deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        ArrayList<Article> articleArrayList = null;

        try {
            JsonObject jsonObject = json.getAsJsonObject();
            JsonArray jsonArray = jsonObject.getAsJsonArray(Constants.MEDIA_STACK_API_MEMBER_NAME);
            articleArrayList = new ArrayList<>(jsonArray.size());

            for (int i = 0; i < jsonArray.size(); i++) {
                articleArrayList.add(context.deserialize(jsonArray.get(i), Article.class));
            }
        } catch (JsonParseException e) {
            //TODO: check which error and show it to user
            //TODO: report error to FirebaseCrashlytics
            Log.e(TAG, String.format("Could not dematerialize element: %s", json.toString()));
        }
        return articleArrayList;
    }
}
