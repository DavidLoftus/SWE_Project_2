package scrabble.gui;

import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import scrabble.input.InputCommand;
import scrabble.input.InputEventHandler;
import scrabble.input.InputListener;

public class CommandPanel extends VBox {

    TextFlow textContainer;
    ScrollPane scrollPane;
    TextField inputField;

    InputEventHandler inputEventHandler = new InputEventHandler();

    public CommandPanel() {

        textContainer = new TextFlow();

        scrollPane = new ScrollPane(textContainer);
        scrollPane.setVvalue(1.0);

        setVgrow(textContainer, Priority.SOMETIMES);

        inputField = new TextField();

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
        textContainer.getChildren().add(text);
        inputField.clear();
        scrollPane.setVvalue(1.0);
    }

    public void addListener(InputListener listener) {
        inputEventHandler.addListener(listener);
    }
}
