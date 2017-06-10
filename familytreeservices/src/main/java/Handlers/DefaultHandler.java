package Handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.nio.file.*;

/**
 * Created by jontt on 5/29/2017.
 */
public class DefaultHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange httpExchange)  {
        try{
            //OutputStream out = httpExchange.getResponseBody();
            //String filePathStr = "webAPItest/web/css/main.css";
            String httpURI = httpExchange.getRequestURI().toString();
            String filePathStr = "";
            boolean success = false;
            if (httpURI.equals("/") || httpURI.equals("/index.html")){ //if the request is for the main page
                filePathStr = "webAPItest/data/web/index.html";
                success = true;
            } else if (httpURI.equals("/css/main.css")){  //request to get the css file
                filePathStr = "webAPItest/data/web/css/main.css";
                success = true;
            }
            if (success == true){
                Path filePath = FileSystems.getDefault().getPath(filePathStr);
                httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                Files.copy(filePath, httpExchange.getResponseBody());


                //exh.getReqURI.toString gets the url path that they ask for

                //httpExchange.getResponseBody().write(Files.readAllBytes(filePath));
                httpExchange.getResponseBody().flush();
                httpExchange.close();


            } else{
                httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                httpExchange.getResponseBody().close();
            }




            //out.flush();


            //OutputStream respBody = httpExchange.getResponseBody();

            //respBody.close();
        } catch (IOException e){
            e.printStackTrace();
        }




        //writeString(respData, respBody);


    }
}
