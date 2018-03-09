package DataBase;

import WindowDisplay.*;

import java.io.FileInputStream;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author Emilie Laurent
 * Contains all the methods to insert data into the database
 */
public class InsertData {

    /**
     * Insert the new product in the database
     * @param product: The concerned product with its information
     * @param user: The active user to know his rights for database permissions
     * @throws SQLException: If the SQL query is invalid
     */
    public static void insertNewProduct(Product product, User user) throws SQLException {

        try {
            user.getUserConnection().setAutoCommit(true);

            String query = "INSERT INTO test.product (PLotNumber, PExpirationDate, PQuantity, Category, LName, PName, CASNumber, Description, Supplier)" +
                    " VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?);";

            PreparedStatement insertProduct = user.getUserConnection().prepareStatement(query);
            insertProduct.setString(1,product.getLotNumber());
            insertProduct.setString(2,product.getExpirationDate());
            insertProduct.setInt(3,product.getProductQuantity());
            insertProduct.setString(4,product.getProductCategory());
            insertProduct.setString(5,product.getProductLocation());
            insertProduct.setString(6,product.getProductName());
            insertProduct.setString(7,product.getProductCAS());
            insertProduct.setString(8,product.getProductDescription());
            insertProduct.setString(9,product.getProductSupplier());

            insertProduct.execute();

        } catch (SQLException e) {
            throw new SQLException();
        }

    }

    /**
     * Insert the new accessory into the database
     * @param accessory: All information of the new accessory to insert in the database
     * @param user: The active user to know his rights for database permissions
     * @throws SQLException: If the SQL query is invalid
     */
    public static void insertNewAccessory(Accessory accessory, User user) throws SQLException {
        try {
            user.getUserConnection().setAutoCommit(true);

            String query = "INSERT INTO test.accessory (ALotNumber, AExpirationDate, AQuantity, LName, Category, " +
                    "AName, Description, Supplier) VALUES (?, ?, ?, ?, ?, ?, ?, ?);";

            PreparedStatement insertAccessory = user.getUserConnection().prepareStatement(query);
            insertAccessory.setString(1, accessory.getAccessoryLotNumber());
            insertAccessory.setString(2, accessory.getAccessoryExpirationDate());
            insertAccessory.setInt(3, accessory.getAccessoryQuantity());
            insertAccessory.setString(4, accessory.getAccessoryLocation());
            insertAccessory.setString(5, accessory.getAccessoryCategory());
            insertAccessory.setString(6, accessory.getAccessoryName());
            insertAccessory.setString(7, accessory.getAccessoryDescription());
            insertAccessory.setString(8, accessory.getAccessorySupplier());

            insertAccessory.execute();

        } catch (SQLException e) {
            throw new SQLException("Wrong SQL query");
        }
    }

    /**
     * Insert the new equipment in the database
     * @param equipment: The equipment with its information to insert into the database
     * @param fileInputStream: Image file to save on equipment into database
     * @param user: The active user to know his rights for database permissions
     * @throws SQLException: If the SQL query is invalid
     */
    public static void insertNewEquipment(Equipment equipment, FileInputStream fileInputStream, User user) throws SQLException {

        user.getUserConnection().setAutoCommit(true);

        String query = "INSERT INTO test.equipment (EAcquisitionYear, EGraph, EUsageTime, EPicture, LName, EName, " +
                "Description, SerialNumber, Category, Supplier, MaintenancesDates, LastMaintenance) VALUES ( ?, '', '', ?, ?, ?, ?, ?, ?, ?, '', ?);";

        PreparedStatement insertEquipment = user.getUserConnection().prepareStatement(query);
        insertEquipment.setString(1, equipment.getEquipmentYear());
        insertEquipment.setBinaryStream(2, fileInputStream);
        insertEquipment.setString(3, equipment.getEquipmentLocation());
        insertEquipment.setString(4, equipment.getEquipmentName());
        insertEquipment.setString(5, equipment.getEquipmentDescription());
        insertEquipment.setString(6, equipment.getEquipmentSerialNumber());
        insertEquipment.setString(7, equipment.getEquipmentCategory());
        insertEquipment.setString(8, equipment.getEquipmentSupplier());

        //To get current date
        LocalDateTime day = LocalDateTime.now();
        insertEquipment.setString(9, day.toString());

        insertEquipment.execute();
    }

    /**
     * Insert a new location in the database
     * @param user: The active user to know his rights for database permissions
     * @param location: All information of new location to insert it in the database
     * @throws SQLException: If the SQL query is invalid
     */
    public static void createNewLocation(User user, Location location) throws SQLException {

        user.getUserConnection().setAutoCommit(true);
        String query = "INSERT INTO test.location (LImage, LName) VALUES (? , ?);";

        PreparedStatement insertLocation = user.getUserConnection().prepareStatement(query);
        insertLocation.setString(1,location.getLocationImagePath());
        insertLocation.setString(2, location.getLocationName());

        insertLocation.execute();

    }

