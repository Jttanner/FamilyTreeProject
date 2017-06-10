package Service;

/**
 * Created by jontt on 5/20/2017.
 */

import java.util.UUID;

import DataAccess.*;
import Handlers.ObjectEncoderDecoder;
import Model.Person;
import Model.User;

/**
 * Class used for Registering a new User
 */
public class RegisterService {
    DataAccess myDAO;
    /**
     * Creates a new object of DataAccess,
     * which uses particular Data Access Objects to communicate with the database
     * DataAccess objects assist in authenticating the user, and accessing the database
     */
    public RegisterService(){
        myDAO = new DataAccess();
    }
    /**
     * Uses the DataAccess to attempt to register a new account.
     *
     * @param request a RegisterRequest object with the registration attempt's information.
     * @return Returns a RegisterResult object with information regarding the registrations success
     */
    public RegisterResult register(RegisterRequest request, String newPersonID){ //check null entries before this function is called
        UserDao myUserDao = myDAO.getUserDataAccess();
        AuthTokenDao myAuthTokenDao = myDAO.getAuthTokenDataAccess();
        PersonDao myPersonDao = myDAO.getPersonDataAccess();
        String userName = request.userName;
        String password = request.password;
        String email = request.email;
        String firstName = request.firstName;
        String lastName = request.lastName;
        String gender = request.gender;
        User newUser = new User(userName, password, email, firstName,
                                lastName, gender, newPersonID);
        //check if they're already there
        if(myUserDao.userExists(userName)){
            myDAO.closeConnection();
            return null;
        }

        //dont need direct access to other fields of user at this point
        myUserDao.insert(newUser);
        //myPersonDao.insert(newPerson);
        //fill in with data

        if (myUserDao.query(userName, password)){
            myAuthTokenDao.insert(userName);
            String authToken = myAuthTokenDao.query(userName);
            RegisterResult finalResult = new RegisterResult(authToken, userName, newPersonID);
            myDAO.closeConnection();
            return finalResult;
        } else {
            myDAO.closeConnection();
            return null;
            //return new LoginResult(false, login, userName, "");
        }
    }

}
