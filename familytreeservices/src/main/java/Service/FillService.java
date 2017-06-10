package Service;

/**
 * Created by jontt on 5/20/2017.
 */

import javax.xml.crypto.Data;

import DataAccess.*;
import Model.*;

/**
 * Class used to fill information into the Database
 */
public class FillService {
    DataAccess myDAO;

    /**
     * Creates a DataAccess Object in order to interact with the database
     * DataAccess objects assist in authenticating the user, and accessing the database
     */
    public FillService(){
        myDAO = new DataAccess();
    }

    /**
     * Uses a DataAccess object to attempt to add a person to the database.
     *
     * @param personsToAdd The object containing the peeople to add's information.
     * @return The result of the success of the operation.
     */
    public FillResult fill(FillRequest personsToAdd, int people){
        if (personsToAdd.fillFamily == null) return null;
        int events = 0;
        PersonDao myPersonDao = myDAO.getPersonDataAccess();
        EventDao myEventDao = myDAO.getEventDataAccess();
        //the last one added to this set is always the user
        String descendant = personsToAdd.fillFamily.get(personsToAdd.fillFamily.size() - 1).getDecendant();
        //clear all previous ancestors associated with the user
        myPersonDao.clearAllForOneUser(descendant);
        myEventDao.clearAllForOneUser(descendant);
        boolean success = true;
        for(Person p : personsToAdd.fillFamily){
            myPersonDao.insert(p);
            for (Event e : p.getEventList()){
                myEventDao.insert(e);
                ++events;
            }
            //insert, then query to see if it was successfully inserted
            if (myPersonDao.query(p.getPerson_ID()) == null) success = false;
        }
        if (!success){
            myDAO.closeConnection();
            return null;
        } else{
            String formattedString = String.format("Successfully added %s persons and %s events to the database", people, events);
            FillResult myFillResult = new FillResult();
            myFillResult.setMessage(formattedString);
            myDAO.closeConnection();
            return myFillResult;
        }
    }

    public User getUser(String userName){
        return myDAO.getUserDataAccess().getUser(userName);
    }

}
