import drawing.AwtDrawing;
import drawing.Drawing;
import drawing.JavaFxDrawing;
import graph.EdgesGraph;
import graph.Graph;
import graph.MatrixGraph;
import javafx.application.Application;
import javafx.scene.Group;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        List<List<Integer>> ed = new ArrayList<>();

        Drawing drawing = "fx".equals(args[2]) ? new JavaFxDrawing() : new AwtDrawing();
        Graph graph;

        try {
            Scanner scanner = new Scanner(new File(args[1]));
            if ("matrix".equals(args[0])) {
                int n = scanner.nextInt();
                for (int i = 0; i < n; i++) {
                    ed.add(new ArrayList<>());
                    for (int j = 0; j < n; j++) {
                        ed.get(i).add(scanner.nextInt());
                    }
                }
                graph = new MatrixGraph(ed, drawing);
            } else if ("edges".equals(args[0])) {
                int n = scanner.nextInt();
                int m = scanner.nextInt();
                for (int i = 0; i < n; i++) {
                    ed.add(new ArrayList<>());
                }
                for (int i = 0; i < m; i++) {
                    int u = scanner.nextInt();
                    int v = scanner.nextInt();
                    ed.get(u - 1).add(v - 1);
                    ed.get(v - 1).add(u - 1);
                }
                graph = new EdgesGraph(ed, drawing);
            } else {
                throw new UnsupportedOperationException();
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        graph.drawGraph();

        if ("fx".equals(args[2])) {
            Application.launch(((JavaFxDrawing) drawing).getClass(), "");
        } else if("awt".equals(args[2])) {
            Frame frame = (AwtDrawing) drawing;
            frame.addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent we) {
                    System.exit(0);
                }
            });
            frame.setSize(500, 500);
            frame.setVisible(true);
        }
    }
}
