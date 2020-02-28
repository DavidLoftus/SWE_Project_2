package scrabble.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import scrabble.BoardPos;
import scrabble.Player;
import scrabble.Square;

public class ScrabbleController {

    @FXML
    public Label currentPlayerLabel;

    @FXML
    public BoardView boardGrid;

    @FXML
    public Button resetButton;

    public void setCurrentPlayer(Player player) {
        currentPlayerLabel.setText("Current Player: " + player.getName());
    }

}
