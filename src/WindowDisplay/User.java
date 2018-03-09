package WindowDisplay;

import javax.swing.*;
import java.io.FileInputStream;
import java.sql.Connection;

/**
 * @author Emilie Laurent
 * This class represent a common user (the person who will use the software)
 */
public class User {
    private String login;
    private String password;
    private String name;
    private String firstName;
    private String mail;
    private String privileges;
    private boolean inRoom;

    private Connection connection;

    private ImageIcon picture;

    /**
     * Construct the User default object
     */
    public User(){
        this.login = "";
        this.password = "";
        this.name = "";
        this.firstName = "";
        this.mail = "";
        this.connection = null;
        this.privileges = "";
        this.picture = null;
        this.inRoom = false;
    }

    /**
     * Construct the User object with given information
     * @param login: Login of the user to connect to database
     * @param password: Password of the user to connect to database
     * @param name: Name of the user
     * @param firstName: First name of the user
     * @param mail: Email of the user
     * @param connection: Information coming from the connection to the database
     * @param privileges: Degree of permission of the user
     * @param picture: Picture chosen by the user
     * @param in: True if user is in room, otherwise false
     */
    public User(String login, String password, String name, String firstName, String mail, Connection connection, String privileges, ImageIcon picture, boolean in){
        this.login = login;
        this.password = password;
        this.name = name;
        this.firstName = firstName;
        this.mail = mail;
        this.connection = connection;
        this.privileges = privileges;
        this.picture = picture;
        this.inRoom = in;

    }

    //Getter and setter methods
    public String getUserLogin() {
        return this.login;
    }

    public String getUserPassword() { return this.password; }

    public String getUserName() {
        return this.name;
    }

    public String getUserFirstName() {
        return this.firstName;
    }

    public String getUserMail() { return this.mail; }

    public Connection getUserConnection(){ return this.connection; }

    public String getUserPrivileges(){ return this.privileges; }

    public ImageIcon getUserPicture(){ return this.picture; }

    public boolean getUserRoomStatus() { return this.inRoom; }

    public void setUserLogin(String login) {
        this.login = login;
    }

    public void setUserFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setUserMail(String mail) {
        this.mail = mail;
    }

    public void setUserName(String name) {
        this.name = name;
    }

    public void setUserPassword(String password) {
        this.password = password;
    }

    public void setUserConnection(Connection connection) { this.connection = connection; }

    public void setUserPrivileges(String privileges){ this.privileges = privileges; }

    public void setUserPicture(ImageIcon picture) { this.picture = picture; }

    public void setUserRoomStatus(boolean status) {this.inRoom = status;}
}
