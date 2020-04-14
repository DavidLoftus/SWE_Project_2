package scrabble.bot;

import java.util.Arrays;
import java.util.List;
import scrabble.UserInterfaceAPI;

public class LogCache {

    private UserInterfaceAPI userInterfaceAPI;
    private int skipLength = 0;

    private int commitLength = 0;

    public LogCache(UserInterfaceAPI userInterfaceAPI) {
        this.userInterfaceAPI = userInterfaceAPI;
    }

    public void commit() {
        skipLength = commitLength;
    }

    public List<String> getLatestLogs() {
        return getLatestLogs(false);
    }

    public List<String> getLatestLogs(boolean shouldCommit) {
        String allInfo = userInterfaceAPI.getAllInfo();

        String data = allInfo.substring(skipLength);
        String[] lines = data.split("\n");

        commitLength = allInfo.length();
        if (shouldCommit) {
            commit();
        }

        return Arrays.asList(lines);
    }
}
