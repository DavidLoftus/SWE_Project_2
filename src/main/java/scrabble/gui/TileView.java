package scrabble.gui;

import javafx.beans.NamedArg;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import scrabble.Tile;

public class TileView extends Label {

    private Tile tile;

    public TileView(@NamedArg("tile") Tile tile) {
        this.tile = tile;

        setBackground(
                new Background(
                        new BackgroundFill(Color.GOLDENROD, new CornerRadii(1.0), Insets.EMPTY)));
        setText(Character.toString(tile.getLetter()));

        setAlignment(Pos.CENTER);
    }
}
