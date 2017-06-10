package Service;

/**
 * Created by jontt on 5/23/2017.
 */

/**
 * Contains information for Clear Service response bodies.  For Clear Service, this is simply whether or not it was successful.
 */
public class ClearResult {
    String message;
    public ClearResult(String message){
        this.message = message;
    }
}
