package metro;

import java.util.*;

public class Graph {
    private Metro metro;
    private List<Line> lineList = Metro.getLinesList();
    private boolean linesChanged;
    private Map<Station, List<Station>> adj;

    public Graph(Metro metro) {
        this.metro = metro;
    }

    public void createAdj() {
        adj = new HashMap<>();
        lineList.forEach(line -> line.getStationsList().forEach(station ->
                adj.put(station, metro.getNeighbors(station, line))));
    }

    public List<Station> bfsOfGraph(String fromLine, String startStation, String toLine, String endStation) {
        createAdj();
        linesChanged = metro.getLine(fromLine) != metro.getLine(toLine);
        Station start = metro.getLine(fromLine).getStation(startStation);
        Station end = metro.getLine(toLine).getStation(endStation);
        //parent list holds value of vertex which introduced this vertex
        Map<Station, Station> parentSt = new HashMap<>();
        HashSet<Station> visited = new HashSet<>();
        Queue<Station> queue = new LinkedList<>();
        queue.offer(start);
        visited.add(start);
        parentSt.put(start, start);

        while (!queue.isEmpty()) {
            Station currentSt = queue.poll();
            if (currentSt == end) break;
            List<Station> neighbours = adj.get(currentSt);
            for (Station n : neighbours) {
                if (!visited.contains(n)) {
                    visited.add(n);
                    queue.offer(n);
                    parentSt.put(n, currentSt);
                }
            }
        }
        return generateRoute(start, end, parentSt);
    }

    public List<Station> generateRoute(Station start, Station end, Map<Station, Station> parentSt) {
        List<Station> route = new ArrayList<>();
        Station searchKey = end;
        while (parentSt.containsKey(searchKey)) {
            if (searchKey.equals(start)) break;
            route.add(searchKey);
            searchKey = parentSt.get(searchKey);
        }
        route.add(searchKey);
        Collections.reverse(route);
        printRoute(route);
        return route;
    }

    public void printRoute(List<Station> route) {
        if (route != null) {
            for (Station s : route) {
                System.out.println(s.getName());
                if (linesChanged && s.hasTransfer()) {
                    System.out.println("Transition to line " + s.getTransfer().get(0).getLine());
                    System.out.println(s.getName());
                }
            }
        }
    }
}
