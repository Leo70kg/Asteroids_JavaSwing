package shapes;

import screens.ApplicationWindow;

import java.awt.*;


public class Asteroid extends Polygon {
    private AsteroidSize size;
    private static final double LARGE_SPEED = 0.5;
    private double speed;
    private boolean containBonus;
    private Bonus bonus;

    public Asteroid(Point[] inShape, Point inPosition, double inRotation, AsteroidSize size, double speed) {
        super(inShape, inPosition, inRotation);
        this.size = size;
        this.speed = speed;
        containBonus = Math.random() < 0.2;
    }

    public void setBonus(Bonus bonus) {
        this.bonus = bonus;
    }

    public Bonus getBonus() {
        return bonus;
    }

    public boolean isContainBonus() {
        return containBonus;
    }

    public AsteroidSize getSize() {
        return size;
    }

    public double getSpeed() {
        return speed;
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

    // Detect if there was a collision
    public boolean collision(Polygon poly) {
        Point[] points = poly.getPoints();
        for(Point p : points) {
            if (this.contains(p)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void move() {

        position.x += speed * Math.cos(Math.toRadians(rotation));
        position.y += speed * Math.sin(Math.toRadians(rotation));

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
    }
}
