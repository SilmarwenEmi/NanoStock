package DataBase;

import NanostockException.NotConnectedException;
import WindowDisplay.*;

import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Objects;

import static DataBase.Connect.*;

/**
 * @author Emilie Laurent
 * Contains all the methods for extracting data from the database
 */
public class ExtractData {
    static private Connection connection;

    //Locations

    /**
     * Extract all locations with all information about it
     * @param frame: Represent the main frame of the program
     * @param sm: Represent the active StockMangement object
     * @param whatKind: To know what kind of item is in the location
     * @param user: The active user to know his rights for database permissions
     * @param words: The couple of words we have to find to display item
     * @return: A list containing all the locations
     * @throws NotConnectedException: If connection to database for user fails (wrong login/password or database not accessible)
     * @throws SQLException: If the SQL query is invalid
     */
    public static ArrayList<Location> extractAllLocations(JFrame frame, StockMangement sm, String whatKind, User user, String words) throws NotConnectedException, SQLException {

        ArrayList<Location> list = new ArrayList<Location>();

        try {
            String query = "SELECT test.location.LName, test.location.LImage FROM test.location ";
            query += " GROUP BY test.location.LName;";

            PreparedStatement locationsQuery = user.getUserConnection().prepareStatement(query);

            //Send prepared query to database
            ResultSet result = locationsQuery.executeQuery();

            //Get all the locations where there are products
            while (result.next()) {
                String imagePath = result.getString("LImage");
                String name = result.getString("LName");

                Location location = new Location(frame, name, imagePath, sm, whatKind, user, words);
                list.add(location);
            }

            result.close();

        } catch (Exception e) {
            throw new SQLException("Wrong SQL query");
        }

        return list;

    }

    /**
     * Extract all locations name only
     * @param user: The active user to know his rights for database permissions
     * @return: A list containing all locations name
     * @throws NotConnectedException: If connection to database for user fails (wrong login/password or database not accessible)
     * @throws SQLException: If the SQL query is invalid
     */
    public static ArrayList<String> extractAllLocationsName(User user) throws NotConnectedException, SQLException {

        ArrayList<String> locationsList = new ArrayList<>();

        try {
            String query = "SELECT test.location.LName FROM test.location ";
            query += " GROUP BY test.location.LName;";

            PreparedStatement locationsProductsQuery = user.getUserConnection().prepareStatement(query);

            //Send prepared query to database
            ResultSet result = locationsProductsQuery.executeQuery();

            //Get all the locations where there are products
            while (result.next()) {
                String locationName = result.getString("LName");
                locationsList.add(locationName);

            }

            result.close();

        } catch (Exception e) {
            throw new SQLException("Wrong SQL query");
        }

        return locationsList;

    }

    //Suppliers

    /**
     * Extract all suppliers with all information about it
     * @param user: The active user to know his rights for database permissions
     * @return: A list containing all suppliers
     * @throws SQLException: If the SQL query is invalid
     */
    public static ArrayList<Supplier> extractAllSuppliers(User user) throws SQLException {

        ArrayList<Supplier> suppliersList = new ArrayList<>();

        try {
            String query = "SELECT test.supplierinfo.* FROM test.supplierinfo ";
            query += " GROUP BY SupplierName;";

            PreparedStatement locationsProductsQuery = user.getUserConnection().prepareStatement(query);

            //Send prepared query to database
            ResultSet result = locationsProductsQuery.executeQuery();

            //Get all the locations where there are products
            while (result.next()) {
                String supplier = result.getString("SupplierName");
                String contactName = result.getString("ContactName");
                String contactPhone = result.getString("ContactPhoneNumber");

                Supplier supplierInfo = new Supplier(supplier, contactName, contactPhone);
                suppliersList.add(supplierInfo);

            }

            result.close();

        } catch (Exception e) {
            throw new SQLException("Wrong SQL query");
        }

        return suppliersList;
    }


    //Products

