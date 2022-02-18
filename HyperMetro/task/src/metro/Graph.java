package metro;

import java.util.*;

public class Graph {
    Metro metro;
    List<Line> lineList = Metro.getLinesList();
    private Map<String, List<Station>> adj;

    public Graph(Metro metro) {
        this.metro = metro;
    }

    public void createAdj() {
        adj = new HashMap<>();
        lineList.forEach(line -> line.getStationsList().forEach(station ->
                adj.putIfAbsent(station.getName(), metro.getNeighbors(station, line))));
    }

    public List<Station> bfsOfGraph(String fromLine, String startStation) {
        Station station = metro.getLine(fromLine).getStation(startStation);
        List<Station> bfs = new ArrayList<>();
        HashSet<Station> visited = new HashSet<>();
        Queue<Station> queue = new LinkedList<>();
        queue.offer(station);
        visited.add(station);
        bfs.add(station);
        while (!queue.isEmpty()) {
            Station s = queue.poll();
            for (Station n : adj.get(s.getName())) {
                if (!visited.contains(n)) {
                    visited.add(n);
                    queue.offer(n);
                    bfs.add(n);
                }
            }
        }
        bfs.forEach(System.out::println);
        return bfs;
    }
}
