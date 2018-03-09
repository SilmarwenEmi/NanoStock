package DataBase;

import NanoStock.properties.TakeProperties;
import NanostockException.NotConnectedException;
import WindowDisplay.User;

import javax.swing.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @author Emilie Laurent
 * Contains all the methods for deleting data in the database
 */
public class DeleteData {

    /**
     * Delete the concerned product
     * @param productNumberId: The identification number of the product in the database
     * @param user: The active user to know his rights for database permissions
     */
    public static void deleteProduct(int productNumberId, User user) {
        Connection connection = user.getUserConnection();

        try {
            user.setUserConnection(connection);

            //Prepare query for sending to database
            String query = "DELETE FROM test.product WHERE PNum = ?;";

            PreparedStatement deleteProduct = user.getUserConnection().prepareStatement(query);
            deleteProduct.setInt(1, productNumberId);

            deleteProduct.execute();

        } catch (SQLException e1) {
            JOptionPane.showMessageDialog(null, "Product has not been deleted.",
                    "Product deletion error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Delete the concerned accessory
     * @param accessoryId: The identification number of the product in the database
     * @param user: The active user to know his rights for database permissions
     */
    public static void deleteAccessory(int accessoryId, User user) {

        try {
            user.setUserConnection(user.getUserConnection());

            //Prepare query for sending to database
            String query = "DELETE FROM test.accessory WHERE ANum = ?;";

            PreparedStatement deleteAccessory = user.getUserConnection().prepareStatement(query);
            deleteAccessory.setInt(1, accessoryId);

            deleteAccessory.execute();

        } catch (SQLException e1) {
            JOptionPane.showMessageDialog(null, "Accessory has not been deleted.",
                    "Accessory deletion error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Delete the concerned accessory
     * @param equipmentId: The identification number of the product in the database
     * @param user: The active user to know his rights for database permissions
     */
    public static void deleteEquipment(int equipmentId, User user) {

        try {
            user.getUserConnection().setAutoCommit(true);

            //Prepare query for sending to database
            String query = "DELETE FROM test.equipment WHERE ENum = ?;";

            PreparedStatement deleteEquipment = user.getUserConnection().prepareStatement(query);
            deleteEquipment.setInt(1, equipmentId);

            deleteEquipment.execute();


        } catch (SQLException e1) {
            JOptionPane.showMessageDialog(null, "Accessory has not been deleted.",
                    "Accessory deletion error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Delete the concerned location
     * @param user: The active user to know his rights for database permissions
     * @param locationName: Name of the location to delete
     * @throws SQLException: If the SQL query is invalid
     */
    public static void deleteLocation(User user, String locationName) throws SQLException {

            user.getUserConnection().setAutoCommit(true);

            //Prepare query for sending to database
            String query = "DELETE FROM test.location WHERE LName = ?; ";

            PreparedStatement deleteLocation = user.getUserConnection().prepareStatement(query);
            deleteLocation.setString(1, locationName);

            deleteLocation.execute();
    }

    /**
     * Delete the user access to database
     * @param user: The active user to know his rights for database permissions
     * @param userToDrop: User with all information to drop from database connection
     * @throws NotConnectedException: If the connection for user to database fails
     */
    public static void dropUserConnection(User user, User userToDrop) throws NotConnectedException {
        try {
            user.getUserConnection().setAutoCommit(true);

            //Prepare query for sending to database
            String query = "DROP USER ?@'localhost';";

            PreparedStatement dropUser = user.getUserConnection().prepareStatement(query);
            dropUser.setString(1, userToDrop.getUserLogin());

            dropUser.execute();

        } catch (SQLException e) {
            throw new NotConnectedException();
        }

    }

    /**
     * Delete user information in the user table
     * @param user: The active user to know his rights for database permissions
     * @param userToDelete: User with all information to drop from database user table
     * @throws NotConnectedException: If the connection for user to database fails
     */
    public static void deleteUser(User user, User userToDelete) throws NotConnectedException {
        try {
            user.getUserConnection().setAutoCommit(true);

            //Prepare query for sending to database
            String query = "DELETE FROM test.user WHERE Login = ?;";

            PreparedStatement deleteUser = user.getUserConnection().prepareStatement(query);
            deleteUser.setString(1, userToDelete.getUserLogin());

            deleteUser.execute();

        } catch (SQLException e) {
            throw new NotConnectedException();
        }
    }

    /**
     * Delete all logs older than number of days mention in config file
     * @param user: The active user to know his rights for database permissions
     * @throws NotConnectedException: If the connection for user to database fails
     */
    public static void deleteLog(User user) throws NotConnectedException {
        try{
            user.getUserConnection().setAutoCommit(true);

            String logDelay = TakeProperties.getLogDelay();

            //Prepare query for sending to database
            String query = "DELETE FROM test.log WHERE DATEDIFF(CURDATE(), LDate) > ? ;";

            PreparedStatement deleteLog = user.getUserConnection().prepareStatement(query);
            deleteLog.setString(1, logDelay);

            deleteLog.execute();

        } catch (SQLException e) {
            throw new NotConnectedException();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Cannot access to file configuration information.",
                    "Configuration file error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Delete all archived alerts older than number of days mention in config file
     * @param user: The active user to know his rights for database permissions
     * @throws NotConnectedException: If the connection for user to database fails
     */
    public static void deleteArchivedAlerts(User user) throws NotConnectedException {
        try{
            user.getUserConnection().setAutoCommit(true);

            String delay = TakeProperties.getAlertDelay();

            //Prepare query for sending to database
            String query = "DELETE FROM test.alert WHERE Archived = 1 AND DATEDIFF(CURDATE(), ADate) > ? ;";

            PreparedStatement deleteLog = user.getUserConnection().prepareStatement(query);
            deleteLog.setString(1, delay);

            deleteLog.execute();

        } catch (SQLException e) {
            throw new NotConnectedException();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Cannot access to file configuration information.",
                    "Configuration file error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Delete all privileges for a given user
     * @param user: The active user to know his rights for database permissions
     * @param userToRevoke: User to revoke all privileges
     * @throws SQLException: If the SQL query is invalid
     */
    public static void revokeAllPrivileges(User user, User userToRevoke) throws SQLException {
        user.getUserConnection().setAutoCommit(true);

        //Prepare query for sending to database
        String query = "REVOKE ALL PRIVILEGES on test.* from ?@'localhost';";

        PreparedStatement revokePrivleges = user.getUserConnection().prepareStatement(query);
        revokePrivleges.setString(1, userToRevoke.getUserLogin());

        revokePrivleges.execute();
    }

    /**
     * Delete alert for concerned deleted product
     * @param user: The active user to know his privileges
     * @param productId: Identification number of the deleted product
     * @throws SQLException: If SQL query is invalid
     * @throws NotConnectedException: If the connection to the database fails
     */
    public static void deleteProductAlert(User user, int productId) throws SQLException, NotConnectedException {
        try {
            user.getUserConnection().setAutoCommit(true);
        } catch (SQLException e) {
            throw new NotConnectedException();
        }

        //Prepare query for sending to database
        String query = "DELETE * FROM test.alert WHERE PNum = ?;";

        PreparedStatement deleteProductAlerts = user.getUserConnection().prepareStatement(query);
        deleteProductAlerts.setInt(1, productId);

        deleteProductAlerts.execute();
    }

    /**
     * Delete alert for concerned deleted product
     * @param user: The active user to know his privileges
     * @param equipmentId: Identification number of the deleted equipment
     * @throws SQLException: If SQL query is invalid
     * @throws NotConnectedException: If the connection to the database fails
     */
    public static void deleteEquipmentAlert(User user, int equipmentId) throws SQLException, NotConnectedException {
        try {
            user.getUserConnection().setAutoCommit(true);
        } catch (SQLException e) {
            throw new NotConnectedException();
        }

        //Prepare query for sending to database
        String query = "DELETE * FROM test.alert WHERE ENum = ?;";

        PreparedStatement deleteEquipmentAlerts = user.getUserConnection().prepareStatement(query);
        deleteEquipmentAlerts.setInt(1, equipmentId);

        deleteEquipmentAlerts.execute();
    }
}
