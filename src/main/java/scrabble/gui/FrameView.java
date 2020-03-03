package scrabble.gui;

import java.util.Collections;
import javafx.geometry.Insets;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import scrabble.Frame;

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

    private void setFrame(Frame frame) {
        this.frame = frame;
    }
}
