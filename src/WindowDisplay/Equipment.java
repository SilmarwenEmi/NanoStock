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
 * Represent a common equipment (something usefull to perform a task)
 */
public class Equipment extends JPanel {
    private JPanel equipment;
    private JPanel equipmentsLargePanel;

    private JLabel equipmentsPicture;
    private JLabel equipmentsName;
    private JLabel equipmentsCategory;
    private JLabel equipmentsYear;
    private JLabel supplier;
    private JLabel supplierName;
    private JLabel supplierPhone;
    private JLabel serialNumberEquipment;

    private JButton trashEquipmentsButton;
    private JButton moreEquipmentsButton;
    private JButton graphTrendButton;
    private JButton saveUseButton;
    private JButton addMaintenanceButton;

    private JEditorPane equipmentsDescription;
    private JEditorPane maintenancesDates;

    private JSpinner hoursUseInput;
    private JSpinner minutesUseInput;

    private int equipmentId;

    private String location;

    /**
     * Construct the default equipment object
     */
    public Equipment(){
        this.equipmentsName.setText("");
        this.equipmentsCategory.setText("");
        this.equipmentsYear.setText("");
        this.equipmentsDescription.setText("");
        this.supplier.setText("");
        this.supplierName.setText("");
        this.supplierPhone.setText("");
        this.equipmentId = 0;
        this.serialNumberEquipment.setText("");
        this.hoursUseInput.setValue(0);
        this.minutesUseInput.setValue(0);
        this.maintenancesDates.setText("");
        this.location = "";

    }

