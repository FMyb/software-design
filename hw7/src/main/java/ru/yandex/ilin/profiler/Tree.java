package ru.yandex.ilin.profiler;


import java.io.PrintStream;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

public class Tree {
    private final Node head;

    public Tree(String name) {
        this.head = new Node(name, null);
    }

    public void addElement(String name, String element) {
        String[] path = name.split("[.]");
        Node node = head;
        for (int i = 0; i < path.length; i++) {
            String tmp = (i == path.length - 1 ? element : null);
            node = node.children.computeIfAbsent(path[i], id -> new Node(id, tmp));
        }
    }

    public void print(PrintStream printStream) {
        printStream.println(head.id);
        head.children.values().forEach(x -> print(printStream, x, ""));
    }

    private void print(PrintStream printStream, Node node, String prefix) {
        String element = node.element == null ? "" : node.element;
        printStream.println(prefix + node.id + " " + element);
        node.children.values().forEach(x -> print(printStream, x, prefix + "*  "));
    }

    private static class Node {
        private final String id;
        private final String element;
        private final Map<String, Node> children = new TreeMap<>();


        private Node(String id, String element) {
            this.id = id;
            this.element = element;
        }
    }
}
