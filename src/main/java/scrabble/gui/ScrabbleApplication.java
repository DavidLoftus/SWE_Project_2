package scrabble.gui;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ScrabbleApplication extends Application {

    private ScrabbleController scrabbleController;

    @Override
    public void start(Stage primaryStage) throws IOException {
        primaryStage.setTitle("Hello World!");

        FXMLLoader fxmlLoader =
                new FXMLLoader(ScrabbleApplication.class.getResource("scrabble.fxml"));

        Scene scene = new Scene(fxmlLoader.load());
        scrabbleController = fxmlLoader.getController();

        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
