package PopUp;

import DataBase.UpdateData;
import NanostockException.NotConnectedException;
import WindowDisplay.Equipment;
import WindowDisplay.StockMangement;
import WindowDisplay.User;

import javax.swing.*;
import javax.swing.text.DateFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;

/**
 * @author Emilie Laurent
 * Contains all the methods and information about the New Location popup
 */
public class NewMaintenance extends JPanel {
    private JPanel newMaintenanceRoot;
    private JButton createButton;
    private JButton cancelButton;
    private JLabel fieldNotCompleted;
    private JFormattedTextField maintenanceDateField;
    private String dates;

    /**
     * Construct the object NewLocation with information given
     * @param frame: Represent the main frame of the software
     * @param popup: Frame where the information must be inserted
     * @param user: The active user with all his information
     * @param equipment: The equipment of which we add new maintenance
     */
    public NewMaintenance(JFrame frame, JFrame popup, User user, Equipment equipment){

        //Add listeners to buttons
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                popup.dispose();
            }
        });


        createButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if(maintenanceDateField.getText().length() != 0){

                    try {
                        dates = equipment.getMaintenancesDates() + "\n" + maintenanceDateField.getText();
                        DataBase.UpdateData.updateEquipmentMaintenances(user, equipment.getEquipmentId(), dates);

                    } catch (SQLException e1) {
                        JOptionPane.showMessageDialog(null, "New maintenance date has not been inserted. " +
                                "An unhandled error occurs.", "Error occurs", JOptionPane.INFORMATION_MESSAGE);
                    }

                    equipment.setMaintenancesDates(dates);
                    frame.repaint();
                    frame.revalidate();
                    popup.dispose();

                    JOptionPane.showMessageDialog(null, "New maintenance date has been successfully inserted. ",
                            "New maintenance date inserted", JOptionPane.INFORMATION_MESSAGE);

                    try {
                        UpdateData.updateLastMaintenance(user, equipment, maintenanceDateField.getText());
                    } catch (SQLException e1) {
                        JOptionPane.showMessageDialog(null, "Last maintenance has " +
                                "been successfully added.", "Equipment maintenance added", JOptionPane.INFORMATION_MESSAGE);
                    }

                } else {
                    fieldNotCompleted.setVisible(true);
                }
            }
        });
    }


    public JPanel getNewMaintenanceRoot() { return newMaintenanceRoot; }

    /**
     * Methods that launches the pop up
     * @param frame: Represent the main frame of the software
     * @param popup: Frame where the information must be inserted
     * @param user: The active user with all his information
     * @param equipment: The equipment of which we add new maintenance
     */
    public static void newMaintenanceDate(JFrame frame,JFrame popup, User user, Equipment equipment){
        popup.setContentPane(new NewMaintenance(frame, popup, user, equipment).getNewMaintenanceRoot());
        popup.setLocationRelativeTo(null);
        popup.setAlwaysOnTop(true);
        popup.setPreferredSize(new Dimension(600,300));
        popup.pack();
        popup.setVisible(true);
    }

    //Custom create some graphical component
    private void createUIComponents() throws ParseException {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        DateFormatter df = new DateFormatter(format);
        this.maintenanceDateField = new JFormattedTextField(df);
        LocalDateTime day = LocalDateTime.now();
        this.maintenanceDateField.setValue(format.parse(day.toString()));
    }
}
