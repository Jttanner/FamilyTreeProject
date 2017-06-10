package Service;

import java.util.List;

import Model.Person;

/**
 * Created by jontt on 5/23/2017.
 */

/**
 * Class containing information regarding the result of a PersonService operation.
 */
public class PersonResult {
    Person[] persons;
    Person person;

    public Person[] getPersons(){
        return persons;
    }

    public void initPersonsArray(int arraySize, List<Person> personsList){
        persons = new Person[arraySize];
        for (int i = 0; i < arraySize; ++i){
            persons[i] = personsList.get(i);
            persons[i].setEventList(null);
        }
    }

    public void initSinglePerson(Person person){
        this.person = person;
        this.person.setEventList(null);
    }
}
