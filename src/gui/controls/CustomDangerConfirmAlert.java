package gui.controls;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.Region;

import java.util.Optional;

public class CustomDangerConfirmAlert extends Alert {

    public CustomDangerConfirmAlert(AlertType type, String content, String title) {
        super(type, content, new ButtonType("Oui", ButtonBar.ButtonData.YES),
                new ButtonType("Non", ButtonBar.ButtonData.NO));
        this.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        this.setHeaderText(title);
        this.setTitle(type.toString());
    }

    public CustomDangerConfirmAlert(AlertType type, String content) {
        this(type, content, type.toString());
    }

    public boolean showWithResult() {
        Optional<ButtonType> result = this.showAndWait();
        return result.isPresent() && result.get().getButtonData() == ButtonBar.ButtonData.YES;
    }
}
