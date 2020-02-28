package scrabble.gui;

import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import scrabble.Board;
import scrabble.BoardPos;

public class BoardView extends GridPane {

    private Board board = null;

    public BoardView() {
    }

    public BoardView(Board board) {
        this.setBoard(board);
    }

    public void setBoard(Board board) {
        this.board = board;
        getChildren().clear();

        for (int i = 0; i < 15; ++i) {
            for (int j = 0; j < 15; ++j) {
                BoardPos pos = new BoardPos(i, j);
                SquareView node = new SquareView(board.getSquareAt(pos));
                super.add(node, i, j);
            }
        }
    }

    public void update() {
        for (Node node : getChildren()) {
            SquareView squareView = (SquareView)node;
            squareView.updateBase();
        }
    }

}
