package scrabble.gui;

import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import scrabble.Square;

public class SquareView extends StackPane {
    private Square square;

    private Rectangle fillRect;
    private Label textLabel;

    public SquareView(Square square) {
        this.square = square;

        super.setAlignment(Pos.CENTER);

        updateBase();
    }

    private static String getModifierLabelText(Square.Modifier modifier) {
        switch (modifier) {
            case DOUBLE_WORD:
                return "Double Word";
            case DOUBLE_LETTER:
                return "Double Letter";
            case TRIPLE_WORD:
                return "Triple Word";
            case TRIPLE_LETTER:
                return "Triple Letter";
            default:
                throw new IllegalArgumentException();
        }
    }

    public void updateBase() {
        this.fillRect = new Rectangle();
        fillRect.setFill(ScrabbleColours.getModifierColour(square.getModifier()));
        fillRect.setStrokeWidth(4);
        fillRect.setStroke(Color.GRAY);
        fillRect.setWidth(50);
        fillRect.setHeight(50);

        this.textLabel = new Label();
        switch (square.getModifier()) {
            case NORMAL:
                break;
            case STAR:
                textLabel.setText("*");
                textLabel.setFont(new Font("Arial", 30));
                break;
            default:
                textLabel.setText(getModifierLabelText(square.getModifier()));
                textLabel.setWrapText(true);
        }

        StackPane.setAlignment(textLabel, Pos.CENTER);

        ObservableList<Node> children = super.getChildren();
        children.clear();

        children.addAll(fillRect, textLabel);

    }

    public void updateLetter() {

    }
}
