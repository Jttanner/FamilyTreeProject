package Service;

/**
 * Created by jontt on 5/30/2017.
 */
public class ErrorResult {

    String message;

    public void writeErrorResult(String errorMessage){
        message = errorMessage;
    }

    public String getErrorResult(){
        return message;
    }
}
