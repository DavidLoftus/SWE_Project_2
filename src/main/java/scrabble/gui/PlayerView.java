package scrabble.gui;

import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import scrabble.Player;

public class PlayerView extends VBox {

    private Player player = null;

    private Label playerNameLabel = new Label("Player 1, score: 0");
    private FrameView frameView = new FrameView();

    public PlayerView() {
        getChildren().setAll(playerNameLabel, frameView);
    }

    public void setPlayer(Player player) {
        this.player = player;
        this.frameView.setFrame(player.getFrame());
        update();
    }

    public void update() {
        playerNameLabel.setText(
                String.format("%s, score: %d", player.getName(), player.getScore()));
        frameView.updateFrame();
    }
}
