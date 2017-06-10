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
public class ClearHandler implements HttpHandler{
    ObjectEncoderDecoder fileTranslator = new ObjectEncoderDecoder();

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        try {
            //post operation
            String requestMethod = exchange.getRequestMethod();
            String requestURI = exchange.getRequestURI().toString();
            if (requestMethod.toLowerCase().equals("post")) {
                if (requestURI.equals("/clear/")){
                    //There is no request body
                    ClearService myClearService = new ClearService();
                    ClearResult myClearResult = myClearService.clearAll();
                    if(myClearResult != null){
                        exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                        OutputStream respBody = exchange.getResponseBody();
                        String respData = fileTranslator.clearResultObjectToJson(myClearResult);
                        respBody.write(respData.getBytes());
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
