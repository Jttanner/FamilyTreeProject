package Service;

/**
 * Created by jontt on 5/23/2017.
 */

import java.util.List;

import DataAccess.*;
import Model.*;

/**
 * Class that utilizes EventDao to interact with Event information in the database.
 */
public class EventService {
    DataAccess myDAO = new DataAccess();

    /**
     * Get information on a particular event connected to an EventID.
     *
     * @param eventID The event ID regarding the event requested.
     * @return The Event object containing all of the information regarding the event.
     */
    public EventResult getEvent(String eventID, String authToken){
        EventDao myEventDao = myDAO.getEventDataAccess();
        AuthTokenDao myAuthTokenDao = myDAO.getAuthTokenDataAccess();
        String authUserName = myAuthTokenDao.queryUserName(authToken);
        if (authUserName == null){
            myDAO.closeConnection();
            return null;
        }
        Event foundEvent = myEventDao.query(eventID);
        if (foundEvent == null){
            myDAO.closeConnection();
            return null;
        }
        if(foundEvent.getDescendant().equals(authUserName)){
            myDAO.closeConnection();;
            return null;
        }
        EventResult myEventResult = new EventResult();
        myEventResult.initSingleEvent(foundEvent);
        myDAO.closeConnection();
        return myEventResult;
    }

    /**
     * Gets all events associated all family members of the current users.
     *
     * @return Object containing an array of Event objects containing the requested event information.
     */
    public EventResult getAllEvents(String authToken) {
        EventDao myEventDao = myDAO.getEventDataAccess();
        AuthTokenDao myAuthTokenDao = myDAO.getAuthTokenDataAccess();
        String userName = myAuthTokenDao.queryUserName(authToken);
        if (userName == null) {
            myDAO.closeConnection();
            return null;
        }
        List<Event> myEvents = myEventDao.queryAllEvents(userName);
        if (myEvents == null){
            myDAO.closeConnection();
            return null;
        }
        EventResult myEventResult = new EventResult();
        myEventResult.initEventsArray(myEvents.size(), myEvents);
        myDAO.closeConnection();
        return myEventResult;
    }
}
