package metro;


import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String filePath = "C:\\Users\\repo\\Desktop\\prague.json"; //args[0];
        Metro metro = new Metro();
        metro.readStationsFile(filePath);
        boolean quit = false;
        while (!quit) {
            List<String> commands = ParseUtil.parseInput(scanner.nextLine().trim());
            switch (commands.get(0)) {
                case "/exit" -> quit = true;
                case "/add-head" -> metro.addStationToLineHead(commands.get(1), commands.get(2));
                case "/append" -> metro.addStationToLine(commands.get(1), commands.get(2));
                case "/remove" -> metro.removeStationFromLine(commands.get(1), commands.get(2));
                case "/output" -> metro.printLine(commands.get(1));
                case "/connect" -> metro.connectStations(commands.get(1), commands.get(2),
                        commands.get(3), commands.get(4));
                default -> System.out.println("Invalid command");
            }
        }
    }
}
