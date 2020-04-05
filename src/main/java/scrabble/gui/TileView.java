package scrabble.gui;

import javafx.beans.NamedArg;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import scrabble.Tile;

public class TileView extends StackPane {

    private Tile tile;

    public TileView(@NamedArg("tile") Tile tile) {
        this.tile = tile;

        setPrefSize(40.0, 40.0);
        setMaxSize(USE_PREF_SIZE, USE_PREF_SIZE);

        setBackground(
                new Background(
                        new BackgroundFill(Color.GOLDENROD, new CornerRadii(7.0), Insets.EMPTY)));
        Label charLabel = new Label(Character.toString(tile.getLetter()));

        Label scoreLabel = new Label(Integer.toString(tile.getValue()));

        getChildren().setAll(charLabel, scoreLabel);

        setAlignment(charLabel, Pos.CENTER);
        setAlignment(scoreLabel, Pos.BOTTOM_RIGHT);
    }

    public Tile getTile() {
        return tile;
    }

    public void setLetter(char letter) {
        Label label = (Label) getChildren().get(0);
        label.setText(Character.toString(Character.toUpperCase(letter)));
    }
}
