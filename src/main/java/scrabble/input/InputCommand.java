package scrabble.input;

import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Stream;

public interface InputCommand {
    static InputCommand valueOf(String str) {
        Stream<Function<String, InputCommand>> parsers =
                Stream.of(
                        ExchangeCommand::valueOf,
                        PlaceCommand::valueOf,
                        BasicCommand.makeParser("PASS"),
                        BasicCommand.makeParser("HELP"),
                        BasicCommand.makeParser("QUIT"));

        return parsers.map(
                        parser -> {
                            try {
                                return parser.apply(str);
                            } catch (IllegalArgumentException e) {
                                return null;
                            }
                        })
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);
    }
}
