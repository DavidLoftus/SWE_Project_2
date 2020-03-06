package scrabble.gui;

import javafx.geometry.Insets;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import scrabble.Square;

public class SquareView extends StackPane {
    private Square square;

    SquareView(Square square) {
        this.square = square;

        setBackground(
                new Background(new BackgroundFill(Color.YELLOW, CornerRadii.EMPTY, Insets.EMPTY)));
    }

    public Color getColourForModifier(Square.Modifier modifier) {
        switch (modifier) {
            case DOUBLE_WORD:
                return Color.YELLOW;
            case DOUBLE_LETTER:
                return Color.web("#32ced1");
            case TRIPLE_WORD:
                return Color.web("#1a6f82");
            case TRIPLE_LETTER:
                return Color.web("#cf503a");
            case NORMAL:
                return Color.GREEN;
            case STAR:
                return Color.web("#edd834");
            default:
                return null; // shouldn't be needed
        }
    }

    public void setSquare(Square square) {
        this.square = square;
    }
}
