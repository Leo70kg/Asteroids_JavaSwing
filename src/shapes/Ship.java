package shapes;

/**
 * A representation of a ship.
 */
import screens.ApplicationWindow;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;


public class Ship extends Polygon implements KeyListener{
    public static final int SHIP_WIDTH = 40;
    public static final int SHIP_HEIGHT = 25;

    private boolean forward;
    private boolean right;
    private boolean left;
    private boolean down;
    private boolean brake = true;
    private boolean shoot = false;
    private boolean haveShield = false;
    private boolean haveBlaster = false;
    private boolean haveBooster = false;
    private double speed = 0;

    private List<Bullet> bulletsShot = new ArrayList<>();

    public Ship(Point[] inShape, Point inPosition, double inRotation) {
        super(inShape, inPosition, inRotation);

    }

    public boolean isHaveShield() {
        return haveShield;
    }

    public void setHaveShield(boolean haveShield) {
        this.haveShield = haveShield;
    }

    public boolean isHaveBlaster() {
        return haveBlaster;
    }

    public void setHaveBlaster(boolean haveBlaster) {
        this.haveBlaster = haveBlaster;
    }

    public boolean isHaveBooster() {
        return haveBooster;
    }

    public void setHaveBooster(boolean haveBooster) {
        this.haveBooster = haveBooster;
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
            if (speed < 5) {
                if (haveBooster) {
                    speed += 0.5;
                }
                else {
                    speed += 0.1;
                }
                speed = speed > 5 ? 5 : speed;
            }
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
            if (speed > 0) {
                if (haveBooster) {
                    speed -= 0.5;

                }
                else {
                    speed -= 0.1;
                }
                speed = speed < 0 ? 0 : speed;
            }
        }
        position.x += speed * Math.cos(Math.toRadians(rotation));
        position.y += speed * Math.sin(Math.toRadians(rotation));

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


    public List<Bullet> getBullets() {
        return bulletsShot;
    }

    @Override
    public void keyPressed(KeyEvent e) {

        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            if (!shoot) {
                if (haveBlaster) {
                    Point p1 = new Point(position.x - Math.sqrt(2)*2, position.y - Math.sqrt(2)*2);
                    Bullet b1 = new Bullet(p1, (int) rotation);
                    Point p2 = new Point(position.x + Math.sqrt(2)*2, position.y + Math.sqrt(2)*2);
                    Bullet b2 = new Bullet(p2, (int) rotation);
                    bulletsShot.add(b1);
                    bulletsShot.add(b2);
                }
                else {
                    Point p = new Point(position.x, position.y);
                    Bullet b = new Bullet(p, (int) rotation);
                    bulletsShot.add(b);
                }
            }

            shoot = true;
        }
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
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            shoot = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            down = false;
        }

    }

    @Override
    public void keyTyped(KeyEvent e) {
        return;
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

