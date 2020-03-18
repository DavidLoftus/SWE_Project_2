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

    InputEventHandler inputEventHandler = new InputEventHandler();

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
                    String inputString = inputField.getText();
                    inputField.clear();

                    println(inputString);

                    InputCommand command = InputCommand.valueOf(inputString);

                    if (command == null) {
                        println("Bad command.");
                    } else {
                        inputEventHandler.accept(command);
                    }
                });

        getChildren().setAll(scrollPane, inputField);
    }

    public void println(String str) {
        print(str + "\n");
    }

    public void print(String str) {
        Text text = new Text(str);
        text.setStyle("-fx-text-fill: #ff282f;");
        textContainer.getChildren().add(text);
        inputField.clear();
        scrollPane.setVvalue(1.0);
    }

    public void addListener(InputListener listener) {
        inputEventHandler.addListener(listener);
    }
}