    /**
     * Extract all products with all needed information
     * @param frame: Represent the main frame of the program
     * @param location: Name of the location of which we're searching products
     * @param user: The active user to know his rights for database permissions
     * @param sm: Represent the active StockMangement object
     * @param words: The couple of words we have to find to display item
     * @return: A list containing all product from one location
     * @throws NotConnectedException: If connection to database for user fails (wrong login/password or database not accessible)
     * @throws SQLException: If the SQL query is invalid
     */
    public static ArrayList<Product> extractAllProductsFromLocation(JFrame frame, String location, User user, StockMangement sm, String words) throws NotConnectedException, SQLException, Exception {

        ArrayList<Product> list = new ArrayList<>();

        try {
            //Prepare query for sending to database
            String query;

            if (words.length() != 0){
                query = "SELECT * FROM test.product WHERE LName = ? and PName like ? ;";
            } else {
                query = "SELECT * FROM test.product WHERE LName = ? ;";
            }

            PreparedStatement searchProductFromOneLocation = user.getUserConnection().prepareStatement(query);
            searchProductFromOneLocation.setString(1, location);

            if (words.length() != 0){
                searchProductFromOneLocation.setString(2, "%" + words + "%" );
            }

            //Send prepared query to database
            ResultSet result = searchProductFromOneLocation.executeQuery();


            //Get all the products
            while (result.next()) {
                int numberId = result.getInt("PNum");
                String lotNumber = result.getString("PLotNumber");
                String expirationDate = result.getString("PExpirationDate");
                int quantity = result.getInt("PQuantity");
                String productName = result.getString("PName");
                String category = result.getString("Category");
                String description = result.getString("Description");
                String CASNumber = result.getString("CASNumber");
                String supplier = result.getString("Supplier");

                //Create the product with data extracted from database
                Product product = new Product(frame, numberId, lotNumber, expirationDate, quantity, productName, description, CASNumber, category, user, supplier, sm, location);

                list.add(product);
            }

            result.close();

        } catch (SQLException e) {
            throw new SQLException("Wrong SQL query product");
        }

        return list;

    }

    /**
     * Extract all products name only
     * @param user: The active user to know his rights for database permissions
     * @return: A list containing all products names
     * @throws NotConnectedException: If connection to database for user fails (wrong login/password or database not accessible)
     * @throws SQLException: If the SQL query is invalid
     */
    public static ArrayList<String> extractProductsNames(User user) throws NotConnectedException, SQLException {

        ArrayList<String> productsNames = new ArrayList<>();

        connection = user.getUserConnection();

        try {
            //Prepare query for sending to database
            String query = "SELECT test.product.PName FROM test.product ";
            query += "GROUP BY PName ;";

            PreparedStatement extractAccessoryCategory = connection.prepareStatement(query);

            //Send prepared query to database
            ResultSet result = extractAccessoryCategory.executeQuery();

            while (result.next()) {
                String name = result.getString("PName");

                productsNames.add(name);
            }

        } catch (SQLException e) {
            throw new SQLException("Wrong SQL query");
        }

        return productsNames;
    }

    /**
     * Extract all products categories only
     * @param user: The active user to know his rights for database permissions
     * @return: A list containing all products categories
     * @throws NotConnectedException: If connection to database for user fails (wrong login/password or database not accessible)
     * @throws SQLException: If the SQL query is invalid
     */
    public static ArrayList<String> extractProductsCategories(User user) throws NotConnectedException, SQLException {

        ArrayList<String> productsCategories = new ArrayList<>();

        try {
            //Prepare query for sending to database
            String query = "SELECT test.product.Category FROM test.product ";
            query += "GROUP BY Category ;";

            PreparedStatement extractAccessoryCategory = user.getUserConnection().prepareStatement(query);

            //Send prepared query to database
            ResultSet result = extractAccessoryCategory.executeQuery();

            while (result.next()) {
                String cat = result.getString("Category");

                productsCategories.add(cat);
            }

        } catch (SQLException e) {
            throw new SQLException("Wrong SQL query");
        }

        return productsCategories;
    }

