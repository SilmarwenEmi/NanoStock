package WindowDisplay;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.Objects;

/**
 * @author Emilie Laurent
 * Represent one line of the alert page
 */
public class AlertLine extends JPanel{
    private JLabel nameDescription;
    private JLabel category;
    private JLabel emissionDate;
    private JLabel location;
    private JLabel tag;
    private JLabel id;

    private JButton moreButton;
    private JButton doneButton;

    private JPanel alert;

    /**
     * Construct the Alert Line object with given information
     * @param frame: Main frame of the software
     * @param user: The active user to know his privileges
     * @param itemId: Identification number of the concerned item
     * @param date: Emission date of that alert
     * @param tag: Kind of alert received of database
     * @param alertId: Identification number of the alert
     * @param sm: Stock management page of the software
     * @param itemType: Type of item concerned (product/accessory/equipment)
     */
    public AlertLine(JFrame frame, User user, int itemId, String date, String tag, String alertId, StockMangement sm, String itemType){
        JPanel rootAlertLine = alert;
        ImageIcon tagImage = new ImageIcon(this.getClass().getResource("/Pictures/" + tag + "Tag.png"));
        this.emissionDate.setText(date);
        this.tag.setIcon(tagImage);
        this.id.setText(alertId);

        switch (itemType){
            case "product":
                Product product = null;
                try {
                    product = DataBase.ExtractData.extractOneProduct(frame, user, itemId, sm);
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(null, "Product information has not been extracted" +
                            " from database.", "Product display error", JOptionPane.ERROR_MESSAGE);
                }
                assert product != null;
                this.nameDescription.setText(product.getProductName());
                this.category.setText(product.getProductCategory());
                this.location.setText(product.getProductLocation());
                break;

            case "equipment":
                Equipment equipment = null;
                try {
                    equipment = DataBase.ExtractData.extractOneEquipement(frame, user, itemId, sm);
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(null, "Equipment information has not been extracted" +
                            " from database.", "Equipment display error", JOptionPane.ERROR_MESSAGE);
                }
                assert equipment != null;
                this.nameDescription.setText(equipment.getEquipmentName());
                this.category.setText(equipment.getEquipmentCategory());
                this.location.setText(equipment.getEquipmentLocation());
                break;
        }

        switch (tag){
            case "lowQuantity" :
                doneButton.addActionListener(new ActionToDo("alertLowQuantityProduct", frame,
                        null, null, user, null, null, null, null, alertId));

                break;

            case "outdatedProduct" :
                Product product = new Product();
                product.setProductId(itemId);
                doneButton.addActionListener(new ActionToDo("alertOutdated", frame, null, null,
                        user, null, null, null, product, alertId));
                break;

            case "maintenanceRequired" :
                Equipment equipment = null;
                try {
                    equipment = DataBase.ExtractData.extractOneEquipement(frame, user, itemId, sm);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                doneButton.addActionListener(new ActionToDo("alertMaintenance", frame, null, null,
                        user, null, equipment, null, null, alertId));
        }

        setBackground(Color.WHITE);
        setLayout(new GridLayout(0, 1));
        add(rootAlertLine);

        //Add listeners to buttons
        moreButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (Objects.equals(itemType, "product")){
                    JFrame popUpProduct = new JFrame("Product information");

                    Product product = null;
                    try {
                        product = DataBase.ExtractData.extractOneProduct(frame, user, itemId, sm);
                    } catch (SQLException e1) {
                        JOptionPane.showMessageDialog(null, "Product information has not been extracted" +
                                " from database.", "Product display error", JOptionPane.ERROR_MESSAGE);
                    }

                    JPanel rootPanel = new JPanel();
                    rootPanel.setBackground(Color.WHITE);
                    assert product != null;
                    rootPanel.add(product.getProductRootPanel());

                    popUpProduct.setContentPane(rootPanel);
                    popUpProduct.setAlwaysOnTop(true);
                    popUpProduct.setPreferredSize(new Dimension(1200,300));
                    popUpProduct.pack();
                    popUpProduct.setVisible(true);

                } else if (Objects.equals(itemType, "equipment")){
                    JFrame popUpProduct = new JFrame("Product information");

                    Equipment equipment = null;
                    try {
                        equipment = DataBase.ExtractData.extractOneEquipement(frame, user, itemId, sm);
                    } catch (SQLException e1) {
                        JOptionPane.showMessageDialog(null, "Equipment information has not been extracted" +
                                " from database.", "Equipment display error", JOptionPane.ERROR_MESSAGE);
                    }

                    JPanel rootPanel = new JPanel();
                    rootPanel.setBackground(Color.WHITE);

                    assert equipment != null;
                    rootPanel.add(equipment.getEquipmentsRootPanel());

                    popUpProduct.setContentPane(rootPanel);
                    popUpProduct.setAlwaysOnTop(true);
                    popUpProduct.setPreferredSize(new Dimension(1200,450));
                    popUpProduct.pack();
                    popUpProduct.setVisible(true);
                }

            }
        });
    }

    //Getter methods
    public JLabel getAlertName() {
        return nameDescription;
    }

    public JLabel getAlertLocation() {
        return location;
    }

    public JLabel getAlertDate() { return emissionDate; }

    public JLabel getAlertCategory() { return category; }

    public JLabel getAlertTag() {
        return tag;
    }

    public JButton getAlertDoneButton() {
        return doneButton;
    }

    public JLabel getAlertId() { return id; }

    public JButton getAlertMoreButton() { return moreButton; }
}
