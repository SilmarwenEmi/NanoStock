package WindowDisplay;

import NanostockException.NotConnectedException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.Objects;

/**
 * @author Emilie Laurent
 * Represent one line with all user information to be displayed on add/remove user page
 */
public class UserLine extends JPanel{
    private JPanel rootPanel;

    private JButton deleteButton;
    private JButton privilegesButton;

    private JLabel name;
    private JLabel firstName;
    private JLabel login;
    private JLabel mail;

    private ImageIcon adminPrivileges = new ImageIcon(this.getClass().getResource("/Pictures/privilegesGranted.png"));
    private ImageIcon userPrivileges = new ImageIcon(this.getClass().getResource("/Pictures/privilegesRevoked.png"));


    /**
     * Construct one line with one user information
     * @param frame: Main frame of the software
     * @param user: All information of the user to display
     * @param currentUser: The active user to know his privileges
     */
    public UserLine(JFrame frame, User user, User currentUser){
        JPanel userLineRoot = rootPanel;

        this.name.setText(user.getUserName());

        this.firstName.setText(user.getUserFirstName());

        this.login.setText(user.getUserLogin());

        this.mail.setText(user.getUserMail());

        if(Objects.equals(user.getUserPrivileges(), "Admin")){
            privilegesButton.setIcon(adminPrivileges);
        } else {
            privilegesButton.setIcon(userPrivileges);
        }

        setBackground(Color.WHITE);

        setLayout(new GridLayout(0, 1));
        add(userLineRoot);


        //Add listener to buttons
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //JOptionPane confirmLowQuantityAction = new JOptionPane();
                int optionDeleteUser = JOptionPane.showConfirmDialog(null, "Are you sure you want " +
                                "to delete user \" " + user.getUserLogin() + " \" ?", "Delete user confirmation",
                        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                //If Yes, delete user
                if(optionDeleteUser == 0){
                    try {
                        DataBase.DeleteData.dropUserConnection(currentUser, user);
                        DataBase.DeleteData.deleteUser(currentUser, user);

                        JOptionPane.showMessageDialog(null, "User " + user.getUserLogin() + " has been " +
                                "successfully deleted. ", "User deleted", JOptionPane.INFORMATION_MESSAGE);

                    } catch (NotConnectedException e1) {
                        JOptionPane.showMessageDialog(null, "User " + user.getUserLogin() +" has not been deleted. " +
                                "An unhandled error occurs.", "Error occurs", JOptionPane.ERROR_MESSAGE);
                    }


                    //Refresh admin page
                    frame.setContentPane(new Admin(frame, currentUser).getRootPanelAdmin());
                    frame.repaint();
                    frame.revalidate();

                }
            }
        });
        privilegesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(Objects.equals(user.getUserLogin(), currentUser.getUserLogin())){
                    //Admin cannot change his own privileges
                    JOptionPane.showMessageDialog(null, "You cannot change your own privileges ",
                            "Cannot change your own privileges", JOptionPane.INFORMATION_MESSAGE);

                } else {
                    int optionDeleteUser = JOptionPane.showConfirmDialog(null, "Are you sure you want " +
                                    "to change user \" " + user.getUserLogin() + " \" privileges ?", "Change user privileges confirmation",
                            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

                    if (optionDeleteUser == 0) {
                        //If changement confirmed
                        if(Objects.equals(user.getUserPrivileges(), "Admin")){
                            //Get user privileges
                            try {
                                DataBase.DeleteData.revokeAllPrivileges(currentUser, user);
                                DataBase.InsertData.getUserPrivilege(currentUser, user);

                                JOptionPane.showMessageDialog(null, "User has successfully been revoked " +
                                        "admin privileges. ", "Admin privileges revoked", JOptionPane.INFORMATION_MESSAGE);


                                DataBase.UpdateData.updateUserPrivileges(currentUser, user, "User");
                                user.setUserPrivileges("User");

                                privilegesButton.setIcon(userPrivileges);

                            } catch (SQLException e1) {
                                JOptionPane.showMessageDialog(null, "User has not been revoked admin privileges. " +
                                        "An unhandled error occurs. ", "Revoke admin privileges error", JOptionPane.ERROR_MESSAGE);
                            }

                        } else {

                            //Get admin privileges
                            try {
                                DataBase.InsertData.getAdminPrivilege(currentUser, user);

                                JOptionPane.showMessageDialog(null, "User has successfully been granted to " +
                                        "admin privileges. ", "Admin privileges granted", JOptionPane.INFORMATION_MESSAGE);

                                DataBase.UpdateData.updateUserPrivileges(currentUser, user, "Admin");
                                user.setUserPrivileges("Admin");

                                privilegesButton.setIcon(adminPrivileges);

                            } catch (SQLException e1) {
                                JOptionPane.showMessageDialog(null, "User has not been granted admin privileges. " +
                                        "An unhandled error occurs. ", "Grant admin privileges error", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    }
                }
            }
        });
    }

    //Getter methods
    public JButton getDeleteButton() {
        return deleteButton;
    }

    public JLabel getUserLineName() {
        return name;
    }

    public JLabel getUserLineFirstName(){
        return firstName;
    }

    public JLabel getUserLineMail(){
        return mail;
    }

    public JLabel getUserLineLogin(){
        return login;
    }

    public JButton getUserLinePrivileges() { return privilegesButton; }


}