    /**
     * Extract only one product with all information
     * @param frame: Represent the main frame of the program
     * @param user: The active user to know his rights for database permissions
     * @param pNum: Product identification number in the database
     * @param sm: Represent the active StockMangement object
     * @return: The product corresponding to the pNum (product number)
     * @throws SQLException: If the SQL query is invalid
     */
    public static Product extractOneProduct(JFrame frame, User user, int pNum, StockMangement sm) throws SQLException {

        Product product = new Product();

        //Prepare query for sending to database
        String query = "SELECT * FROM test.product WHERE PNum = ?;";

        PreparedStatement extractOneProduct = user.getUserConnection().prepareStatement(query);
        extractOneProduct.setInt(1,pNum);

        //Send prepared query to database
        ResultSet result = extractOneProduct.executeQuery();

        while(result.next()){
            int productId = result.getInt("PNum");
            String lot = result.getString("PLotNumber");
            String expirationDate = result.getString("PExpirationDate");
            int quantity = result.getInt("PQuantity");
            String cat = result.getString("Category");
            String location = result.getString("LName");
            String productName = result.getString("PName");
            String cas = result.getString("CASNumber");
            String description = result.getString("Description");
            String supplier = result.getString("Supplier");

            product = new Product(frame, productId, lot, expirationDate, quantity, productName, description, cas, cat, user, supplier, sm, location);
        }

    return product;

    }


    //Equipments

    /**
     * Extract all equipment in only one given location
     * @param frame: Represent the main frame of the program
     * @param location: Name of the location of which we have to find all the equipments
     * @param user: The active user to know his rights for database permissions
     * @param sm: Represent the active StockMangement object
     * @param words: The couple of words we have to find to display item
     * @param defaultPicture: The defaultPicture of the equipment
     * @return: A list containing all the equipments inside this location
     * @throws NotConnectedException: If connection to database for user fails (wrong login/password or database not accessible)
     * @throws SQLException: If the SQL query is invalid
     */
    public static ArrayList<Equipment> extractAllEquipmentsFromLocation(JFrame frame, String location, User user, StockMangement sm,
                                                                        String words, ImageIcon defaultPicture)
            throws NotConnectedException, SQLException {

        ArrayList<Equipment> listEquipment = new ArrayList<>();

        try {

            //Prepare query for sending to database
            String query;

            if (words.length() != 0){
                query = "SELECT * FROM test.equipment, test.supplierinfo WHERE test.equipment.Supplier = test.supplierinfo.SupplierName " +
                        "and test.equipment.LName=? and EName like ? ;";
            } else {
                query = "SELECT * FROM test.equipment, test.supplierinfo ";
                query += "WHERE test.equipment.Supplier = test.supplierinfo.SupplierName and test.equipment.LName=?;";
            }

            PreparedStatement searchEquipmentsFromOneLocation = user.getUserConnection().prepareStatement(query);
            searchEquipmentsFromOneLocation.setString(1, location);
            if (words.length() != 0){
                searchEquipmentsFromOneLocation.setString(2,"%" + words + "%");
            }

            //Send prepared query to database
            ResultSet result = searchEquipmentsFromOneLocation.executeQuery();

            while (result.next()) {

                int numberId = result.getInt("ENum");
                String acquisitionYear = result.getString("EAcquisitionYear");
                //String graphPath = result.getString("EGraph");
                String maintenanceDates = result.getString("MaintenancesDates");
                String category = result.getString("Category");
                String name = result.getString("EName");
                String description = result.getString("Description");
                String serialNumber = result.getString("SerialNumber");
                String usageTime = result.getString("EUsageTime");
                String supplierInfo = result.getString("Supplier");
                String supplierName = result.getString("ContactName");
                String supplierPhone = result.getString("ContactPhoneNumber");

                Supplier supplier = new Supplier(supplierInfo, supplierName, supplierPhone);

                //Create the product with data extracted from database and set default picture if not given by software user
                Equipment equipment;
                ImageIcon picture;
                byte[] imageBytes = result.getBytes ("EPicture");

                if (imageBytes.length == 0){
                    equipment = new Equipment(frame, name, acquisitionYear, category, description, usageTime,
                            serialNumber, user, supplier, sm, numberId, maintenanceDates, location, defaultPicture);
                } else {
                    picture = new ImageIcon(imageBytes);
                    equipment = new Equipment(frame, name, acquisitionYear, category, description, usageTime,
                            serialNumber, user, supplier, sm, numberId, maintenanceDates, location, picture);
                }

                listEquipment.add(equipment);
            }


            result.close();

        } catch (SQLException e) {
            throw new SQLException("Wrong SQL query equipment");
        }

        return listEquipment;

    }


