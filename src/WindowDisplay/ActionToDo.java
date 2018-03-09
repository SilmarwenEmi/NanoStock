package WindowDisplay;

import DataBase.*;
import NanoStock.properties.TakeProperties;
import NanostockException.NotConnectedException;
import PopUp.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Objects;

import static DataBase.Connect.connectToDB;

/**
 * @author Emilie Laurent
 * This class gathers different action that can be done from different buttons
 */
public class ActionToDo implements ActionListener {

    private String actionToDo;
    private String alertId;

    private JFrame frame;
    private ImageIcon image;
    private LogIn login;
    private User userInUse;
    private Admin admin;
    private Equipment equip;
    private StockMangement sm;
    private Product prod;

    /**
     * Construct the object NewAccessory with information given
     * @param actionType: Type of the action to execute
     * @param frame: Represent the main frame of the software
     * @param image: The image needed to execute the action
     * @param login: The login page of the software
     * @param user: The active user to interact with the database and to know his privileges
     * @param admin: The admin page of the software
     * @param equipment: The equipment concerned with its characteristics
     * @param sm: The stock management page of the software
     * @param product: The product concerned with its characteristics
     * @param alertId: The identification number of the concerned alert
     */
    public ActionToDo(String actionType, JFrame frame, ImageIcon image, LogIn login, User user, Admin admin,
                      Equipment equipment, StockMangement sm, Product product, String alertId) {
        this.actionToDo = actionType;
        this.frame = frame;
        this.image = image;
        this.login = login;
        this.userInUse = user;
        this.admin = admin;
        this.equip = equipment;
        this.sm = sm;
        this.prod = product;
        this.alertId = alertId;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        switch (actionToDo){

            case "logout":
                           DataBase.InsertData.insertNewLog(userInUse, "User disconnection", "loggedOut");

                           if (userInUse.getUserRoomStatus()){
                               int optionLogout = JOptionPane.showConfirmDialog(null, "Are " +
                                               "you quitting the room ?","Logout, room exit confirmation",
                                       JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

                               if (optionLogout == 0){
                                   DataBase.InsertData.insertNewLog(userInUse, "Room exit", "RoomExit");
                                   try {
                                       UpdateData.roomExit(userInUse);
                                       userInUse.setUserRoomStatus(false);
                                   } catch (NotConnectedException e1) {
                                       JOptionPane.showMessageDialog(null, "User has not been disconnected. " +
                                               "An unhandled error occurs.", "Stock management display " +
                                               "error", JOptionPane.ERROR_MESSAGE);
                                   }
                               }
                           }

                           frame.setContentPane(new LogIn(frame).getRootPanelLogIn());
                           frame.repaint();
                           frame.revalidate();

                           userInUse = new User();

                           break;


            case "switchToWhatDoing": frame.setContentPane(new WhatDoing(frame, userInUse).getRootPanelWhatDoing());
                                      frame.repaint();
                                      frame.revalidate();
                                      break;


            case "switchToUserProfile": frame.setContentPane(new UserProfile(frame, userInUse).getRootPanelUserProfile());
                                        frame.repaint();
                                        frame.revalidate();
                                        break;

            case "switchToStock":
                                  try {
                                      frame.setContentPane(new StockMangement(frame, userInUse).getRootPanelStock());

                                  } catch (NotConnectedException | SQLException e1) {
                                      e1.printStackTrace();
                                  }
                                  frame.repaint();
                                  frame.revalidate();
                                  break;

            case "switchToAlerts": frame.setContentPane(new Alerts(frame, userInUse, sm).getRootPanelAlerts());
                                   frame.repaint();
                                   frame.revalidate();
                                   break;

            case "switchToLog": frame.setContentPane(new LogTrail(frame, userInUse).getRootPanelLogTrail());
                                frame.repaint();
                                frame.revalidate();
                                break;

            case "switchToAdmin": frame.setContentPane(new Admin(frame, userInUse).getRootPanelAdmin());
                                  frame.repaint();
                                  frame.revalidate();
                                  break;

            case "roomExit": frame.setContentPane(new LogIn(frame).getRootPanelLogIn());
                             frame.repaint();
                             frame.revalidate();

                             //Add to log and logout
                             DataBase.InsertData.insertNewLog(userInUse, "User disconnection", "loggedOut");
                             DataBase.InsertData.insertNewLog(userInUse, "Room exit", "RoomExit");

                             try {
                                 DataBase.UpdateData.roomExit(userInUse);
                                 userInUse.setUserRoomStatus(false);
                                 userInUse.getUserConnection().close();
                             } catch (SQLException | NotConnectedException e1) {
                                 JOptionPane.showMessageDialog(null, "User has not been disconnected. " +
                                         "An unhandled error occurs.", "Stock management display " +
                                         "error", JOptionPane.ERROR_MESSAGE);
                             }
                             userInUse = new User();
                             break;

            case "roomAccess":
                               try {
                                   frame.setContentPane(new StockMangement(frame, userInUse).getRootPanelStock());
                                   DataBase.UpdateData.roomAccess(userInUse);
                                   userInUse.setUserRoomStatus(true);
                               } catch (SQLException e1) {
                                   JOptionPane.showMessageDialog(null, "Stock management has not been" +
                                                    "loaded. An unhandled error occurs.", "Stock management display " +
                                                    "error", JOptionPane.ERROR_MESSAGE);
                               } catch (NotConnectedException e1) {
                                   JOptionPane.showMessageDialog(null, "Connection to the " +
                                           "database fails. An unhandled error occurs.", "Database connection " +
                                           "error", JOptionPane.ERROR_MESSAGE);
                               }
                               frame.repaint();
                               frame.revalidate();
                               DataBase.InsertData.insertNewLog(userInUse, "Room access", "RoomAccess");
                               break;

            case "doingWhat": frame.setContentPane(new WhatDoing(frame, userInUse).getRootPanelWhatDoing());
                              frame.repaint();
                              frame.revalidate();
                              break;

            case "login": String id = login.getLogin();
                          String password = login.getPassword();

                          try {
                              ImageIcon defaultPicture = new ImageIcon(this.getClass().getResource("/Pictures/defaultProfilePicture.png"));

                              String serverAddress = TakeProperties.getServerAddress();
                              String dbName = TakeProperties.getDBName();

                              Connection connection = connectToDB(id, password, serverAddress, dbName);
                              userInUse = ExtractData.extractUserData(id, password, defaultPicture, connection);
                              frame.setContentPane(new WhatDoing(frame, userInUse).getRootPanelWhatDoing());
                              frame.repaint();
                              frame.revalidate();

                              DataBase.InsertData.insertNewLog(userInUse,"User connection", "loggedIn");


                              //add action by closing window (disconnection and eventually room exit)
                              frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

                              frame.addWindowListener(new WindowAdapter(){
                                  public void windowClosing(WindowEvent e){

                                      if (Objects.equals(userInUse, new User())){
                                          frame.dispose();
                                          System.exit(0);
                                      } else {
                                          try {
                                              //Disconnection
                                              DataBase.InsertData.insertNewLog(userInUse, "User disconnection", "loggedOut");

                                              if(userInUse.getUserRoomStatus()){
                                                  //Exit the room ?
                                                  int optionLogout = JOptionPane.showConfirmDialog(null, "Are " +
                                                                  "you quitting the room ?","Logout, room exit confirmation",
                                                          JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

                                                  if (optionLogout == 0){
                                                      DataBase.InsertData.insertNewLog(userInUse, "Room exit", "RoomExit");
                                                      DataBase.UpdateData.roomExit(userInUse);
                                                      userInUse.setUserRoomStatus(false);
                                                  }
                                              }

                                              userInUse.getUserConnection().close();
                                              frame.dispose();
                                              System.exit(0);

                                          } catch (SQLException | NotConnectedException e1) {
                                              JOptionPane.showMessageDialog(null, "You have not been disconnected from databse",
                                                      "Disconnection error", JOptionPane.ERROR_MESSAGE);
                                          }
                                      }

                                  }
                              });

                          } catch (Exception e1){

                              //If wrong login or password, refresh page with error message
                              LogIn loginFail = new LogIn(frame);
                              frame.setContentPane(loginFail.getRootPanelLogIn());
                              frame.repaint();
                              frame.revalidate();
                              loginFail.updateVisibilityConnectionError(true);
                          }
                          break;

            case "zoomImage": JOptionPane.showMessageDialog(null, null, "Trend",
                              JOptionPane.INFORMATION_MESSAGE, image);
                              break;

            case "alertOutdated" : int optionOutdated = JOptionPane.showConfirmDialog(null, "Are " +
                                             "you sure to delete outdated products in the database ? Be sure there are no " +
                    "more product in physical location","Delete outdated products", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

                                    if (optionOutdated == 0){

                                        //Delete outdated product
                                        DeleteData.deleteProduct(prod.getProductId(), userInUse);
                                        JOptionPane.showMessageDialog(null, "Outdated product has " +
                                                "successfully been deleted in database ", "Outdated product deleted", JOptionPane.INFORMATION_MESSAGE);

                                        //Archive alert
                                        try {
                                            UpdateData.archiveAlert(userInUse, alertId);
                                            frame.setContentPane(new Alerts(frame, userInUse, sm).getRootPanelAlerts());
                                            frame.repaint();
                                            frame.revalidate();
                                        } catch (SQLException e1) {
                                            JOptionPane.showMessageDialog(null, "Outdated product  " +
                                                    "alert has not been archived in database ", "Outdated product alert error", JOptionPane.ERROR_MESSAGE);
                                        }

                                        String description = "Alert " + alertId + " has been archived";
                                        DataBase.InsertData.insertNewLog(userInUse, description, "AlertArchived");


                                    }

                                    break;

            case "alertMaintenance" : LocalDate day = LocalDate.now();
                                     int optionMaintenance = JOptionPane.showConfirmDialog(null,
                                              "Are you sure you have done the maintenance in date of " + day.toString() + " ?",
                                              "Typing maintenance done",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

                                      if (optionMaintenance == 0){
                                          try {
                                              String dates = equip.getMaintenancesDates() + "\n" + day.toString();
                                              UpdateData.updateEquipmentMaintenances(userInUse, equip.getEquipmentId(), dates);
                                              UpdateData.updateLastMaintenance(userInUse, equip, day.toString());
                                              UpdateData.archiveAlert(userInUse, alertId);
                                          } catch (SQLException e1) {
                                              JOptionPane.showMessageDialog(null, "Equipment maintenance  " +
                                                      " has not been updated in database ", "Equipment maintenance error", JOptionPane.ERROR_MESSAGE);
                                          }

                                          frame.setContentPane(new Alerts(frame, userInUse, sm).getRootPanelAlerts());
                                          frame.repaint();
                                          frame.revalidate();

                                      } else {

                                          int optionMaintenanceBis = JOptionPane.showConfirmDialog(null,
                                                  "Are you sure you have done update the maintenance done ? You will just archive the alert.",
                                                  "Typing maintenance done",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                                          if(optionMaintenanceBis == 0) {
                                              try {
                                                  UpdateData.archiveAlert(userInUse, alertId);
                                              } catch (SQLException e1) {
                                                  JOptionPane.showMessageDialog(null, "Alert has " +
                                                          "not been archived in database ", "Archive alert error", JOptionPane.ERROR_MESSAGE);
                                              }

                                              String description = "Alert " + alertId + " has been archived";
                                              DataBase.InsertData.insertNewLog(userInUse, description, "AlertArchived");

                                              frame.setContentPane(new Alerts(frame, userInUse, sm).getRootPanelAlerts());
                                              frame.repaint();
                                              frame.revalidate();
                                          }
                                      }
                                      break;

            case "alertLowQuantityProduct" :
                                      int optionLowQuantity = JOptionPane.showConfirmDialog(null,
                                              "Do you have resupplied the product ?", "Product resupplied",
                                              JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

                                      if(optionLowQuantity == 0){
                                          try {
                                              UpdateData.archiveAlert(userInUse, alertId);
                                              frame.setContentPane(new Alerts(frame, userInUse, sm).getRootPanelAlerts());
                                              frame.repaint();
                                              frame.revalidate();

                                          } catch (SQLException e1) {
                                              JOptionPane.showMessageDialog(null, "Alert has not been archived in the database.",
                                                      "Alert archived error", JOptionPane.ERROR_MESSAGE);
                                          }

                                          String description = "Alert " + alertId + " has been archived";
                                          DataBase.InsertData.insertNewLog(userInUse, description, "AlertArchived");

                                          frame.repaint();
                                          frame.revalidate();
                                      }
                                      break;

            case "newLocation" : JFrame popUpLocation = new JFrame("New location");
                                 popUpLocation.setResizable(false);
                                 NewLocation.newLocation(popUpLocation, userInUse, frame, admin);
                                 break;

            case "newProductCategory" : JFrame popUpProductCategory = new JFrame("New category");
                                        popUpProductCategory.setResizable(false);
                                        NewCategory.newEquipmentCategory(popUpProductCategory, admin, userInUse, "product");
                                        break;

            case "newEquipmentCategory" : JFrame popUpEquipmentCategory = new JFrame("New category");
                                          popUpEquipmentCategory.setResizable(false);
                                          NewCategory.newEquipmentCategory(popUpEquipmentCategory, admin, userInUse, "equipment");
                                          break;

            case "newAccessoryCategory" : JFrame popUpAccessoryCategory = new JFrame("New category");
                                          popUpAccessoryCategory.setResizable(false);
                                          NewCategory.newEquipmentCategory(popUpAccessoryCategory, admin, userInUse, "accessory");
                                          break;

            case "newSupplier" : JFrame popUpSupplier = new JFrame("New supplier");
                                 popUpSupplier.setResizable(false);
                                 NewSupplier.newSupplier(popUpSupplier, userInUse, admin, frame);
                                 break;

            case "newAccessory" : JFrame popUp = new JFrame("New accessory");
                                  popUp.setResizable(false);
                                  NewAccessory.newAccessory(popUp, userInUse,admin);
                                  break;

            case "newProductName" : JFrame popUpProductName = new JFrame("New product name");
                                    popUpProductName.setResizable(false);
                                    NewProductName.newProductName(popUpProductName, admin, userInUse);
                                    break;

            case "newUser" : JFrame popUpNewUser = new JFrame("New user");
                             popUpNewUser.setResizable(false);
                             NewUser.newUser(popUpNewUser, userInUse, frame);
                             break;

            case "newMaintenance" : JFrame popUpNewMaintenance = new JFrame("New maintenance date");
                                    popUpNewMaintenance.setResizable(false);

                                    NewMaintenance.newMaintenanceDate(frame, popUpNewMaintenance, userInUse, equip);

                                    break;

            case "deleteEquipment" : int optionTrashEquipment = JOptionPane.showConfirmDialog(null,
                                    "Are you sure to delete the equipment named " + equip.getEquipmentName() + " ?",
                                    "Delete equipment confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

                                    if (optionTrashEquipment == 0){
                                        DataBase.DeleteData.deleteEquipment(equip.getEquipmentId(), userInUse);
                                        sm.displayEquipments(frame, userInUse, "");

                                        JOptionPane.showMessageDialog(null, "Equipment " + equip.getEquipmentName() +
                                                " has been removed from database.", "Equipment removed", JOptionPane.INFORMATION_MESSAGE);

                                        try {
                                            DeleteData.deleteEquipmentAlert(userInUse, equip.getEquipmentId());
                                        } catch (SQLException | NotConnectedException e1) {
                                            JOptionPane.showMessageDialog(null, "Equipment " + equip.getEquipmentName() +
                                                    " alerts have not been removed from database.", "Equipment alerts not removed", JOptionPane.ERROR_MESSAGE);
                                        }
                                    }
                                    DataBase.InsertData.insertNewLog(userInUse, "Equipment \" " + equip.getEquipmentName() + " \" deleted" , "EquipDeleted");
                                    break;

            case "deleteProduct" : int optionTrashProduct = JOptionPane.showConfirmDialog(null,
                                        "Are you sure to delete the product named " + prod.getProductName() + " ?", "Delete product confirmation",
                                        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

                                    if (optionTrashProduct == 0){
                                        DataBase.DeleteData.deleteProduct(prod.getProductId(), userInUse);

                                        sm.displayProducts(frame, userInUse, "");

                                        JOptionPane.showMessageDialog(null, "Product " + prod.getProductName() +
                                                " has been removed from database.", "Product removed", JOptionPane.INFORMATION_MESSAGE);

                                        try {
                                            DeleteData.deleteProductAlert(userInUse, prod.getProductId());
                                        } catch (SQLException | NotConnectedException e1) {
                                            JOptionPane.showMessageDialog(null, "Product " + prod.getProductName() +
                                                    " alerts has not been removed from database.", "Product alerts not removed", JOptionPane.ERROR_MESSAGE);
                                        }
                                    }
                                    DataBase.InsertData.insertNewLog(userInUse, "Product \" " + prod.getProductName() + " \" deleted" , "ProductDeleted");
                                    break;
        }

    }

}
