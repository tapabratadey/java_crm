package model;

/**
 * User Class
 * User class holds:
 * 1. User Id
 * 2. User Name
 * 3. User Password
 * 4. Setter Functions
 * 5. Getter Functions
 */

public class User {
    private int userId;
    private String userName;
    private String userPassword;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }
}
