package Service;

/**
 * Created by jontt on 5/23/2017.
 */

import Model.Event;
import Model.Person;
import Model.User;

/**
 * Class containing the information of a database load attempt.
 */
public class LoadRequest {
    User[] users;
    Person[] persons;
    Event[] events;

    public boolean fieldsNotNull(){
        if (users == null || persons == null || events == null){
            return false;
        } else{
            return true;
        }
    }

}
