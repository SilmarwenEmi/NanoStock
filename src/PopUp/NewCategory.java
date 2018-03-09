package PopUp;

import WindowDisplay.User;
import WindowDisplay.Admin;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author Emilie Laurent
 * Contains all the methods and information about the New Accessory
 */
public class NewCategory {
    private JPanel rootEquipmentCategory;
    private JTextField categoryName;
    private JButton createButton;
    private JButton cancelButton;
    private JLabel fieldNotCompleted;

    /**
     * Construct the object NewCategory with information given
     * @param popup: Frame where the information must be inserted
     * @param admin: Represent the admin page in the software
     * @param user: The active user with all his information
     * @param whatKind: Say whether it is a product, equipment or accessory
     */
    public NewCategory(JFrame popup, Admin admin, User user, String whatKind) {

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
                if(categoryName.getText().length() > 0){
                    assert (admin != null);
                    admin.addCategory(user, categoryName.getText(), whatKind);
                    popup.dispose();
                } else {
                    fieldNotCompleted.setVisible(true);
                    popup.repaint();
                    popup.revalidate();
                }

            }
        });
    }

    public JPanel getRootNewEquipmentCategory() {
        return rootEquipmentCategory;
    }

    /**
     * Methods that launches the pop up
     * @param popup: Frame where the information must be inserted
     * @param admin: Represent the admin page in the software
     * @param user: The active user with his information
     * @param whatKind: Say whether it is a product, equipment or accessory
     */
    public static void newEquipmentCategory(JFrame popup, Admin admin, User user, String whatKind){
        popup.setContentPane(new NewCategory(popup, admin, user, whatKind).getRootNewEquipmentCategory());
        popup.setLocationRelativeTo(null);
        popup.setAlwaysOnTop(true);
        popup.setPreferredSize(new Dimension(600,300));
        popup.pack();
        popup.setVisible(true);
    }
}
