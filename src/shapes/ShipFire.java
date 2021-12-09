package shapes;

import screens.ApplicationWindow;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

public class ShipFire extends Polygon implements KeyListener {

    private boolean forward;
    private boolean right;
    private boolean left;
    private boolean down;
    private boolean brake = true;


    public ShipFire(Point[] inShape, Point inPosition, double inRotation) {
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
        // Check forward movement
        if (forward) {
            position.x += 3 * Math.cos(Math.toRadians(rotation));
            position.y += 3 * Math.sin(Math.toRadians(rotation));
            brake = false;
        }
        // Check rotation to right
        if (right) {
            rotate(3);
        }
        // Check rotation to left
        if (left) {
            rotate(-3);
        }
        if (down) {
            brake = true;
        }

        if (!brake) {
            position.x += 3 * Math.cos(Math.toRadians(rotation));
            position.y += 3 * Math.sin(Math.toRadians(rotation));
        }

        if (position.x > ApplicationWindow.SCREEN_WIDTH) {
            position.x -= ApplicationWindow.SCREEN_WIDTH;
        } else if(position.x < 0) {
            position.x += ApplicationWindow.SCREEN_WIDTH;
        }
        if (position.y > ApplicationWindow.SCREEN_HEIGHT) {
            position.y -= ApplicationWindow.SCREEN_HEIGHT;
        } else if(position.y < 0) {
            position.y += ApplicationWindow.SCREEN_HEIGHT;
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {

        if (e.getKeyCode() == KeyEvent.VK_UP) {
            forward = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            right = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            left = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            down = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_UP) {
            forward = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            right = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            left = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            down = false;
        }

    }

    @Override
    public void keyTyped(KeyEvent e) {
        return;
    }
}
