package scrabble.input;

import java.util.ArrayList;
import java.util.List;

public class InputEventHandler implements InputListener {

    private List<InputListener> listeners = new ArrayList<>();
    private List<InputListener> oneTimeListeners = new ArrayList<>();

    public void addListener(InputListener listener) {
        listeners.add(listener);
    }

    public void addOneTimeListener(InputListener listener) {
        oneTimeListeners.add(listener);
    }

    public void removeListener(InputListener listener) {
        listeners.remove(listener);
        oneTimeListeners.remove(listener);
    }

    @Override
    public void accept(String input) {
        listeners.forEach(listener -> listener.accept(input));

        int len = oneTimeListeners.size();
        for (int i = 0; i < len; ++i) {
            oneTimeListeners.get(i).accept(input);
        }
        oneTimeListeners.subList(0, len).clear();
    }
}
