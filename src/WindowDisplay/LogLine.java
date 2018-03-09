package WindowDisplay;

import javax.swing.*;
import java.awt.*;

/**
 * @author Emilie Laurent
 * A log line is one line that will be displayed on the log trail page of the software
 */
public class LogLine extends JPanel{
    private JPanel rootLogLine;

    private JLabel descriptionLog;
    private JLabel logTag;
    private JLabel emissionDate;
    private JLabel hour;
    private JLabel user;

    /**
     * Construct one line of the log trail
     * @param descr: The name of the item or the description of the log
     * @param tag: What kind of log it is (displayed with an image)
     * @param date: Date of performed log
     * @param hour: Hour of performed log
     * @param login: Who did that task logged
     */
    public LogLine(String descr, String tag, String date, String hour, String login) {

        ImageIcon tagImage = new ImageIcon(this.getClass().getResource("/Pictures/Log_" + tag + ".png"));

        this.descriptionLog.setText(descr);
        this.emissionDate.setText(date);
        this.hour.setText(hour);
        this.logTag.setIcon(tagImage);
        this.user.setText(login);

        setBackground(Color.WHITE);
        setLayout(new GridLayout(0, 1));
        add(rootLogLine);
    }

    //Getter methods
    public JLabel getDescriptionLog() {
        return descriptionLog;
    }

    public JLabel getLogTag() {
        return logTag;
    }

    public JLabel getEmissionDate() {
        return emissionDate;
    }

    public JLabel getHour() {
        return hour;
    }

    public JLabel getUser() {
        return user;
    }
}
