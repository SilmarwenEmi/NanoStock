package DataBase;

import NanostockException.NotConnectedException;
import WindowDisplay.Equipment;
import WindowDisplay.StockMangement;
import WindowDisplay.User;

import javax.swing.*;
import java.io.FileInputStream;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;

/**
 * @author Emilie Laurent
 * Contains all the methods to updata data in the database
 */
public class UpdateData {

    /**
     * Update user password to access database
     * @param user: The active user to know his rights for database permissions
     * @throws SQLException: If the SQL query is invalid
     */
    public static void changeUserPassword(User user) throws SQLException {

        user.setUserConnection(user.getUserConnection());

        //Prepare query for sending to database
        String query = "ALTER USER ?@'localhost' IDENTIFIED BY ?;";

        PreparedStatement changePassword = user.getUserConnection().prepareStatement(query);
        changePassword.setString(1, user.getUserLogin());
        changePassword.setString(2,user.getUserPassword());

        changePassword.execute();
    }

    /**
     * Change the user picture in the user table of the database
     * @param user: All information about concerned user
     * @param fileInputStream: New picture of  the user
     * @throws SQLException: If the SQL query is invalid
     */
    public static void updatePicture(User user, FileInputStream fileInputStream) throws SQLException {
        user.setUserConnection(user.getUserConnection());

        //Prepare query for sending to database
        String query = "UPDATE test.user SET UPicture = ? WHERE Login = ? ;";

        PreparedStatement updatePicture = user.getUserConnection().prepareStatement(query);
        updatePicture.setBinaryStream(1, fileInputStream);
        updatePicture.setString(2, user.getUserLogin());

        updatePicture.execute();
    }


    /**
     * Update the product quantity in the database
     * @param user: The active user to know his rights for database permissions
     * @param quantity: The new quantity for modifying old product quantity in database
     * @param pnum: Concerned product identification number in the database
     * @throws SQLException If the SQL query is invalid
     */
    public static void updateProductQuantity(User user, int quantity, int pnum) throws SQLException {

        user.getUserConnection().setAutoCommit(true);

        String query = "UPDATE test.product SET PQuantity = ? WHERE PNum = ?;";

        PreparedStatement updateProductQuantity = user.getUserConnection().prepareStatement(query);
        updateProductQuantity.setInt(1, quantity);
        updateProductQuantity.setInt(2, pnum);

        updateProductQuantity.execute();

    }

    /**
     * Update the accessory quantity in the database
     * @param user: The active user to know his rights for database permissions
     * @param quantity: The new quantity for modifying old accessory quantity in database
     * @param anum: Concerned accessory identification number in the database
     * @throws SQLException If the SQL query is invalid
     */
    public static void updateAccessoryQuantity(User user, int quantity, int anum) throws SQLException {

        user.getUserConnection().setAutoCommit(true);

        String query = "UPDATE test.accessory SET AQuantity = ? WHERE ANum = ?;";

        PreparedStatement updateAccessoryQuantity = user.getUserConnection().prepareStatement(query);
        updateAccessoryQuantity.setInt(1, quantity);
        updateAccessoryQuantity.setInt(2, anum);

        updateAccessoryQuantity.execute();

    }

    /**
     * Update the equipment total usage time
     * @param user: The active user to know his rights for database permissions
     * @param usageTime: Total usage time of the equipment for updating old value in database
     * @param ENum: Concerned equipment identification number in the database
     * @throws SQLException: If the SQL query is invalid
     */
    public static void updateEquipmentUsageTime(User user, String usageTime, int ENum) throws SQLException {

        user.getUserConnection().setAutoCommit(true);

        String query = "UPDATE test.equipment SET EUsageTime = ? WHERE ENum = ?;";

        PreparedStatement updateEquipmentUsageTime = user.getUserConnection().prepareStatement(query);
        updateEquipmentUsageTime.setString(1, usageTime);
        updateEquipmentUsageTime.setInt(2, ENum);

        updateEquipmentUsageTime.execute();

    }

