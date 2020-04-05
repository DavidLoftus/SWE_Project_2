package scrabble.input;

public class NameCommand implements InputCommand {
    public String name;

    public NameCommand(String name) {
        this.name = name;
    }

    /**
     * The command to change a player's name
     *
     * @param str the player's new name
     * @return a new NameCommand
     */
    public static NameCommand valueOf(String str) {
        String[] strings = str.split(" ", 2);
        if (strings.length < 1 || !strings[0].equalsIgnoreCase("NAME")) {
            return null;
        }
        return new NameCommand(strings.length < 2 ? "" : strings[1]);
    }
}
