package graph;

import drawing.Drawing;

import java.util.List;

public abstract class Graph {
    private final Drawing drawing;
    protected final int n;

    protected Graph(int n, Drawing drawing) {
        this.n = n;
        this.drawing = drawing;
    }

    public void drawVertices() {
        for (int i = 0; i < n; i++) {
            List<Long> coordinates = coordinates(i);
            drawing.drawCircle(coordinates.get(0), coordinates.get(1), coordinates.get(2));
        }
    }

    public void drawEdge(int v1, int v2) {
        List<Long> coord1 = coordinates(v1);
        List<Long> coord2 = coordinates(v2);
        drawing.drawLine(coord1.get(0), coord1.get(1), coord2.get(0), coord2.get(1));
    }

    public abstract void drawGraph();

    private List<Long> coordinates(int v) {
        long w = drawing.height();
        long h = drawing.height();
        double r = Math.min(w, h) / 2.0;
        double angle = 2 * Math.PI / n;
        double c = Math.sqrt(2 * r * r - 2 * r * r * Math.cos(angle));
        double s = 0.5 * r * r * Math.sin(angle);
        double rad = 2 * s / (r + r + c);
        double x0 = w / 2.0;
        double y0 = h / 2.0;
        double x1 = x0 + Math.cos(angle * v) * r;
        double y1 = y0 + Math.sin(angle * v) * r;
        double x2 = x0 + Math.cos(angle * (v + 1)) * r;
        double y2 = y0 + Math.sin(angle * (v + 1)) * r;
        double xc = (x1 + x2) / 2;
        double yc = (y1 + y2) / 2;
        double bisectorLen = Math.sqrt(Math.pow(xc - x0, 2) + Math.pow(yc - y0, 2));
        double dx = (xc - x0) / bisectorLen;
        double dy = (yc - y0) / bisectorLen;
        double len = bisectorLen * 2 * r / (2 * r + c);
        return List.of((long) (x0 + dx * len), (long) (y0 + dy * len), (long) rad);
    }
}
