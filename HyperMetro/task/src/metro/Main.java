package metro;


import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String filePath =  args[0]; //"C:\\Users\\repo\\Desktop\\prague_w_time.json"; //
        Metro metro = new Metro();
        metro.readStationsFile(filePath);
        boolean quit = false;
        while (!quit) {
            List<String> cmds = ParseUtil.parseInput(scanner.nextLine().trim());
            switch (cmds.get(0)) {
                case "/exit" -> quit = true;
                case "/add-head" -> {
                    if (cmds.size() == 4) {
                        metro.addStationToLineHead(cmds.get(1), cmds.get(2), cmds.get(3));
                    } else {
                        metro.addStationToLineHead(cmds.get(1), cmds.get(2));
                    }
                }
                case "/append" -> {
                    if (cmds.size() == 4) {
                        metro.addStationToLine(cmds.get(1), cmds.get(2), cmds.get(3));
                    } else {
                        metro.addStationToLine(cmds.get(1), cmds.get(2));
                    }
                }
                case "/remove" -> metro.removeStationFromLine(cmds.get(1), cmds.get(2));
                case "/output" -> metro.printLine(cmds.get(1));
                case "/connect" -> metro.connectStations(cmds.get(1), cmds.get(2),
                        cmds.get(3), cmds.get(4));
                case "/route" -> metro.route(cmds.get(1), cmds.get(2),
                        cmds.get(3), cmds.get(4));
                case "/fastest-route" -> metro.fastestRoute(cmds.get(1), cmds.get(2),
                        cmds.get(3), cmds.get(4));
                default -> System.out.println("Invalid command");
            }
        }
    }
}
