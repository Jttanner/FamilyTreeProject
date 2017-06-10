package Handlers;

/**
 * Created by jontt on 5/26/2017.
 */

import com.google.gson.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;

import Model.*;
import Service.*;

public class ObjectEncoderDecoder {


    /*
    Two nested classes used for reading data
    from json strings for fake data to use for
    filling Persons and events
     */
    class JsonNamesData{
        String data[];
    }

    class LocationDataContents{
        String country;
        String city;
        String latitude;
        String longitude;
    }

    class JsonLocationsData{
        LocationDataContents data[];

        public LocationDataContents[] getDataContents(){
            return data;
        }
    }

    List<String> maleFirstNames = new ArrayList<>();
    List<String> femaleFirstNames = new ArrayList<>();
    List<String> lastNames = new ArrayList<>();
    List<LocationDataContents> locations = new ArrayList<>();

    int eventsGenerated;
    int peopleGenerated;

    String firstNamesJson;
    String lastNamesJson;
    String maleNamesJson;
    String locationsJson;

    Gson myGson;

    ObjectEncoderDecoder(){
        //initialize Gson
        myGson = new Gson();
        //initialize the lists of data
        initDataLists();
    }

    private void initDataLists(){
        //open all of the json files for the data
        String jsonFilesPath = "webAPItest/data/json/";
        File femaleNamesFile = new File(jsonFilesPath + "fnames.json");
        File maleNamesFile = new File(jsonFilesPath + "mnames.json");
        File lastNamesFile = new File (jsonFilesPath + "snames.json");
        File locationsFile = new File (jsonFilesPath + "locations.json");
        //load all of the files into json strings
        firstNamesJson = fileToString(femaleNamesFile, firstNamesJson);
        lastNamesJson = fileToString(lastNamesFile, lastNamesJson);
        maleNamesJson = fileToString(maleNamesFile, maleNamesJson);
        locationsJson = fileToString(locationsFile, locationsJson);
        JsonLocationsData myLocations = myGson.fromJson(locationsJson, JsonLocationsData.class);
        JsonNamesData myLastNames = myGson.fromJson(lastNamesJson, JsonNamesData.class);
        JsonNamesData myMaleNames = myGson.fromJson(maleNamesJson, JsonNamesData.class);
        JsonNamesData myFemaleNames = myGson.fromJson(firstNamesJson, JsonNamesData.class);
        for (String s : myLastNames.data){
            lastNames.add(s);
        }
        for (String s : myFemaleNames.data){
            femaleFirstNames.add(s);
        }
        for (String s : myMaleNames.data){
            maleFirstNames.add(s);
        }
        for (LocationDataContents d : myLocations.data){
            locations.add(d);
        }
    }

    private String fileToString(File readFile, String outputString){
        try{
            Scanner scanner = new Scanner(readFile);
            outputString = scanner.useDelimiter("\\A").next();
            scanner.close();
            return outputString;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null; //return null if the file wasn't found
        }
    }

    public void resetPeopleGenerated(){
        peopleGenerated = 0;
    }

    public int getPeopleGenerated(){
        return peopleGenerated;
    }


    /**
     * Creates new family tree data for a user.
     *
     * @param fillUser The user to generate data for.
     * @param generations The number of generations to add to the User's family tree
     * @return Returns new family tree data for a user.
     */
    public List<Person> userFamilyTreeModel(User fillUser, int generations){
        if (fillUser == null) return null;
        List<Person> myFamily = new ArrayList<>();
        Person userAsPerson = new Person(fillUser.getPerson_ID(), fillUser.getUsername(), fillUser.getFirst_Name(),
                                         fillUser.getLast_Name(), fillUser.getGender(), null, null, null);
        ++peopleGenerated;
        Random random = new Random();

        int currentIndex = 0;
        for (int i = 0; i < generations; ++i){
            int currentNumberOfPeople = (int) Math.pow(2, (i+1));
            int femaleFirstNameRandomIndex = random.nextInt(femaleFirstNames.size()) + 1;
            int maleFirstNameRandomIndex = random.nextInt(maleFirstNames.size()) + 1;
            int maleLastNameRandomIndex = random.nextInt(lastNames.size()) + 1;
            int femaleLastNameRandomIndex = random.nextInt(lastNames.size()) + 1;
            int locationRandomIndex = random.nextInt(locations.size()) + 1;
            String male = "m";
            String female = "f";
            for (int j = 0; j < currentNumberOfPeople / 2; ++j){
                int previousGenerationSize = (int) Math.sqrt(currentNumberOfPeople);
                String newHusbandID = UUID.randomUUID().toString();
                String newHusbandFirstName = maleFirstNames.get(maleFirstNameRandomIndex);
                String newHusbandLastName = lastNames.get(maleLastNameRandomIndex);
                int childIndex = previousGenerationSize + j;
                String newWifeID = UUID.randomUUID().toString();
                String newWifeFirstName = femaleFirstNames.get(femaleFirstNameRandomIndex);
                String newWifeLastName = lastNames.get(femaleLastNameRandomIndex);
                Person husband = new Person(newHusbandID, userAsPerson.getDecendant(), newHusbandFirstName, newHusbandLastName,
                                            male, null, null, newWifeID);
                Person wife = new Person(newWifeID, userAsPerson.getDecendant(), newWifeFirstName, newWifeLastName,
                                         female, null, null, newHusbandID);
                generateEvents(husband);
                generateEvents(wife);
                myFamily.add(currentIndex++, husband);
                myFamily.add(currentIndex++, wife);
                myFamily.get(childIndex).setMother(wife.getPerson_ID());
                myFamily.get(childIndex).setFather(husband.getPerson_ID());
                peopleGenerated += 2;
                if (generations != 0){
                    userAsPerson.setFather(newHusbandID);
                    userAsPerson.setMother(newWifeID);
                }
            }
        }
        if (generations != 0){
            int fatherIndex = 0;
            int motherIndex = 1;
            userAsPerson.setFather(myFamily.get(fatherIndex).getPerson_ID());
            userAsPerson.setMother(myFamily.get(motherIndex).getPerson_ID());
        }
        myFamily.add(currentIndex, userAsPerson);
        return myFamily;
    }

