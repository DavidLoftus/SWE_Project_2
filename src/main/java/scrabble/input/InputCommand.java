package scrabble.input;

import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Stream;

public interface InputCommand {

    boolean usesTurn();

    static InputCommand valueOf(String str) {
        Stream<Function<String, InputCommand>> parsers =
                Stream.of(ExchangeCommand::valueOf, HelpCommand::valueOf, PlaceCommand::valueOf);

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
