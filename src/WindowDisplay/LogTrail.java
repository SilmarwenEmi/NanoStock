package WindowDisplay;

import NanostockException.NotConnectedException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;

import static DataBase.ExtractData.countRowsAlerts;

/**
 * @author Emilie Laurent
 * Display all the log on this page
 */
public class LogTrail {
    private JPanel rootPanelLogTrail;
    private JPanel navigatorPanel;
    private JPanel collapsedBar;

    private JButton logoutButton;
    private JButton alertButton;
    private JButton userIDButton;
    private JButton switchToStock;
    private JButton switchToAlerts;
    private JButton switchToAdmin;
    private JButton collapseButton;
    private JButton expandButton;

    private JPanel descriptionPanel;
    private JPanel typePanel;
    private JPanel datePanel;
    private JPanel timePanel;
    private JPanel userPanel;
    private JButton deleteLogButton;
    private JButton returnHomeButton;

    /**
     * Construct the log trail page with given information
     * @param frame: Main frame of the software
     * @param user: The active user to know his privileges
     */
    public LogTrail(JFrame frame, User user){

        userIDButton.setText(user.getUserName() + " " + user.getUserFirstName());

        //Display alerts logo if there is alerts or not
        boolean isThereAlerts = false;
        try {
            isThereAlerts = countRowsAlerts(user, 0) > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if ( isThereAlerts ){
            ImageIcon newAlertsImage = new ImageIcon(this.getClass().getResource("/Pictures/newAlerts.png"));
            alertButton.setIcon(newAlertsImage);
        } else {
            ImageIcon noAlertsImage = new ImageIcon(this.getClass().getResource("/Pictures/alert.png"));
            alertButton.setIcon(noAlertsImage);
        }

        //Display user picture
        if (user.getUserPicture() != null){
            userIDButton.setIcon(ResizeImageFromIcon(user.getUserPicture(), 30, 30));
        } else {
            ImageIcon defaultPictureSmall = new ImageIcon(this.getClass().getResource("/Pictures/person_random.png"));
            userIDButton.setIcon(defaultPictureSmall);
        }

        //add listener to buttons
        logoutButton.addActionListener(new ActionToDo("logout", frame,null,null, user, null, null, null, null, null));
        switchToStock.addActionListener(new ActionToDo("switchToStock", frame,null,null, user, null, null, null, null, null));
        switchToAlerts.addActionListener(new ActionToDo("switchToAlerts", frame,null,null, user, null, null, null, null, null));
        switchToAdmin.addActionListener(new ActionToDo("switchToAdmin", frame,null,null, user, null, null, null, null, null));
        alertButton.addActionListener(new ActionToDo("switchToAlerts", frame,null,null, user, null, null, null, null, null));
        userIDButton.addActionListener(new ActionToDo("switchToUserProfile",frame,null,null, user, null, null, null, null, null));
        returnHomeButton.addActionListener(new ActionToDo("switchToWhatDoing", frame, null, null, user, null, null, null, null, null));


        collapseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                collapsedBar.setVisible(true);
                navigatorPanel.setVisible(false);
            }
        });
        expandButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                collapsedBar.setVisible(false);
                navigatorPanel.setVisible(true);
            }
        });
        deleteLogButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    DataBase.DeleteData.deleteLog(user);
                } catch (NotConnectedException e1) {
                    JOptionPane.showMessageDialog(null, "Old logs have not been deleted. ",
                            "Delete log error", JOptionPane.ERROR_MESSAGE);
                }

                displayLogTrail(frame, user);
            }
        });

        //Display log
        displayLogTrail(frame, user);
    }

    //Getter methods
    public JPanel getRootPanelLogTrail(){
        return rootPanelLogTrail;
    }

    /**
     * Display all the log with all information in the correct column
     * @param frame: Main frame of the software
     * @param user: The active user to know his privileges
     */
    public void displayLogTrail(JFrame frame, User user){

        //Empty all panel to display logs from scratch
        descriptionPanel.removeAll();
        typePanel.removeAll();
        datePanel.removeAll();
        timePanel.removeAll();
        userPanel.removeAll();

        //Initialize all the panels with their layout
        descriptionPanel.setLayout(new GridBagLayout());
        GridBagConstraints descriptionLayout = new GridBagConstraints();
        descriptionLayout.fill = GridBagConstraints.BOTH;
        descriptionLayout.weightx = 1.0;
        descriptionLayout.weighty = 3.0;
        descriptionLayout.gridx = 0;
        descriptionLayout.gridy = 0;

        typePanel.setLayout(new GridBagLayout());
        GridBagConstraints typeLayout = new GridBagConstraints();
        typeLayout.fill = GridBagConstraints.BOTH;
        typeLayout.weightx = 1.0;
        typeLayout.weighty = 3.0;
        typeLayout.gridx = 0;
        typeLayout.gridy = 0;

        datePanel.setLayout(new GridBagLayout());
        GridBagConstraints dateLayout = new GridBagConstraints();
        dateLayout.fill = GridBagConstraints.BOTH;
        dateLayout.weightx = 1.0;
        dateLayout.weighty = 3.0;
        dateLayout.gridx = 0;
        dateLayout.gridy = 0;

        timePanel.setLayout(new GridBagLayout());
        GridBagConstraints timeLayout = new GridBagConstraints();
        timeLayout.fill = GridBagConstraints.BOTH;
        timeLayout.weightx = 1.0;
        timeLayout.weighty = 3.0;
        timeLayout.gridx = 0;
        timeLayout.gridy = 0;

        userPanel.setLayout(new GridBagLayout());
        GridBagConstraints userLayout = new GridBagConstraints();
        userLayout.fill = GridBagConstraints.BOTH;
        userLayout.weightx = 1.0;
        userLayout.weighty = 3.0;
        userLayout.gridx = 0;
        userLayout.gridy = 0;

        //Display all logs on all the panels
        ArrayList<LogLine> logLineList = null;
        try {
            logLineList = DataBase.ExtractData.extractAllLog(user);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "All log cannot be extracted." +
                    "An unhandled error occurs. ", "Log extraction error", JOptionPane.ERROR_MESSAGE);
        }

        if (logLineList != null) {
            Collections.reverse(logLineList);

            for (LogLine logLine: logLineList){
                descriptionPanel.add(logLine.getDescriptionLog(), descriptionLayout);
                typePanel.add(logLine.getLogTag(), typeLayout);
                datePanel.add(logLine.getEmissionDate(), dateLayout);
                timePanel.add(logLine.getHour(), timeLayout);
                userPanel.add(logLine.getUser(), userLayout);

                descriptionLayout.gridy += 1;
                typeLayout.gridy += 1;
                dateLayout.gridy += 1;
                timeLayout.gridy += 1;
                userLayout.gridy += 1;
            }
        }

        frame.repaint();
        frame.revalidate();
    }

    /**
     * Resize an image
     * @param image: Image to resize
     * @param width: Width wanted for the resized image
     * @param height: Height wanted for the resized image
     * @return A resized version of the image
     */
    public ImageIcon ResizeImageFromIcon(ImageIcon image, int width, int height){
        Image img = image.getImage();
        Image newImg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);

        return new ImageIcon(newImg);
    }
}
