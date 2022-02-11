package metro;

import java.util.ArrayList;
import java.util.List;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParseUtil {

    public static List<String> parseInput(String input) {
        List<String> commands = new ArrayList<>();
        Pattern pattern = Pattern.compile("\"(.*?)\"|(\\S+)");
        Matcher matcher = pattern.matcher(input);

        while (matcher.find()) {
            commands.add(matcher.group().replace("\"", ""));
        }
        return commands;
    }
}
