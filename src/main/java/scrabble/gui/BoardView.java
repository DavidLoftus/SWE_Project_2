package scrabble.gui;

import java.util.Collections;
import javafx.scene.Node;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import scrabble.Board;
import scrabble.BoardPos;

public class BoardView extends GridPane {

    private Board board = null;

    public BoardView() {

        widthProperty().addListener((__, ___, w) -> setHeight(w.doubleValue()));
        heightProperty().addListener((__, ___, h) -> setWidth(h.doubleValue()));

        RowConstraints rowConstraints = new RowConstraints();
        ColumnConstraints columnConstraints = new ColumnConstraints();

        getRowConstraints().setAll(Collections.nCopies(15, rowConstraints));
        getColumnConstraints().setAll(Collections.nCopies(15, columnConstraints));
    }

    public void setBoard(Board board) {
        this.board = board;
        updateGridBase();
        updateGridTiles();

        System.out.printf("%f, %f\n", getWidth(), getHeight());
    }

    private void updateGridBase() {
        assert board != null;

        for (int i = 0; i < 15; ++i) {
            for (int j = 0; j < 15; ++j) {
                BoardPos pos = new BoardPos(i, j);
                add(board.getSquareViewAt(pos), i, j);
            }
        }
    }

    public void updateGridTiles() {
        assert board != null;
        for (Node child : getChildrenUnmodifiable()) {
            SquareView squareView = (SquareView) child;
            squareView.updateTile();
        }
    }
}
