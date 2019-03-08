package helpers.api.harbor.service;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import helpers.api.harbor.HarborClient;
import okhttp3.Request;

/**
 * @author jtremeaux
 */
public class UserHarborService {
    public HarborClient harborClient;

    public UserHarborService(HarborClient harborClient) {
        this.harborClient = harborClient;
    }

    /**
     * Find a user by username.
     *
     * @param userName The User name
     * @return The user ID or null
     */
    public Integer findUserByUsername(String userName) {
        Request request = new Request.Builder()
                .url(harborClient.getUrl("/users/?username=" + userName))
                .header("Authorization", harborClient.getAuthorizationHeader())
                .get()
                .build();
        return harborClient.execute(request,
                (response) -> {
                    JsonArray users = new JsonParser().parse(response.body().string()).getAsJsonArray();
                    for (JsonElement user : users) {
                        Integer userId = user.getAsJsonObject().get("user_id").getAsInt();
                        String name = user.getAsJsonObject().get("username").getAsString();
                        if (userName.equals(name)) {
                            return userId;
                        }
                    }
                    return null;
                },
                (response) -> {
                    throw new RuntimeException("Error searching user: " + userName + ", response was: " + response.body().string());
                });
    }
}
