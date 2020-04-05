package scrabble.gui;

import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import scrabble.Square;
import scrabble.Tile;

public class SquareView extends StackPane {

    private Square square;

    private Label modifierLabel;

    public SquareView(Square square) {
        this.square = square;

        Square.Modifier modifier = square.getModifier();

        setPrefSize(50, 50);

        setBackground(
                new Background(
                        new BackgroundFill(
                                getColourForModifier(modifier),
                                new CornerRadii(1.0),
                                new Insets(1.0))));

        modifierLabel = getLabelForModifier(modifier);

        setAlignment(Pos.CENTER);

        updateTile();
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

    public Label getLabelForModifier(Square.Modifier modifier) {
        Label label = new Label();
        label.setWrapText(true);
        label.setTextAlignment(TextAlignment.CENTER);
        label.setFont(new Font(10.0));
        switch (modifier) {
            case DOUBLE_WORD:
                label.setText("Double Word");
                break;
            case DOUBLE_LETTER:
                label.setText("Double Letter");
                break;
            case TRIPLE_WORD:
                label.setText("Triple Word");
                break;
            case TRIPLE_LETTER:
                label.setText("Triple Letter");
                break;
            case NORMAL:
                break;
            case STAR:
                label.setText("*");
                break;
        }
        return label;
    }

    public void updateTile() {
        ObservableList<Node> children = getChildren();
        if (square.isEmpty()) {
            children.setAll(modifierLabel);
            return;
        }
        if (!children.isEmpty()) {
            Node child = children.get(0);
            if (child instanceof TileView) {
                TileView tileView = (TileView) child;
                if (tileView.getTile() == square.getTile()) {
                    return;
                }
            }
        }
        TileView tileView = new TileView(square.getTile());

        if (square.getTile() == Tile.BLANK) {
            tileView.setLetter(square.getLetter());
        }

        getChildren().setAll(modifierLabel, tileView);
    }
}
