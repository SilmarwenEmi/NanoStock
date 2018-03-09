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
public class NewProductName {
    private JPanel rootProductName;
    private JTextField productNameEntered;
    private JButton cancelButton;
    private JButton createButton;
    private JLabel fieldNotCompleted;

    /**
     * Construct the object NewAccessory with information given
     * @param popup: Frame where the information must be inserted
     * @param admin: Represent the admin page in the software
     * @param user: The active user with all his information
     */
    public NewProductName(JFrame popup, Admin admin, User user){

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

                if(productNameEntered.getText().length() > 0){
                    assert (admin != null);
                    Admin.addProduct(user, productNameEntered.getText(), admin);
                    popup.dispose();
                } else {
                    fieldNotCompleted.setVisible(true);
                    popup.repaint();
                    popup.revalidate();
                }

            }
        });
    }

    public JPanel getRootProductName() {
        return rootProductName;
    }

    public String getNewProductName() { return productNameEntered.getText(); }

    /**
     * Methods that launches the pop up
     * @param popup: Frame where the information must be inserted
     * @param admin: Represent the admin page of the software
     * @param user: The active user with his information
     */
    public static void newProductName(JFrame popup, Admin admin, User user){
        popup.setContentPane(new NewProductName(popup, admin, user).getRootProductName());
        popup.setLocationRelativeTo(null);
        popup.setAlwaysOnTop(true);
        popup.setPreferredSize(new Dimension(600,300));
        popup.pack();
        popup.setVisible(true);
    }
}
