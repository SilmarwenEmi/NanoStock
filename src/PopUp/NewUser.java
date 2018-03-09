package PopUp;

import WindowDisplay.User;
import WindowDisplay.Admin;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.Objects;

/**
 * @author Emilie Laurent
 * Contains all the methods and information about the New Accessory popup
 */
public class NewUser {

    private JPanel newUserRoot;

    private JTextField name;
    private JTextField firstName;
    private JTextField login;
    private JTextField email;

    private JPasswordField password;
    private JPasswordField confirmedPassword;

    private JButton createButton;
    private JButton cancelButton;

    private JLabel differentPassword;
    private JLabel allFieldsCompletedError;

    private User newUser;

    /**
     * Construct the object NewAccessory with information given
     * @param popup: Frame where the information must be inserted
     * @param user: The active user with all his information
     * @param frame: Represent the main frame of the software
     */
    public NewUser(JFrame popup, User user, JFrame frame) {

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

                if(getNewUserLogin().length() != 0 && getNewUserPassword().length() != 0 && getNewUserFirstName().length() != 0 && getNewUserFirstName().length() != 0 && getNewUserMail().length() != 0){

                    if(Objects.equals(getNewUserPassword(), getNewUserConfirmedPasword())){
                        differentPassword.setVisible(false);
                        newUser = new User(getNewUserLogin(),getNewUserPassword(),getNewUserName(),getNewUserFirstName(),getNewUserMail(),null, "User", null, false);

                        try {
                            DataBase.InsertData.createNewUser(user, newUser);
                            DataBase.InsertData.getUserPrivilege(user, newUser);
                            DataBase.InsertData.insertNewUser(user, newUser);

                            popup.dispose();
                            JOptionPane.showMessageDialog(null, "New user created with login "
                                    + getNewUserLogin(), "User created", JOptionPane.INFORMATION_MESSAGE);

                        } catch (SQLException e1) {
                            popup.dispose();
                            JOptionPane.showMessageDialog(null, "New user has not been created. " +
                                    "An unhandled error occurs.", "Error occurs", JOptionPane.ERROR_MESSAGE);
                        }


                        //Refresh admin page
                        frame.setContentPane(new Admin(frame, user).getRootPanelAdmin());
                        frame.repaint();
                        frame.revalidate();

                    } else {
                        //Different password error
                        differentPassword.setVisible(true);
                        allFieldsCompletedError.setVisible(false);
                        popup.repaint();
                        popup.revalidate();
                    }

                } else {
                    //All field not completed error
                    allFieldsCompletedError.setVisible(true);
                    differentPassword.setVisible(false);
                    popup.repaint();
                    popup.revalidate();
                }

            }
        });
    }


    public JPanel getNewUserRoot() {
        return this.newUserRoot;
    }

    public String getNewUserName(){ return name.getText(); }

    public String getNewUserFirstName(){ return firstName.getText(); }

    public String getNewUserMail(){ return email.getText(); }

    public String getNewUserLogin(){ return login.getText(); }

    @Deprecated
    public String getNewUserPassword(){ return password.getText(); }

    @Deprecated
    public String getNewUserConfirmedPasword(){ return confirmedPassword.getText(); }

    /**
     * Methods that launches the pop up
     * @param popup: Frame where the information must be inserted
     * @param user: The active user with all his information
     * @param frame: Represent the main frame of the software
     */
    public static void newUser(JFrame popup, User user, JFrame frame){
        popup.setContentPane(new NewUser(popup, user, frame).getNewUserRoot());
        popup.setLocationRelativeTo(null);
        popup.setAlwaysOnTop(true);
        popup.setPreferredSize(new Dimension(750,500));
        popup.pack();
        popup.setVisible(true);
    }
}
