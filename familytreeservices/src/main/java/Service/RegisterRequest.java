package Service;

import java.util.UUID;

import Model.User;

/**
 * Created by jontt on 5/23/2017.
 */

/**
 * Class containing the request to create a new user with the newUser's data.
 */
public class RegisterRequest {
    String userName;
    String password;
    String email;
    String firstName;
    String lastName;
    String gender;

    public RegisterRequest(String userName, String password, String email, String firstName, String lastName, String gender){
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
    }

    /**
     * Checks if any of the fields of User are null
     *
     * @return Returns false if any field is null, true if none are null.
     */
    public boolean requestFieldsNotNull(){
        if(firstName == null ||
                lastName == null ||
                email == null ||
                gender == null ||
                userName == null ||
                password == null ){
            return false;
        } else{
            return true;
        }
    }

    public String getUserName(){
        return userName;
    }

    public User generateUserFromRequest(String personID){
        User newUser = new User(userName, password, email, firstName, lastName, gender, personID);
        return newUser;
    }
}
