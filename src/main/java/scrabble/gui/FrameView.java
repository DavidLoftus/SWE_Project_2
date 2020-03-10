package scrabble.gui;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import scrabble.Frame;
import scrabble.Tile;

public class FrameView extends GridPane {
    private Frame frame;

    public FrameView() {
        super();
        setHgap(5.0);
        setPrefSize(320.0, 55.0);
        setPadding(new Insets(5.0, 5.0, 10.0, 5.0));

        getRowConstraints().setAll(new RowConstraints());

        ColumnConstraints columnConstraints = new ColumnConstraints();
        columnConstraints.setPrefWidth(40.0);
        getColumnConstraints().setAll(Collections.nCopies(7, columnConstraints));

        setBackground(
                new Background(
                        new BackgroundFill(Color.DARKGREEN, CornerRadii.EMPTY, Insets.EMPTY)));
    }

    public FrameView(Frame frame) {
        this();
        setFrame(frame);
    }

    public void setFrame(Frame frame) {
        this.frame = frame;
        updateFrame();
    }

    public void updateFrame() {
        ObservableList<Node> children = getChildren();
        List<Tile> tiles = frame.getTiles();

        children.setAll(tiles.stream().map(TileView::new).collect(Collectors.toList()));
        for (int i = 0; i < children.size(); ++i) {
            setColumnIndex(children.get(i), i);
        }
    }
}
