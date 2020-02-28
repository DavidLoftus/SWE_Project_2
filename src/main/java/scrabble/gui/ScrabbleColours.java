package scrabble.gui;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import scrabble.Square;

public class ScrabbleColours {

    public static Paint getModifierColour(Square.Modifier modifier) {
        switch (modifier) {
            case DOUBLE_WORD:
                return Color.ORANGE;
            case DOUBLE_LETTER:
                return Color.LIGHTGRAY;
            case TRIPLE_WORD:
                return Color.RED;
            case TRIPLE_LETTER:
                return Color.DARKBLUE;
            case NORMAL:
                return Color.TEAL;
            case STAR:
                return Color.PEACHPUFF;
            default:
                throw new IllegalArgumentException();
        }
    }

}
