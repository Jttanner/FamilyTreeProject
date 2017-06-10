package Service;

/**
 * Created by jontt on 5/23/2017.
 */

import java.util.List;

import Model.Event;
import Model.Person;

/**
 * Class containing information regarding an attempted fill
 */
public class FillRequest {
    String userName;
    int generations;
    List<Person> fillFamily;

    public FillRequest(String userName, int generations, List<Person> fillFamily){
        this.userName = userName;
        this.generations = generations;
        this.fillFamily = fillFamily;
    }
}
