package scrabble.gui;

import java.util.Collections;
import javafx.geometry.HPos;
import javafx.scene.Node;
import javafx.scene.control.Label;
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

        getRowConstraints().setAll(Collections.nCopies(16, rowConstraints));
        getColumnConstraints().setAll(Collections.nCopies(16, columnConstraints));

        for (int i = 0; i < 15; ++i) {
            Label columnLabel = new Label(Integer.toString(i));
            add(columnLabel, 1 + i, 0);
            setHalignment(columnLabel, HPos.CENTER);

            Label rowLabel = new Label(String.valueOf((char) ('A' + i)));
            add(rowLabel, 0, 1 + i);
            setHalignment(rowLabel, HPos.CENTER);
        }
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
                add(board.getSquareViewAt(pos), j + 1, i + 1);
            }
        }
    }

    public void updateGridTiles() {
        assert board != null;
        for (Node child : getChildrenUnmodifiable()) {
            if (child instanceof SquareView) {
                SquareView squareView = (SquareView) child;
                squareView.updateTile();
            }
        }
    }
}