    public void generateEvents(Person myPerson){
        int numberOfEventsAdded = 0;
        Set<String> eventTypes = new TreeSet<>(); //fix decendants vs ancestors years later
        eventTypes.add("adventure");
        eventTypes.add("baptism");
        eventTypes.add("birth");
        eventTypes.add("christening");
        eventTypes.add("death");
        eventTypes.add("marriage");
        eventTypes.add("war");
        Random random = new Random();
        for (String s : eventTypes){
            int randomLocationIndex = random.nextInt(locations.size()) + 1;
            int randomYear = random.nextInt(2017);
            String newEventID = UUID.randomUUID().toString();
            String latitude = locations.get(randomLocationIndex).latitude;
            String longitude = locations.get(randomLocationIndex).longitude;
            String country = locations.get(randomLocationIndex).country;
            String city = locations.get(randomLocationIndex).city;
            Event newEvent = new Event(newEventID, myPerson.getDecendant(), myPerson.getPerson_ID(),
                                       latitude, longitude, country, city, s, Integer.toString(randomYear));
           // myPerson.getEventList().add(newEvent);
            myPerson.pushEventList(newEvent);
            ++numberOfEventsAdded;
        }
    }

    public Object jsonToLoginRequestObject(String jsonFileString){
        Object objectCopy = myGson.fromJson(jsonFileString, Service.LoginRequest.class);
        return objectCopy;
    }

    public RegisterRequest jsonToRegisterRequestObject(String jsonFileString){
        RegisterRequest objectCopy = myGson.fromJson(jsonFileString, Service.RegisterRequest.class);
        return objectCopy;
    }

    public LoadRequest jsonToLoadRequestObject(String jsonFileString){
        LoadRequest objectCopy = myGson.fromJson(jsonFileString, Service.LoadRequest.class);
        return objectCopy;
    }

    public String loadResultObjectToJson(LoadResult result){
        String jsonString = myGson.toJson(result);
        return jsonString;
    }

    public String registerResultObjectToJson(RegisterResult result){
        String jsonString = myGson.toJson(result);
        return jsonString;
    }

    public String personResultObjectToJson(PersonResult result){
        String jsonString = myGson.toJson(result);
        return jsonString;
    }

    public PersonRequest jsonToPersonRequest(String jsonString){
        PersonRequest request = myGson.fromJson(jsonString, PersonRequest.class);
        return request;
    }

    public String eventResultObjectToJson(EventResult result){
        String jsonString = myGson.toJson(result);
        return jsonString;
    }

    public String jsonErrorMessage(String message){
        ErrorResult errorString = new ErrorResult();
        errorString.writeErrorResult(message);
        return myGson.toJson(errorString);
    }

    public String UserObjectToJson(Object jsonObject){
        String jsonString = myGson.toJson(jsonObject);
        return jsonString;
    }

    public String clearResultObjectToJson(ClearResult result){
        return myGson.toJson(result);
    }

    //write an input stream to a string
    public static String readString(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        InputStreamReader sr = new InputStreamReader(is);
        char[] buf = new char[1024];
        int len;
        while ((len = sr.read(buf)) > 0) {
            sb.append(buf, 0, len);
        }
        return sb.toString();
    }

}
