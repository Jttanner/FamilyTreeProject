package Handlers;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;

import Service.LoginRequest;
import Service.LoginResult;
import Service.LoginService;

/**
 * Created by jontt on 5/26/2017.
 */
public class LoginHandler implements HttpHandler {
    ObjectEncoderDecoder fileTranslator = new ObjectEncoderDecoder();
    @Override
    public void handle(HttpExchange exchange) throws IOException {

        boolean success = false;

        try {
            //post operation

            String requestMethod = exchange.getRequestMethod();
            String requestURI = exchange.getRequestURI().toString();

            if (requestMethod.toLowerCase().equals("post")) {
                // Get the HTTP request headers

                if (requestURI.equals("/user/login")){
                    //get the headers and put them i na string
                    Headers reqHeaders = exchange.getRequestHeaders();
                    InputStream is = exchange.getRequestBody();
                    String reqBody = fileTranslator.readString(is);
                    LoginRequest myLoginRequest = null;
                    try{ //transfer the json request to an object
                        myLoginRequest = (LoginRequest) fileTranslator.jsonToLoginRequestObject(reqBody);
                    } catch(Exception e){
                        //the request remaining as null will be enough to catch the invalid login attempt
                    }
                    //make sure that all of the fields we need aren't null
                    if (myLoginRequest == null ||
                            myLoginRequest.getPassword() == null ||
                            myLoginRequest.getUserName() == null) {
                            success = false;

                    } else{ //if they are valid, execute SQL queries for login
                        LoginService myLoginService = new LoginService();
                        LoginResult myLoginResult = myLoginService.login(myLoginRequest);
                        if (myLoginResult != null){ //password matched! (the result was not null)
                            exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                            OutputStream respBody = exchange.getResponseBody();
                            String respData = fileTranslator.UserObjectToJson(myLoginResult);
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
                        String errorResult = fileTranslator.jsonErrorMessage("Invalid Username or Password.");
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
