package WindowDisplay;

import NanostockException.NotConnectedException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;

import static DataBase.ExtractData.*;

/**
 * @author Emilie Laurent
 * Represent the stock management page of the software
 */
public class StockMangement {

    private JPanel rootPanelStockMangement;
    private JPanel navigatorPanel;
    private JPanel collapsedBar;
    private JPanel dataProducts;
    private JPanel dataEquipments;
    private JPanel dataAccessories;

    private JButton logoutButton;
    private JButton alertButton;
    private JButton userIDButton;
    private JButton switchToAlerts;
    private JButton switchToAdmin;
    private JButton switchToLog;
    private JButton collapseButton;
    private JButton expandButton;
    private JButton searchEquipmentsButton;
    private JButton searchAccessoriesButton;
    private JButton searchProductsButton;
    private JButton returnHomeButton;

    private JTextField searchEquipments;
    private JTextField searchAccessories;
    private JTextField searchProducts;

    //image declarations
    private ImageIcon lessButtonLogo = new ImageIcon(this.getClass().getResource("/Pictures/Less.png"));
    private ImageIcon moreButtonLogo = new ImageIcon(this.getClass().getResource("/Pictures/More.png"));

    //Layout constraints declaration
    private GridBagConstraints productsLayout = new GridBagConstraints();
    private GridBagConstraints accessoriesLayout = new GridBagConstraints();
    private GridBagConstraints equipmentsLayout = new GridBagConstraints();


