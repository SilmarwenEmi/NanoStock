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

/**
 * @author Laurent Emilie
 * JUnit tests for InsertData class
 */
public class InsertDataTests {

    private static Supplier supplier;
    private static Supplier supplierToInsert;
    private static Product product;
    private static Equipment equipment;
    private static Accessory accessory;
    private static LogLine logLine;
    private static User user;
    private static User userToInsert;
    private static Connection connection;
    private static Location location;

    private static int supplierRows;
    private static int productRows;
    private static int equipmentRows;
    private static int accessoryRows;
    private static int logLineRows;
    private static int userRows;
    private static int locationRows;

    private static int supplierRowsAfter;
    private static int productRowsAfter;
    private static int equipmentRowsAfter;
    private static int accessoryRowsAfter;
    private static int logLineRowsAfter;
    private static int userRowsAfter;
    private static int locationRowsAfter;

    @BeforeClass
    public static void setUpBeforClass() throws NotConnectedException {

        connection = Connect.connectToDB("Emilie", "passwordTest", "localhost","test");
    }

    @Before
    public void setUp() throws SQLException, NotConnectedException {
        user = new User("Emilie", "passwordTest", "Laurent", "Emilie", "emilie.laurent@student.unamur.be", connection, "Admin", null, false);

        userToInsert = new User("jaDoe", "testPassword", "Doe", "Jane", "jane.doe@nanostock.be", connection, "User", null, false);

        supplier = new Supplier("Glaxo Smith Kline", "ContactTestName", "8562562");

        supplierToInsert = new Supplier("newSupplierTest", "ContactTestName", "8562562");

        product = new Product(null, 0, "2565285235", "2017-05-10", 42, "ProductTest",
                "descriptionTest", "8652385623", "categoryTest", user, "Glaxo Smith Kline", null, "Cabinet 1");

        equipment = new Equipment(null, "equipmentTest", "2015", "categoryTest", "descriptionTest",
                "", "745849578956", user, supplier, null, 0, "", "Cabinet 1", null);

        accessory = new Accessory(null, "testAccessory", "testCategory", 0, "12345678988965986568",
                "2017-05-10", "testDescription", user, supplier.getSupplier(), 0, null, "Cabinet 1");

        logLine = new LogLine("testLogName", "RoomAccess", "2017-12-28", "18:15:42", user.getUserLogin());

        location = new Location(null, "TestLocation", "/Pictures/Location1.png", null, "product", user, "");

        supplierRows = DataBase.ExtractData.countRowsSupplier(user);
        productRows = DataBase.ExtractData.countRowsProduct(user);
        equipmentRows = DataBase.ExtractData.countRowsEquipment(user);
        accessoryRows = DataBase.ExtractData.countRowsAccessory(user);
        logLineRows = DataBase.ExtractData.countRowsLogTrail(user);
        userRows = DataBase.ExtractData.countRowsUser(user);
        locationRows = DataBase.ExtractData.countRowsLocation(user);

        supplierRowsAfter = 0;
        productRowsAfter = 0;
        equipmentRowsAfter = 0;
        accessoryRowsAfter = 0;
        logLineRowsAfter = 0;
        userRowsAfter = 0;
        locationRowsAfter = 0;

    }

    @After
    public void eraseAfter(){
        supplier = null;
        product = null;
        equipment = null;
        accessory = null;
        logLine = null;
        user = null;
        location = null;

        supplierRows = 0;
        productRows = 0;
        equipmentRows = 0;
        accessoryRows = 0;
        logLineRows = 0;
        userRows = 0;
        locationRows = 0;

        supplierRowsAfter = 0;
        productRowsAfter = 0;
        equipmentRowsAfter = 0;
        accessoryRowsAfter = 0;
        logLineRowsAfter = 0;
        userRowsAfter = 0;
        locationRowsAfter = 0;
    }

    @AfterClass
    public static void eraseAfterClass(){
        connection = null;
    }

    @Test
    public void insertNewProductTest() throws SQLException {

        DataBase.InsertData.insertNewProduct(product, user);

        productRowsAfter = DataBase.ExtractData.countRowsProduct(user);
        assertTrue(productRowsAfter == productRows + 1);
    }

    @Test
    public void insertNewEquipmentTest() throws SQLException {

        DataBase.InsertData.insertNewEquipment(equipment, null, user);

        equipmentRowsAfter = DataBase.ExtractData.countRowsEquipment(user);
        assertTrue(equipmentRowsAfter == equipmentRows + 1);
    }

    @Test
    public void insertNewAccessoryTest() throws SQLException {

        DataBase.InsertData.insertNewAccessory(accessory, user);

        accessoryRowsAfter = DataBase.ExtractData.countRowsAccessory(user);
        assertTrue(accessoryRowsAfter == accessoryRows +1 );
    }

    @Test
    public void insertNewLogTest() throws SQLException {

        DataBase.InsertData.insertNewLog(user, logLine.getDescriptionLog().getText(), "ProductAdded");

        logLineRowsAfter = DataBase.ExtractData.countRowsLogTrail(user);
        assertTrue(logLineRowsAfter == logLineRows + 1);
    }

    @Test
    public void insertNewUserTest() throws SQLException {

        DataBase.InsertData.insertNewUser(user, userToInsert);

        userRowsAfter = DataBase.ExtractData.countRowsUser(user);
        assertTrue(userRowsAfter == userRows + 1);
    }

    @Test
    public void createNewSupplierTest() throws SQLException {

        DataBase.InsertData.createNewSupplier(user, supplierToInsert);

        supplierRowsAfter = DataBase.ExtractData.countRowsSupplier(user);
        assertTrue(supplierRowsAfter == supplierRows + 1);
    }

    @Test
    public void createNewLocation() throws SQLException {

        DataBase.InsertData.createNewLocation(user, location);

        locationRowsAfter = DataBase.ExtractData.countRowsLocation(user);
        assertTrue(locationRowsAfter == locationRows + 1);

    }
}
