package PopUp;

import WindowDisplay.User;
import NanostockException.NotConnectedException;
import WindowDisplay.Admin;
import WindowDisplay.Location;
import org.junit.Test;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

/**
 * @author Emilie Laurent
 * Contains all the methods and information about the New Location popup
 */
public class NewLocation {
    private JPanel rootNewLocation;

    private JButton createButton;
    private JButton cancelButton;

    private JTextField newLocationName;

    private JRadioButton locationLogo1;
    private JRadioButton locationLogo2;
    private JRadioButton locationLogo3;
    private JRadioButton locationLogo4;
    private JRadioButton locationLogo5;

    private JLabel fieldNotCompleted;


    /**
     * Construct the object NewLocation with information given
     * @param popup: Frame where the information must be inserted
     * @param user: The active user with all his information
     * @param frame: Represent the main frame of the software
     * @param admin: Represent the admin page in the software
     */
    public NewLocation(JFrame popup, User user, JFrame frame, Admin admin) {

        //To select only one JRadioButton
        ButtonGroup selectionLocationLogo = new ButtonGroup();
        selectionLocationLogo.add(locationLogo1);
        selectionLocationLogo.add(locationLogo2);
        selectionLocationLogo.add(locationLogo3);
        selectionLocationLogo.add(locationLogo4);
        selectionLocationLogo.add(locationLogo5);


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
                String logoSelected = "";
                if(newLocationName.getText().length() > 0){
                    try {
                        if (locationLogo1.isSelected()){
                            logoSelected = "/Pictures/Location1.png";
                        } else if (locationLogo2.isSelected()){
                            logoSelected = "/Pictures/Location2.png";
                        } else if (locationLogo3.isSelected()){
                            logoSelected = "/Pictures/Location3.png";
                        } else if (locationLogo4.isSelected()){
                            logoSelected = "/Pictures/Location4.png";
                        } else if (locationLogo5.isSelected()){
                            logoSelected = "/Pictures/Location5.png";
                        }

                        Location location = new Location(frame, newLocationName.getText(), logoSelected, null, "product", user, "");
                        DataBase.InsertData.createNewLocation(user, location);
                        assert (admin!= null);
                        //Add the new location to the list
                        WindowDisplay.Admin.addLocationItem(user, admin);

                        frame.repaint();
                        frame.revalidate();


                    } catch (NotConnectedException | SQLException e1) {
                        JOptionPane.showMessageDialog(null, "New location has not been created." +
                                "An unhandled error occurs. ", "New location error", JOptionPane.ERROR_MESSAGE);
                    }
                    DataBase.InsertData.insertNewLog(user, "Location \" " + newLocationName.getText() + " \" added" , "LocAdded");
                    popup.dispose();

                } else {
                    fieldNotCompleted.setVisible(true);
                    popup.repaint();
                    popup.revalidate();
                }

            }
        });
    }

    public JPanel getRootNewLocation() {
        return rootNewLocation;
    }

    /**
     * Methods that launches the pop up
     * @param popup: Frame where the information must be inserted
     * @param user: The active user with his information
     * @param frame: Represent the main frame of the software
     * @param admin: Represent the admin page of the software
     */
    public static void newLocation(JFrame popup, User user, JFrame frame, Admin admin){
        popup.setContentPane(new NewLocation(popup, user, frame, admin).getRootNewLocation());
        popup.setLocationRelativeTo(null);
        popup.setAlwaysOnTop(true);
        popup.setPreferredSize(new Dimension(600,300));
        popup.pack();
        popup.setVisible(true);
    }

}