    /**
     * Construct the stock management page
     * @param frame: Main frame of the software
     * @param user: The active user to know his privileges
     * @throws NotConnectedException: If the connection to the database fails
     * @throws SQLException: If SQL query is invalid
     */
    public StockMangement(JFrame frame, User user) throws NotConnectedException, SQLException {

        //Configure userId button
        userIDButton.setText(user.getUserName() + " " + user.getUserFirstName());
        userIDButton.setIcon(ResizeImageFromIcon(user.getUserPicture(), 30, 30));

        //Configure small alerts button
        boolean isThereAlerts = false;
        try {
            isThereAlerts = countRowsAlerts(user, 0) > 0;
        } catch (SQLException e) {
            System.out.println("Alerts count error stock management");
        }

        if ( isThereAlerts ){
            ImageIcon newAlertsImage = new ImageIcon(this.getClass().getResource("/Pictures/newAlerts.png"));
            alertButton.setIcon(newAlertsImage);
        } else {
            ImageIcon noAlertsImage = new ImageIcon(this.getClass().getResource("/Pictures/alert.png"));
            alertButton.setIcon(noAlertsImage);
        }

        //Admin or user view
        if (Objects.equals(user.getUserPrivileges(), "Admin")) {
            switchToLog.setVisible(true);
            switchToAdmin.setVisible(true);
        } else {
            switchToLog.setVisible(false);
            switchToAdmin.setVisible(false);
        }

        //add listener to buttons
        logoutButton.addActionListener(new ActionToDo("logout", frame, null, null, user, null, null, this, null, null));
        switchToAlerts.addActionListener(new ActionToDo("switchToAlerts", frame, null, null, user, null, null, this, null, null));
        switchToLog.addActionListener(new ActionToDo("switchToLog", frame, null,  null, user, null, null, this, null, null));
        switchToAdmin.addActionListener(new ActionToDo("switchToAdmin", frame, null, null, user, null, null, this, null, null));
        alertButton.addActionListener(new ActionToDo("switchToAlerts", frame, null, null, user, null, null, this, null, null));
        userIDButton.addActionListener(new ActionToDo("switchToUserProfile", frame, null, null, user, null, null, this, null, null));
        returnHomeButton.addActionListener(new ActionToDo("switchToWhatDoing", frame, null, null, user, null, null, this, null, null));

        collapseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                navigatorPanel.setVisible(false);
                collapsedBar.setVisible(true);
            }
        });
        expandButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                navigatorPanel.setVisible(true);
                collapsedBar.setVisible(false);
            }
        });

        searchEquipmentsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (searchEquipments.getText().length() == 0) {
                    displayEquipments(frame, user, "");
                } else {
                    displayEquipments(frame, user, searchEquipments.getText());
                }
            }
        });
        searchProductsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (searchProducts.getText().length() == 0) {
                    displayProducts(frame, user, "");
                } else {
                    displayProducts(frame, user, searchProducts.getText());
                }
            }
        });
        searchAccessoriesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (searchAccessories.getText().length() == 0) {
                    displayAccessories(frame, user, "");
                } else {
                    displayAccessories(frame, user, searchAccessories.getText());
                }
            }
        });

        //Display equipments, products and accessories
        displayEquipments(frame, user, "");
        displayAccessories(frame, user, "");
        displayProducts(frame, user, "");

    }

    //Getter and setter methods
    public JPanel getRootPanelStock() {
        return rootPanelStockMangement;
    }

    public JPanel getCollapsedBar() {
        return collapsedBar;
    }

    public JPanel getNavigatorPanel() {
        return navigatorPanel;
    }

    public JButton getLogoutButton() {
        return logoutButton;
    }

    public ImageIcon getLessButtonLogo() {
        return lessButtonLogo;
    }

    public ImageIcon getMoreButtonLogo() {
        return moreButtonLogo;
    }

    /**
     * Display all equipments
     * @param frame: Main frame of the software
     * @param user: The active user to know his privileges
     * @param wordsToSearch: The couple of words user of the software typed to display item searched
     */
    public void displayEquipments(JFrame frame, User user, String wordsToSearch) {

        //Empty the equipments panel
        dataEquipments.removeAll();

        //Initialize equipments panel with layout
        dataEquipments.setLayout(new GridBagLayout());
        equipmentsLayout.fill = GridBagConstraints.BOTH;
        equipmentsLayout.weightx = 1.0;
        equipmentsLayout.gridx = 0;
        equipmentsLayout.gridy = 0;

        //Display locations
        ArrayList<Location> locationEquipmentList;
        try {
            locationEquipmentList = DataBase.ExtractData.extractAllLocations(frame, this, "equipment", user, wordsToSearch);

            for (Location locE : locationEquipmentList) {
                equipmentsLayout.gridy += 1;
                dataEquipments.add(locE, equipmentsLayout);
            }

        } catch (NotConnectedException | SQLException e) {
            JOptionPane.showMessageDialog(null, "Equipment has not been displayed. An unhandled " +
                    "error occurs. ", "Equipment display error", JOptionPane.ERROR_MESSAGE);
        }

        frame.repaint();
        frame.revalidate();

    }


    /**
     * Display accessories in the stock management page
     * @param frame: Main frame of the software
     * @param user: The active user to know his privileges
     * @param wordsToSearch: The couple of words we have to find to display item
     */
    public void displayAccessories(JFrame frame, User user, String wordsToSearch) {

        //Empty accessories panel
        dataAccessories.removeAll();

        //Initialize accessories panel with layout
        dataAccessories.setLayout(new GridBagLayout());
        accessoriesLayout.fill = GridBagConstraints.BOTH;
        accessoriesLayout.weightx = 1.0;
        accessoriesLayout.gridx = 0;
        accessoriesLayout.gridy = 0;

        //Display locations
        ArrayList<Location> locationAccessoryList;
        try {
            locationAccessoryList = DataBase.ExtractData.extractAllLocations(frame, this, "accessory", user, wordsToSearch);
            for (Location locA : locationAccessoryList) {
                accessoriesLayout.gridy += 1;
                dataAccessories.add(locA, accessoriesLayout);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Accessory has not been displayed. An unhandled " +
                    "error occurs. ", "Accessory display error", JOptionPane.ERROR_MESSAGE);
        }

        frame.repaint();
        frame.revalidate();
    }

    /**
     * Display products in the stock management page
     * @param frame: Main frame of the software
     * @param user: The active user to know his privileges
     * @param wordsToSearch: The couple of words we have to find to display item
     */
    public void displayProducts(JFrame frame, User user, String wordsToSearch) {

        //Empty products panel
        dataProducts.removeAll();

        //Initialize products panel with layout
        dataProducts.setLayout(new GridBagLayout());
        productsLayout.fill = GridBagConstraints.BOTH;
        productsLayout.weightx = 1.0;
        productsLayout.gridx = 0;
        productsLayout.gridy = 0;

        //Display locations
        ArrayList<Location> locationProductList;
        try {
            locationProductList = DataBase.ExtractData.extractAllLocations(frame, this, "product", user, wordsToSearch);
            for (Location locP : locationProductList) {
                productsLayout.gridy += 1;
                dataProducts.add(locP, productsLayout);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Product has not been displayed. An unhandled " +
                    "error occurs. ", "Product display error", JOptionPane.ERROR_MESSAGE);
        }

        frame.repaint();
        frame.revalidate();

    }

    /**
     * Resize a given image
     * @param image: Image to resize
     * @param width: Width wanted of resized image
     * @param height: Height wanted of resized image
     * @return A resized version of the image
     */
    public ImageIcon ResizeImageFromIcon(ImageIcon image, int width, int height){
        Image img = image.getImage();
        Image newImg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);

        return new ImageIcon(newImg);
    }

}
