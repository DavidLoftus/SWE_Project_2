package scrabble.gui;

import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class CommandPanel extends VBox {

    public CommandPanel() {

        TextFlow textContainer = new TextFlow();
        textContainer.setBackground(
                new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));

        ScrollPane scrollPane = new ScrollPane(textContainer);
        scrollPane.setVvalue(1.0);

        setVgrow(textContainer, Priority.SOMETIMES);

        TextField inputField = new TextField();

        inputField.setOnAction(
                evt -> {
                    Text text = new Text(inputField.getText() + "\n");
                    text.setStyle("-fx-text-fill: #ff282f;");
                    textContainer.getChildren().add(text);
                    inputField.clear();
                    scrollPane.setVvalue(1.0);
                });

        getChildren().setAll(scrollPane, inputField);
    }
}
