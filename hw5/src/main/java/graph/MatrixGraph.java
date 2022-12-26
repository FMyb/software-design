package graph;

import drawing.Drawing;

import java.util.List;

public class MatrixGraph extends Graph {
    private final List<List<Integer>> matrix;

    public MatrixGraph(List<List<Integer>> matrix, Drawing drawing) {
        super(matrix.size(), drawing);
        this.matrix = matrix;
    }

    @Override
    public void drawGraph() {
        drawVertices();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (matrix.get(i).get(j) == 1) {
                    drawEdge(i, j);
                }
            }
        }
    }
}
