package Service;

/**
 * Created by jontt on 5/23/2017.
 */

/**
 * Class containing information regarding a registartions success and a new User's data.
 */
public class RegisterResult {
    String AuthToken;
    String userName;
    String personID;

    public String getAuthToken(){
        return AuthToken;
    }

    RegisterResult(String AuthToken, String userName, String personID){
        this.AuthToken = AuthToken;
        this.userName = userName;
        this.personID = personID;
    }

}