    /**
     * Update the equipment maintenance
     * @param user: The active user to know his rights for database permissions
     * @param Enum: Concerned equipment identification number in the database
     * @param dates: All dates for modifying old maintenance dates in database
     * @throws SQLException: If the SQL query is invalid
     */
    public static void updateEquipmentMaintenances(User user, int Enum, String dates) throws SQLException {

        user.getUserConnection().setAutoCommit(true);

        String query = "UPDATE test.equipment SET MaintenancesDates = ?, NeedMaintenance = '0' WHERE ENum = ?;";

        PreparedStatement updateEquipmentMaintenances = user.getUserConnection().prepareStatement(query);
        updateEquipmentMaintenances.setString(1, dates);
        updateEquipmentMaintenances.setInt(2, Enum);

        updateEquipmentMaintenances.execute();

    }

    /**
     * Archive the alert
     * @param user: The active user to know his rights for database permissions
     * @param alertId: Concerned alert identification number in the database
     * @throws SQLException: If the SQL query is invalid
     */
    public static void archiveAlert(User user, String alertId) throws SQLException {

        user.getUserConnection().setAutoCommit(true);

        String query = "UPDATE test.alert SET Archived = 1 WHERE AId = ?;";

        PreparedStatement archiveAlert = user.getUserConnection().prepareStatement(query);
        archiveAlert.setString(1, alertId);

        archiveAlert.execute();

    }

    /**
     * Update the last maintenance for the alert
     * @param user: The active user to know his rights for database permissions
     * @param equipment: The equipment concerned by the changement
     * @param date: Date of the last maintenance
     * @throws SQLException: If the SQL query is invalid
     */
    public static void updateLastMaintenance(User user, Equipment equipment, String date) throws SQLException {
        user.getUserConnection().setAutoCommit(true);

        String query = "UPDATE test.equipment SET LastMaintenance = ?, NeedMaintenance = '0' WHERE ENum = ?;";

        PreparedStatement updateEquipmentMaintenances = user.getUserConnection().prepareStatement(query);
        updateEquipmentMaintenances.setString(1, date);
        updateEquipmentMaintenances.setInt(2, equipment.getEquipmentId());

        updateEquipmentMaintenances.execute();
    }

    /**
     * Update the user privileges in the user table
     * @param user: The active user to know his rights for database permissions
     * @param userToGrant: User to change privileges in the table
     * @param privileges: New privilege state for the user given
     * @throws SQLException: If the SQL query is invalid
     */
    public static void updateUserPrivileges(User user, User userToGrant, String privileges) throws SQLException {
        user.getUserConnection().setAutoCommit(true);

        String query = "UPDATE test.user SET Privileges = ? WHERE Login = ?;";

        PreparedStatement grantUser = user.getUserConnection().prepareStatement(query);
        grantUser.setString(1, privileges);
        grantUser.setString(2, userToGrant.getUserLogin());

        grantUser.execute();
    }

    /**
     * Mark the user in the room
     * @param user: The active user to know his privileges and change his status
     * @throws NotConnectedException If the connection to the database fails
     */
    public static void roomAccess(User user) throws NotConnectedException {
        try {
            user.getUserConnection().setAutoCommit(true);
            String query = "UPDATE test.user SET InRoom = '1' WHERE Login = ?;";

            PreparedStatement roomAccess = user.getUserConnection().prepareStatement(query);
            roomAccess.setString(1, user.getUserLogin());

            roomAccess.execute();
        } catch (SQLException e) {
            throw new NotConnectedException();
        }
    }

    /**
     * Mark the user out the room
     * @param user: The active user to know his privileges and change his status
     * @throws NotConnectedException If the connection to the database fails
     */
    public static void roomExit(User user) throws NotConnectedException {
        try {
            user.getUserConnection().setAutoCommit(true);
            String query = "UPDATE test.user SET InRoom = '0' WHERE Login = ?;";

            PreparedStatement roomExit = user.getUserConnection().prepareStatement(query);
            roomExit.setString(1, user.getUserLogin());

            roomExit.execute();

        } catch (SQLException e) {
            throw new NotConnectedException();
        }
    }

}
