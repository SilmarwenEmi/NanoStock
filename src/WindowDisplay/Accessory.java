package WindowDisplay;

import NanostockException.NotConnectedException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.Objects;

/**
 * @author Emilie Laurent
 * This class represent a common accessory (something you can use to perform a task)
 */
public class Accessory extends JPanel {
    private JPanel accessory;
    private JPanel accessoriesLargePanel;

    private JSpinner quantitySpinner;

    private JButton moreAccessoryButton;
    private JButton trashAccessoriesButton;
    private JButton updateAccessoryQuantity;

    private JEditorPane accessoryDescriptionPanel;

    private JLabel accessoriesName;
    private JLabel accessoriesCategory;
    private JLabel accessoriesLot;
    private JLabel accessoriesExpirationDate;
    private JLabel supplier;

    private int accessoryId;

    private String accessoryLocation;

    /**
     * Construct the accessory object with information given
     * @param frame: Represent the main frame of the software
     * @param name: The name of the accessory
     * @param cat: The name of the accessory category
     * @param quantity: The number of accessory in the stock
     * @param lot: The lot number of the accessory
     * @param expirationDate: The expiration date of the accessory
     * @param description: The eventual description of the accessory
     * @param user: The active user to know his privileges
     * @param supplier: The name of the firm who sold the accessory
     * @param id: Identification number of the accessory in the database
     * @param sm: Represent the stock management page of the software
     * @param location: The name of the location where the accessory is stocked
     */
    public Accessory(JFrame frame, String name, String cat, int quantity, String lot, String expirationDate, String description,
                     User user, String supplier, int id, StockMangement sm, String location){
        JPanel rootAccessory = accessory;
        JPanel panelLarge = accessoriesLargePanel;

        this.accessoriesName.setText(name);
        this.accessoriesCategory.setText(cat);
        this.quantitySpinner.setValue(quantity);
        this.accessoriesLot.setText(lot);
        this.accessoriesExpirationDate.setText(expirationDate);
        this.accessoryDescriptionPanel.setText(description);
        this.supplier.setText(supplier);
        this.accessoryId = id;
        this.accessoryLocation = location;

        //Display or not the trash button in function of user privileges
        if (Objects.equals(user.getUserPrivileges(), "Admin")){
            trashAccessoriesButton.setVisible(true);
        } else {
            trashAccessoriesButton.setVisible(false);
        }

        setBackground(Color.WHITE);
        setLayout(new GridLayout(0, 1));
        add(rootAccessory);

        //Add listeners to buttons
        moreAccessoryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                StockMangement sm = null;
                try {
                    sm = new StockMangement(frame, user);
                } catch (NotConnectedException | SQLException e1) {
                    e1.printStackTrace();
                }
                if(!panelLarge.isVisible()){
                    assert sm != null;
                    moreAccessoryButton.setIcon(sm.getLessButtonLogo());
                    panelLarge.setVisible(true);
                } else {
                    assert sm != null;
                    moreAccessoryButton.setIcon(sm.getMoreButtonLogo());
                    panelLarge.setVisible(false);
                }
            }
        });

        trashAccessoriesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                int optionTrashAccessory = JOptionPane.showConfirmDialog(null,
                        "Are you sure to delete the equipment named " + accessoriesName.getText() + " ?", "Delete equipment confirmation",
                        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (optionTrashAccessory == 0) {
                    DataBase.DeleteData.deleteAccessory(getAccessoryId(), user);

                    sm.displayAccessories(frame, user, "");

                    JOptionPane.showMessageDialog(null, "Accessory " + getAccessoryName() +
                            " has been removed from database.", "Accessory removed", JOptionPane.INFORMATION_MESSAGE);

                    DataBase.InsertData.insertNewLog(user, "Accessory \" " + getAccessoryName() + " \" deleted" , "AccDeleted");
                }

            }
        });
        updateAccessoryQuantity.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int quantity = (int) quantitySpinner.getValue();
                try {
                    DataBase.UpdateData.updateAccessoryQuantity(user, quantity, getAccessoryId());

                    sm.displayAccessories(frame, user, "");

                    JOptionPane.showMessageDialog(null, "Quantity of " + getAccessoryName() +
                            " has been updated in database.", "Accessory quantity updated", JOptionPane.INFORMATION_MESSAGE);

                } catch (SQLException e1) {
                    JOptionPane.showMessageDialog(null, "Quantity of " + getAccessoryName() +
                            " has not been updated in database.", "Error Accessory quantity update", JOptionPane.ERROR_MESSAGE);
                }
            }

        });
    }

    //Getter and setter methods
    public int getAccessoryId() {
        return accessoryId;
    }

    public String getAccessoryName(){
        return accessoriesName.getText();
    }

    public String getAccessoryLotNumber() { return accessoriesLot.getText(); }

    public String getAccessoryLocation() { return accessoryLocation; }

    public int getAccessoryQuantity() { return (int) quantitySpinner.getValue(); }

    public String getAccessoryDescription() { return accessoryDescriptionPanel.getText(); }

    public String getAccessorySupplier() { return supplier.getText(); }

    public String getAccessoryCategory() { return accessoriesCategory.getText(); }

    public String getAccessoryExpirationDate() { return accessoriesExpirationDate.getText(); }

    public void setAccessoryId(int id) { accessoryId = id; }

    //Custom create for graphical component
    private void createUIComponents() {
        SpinnerNumberModel spinnerModel = new SpinnerNumberModel(0,0,99999,1);
        quantitySpinner = new JSpinner(spinnerModel);
    }
}
