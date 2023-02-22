package metro;

import java.util.*;

public class Graph {
    private Metro metro;
    private List<Line> lineList = Metro.getLinesList();
    private Map<Station, List<Station>> adj;
    private Map<Station, Map<Station, Integer>> adjDks;

    public Graph(Metro metro) {
        this.metro = metro;
    }

    public void createAdj() {
        adj = new HashMap<>();
        lineList.forEach(line -> line.getStationsList().forEach(station ->
                adj.put(station, metro.getNeighbors(station, line))));
    }

    public void createAdjDijkstra() {
        adjDks = new HashMap<>();
        lineList.forEach(line -> line.getStationsList().forEach(station ->
                adjDks.put(station, metro.getNeighborsDijkstra(station, line))));

    }

    public void bfsOfGraph(String fromLine, String startStation, String toLine, String endStation) {
        createAdj();
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
        List<Station> route = generateRoute(start, end, parentSt);
        printRoute(route);
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
        return route;
    }

    public void dijkstra(String fromLine, String startStation, String toLine, String endStation) {
        createAdjDijkstra();
        PriorityQueue<Map.Entry<Station, Integer>> pq =
                new PriorityQueue<>(Comparator.comparingInt(Map.Entry::getValue));
        List<Station> visited = new ArrayList<>();
        Station start = metro.getLine(fromLine).getStation(startStation);
        Station end = metro.getLine(toLine).getStation(endStation);
        Map<Station, Integer> timeToStart = new HashMap<>();
        Map<Station, Station> parentStat = new HashMap<>();

        // set all distances to infinity (here int MAX_VALUE)
        adjDks.keySet().forEach(station -> timeToStart.put(station, Integer.MAX_VALUE));
        // set
        timeToStart.put(start, 0);
        pq.offer(new AbstractMap.SimpleEntry<>(start, 0));
        visited.add(start);
        while (!pq.isEmpty()) {
            Station p = pq.poll().getKey();
            if (p == end) break;
            Map<Station, Integer> neighbours = adjDks.get(p);
            for (Station n : neighbours.keySet()) {
                if (!visited.contains(n)) {
                    visited.add(n);
                    int currentTime = timeToStart.get(p) + neighbours.get(n);
                    // for p calculate the shortest time of each neighbor to start station
                    if (currentTime < timeToStart.get(n)) {
                        timeToStart.put(n, currentTime);
                        parentStat.put(n, p);
                        pq.offer(new AbstractMap.SimpleEntry<>(n, currentTime));
                    }
                }
            }
        }
        List<Station> fastestRoute = generateRoute(start, end, parentStat);
        printRoute(fastestRoute, timeToStart.get(end));
    }

    public void printRoute(List<Station> route, Integer time) {
        if (route != null) {
            for (Station s : route) {
                System.out.println(s.getName());
                if (s.hasTransfer() && wasTransferred(s, route)) {
                    System.out.println("Transition to line " + getNextLine(s, route).getName());
                }
            }
            System.out.println("Total: " + time + " minutes in the way");
        }
    }

    public void printRoute(List<Station> route) {
        if (route != null) {
            for (Station s : route) {
                System.out.println(s.getName());
                if (s.hasTransfer() && wasTransferred(s, route)) {
                    System.out.println("Transition to line " + getNextLine(s, route).getName());
                    System.out.println(s.getName());
                }
            }
        }
    }

    public boolean wasTransferred(Station station, List<Station> route) {
        Line lineOfCurrSt= metro.getLine(station);
        Line lineOfNextSt = getNextLine(station, route);
        if (lineOfNextSt == null) {
            return false;
        }
        return !lineOfCurrSt.getName().equals(lineOfNextSt.getName());
    }

    public Line getNextLine(Station station, List<Station> route) {
        int index = route.indexOf(station);
        boolean isLastSt = route.indexOf(station) == route.size()-1;
        if (isLastSt) {
            return null;
        }
        Station nexStation = route.get(index + 1);
        return metro.getLine(nexStation);
    }
}
