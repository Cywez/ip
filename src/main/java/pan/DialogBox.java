package pan;

import java.io.IOException;
import java.util.Collections;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

/**
 * One chat bubble, with its layout loaded from {@code DialogBox.fxml}: a
 * wrapped-text {@link Label} beside an avatar {@link ImageView}.
 *
 * <p>The FXML uses the {@code <fx:root>} construct, so each {@code DialogBox}
 * instance is both the root node and the controller of its own copy of the
 * layout. A user bubble keeps the avatar on the right; PanPan's bubble is
 * {@linkplain #flip() flipped} so the avatar sits on the left.
 */
public class DialogBox extends HBox {

    @FXML
    private Label dialog;
    @FXML
    private ImageView displayPicture;

    private DialogBox(String text, Image avatar) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/DialogBox.fxml"));
            fxmlLoader.setController(this);
            fxmlLoader.setRoot(this);
            fxmlLoader.load();
        } catch (IOException e) {
            // A missing or malformed FXML file is a packaging bug, not a user
            // error, so fail loudly instead of showing a broken bubble.
            throw new RuntimeException(e);
        }

        dialog.setText(text);
        displayPicture.setImage(avatar);
    }

    /**
     * Reverses the children (avatar first, then text) and left-aligns the row,
     * turning a user bubble into a "them" bubble.
     */
    private void flip() {
        setAlignment(Pos.TOP_LEFT);
        ObservableList<Node> children = FXCollections.observableArrayList(getChildren());
        Collections.reverse(children);
        getChildren().setAll(children);
    }

    /** Returns a bubble for something the user said, with the avatar on the right. */
    public static DialogBox getUserDialog(String text, Image avatar) {
        return new DialogBox(text, avatar);
    }

    /** Returns a bubble for something PanPan said, with the avatar on the left. */
    public static DialogBox getPanDialog(String text, Image avatar) {
        DialogBox box = new DialogBox(text, avatar);
        box.flip();
        return box;
    }
}