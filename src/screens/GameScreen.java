package screens;

import shapes.*;
import shapes.Point;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;


public class GameScreen extends JPanel {

    private List<Asteroid> randomAsteroids;
    private List<Bonus> bonusList;

    private Ship ship;

    private AlienShip alienShip;

    private Star[] stars;

//    private ShipFire shipFire;

    private List<Bullet> bullets = new ArrayList<>();
    private List<Bullet> bulletsAlien;

    private int scores;
    private int bonusScore;

    private int lives;

    private boolean isAlive;

    private int level;
    private boolean levelUp;

    private boolean isPause;

    private boolean isHyperspaceJump = false;
    private int initialNumOfAsteroids;
    private List<Point> objectsLocations = new ArrayList<>();

    private static final int COLLISION_PERIOD = 100;
    private static final int BONUS = 10000;
    private int counter;
    // how we track asteroid collisions
    private boolean collision = false;
    private boolean collisionAlien = false;
    private boolean collisionAlienShip = false;

    private boolean alienOccur = false;
    private int alienOccurTime = (int) ((Math.random() * 300) + 500);
    private static int collisionTime = COLLISION_PERIOD;
    private static int collisionAlienTime = COLLISION_PERIOD;
    private double blasterStartMilsTime;
    private static double blasterLastMilsTime = 5000;
    private double boosterStartMilsTime;
    private static double boosterLastMilsTime = 5000;

    public void initialise() {
        lives = 3;
        initialNumOfAsteroids = 4;
        isAlive = true;
        levelUp = false;
        isPause = true;
        alienOccur = false;
        scores = 0;
        counter = 0;
        bonusScore = BONUS;
        randomAsteroids = createRandomAsteroids(initialNumOfAsteroids, AsteroidSize.LARGE);
        stars = createStars(200, 3);
        // create the ship
        ship = createShip();
//        shipFire = createShipFire();
        bonusList = new ArrayList<>();

        this.addKeyListener(ship);
//        this.addKeyListener(shipFire);
    }

    public GameScreen() {
        initialise();
        level = 1;
        this.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_P) {
                    isPause = !isPause;
                }

