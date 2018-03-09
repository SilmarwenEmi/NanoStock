package WindowDisplay;

import javax.swing.*;

/**
 * @author Emilie Laurent
 * Represent the whatdoing page (aka main page) of the software
 */
public class WhatDoing {
    private JPanel rootPanelWhatDoing;

    private JButton roomAccessButton;
    private JButton nanoStockButton;
    private JButton roomExitButton;

    private JButton logoutButton;

    /**
     * Construct the main page of the ssoftware with information given
     * @param frame: Main frame of the software
     * @param user: The active user to know his privileges
     */
    public WhatDoing(JFrame frame, User user){

        //add listener to buttons
        logoutButton.addActionListener(new ActionToDo("logout", frame,null,null, user, null, null, null, null, null));
        nanoStockButton.addActionListener(new ActionToDo("switchToStock", frame,null,null, user, null, null, null, null, null));
        roomExitButton.addActionListener(new ActionToDo("roomExit", frame,null,null, user, null, null, null, null, null));
        roomAccessButton.addActionListener(new ActionToDo("roomAccess", frame,null,null, user, null, null, null, null, null));

        if(user.getUserRoomStatus()){
            roomAccessButton.setEnabled(false);
        }

    }

    //getter methods
    public JPanel getRootPanelWhatDoing() {
        return rootPanelWhatDoing;
    }
}
