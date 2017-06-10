package Handlers;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;

import Service.*;

/**
 * Created by jontt on 5/30/2017.
 */
public class PersonHandler implements HttpHandler {
    ObjectEncoderDecoder fileTranslator = new ObjectEncoderDecoder();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        //check for just /person or for /person/stuff

        boolean success = false;
        String requestMethod = exchange.getRequestMethod();
        String requestURI = exchange.getRequestURI().toString();
        if (requestMethod.toLowerCase().equals("get")){
            Headers reqHeaders = exchange.getRequestHeaders();
            String authToken = reqHeaders.getFirst("Authorization");
            PersonService myPersonService = new PersonService();
            String allPersonURI = "/person/";
            PersonResult myPersonResult = null;
            if (requestURI.equals(allPersonURI)){ //get all relatives
                myPersonResult = myPersonService.getAllFamilyMembers(authToken);
            } else {
                String personID = requestURI.substring(allPersonURI.length()); //get one person
                myPersonResult = myPersonService.getPerson(personID, authToken);
            }
            if (myPersonResult != null){
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                OutputStream respBody = exchange.getResponseBody();
                String respData = fileTranslator.personResultObjectToJson(myPersonResult);
                //write and send the response body
                respBody.write(respData.getBytes());
                respBody.flush();
                System.out.println(respData);
                respBody.close();
                success = true;
            } else{
                success = false;
            }

            if(!success){
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                OutputStream respBody = exchange.getResponseBody();
                String errorResult = fileTranslator.jsonErrorMessage("Invalid request.");
                respBody.write(errorResult.getBytes());
                respBody.flush();
                respBody.close();
            }

        }
    }
}