    /**
     * Extract all equipment categories only
     * @param user: The active user to know his rights for database permissions
     * @return: A list containing all equipments categories
     * @throws NotConnectedException: If connection to database for user fails (wrong login/password or database not accessible)
     * @throws SQLException: If the SQL query is invalid
     */
    public static ArrayList<String> extractEquipmentsCategories(User user) throws NotConnectedException, SQLException {
        ArrayList<String> equipmentsCategories = new ArrayList<>();

        connection = user.getUserConnection();

        try {
            //Prepare query for sending to database
            String query = "SELECT test.equipment.Category FROM test.equipment ";
            query += "GROUP BY Category ;";

            PreparedStatement extractEquipmentCategory = connection.prepareStatement(query);

            //Send prepared query to database
            ResultSet result = extractEquipmentCategory.executeQuery();

            while (result.next()) {
                String cat = result.getString("Category");

                equipmentsCategories.add(cat);
            }


        } catch (SQLException e) {
            throw new SQLException("Wrong SQL query");
        }

        return equipmentsCategories;

    }

    /**
     * Extract only one equipment
     * @param frame: Represent the main frame of the program
     * @param user: The active user to know his rights for database permissions
     * @param eNum: Equipment identification number of the database
     * @param sm: Represent the active StockMangement object
     * @return: All the information of the equipment having eNum
     * @throws SQLException: If the SQL query is invalid
     */
    public static Equipment extractOneEquipement(JFrame frame, User user, int eNum, StockMangement sm) throws SQLException {

        Equipment equipment = new Equipment();

        //Prepare query for sending to database
        String query = "SELECT * FROM test.equipment WHERE ENum = ? ;";

        PreparedStatement extractOneEquipment = user.getUserConnection().prepareStatement(query);
        extractOneEquipment.setInt(1,eNum);

        //Send prepared query to database
        ResultSet result = extractOneEquipment.executeQuery();

        while(result.next()){
            int equipmentId = result.getInt("ENum");
            String year = result.getString("EAcquisitionYear");
            String usageTime = result.getString("EUsageTime");
            String location = result.getString("LName");
            String name = result.getString("EName");
            String description = result.getString("Description");
            String serialNumber = result.getString("SerialNumber");
            String cat = result.getString("Category");
            String maintenances = result.getString("MaintenancesDates");
            String supplier = result.getString("Supplier");


            byte[] imageBytes = result.getBytes ("EPicture");
            ImageIcon picture = new ImageIcon(imageBytes);

            Supplier supplierInfo = new Supplier(supplier, "", "");

            equipment = new Equipment(frame, name, year, cat, description, usageTime, serialNumber, user, supplierInfo, sm, equipmentId, maintenances, location, picture);
        }

        return equipment;
    }

    //Accessories

    /**
     * Extract all accessories information from one given location
     * @param frame: Represent the main frame of the program
     * @param location: Name of the location of which we have to search all accessories
     * @param user: The active user to know his rights for database permissions
     * @param sm: Represent the active StockMangement object
     * @param words: The couple of words we have to find to display item
     * @return A list of all accessories inside the location
     * @throws NotConnectedException: If connection to database for user fails (wrong login/password or database not accessible)
     * @throws SQLException: If the SQL query is invalid
     */
    public static ArrayList<Accessory> extractAllAccessoriesFromLocation(JFrame frame, String location, User user, StockMangement sm, String words)
            throws NotConnectedException, SQLException {

        ArrayList<Accessory> listAccessory = new ArrayList<>();


        //Prepare query for sending to database
        String query;

        if (words.length() != 0){
            query = "SELECT * FROM test.accessory WHERE LName = ? and AName like ? ;";
        } else {
            query = "SELECT * FROM test.accessory WHERE LName = ? ;";
        }


        PreparedStatement searchAccessoryFromOneLocation = user.getUserConnection().prepareStatement(query);
        searchAccessoryFromOneLocation.setString(1, location);

        if (words.length() != 0){
            searchAccessoryFromOneLocation.setString(2, "%" + words + "%");
        }

        //Send prepared query to database
        ResultSet result = searchAccessoryFromOneLocation.executeQuery();

        //Get all the products
        while (result.next()) {

            int numberId = result.getInt("ANum");
            String lot = result.getString("ALotNumber");
            String expirationDate = result.getString("AExpirationDate");
            int quantity = result.getInt("AQuantity");
            String category = result.getString("Category");
            String name = result.getString("AName");
            String description = result.getString("Description");
            String supplier = result.getString("Supplier");

            //Create the product with data extracted from database
            Accessory accessory = new Accessory(frame, name, category, quantity, lot, expirationDate, description,
                    user, supplier, numberId, sm, "");

            listAccessory.add(accessory);
        }

        result.close();


    return listAccessory;

    }