                if (e.getKeyCode() == KeyEvent.VK_S) {
                    isHyperspaceJump = true;
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });

        this.addComponentListener( new ComponentAdapter() {
            @Override
            public void componentShown( ComponentEvent e ) {
                initialise();
                GameScreen.this.requestFocusInWindow();
            }
        });

    }

    private void getAllObjectLocation() {
        List<Point> locations = new ArrayList<>();
        for (Asteroid asteroid : randomAsteroids) {
            locations.add(asteroid.position);
        }

        if (alienOccur) {
            locations.add(alienShip.position);
        }
        objectsLocations = locations;
    }

    private boolean containPoints(double inX, double inY) {
        for (Point point : objectsLocations) {
            if (inX <= point.x + 10 && inX >= point.x - 10) {
                if (inY <= point.y + 10 && inY >= point.y - 10) {
                    return true;
                }
            }
        }
        return false;
    }

    private Point generateRandomLocation() {
        double x, y;
        do {
            x = Math.random() * ApplicationWindow.SCREEN_WIDTH;
            y = Math.random() * ApplicationWindow.SCREEN_HEIGHT;
        } while (containPoints(x, y));

        return new Point(x, y);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        this.setBackground(Color.BLACK);
        g.setColor(Color.black);
        g.fillRect(0,0, ApplicationWindow.SCREEN_WIDTH, ApplicationWindow.SCREEN_HEIGHT);
        counter++;
        g.setColor(Color.white);
        g.drawString("Counter is " + counter,10,10);
        g.drawString("Scores: "+ scores, 10, 20);
        g.drawString("Level: "+ level, 10, 30);
        g.drawString("Lives Left: "+ lives, 10, 40);

        // display the random asteroids
        for (Asteroid asteroid : randomAsteroids) {
            asteroid.paint(g, Color.white);
        }
        for (Star star : stars) {
            star.paint(g, Color.CYAN);
        }
        if (collision) {
            ship.paint(g, Color.red);
        } else {
            ship.paint(g, Color.magenta);
        }

        for (Bonus b : bonusList) {
            if (b.getType() == BonusType.SHIELD) {
                b.paint(g, Color.RED);
            }
            else if (b.getType() == BonusType.BLASTERS) {
                b.paint(g, Color.pink);
            }
            else {
                b.paint(g, Color.yellow);
            }
        }

        if (alienOccur) {
            alienShip.paint(g, Color.GREEN);
            bulletsAlien = alienShip.getBullets();
            for (Bullet b : bulletsAlien) {
                b.paint(g, Color.white);
            }
        }

        bullets = ship.getBullets();
        for (Bullet b : bullets) {
            b.paint(g, Color.white);
        }
    }

    public void move() {
        if (counter == alienOccurTime) {
            alienOccur = true;
            alienShip = createAlienShip();
            bulletsAlien = new ArrayList<>();
        }
        List<Asteroid> asteroidremove = new ArrayList<>();
        List<Bonus> bonusRemove = new ArrayList<>();

        for (Bonus bonus : bonusList) {
            if (bonus.collision(ship)) {
                if (bonus.getType() == BonusType.SHIELD) {
                    ship.setHaveShield(true);
                }
                else if (bonus.getType() == BonusType.BLASTERS) {
                    blasterStartMilsTime = System.currentTimeMillis();
                    ship.setHaveBlaster(true);
                }
                else {
                    boosterStartMilsTime = System.currentTimeMillis();;
                    ship.setHaveBooster(true);
                }
                bonusRemove.add(bonus);
            }

        }

        if (ship.isHaveBlaster()) {
            if (System.currentTimeMillis() - blasterStartMilsTime > blasterLastMilsTime) {
                ship.setHaveBlaster(false);
            }
        }

        if (ship.isHaveBooster()) {
            if (System.currentTimeMillis() - boosterStartMilsTime > boosterLastMilsTime) {
                ship.setHaveBooster(false);
            }
        }

        for (Asteroid asteroid : randomAsteroids) {
            asteroid.move();
            if (!collision) {
                collision = asteroid.collision(ship);
                if (collision) {
                    asteroidremove.add(asteroid);
                }
            }

            if (alienOccur) {
                if (!collisionAlien) {
                    collisionAlien = asteroid.collision(alienShip);
                    if (collisionAlien) {
                        asteroidremove.add(asteroid);
                        alienOccur = false;
                    }
                }
            }
        }

        if (collision) {
            if (collisionTime == 100) {
                if (ship.isHaveShield()) {
                    ship.setHaveShield(false);
                }
                else {
                    isAlive = false;
                    lives--;
                }
            }
            collisionTime -= 1;

            if (collisionTime <= 0) {
                collision = false;
                collisionTime = COLLISION_PERIOD;
            }
        }

        if (alienOccur) {
            if (!collisionAlienShip) {
                collisionAlienShip = alienShip.collision(ship);
                if (collisionAlienShip) {
                    alienOccur = false;
                }
            }
        }
        if (collisionAlienShip) {
            if (collisionAlienTime == 100) {
                isAlive = false;
                lives--;

            }
            collisionAlienTime -= 1;

            if (collisionAlienTime <= 0) {
                collisionAlienShip = false;
                collisionAlienTime = COLLISION_PERIOD;
            }
        }

        if (isHyperspaceJump) {
            getAllObjectLocation();
            Point newLocation = generateRandomLocation();
            this.setShip(createShipWithLocation(newLocation));
            this.addKeyListener(getShip());
            isHyperspaceJump = !isHyperspaceJump;
        }
        ship.move();
//        shipFire.move();
        if (alienOccur) {
            alienShip.move();
        }

        List<Bullet> bulletsAlienRemove = new ArrayList<>();
        List<Bullet> bulletsremove = new ArrayList<>();

        if (alienOccur) {
            bulletsAlien = alienShip.getBullets();
            for (int i = 0; i < bulletsAlien.size(); i++) {
                bulletsAlien.get(i).move();
                if (bulletsAlien.get(i).outOfBound()) {
                    bulletsAlienRemove.add(bulletsAlien.get(i));
                }

                if (ship.contains(bulletsAlien.get(i).getCenter())) {
                    isAlive = false;
                    lives--;
                    bulletsAlienRemove.add(bulletsAlien.get(i));
                }
            }
            for (int i = 0; i < bulletsAlienRemove.size(); i++) {
                bulletsAlien.remove(bulletsAlienRemove.get(i));
            }
        }
        bullets = ship.getBullets();
        for (int i = 0; i < bullets.size(); i++) {
            bullets.get(i).move();
            if (bullets.get(i).outOfBound()) {
                bulletsremove.add(bullets.get(i));
            }

            for (int j = 0; j < randomAsteroids.size(); j++) {
                if (randomAsteroids.get(j).contains(bullets.get(i).getCenter())) {
                    asteroidremove.add(randomAsteroids.get(j));
                    bulletsremove.add(bullets.get(i));
                }
            }
            if (alienOccur) {
                if (alienShip.contains(bullets.get(i).getCenter())) {
                    alienOccur = !alienOccur;
                    scores += 200;
                }
            }
        }

        for (int i = 0; i < bonusRemove.size(); i++) {
            bonusList.remove(bonusRemove.get(i));
        }

        for (int i = 0; i < asteroidremove.size(); i++) {
            if (asteroidremove.get(i).getSize() == AsteroidSize.LARGE) {
                randomAsteroids.add(createAsteroid(asteroidremove.get(i).position,
                        AsteroidSize.MEDIUM, true,
                        (Math.random() * 1.0 + 1.0) * asteroidremove.get(i).getSpeed()));
                randomAsteroids.add(createAsteroid(asteroidremove.get(i).position,
                        AsteroidSize.MEDIUM, false,
                        (Math.random() * 1.0 + 1.0) * asteroidremove.get(i).getSpeed()));

                scores += 25;
                if (asteroidremove.get(i).isContainBonus()) {
                    bonusList.add(asteroidremove.get(i).getBonus());
                }
            }
            else if (asteroidremove.get(i).getSize() == AsteroidSize.MEDIUM) {
                randomAsteroids.add(createAsteroid(asteroidremove.get(i).position,
                        AsteroidSize.SMALL, true,
                        (Math.random() * 1.0 + 1.0) * asteroidremove.get(i).getSpeed()));
                randomAsteroids.add(createAsteroid(asteroidremove.get(i).position,
                        AsteroidSize.SMALL, false,
                        (Math.random() * 1.0 + 1.0) * asteroidremove.get(i).getSpeed()));

                scores += 50;
                if (asteroidremove.get(i).isContainBonus()) {
                    bonusList.add(asteroidremove.get(i).getBonus());
                }
            }
            else {
                scores += 100;
                if (asteroidremove.get(i).isContainBonus()) {
                    bonusList.add(asteroidremove.get(i).getBonus());
                }
            }
            randomAsteroids.remove(asteroidremove.get(i));

        }

        for (int i = 0; i < bulletsremove.size(); i++) {
            bullets.remove(bulletsremove.get(i));
        }

        if (scores >= bonusScore) {
            lives++;
            bonusScore += BONUS;
        }
        if (randomAsteroids.isEmpty()) {
            levelUp = true;
            level++;
        }

    }

    // private helper method to create the Ship
    public Ship createShip() {
        // Look of ship
        Point[] shipShape = {
                new Point(0, 0),
                new Point(Ship.SHIP_WIDTH/3.5, Ship.SHIP_HEIGHT/2),
                new Point(0, Ship.SHIP_HEIGHT),
                new Point(Ship.SHIP_WIDTH, Ship.SHIP_HEIGHT/2)
        };
        // Set ship at the middle of the screen
        Point startingPosition = new Point((ApplicationWindow.SCREEN_WIDTH - Ship.SHIP_WIDTH)/2,
                (ApplicationWindow.SCREEN_HEIGHT - Ship.SHIP_HEIGHT)/2);
        int startingRotation = 0; // Start facing to the right
        return new Ship(shipShape, startingPosition, startingRotation);
    }

    public Ship createShipWithLocation(Point location) {
        // Look of ship
        Point[] shipShape = {
                new Point(0, 0),
                new Point(Ship.SHIP_WIDTH/3.5, Ship.SHIP_HEIGHT/2),
                new Point(0, Ship.SHIP_HEIGHT),
                new Point(Ship.SHIP_WIDTH, Ship.SHIP_HEIGHT/2)
        };
        // Set ship at the middle of the screen
        Point startingPosition = new Point(location.x, location.y);
        int startingRotation = 0; // Start facing to the right
        return new Ship(shipShape, startingPosition, startingRotation);
    }

    public Bonus createBonus(Asteroid asteroid, BonusType type) {
        Point[] bonusShape = new Point[10];
        Point startingPosition = new Point(asteroid.position.x, asteroid.position.y);

        double radius = 10.0;
        double innerRadius = radius*Math.sin(Math.toRadians(18)/Math.sin(Math.toRadians(54)));

        for (int i = 18; i < 360; i += 72) {
            bonusShape[(i-18)/36] = new Point(startingPosition.x + (int) (radius * Math.cos(Math.toRadians(i))),
                    startingPosition.y - (int) (radius * Math.sin(Math.toRadians(i))));
        }

        // Here (i-18)/36 will be 1, 3, 5, 7, 9
        for (int i = 54; i < 360; i += 72) {
            bonusShape[(i-18)/36] = new Point(startingPosition.x + (int) (innerRadius * Math.cos(Math.toRadians(i))),
                    startingPosition.y - (int) (innerRadius * Math.sin(Math.toRadians(i))));
        }
        int startingRotation = 0;
        return new Bonus(bonusShape, startingPosition, startingRotation, type);
    }

    public ShipFire createShipFire() {
        Point[] points = ship.getPoints();
        Point[] shipShape = {
                new Point((points[0].x + points[1].x) / 2, (points[0].y + points[1].y) / 2),
                new Point(points[0].x, (points[0].y + points[1].y) / 2),
                new Point(points[0].x, (points[2].y + points[1].y) / 2),
                new Point((points[2].x + points[1].x) / 2, (points[2].y + points[1].y) / 2),
                new Point(points[1].x, points[1].y)
        };
        // Set ship at the middle of the screen
        Point startingPosition = new Point(points[1].x - (shipShape[4].x - shipShape[1].x),
                points[1].y - (shipShape[2].y - shipShape[1].y) / 2);
        double inRotation = ship.rotation;
        return new ShipFire(shipShape, startingPosition, inRotation);
    }

    public AlienShip createAlienShip() {
        // Look of ship
        Point[] shipShape = {
                new Point(AlienShip.SHIP_WIDTH/3, 0),
                new Point(0, AlienShip.SHIP_HEIGHT/2),
                new Point(AlienShip.SHIP_WIDTH/3, AlienShip.SHIP_HEIGHT),
                new Point(AlienShip.SHIP_WIDTH/3*2, AlienShip.SHIP_HEIGHT),
                new Point(AlienShip.SHIP_WIDTH, AlienShip.SHIP_HEIGHT/2),
                new Point(AlienShip.SHIP_WIDTH/3*2, 0)
        };
        // Set ship at the middle of the screen
        Point startingPosition = new Point(20, 60);
        double inRotation = Math.random() * 360;
        return new AlienShip(shipShape, startingPosition, inRotation);
    }

    //  Create an array of random asteroids
    public List<Asteroid> createRandomAsteroids(int numberOfAsteroids, AsteroidSize size) {
        int maxAsteroidWidth, minAsteroidWidth;
        if (size == AsteroidSize.LARGE) {
            maxAsteroidWidth = 70;
            minAsteroidWidth = 70;
        }
        else if (size == AsteroidSize.MEDIUM) {
            maxAsteroidWidth = 50;
            minAsteroidWidth = 50;
        }
        else {
            maxAsteroidWidth = 30;
            minAsteroidWidth = 30;
        }
        List<Asteroid> asteroids = new ArrayList<>(numberOfAsteroids);

        for (int i = 0; i < numberOfAsteroids; ++i) {
            // Create random asteroids by sampling points on a circle
            // Find the radius first.
//            int radius = (int) (Math.random() * (maxAsteroidWidth - minAsteroidWidth) + minAsteroidWidth);
            int radius = minAsteroidWidth;
            // Find the circles angle
            double angle = (Math.random() * Math.PI * 1.0/2.0);
            if (angle < Math.PI * 1.0/5.0) {
                angle += Math.PI * 1.0/5.0;
            }
            // Sample and store points around that circle
            List<Point> asteroidSides = new ArrayList<>();
            double originalAngle = angle;
            while (angle < 2 * Math.PI) {
                double x = Math.cos(angle) * radius;
                double y = Math.sin(angle) * radius;
                asteroidSides.add(new Point(x, y));
                angle += originalAngle;
            }
            // Set everything up to create the asteroid
            Point[] inSides = asteroidSides.toArray(new Point[asteroidSides.size()]);

            Point startingPosition = new Point((double) (ApplicationWindow.SCREEN_WIDTH - Ship.SHIP_WIDTH) / 2,
                    (double) (ApplicationWindow.SCREEN_HEIGHT - Ship.SHIP_HEIGHT) / 2);

            double x, y;
            double rd = Math.random();
            if (rd < 0.33) {
                x = Math.random() * ((double) (ApplicationWindow.SCREEN_WIDTH - Ship.SHIP_WIDTH) / 2 -
                        2 * Ship.SHIP_WIDTH);
                y = Math.random() * ApplicationWindow.SCREEN_HEIGHT;
            }
            else if (rd > 0.67) {
                x = Math.random() * ((double) (ApplicationWindow.SCREEN_WIDTH - Ship.SHIP_WIDTH) / 2 -
                        2 * Ship.SHIP_WIDTH) +
                        ((double) (ApplicationWindow.SCREEN_WIDTH + Ship.SHIP_WIDTH) / 2 + 2 * Ship.SHIP_WIDTH);
                y = Math.random() * ApplicationWindow.SCREEN_HEIGHT;
            }
            else {
                x = Math.random() * (Ship.SHIP_WIDTH * 4) +
                        ((double) (ApplicationWindow.SCREEN_WIDTH - Ship.SHIP_WIDTH) / 2 - 2 * Ship.SHIP_WIDTH);
                y = Math.random() * ((double) (ApplicationWindow.SCREEN_HEIGHT - Ship.SHIP_HEIGHT) / 2 -
                        2 * Ship.SHIP_HEIGHT);
            }
            Point inPosition = new Point(x, y);
            double inRotation = Math.random() * 360;
            Asteroid aster = new Asteroid(inSides, inPosition, inRotation, size, Math.random() * 0.5 + 0.3);
            if (aster.isContainBonus()) {
                Bonus bonus;
                if (rd < 0.33) {
                    bonus = createBonus(aster, BonusType.SHIELD);
                }
                else if (rd > 0.67) {
                    bonus = createBonus(aster, BonusType.BOOSTER);
                }
                else {
                    bonus = createBonus(aster, BonusType.BLASTERS);
                }
                aster.setBonus(bonus);
            }
            asteroids.add(aster);
        }
        return asteroids;
    }

    //  Create an asteroid
    private Asteroid createAsteroid(Point inPosition, AsteroidSize size, boolean left, double speed) {
        Point point = inPosition.clone();
        int maxAsteroidWidth, minAsteroidWidth;
        if (size == AsteroidSize.LARGE) {
            maxAsteroidWidth = 70;
            minAsteroidWidth = 70;
        }
        else if (size == AsteroidSize.MEDIUM) {
            maxAsteroidWidth = 50;
            minAsteroidWidth = 50;
        }
        else {
            maxAsteroidWidth = 30;
            minAsteroidWidth = 30;
        }
        int radius = minAsteroidWidth;
        // Find the circles angle
        double angle = (Math.random() * Math.PI * 1.0/2.0);
        if (angle < Math.PI * 1.0/5.0) {
            angle += Math.PI * 1.0/5.0;
        }
        // Sample and store points around that circle
        List<Point> asteroidSides = new ArrayList<>();
        double originalAngle = angle;
        while (angle < 2 * Math.PI) {
            double x = Math.cos(angle) * radius;
            double y = Math.sin(angle) * radius;
            asteroidSides.add(new Point(x, y));
            angle += originalAngle;
        }
        // Set everything up to create the asteroid
        Point[] inSides = asteroidSides.toArray(new Point[asteroidSides.size()]);
        double inRotation;
        if (size == AsteroidSize.LARGE) {
            inRotation = Math.random() * 360;
        }
        else if (size == AsteroidSize.MEDIUM) {
            if (!left) {
                inRotation = (Math.random() * 20) + 10;
            }
            else {
                inRotation = (Math.random() * 20) + 330;
            }
        }
        else {
            if (!left) {
                inRotation = (Math.random() * 20) + 20;
            }
            else {
                inRotation = (Math.random() * 20) + 320;
            }
        }
        double rd = Math.random();
        Asteroid aster = new Asteroid(inSides, point, inRotation, size, speed);
        if (aster.isContainBonus()) {
            Bonus bonus;
            if (rd < 0.33) {
                bonus = createBonus(aster, BonusType.SHIELD);
            }
            else if (rd > 0.67) {
                bonus = createBonus(aster, BonusType.BOOSTER);
            }
            else {
                bonus = createBonus(aster, BonusType.BLASTERS);
            }
            aster.setBonus(bonus);
        }
        return aster;

    }

    // Create a certain number of stars with a given max radius
    public Star[] createStars(int numberOfStars, int maxRadius) {
        Star[] stars = new Star[numberOfStars];
        for(int i = 0; i < numberOfStars; ++i) {
            Point center = new Point(Math.random() * ApplicationWindow.SCREEN_WIDTH,
                    Math.random() * ApplicationWindow.SCREEN_HEIGHT);
            int radius = (int) (Math.random() * maxRadius);
            if(radius < 1) {
                radius = 1;
            }
            stars[i] = new Star(center, radius);
        }
        return stars;
    }

    public int getLives() {
        return lives;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public void setAlive(boolean alive) {
        isAlive = alive;
    }

    public Ship getShip() {
        return ship;
    }

    public void setShip(Ship ship) {
        this.ship = ship;
    }

    public void setLives(int lives) {
        this.lives = lives;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public boolean isLevelUp() {
        return levelUp;
    }

    public void setLevelUp(boolean levelUp) {
        this.levelUp = levelUp;
    }

    public List<Asteroid> getRandomAsteroids() {
        return randomAsteroids;
    }

    public void setRandomAsteroids(List<Asteroid> randomAsteroids) {
        this.randomAsteroids = randomAsteroids;
    }

    public int getInitialNumOfAsteroids() {
        return initialNumOfAsteroids;
    }

    public void setInitialNumOfAsteroids(int initialNumOfAsteroids) {
        this.initialNumOfAsteroids = initialNumOfAsteroids;
    }

    public int getScores() {
        return scores;
    }

    public void setScores(int scores) {
        this.scores = scores;
    }

    public boolean isPause() {
        return isPause;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

}
