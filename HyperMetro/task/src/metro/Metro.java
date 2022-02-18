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
    private Station station;
    public LinkedList<Station> stationsList;

    public Line(String name) {
        this.name = name;
        this.stationsList = new LinkedList<>();
    }

    public String getName() {
        return name;
    }

    public Station getStation(String stationName) {
        return stationsList.stream()
                .filter(s -> s.getName().equals(stationName))
                .findAny()
                .orElse(null);
    }

    public LinkedList<Station> getStationsList() {
        return stationsList;
    }
}

class Station {
    private String name;
    private List<Transfer> transfer;

    public Station(String name) {
        this.name = name;
        this.transfer = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public List<Transfer> getTransfer() {
        return transfer;
    }

    public boolean hasTransfer() {
        return transfer.size() !=0;
    }

    public void setTransfer(Transfer transition) {
        transfer.add(transition);
    }

    @Override
    public String toString() {
//        if (hasTransfer()) {
//            return name + transfer.get(0);
//        } else
            return name;
    }
}

class Transfer {
    private String line;
    private String station;

    public Transfer(String line, String station) {
        this.line = line;
        this.station = station;
    }

    public String getLine() {
        return line;
    }

    public String getStation() {
        return station;
    }

    @Override
    public String toString() {
        return " - " + station + " (" + line + " line)";
    }
}

public class Metro {

    private Map<String, Map<Integer, Station>> linesMap;
    private static List<Line> linesList;
    List<Station> transChecked;

    public Metro() {
        this.linesMap = new HashMap<>();
        this.transChecked = new ArrayList<>();
    }

    public static List<Line> getLinesList() {
        return linesList;
    }

    public List<Station> getNeighbors(Station station, Line line) {
        List<Station> neighbors = new ArrayList<>();
        int index = line.getStationsList().indexOf(station);
        ListIterator<Station> li = line.getStationsList().listIterator(index);
        if (li.hasPrevious()) {
            neighbors.add(li.previous());
            li.next(); // move iterator to current
        }
        li.next(); // move iterator to next
        if (li.hasNext()) {
            neighbors.add(li.next());
        }
        if (station.hasTransfer() && !transChecked.contains(station)) {
            neighbors.addAll(getTransferNeighbors(station));
        }
        return neighbors;
    }

    public List<Station> getTransferNeighbors(Station station) {
        List<Station> neighbors = new ArrayList<>();
        String transLineName = station.getTransfer().get(0).getLine();
        String transStationName = station.getTransfer().get(0).getStation();
        Line transLine = getLine(transLineName);
        Station transStation = transLine.getStation(transStationName);
        transChecked.add(station);

        int index = transLine.stationsList.indexOf(transStation);
        ListIterator<Station> li = transLine.stationsList.listIterator(index);
        if (li.hasPrevious()) {
            neighbors.add(li.previous());
            li.next(); // move iterator to current
        }
        li.next(); // move iterator to next
        if (li.hasNext()) {
            neighbors.add(li.next());
        }
        return neighbors;
    }


    public void readStationsFile(String filePath) {
        Type type = new TypeToken<HashMap<String, HashMap<Integer, Station>>>() {
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
            //add stations objects to LinkedList
            stations.forEach((no, stationData) -> line.stationsList.add(stationData));
            linesList.add(line);
        });
    }

    public Line getLine(String lineName) {
        return linesList.stream()
                .filter(l -> l.getName().equals(lineName))
                .findAny()
                .orElse(null);
    }


    public void addStationToLine(String lineName, String stationName) {
        getLine(lineName).stationsList.addLast(new Station(stationName));
    }

    public void addStationToLineHead(String lineName, String stationName) {
        getLine(lineName).stationsList.addFirst(new Station(stationName));
    }

    public void removeStationFromLine(String lineName, String stationName) {
        Line line = getLine(lineName);
        line.stationsList.remove(line.getStation(stationName));
    }

    public void connectStations(String l1, String s1, String l2, String s2) {
        getLine(l1).getStation(s1).setTransfer(new Transfer(l2, s2));
        getLine(l2).getStation(s2).setTransfer(new Transfer(l1, s2));
    }

    public void printLine(String lineName) {
        Line line = getLine(lineName);
        System.out.println("depot");
        for (int i = 0; i < line.stationsList.size(); i++) {
            System.out.print(line.stationsList.get(i));
//            System.out.println(" - Neighbours: " + getNeighbors(line.getStationsList().get(i)));
        }
        System.out.println("depot");
    }
}