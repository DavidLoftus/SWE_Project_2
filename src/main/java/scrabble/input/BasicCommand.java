package scrabble.input;

import java.util.function.Function;

public class BasicCommand implements InputCommand {
    public final String command;

    public BasicCommand(String command) {
        this.command = command;
    }

    public static Function<String, InputCommand> makeParser(String command) {
        return s -> {
            if (s.equalsIgnoreCase(command)) {
                return new BasicCommand(command);
            }
            return null;
        };
    }
}
