package WindowDisplay;

import javax.swing.*;
import java.sql.SQLException;

/**
 * @author Emilie Laurent
 * An ArchivedAlertLine is one line in the archived alerts display
 */
public class ArchivedAlertLine {
    private JLabel nameDescription;
    private JLabel category;
    private JLabel emissionDate;
    private JLabel location;
    private JLabel tag;
    private JLabel alertId;

    /**
     * Construct one archived alert line to be displayed
     * @param frame: Main frame of the software
     * @param user: The active user to know his privileges
     * @param itemId: Identification number of the concerned item
     * @param emissionDate: Date of emission of the alert
     * @param tag: Alert type
     * @param alertId: Identification number of the alert
     * @param sm: Stock Management page of the software
     * @param itemType: Type of concerned item
     */
    public ArchivedAlertLine(JFrame frame, User user, int itemId, String emissionDate, String tag, String alertId, StockMangement sm, String itemType){

        ImageIcon tagImage = new ImageIcon(this.getClass().getResource("/Pictures/" + tag + "Tag.png"));
        this.emissionDate.setText(emissionDate);
        this.tag.setIcon(tagImage);
        this.alertId.setText(alertId);

        switch (itemType){
            case "product":
                Product product = null;
                try {
                    product = DataBase.ExtractData.extractOneProduct(frame, user, itemId, sm);
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(null, "Product cannot be extracted." +
                            "An unhandled error occurs. ", "Product extraction error", JOptionPane.ERROR_MESSAGE);
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
                    JOptionPane.showMessageDialog(null, "Equipment cannot be extracted." +
                            "An unhandled error occurs. ", "Equipment extraction error", JOptionPane.ERROR_MESSAGE);
                }
                assert equipment != null;
                this.nameDescription.setText(equipment.getEquipmentName());
                this.category.setText(equipment.getEquipmentCategory());
                this.location.setText(equipment.getEquipmentLocation());
                break;
        }
    }

    //Getter methods
    public JLabel getArchivedAlertName() {
        return nameDescription;
    }

    public JLabel getArchivedAlertLocation() {
        return location;
    }

    public JLabel getArchivedAlertDate() { return emissionDate; }

    public JLabel getArchivedAlertCategory() { return category; }

    public JLabel getArchivedAlertTag() {
        return tag;
    }

    public JLabel getArchivedAlertId() { return alertId; }


}
