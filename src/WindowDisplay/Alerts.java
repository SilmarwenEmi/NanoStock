package WindowDisplay;

import NanostockException.NotConnectedException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Array;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;

import static DataBase.ExtractData.countRowsAlerts;

/**
 * @author Emilie Laurent
 * This class represent the alert page of the software
 */
public class Alerts {
    private JPanel rootPanelAlerts;
    private JPanel alertsDataNavigatorPanel;
    private JPanel navigatorPanel;
    private JPanel collapsedBar;
    private JPanel alertsDataPanel;
    private JPanel namePanel;
    private JPanel categoryPanel;
    private JPanel locationPanel;
    private JPanel datePanel;
    private JPanel alertTagPanel;
    private JPanel moreButtonPanel;
    private JPanel idNumberPanel;
    private JPanel archiveAlertPanel;
    private JPanel nameArchivedPanel;
    private JPanel categoryArchivedPanel;
    private JPanel dateArchivedPanel;
    private JPanel locationArchivedPanel;
    private JPanel tagArchivedPanel;
    private JPanel alertIdArchivedPanel;
    private JButton deleteArchivedAlertsButton;

    private JButton logoutButton;
    private JButton alertButton;
    private JButton userIDButton;
    private JButton switchToStock;
    private JButton switchToAdmin;
    private JButton switchToLog;
    private JButton collapseButton;
    private JButton expandButton;
    private JButton returnHomeButton;
    private JPanel deleteAlertPanel;

