package scrabble.input;

import java.util.ArrayList;
import java.util.List;

public class InputEventHandler implements InputListener {

    private List<InputListener> listeners = new ArrayList<>();

    public void addListener(InputListener listener) {
        listeners.add(listener);
    }

    public void removeListener(InputListener listener) {
        listeners.remove(listener);
    }

    @Override
    public void accept(String input) {
        listeners.forEach(listener -> listener.accept(input));
    }
}
