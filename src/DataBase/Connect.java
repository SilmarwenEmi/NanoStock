package DataBase;

import NanostockException.NotConnectedException;
import java.sql.*;

/**
 * @author Emilie Laurent
 * Contains all the methods for connecting to the database
 */
public class Connect {

    /**
     * Enable the connection to the database.
     * @param login: The login of the user to connect to database
     * @param password: The password for that login to connect to database
     * @param serverAddress: Address of the mysql server with which we want to be connected
     * @param DBName: Name of the mysql database with which we want to be connected
     * @return The necessary to send queries to the database
     * @throws NotConnectedException whether we're not connected
     */
    public static Connection connectToDB(String login, String password, String serverAddress, String DBName) throws NotConnectedException {
        try {

            String url = "jdbc:mysql://"+ serverAddress +"/" + DBName + "?autoReconnect=true&useSSL=false";

            Connection connection = DriverManager.getConnection(url, login, password);

            return connection;

        } catch (Exception exception) {
            throw new NotConnectedException("No connection to database");
        }

    }
}
