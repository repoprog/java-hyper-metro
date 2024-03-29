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
    private List<String> prev;
    private List<String> next;
    private List<Transfer> transfer;
    private Integer time;


    public Station(String name, Integer time) {
        this.name = name;
        this.transfer = new ArrayList<>();
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public Integer getTime() {
        return time;
    }

    public List<String> getPrev() {
        return prev;
    }

    public List<String> getNext() {
        return next;
    }

    public List<Transfer> getTransfer() {
        return transfer;
    }


    public boolean hasTransfer() {
        return transfer.size() != 0;
    }

    public void setTransfer(Transfer transition) {
        transfer.add(transition);
    }

    @Override
    public String toString() {
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

    private Map<String, List<Station>> linesMap;
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
        station.getPrev().forEach(sName -> neighbors.add(line.getStation(sName)));
        station.getNext().forEach(sName -> neighbors.add(line.getStation(sName)));
        // recursively get neighbours of transfer station
        if (station.hasTransfer() && !transChecked.contains(station)) {
            for(int i = 0; i < station.getTransfer().size(); i++) {
                String transLineName = station.getTransfer().get(i).getLine();
                String transStationName = station.getTransfer().get(i).getStation();
                Line transLine = getLine(transLineName);
                Station transStation = transLine.getStation(transStationName);
                transChecked.add(transStation);
                neighbors.addAll(getNeighbors(transStation, transLine));
            }
        }
        transChecked.clear();
        return neighbors;
    }

    public Map<Station, Integer> getNeighborsDijkstra(Station station, Line line) {
        int TRANSFER_TIME = 5;
        Map<Station, Integer> neighbors = new HashMap<>();
        int index = line.getStationsList().indexOf(station);
        ListIterator<Station> li = line.getStationsList().listIterator(index);
        // For Graph if previousSt take previous neighbour time if nextSt take currSt time
        station.getPrev().forEach(sName ->
                neighbors.put(line.getStation(sName), line.getStation(sName).getTime()));
        station.getNext().forEach(sName ->
                neighbors.put(line.getStation(sName), station.getTime()));
        if (station.hasTransfer()) {
            for (int i = 0; i < station.getTransfer().size(); i++) {
                String transLineName = station.getTransfer().get(i).getLine();
                String transStationName = station.getTransfer().get(i).getStation();
                Line transLine = getLine(transLineName);
                Station transStation = transLine.getStation(transStationName);
                neighbors.put(transStation, TRANSFER_TIME);
            }
        }
        return neighbors;
    }

    public void readStationsFile(String filePath) {
        Type type = new TypeToken<HashMap<String, List<Station>>>() {
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
            //add stations objects to LinkedList
            stations.forEach((station) -> line.stationsList.add(station));
            linesList.add(line);
        });
    }

    public Line getLine(String lineName) {
        return linesList.stream()
                .filter(l -> l.getName().equals(lineName))
                .findAny()
                .orElse(null);
    }
    public Line getLine(Station station) {
        Line line = null;
        for (Line l : linesList) {
            if (l.getStationsList().contains(station))
                line = l;
        }
        return line;
    }

    public void addStationToLine(String lineName, String stationName, String time) {
        getLine(lineName).stationsList.addLast(new Station(stationName, Integer.parseInt(time)));
    }

    public void addStationToLine(String lineName, String stationName) {
        getLine(lineName).stationsList.addLast(new Station(stationName, 0));
    }

    public void addStationToLineHead(String lineName, String stationName, String time) {
        getLine(lineName).stationsList.addFirst(new Station(stationName, Integer.parseInt(time)));
    }

    public void addStationToLineHead(String lineName, String stationName) {
        getLine(lineName).stationsList.addFirst(new Station(stationName, 0));
    }

    public void removeStationFromLine(String lineName, String stationName) {
        Line line = getLine(lineName);
        line.stationsList.remove(line.getStation(stationName));
    }

    public void connectStations(String l1, String s1, String l2, String s2) {
        getLine(l1).getStation(s1).setTransfer(new Transfer(l2, s2));
        getLine(l2).getStation(s2).setTransfer(new Transfer(l1, s2));
    }

    public void route(String fromLine, String startStation, String toLine, String endStation) {
        Graph graph = new Graph(this);
        graph.bfsOfGraph(fromLine, startStation, toLine, endStation);
    }

    public void fastestRoute(String fromLine, String startStation, String toLine, String endStation) {
        Graph graph = new Graph(this);
        graph.dijkstra(fromLine, startStation, toLine, endStation);
    }

    public void printLine(String lineName) {
        Line line = getLine(lineName);
        System.out.println("depot");
        for (int i = 0; i < line.stationsList.size(); i++) {
            System.out.println(line.stationsList.get(i));
        }
        System.out.println("depot");
    }
}