package Service;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import DataAccess.DataAccess;
import Model.Person;

/**
 * Created by jontt on 5/20/2017.
 */

/**
 * Class used to clear various information from the database
 */
public class ClearService {
    DataAccess myDAO;

    /**
     * Creates a DataAccess object to interact with the database in order to clear information.
     * DataAccess objects assist in authenticating the user, and accessing the database
     */

    /**
     * clears the entire database.
     * @return ClearResult object containing success or failure information.
     */
    public ClearResult clearAll(){
        myDAO = new DataAccess();
        boolean allCleared = false;
        String resultMessage = null;
        if (myDAO.clearAllTables()){
            resultMessage = "Clear Succeeded";
        } else{
            resultMessage = "Clear Failed";
        }
        myDAO.closeConnection();
        return new ClearResult(resultMessage);
    }
}
