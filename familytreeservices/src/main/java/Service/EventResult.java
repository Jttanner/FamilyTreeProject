package Service;

import java.util.List;

import Model.Event;


/**
 * Created by jontt on 5/23/2017.
 */

/**
 * Class contianing the information regarding the result of an Event Service operation.
 */
public class EventResult {
    Event event;
    Event[] events;

    public Event[] getEvents(){
        return events;
    }

    public void initSingleEvent(Event event){
        this.event = event;
    }

    public void initEventsArray(int arraySize, List<Event> eventList){
        events = new Event[arraySize];
        for (int i = 0; i < arraySize; ++i){
            events[i] = eventList.get(i);
        }
    }

}
