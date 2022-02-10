package metro;

import java.io.File;
import java.io.IOException;
import java.util.*;

class Line {
    private String name;
    LinkedList<String> stationsList;

    public Line(String name) {
        this.name = name;
        this.stationsList = new LinkedList<>();
    }

    public void setStationsList(LinkedList<String> stationsList) {
        this.stationsList = stationsList;
    }

    public String getName() {
        return name;
    }
}

public class Metro {

    public Map<String, Map<Integer, String>> linesMap;
    private List<Line> linesList;

    public Metro() {
        this.linesMap = new HashMap<>();
    }

    public String readStationsFile(String filePath) {
        StringBuilder jsonSerialized = new StringBuilder();
        File file = new File(filePath);
        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNext()) {
                jsonSerialized.append(scanner.nextLine());
            }
        } catch (IOException e) {
            System.out.println("Error! Such a file doesn't exist!");
        }
        return jsonSerialized.toString();
    }

    public void mapDeserializedMetroLines() {
        linesList = new ArrayList<>();
        for (var l : linesMap.entrySet()) {
            Line line = new Line(l.getKey());
            // get single line map<> and loop stations map
            for (Map.Entry<Integer, String> s : linesMap.get(l.getKey()).entrySet()) {
                line.stationsList.add(s.getValue());
            }
            linesList.add(line);
        }
    }

    public Line getLine(String lineName) {
        return linesList.stream()
                .filter(l -> l.getName().equals(lineName))
                .findAny()
                .orElse(null);
    }

    public void addStationToLine(String lineName, String station) {
        Line line = getLine(lineName);
        line.stationsList.add(station);
    }

    public void addStationToLineHead(String lineName, String station) {
        Line line = getLine(lineName);
        line.stationsList.addFirst(station);
    }

    public void removeStationFromLine(String lineName, String station) {
        Line line = getLine(lineName);
        line.stationsList.remove(station);
    }
//    public void sortStations() {
//        Map<Integer, String> stationsMap = linesMap.get()
//    }

    public void printLine(String lineName) {
        Line line = getLine(lineName);
        for (int i = 0; i < line.stationsList.size(); i++) {
            String previous = i == 0 ? "depot" : line.stationsList.get(i - 1);
            String next = i == line.stationsList.size() - 1 ? "depot" : line.stationsList.get(i + 1);
            System.out.println(previous + " - " + line.stationsList.get(i) + " - " + next);
        }
    }
}
