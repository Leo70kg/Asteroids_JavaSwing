package shapes;

import screens.ApplicationWindow;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class AlienShip extends Polygon{

    public static final int SHIP_WIDTH = 80;
    public static final int SHIP_HEIGHT = 50;

    private double shotStartTime = System.currentTimeMillis();
    private double shotIntervalInMils = 1000;

    private List<Bullet> bulletsShot = new ArrayList<>();

    public AlienShip(Point[] inShape, Point inPosition, double inRotation) {
        super(inShape, inPosition, inRotation);

    }

    // Create paint method to paint a ship
    public void paint(Graphics brush, Color color) {
        Point[] points = getPoints();
        int[] xPoints = new int[points.length];
        int[] yPoints = new int[points.length];
        int nPoints = points.length;
        for(int i = 0; i < nPoints; ++i) {
            xPoints[i] = (int) points[i].x;
            yPoints[i] = (int) points[i].y;
        }
        brush.setColor(color);
        brush.fillPolygon(xPoints, yPoints, nPoints);

    }

    public void move() {
        position.x += 0.6 * Math.cos(Math.toRadians(rotation));
        position.y += 0.6 * Math.sin(Math.toRadians(rotation));

        /**
         * If the asteroid moves off of the screen from the
         * x or y axis, have it re-appear from the other side.
         */
        if (position.x > ApplicationWindow.SCREEN_WIDTH) {
            position.x -= ApplicationWindow.SCREEN_WIDTH;
        } else if(position.x < 0) {
            position.x += ApplicationWindow.SCREEN_WIDTH;
        }
        if(position.y > ApplicationWindow.SCREEN_HEIGHT) {
            position.y -= ApplicationWindow.SCREEN_HEIGHT;
        } else if(position.y < 0) {
            position.y += ApplicationWindow.SCREEN_HEIGHT;
        }

        if (System.currentTimeMillis() - shotStartTime > shotIntervalInMils) {
            Point p = new Point(position.x, position.y);
            Bullet b = new Bullet(p, (int) rotation);
            bulletsShot.add(b);
            shotStartTime += shotIntervalInMils;
        }
    }


    public List<Bullet> getBullets() {
        return bulletsShot;
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