    /**
     * Extract all accessories categories only
     * @param user: The active user to know his rights for database permissions
     * @return A list containing all categories of the accessories
     * @throws NotConnectedException: If connection to database for user fails (wrong login/password or database not accessible)
     * @throws SQLException: If the SQL query is invalid
     */
    public static ArrayList<String> extractAccessoriesCategories(User user) throws NotConnectedException, SQLException {
        ArrayList<String> accessoriesCategories = new ArrayList<>();

        try {
            //Prepare query for sending to database
            String query = "SELECT test.accessory.Category FROM test.accessory ";
            query += "GROUP BY Category ;";

            PreparedStatement extractAccessoryCategory = user.getUserConnection().prepareStatement(query);

            //Send prepared query to database
            ResultSet result = extractAccessoryCategory.executeQuery();

            while (result.next()) {
                String cat = result.getString("Category");

                accessoriesCategories.add(cat);
            }


        } catch (SQLException e) {
            throw new SQLException("Wrong SQL query");
        }

        return accessoriesCategories;

    }


    /**
     * Extract all accessories names only
     * @param user: The active user to know his rights for database permissions
     * @return A list containing all accessories names
     * @throws NotConnectedException: If connection to database for user fails (wrong login/password or database not accessible)
     * @throws SQLException: If the SQL query is invalid
     */
    public static ArrayList<String> extractAccessoriesNames(User user) throws SQLException, NotConnectedException {
        ArrayList<String> accessoriesNames = new ArrayList<>();

        //Prepare query for sending to database
        String query = "SELECT test.accessory.AName FROM test.accessory ";
        query += "GROUP BY AName ;";

        PreparedStatement extractAccessoryCategory = user.getUserConnection().prepareStatement(query);

        //Send prepared query to database
        ResultSet result = extractAccessoryCategory.executeQuery();

        while (result.next()) {
            String name = result.getString("AName");
            accessoriesNames.add(name);
        }

        return accessoriesNames;
    }


    /**
     * Extract all user information
     * @param userLogin: Login of the user to connect to the database
     * @param userPassword: Password of the user to connect to the database
     * @param defaultPicture: Default picture for the user
     * @param connection: All information for database connection
     * @return All information of the user
     * @throws NotConnectedException: If connection to database for user fails (wrong login/password or database not accessible)
     * @throws SQLException: If the SQL query is invalid
     */
    public static User extractUserData(String userLogin, String userPassword, ImageIcon defaultPicture, Connection connection) throws NotConnectedException, SQLException {

        User user = null;

        //Prepare query for sending to database
        String query = "SELECT * FROM test.user";
        query += " WHERE test.user.login = ?;";

        PreparedStatement searchUserData = connection.prepareStatement(query);
        searchUserData.setString(1, userLogin);

        //Send prepared query to database
        ResultSet result = searchUserData.executeQuery();

        //Get all the products
        while (result.next()) {
            String name = result.getString("Name");
            String firstName = result.getString("FirstName");
            String login = result.getString("Login");
            String mail = result.getString("Email");
            boolean inRoom = result.getBoolean("InRoom");
            String privileges = result.getString("Privileges");

            //Extract user picture
            ImageIcon picture;
            byte[] imageBytes = result.getBytes ("UPicture");
            if (imageBytes == null){
                user = new User(login, userPassword, name, firstName, mail, connection, privileges, defaultPicture, inRoom);
            } else {
                picture = new ImageIcon(imageBytes);
                user = new User(login, userPassword, name, firstName, mail, connection, privileges, picture, inRoom);
            }

        }

        result.close();

        return user;
    }

