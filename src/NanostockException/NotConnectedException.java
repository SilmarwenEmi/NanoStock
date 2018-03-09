package NanostockException;

/**
 * @author Emilie Laurent
 * Exception thrown if we cannot be connected
 */
public class NotConnectedException extends Exception {
    /**
     * Exception without text in the console
     */
    public NotConnectedException(){super();}

    /**
     * Exception with text in the console
     * @param s: Text to print in the console
     */
    public NotConnectedException(String s) {super(s);}

}
