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

    public List<Station> getNeighbor(Station station) {
        List<Station> neighbors = new ArrayList<>();
        int index = stationsList.indexOf(station);
        ListIterator<Station> li = stationsList.listIterator(index);
        if (li.hasPrevious()) {
            neighbors.add(li.previous());
            li.next(); // move iterator to current
        }
        li.next(); // move iterator to next
        if (li.hasNext()) {
            neighbors.add(li.next());
        }
        if (station.getTransfer().size() != 0) {
            String stationName = station.getTransfer().get(0).getStation();
            neighbors.add(getStation(stationName));
        }
        System.out.println("Neighbours of: " + station + " " + neighbors);
        return neighbors;
    }
}

class Station {
    private String name;
    private List<Transfer> transfer;

    public Station(String name) {
        this.name = name;
        this.transfer = new ArrayList<>();
    }

    static class Transfer {
        private String line;
        private String station;

        public Transfer(String line, String station) {
            this.line = line;
            this.station = station;
        }

        public String getStation() {
            return station;
        }

        @Override
        public String toString() {
            return " - " + station + " (" + line + " line)";
        }
    }

    public String getName() {
        return name;
    }

    public void setTransfer(Transfer transition) {
        transfer.add(transition);
    }

    public List<Transfer> getTransfer() {
        return transfer;
    }

    @Override
    public String toString() {
        if (transfer.size() != 0) {
            return name + transfer.get(0);
        } else return name;
    }
}

class Transfer {
    private String line;
    private String station;

    public Transfer(String line, String station) {
        this.line = line;
        this.station = station;
    }

    @Override
    public String toString() {
        return " - " + station + " (" + line + " line)";
    }

    public String getStation() {
        return station;
    }
}

public class Metro {

    private Map<String, Map<Integer, Station>> linesMap;
    private List<Line> linesList;

    public Metro() {
        this.linesMap = new HashMap<>();
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

    public void addStationToLine(String lineName, String station) {
        getLine(lineName).stationsList.addLast(new Station(station));
    }

    public void addStationToLineHead(String lineName, String station) {
        getLine(lineName).stationsList.addFirst(new Station(station));
    }

    public void removeStationFromLine(String lineName, String station) {
        Line line = getLine(lineName);
        line.stationsList.remove(line.getStation(station));
    }

    public void connectStations(String fromLine, String fromStation, String toLine, String toStation) {
        getLine(fromLine).getStation(fromStation).setTransfer(new Station.Transfer(toLine, toStation));
        getLine(toLine).getStation(toStation).setTransfer(new Station.Transfer(fromLine, toStation));
    }

    public void printLine(String lineName) {
        Line line = getLine(lineName);
        System.out.println("depot");
        for (int i = 0; i < line.stationsList.size(); i++) {
            System.out.println(line.stationsList.get(i));
            line.getNeighbor(line.stationsList.get(i));
        }
        System.out.println("depot");
    }
}
