package gui.controls;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.Region;

public class CustomAlert extends Alert {

    public CustomAlert(AlertType type, String content, String title, ButtonType... buttons) {
        super(type, content, buttons);
        this.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        this.setHeaderText(title);
        this.setTitle(type.toString());
    }

    public CustomAlert(AlertType type, String content) {
        this(type, content, type.toString());
    }

}
