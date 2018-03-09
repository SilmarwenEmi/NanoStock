package WindowDisplay;

import NanostockException.NotConnectedException;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.DateFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.CodeSource;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static DataBase.ExtractData.countRowsAlerts;

/**
 * @author Emilie Laurent
 * This class represent the admin page of the software
 */
public class Admin {
    private JPanel rootpanelAdmin;
    private JPanel navigatorPanel;
    private JPanel collapsedBar;
    private JPanel adminPrivilegesPanel;

    private JButton logoutButton;
    private JButton alertButton;
    private JButton userIDButton;
    private JButton switchToStock;
    private JButton switchToAlerts;
    private JButton switchToLog;
    private JButton collapseButton;
    private JButton expandButton;
    private JButton createNewEquipmentButton;
    private JButton cancelNewEquipmentButton;
    private JButton newCategoryEquipmentButton;
    private JButton addNewAccessoryButton;
    private JButton addNewCategoryAccessories;
    private JButton addNewAccessoryLocationButton;
    private JButton createNewAccessoryButton;
    private JButton addNewProductButton;
    private JButton addNewProductLocationButton;
    private JButton addCategoryProductsButton;
    private JButton cancelNewProductButton;
    private JButton createNewProductButton;
    private JButton addNewSupplierEquipmentButton;
    private JButton addNewAccessorySupplierButton;
    private JButton addNewProductSupplierButton;
    private JButton addNewEquipmentLocationButton;
    private JButton cancelNewAccessoryButton;
    private JButton returnHomeButton;
    private JButton addNewUserButton;
    private JButton addEquipmentPictureButton;

    private JLabel allFieldNotCompleted;
    private JLabel allAccessoryFieldsNotCompleted;
    private JLabel allProductFieldsNotCompleted;
    private JPanel userNamePanel;
    private JPanel userFisrtNamePanel;
    private JPanel userLoginPanel;
    private JPanel userMailPanel;
    private JPanel userDeleteButton;
    private JLabel pictureNewEquipment;

    private JEditorPane descriptionNewEquipment;
    private JEditorPane descriptionAccessory;

    private JTextField nameNewEquipment;
    private JTextField yearNewEquipment;
    private JTextField serialNumberNewEquipment;
    private JTextField lotAccessory;
    private JTextField lotProducts;
    private JTextField casProducts;

    private JComboBox<String> categoryNewEquipment;
    private JComboBox<String> locationNewEquipment;
    private JComboBox<String> nameAccessories;
    private JComboBox<String> categoryAccessories;
    private JComboBox<String> locationAccessories;
    private JComboBox<String> nameProducts;
    private JComboBox<String> categoryProducts;
    private JComboBox<String> locationProducts;
    private JComboBox<String> supplierProduct;
    private JComboBox<String> supplierNewEquipment;
    private JComboBox<String> supplierNewAccessory;

    private JSpinner quantitySpinner;
    private JSpinner quantityAccessory;

    private JEditorPane descriptionProduct;

    private JFormattedTextField expirationDateProducts;
    private JFormattedTextField expirationDateAccessory;

    private File selectedFile;

