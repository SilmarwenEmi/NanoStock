import static org.junit.Assert.*;

import DataBase.Connect;
import DataBase.DeleteData;
import DataBase.ExtractData;
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
 * JUnit tests for DeleteData class
 */
public class DeleteDataTests {

    private static Supplier supplier;
    private static Supplier supplierToDelete;
    private static Product product;
    private static Equipment equipment;
    private static Accessory accessory;
    private static LogLine logLine;
    private static User user;
    private static User userToDelete;
    private static Connection connection;
    private static Location location;

    private static int supplierRowsDelete;
    private static int productRowsDelete;
    private static int equipmentRowsDelete;
    private static int accessoryRowsDelete;
    private static int logLineRowsDelete;
    private static int userRowsDelete;
    private static int locationRowsDelete;

    private static int supplierRowsAfterDelete;
    private static int productRowsAfterDelete;
    private static int equipmentRowsAfterDelete;
    private static int accessoryRowsAfterDelete;
    private static int logLineRowsAfterDelete;
    private static int userRowsAfterDelete;
    private static int locationRowsAfterDelete;

    @BeforeClass
    public static void setUpBeforClass() throws NotConnectedException {

        connection = Connect.connectToDB("Emilie", "passwordTest", "localhost","test");
    }

    @Before
    public void setUp() throws SQLException, NotConnectedException {
        user = new User("Emilie", "passwordTest", "Laurent", "Emilie", "emilie.laurent@student.unamur.be", connection, "Admin", null, false);

        userToDelete= new User("jaDoe", "testPassword", "Doe", "Jane", "jane.doe@nanostock.be", connection, "User", null, false);

        supplier = new Supplier("Glaxo Smith Kline", "ContactTestName", "8562562");

        supplierToDelete = new Supplier("newSupplierTest", "ContactTestName", "8562562");

        product = new Product(null, 21, "2565285235", "2017-05-10", 42, "ProductTest",
                "descriptionTest", "8652385623", "categoryTest", user, "Glaxo Smith Kline", null, "Cabinet 1");

        equipment = new Equipment(null, "equipmentTest", "2015", "categoryTest", "descriptionTest",
                "", "745849578956", user, supplier, null, 29, "", "Cabinet 1", null);

        accessory = new Accessory(null, "testAccessory", "testCategory", 0, "12345678988965986568",
                "2017-05-10", "testDescription", user, supplier.getSupplier(), 20, null, "Cabinet 1");

        logLine = new LogLine("testLogName", "RoomAccess", "2017-12-28", "18:15:42", user.getUserLogin());

        location = new Location(null, "TestLocation", "/Pictures/Location1.png", null, "product", user, "");

        supplierRowsDelete = DataBase.ExtractData.countRowsSupplier(user);
        productRowsDelete = DataBase.ExtractData.countRowsProduct(user);
        equipmentRowsDelete = DataBase.ExtractData.countRowsEquipment(user);
        accessoryRowsDelete = DataBase.ExtractData.countRowsAccessory(user);
        logLineRowsDelete = DataBase.ExtractData.countRowsLogTrail(user);
        userRowsDelete = DataBase.ExtractData.countRowsUser(user);
        locationRowsDelete = DataBase.ExtractData.countRowsLocation(user);

        supplierRowsAfterDelete = 0;
        productRowsAfterDelete = 0;
        equipmentRowsAfterDelete = 0;
        accessoryRowsAfterDelete = 0;
        logLineRowsAfterDelete = 0;
        userRowsAfterDelete = 0;
        locationRowsAfterDelete = 0;

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

        supplierRowsDelete = 0;
        productRowsDelete = 0;
        equipmentRowsDelete = 0;
        accessoryRowsDelete = 0;
        logLineRowsDelete = 0;
        userRowsDelete = 0;
        locationRowsDelete = 0;

        supplierRowsAfterDelete = 0;
        productRowsAfterDelete = 0;
        equipmentRowsAfterDelete = 0;
        accessoryRowsAfterDelete = 0;
        logLineRowsAfterDelete = 0;
        userRowsAfterDelete = 0;
        locationRowsAfterDelete = 0;
    }

    @AfterClass
    public static void eraseAfterClass(){
        connection = null;
    }

    @Test
    public void deleteProductTest() throws SQLException {

        DataBase.DeleteData.deleteProduct(product.getProductId(), user);
        productRowsAfterDelete = DataBase.ExtractData.countRowsProduct(user);
        assertTrue(productRowsAfterDelete == productRowsDelete -1);
    }

    @Test
    public void deleteAccessoryTest() throws SQLException {
        DataBase.DeleteData.deleteAccessory(accessory.getAccessoryId(), user);
        accessoryRowsAfterDelete = ExtractData.countRowsAccessory(user);
        assertTrue(accessoryRowsAfterDelete == accessoryRowsDelete -1);
    }

    @Test
    public void deleteEquipmentTest() throws SQLException {
        DataBase.DeleteData.deleteEquipment(equipment.getEquipmentId(), user);
        equipmentRowsAfterDelete = ExtractData.countRowsEquipment(user);
        assertTrue(equipmentRowsAfterDelete == equipmentRowsDelete -1);
    }

    @Test
    public void deleteLocationTest() throws SQLException {
        DataBase.DeleteData.deleteLocation(user, location.getLocationName());
        locationRowsAfterDelete = ExtractData.countRowsLocation(user);
        assertTrue(locationRowsAfterDelete == locationRowsDelete -1);
    }

    @Test
    public void deleteUserTest() throws NotConnectedException, SQLException {
        DataBase.DeleteData.deleteUser(user, userToDelete);
        userRowsAfterDelete = ExtractData.countRowsUser(user);
        assertTrue(userRowsAfterDelete == userRowsDelete -1);
    }


}
