package pan;

/**
 * Signals an error caused by invalid user input, carrying a friendly,
 * PanPan-voiced message that is shown directly to the user.
 */
public class PanException extends Exception {
    public PanException(String message) {
        super(message);
    }
}