    /**
     * Construct the equipment object with information given
     * @param frame: Main frame of the software
     * @param name: Name of the equipment
     * @param year: Acquisition year of the equipment
     * @param cat: Category of the equipment
     * @param description: Description of the equipment
     * @param usageTime: Total usage time of the equipment
     * @param serialNumber: Serial number of the equipment
     * @param user: The active user to know his privileges
     * @param supplier: All information of the supplier of this equipment
     * @param sm: Stock Management page of the software
     * @param id: Identification number of the equipment
     * @param maintenance: All the maintenance dates oh this equipment
     * @param location: The location where the equipment is stocked
     * @param picture: Picture chosen for this equipment
     */
    public Equipment(JFrame frame, String name, String year, String cat, String description, String usageTime, String serialNumber,
                     User user, Supplier supplier, StockMangement sm, int id, String maintenance, String location, ImageIcon picture){
        JPanel rootEquipment = equipment;
        JPanel panelLarge = equipmentsLargePanel;

        String hours = "00";
        String minutes = "00";
        if(usageTime.length() != 0){
            hours = usageTime.substring(0,2);
            minutes = usageTime.substring(3,5);
        }

        this.equipmentsName.setText(name);
        this.equipmentsCategory.setText(cat);
        this.equipmentsYear.setText(year);
        this.equipmentsDescription.setText(description);
        this.supplier.setText(supplier.getSupplier());
        this.supplierName.setText(supplier.getContactName());
        this.supplierPhone.setText(supplier.getContactPhone());
        this.equipmentId = id;
        this.serialNumberEquipment.setText(serialNumber);
        this.hoursUseInput.setValue(Integer.parseInt(hours));
        this.minutesUseInput.setValue(Integer.parseInt(minutes));
        this.maintenancesDates.setText(maintenance);
        this.location = location;


        if (Objects.equals(user.getUserPrivileges(), "Admin")){
            trashEquipmentsButton.setVisible(true);
            addMaintenanceButton.setVisible(true);
        } else {
            trashEquipmentsButton.setVisible(false);
            addMaintenanceButton.setVisible(false);
        }

        ImageIcon imgGraphTrend = new ImageIcon(this.getClass().getResource("/Pictures/Trends_Large.png"));
        graphTrendButton.addActionListener(new ActionToDo("zoomImage",frame, imgGraphTrend,null, user, null, null, sm, null, null));
        addMaintenanceButton.addActionListener(new ActionToDo("newMaintenance", frame, null,null, user, null, this, sm, null, null));
        trashEquipmentsButton.addActionListener(new ActionToDo("deleteEquipment", frame, null, null, user, null, this, sm, null, null));

        setBackground(Color.WHITE);

        //Display and resize equipment picture

        if (picture == null) {
            ImageIcon defaultPicture = new ImageIcon(this.getClass().getResource("/Pictures/cameraSmall.png"));
            equipmentsPicture.setIcon(ResizeImage(defaultPicture));
        } else {
            //equipmentsPicture.setIcon(imgGraphTrend1);
            equipmentsPicture.setIcon(ResizeImage(picture));
        }

        setLayout(new GridLayout(0, 1));
        add(rootEquipment);

        //Add listeners to buttons
        moreEquipmentsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                StockMangement sm = null;
                try {
                    sm = new StockMangement(frame, user);
                } catch (NotConnectedException | SQLException e1) {
                    e1.printStackTrace();
                }
                if(!panelLarge.isVisible()){
                    //assert sm != null;
                    assert sm != null;
                    moreEquipmentsButton.setIcon(sm.getLessButtonLogo());
                    panelLarge.setVisible(true);
                } else {
                    //assert sm != null;
                    assert sm != null;
                    moreEquipmentsButton.setIcon(sm.getMoreButtonLogo());
                    panelLarge.setVisible(false);
                }
            }
        });

        saveUseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                int hours = getHoursOfUse();
                int minutes = getMinutesOfUse();
                String usageTime = "" + hours + ":" + minutes;

                try {
                    DataBase.UpdateData.updateEquipmentUsageTime(user, usageTime, equipmentId);
                } catch (SQLException e1) {
                    JOptionPane.showMessageDialog(null, "Usage time has not been changed in database.",
                            "Usage time changing error", JOptionPane.ERROR_MESSAGE);
                }

                JOptionPane.showMessageDialog(null, "Usage time has been updated in database.",
                        "Usage time upadated", JOptionPane.INFORMATION_MESSAGE);

            }
        });
    }

    //Getter and setter methods
    public JPanel getEquipmentsRootPanel() { return equipment; }

    public int getEquipmentId() {
        return equipmentId;
    }

    public String getEquipmentName(){
        return equipmentsName.getText();
    }

    public int getHoursOfUse(){ return (int) hoursUseInput.getValue(); }

    public int getMinutesOfUse(){ return (int) minutesUseInput.getValue(); }

    public String getMaintenancesDates() { return this.maintenancesDates.getText(); }

    public String getEquipmentLocation(){ return this.location; }

    public String getEquipmentYear(){
        return equipmentsYear.getText();
    }

    public String getEquipmentDescription(){
        return equipmentsDescription.getText();
    }

    public String getEquipmentCategory(){
        return equipmentsCategory.getText();
    }

    public String getEquipmentSupplier(){
        return supplier.getText();
    }

    public String getEquipmentSerialNumber(){
        return serialNumberEquipment.getText();
    }

    public void setEquipmentsName(String name) { this.equipmentsName.setText(name); }

    public void setEquipmentsCategory(String category) { this.equipmentsCategory.setText(category); }

    public void setEquipmentsYear(String year) { this.equipmentsYear.setText(year); }

    public void setEquipmentsDescription(String description) { this.equipmentsDescription.setText(description); }

    public void setEquipmentsSupplier(String supplier) { this.supplier.setText(supplier); }

    public void setEquipmentsSerialNumber(String serialNumber) { this.serialNumberEquipment.setText(serialNumber); }

    public void setEquipmentLocation(String location) { this.location = location; }

    public void setMaintenancesDates(String date) { this.maintenancesDates.setText(date); }

    public void setEquipmentsId(int id) { this.equipmentId = id; }

    //Custom create of some graphical components
    private void createUIComponents() {
        //Custom create
        SpinnerNumberModel spinnerModelHours = new SpinnerNumberModel(0,0,99999,1);
        SpinnerNumberModel spinnerModelMinutes = new SpinnerNumberModel(0,0,59,1);
        hoursUseInput = new JSpinner(spinnerModelHours);
        minutesUseInput = new JSpinner(spinnerModelMinutes);
    }

    /**
     * Resize a given image
     * @param image: Image given
     * @return A resized version of the image (size: 70*70)
     */
    public ImageIcon ResizeImage(ImageIcon image){
        Image img = image.getImage();
        Image newImg = img.getScaledInstance(70, 70, Image.SCALE_SMOOTH);

        return new ImageIcon(newImg);
    }
}
