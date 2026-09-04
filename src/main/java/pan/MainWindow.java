package pan;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

/**
 * Controller for {@code MainWindow.fxml}.
 *
 * <p>The layout (scroll area, text field, "Send" button) is described in the
 * FXML file; this class only holds references to those controls - injected via
 * {@code @FXML} - and reacts to the user pressing Enter or clicking "Send".
 */
public class MainWindow extends AnchorPane {

    /** How long PanPan's goodbye stays on screen before the window closes. */
    private static final Duration EXIT_DELAY = Duration.seconds(1.5);

    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox dialogContainer;
    @FXML
    private TextField userInput;
    @FXML
    private Button sendButton;

    private Pan pan;

    private final Image userImage = new Image(getClass().getResourceAsStream("/images/DaHuman.png"));
    private final Image panImage = new Image(getClass().getResourceAsStream("/images/DaPan.png"));

    /**
     * Runs once, right after the {@code @FXML} fields are injected. Keeps the
     * scroll area pinned to the newest message as the dialog container grows.
     */
    @FXML
    public void initialize() {
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
    }

    /**
     * Injects the chatbot this window talks to and shows its opening greeting.
     *
     * @param pan the {@link Pan} back end.
     */
    public void setPan(Pan pan) {
        this.pan = pan;
        dialogContainer.getChildren().add(DialogBox.getPanDialog(pan.getWelcome(), panImage));
    }

    /**
     * Shows the user's line and PanPan's reply as two new dialog boxes, clears
     * the text field, and closes the window shortly after a {@code bye}.
     */
    @FXML
    private void handleUserInput() {
        String input = userInput.getText();
        if (input.isBlank()) {
            return;
        }

        String response = pan.getResponse(input);
        dialogContainer.getChildren().addAll(
                DialogBox.getUserDialog(input, userImage),
                DialogBox.getPanDialog(response, panImage));
        userInput.clear();

        if (pan.isExit()) {
            userInput.setDisable(true);
            sendButton.setDisable(true);
            PauseTransition pause = new PauseTransition(EXIT_DELAY);
            pause.setOnFinished((event) -> Platform.exit());
            pause.play();
        }
    }
}