package scrabble.gui;

import java.io.OutputStream;
import java.io.PrintStream;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
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

                    inputEventHandler.accept(inputString);
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

    public PrintStream getOutputStream() {
        OutputStream os =
                new OutputStream() {

                    private StringBuffer buffer = new StringBuffer();

                    @Override
                    public void write(int i) {
                        buffer.appendCodePoint(i);
                    }

                    @Override
                    public void write(byte[] b) {
                        buffer.append(new String(b));
                    }

                    @Override
                    public void write(byte[] b, int off, int len) {
                        buffer.append(new String(b, off, len));
                    }

                    @Override
                    public void flush() {
                        print(buffer.toString());
                        buffer.delete(0, buffer.length());
                    }
                };
        return new PrintStream(os, true);
    }
}
