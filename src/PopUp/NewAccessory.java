package PopUp;

import WindowDisplay.User;
import WindowDisplay.Admin;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author Emilie Laurent
 * Contains all the methods and information about the New Accessory popup
 */
public class NewAccessory {
    private JPanel rootNewAccessory;
    private JTextField accessoryName;
    private JButton createButton;
    private JButton cancelButton;
    private JLabel fieldNotCompleted;

    /**
     * Construct the object NewAccessory with information given
     * @param popup: Frame where the information must be inserted
     * @param user: The active user with all his information
     * @param admin: Represent the admin page in the software
     */
    public NewAccessory(JFrame popup, User user, Admin admin) {

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
                if(accessoryName.getText().length() > 0){
                    assert (admin != null);
                    Admin.addAccessory(user, accessoryName.getText(), admin);
                    popup.dispose();
                } else {
                    fieldNotCompleted.setVisible(true);
                    popup.repaint();
                    popup.revalidate();
                }
            }
        });
    }

    public JPanel getRootNewAccessory() {
        return rootNewAccessory;
    }

    /**
     * Method that launches the pop up
     * @param popup: Frame where the information must be inserted
     * @param user: The active user with his information
     * @param admin: Represent the admin page in the software
     */
    public static void newAccessory(JFrame popup, User user, Admin admin){
        popup.setContentPane(new NewAccessory(popup, user, admin).getRootNewAccessory());
        popup.setLocationRelativeTo(null);
        popup.setAlwaysOnTop(true);
        popup.setPreferredSize(new Dimension(600,300));
        popup.pack();
        popup.setVisible(true);
    }
}
