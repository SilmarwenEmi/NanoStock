package NanoStock.properties;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * @author Emilie Laurent
 * This class contains methods to read informations in the config file (modified by the user)
 */
public class TakeProperties {

    /**
     * Get the mysql server address typed in the file NanoStock.properties
     * @return Server address
     * @throws IOException If configuration file is not found
     */
    public static String getServerAddress() throws IOException {

        String versionString;

        //to load application's properties, we use this class
        Properties mainProperties = new Properties();

        FileInputStream file;

        //the base folder is ./, the root of the main.properties file
        String path = "./NanoStock.properties";

        //load the file handle for main.properties
        file = new FileInputStream(path);

        //load all the properties from this file
        mainProperties.load(file);

        //we have loaded the properties, so close the file handle
        file.close();

        //retrieve the property we are interested
        versionString = mainProperties.getProperty("serverAddress");

        return versionString;
    }

    /**
     * Get the mysql database name typed in the file configuration file
     * @return Database name
     * @throws IOException If configuration file is not found
     */
    public static String getDBName() throws IOException {

        String versionString;

        //to load application's properties, we use this class
        Properties mainProperties = new Properties();

        FileInputStream file;

        //the base folder is ./, the root of the main.properties file
        String path = "./NanoStock.properties";

        //load the file handle for main.properties
        file = new FileInputStream(path);

        //load all the properties from this file
        mainProperties.load(file);

        //we have loaded the properties, so close the file handle
        file.close();

        //retrieve the property we are interested
        versionString = mainProperties.getProperty("databaseName");

        return versionString;
    }

    /**
     * Get the delay in days to delete log typed in the file configuration file
     * @return Number of days to keep
     * @throws IOException If configuration file is not found
     */
    public static String getLogDelay() throws IOException {

        String versionString;

        //to load application's properties, we use this class
        Properties mainProperties = new Properties();

        FileInputStream file;

        //the base folder is ./, the root of the main.properties file
        String path = "./NanoStock.properties";

        //load the file handle for main.properties
        file = new FileInputStream(path);

        //load all the properties from this file
        mainProperties.load(file);

        //we have loaded the properties, so close the file handle
        file.close();

        //retrieve the property we are interested
        versionString = mainProperties.getProperty("logDelay");

        return versionString;
    }

    /**
     * Get the delay in days to delete alerts typed in the file configuration file
     * @return Number of days to keep
     * @throws IOException If configuration file is not found
     */
    public static String getAlertDelay() throws IOException {

        String versionString;

        //to load application's properties, we use this class
        Properties mainProperties = new Properties();

        FileInputStream file;

        //the base folder is ./, the root of the main.properties file
        String path = "./NanoStock.properties";

        //load the file handle for main.properties
        file = new FileInputStream(path);

        //load all the properties from this file
        mainProperties.load(file);

        //we have loaded the properties, so close the file handle
        file.close();

        //retrieve the property we are interested
        versionString = mainProperties.getProperty("alertDelay");

        return versionString;
    }
}
