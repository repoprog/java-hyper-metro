package metro;


import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String filePath = "C:\\Users\\repo\\Desktop\\prague_w_time.json"; //args[0];
        Metro metro = new Metro();
        metro.readStationsFile(filePath);
        boolean quit = false;
        while (!quit) {
            List<String> cmds = ParseUtil.parseInput(scanner.nextLine().trim());
            switch (cmds.get(0)) {
                case "/exit" -> quit = true;
                case "/add-head" -> metro.addStationToLineHead(cmds.get(1), cmds.get(2), cmds.get(3));
                case "/append" -> metro.addStationToLine(cmds.get(1), cmds.get(2), cmds.get(3));
                case "/remove" -> metro.removeStationFromLine(cmds.get(1), cmds.get(2));
                case "/output" -> metro.printLine(cmds.get(1));
                case "/connect" -> metro.connectStations(cmds.get(1), cmds.get(2),
                        cmds.get(3), cmds.get(4));
                case "/route" -> metro.route(cmds.get(1), cmds.get(2),
                        cmds.get(3), cmds.get(4));
                default -> System.out.println("Invalid command");
            }
        }
    }
}
