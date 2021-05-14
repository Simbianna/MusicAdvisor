package task4.advisor;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


public class JsonProcessor {

    public static String parseAccessToken(String json) {
        JsonObject o = JsonParser.parseString(json).getAsJsonObject();
        return o.get("access_token").getAsString();
    }

    public static String parseRefreshToken(String json) {
        JsonObject o = JsonParser.parseString(json).getAsJsonObject();
        return o.get("refresh_token").getAsString();
    }

    public static String parseFeatured(String json) {
        JsonObject o = JsonParser.parseString(json).getAsJsonObject();
        JsonArray array = o.get("playlists").getAsJsonObject().get("items").getAsJsonArray();
        StringBuilder sb = new StringBuilder();

        JsonObject o1;
        for (JsonElement e : array) {
            o1 = e.getAsJsonObject();
            sb.append(o1.get("name").getAsString())
                    .append("\n")
                    .append(o1.get("external_urls").getAsJsonObject().get("spotify").getAsString())
                    .append("\n\n");
        }
        return sb.toString();
    }

    public static String parseNewAlbums(String json) {
        JsonObject o = JsonParser.parseString(json).getAsJsonObject();
        JsonArray array = o.get("albums").getAsJsonObject().get("items").getAsJsonArray();
        StringBuilder sb = new StringBuilder();

        for (JsonElement e : array) {
            JsonObject o1 = e.getAsJsonObject();
            sb.append(o1.get("name").getAsString())
                    .append("\n")
                    .append("[");

            JsonArray artists = o1.getAsJsonArray("artists");
            for (JsonElement artist : artists
            ) {
                sb.append(artist.getAsJsonObject().get("name").getAsString())
                        .append(", ");
            }
            sb.delete(sb.length() - 2, sb.length())
                    .append("]")
                    .append("\n")
                    .append(o1.get("external_urls").getAsJsonObject().get("spotify").getAsString())
                    .append("\n\n");
        }
        return sb.toString();
    }

    public static String parseCategories(String json) {
        JsonObject o = JsonParser.parseString(json).getAsJsonObject();
        JsonArray array = o.get("categories").getAsJsonObject().get("items").getAsJsonArray();
        StringBuilder sb = new StringBuilder();

        for (JsonElement e : array) {
            JsonObject o1 = e.getAsJsonObject();

            sb.append(o1.get("name").getAsString())
                    .append("\n");
        }
        return sb.toString();
    }

    public static String parsePlayListsByCategory(String json) {
        JsonObject o = JsonParser.parseString(json).getAsJsonObject();

        if (o.get("playlists") != null) {
            JsonObject oo = o.get("playlists").getAsJsonObject();
            JsonArray array = oo.get("items").getAsJsonArray();
            StringBuilder sb = new StringBuilder();

            for (JsonElement e : array) {
                JsonObject o1 = e.getAsJsonObject();
                sb.append(o1.get("name").getAsString())
                        .append("\n")
                        .append(o1.get("external_urls").getAsJsonObject().get("spotify").getAsString())
                        .append("\n\n");
            }
            return sb.toString();
        }
        else return o.toString();
    }

    public static String getCategoryId(String json, String category) {
        String result = null;
        JsonObject o = JsonParser.parseString(json).getAsJsonObject();
        JsonArray array = o.get("categories").getAsJsonObject().get("items").getAsJsonArray();

        for (JsonElement e : array) {
            JsonObject o1 = e.getAsJsonObject();
            System.out.println(o1.get("name").getAsString());
            if (o1.get("name").getAsString().equals(category)) {
                System.out.println("found");
                result = o1.get("id").getAsString();
                System.out.println(result);
            }
        }
        return result;
    }

    public static void printErrorMessage(String json) {
        JsonObject o = JsonParser.parseString(json).getAsJsonObject();
        System.out.println("Error: " + o.get("error_description"));
    }
}