    /**
     * Refresh the user picture
     * @param user: : The active user to know his rights for database permissions
     * @throws SQLException: If the SQL query is invalid
     */
    public static void refreshUserPicture(User user) throws SQLException {

        //Prepare query for sending to database
        String query = "SELECT * FROM test.user";
        query += " WHERE test.user.login = ?;";

        PreparedStatement searchUserData = user.getUserConnection().prepareStatement(query);
        searchUserData.setString(1, user.getUserLogin());

        //Send prepared query to database
        ResultSet result = searchUserData.executeQuery();

        //Extract user picture
        while (result.next()) {
            ImageIcon picture;
            byte[] imageBytes = result.getBytes ("UPicture");
            picture = new ImageIcon(imageBytes);
            user.setUserPicture(picture);

        }

        result.close();

    }

    /**
     * Extract all users information
     * @param user: The active user to know his rights for database permissions
     * @return A list containing all the users
     * @throws NotConnectedException: If connection to database for user fails (wrong login/password or database not accessible)
     * @throws SQLException: If the SQL query is invalid
     */
    public static ArrayList<User> extractAllUsers(User user) throws NotConnectedException, SQLException {

        ArrayList<User> userList = new ArrayList<>();

        //Prepare query for sending to database
        String query = "SELECT * FROM test.user";

        PreparedStatement searchUserData = user.getUserConnection().prepareStatement(query);
        user.setUserConnection(user.getUserConnection());

        //Send prepared query to database
        ResultSet result = searchUserData.executeQuery();

        //Get all the products
        while (result.next()) {
            String name = result.getString("Name");
            String firstName = result.getString("FirstName");
            String login = result.getString("Login");
            String mail = result.getString("Email");
            String privileges = result.getString("Privileges");

            //Create the user with data extracted from database
            User userInfo = new User(login, null, name, firstName, mail, null, privileges, null, false);
            userList.add(userInfo);
        }

        result.close();

        return userList;
    }

    /**
     * Extract all products alerts
     * @param user: The active user to know his rights for database permissions
     * @param frame: Represent the main frame of the program
     * @param sm: Represent the active StockMangement object
     * @return A list containing all new products alerts (alerts not archived) information
     * @throws SQLException: If the SQL query is invalid
     */
    public static ArrayList<AlertLine> extractAllNewAlerts(User user, JFrame frame, StockMangement sm) throws SQLException {

        ArrayList<AlertLine> newAlertsList = new ArrayList<>();

        //Prepare query for sending to database
        String query = "SELECT AId, ADate, ATag, PNum, ENum FROM test.alert WHERE Archived = '0' GROUP BY AId, ATag, PNum, ENum" +
                " ORDER BY ADate desc;";

        PreparedStatement searchNewAlertsData = user.getUserConnection().prepareStatement(query);
        user.setUserConnection(user.getUserConnection());

        //Send prepared query to database
        ResultSet result = searchNewAlertsData.executeQuery();

        if(result == null) {
            return newAlertsList;
        }

        //Get all the products
        while (result.next()) {
            String alertId = result.getString("AId");
            String date = result.getString("ADate");
            String tag = result.getString("ATag");
            int PNum = result.getInt("PNum");
            int ENum = result.getInt("ENum");

            String formattedAlertId = "";
            if (alertId.length() == 1){
                formattedAlertId = "000" + alertId + " ";
            } else if (alertId.length() == 2){
                formattedAlertId = "00" + alertId + " ";
            } else if (alertId.length() == 3){
                formattedAlertId = "0" + alertId + " ";
            }

            if(Objects.equals(tag, "outdatedProduct") || Objects.equals(tag, "lowQuantity")){
                AlertLine newAlert = new AlertLine(frame, user, PNum, date, tag, formattedAlertId, sm, "product");
                newAlertsList.add(newAlert);
            } else if (Objects.equals(tag, "maintenanceRequired")){
                AlertLine newAlert = new AlertLine(frame, user, ENum, date, tag, formattedAlertId, sm, "equipment");
                newAlertsList.add(newAlert);
            }
        }

        return newAlertsList;

    }