    /**
     * Insert a new supplier in the database
     * @param user: The active user to know his rights for database permissions
     * @param supplier: All new supplier information to insert in database
     * @throws SQLException: If the SQL query is invalid
     */
    public static void createNewSupplier(User user, Supplier supplier) throws SQLException {

        user.getUserConnection().setAutoCommit(true);
        String query = "INSERT INTO test.supplierinfo (SupplierName, ContactName, ContactPhoneNumber) " +
                "VALUES (?, ?, ?);";

        PreparedStatement insertSupplier = user.getUserConnection().prepareStatement(query);
        insertSupplier.setString(1, supplier.getSupplier());
        insertSupplier.setString(2, supplier.getContactName());
        insertSupplier.setString(3, supplier.getContactPhone());

        insertSupplier.execute();

    }

    /**
     * Create a new user access to the database
     * @param user: The active user to know his rights for database permissions
     * @param userToInsert: The new user information to create new database connection
     * @throws SQLException: If the SQL query is invalid
     */
    public static void createNewUser(User user, User userToInsert) throws SQLException {

        user.getUserConnection().setAutoCommit(true);

        String query = "CREATE USER ?@'localhost' IDENTIFIED by ?;";

        PreparedStatement insertNewUser = user.getUserConnection().prepareStatement(query);
        insertNewUser.setString(1, userToInsert.getUserLogin());
        insertNewUser.setString(2, userToInsert.getUserPassword());

        insertNewUser.execute();

    }

    /**
     * Get the user privilege to user given
     * @param user: The active user to know his rights for database permissions
     * @param userToGrant: All information of the user to give user privilege for using the database
     * @throws SQLException: If the SQL query is invalid
     */
    public static void getUserPrivilege(User user, User userToGrant) throws SQLException {

        user.getUserConnection().setAutoCommit(true);

        String query = "GRANT select, update, insert on test.* to ?@'localhost';";

        PreparedStatement grantUser = user.getUserConnection().prepareStatement(query);
        grantUser.setString(1, userToGrant.getUserLogin());

        grantUser.execute();

    }

    /**
     * Get the admin privilege to user given
     * @param user: The active user to know his rights for database permissions
     * @param userToGrant: All information of the user to give admin privilege for using the database
     * @throws SQLException: If the SQL query is invalid
     */
    public static void getAdminPrivilege(User user, User userToGrant) throws SQLException {

        user.getUserConnection().setAutoCommit(true);

        String query = "GRANT ALL PRIVILEGES on test.* to ?@'localhost';";

        PreparedStatement grantAdmin = user.getUserConnection().prepareStatement(query);
        grantAdmin.setString(1, userToGrant.getUserLogin());

        grantAdmin.execute();

    }

    /**
     * Insert new user in the table user in the database
     * @param user: The active user to know his rights for database permissions
     * @param userToInsert: All information of new user to insert in user table database
     * @throws SQLException: If the SQL query is invalid
     */
    public static void insertNewUser(User user, User userToInsert) throws SQLException {

        user.getUserConnection().setAutoCommit(true);

        String query = "INSERT INTO test.user (Name, FirstName, Login, Email, Privileges) VALUES (?, ?, ?, ?, ?) ;";

        PreparedStatement insertUser = user.getUserConnection().prepareStatement(query);
        insertUser.setString(1, userToInsert.getUserName());
        insertUser.setString(2, userToInsert.getUserFirstName());
        insertUser.setString(3, userToInsert.getUserLogin());
        insertUser.setString(4, userToInsert.getUserMail());
        insertUser.setString(5, userToInsert.getUserPrivileges());

        insertUser.execute();

    }

    /**
     * Insert new log in database
     * @param user: The active user to know his rights for database permissions
     * @param description: Which action has been done by user
     * @param type: Which type of log is executed
     */
    public static void insertNewLog(User user, String description, String type){
        //To get current date
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime day = LocalDateTime.now();

        //To get current time
        DateTimeFormatter hourFormat = DateTimeFormatter.ofPattern("HH:mm:ss");
        LocalDateTime hour = LocalDateTime.now();

        try {
            user.getUserConnection().setAutoCommit(true);
            String query = "INSERT INTO test.log (LDescription, LType, LDate, LTime, Login) VALUES ( ?, ?, ?, ?, ?);";

            PreparedStatement insertLog = user.getUserConnection().prepareStatement(query);
            insertLog.setString(1, description);
            insertLog.setString(2, type);
            insertLog.setString(3, dateFormat.format(day));
            insertLog.setString(4, hourFormat.format(hour));
            insertLog.setString(5, user.getUserLogin());

            insertLog.execute();

        } catch (SQLException e) {
            System.out.println("log has not been inserted");
        }

    }

}
