package scrabble.input;

public class NameCommand implements InputCommand {
    public String name;

    public NameCommand(String name) {
        this.name = name;
    }

    public static NameCommand valueOf(String str) {
        String[] strings = str.split(" ", 1);
        if (strings.length < 1 || strings[0].equalsIgnoreCase("NAME")) {
            return null;
        }
        return new NameCommand(strings.length < 2 ? "" : strings[1]);
    }
}