    /**
     * Extract all archived alerts
     * @param user: The active user to know his rights for database permissions
     * @param frame: Represent the main frame of the program
     * @param sm: Represent the active StockMangement object
     * @return A list containing all archived products alerts information
     * @throws SQLException: If the SQL query is invalid
     */
    public static ArrayList<ArchivedAlertLine> extractAllArchivedAlerts(User user, JFrame frame, StockMangement sm) throws SQLException {
        ArrayList<ArchivedAlertLine> archivedAlertList = new ArrayList<>();

        //Prepare query for sending to database
        String query = "SELECT AId, ADate, ATag, PNum, ENum FROM test.alert WHERE Archived = '1' GROUP BY AId, ATag, PNum, ENum" +
                " ORDER BY ADate desc;";

        PreparedStatement searchArchivedAlertsData = user.getUserConnection().prepareStatement(query);
        user.setUserConnection(user.getUserConnection());

        //Send prepared query to database
        ResultSet result = searchArchivedAlertsData.executeQuery();

        if(result == null) {
            return archivedAlertList;
        }

        //Get all the products
        while (result.next()) {
            String alertId = result.getString("AId");
            String date = result.getString("ADate");
            String tag = result.getString("ATag");
            int PNum = result.getInt("PNum");
            int ENum = result.getInt("ENum");

            String formattedAlertId = "";
            if (alertId.length() == 1){
                formattedAlertId = "000" + alertId + " ";
            } else if (alertId.length() == 2){
                formattedAlertId = "00" + alertId + " ";
            } else if (alertId.length() == 3){
                formattedAlertId = "0" + alertId + " ";
            }

            if(Objects.equals(tag, "lowQuantity") || Objects.equals(tag, "outdatedProduct")){
                ArchivedAlertLine archivedAlert = new ArchivedAlertLine(frame, user, PNum, date, tag, formattedAlertId, sm, "product");
                archivedAlertList.add(archivedAlert);
            } else if (Objects.equals(tag, "maintenanceRequired")){
                ArchivedAlertLine archivedAlert = new ArchivedAlertLine(frame, user, ENum, date, tag, formattedAlertId, sm, "equipment");
                archivedAlertList.add(archivedAlert);
            }
        }

        return archivedAlertList;
    }


    /**
     * Extract all log contained in database
     * @param user: The active user to know his rights for database permissions
     * @return A list containing all log
     * @throws SQLException: If the SQL query is invalid
     */
    public static ArrayList<LogLine> extractAllLog(User user) throws SQLException {

        ArrayList<LogLine> logList = new ArrayList<>();

        //Prepare query for sending to database
        String query = "SELECT * FROM test.log ORDER BY LDate asc;";

        PreparedStatement searchAllLog = user.getUserConnection().prepareStatement(query);

        //Send prepared query to database
        ResultSet result = searchAllLog.executeQuery();

        //Get all the products
        while (result.next()) {
            String description = result.getString("LDescription");
            String type = result.getString("LType");
            String date = result.getString("LDate");
            String time = result.getString("LTime");
            String login = result.getString("Login");

            LogLine log = new LogLine(description, type, date, time, login);
            logList.add(log);
        }


        return logList;

    }

    /**
     * Count the number of rows for equipment table in the database
     * @param user: The active user to know his rights for database permissions
     * @return The number of rows
     * @throws SQLException: If the SQL query is invalid
     */
    public static int countRowsEquipment(User user) throws SQLException {
        int rowsNumber = 0;

        String query = "SELECT count(*) as NumberOfRows FROM test.equipment;";

        PreparedStatement countEquipmentRows = user.getUserConnection().prepareStatement(query);

        ResultSet result = countEquipmentRows.executeQuery();

        while (result.next()) {
            rowsNumber = result.getInt("NumberOfRows");
        }

        return rowsNumber;
    }

    /**
     * Count the number of rows for product table in the database
     * @param user: The active user to know his rights for database permissions
     * @return The number of rows
     * @throws SQLException: If the SQL query is invalid
     */
    public static int countRowsProduct(User user) throws SQLException {
        int rowsNumber = 0;

        String query = "SELECT count(*) as NumberOfRows FROM test.product;";

        PreparedStatement countProductRows = user.getUserConnection().prepareStatement(query);

        ResultSet result = countProductRows.executeQuery();

        while (result.next()) {
            rowsNumber = result.getInt("NumberOfRows");
        }

        return rowsNumber;
    }

