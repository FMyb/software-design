package graph;

import drawing.Drawing;

import java.util.List;

public class EdgesGraph extends Graph {
    private List<List<Integer>> ed;

    public EdgesGraph(List<List<Integer>> ed, Drawing drawing) {
        super(ed.size(), drawing);
        this.ed = ed;
    }

    @Override
    public void drawGraph() {
        drawVertices();
        for (int i = 0; i < ed.size(); i++) {
            for (int j : ed.get(i)) {
                drawEdge(i, j);
            }
        }
    }
}
