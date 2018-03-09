package WindowDisplay;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.Objects;

import static DataBase.ExtractData.countRowsAlerts;

/**
 * @author Emilie Laurent
 * Represent the user profile page of the software
 */
public class UserProfile {
    private JPanel rootPanelUserProfile;

    private JButton logoutButton;
    private JButton alertButton;
    private JButton userIDButton;
    private JButton saveChanges;
    private JButton cancelButton;
    private JButton profilePictureButton;
    private JButton returnHomeButton;

    private JLabel name;
    private JLabel firstName;
    private JLabel login;
    private JLabel email;
    private JLabel differentPassword;
    private JLabel allFieldsCompletedError;
    private JLabel pictureLabel;

    private JPasswordField password;
    private JPasswordField retypedPassword;

    private File selectedFile;

    /**
     * Construct the profile page with all his information
     * @param frame: Main frame of the software
     * @param user: The active user to know his privileges
     */
    public UserProfile(JFrame frame, User user){

        userIDButton.setText(user.getUserName() + " " + user.getUserFirstName());
        selectedFile = null;

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

        if (user.getUserPicture() != null){
            pictureLabel.setIcon(ResizeImageFromIcon(user.getUserPicture(), 130, 130));
            userIDButton.setIcon(ResizeImageFromIcon(user.getUserPicture(), 30, 30));
        } else {
            ImageIcon defaultPicture = new ImageIcon(this.getClass().getResource("/Pictures/defaultProfilePicture.png"));
            ImageIcon defaultPictureSmall = new ImageIcon(this.getClass().getResource("/Pictures/person_random.png"));
            pictureLabel.setIcon(defaultPicture);
            userIDButton.setIcon(defaultPictureSmall);
        }

        name.setText(user.getUserName());
        firstName.setText(user.getUserFirstName());
        login.setText(user.getUserLogin());
        email.setText(user.getUserMail());

        //add listener to buttons
        logoutButton.addActionListener(new ActionToDo("logout", frame,null,null, user, null, null, null, null, null));
        cancelButton.addActionListener(new ActionToDo("switchToWhatDoing", frame,null,null, user, null, null, null, null, null));
        returnHomeButton.addActionListener(new ActionToDo("switchToWhatDoing", frame, null, null, user, null, null, null, null, null));
        alertButton.addActionListener(new ActionToDo("switchToAlerts", frame, null, null, user, null, null, null, null, null));

        saveChanges.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FileInputStream fileInputStream = null;
                if (selectedFile != null){
                    try {
                        fileInputStream = new FileInputStream(selectedFile);

                    } catch (FileNotFoundException e1) {
                        JOptionPane.showMessageDialog(null, "File selected has not been found to " +
                                "be saved into database. " + "File not found.", "Error occurs", JOptionPane.ERROR_MESSAGE);
                    }
                }

                if (getPassword().length() != 0 && getConfirmedPassword().length() != 0 ){
                    //Whether the two passwords are equal
                    if (Objects.equals(getPassword(), getConfirmedPassword())){
                        user.setUserPassword(getPassword());
                        try {
                            //Change password to access database
                            DataBase.UpdateData.changeUserPassword(user);

                            JOptionPane.showMessageDialog(null, "User password has successfully been changed.",
                                    "Password changed", JOptionPane.INFORMATION_MESSAGE);

                            //Update picture in database
                            if (fileInputStream != null){
                                DataBase.UpdateData.updatePicture(user, fileInputStream);
                                DataBase.ExtractData.refreshUserPicture(user);
                            }

                        } catch (SQLException e1) {
                            JOptionPane.showMessageDialog(null, "Password has not been changed. " +
                                    "An unhandled error occurs.", "Error occurs", JOptionPane.ERROR_MESSAGE);
                        }

                        frame.setContentPane(new WhatDoing(frame, user).getRootPanelWhatDoing());
                        frame.repaint();
                        frame.revalidate();

                    } else {

                        //Whether different passwords typed
                        differentPassword.setVisible(true);
                        allFieldsCompletedError.setVisible(false);
                        frame.repaint();
                        frame.revalidate();
                    }

                } else if (fileInputStream != null){
                    //Update picture in database, if change picture and do not change passwords
                    try {
                        DataBase.UpdateData.updatePicture(user, fileInputStream);
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }

                    try {
                        DataBase.ExtractData.refreshUserPicture(user);
                    } catch (SQLException e1) {
                        JOptionPane.showMessageDialog(null, "User picture cannot be refreshed." +
                                "An unhandled error occurs. ", "User picture refreshment error", JOptionPane.ERROR_MESSAGE);
                    }

                    frame.setContentPane(new UserProfile(frame, user).getRootPanelUserProfile());
                    frame.repaint();
                    frame.revalidate();

                } else {
                    //If cancel, return to main page
                    frame.setContentPane(new WhatDoing(frame, user).getRootPanelWhatDoing());
                    frame.repaint();
                    frame.revalidate();
                }
            }
        });
        profilePictureButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser file = new JFileChooser();
                file.setCurrentDirectory(new File(System.getProperty("user.home")));
                FileNameExtensionFilter filter = new FileNameExtensionFilter("*.Images",
                        "jpg", "png");
                file.addChoosableFileFilter(filter);

                int result = file.showSaveDialog(null);

                if (result == JFileChooser.APPROVE_OPTION){
                    selectedFile = file.getSelectedFile();
                    pictureLabel.setIcon(ResizeImageFromPath(selectedFile.getAbsolutePath()));

                } else if(result == JFileChooser.CANCEL_OPTION){
                    JOptionPane.showMessageDialog(null, "No image file selected.",
                            "Image selection error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

    }

    //Getter methods
    public JPanel getRootPanelUserProfile() {
        return rootPanelUserProfile;
    }

    public String getPassword(){ return password.getText(); }

    public String getConfirmedPassword(){ return retypedPassword.getText(); }

    /**
     * Resize a given image from its path
     * @param imagePath: Path of the image to resize
     * @return A resized image of the given image
     */
    public ImageIcon ResizeImageFromPath(String imagePath){
        ImageIcon image = new ImageIcon(imagePath);
        Image img = image.getImage();
        Image newImg = img.getScaledInstance(pictureLabel.getWidth(), pictureLabel.getHeight(), Image.SCALE_SMOOTH);

        return new ImageIcon(newImg);
    }

    /**
     * Resize a given image
     * @param image: Image to resize
     * @param width: Width wanted of resized image
     * @param height: Height wanted of resized image
     * @return Resized image with size asked
     */
    public ImageIcon ResizeImageFromIcon(ImageIcon image, int width, int height){
                Image img = image.getImage();
        Image newImg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);

        return new ImageIcon(newImg);
    }
}
