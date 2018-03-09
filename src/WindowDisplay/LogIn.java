package WindowDisplay;

import javax.swing.*;
import java.io.File;

/**
 * @author Emilie Laurent
 * The log in page of the software
 */
public class LogIn extends JPanel {
    private JPanel rootPanelLogIn;
    private JPasswordField password;
    private JButton loginButton;
    private JLabel connectionErrorLabel;
    private JTextField login;


    /**
     * Construct the login object
     * @param frame: The main frame of the software
     */
    public LogIn(JFrame frame){
        //add listeners to buttons
        User user = new User();
        loginButton.addActionListener(new ActionToDo("login", frame,null,this, user, null, null, null, null, null));

    }

    //Getter methods
    public JPanel getRootPanelLogIn() {
        return rootPanelLogIn;
    }

    public String getLogin(){ return login.getText(); }

    @Deprecated
    public String getPassword() {
        return password.getText();
    }

    public void updateVisibilityConnectionError(boolean visibility){
        connectionErrorLabel.setVisible(visibility);
    }

    /**
     * Launches the login page
     * @param frame: Main frame of the software
     */
    public static void login(JFrame frame){

        //initialize the main panel
        frame.setContentPane(new LogIn(frame).getRootPanelLogIn());

        //kill process on close
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        //display the window in the center and full size
        frame.setLocationRelativeTo(null);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.pack();
        frame.setVisible(true);
    }


}
