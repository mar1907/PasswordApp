package model;

/**
 * This class models the accounts
 */
public class Account {

    private String domain;
    private String username;
    private String password;

    /**
     * Constructor
     * @param domain domain (e.g. Facebook, Google, etc.)
     * @param username username
     * @param password password
     */
    public Account(String domain, String username, String password) {
        this.domain = domain;
        this.username = username;
        this.password = password;
    }

    public String getDomain() {
        return domain;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
