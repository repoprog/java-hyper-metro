package metro;


import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String filePath = "C:\\Users\\repo\\Desktop\\test.json";// args[0];
        Metro metro = new Metro();
        ParseUtil.parseFile(filePath, metro);
        boolean quit = false;
        while (!quit) {
            List<String> commands = ParseUtil.parseInput(scanner.nextLine());
            commands.forEach(c -> System.out.println(c));
            switch (commands.get(0)) {
                case "/exit" -> quit = true;
                case "/add" -> metro.addStationToLineHead(commands.get(1), commands.get(2));
                case "/append" -> metro.addStationToLine(commands.get(1), commands.get(2));
                case "/remove" -> metro.removeStationFromLine(commands.get(1), commands.get(2));
                case "/output" -> metro.printLine(commands.get(1));
                default -> System.out.println("Invalid command");
            }
        }
//        metro.addDepots();

    }
}

