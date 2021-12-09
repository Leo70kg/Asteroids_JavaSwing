package shapes;

import java.awt.*;

public class Bonus extends Polygon{
    private BonusType type;

    public Bonus(Point[] inShape, Point inPosition, double inRotation, BonusType type) {
        super(inShape, inPosition, inRotation);
        this.type = type;
    }

    public BonusType getType() {
        return type;
    }

    @Override
    public void paint(Graphics brush, Color color) {
        Point[] pts = getPoints();
        int[] xpts = new int[pts.length];
        int[] ypts = new int[pts.length];
        int npts = pts.length;

        for (int i = 0; i < npts; i++) {
            xpts[i] = (int)pts[i].x;
            ypts[i] = (int)pts[i].y;
        }

        brush.setColor(color);
        brush.drawPolygon(xpts, ypts, npts);

    }

    @Override
    public void move() {

    }

    public boolean collision(Polygon poly) {
        Point[] points = poly.getPoints();
        for(Point p : points) {
            if (this.contains(p)) {
                return true;
            }
        }
        return false;
    }
}
