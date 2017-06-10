package Service;

/**
 * Created by jontt on 5/23/2017.
 */

import java.util.List;
import java.util.Set;

import DataAccess.*;
import Model.*;

/**
 * Service using Data Access Objects for accessing Person information in the database.
 */
public class PersonService {
    DataAccess myDAO = new DataAccess();

    /**
     * Gets all of the Person objects which are considered family to the current user.
     *
     * @return the object containing an array of the requested Person objects
     */
    public PersonResult getAllFamilyMembers(String authToken){
        PersonDao myPersonDao = myDAO.getPersonDataAccess();
        AuthTokenDao myAuthTokenDao = myDAO.getAuthTokenDataAccess();
        String userName = myAuthTokenDao.queryUserName(authToken);
        if (userName == null) {
            myDAO.closeConnection();
            return null;
        }
        List<Person> myFamily = myPersonDao.queryFamily(userName);
        if (myFamily == null){
            myDAO.closeConnection();
            return null;
        }
        PersonResult myPersonResult = new PersonResult();
        myPersonResult.initPersonsArray(myFamily.size(), myFamily);
        myDAO.closeConnection();
        return myPersonResult;
    }


    /**
     * Gets a Person's information linked to a particular PersonID
     *
     * @param personID The request containing the PersonID of the person to look up
     * @return The object containing the Person object requested
     */
    public PersonResult getPerson(String personID, String authToken){
        PersonDao myPersonDao = myDAO.getPersonDataAccess();
        AuthTokenDao myAuthTokenDao = myDAO.getAuthTokenDataAccess();
        String descendant = myPersonDao.query(personID).getDecendant();
        String tokensUserName = myAuthTokenDao.queryUserName(authToken);
        if (tokensUserName == null){
            myDAO.closeConnection();
            return null;
        }
        if (!tokensUserName.equals(descendant)){
            myDAO.closeConnection();
            return null;
        }
        Person foundPerson = myPersonDao.query(personID);
        if (foundPerson == null){
            myDAO.closeConnection();
            return null;
        }
        PersonResult myPersonResult = new PersonResult();
        myPersonResult.initSinglePerson(foundPerson);
        myDAO.closeConnection();
        return myPersonResult;
    }
}
