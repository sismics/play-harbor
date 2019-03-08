package helpers.api.harbor;

import com.sismics.sapparot.function.CheckedConsumer;
import com.sismics.sapparot.function.CheckedFunction;
import com.sismics.sapparot.okhttp.OkHttpHelper;
import helpers.api.harbor.service.LoginHarborService;
import helpers.api.harbor.service.ProjectHarborService;
import helpers.api.harbor.service.UserHarborService;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import play.Play;
import play.libs.Codec;

import static org.mockito.Mockito.mock;

/**
 * @author jtremeaux
 */
public class HarborClient {
    private OkHttpClient client;

    private static HarborClient harborClient;

    private LoginHarborService loginService;

    private ProjectHarborService projectService;

    private UserHarborService userService;

    public static HarborClient get() {
        if (harborClient == null) {
            harborClient = new HarborClient();
        }
        return harborClient;
    }

    public HarborClient() {
        client = createClient();
        if (isMock()) {
            loginService = mock(LoginHarborService.class);
            projectService = mock(ProjectHarborService.class);
            userService = mock(UserHarborService.class);
        } else {
            loginService = new LoginHarborService(this);
            projectService = new ProjectHarborService(this);
            userService = new UserHarborService(this);
        }
    }

    private static OkHttpClient createClient() {
        return new OkHttpClient.Builder()
                .build();
    }

    public String getHarborUrl() {
        return Play.configuration.getProperty("harbor.url") + "/api";
    }

    public String getHarborUser() {
        return Play.configuration.getProperty("harbor.user");
    }

    public String getHarborPassword() {
        return Play.configuration.getProperty("harbor.password");
    }

    public String getAuthorizationHeader() {
        return "Basic " + Codec.encodeBASE64(getHarborUser() + ":" + getHarborPassword());
    }

    public String getUrl(String url) {
        return getHarborUrl() + url;
    }

    public OkHttpClient getClient() {
        return client;
    }

    public LoginHarborService getLoginService() {
        return loginService;
    }

    public ProjectHarborService getProjectService() {
        return projectService;
    }

    public UserHarborService getUserService() {
        return userService;
    }

    private boolean isMock() {
        return Boolean.valueOf(Play.configuration.getProperty("harbor.mock", "false"));
    }

    public <T> T execute(Request request, CheckedFunction<Response, T> onSuccess, CheckedConsumer<Response> onFailure) {
        return OkHttpHelper.execute(getClient(), request, onSuccess, onFailure);
    }
}
