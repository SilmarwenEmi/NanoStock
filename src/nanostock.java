import javax.swing.*;

/**
 * @author Emilie Laurent
 * @overview Contains the main method to run the program
 */
public class nanostock {

    /**
     * Main method of the program
     * @param args: Command-line arguments
     */
    public static void main(String args[]){
        JFrame frame = new JFrame("NanoStock");

        //Launches the log in page
        WindowDisplay.LogIn.login(frame);
    }

}
