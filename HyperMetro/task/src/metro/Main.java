package metro;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import java.io.FileReader;
import java.io.IOException;

import java.lang.reflect.Type;
import java.util.*;

class Line {
    private String name;
    public LinkedList<String> stationsList;

    public Line(String name) {
        this.name = name;
        this.stationsList = new LinkedList<>();
    }

    public String getName() {
        return name;
    }
}

public class Metro {

    private Map<String, Map<Integer, String>> linesMap;
    private List<Line> linesList;

    public Metro() {
        this.linesMap = new HashMap<>();
    }

    public void readStationsFile(String filePath) {
        Type type = new TypeToken<HashMap<String, HashMap<Integer, String>>>() {
        }.getType();
        Gson gson = new Gson();
        try (JsonReader reader = new JsonReader(new FileReader(filePath))) {
            linesMap = gson.fromJson(reader, type);
        } catch (com.google.gson.JsonIOException | com.google.gson.JsonSyntaxException e) {
            System.out.println("Incorrect file");
        } catch (IOException e) {
            System.out.println("Error! Such a file doesn't exist!");
        }
        mapAndSortMetroLines();
    }

    public void mapAndSortMetroLines() {
        linesList = new ArrayList<>();
        linesMap.forEach((mLine, stations) -> {
            //create line and set name
            Line line = new Line(mLine);
            //sort stations by key and replace in old map , treeMap is sorted
            linesMap.put(mLine, new TreeMap<>(stations));
            //add stations names to LinkedList
            stations.forEach((no, name) -> line.stationsList.add(name));
            linesList.add(line);
        });
    }


    public Line getLine(String lineName) {
        return linesList.stream()
                .filter(l -> l.getName().equals(lineName))
                .findAny()
                .orElse(null);
    }

    public void addStationToLine(String lineName, String station) {
        getLine(lineName).stationsList.addLast(station);
    }

    public void addStationToLineHead(String lineName, String station) {
        getLine(lineName).stationsList.addFirst(station);
    }

    public void removeStationFromLine(String lineName, String station) {
        getLine(lineName).stationsList.remove(station);
    }

    public void printLine(String lineName) {
        Line line = getLine(lineName);
        for (int i = 0; i < line.stationsList.size(); i++) {
            String previous = i == 0 ? "depot" : line.stationsList.get(i - 1);
            String next = i == line.stationsList.size() - 1 ? "depot" : line.stationsList.get(i + 1);
            System.out.println(previous + " - " + line.stationsList.get(i) + " - " + next);
        }
    }
}
