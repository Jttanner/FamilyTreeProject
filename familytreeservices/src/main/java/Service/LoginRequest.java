package Service;

/**
 * Created by jontt on 5/23/2017.
 */

/**
 * Class containing the information regarding a User's login attempt.
 */
public class LoginRequest {
    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    String userName;
    String password;

    public LoginRequest(String userName, String password){
        this.userName = userName;
        this.password = password;
    }

}
