package Handlers;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.UUID;

import Model.*;
import Service.*;

/**
 * Created by jontt on 5/30/2017.
 */
public class LoadHandler implements HttpHandler {
    ObjectEncoderDecoder fileTranslator = new ObjectEncoderDecoder();
    boolean success = false;
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String requestMethod = exchange.getRequestMethod();
        String requestURI = exchange.getRequestURI().toString();
        if (requestMethod.toLowerCase().equals("post")){
            Headers reqHeaders = exchange.getRequestHeaders();
            InputStream is = exchange.getRequestBody();
            String reqBody = fileTranslator.readString(is);
            LoadRequest myLoadRequest = null;
            try{ //transfer the json request to an object
                myLoadRequest = (LoadRequest) fileTranslator.jsonToLoadRequestObject(reqBody);
            } catch(Exception e){
                //the request remaining null will be enough to catch the invalid register attempt
            }
            if (myLoadRequest.fieldsNotNull()){
                ClearService myClearService = new ClearService();
                myClearService.clearAll();
                LoadService myLoadService = new LoadService();
                LoadResult myLoadResult = myLoadService.uploadAll(myLoadRequest);
                if (myLoadResult != null){ //result was successful
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                    OutputStream respBody = exchange.getResponseBody();
                    String respData = fileTranslator.loadResultObjectToJson(myLoadResult);
                    //write and send the response body
                    respBody.write(respData.getBytes());
                    respBody.flush();
                    System.out.println(respData);
                    respBody.close();
                    success = true;
                }
                if (!success){
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                    OutputStream respBody = exchange.getResponseBody();
                    String errorResult = fileTranslator.jsonErrorMessage("Load unsuccessful.");
                    respBody.write(errorResult.getBytes());
                    respBody.flush();
                    respBody.close();
                }
            }
        }
    }
}
