package scrabble;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class Bots {

    private static final String[] ALL_BOT_NAMES = {
        "scrabble.Bot0", "scrabble.Bot1", "BetrayedBot", "TheFloorIsMadeOfJava"
    };
    private BotAPI[] bots = new BotAPI[Scrabble.NUM_PLAYERS];

    Bots(Scrabble scrabble, UserInterface ui, String[] args) {
        String[] botNames = new String[Scrabble.NUM_PLAYERS];
        if (args.length != Scrabble.NUM_PLAYERS) {
            botNames[0] = "BetrayedBot";
            botNames[1] = "BetrayedBot";
        } else {
            for (int i = 0; i < Scrabble.NUM_PLAYERS; i++) {
                boolean found = false;
                for (int j = 0; (j < ALL_BOT_NAMES.length) && !found; j++) {
                    if (args[i].equals(ALL_BOT_NAMES[j])) {
                        found = true;
                        botNames[i] = args[i];
                    }
                }
                if (!found) {
                    System.out.println("Error: Bot name not found");
                    System.exit(-1);
                }
            }
        }
        for (int i = 0; i < Scrabble.NUM_PLAYERS; i++) {
            try {
                Class<?> botClass = Class.forName(botNames[i]);
                Constructor<?> botCons =
                        botClass.getDeclaredConstructor(
                                PlayerAPI.class,
                                OpponentAPI.class,
                                BoardAPI.class,
                                UserInterfaceAPI.class,
                                DictionaryAPI.class);
                long memoryUsageBefore =
                        Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
                if (i == 0) {
                    bots[i] =
                            (BotAPI)
                                    botCons.newInstance(
                                            scrabble.getCurrentPlayer(),
                                            scrabble.getOpposingPlayer(),
                                            scrabble.getBoard(),
                                            ui,
                                            scrabble.getDictionary());
                } else {
                    bots[i] =
                            (BotAPI)
                                    botCons.newInstance(
                                            scrabble.getOpposingPlayer(),
                                            scrabble.getCurrentPlayer(),
                                            scrabble.getBoard(),
                                            ui,
                                            scrabble.getDictionary());
                }

                long memoryUsed =
                        Runtime.getRuntime().totalMemory()
                                - Runtime.getRuntime().freeMemory()
                                - memoryUsageBefore;

                long memoryUsedAfterGC =
                        Runtime.getRuntime().totalMemory()
                                - Runtime.getRuntime().freeMemory()
                                - memoryUsageBefore;

                System.out.printf(
                        "%s constructor used %.3fmb, %.3fmb after GC\n",
                        botNames[i], memoryUsed / 1e6, memoryUsedAfterGC / 1e6);

            } catch (IllegalAccessException ex) {
                System.out.println("Error: Bot instantiation fail (IAE)");
                System.exit(1);
            } catch (InstantiationException ex) {
                System.out.println("Error: Bot instantiation fail (IE)");
                System.exit(1);
            } catch (ClassNotFoundException ex) {
                System.out.println("Error: Bot instantiation fail (CNFE)");
                Thread.currentThread().interrupt();
            } catch (InvocationTargetException ex) {
                System.out.println("Error: Bot instantiation fail (ITE)");
                Thread.currentThread().interrupt();
            } catch (NoSuchMethodException ex) {
                System.out.println("Error: Bot instantiation fail (NSME)");
                Thread.currentThread().interrupt();
            }
        }
    }

    public BotAPI getBot(int index) {
        return bots[index];
    }
}
