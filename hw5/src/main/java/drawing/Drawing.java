package drawing;

public interface Drawing {
    long width();

    long height();

    void drawCircle(long x, long y, long r);

    void drawLine(long x1, long y1, long x2, long y2);
}