    /**
     * Count the number of rows for log table in the database
     * @param user: The active user to know his rights for database permissions
     * @return The number of rows
     * @throws SQLException: If the SQL query is invalid
     */
    public static int countRowsLogTrail(User user) throws SQLException {
        int rowsNumber = 0;

        String query = "SELECT count(*) as NumberOfRows FROM test.log;";

        PreparedStatement countLogRows = user.getUserConnection().prepareStatement(query);

        ResultSet result = countLogRows.executeQuery();

        while (result.next()) {
            rowsNumber = result.getInt("NumberOfRows");
        }

        return rowsNumber;
    }

    /**
     * Count the number of rows for accessory table in the database
     * @param user: The active user to know his rights for database permissions
     * @return The number of rows
     * @throws SQLException: If the SQL query is invalid
     */
    public static int countRowsAccessory(User user) throws SQLException {
        int rowsNumber = 0;

        String query = "SELECT count(*) as NumberOfRows FROM test.accessory;";

        PreparedStatement countAccessoryRows = user.getUserConnection().prepareStatement(query);

        ResultSet result = countAccessoryRows.executeQuery();

        while (result.next()) {
            rowsNumber = result.getInt("NumberOfRows");
        }

        return rowsNumber;
    }

    /**
     * Count the number of rows for alert table in the database
     * @param user: The active user to know his rights for database permissions
     * @param archived: 0 for non-archived alerts, 1 for archived alerts
     * @return The number of rows in alerts (archived or non-archived) table
     * @throws SQLException: If the SQL query is invalid
     */
    public static int countRowsAlerts(User user, int archived) throws SQLException {
        int rowsNumber = 0;

        String query = "SELECT count(*) as NumberOfRows FROM test.alert WHERE Archived = ?;";

        PreparedStatement countAlertsRows = user.getUserConnection().prepareStatement(query);
        countAlertsRows.setInt(1, archived);

        ResultSet result = countAlertsRows.executeQuery();

        while (result.next()) {
            rowsNumber = result.getInt("NumberOfRows");
        }

        return rowsNumber;
    }

    /**
     * Count the number of rows for supplier table in the database
     * @param user: The active user to know his rights for database permissions
     * @return The number of rows in supplier table
     * @throws SQLException: If the SQL query is invalid
     */
    public static int countRowsSupplier(User user) throws SQLException {
        int rowsNumber = 0;

        String query = "SELECT count(*) as NumberOfRows FROM test.supplierinfo;";

        PreparedStatement countSupplierRows = user.getUserConnection().prepareStatement(query);

        ResultSet result = countSupplierRows.executeQuery();

        while (result.next()) {
            rowsNumber = result.getInt("NumberOfRows");
        }

        return rowsNumber;
    }

    /**
     * Count the number of rows for user table in the database
     * @param user: The active user to know his rights for database permissions
     * @return The number of rows in user table
     * @throws SQLException: If the SQL query is invalid
     */
    public static int countRowsUser(User user) throws SQLException {

        int rowsNumber = 0;

        String query = "SELECT count(*) as NumberOfRows FROM test.user;";


        PreparedStatement countUserRows = user.getUserConnection().prepareStatement(query);

        ResultSet result = countUserRows.executeQuery();

        while (result.next()) {
            rowsNumber = result.getInt("NumberOfRows");
        }

        return rowsNumber;

    }

    /**
     * Count the number of rows for location table in the database
     * @param user: The active user to know his rights for database permissions
     * @return The number of rows in location table
     * @throws SQLException: If the SQL query is invalid
     */
    public static int countRowsLocation(User user) throws SQLException {

        int rowsNumber = 0;

        String query = "SELECT count(*) as NumberOfRows FROM test.location;";

        PreparedStatement countLocationRows = user.getUserConnection().prepareStatement(query);

        ResultSet result = countLocationRows.executeQuery();

        while (result.next()) {
            rowsNumber = result.getInt("NumberOfRows");
        }

        return rowsNumber;

    }

}
