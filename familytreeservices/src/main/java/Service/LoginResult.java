package Service;

/**
 * Created by jontt on 5/23/2017.
 */

import java.util.UUID;

/**
 * Class containing information regarding the success of an attempted login.
 */
public class LoginResult {
    String authToken;
    String userName;
    String personID;

    LoginResult (LoginRequest request, String userName, String personID, String authToken){
        this.userName = userName;
        this.personID = personID;
        this.authToken = authToken;

    }

    public String getAuthToken() {
        return authToken;
    }

}
