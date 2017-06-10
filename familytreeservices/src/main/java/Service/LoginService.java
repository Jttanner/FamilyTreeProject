package Service;

/**
 * Created by jontt on 5/20/2017.
 */

import DataAccess.*;

/**
 * Class used to log in an existing user.
 */
public class LoginService {

    DataAccess myDAO;

    /**
     * Creates a DataAccess object in order to interact with the database
     * DataAccess objects assist in authenticating the user, and accessing the database
     */
    public LoginService(){
        myDAO = new DataAccess();
    }

    /**
     * Uses a DataAccess object in order to attempt to login a User,
     * Also uses a DataAccess object in order to create an Authentication Token for the User
     *
     * @param login An object containing the login attempt's information
     * @return Returns and object with the information associated with a failed or successful login
     */
    public LoginResult login(LoginRequest login) {
        UserDao myUserDao = myDAO.getUserDataAccess();
        AuthTokenDao myAuthTokenDao = myDAO.getAuthTokenDataAccess();
        String userName = login.getUserName();
        String password = login.getPassword();
        if (myUserDao.query(userName, password) == true){
            myAuthTokenDao.insert(userName);
            LoginResult finalResult = new LoginResult(login, userName, myUserDao.getUserID(userName) , myAuthTokenDao.query(userName));
            myDAO.closeConnection();
            return finalResult;
        } else {
            myDAO.closeConnection();
            return null;
            //return new LoginResult(false, login, userName, "");
        }
    }
}
