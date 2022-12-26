package drawing;

import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.List;

public class AwtDrawing extends Frame implements Drawing {
    private final long width = 500;
    private final long height = 500;
    private final List<List<Long>> circles = new ArrayList<>();
    private final List<List<Long>> lines = new ArrayList<>();

    @Override
    public void paint(Graphics g) {
        Graphics2D graphics2D = (Graphics2D) g;
        graphics2D.setPaint(Color.black);
        for (var circle : circles) {
            double x = (double) circle.get(0);
            double y = (double) circle.get(1);
            double r = (double) circle.get(2);
            graphics2D.fill(new Ellipse2D.Double(x - r / 2, y - r / 2, r, r));
        }
        for (List<Long> line : lines) {
            double x1 = (double) line.get(0);
            double y1 = (double) line.get(1);
            double x2 = (double) line.get(2);
            double y2 = (double) line.get(3);
            graphics2D.draw(new Line2D.Double(x1, y1, x2, y2));
        }
    }

    @Override
    public long width() {
        return width;
    }

    @Override
    public long height() {
        return height;
    }

    @Override
    public void drawCircle(long x, long y, long r) {
        circles.add(List.of(x, y, r));
    }

    @Override
    public void drawLine(long x1, long y1, long x2, long y2) {
        lines.add(List.of(x1, y1, x2, y2));
    }
}
