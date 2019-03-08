package helpers.api.harbor.model;

/**
 * @author jtremeaux
 */
public class HarborUser {
    public Integer id;

    public String username;

    public HarborUser(Integer id, String username) {
        this.id = id;
        this.username = username;
    }

    public Integer getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }
}
