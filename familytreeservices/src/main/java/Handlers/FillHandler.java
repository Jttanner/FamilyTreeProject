package Handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;

import Service.FillRequest;
import Service.FillResult;
import Service.FillService;
import Model.*;

/**
 * Created by jontt on 5/30/2017.
 */
public class FillHandler implements HttpHandler {
    ObjectEncoderDecoder fileTranslator = new ObjectEncoderDecoder();
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        boolean success = false;
        String requestMethod = exchange.getRequestMethod();
        String requestURI = exchange.getRequestURI().toString();
        if (requestMethod.toLowerCase().equals("post")){
            int userNameBeginIndex = 6;
            int generations = 4;
            String userNameRequest = requestURI.substring(userNameBeginIndex);
            int backslashIndex = userNameRequest.length() - 2; //might need to move if its double digit?
            String userName = "";
            if(userNameRequest.substring(backslashIndex).charAt(0) =='/'){
                int generationsIndex = userNameRequest.length() - 1;
                generations = userNameRequest.charAt(generationsIndex) - '0'; //change from char to int
                userName = userNameRequest.substring(0, backslashIndex);
            } else{
                userName = userNameRequest;
            }
            //add that int # of generations to that username
            FillService myFillService = new FillService();
            User currentUser = myFillService.getUser(userName);
            FillRequest myFillRequest = new FillRequest(userName, generations, fileTranslator.userFamilyTreeModel(currentUser, generations));
            FillResult myFillResult = myFillService.fill(myFillRequest, fileTranslator.getPeopleGenerated());
            fileTranslator.resetPeopleGenerated();
            if (myFillResult != null){
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                OutputStream respBody = exchange.getResponseBody();
                String respData = fileTranslator.UserObjectToJson(myFillResult);
                //write and send the response body
                respBody.write(respData.getBytes());
                respBody.flush();
                System.out.println(respData);
                respBody.close();
                success = true;
            }
            if (!success) { //invalid username or password input (some kind of login error)
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                OutputStream respBody = exchange.getResponseBody();
                String errorResult = fileTranslator.jsonErrorMessage("Fill not executed successfully.");
                respBody.write(errorResult.getBytes());
                respBody.flush();
                respBody.close();
            }
        }

    }
}
