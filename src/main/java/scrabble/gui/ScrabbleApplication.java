package scrabble.gui;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import scrabble.*;
import scrabble.exceptions.BadWordPlacementException;

public class ScrabbleApplication extends Application {

    private ScrabbleController scrabbleController;

    @Override
    public void start(Stage primaryStage) throws IOException, BadWordPlacementException {
        primaryStage.setTitle("Hello World!");

        FXMLLoader fxmlLoader =
                new FXMLLoader(ScrabbleApplication.class.getResource("scrabble.fxml"));

        Scene scene = new Scene(fxmlLoader.load());
        scrabbleController = fxmlLoader.getController();

        primaryStage.setScene(scene);
        primaryStage.show();

        Board board = new Board();

        Player player = new Player("Bob");
        player.getFrame().setAll(Tile.A, Tile.P, Tile.BLANK, Tile.L, Tile.E);

        board.applyWordPlacement(
                player,
                new WordPlacement(new BoardPos(7, 7), WordPlacement.Direction.HORIZONTAL, "APPLE"));

        player.getFrame().setAll(Tile.A, Tile.P, Tile.BLANK, Tile.L, Tile.E, Tile.F);

        scrabbleController.boardGrid.setBoard(board);
        scrabbleController.frame.setFrame(player.getFrame());

        TileView node = (TileView) scrabbleController.frame.getChildren().get(0);

        System.out.printf("%s\n", node);
    }
}