    /**
     * Construct the admin object with information given
     * @param frame: Represent the main frame of the software
     * @param user: The active user to know his previleges
     */
    public Admin(JFrame frame, User user) {

        userIDButton.setText(user.getUserName() + " " + user.getUserFirstName());

        boolean isThereAlerts = false;
        try {
            isThereAlerts = countRowsAlerts(user, 0) > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if ( isThereAlerts ){
            ImageIcon newAlertsImage = new ImageIcon(this.getClass().getResource("/Pictures/newAlerts.png"));
            alertButton.setIcon(newAlertsImage);
        } else {
            ImageIcon noAlertsImage = new ImageIcon(this.getClass().getResource("/Pictures/alert.png"));
            alertButton.setIcon(noAlertsImage);
        }

        if (user.getUserPicture() != null){
            userIDButton.setIcon(ResizeImageFromIcon(user.getUserPicture(), 30, 30));
        } else {
            ImageIcon defaultPictureSmall = new ImageIcon(this.getClass().getResource("/Pictures/person_random.png"));
            userIDButton.setIcon(defaultPictureSmall);
        }

        //Add listener to buttons
        logoutButton.addActionListener(new ActionToDo("logout", frame,null,null, user, null, null, null, null, null));
        switchToStock.addActionListener(new ActionToDo("switchToStock", frame,null, null, user, null, null, null, null, null));
        switchToAlerts.addActionListener(new ActionToDo("switchToAlerts", frame,null, null, user, null, null, null, null, null));
        switchToLog.addActionListener(new ActionToDo("switchToLog", frame,null,null, user, null, null, null, null, null));
        alertButton.addActionListener(new ActionToDo("switchToAlerts", frame,null, null, user, null, null, null, null, null));
        userIDButton.addActionListener(new ActionToDo("switchToUserProfile",frame,null, null, user, null, null, null, null, null));
        cancelNewProductButton.addActionListener(new ActionToDo("switchToAdmin", frame, null,  null, user, null, null, null, null, null));
        cancelNewAccessoryButton.addActionListener(new ActionToDo("switchToAdmin", frame, null, null, user, null, null, null, null, null));
        cancelNewEquipmentButton.addActionListener(new ActionToDo("switchToAdmin", frame, null,  null, user, null, null, null, null, null));
        returnHomeButton.addActionListener(new ActionToDo("switchToWhatDoing", frame, null, null, user, null, null, null, null, null));

        //Add popup to buttons
        addNewEquipmentLocationButton.addActionListener(new ActionToDo("newLocation", frame, null, null, user, this, null, null, null, null));
        addNewAccessoryLocationButton.addActionListener(new ActionToDo("newLocation", frame, null, null, user, this, null, null, null, null));
        addNewProductLocationButton.addActionListener(new ActionToDo("newLocation", frame, null, null, user, this, null, null, null, null));

        newCategoryEquipmentButton.addActionListener(new ActionToDo("newEquipmentCategory", frame, null, null, user, this, null, null, null, null));
        addNewCategoryAccessories.addActionListener(new ActionToDo("newAccessoryCategory", frame, null, null, user, this, null, null, null, null));
        addCategoryProductsButton.addActionListener(new ActionToDo("newProductCategory", frame, null, null, user, this, null, null, null, null));

        addNewSupplierEquipmentButton.addActionListener(new ActionToDo("newSupplier", frame, null,null, user, this, null, null, null, null));
        addNewAccessorySupplierButton.addActionListener(new ActionToDo("newSupplier", frame, null, null, user, this, null, null, null, null));
        addNewProductSupplierButton.addActionListener(new ActionToDo("newSupplier", frame, null, null, user, this, null, null, null, null));

        addNewAccessoryButton.addActionListener(new ActionToDo("newAccessory", frame, null, null, user, this, null, null, null, null));

        addNewProductButton.addActionListener(new ActionToDo("newProductName", frame, null, null, user, this, null, null, null, null));

        addNewUserButton.addActionListener(new ActionToDo("newUser", frame, null,null, user, this, null, null, null, null));

        //Initialize JComboBoxes
        addLocationItem(user, this);
        addCategory(user, null, "equipment");
        addCategory(user, null, "product");
        addCategory(user, null, "accessory");
        addSupplierItem(user, this);
        addAccessory(user, null, this);
        addProduct(user, null, this);

        //Display users in add/remove user
        displayUsers(frame, user);

        //Add listeners to other buttons
        collapseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                collapsedBar.setVisible(true);
                navigatorPanel.setVisible(false);
            }
        });
        expandButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                collapsedBar.setVisible(false);
                navigatorPanel.setVisible(true);
            }
        });

        createNewProductButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Get values typed by user
                String name = getProductName();
                String category = getProductCategory();
                String location = getProductLocation();
                String expirationDate = getProductExpirationDate();
                int quantity = getProductQuantity();
                String lotNumber = getProductLotNumber();
                String cas = getProductCas();
                String description = getProductDescription();
                String supplier = getProductSupplier();

                if(name.length() != 0 && category.length() != 0 && location.length() != 0 && expirationDate.length() != 0 && lotNumber.length() != 0 && cas.length() != 0 && supplier.length() != 0){
                    try {
                        //Create product to insert into database
                        Product product = new Product();
                        product.setProductName(name);
                        product.setProductCategory(category);
                        product.setProductLocation(location);
                        product.setProductExpirationDate(expirationDate);
                        product.setProductQuantity(quantity);
                        product.setProductLot(lotNumber);
                        product.setProductCAS(cas);


                        if (description.length() != 0){
                            product.setProductDescription(description);
                        } else {
                            product.setProductDescription("");
                        }

                        product.setProductSupplier(supplier);

                        DataBase.InsertData.insertNewProduct(product, user);

                        JOptionPane.showMessageDialog(null, "The product " + name + " has been successfully added to the database",
                                "Product added", JOptionPane.INFORMATION_MESSAGE);

                        frame.setContentPane(new Admin(frame, user).getRootPanelAdmin());
                        frame.repaint();
                        frame.revalidate();

                        DataBase.InsertData.insertNewLog(user, "Product \" " + getProductName() + " \" added" , "ProductAdded");

                    } catch (SQLException e1) {
                        JOptionPane.showMessageDialog(null, "New product has not been created. " +
                                "An unhandled error occurs.", "Error occurs", JOptionPane.ERROR_MESSAGE);
                    }

                } else {
                    allProductFieldsNotCompleted.setVisible(true);
                    frame.repaint();
                    frame.revalidate();
                }

            }
        });
        createNewEquipmentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Get info typed by user
                String name = getEquipmentName();
                String category = getEquipmentCategory();
                String location = getEquipmentLocation();
                String year = getEquipmentYear();
                String serialNumber = getEquipmentSerialNumber();
                String supplier = getEquipmentSupplier();
                String description = getEquipmentDescription();

                FileInputStream fileInputStream = null;

                if (selectedFile != null){
                    try {
                        fileInputStream = new FileInputStream(selectedFile);

                    } catch (FileNotFoundException e1) {
                        JOptionPane.showMessageDialog(null, "File selected has not been found to " +
                                "be saved into database. " + "File not found.", "Error occurs", JOptionPane.ERROR_MESSAGE);
                    }
                }

                //Insert equipment information in database
                if (name.length() != 0 && category.length() != 0 && location.length() != 0 && year.length()!= 0 && serialNumber.length() != 0 && supplier.length() != 0 && fileInputStream != null){
                    try {
                        Equipment equipment = new Equipment();
                        equipment.setEquipmentsName(name);
                        equipment.setEquipmentsCategory(category);
                        equipment.setEquipmentLocation(location);
                        equipment.setEquipmentsYear(year);
                        equipment.setEquipmentsSerialNumber(serialNumber);
                        equipment.setEquipmentsSupplier(supplier);

                        if(description.length() != 0) {
                            equipment.setEquipmentsDescription(description);
                        } else {
                            equipment.setEquipmentsDescription("");
                        }

                        DataBase.InsertData.insertNewEquipment(equipment, fileInputStream, user);

                        JOptionPane.showMessageDialog(null, "The equipment " + name + " has been successfully added to the database",
                                "Equipment added", JOptionPane.INFORMATION_MESSAGE);

                        LocalDateTime day = LocalDateTime.now();
                        DataBase.UpdateData.updateLastMaintenance(user, equipment, day.toString());

                        frame.setContentPane(new Admin(frame, user).getRootPanelAdmin());
                        frame.repaint();
                        frame.revalidate();

                        DataBase.InsertData.insertNewLog(user, "Equipment \" " + getEquipmentName() + " \" added" , "EquipAdded");

                    } catch (SQLException e1) {
                        JOptionPane.showMessageDialog(null, "New equipment has not been created. " +
                                "An unhandled error occurs.", "Error occurs", JOptionPane.ERROR_MESSAGE);
                    }

                } else {
                    allFieldNotCompleted.setVisible(true);
                    frame.repaint();
                    frame.revalidate();

                }

            }
        });
        createNewAccessoryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Get info typed by user
                String name = getAccessoryName();
                String category = getAccessoryCategory();
                String location = getAccessoryLocation();
                String expirationDate = getAccessoryExpirationDate();
                String lotNumber = getAccessoryLotNumber();
                String supplier = getAccessorySupplier();
                String description = getAccessoryDescription();
                int quantity = getAccessoryQuantity();

                if (name.length() != 0 && category.length() != 0 && location.length() != 0 && expirationDate.length()!= 0 && lotNumber.length() != 0 && supplier.length() != 0 && description.length() != 0){
                    Accessory accessory = new Accessory(frame, name, category, quantity, lotNumber, expirationDate, description, user, supplier, 0, null, location);

                    //Insert accessory information in database
                    try {
                        DataBase.InsertData.insertNewAccessory(accessory, user);

                        JOptionPane.showMessageDialog(null, "The accessory " + name + " has been successfully added to the database",
                                "Accessory added", JOptionPane.INFORMATION_MESSAGE);

                        frame.setContentPane(new Admin(frame, user).getRootPanelAdmin());
                        frame.repaint();
                        frame.revalidate();

                        DataBase.InsertData.insertNewLog(user, "Accessory \" " + getAccessoryName() + " \" added" , "AccAdded");

                    } catch (SQLException e1) {
                        JOptionPane.showMessageDialog(null, "New accessory has not been created. " +
                                "An unhandled error occurs.", "Error occurs", JOptionPane.INFORMATION_MESSAGE);
                    }


                } else {
                    allAccessoryFieldsNotCompleted.setVisible(true);
                    frame.repaint();
                    frame.revalidate();
                }

            }
        });

        addEquipmentPictureButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //User can choose his picture
                JFileChooser file = new JFileChooser();
                file.setCurrentDirectory(new File(System.getProperty("user.home")));
                FileNameExtensionFilter filter = new FileNameExtensionFilter("*.Images",
                        "jpg", "png");
                file.addChoosableFileFilter(filter);

                int result = file.showSaveDialog(null);
                if (result == JFileChooser.APPROVE_OPTION){
                    selectedFile = file.getSelectedFile();
                    double size = selectedFile.length()/(1024*1024);
                    if (size <= 2.00){
                        String path = selectedFile.getAbsolutePath();
                        pictureNewEquipment.setIcon(ResizeImage(path));
                    } else {
                        JOptionPane.showMessageDialog(null, "Image selected is too heavy. Weight limit is 2MB",
                                "Image selection error", JOptionPane.ERROR_MESSAGE);
                    }

                } else if(result == JFileChooser.CANCEL_OPTION){
                    JOptionPane.showMessageDialog(null, "No image file selected.",
                            "Image selection error", JOptionPane.ERROR_MESSAGE);
                }

            }

        });

    }

    //Getter methods
    public JPanel getRootPanelAdmin(){ return rootpanelAdmin; }

    public String getProductName(){ return nameProducts.getSelectedItem().toString();}

    public String getProductCategory(){ return categoryProducts.getSelectedItem().toString(); }

    public String getProductLocation(){ return locationProducts.getSelectedItem().toString(); }

    public String getProductExpirationDate(){ return expirationDateProducts.getText(); }

    public int getProductQuantity(){ return (int) quantitySpinner.getValue(); }

    public String getProductLotNumber(){ return lotProducts.getText(); }

    public String getProductCas(){ return casProducts.getText(); }

    public String getProductDescription(){ return descriptionProduct.getText(); }

    public String getProductSupplier(){ return supplierProduct.getSelectedItem().toString(); }

    public String getEquipmentName(){ return nameNewEquipment.getText(); }

    public String getEquipmentCategory(){ return categoryNewEquipment.getSelectedItem().toString(); }

    public String getEquipmentLocation(){ return locationNewEquipment.getSelectedItem().toString(); }

    public String getEquipmentYear(){ return yearNewEquipment.getText(); }

    public String getEquipmentSerialNumber(){ return serialNumberNewEquipment.getText(); }

    public String getEquipmentSupplier(){ return supplierNewEquipment.getSelectedItem().toString(); }

    public String getEquipmentDescription(){ return descriptionNewEquipment.getText(); }

    public String getAccessoryName() { return nameAccessories.getSelectedItem().toString(); }

    public String getAccessoryCategory() { return categoryAccessories.getSelectedItem().toString(); }

    public String getAccessoryLocation() { return locationAccessories.getSelectedItem().toString(); }

    public String getAccessoryExpirationDate() { return expirationDateAccessory.getText(); }

    public String getAccessoryLotNumber() { return lotAccessory.getText(); }

    public String getAccessorySupplier() { return supplierNewAccessory.getSelectedItem().toString(); }

    public String getAccessoryDescription() { return descriptionAccessory.getText(); }

    public int getAccessoryQuantity() { return (int) quantityAccessory.getValue(); }

    public File getSelectedFile() { return selectedFile; }

    //Other methods
    private void createUIComponents() throws ParseException {
        //Custom create
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        DateFormatter df = new DateFormatter(format);
        this.expirationDateProducts = new JFormattedTextField(df);
        this.expirationDateProducts.setValue(format.parse("2017-12-31"));
        this.expirationDateAccessory = new JFormattedTextField(df);
        this.expirationDateAccessory.setValue(format.parse("2017-12-31"));

        SpinnerNumberModel spinnerModel = new SpinnerNumberModel(0,0,99999,1);
        quantitySpinner = new JSpinner(spinnerModel);
        quantityAccessory = new JSpinner(spinnerModel);

        LocalDateTime day = LocalDateTime.now();
        expirationDateAccessory.setValue(format.parse(day.toString()));
        expirationDateProducts.setValue(format.parse(day.toString()));

    }

    /**
     * Add location names to JComboBoxes
     * @param user: The active user to know his privileges
     * @param admin: The admin page of the software to update it
     */
    public static void addLocationItem(User user, Admin admin){
        admin.locationAccessories.removeAllItems();
        admin.locationProducts.removeAllItems();
        admin.locationNewEquipment.removeAllItems();

        //The JComboBoxes begins with an empty slot
        admin.locationAccessories.addItem("");
        admin.locationNewEquipment.addItem("");
        admin.locationProducts.addItem("");

        //Add values to JComboBoxes
        try {
            ArrayList<String> locationsList = DataBase.ExtractData.extractAllLocationsName(user);
            for (String loc : locationsList){
                admin.locationAccessories.addItem(loc);
                admin.locationNewEquipment.addItem(loc);
                admin.locationProducts.addItem(loc);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Location names have not been displayed.",
                    "Location names display error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Add category names existing in the database to JComboBoxes
     * @param user: The actives user to know his privileges
     * @param category: Add a new category to JComboBoxes
     * @param whatKind: Type of object concerned (product/equipment/accessory)
     */
    public void addCategory(User user, String category, String whatKind){
        switch(whatKind){
            case "product": categoryProducts.removeAllItems();
                            categoryProducts.addItem("");
                            try {
                                ArrayList<String> productsCategories = DataBase.ExtractData.extractProductsCategories(user);
                                for (String cat : productsCategories){
                                    categoryProducts.addItem(cat);
                                }
                                if (category != null){
                                    categoryProducts.addItem(category);
                                }
                            } catch (NotConnectedException | SQLException e) {
                                JOptionPane.showMessageDialog(null, "Product category names have not been displayed.",
                                        "Product category names display error", JOptionPane.ERROR_MESSAGE);
                            }
                            break;

            case "equipment": categoryNewEquipment.removeAllItems();
                              categoryNewEquipment.addItem(""); //The JComboBox begins with an empty slot
                              try {
                                  ArrayList<String> equipmentsCategories = DataBase.ExtractData.extractEquipmentsCategories(user);
                                  for (String cat : equipmentsCategories){
                                      categoryNewEquipment.addItem(cat);
                                  }
                                  if (category != null){
                                      categoryNewEquipment.addItem(category);
                                  }

                              } catch (Exception e) {
                                  JOptionPane.showMessageDialog(null, "Equipment category names have not been displayed.",
                                          "Equipment category names display error", JOptionPane.ERROR_MESSAGE);
                              }
                              break;

            case "accessory": categoryAccessories.removeAllItems();
                              categoryAccessories.addItem(""); //The JComboBox begins with an empty slot
                              try {
                                  ArrayList<String> accessoriesCategories = DataBase.ExtractData.extractAccessoriesCategories(user);
                                  for (String cat : accessoriesCategories){
                                      categoryAccessories.addItem(cat);
                                  }

                              } catch (Exception e) {
                                  JOptionPane.showMessageDialog(null, "Accessory category names have not been displayed.",
                                          "Accessory category names display error", JOptionPane.ERROR_MESSAGE);
                              }
                              if (category != null){
                                  categoryAccessories.addItem(category);
                              }
        }
    }

    /**
     * Add supplier names to JComboBoxes
     * @param user: The active user to know his privileges
     * @param admin: The admin page of the software
     */
    public static void addSupplierItem(User user, Admin admin){
        admin.supplierNewEquipment.removeAllItems();
        admin.supplierProduct.removeAllItems();
        admin.supplierNewAccessory.removeAllItems();

        //JComboBoxes begins with a blank line
        admin.supplierNewEquipment.addItem("");
        admin.supplierProduct.addItem("");
        admin.supplierNewAccessory.addItem("");

        try {
            ArrayList<Supplier> supplierList = DataBase.ExtractData.extractAllSuppliers(user);
            for (Supplier sup : supplierList){
                admin.supplierNewEquipment.addItem(sup.getSupplier());
                admin.supplierProduct.addItem(sup.getSupplier());
                admin.supplierNewAccessory.addItem(sup.getSupplier());
            }

        } catch (Exception e){
            JOptionPane.showMessageDialog(null, "Supplier names have not been displayed.",
                    "Supplier names display error", JOptionPane.ERROR_MESSAGE);
        }

    }

    /**
     * Add accessory names to JComboBoxes
     * @param user: The active user to know his privileges
     * @param accessoryName: Name of accessory to add
     * @param admin: Admin page of the software
     */
    public static void addAccessory(User user, String accessoryName, Admin admin){
        admin.nameAccessories.removeAllItems();
        admin.nameAccessories.addItem("");
        try {
            ArrayList<String> accessoriesName = DataBase.ExtractData.extractAccessoriesNames(user);
            for (String name : accessoriesName){
                admin.nameAccessories.addItem(name);
            }
            if (accessoryName != null){
                admin.nameAccessories.addItem(accessoryName);
            }

        } catch (NotConnectedException | SQLException e) {
            JOptionPane.showMessageDialog(null, "Accessories names have not been displayed.",
                    "Accessories names display error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Add product names to JComboBoxes
     * @param user: The active user to know his privileges
     * @param productName: The product name to add
     * @param admin: Admin page of the software
     */
    public static void addProduct(User user, String productName, Admin admin){
        admin.nameProducts.removeAllItems();
        admin.nameProducts.addItem("");
        try {
            ArrayList<String> productsNames = DataBase.ExtractData.extractProductsNames(user);
            for (String name : productsNames){
                admin.nameProducts.addItem(name);
            }
            if (productName != null){
                admin.nameProducts.addItem(productName);
            }
        } catch (NotConnectedException | SQLException e) {
            JOptionPane.showMessageDialog(null, "Product names have not been displayed.",
                    "Product names display error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Display all the users with their information
     * @param frame: Main frame of the software
     * @param user: The active user to know his privileges
     */
    public void displayUsers(JFrame frame, User user){
        userNamePanel.setLayout(new GridBagLayout());
        GridBagConstraints nameLayout = new GridBagConstraints();
        nameLayout.fill = GridBagConstraints.BOTH;
        nameLayout.weightx = 1.0;
        nameLayout.weighty = 2;
        nameLayout.gridx = 0;
        nameLayout.gridy = 0;

        userFisrtNamePanel.setLayout(new GridBagLayout());
        GridBagConstraints firstNameLayout = new GridBagConstraints();
        firstNameLayout.fill = GridBagConstraints.BOTH;
        firstNameLayout.weightx = 1.0;
        firstNameLayout.weighty = 2;
        firstNameLayout.gridx = 0;
        firstNameLayout.gridy = 0;

        userLoginPanel.setLayout(new GridBagLayout());
        GridBagConstraints loginLayout = new GridBagConstraints();
        loginLayout.fill = GridBagConstraints.BOTH;
        loginLayout.weightx = 1.0;
        loginLayout.weighty = 2;
        loginLayout.gridx = 0;
        loginLayout.gridy = 0;

        userMailPanel.setLayout(new GridBagLayout());
        GridBagConstraints mailLayout = new GridBagConstraints();
        mailLayout.fill = GridBagConstraints.BOTH;
        mailLayout.weightx = 1.0;
        mailLayout.weighty = 2;
        mailLayout.gridx = 0;
        mailLayout.gridy = 0;

        userDeleteButton.setLayout(new GridBagLayout());
        GridBagConstraints deleteLayout = new GridBagConstraints();
        deleteLayout.fill = GridBagConstraints.BOTH;
        deleteLayout.weightx = 1.0;
        deleteLayout.weighty = 2;
        deleteLayout.gridx = 0;
        deleteLayout.gridy = 0;

        adminPrivilegesPanel.setLayout(new GridBagLayout());
        GridBagConstraints adminPrivilegesLayout = new GridBagConstraints();
        adminPrivilegesLayout.fill = GridBagConstraints.BOTH;
        adminPrivilegesLayout.weightx = 1.0;
        adminPrivilegesLayout.weighty = 2;
        adminPrivilegesLayout.gridx = 0;
        adminPrivilegesLayout.gridy = 0;

        ArrayList<User> userInfoList;
        try {
            userInfoList = DataBase.ExtractData.extractAllUsers(user);
            for (User u : userInfoList) {
                UserLine userLine = new UserLine(frame, u, user);

                nameLayout.gridy += 1;
                firstNameLayout.gridy += 1;
                loginLayout.gridy += 1;
                mailLayout.gridy += 1;
                deleteLayout.gridy += 1;
                adminPrivilegesLayout.gridy += 1;

                userNamePanel.add(userLine.getUserLineName(), nameLayout);
                userFisrtNamePanel.add(userLine.getUserLineFirstName(), firstNameLayout);
                userLoginPanel.add(userLine.getUserLineLogin(), loginLayout);
                userMailPanel.add(userLine.getUserLineMail(), mailLayout);
                userDeleteButton.add(userLine.getDeleteButton(), deleteLayout);
                adminPrivilegesPanel.add(userLine.getUserLinePrivileges(), adminPrivilegesLayout);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Users information have not been displayed.",
                    "User information display error", JOptionPane.ERROR_MESSAGE);
        }

    }

    /**
     * Resize a given image
     * @param imagePath: Path of the selected image
     * @return: A resized version of the selected image
     */
    public ImageIcon ResizeImage(String imagePath){
        ImageIcon image = new ImageIcon(imagePath);
        Image img = image.getImage();
        Image newImg = img.getScaledInstance(pictureNewEquipment.getWidth(), pictureNewEquipment.getHeight(), Image.SCALE_SMOOTH);

        return new ImageIcon(newImg);
    }

    /**
     * Resize a given image
     * @param image: Image selected
     * @param width: Width wanted of the resized image
     * @param height: Height wanted of the resized image
     * @return: A resized version of the selected image
     */
    public ImageIcon ResizeImageFromIcon(ImageIcon image, int width, int height){
        Image img = image.getImage();
        Image newImg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);

        return new ImageIcon(newImg);
    }

}
