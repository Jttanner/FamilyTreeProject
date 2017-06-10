package theappshelf.familytreeproject.tasks;

import android.os.AsyncTask;
import android.widget.Toast;

import Service.LoginRequest;
import Service.LoginResult;
import theappshelf.familytreeproject.ServerProxy.*;
import theappshelf.familytreeproject.clientModel.FamilyTreeData;

/**
 * Created by jontt on 6/6/2017.
 */
public class loginTask extends AsyncTask<String, String, String> {
    FamilyTreeData myDataSingleton = FamilyTreeData.get();
    //loginfragment add the activity as a constructor
    loginFragment fragment;
    String authToken = null;
    LoginResult result;

    public loginTask(loginFragment fragment){
        this.fragment = fragment;
    }

    boolean succeeded = false;

    @Override
    protected String doInBackground(String... params) {
        ServerProxy myServerProxy = new ServerProxy();
        LoginRequest request = new LoginRequest(myDataSingleton.getUserName(), myDataSingleton.getPassword());
        result = myServerProxy.login(request, "/user/login");
        if (result != null){
            authToken = result.getAuthToken();
            succeeded = true;
            return "Login Successful!";
        } else{
            succeeded = false;
            return null;
        }
    }

    @Override
    protected void onPostExecute(String s) {
        //toast the result
        //fetch all the data into the singleton with
        //do getDataTask
        if (authToken != null){
            getDataTask getData = new getDataTask(fragment, authToken, "Login");
            getData.execute();
        } else{
            Toast.makeText(fragment.getActivity(), "Invalid Login.", Toast.LENGTH_LONG).show();
        }

    }

    //onPostExecute:
    //if succeeded, get data, then change fragment
    //if failed, toast bad login
}
