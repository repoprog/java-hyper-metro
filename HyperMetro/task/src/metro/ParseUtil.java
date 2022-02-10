package metro;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class ParseUtil {

    public static List<String> parseInput(String input) {
        List<String> commands = new ArrayList<>();
        Pattern pattern = Pattern.compile("\"(.*?)\"|(\\S+)");
        Matcher matcher = pattern.matcher(input);

        while (matcher.find()) {
            commands.add(matcher.group().replace("\"", ""));
        } return commands;
    }

    public static void parseFile(String filePath, Metro metro) {
        String jsonContent = metro.readStationsFile(filePath);
        Gson gson = new Gson();
        Type metroLinesType = new TypeToken<Map<String, Map<Integer, String>>>() {}.getType();
        metro.linesMap = gson.fromJson(jsonContent, metroLinesType);
//        metro.sortStations();
        metro.mapDeserializedMetroLines();
    }
}