    /**
     * Construct the alert object with given information
     * @param frame: Main frame of the software
     * @param user: The active user to know his privileges
     * @param sm: Stock management page of the software
     */
    public Alerts(JFrame frame, User user, StockMangement sm) {

        userIDButton.setText(user.getUserName() + " " + user.getUserFirstName());

        boolean isThereAlerts = false;
        try {
            isThereAlerts = countRowsAlerts(user, 0) > 0;
        } catch (SQLException e) {
            System.out.println("error count new alert stock management");
        }

        if ( isThereAlerts ){
            ImageIcon newAlertsImage = new ImageIcon(this.getClass().getResource("/Pictures/newAlerts.png"));
            alertButton.setIcon(newAlertsImage);
        } else {
            ImageIcon noAlertsImage = new ImageIcon(this.getClass().getResource("/Pictures/alert.png"));
            alertButton.setIcon(noAlertsImage);
        }

        if (user.getUserPicture() != null){
            userIDButton.setIcon(ResizeImageFromIcon(user.getUserPicture(), 30, 30));
        } else {
            ImageIcon defaultPictureSmall = new ImageIcon(this.getClass().getResource("/Pictures/person_random.png"));
            userIDButton.setIcon(defaultPictureSmall);
        }

        //add listener to buttons
        logoutButton.addActionListener(new ActionToDo("logout", frame,null, null, user, null, null, sm, null, null));
        switchToStock.addActionListener(new ActionToDo("switchToStock", frame,null,null, user, null, null, sm, null, null));
        switchToLog.addActionListener(new ActionToDo("switchToLog", frame,null,null, user, null, null, sm, null, null));
        switchToAdmin.addActionListener(new ActionToDo("switchToAdmin", frame,null,null, user, null, null, sm, null, null));
        userIDButton.addActionListener(new ActionToDo("switchToUserProfile",frame,null,null, user, null, null, sm, null, null));
        returnHomeButton.addActionListener(new ActionToDo("switchToWhatDoing", frame, null, null, user, null, null, null, null, null));


        if (Objects.equals(user.getUserPrivileges(), "Admin")){
            switchToLog.setVisible(true);
            switchToAdmin.setVisible(true);
            deleteAlertPanel.setVisible(true);
        } else {
            switchToLog.setVisible(false);
            switchToAdmin.setVisible(false);
            deleteAlertPanel.setVisible(false);
        }

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

        deleteArchivedAlertsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try {
                    DataBase.DeleteData.deleteArchivedAlerts(user);
                } catch (NotConnectedException e1) {
                    JOptionPane.showMessageDialog(null, "Archived alerts have not been deleted. ",
                            "Delete archived alerts error", JOptionPane.ERROR_MESSAGE);
                }

                displayArchivedAlerts(user, frame, sm);

            }
        });

        //Display new alerts
        displayNewAlerts(user, frame, sm);

        //Display archived alerts
        displayArchivedAlerts(user, frame, sm);

    }


    //Getter methods
    public JPanel getRootPanelAlerts(){
        return rootPanelAlerts;
    }

    public JPanel getAlertsDataPanel() {
        return alertsDataPanel;
    }

    public JPanel getAlertsDataNavigatorPanel() {
        return alertsDataNavigatorPanel;
    }

    /**
     * Display the new alerts in the alert page
     * @param user: The active user to know his privileges
     * @param frame: Main frame of the software
     * @param sm: Stock management page of the software
     */
    public void displayNewAlerts(User user, JFrame frame, StockMangement sm){

        namePanel.removeAll();
        categoryPanel.removeAll();
        locationPanel.removeAll();
        datePanel.removeAll();
        moreButtonPanel.removeAll();
        alertTagPanel.removeAll();
        idNumberPanel.removeAll();
        archiveAlertPanel.removeAll();

        namePanel.setLayout(new GridBagLayout());
        GridBagConstraints nameLayout = new GridBagConstraints();
        nameLayout.fill = GridBagConstraints.BOTH;
        nameLayout.weightx = 1.0;
        nameLayout.weighty = 2;
        nameLayout.gridx = 0;
        nameLayout.gridy = 0;

        categoryPanel.setLayout(new GridBagLayout());
        GridBagConstraints categoryLayout = new GridBagConstraints();
        categoryLayout.fill = GridBagConstraints.BOTH;
        categoryLayout.weightx = 1.0;
        categoryLayout.weighty = 2;
        categoryLayout.gridx = 0;
        categoryLayout.gridy = 0;

        locationPanel.setLayout(new GridBagLayout());
        GridBagConstraints locationLayout = new GridBagConstraints();
        locationLayout.fill = GridBagConstraints.BOTH;
        locationLayout.weightx = 1.0;
        locationLayout.weighty = 2;
        locationLayout.gridx = 0;
        locationLayout.gridy = 0;

        datePanel.setLayout(new GridBagLayout());
        GridBagConstraints dateLayout = new GridBagConstraints();
        dateLayout.fill = GridBagConstraints.BOTH;
        dateLayout.weightx = 1.0;
        dateLayout.weighty = 2;
        dateLayout.gridx = 0;
        dateLayout.gridy = 0;

        alertTagPanel.setLayout(new GridBagLayout());
        GridBagConstraints tagLayout = new GridBagConstraints();
        tagLayout.fill = GridBagConstraints.BOTH;
        tagLayout.weightx = 1.0;
        tagLayout.weighty = 2;
        tagLayout.gridx = 0;
        tagLayout.gridy = 0;

        moreButtonPanel.setLayout(new GridBagLayout());
        GridBagConstraints moreLayout = new GridBagConstraints();
        moreLayout.fill = GridBagConstraints.BOTH;
        moreLayout.weightx = 1.0;
        moreLayout.weighty = 2;
        moreLayout.gridx = 0;
        moreLayout.gridy = 0;

        idNumberPanel.setLayout(new GridBagLayout());
        GridBagConstraints idNumberLayout = new GridBagConstraints();
        idNumberLayout.fill = GridBagConstraints.BOTH;
        idNumberLayout.weightx = 1.0;
        idNumberLayout.weighty = 2;
        idNumberLayout.gridx = 0;
        idNumberLayout.gridy = 0;

        archiveAlertPanel.setLayout(new GridBagLayout());
        GridBagConstraints archiveLayout = new GridBagConstraints();
        archiveLayout.fill = GridBagConstraints.BOTH;
        archiveLayout.weightx = 1.0;
        archiveLayout.weighty = 2;
        archiveLayout.gridx = 0;
        archiveLayout.gridy = 0;

        ArrayList<AlertLine> newAlertsList = null;

        //Extract all new alerts
        try {
            newAlertsList = DataBase.ExtractData.extractAllNewAlerts(user, frame, sm);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "New alerts have not been extracted. ",
                    "Extraction new alerts error", JOptionPane.ERROR_MESSAGE);
        }

        //Display all new alerts
        assert newAlertsList != null;
        for (AlertLine alertLine: newAlertsList){

            namePanel.add(alertLine.getAlertName(), nameLayout);
            categoryPanel.add(alertLine.getAlertCategory(), categoryLayout);
            locationPanel.add(alertLine.getAlertLocation(), locationLayout);
            datePanel.add(alertLine.getAlertDate(), dateLayout);
            alertTagPanel.add(alertLine.getAlertTag(), tagLayout);
            moreButtonPanel.add(alertLine.getAlertMoreButton(), moreLayout);
            idNumberPanel.add(alertLine.getAlertId(), idNumberLayout);
            archiveAlertPanel.add(alertLine.getAlertDoneButton(), archiveLayout);

            nameLayout.gridy += 1;
            categoryLayout.gridy += 1;
            locationLayout.gridy += 1;
            dateLayout.gridy += 1;
            tagLayout.gridy += 1;
            moreLayout.gridy += 1;
            idNumberLayout.gridy += 1;
            archiveLayout.gridy += 1;

        }

        frame.repaint();
        frame.revalidate();

    }

    /**
     * Display all the archived alerts in the alert page
     * @param user: The active user to know his privileges
     * @param frame: Main frame of the software
     * @param sm: Stock management page
     */
    public void displayArchivedAlerts(User user, JFrame frame, StockMangement sm){

        nameArchivedPanel.removeAll();
        categoryArchivedPanel.removeAll();
        locationArchivedPanel.removeAll();
        dateArchivedPanel.removeAll();
        tagArchivedPanel.removeAll();
        alertIdArchivedPanel.removeAll();

        nameArchivedPanel.setLayout(new GridBagLayout());
        GridBagConstraints nameArchivedLayout = new GridBagConstraints();
        nameArchivedLayout.fill = GridBagConstraints.BOTH;
        nameArchivedLayout.weightx = 1.0;
        nameArchivedLayout.weighty = 2;
        nameArchivedLayout.gridx = 0;
        nameArchivedLayout.gridy = 0;

        categoryArchivedPanel.setLayout(new GridBagLayout());
        GridBagConstraints categoryArchivedLayout = new GridBagConstraints();
        categoryArchivedLayout.fill = GridBagConstraints.BOTH;
        categoryArchivedLayout.weightx = 1.0;
        categoryArchivedLayout.weighty = 2;
        categoryArchivedLayout.gridx = 0;
        categoryArchivedLayout.gridy = 0;

        locationArchivedPanel.setLayout(new GridBagLayout());
        GridBagConstraints locationArchivedLayout = new GridBagConstraints();
        locationArchivedLayout.fill = GridBagConstraints.BOTH;
        locationArchivedLayout.weightx = 1.0;
        locationArchivedLayout.weighty = 2;
        locationArchivedLayout.gridx = 0;
        locationArchivedLayout.gridy = 0;

        dateArchivedPanel.setLayout(new GridBagLayout());
        GridBagConstraints dateArchivedLayout = new GridBagConstraints();
        dateArchivedLayout.fill = GridBagConstraints.BOTH;
        dateArchivedLayout.weightx = 1.0;
        dateArchivedLayout.weighty = 2;
        dateArchivedLayout.gridx = 0;
        dateArchivedLayout.gridy = 0;

        tagArchivedPanel.setLayout(new GridBagLayout());
        GridBagConstraints tagArchivedLayout = new GridBagConstraints();
        tagArchivedLayout.fill = GridBagConstraints.BOTH;
        tagArchivedLayout.weightx = 1.0;
        tagArchivedLayout.weighty = 2;
        tagArchivedLayout.gridx = 0;
        tagArchivedLayout.gridy = 0;

        alertIdArchivedPanel.setLayout(new GridBagLayout());
        GridBagConstraints idArchivedLayout = new GridBagConstraints();
        idArchivedLayout.fill = GridBagConstraints.BOTH;
        idArchivedLayout.weightx = 1.0;
        idArchivedLayout.weighty = 2;
        idArchivedLayout.gridx = 0;
        idArchivedLayout.gridy = 0;

        ArrayList<ArchivedAlertLine> archivedAlertListProduct = null;

        //Extract all archived alerts
        try {
            archivedAlertListProduct = DataBase.ExtractData.extractAllArchivedAlerts(user, frame, sm);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Archived alerts have not been extracted. ",
                    "Extraction archived alerts error", JOptionPane.ERROR_MESSAGE);
        }

        //Display products archived alerts
        assert archivedAlertListProduct != null;
        for (ArchivedAlertLine archivedAlertLine: archivedAlertListProduct){

            nameArchivedPanel.add(archivedAlertLine.getArchivedAlertName(), nameArchivedLayout);
            categoryArchivedPanel.add(archivedAlertLine.getArchivedAlertCategory(), categoryArchivedLayout);
            locationArchivedPanel.add(archivedAlertLine.getArchivedAlertLocation(), locationArchivedLayout);
            dateArchivedPanel.add(archivedAlertLine.getArchivedAlertDate(), dateArchivedLayout);
            tagArchivedPanel.add(archivedAlertLine.getArchivedAlertTag(), tagArchivedLayout);
            alertIdArchivedPanel.add(archivedAlertLine.getArchivedAlertId(), idArchivedLayout);

            nameArchivedLayout.gridy += 1;
            categoryArchivedLayout.gridy += 1;
            locationArchivedLayout.gridy += 1;
            dateArchivedLayout.gridy += 1;
            tagArchivedLayout.gridy += 1;
            idArchivedLayout.gridy += 1;

        }


        frame.repaint();
        frame.revalidate();
    }

    /**
     * Resize a given image
     * @param image: Image to resize
     * @param width: Width wanted of the image new version
     * @param height: Height wanted of the image new version
     * @return New version of the image resized
     */
    public ImageIcon ResizeImageFromIcon(ImageIcon image, int width, int height){
        Image img = image.getImage();
        Image newImg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);

        return new ImageIcon(newImg);
    }

}
