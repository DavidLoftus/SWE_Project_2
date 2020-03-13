package scrabble.gui;

import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class CommandPanel extends VBox {

    TextFlow textContainer;
    ScrollPane scrollPane;
    TextField inputField;

    public CommandPanel() {

        textContainer = new TextFlow();
        textContainer.setBackground(
                new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));

        scrollPane = new ScrollPane(textContainer);
        scrollPane.setVvalue(1.0);

        setVgrow(textContainer, Priority.SOMETIMES);

        TextField inputField = new TextField();

        inputField.setOnAction(
                evt -> {
                    print(inputField.getText());
                });

        getChildren().setAll(scrollPane, inputField);
    }

    public void print(String str) {
        Text text = new Text(str + "\n");
        text.setStyle("-fx-text-fill: #ff282f;");
        textContainer.getChildren().add(text);
        inputField.clear();
        scrollPane.setVvalue(1.0);
    }
}
