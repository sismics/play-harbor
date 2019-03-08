package helpers.api.harbor.service;

import com.google.gson.*;
import helpers.api.harbor.HarborClient;
import helpers.api.harbor.model.HarborUser;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jtremeaux
 */
public class ProjectHarborService {
    public HarborClient harborClient;

    public ProjectHarborService(HarborClient harborClient) {
        this.harborClient = harborClient;
    }

    /**
     * Get the project.
     *
     * @param id The project ID
     * @return Project found
     */
    public boolean getProjectById(Integer id) {
        Request request = new Request.Builder()
                .url(harborClient.getUrl("/projects/" + id))
                .header("Authorization", harborClient.getAuthorizationHeader())
                .get()
                .build();
        return harborClient.execute(request,
                (response) -> Boolean.TRUE,
                null) != null;
    }

    /**
     * Get the project.
     *
     * @param name The project Name
     * @return Project found
     */
    public Integer getProjectByName(String name) {
        Request request = new Request.Builder()
                .url(harborClient.getUrl("/projects/?project_name=" + name))
                .header("Authorization", harborClient.getAuthorizationHeader())
                .get()
                .build();
        return harborClient.execute(request,
                (response) -> {
                    JsonObject json = new JsonParser().parse(response.body().string()).getAsJsonArray().get(0).getAsJsonObject();
                    return json.get("project_id").getAsInt();
                },
                (response) -> {
                    throw new RuntimeException("Error getting project: " + name);
                });
    }

    /**
     * Get the project members.
     *
     * @param projectId The project ID
     * @return List of project members
     */
    public List<HarborUser> getProjectMember(Integer projectId) {
        Request request = new Request.Builder()
                .url(harborClient.getUrl("/projects/" + projectId + "/members"))
                .header("Authorization", harborClient.getAuthorizationHeader())
                .get()
                .build();

        return harborClient.execute(request,
                (response) -> {
                    JsonArray members = new JsonParser().parse(response.body().string()).getAsJsonArray();
                    List<HarborUser> users = new ArrayList<>();
                    for (JsonElement json : members) {
                        JsonObject member = json.getAsJsonObject();
                        Integer userId = member.get("user_id").getAsInt();
                        String username = member.get("username").getAsString();
                        users.add(new HarborUser(userId, username));
                    }
                    return users;
                },
                (response) -> {
                    throw new RuntimeException("Error getting project: " + projectId);
                });
    }

    /**
     * Create a new project.
     *
     * @param name The project name
     */
    public void createProject(String name) {
        JsonObject formBody = new JsonObject();
        formBody.addProperty("project_name", name);
        formBody.addProperty("public", 0);
        Request request = new Request.Builder()
                .url(harborClient.getUrl("/projects"))
                .header("Authorization", harborClient.getAuthorizationHeader())
                .post(RequestBody.create(MediaType.parse("application/json"), new GsonBuilder().create().toJson(formBody)))
                .build();
        harborClient.execute(request,
                null,
                (response) -> {
                    throw new RuntimeException("Error creating project: " + name);
                });
    }

    /**
     * Add a user to a project.
     *
     * @param id The project ID
     * @param username The username
     */
    public void createUser(Integer id, String username) {
        JsonObject formBody = new JsonObject();
        formBody.addProperty("username", username);
        JsonArray roles = new JsonArray();
        roles.add(2);
        formBody.add("roles", roles);
        Request request = new Request.Builder()
                .url(harborClient.getUrl("/projects/" + id + "/members"))
                .header("Authorization", harborClient.getAuthorizationHeader())
                .post(RequestBody.create(MediaType.parse("application/json"), new GsonBuilder().create().toJson(formBody)))
                .build();
        harborClient.execute(request,
                null,
                (response) -> {
            throw new RuntimeException("Error creating adding user to project: " + username + ", response was: " + response.body().string());
        });
    }

    /**
     * Delete a user from a project.
     *
     * @param projectId The project ID
     * @param userId The username
     */
    public void deleteUser(Integer projectId, Integer userId) {
        Request request = new Request.Builder()
                .url(harborClient.getUrl("/projects/" + projectId + "/members/" + userId))
                .header("Authorization", harborClient.getAuthorizationHeader())
                .delete()
                .build();
        harborClient.execute(request,
                null,
                (response) -> {
                    throw new RuntimeException("Error deleting user from project: " + userId + ", response was: " + response.body().string());
                });
    }
}
