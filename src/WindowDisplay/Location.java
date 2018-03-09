package WindowDisplay;

import NanostockException.NotConnectedException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;

/**
 * @author Emilie Laurent
 * A location represent a physical place where we can store equipments, accessories or products
 */
public class Location extends JPanel {
    private JButton locationTrashButton;
    private JButton locationExpanseCollapse;

    private JLabel locationName;
    private JLabel locationLogo;
    private JPanel rootLocation;
    private JPanel dataLocation;

    private ImageIcon collapseMenuLogo = new ImageIcon(this.getClass().getResource("/Pictures/collapseMenu.png"));
    private ImageIcon expandMenuLogo = new ImageIcon(this.getClass().getResource("/Pictures/expandMenu.png"));

    private boolean open = false;

    private String imagePath;

    /**
     * Construction location object with given information
     * @param frame: Main frame of the software
     * @param name: Name of the location
     * @param imagePath: Path of the image chosen for the location
     * @param sm: Stock Management page of the software
     * @param type: Type of item stored in
     * @param user: The active user to know his privileges
     * @param words: The couple of words we have to find to display item
     * @throws NotConnectedException: If the connection to the database fails
     * @throws SQLException: If SQL query is invalid
     */
    public Location(JFrame frame, String name, String imagePath, StockMangement sm, String type, User user, String words) throws NotConnectedException, SQLException {
        JPanel root = rootLocation;

        setLayout(new GridLayout(0, 1));

        this.imagePath = imagePath;
        ImageIcon image = new ImageIcon(this.getClass().getResource(imagePath));
        this.locationName.setText(name);
        this.locationLogo.setIcon(image);

        setBackground(Color.WHITE);
        add(root);

        this.dataLocation.setLayout(new GridBagLayout());
        GridBagConstraints locationLayout = new GridBagConstraints();
        locationLayout.fill = GridBagConstraints.BOTH;
        locationLayout.weightx = 1.0;
        locationLayout.gridx = 0;
        locationLayout.gridy = 0;

        //Display items in function of their type
        switch(type){
            case "product" :
                ArrayList<Product> productList = null;
                try {
                    productList = DataBase.ExtractData.extractAllProductsFromLocation(frame, locationName.getText(), user, sm, words);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Cannot extract product for " + this.locationName.getText()
                            + " location.","Product extraction error", JOptionPane.ERROR_MESSAGE);
                }
                assert productList != null;
                if (productList.size() != 0){
                                 for (Product prod : productList) {
                                     locationLayout.gridy += 1;
                                     dataLocation.add(prod, locationLayout);
                                 }
                                 locationTrashButton.setVisible(false);
                                 locationExpanseCollapse.setVisible(true);
                             } else {
                                 if (Objects.equals(user.getUserPrivileges(), "Admin")){
                                     locationTrashButton.setVisible(true);
                                 } else {
                                     locationTrashButton.setVisible(false);
                                 }
                                 locationExpanseCollapse.setVisible(false);
                             }

                             break;

            case "equipment" :
                               ArrayList<Equipment> equipmentList;
                               ImageIcon defaultPicture = new ImageIcon(this.getClass().getResource("/Pictures/cameraSmall.png"));
                               equipmentList = DataBase.ExtractData.extractAllEquipmentsFromLocation(frame, locationName.getText(), user, sm, words, defaultPicture);
                               if (equipmentList.size() != 0){
                                   for (Equipment equip : equipmentList) {
                                       locationLayout.gridy += 1;
                                       dataLocation.add(equip, locationLayout);
                                   }
                                   locationExpanseCollapse.setVisible(true);
                                   locationTrashButton.setVisible(false);
                               } else {
                                   if (Objects.equals(user.getUserPrivileges(), "Admin")){
                                       locationTrashButton.setVisible(true);
                                   } else {
                                       locationTrashButton.setVisible(false);
                                   }
                                   locationExpanseCollapse.setVisible(false);
                               }

                               break;

            case "accessory" :
                               ArrayList<Accessory> accessoryList;
                               accessoryList = DataBase.ExtractData.extractAllAccessoriesFromLocation(frame, locationName.getText(), user, sm, words);
                               if (accessoryList.size() != 0){
                                   for (Accessory acc : accessoryList) {
                                       locationLayout.gridy += 1;
                                       dataLocation.add(acc, locationLayout);
                                   }
                                   locationExpanseCollapse.setVisible(true);
                                   locationTrashButton.setVisible(false);
                               } else {
                                   if (Objects.equals(user.getUserPrivileges(), "Admin")){
                                       locationTrashButton.setVisible(true);
                                   } else {
                                       locationTrashButton.setVisible(false);
                                   }
                                   locationExpanseCollapse.setVisible(false);
                               }

                               break;
        }

        //Add listeners to buttons
        locationExpanseCollapse.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (open){
                    open = false;
                    locationExpanseCollapse.setIcon(expandMenuLogo);
                    dataLocation.setVisible(false);

                } else {
                    open = true;
                    locationExpanseCollapse.setIcon(collapseMenuLogo);

                    dataLocation.setVisible(true);

                    frame.repaint();
                    frame.revalidate();

                }
            }
        });
        locationTrashButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int optionTrashLocation = JOptionPane.showConfirmDialog(null,
                        "Are you sure to delete the equipment named " + getLocationName() + " ?", "Delete location confirmation",
                        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (optionTrashLocation == 0) {
                    try {
                        DataBase.DeleteData.deleteLocation(user, getLocationName());

                        JOptionPane.showMessageDialog(null, "Location " + locationName.getText() +
                                " has been successfully deleted.", "Location deleted", JOptionPane.INFORMATION_MESSAGE);

                        sm.displayEquipments(frame, user, "");
                        sm.displayAccessories(frame, user, "");
                        sm.displayProducts(frame, user, "");

                        DataBase.InsertData.insertNewLog(user, "Location \" " + getLocationName() + " \" added" , "LocationDeleted");

                    } catch (SQLException e1) {
                        JOptionPane.showMessageDialog(null, "Location " + locationName.getText() +
                                " cannot be deleted. It may not be empty.", "Location not deleted", JOptionPane.ERROR_MESSAGE);
                    }
                }

            }
        });
    }

    //Getter methods
    public String getLocationImagePath(){ return this.imagePath; }

    public String getLocationName(){
        return locationName.getText();
    }


}

