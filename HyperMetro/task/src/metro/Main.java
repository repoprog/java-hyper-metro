package metro;


import java.io.*;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        String filePath = args[0]; //"C:\\Users\\repo\\Desktop\\stations.txt";/
        Metro metro = new Metro();
        metro.readStationsFile(filePath);
        metro.addDepots();
    }
}

class Metro {

    private SingleLinkedLis stations;

    public Metro() {
        stations = new SingleLinkedLis();
    }

    public void readStationsFile(String filePath) {
        File file = new File(filePath);
        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNext()) {
                stations.addLast(scanner.nextLine());
            }
        } catch (IOException e) {
            System.out.println("Error! Such a file doesn't exist!");
        }
    }

    public void addDepots() {
        if (!stations.isEmpty()) {
            stations.addFirst("depot");
            stations.addLast("depot");
            stations.print();
        }
    }
}


class SingleLinkedLis {

    public Node head = null;

    class Node {
        private String data;
        private Node next;

        public Node(String data) {
            this.data = data;
            this.next = null;
        }
    }

    public boolean isEmpty() {
        return head == null;
    }

    public void addFirst(String data) {
        Node newNode = new Node(data);
        newNode.next = head;
        head = newNode;
    }

    public void addLast(String data) {
        Node newNode = new Node(data);
        if (head == null) {
            head = newNode;
        } else {
            Node current = head;
            while (current.next != null) {
                current = current.next;
            }
            current.next = newNode;
        }
    }

    public void print() {
        if (head == null) {
            System.out.println("The List is empty.");
        } else {
            Node startStation = null;
            Node current = head;
            boolean quit = false;
            while (!quit) {
                int printedStations = 0;
                while (printedStations++ != 3) {
                    System.out.print(printedStations != 3 ? current.data + " - " : current.data);
                    if (printedStations == 2) {
                        startStation = current;
                    }
                    current = current.next;
                    if (current == null) {
                        quit = true;
                    }
                }
                System.out.println();
                current = startStation;
            }
        }
    }
}
