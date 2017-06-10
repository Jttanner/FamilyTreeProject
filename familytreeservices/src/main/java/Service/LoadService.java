package Service;

/**
 * Created by jontt on 5/23/2017.
 */

import DataAccess.*;
import Model.*;

/**
 * Class used to Clear all data from the database, and then load the posted user, person, and event data into the database
 */
public class LoadService {
    DataAccess myDAO;

    public LoadService(){
        myDAO = new DataAccess();
    }
    /**
     * uses a ClearService object to clear all of the information from the database
     * @return Returns the result of the clear operation.
     */
    public ClearResult useClearAllService(){
        return null;
    }

    /**
     * Uploads all of the passed in users, people, and events to the database.
     *
     * @param load the object containing the requested information for loading this information to the database
     * @return The result of success for the operation.
     */
    public LoadResult uploadAll(LoadRequest load){
        UserDao userDao = myDAO.getUserDataAccess();
        PersonDao personDao = myDAO.getPersonDataAccess();
        EventDao eventDao = myDAO.getEventDataAccess();
        int newUserCount = 0;
        int newPersonCount = 0;
        int newEventCount = 0;
        for (User u : load.users){
            userDao.insert(u);
            if (userDao.query(u.getUsername(), u.getPassword()) == false) return null;
            ++newUserCount;
        }
        for (Person p : load.persons){
            personDao.insert(p);
            if (personDao.query(p.getPerson_ID()) == null) return null;
            ++newPersonCount;
        }
        for (Event e : load.events){
            if(!eventDao.insert(e)) return null;
            ++newEventCount;
        }
        LoadResult result = new LoadResult();
        String resultMessage = String.format("Successfully added %s users, %s persons, and %s events to the database.",
                                              newUserCount, newPersonCount, newEventCount);
        result.setMessage(resultMessage);
        myDAO.closeConnection();
        return result;
    }
}
