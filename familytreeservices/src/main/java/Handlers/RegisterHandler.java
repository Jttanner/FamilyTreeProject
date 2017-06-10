package Handlers;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.UUID;


import Service.*;
import Model.*;

/**
 * Created by jontt on 5/30/2017.
 */
public class RegisterHandler implements HttpHandler{
    ObjectEncoderDecoder fileTranslator = new ObjectEncoderDecoder();
    @Override
    public void handle(HttpExchange exchange) throws IOException {

        boolean success = false;

        /*
         * I STILL NEED TO MAKE IT GENERATE RANDOM FAMILY HISTORY DATA
         */


        try {
            //post operation

            String requestMethod = exchange.getRequestMethod();
            String requestURI = exchange.getRequestURI().toString();

            if (requestMethod.toLowerCase().equals("post")) {
                // Get the HTTP request headers

                if (requestURI.equals("/user/register")){
                    //get the headers and put them in a string
                    Headers reqHeaders = exchange.getRequestHeaders();
                    InputStream is = exchange.getRequestBody();
                    String reqBody = fileTranslator.readString(is);
                    RegisterRequest myRegisterRequest = null;
                    try{ //transfer the json request to an object
                        myRegisterRequest = (RegisterRequest) fileTranslator.jsonToRegisterRequestObject(reqBody);
                    } catch(Exception e){
                        //the request remaining null will be enough to catch the invalid register attempt
                    }
                    //make sure that all of the fields we need aren't null
                    if (!myRegisterRequest.requestFieldsNotNull()) {
                        success = false;

                    } else{ //if they are valid, execute SQL queries for login
                        RegisterService myRegisterService = new RegisterService();
                        String personID = UUID.randomUUID().toString(); //a new personID for the new User
                        RegisterResult myRegisterResult = myRegisterService.register(myRegisterRequest, personID);
                        User newUser = myRegisterRequest.generateUserFromRequest(personID);
                        FillService fillNewUserService = new FillService();
                        FillRequest fillNewUserRequest = new FillRequest(myRegisterRequest.getUserName(), 4,
                                                         fileTranslator.userFamilyTreeModel(newUser, 4));
                        if (myRegisterResult!= null){
                            fillNewUserService.fill(fillNewUserRequest, fileTranslator.getPeopleGenerated());
                        }
                        if (myRegisterResult != null){ //password matched! (the result was not null)
                            exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                            OutputStream respBody = exchange.getResponseBody();
                            String respData = fileTranslator.registerResultObjectToJson(myRegisterResult);
                            //write and send the response body
                            respBody.write(respData.getBytes());
                            respBody.flush();
                            System.out.println(respData);
                            respBody.close();
                            success = true;
                        }
                    }

                   if (!success) { //invalid username or password input (some kind of login error)
                        exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                        OutputStream respBody = exchange.getResponseBody();
                        String errorResult = fileTranslator.jsonErrorMessage("Username already taken or bad field entry.");
                        respBody.write(errorResult.getBytes());
                        respBody.flush();
                        respBody.close();
                    }
                }
            }


        }
        catch (IOException e) { //For if a server error occurs
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, 0);
            exchange.getResponseBody().write("404 error".getBytes());
            exchange.getResponseBody().close();
            // Display/log the stack trace
            e.printStackTrace();
        }
    }

}
