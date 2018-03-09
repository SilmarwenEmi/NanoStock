package PopUp;

import WindowDisplay.User;
import WindowDisplay.Admin;
import WindowDisplay.Supplier;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

/**
 * @author Emilie Laurent
 * Contains all the methods and information about the New Accessory popup
 */
public class NewSupplier {
    private JPanel rootSupplier;

    private JTextField supplierName;
    private JTextField contactName;
    private JTextField contactPhone;

    private JButton createButton;
    private JButton cancelButton;

    private JLabel allFieldsCompletedError;

    /**
     * Construct the object NewAccessory with information given
     * @param popup: Frame where the information must be inserted
     * @param user: The active user with all his information
     * @param admin: Represent the admin page in the software
     * @param frame: Represent the main frame of the software
     */
    public NewSupplier(JFrame popup, User user, Admin admin, JFrame frame){

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

                if(supplierName.getText().length() > 0 && contactName.getText().length() > 0 && contactPhone.getText().length() >0){
                    try {
                        Supplier supplier = new Supplier(supplierName.getText(), contactName.getText(), contactPhone.getText());
                        DataBase.InsertData.createNewSupplier(user, supplier);
                        assert (admin!= null);
                        WindowDisplay.Admin.addSupplierItem(user, admin);

                        frame.repaint();
                        frame.revalidate();

                    } catch (SQLException e1) {
                        JOptionPane.showMessageDialog(null, "New supplier has not been created." +
                                "An unhandled error occurs.", "Equipment alerts not removed", JOptionPane.ERROR_MESSAGE);
                    }

                    popup.dispose();
                } else {
                    allFieldsCompletedError.setVisible(true);
                    popup.repaint();
                    popup.revalidate();
                }
            }
        });
    }

    public JPanel getRootSupplier() {
        return rootSupplier;
    }

    /**
     * Methods that launches the pop up
     * @param popup: Frame where the information must be inserted
     * @param user: The active user with all his information
     * @param admin: Represent the admin page in the software
     * @param frame: Represent the main frame of the software
     */
    public static void newSupplier(JFrame popup, User user, Admin admin, JFrame frame){
        popup.setContentPane(new NewSupplier(popup, user, admin, frame).getRootSupplier());
        popup.setLocationRelativeTo(null);
        popup.setAlwaysOnTop(true);
        popup.setPreferredSize(new Dimension(600,300));
        popup.pack();
        popup.setVisible(true);
    }
}
