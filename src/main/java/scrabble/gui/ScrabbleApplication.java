package scrabble.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;

public class ScrabbleApplication extends Application {

    private ScrabbleController scrabbleController;

    @Override
    public void start(Stage primaryStage) throws IOException {
        primaryStage.setTitle("Hello World!");

        FXMLLoader fxmlLoader = new FXMLLoader(ScrabbleApplication.class.getResource("scrabble.fxml"));

        Scene scene = new Scene(fxmlLoader.load());
        scrabbleController = fxmlLoader.getController();

        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
