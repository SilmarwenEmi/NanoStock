import static org.junit.Assert.*;

import DataBase.Connect;
import NanostockException.NotConnectedException;
import WindowDisplay.*;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * @author Laurent Emilie
 * JUnit tests for ExtractData class
 */
public class ExtractDataTest {

    private static Connection connection;
    private static User user;
    private static ArrayList<Supplier> supplierArrayList;
    private static ArrayList<Product> productArrayList;
    private static ArrayList<Equipment> equipmentArrayList;
    private static ArrayList<Accessory> accessoryArrayList;
    private static ArrayList<LogLine> logLineArrayList;
    private static ArrayList<AlertLine> alertLineNewsArrayList;
    private static ArrayList<AlertLine> alertLineArchivedArrayList;

    private static ArrayList<User> userArrayList;
    private static ArrayList<Location> locationArrayList;

    private static int supplierRows;
    private static int productRows;
    private static int equipmentRows;
    private static int accessoryRows;
    private static int logLineRows;
    private static int alertLineNewsRows;
    private static int alertLineArchivedRows;
    private static int userRows;
    private static int locationRows;

    @BeforeClass
    public static void setUpBeforClass() throws NotConnectedException {

        connection = Connect.connectToDB("Emilie", "passwordTest", "localhost", "test");
    }

    @Before
    public void setUp() throws SQLException, NotConnectedException {
        user = new User("Emilie", "passwordTest", "Laurent", "Emilie", "emilie.laurent@student.unamur.be", connection, "Admin", null, false);
        supplierArrayList = new ArrayList<>();
        productArrayList = new ArrayList<>();
        equipmentArrayList = new ArrayList<>();
        accessoryArrayList = new ArrayList<>();
        logLineArrayList = new ArrayList<>();
        alertLineNewsArrayList = new ArrayList<>();
        alertLineArchivedArrayList = new ArrayList<>();
        userArrayList = new ArrayList<>();
        locationArrayList = new ArrayList<>();

        supplierRows = DataBase.ExtractData.countRowsSupplier(user);
        productRows = DataBase.ExtractData.countRowsProduct(user);
        equipmentRows = DataBase.ExtractData.countRowsEquipment(user);
        accessoryRows = DataBase.ExtractData.countRowsAccessory(user);
        logLineRows = DataBase.ExtractData.countRowsLogTrail(user);
        alertLineNewsRows = DataBase.ExtractData.countRowsAlerts(user, 0);
        alertLineArchivedRows = DataBase.ExtractData.countRowsAlerts(user, 1);
        userRows = DataBase.ExtractData.countRowsUser(user);
        locationRows = DataBase.ExtractData.countRowsLocation(user);
    }

    @After
    public void eraseAfter(){

        user = null;
        supplierArrayList = null;
        productArrayList = null;
        productArrayList = null;
        equipmentArrayList = null;
        accessoryArrayList = null;
        logLineArrayList = null;
        alertLineNewsArrayList = null;
        alertLineArchivedArrayList = null;

        supplierRows = 0;
        productRows = 0;
        equipmentRows = 0;
        accessoryRows = 0;
        logLineRows = 0;
        alertLineNewsRows = 0;
        alertLineArchivedRows = 0;
        userRows = 0;
        locationRows = 0;

    }

    @AfterClass
    public static void eraseAfterClass(){
        connection = null;
    }

    @Test
    public void extractAllLocationsTest() throws NotConnectedException, SQLException {

        locationArrayList = DataBase.ExtractData.extractAllLocations(null, null, "", user, "");
        assertTrue(locationArrayList.size() == locationRows);
    }

    @Test
    public void extractAllSuppliers() throws SQLException {

        supplierArrayList = DataBase.ExtractData.extractAllSuppliers(user);
        assertTrue(supplierArrayList.size() == supplierRows);
    }

    @Test
    public void extractAllUsersTest() throws NotConnectedException, SQLException {

        userArrayList = DataBase.ExtractData.extractAllUsers(user);
        assertTrue(userArrayList.size() == userRows);
    }

    @Test
    public void extractAllLogTest() throws SQLException {

        logLineArrayList = DataBase.ExtractData.extractAllLog(user);
        assertTrue(logLineArrayList.size() == logLineRows);
    }

}
