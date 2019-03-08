package helpers.api.harbor.service;

import helpers.api.harbor.HarborClient;
import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import play.Play;

/**
 * @author jtremeaux
 */
public class LoginHarborService {
    public HarborClient harborClient;

    public LoginHarborService(HarborClient harborClient) {
        this.harborClient = harborClient;
    }

    /**
     * Login.
     *
     * @param username The username
     * @param password The password
     */
    public void login(String username, String password) {
        RequestBody formBody = new FormBody.Builder()
                .add("principal", username)
                .add("password", password)
                .build();
        Request request = new Request.Builder()
                .url(Play.configuration.getProperty("harbor.url") + "/login")
                .post(formBody)
                .build();
        harborClient.execute(request,
                null,
                (response) -> {
                    throw new RuntimeException("Login error" + ", response was: " + response.body().string());
                });
    }
}
