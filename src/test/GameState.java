package test;

import org.newdawn.slick.*;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

import java.util.ArrayList;
import java.util.Random;

public class GameState extends BasicGameState {
    Music music;
    private int highScore = 0;
    private int spanwrate;
    private float speed;
    private int score;
    private org.newdawn.slick.geom.Circle mouseBall;
    private ArrayList<org.newdawn.slick.geom.Circle> balls;
    private ArrayList<Circle> healthPickupList;
    private ArrayList<Circle> bombPickupList;
    private int timePassed;
    private int health;
    private int bombCounter;

    @Override
    public void init(GameContainer gameContainer, StateBasedGame stateBasedGame) throws SlickException {
        destroyAll();
        music = new Music("resources/music.ogg");
        score = 0;
        speed = 5f;
        health = 10;
        bombCounter = 2;
        spanwrate = 300;
        balls = new ArrayList<org.newdawn.slick.geom.Circle>();
        mouseBall = new org.newdawn.slick.geom.Circle(400, 300, 20);
        timePassed = 0;
        healthPickupList = new ArrayList<Circle>();
        bombPickupList = new ArrayList<Circle>();
        gameContainer.setShowFPS(false);
        healthPickupList.add(new Circle(609, 0, 10));
        bombPickupList.add(new Circle(609, 0, 10));
    }

    @Override
    public void render(GameContainer gameContainer, StateBasedGame stateBasedGame, Graphics graphics) throws SlickException {
        graphics.setColor(Color.blue);
        graphics.fill(mouseBall);
        if (!music.playing()) {
            music.play();
            music.setVolume(0.1f);
        }

        graphics.setColor(Color.cyan);

        for (Circle ball : balls) {
            graphics.fill(ball);
        }
        graphics.setColor(Color.green);
        if (!healthPickupList.isEmpty()) {
            for (Circle pickup : healthPickupList) {
                graphics.fill(pickup);
            }
        }
        graphics.setColor(Color.white);
        if (!bombPickupList.isEmpty()) {
            for (Circle bomb : bombPickupList) {
                graphics.fill(bomb);
            }
        }
        graphics.drawString("Score: " + score, 0, 0);
        graphics.drawString("Lives: " + health, 0, 15);
        graphics.drawString("Bombs: " + bombCounter, 0, 30);
        graphics.drawString("High score: " + highScore, 0, 585);
    }

    @Override
    public void update(GameContainer gameContainer, StateBasedGame stateBasedGame, int delta) throws SlickException {
        //frames passed
        timePassed += delta;

        //Random for spawn chances
        Random rnd = new Random();

        //Score system
        if (score > 700) {
            speed = 1f;
        } else if (score > 550) {
            spanwrate = 100;
        } else if (score > 450) {
            speed = 2f;
        } else if (score > 350) {
            spanwrate = 200;
        } else if (score > 150) {
            speed = 3f;
        } else if (score > 50) {
            spanwrate = 250;
            speed = 4f;
        }


        // Making the ball follow the mouse movement
        mouseBall.setCenterX(gameContainer.getInput().getMouseX());
        mouseBall.setCenterY(gameContainer.getInput().getMouseY());

        //Bomb related things
        if (gameContainer.getInput().isMousePressed(0) && bombCounter != 0) {
            bombCounter--;
            destroyAll();
        }


        //Spawning bombs
        spawnPickups(delta, bombPickupList, rnd.nextInt(300));

        //adding the bomb to the counter
        if (!bombPickupList.isEmpty()) {
            for (int pickup = bombPickupList.size() - 1; pickup > 0; pickup--) {
                if (bombPickupList.get(pickup).intersects(mouseBall)) {
                    bombPickupList.remove(pickup);
                    bombCounter++;
                }
            }
        }

        //Spawning the balls
        if (spanwrate < timePassed) {
            int xPos = rnd.nextInt(800);
            timePassed = 0;
            balls.add(new Circle(xPos, 0, 10));
        }

        //Health pickup relates things

        //Health pickup spawn
        spawnPickups(delta, healthPickupList, rnd.nextInt(180));

        //Adding 1 to health if caught
        if (!healthPickupList.isEmpty()) {
            for (int pickup = healthPickupList.size() - 1; pickup > 0; pickup--) {
                if (healthPickupList.get(pickup).intersects(mouseBall)) {
                    healthPickupList.remove(pickup);
                    health++;
                }
            }
        }

        for (Circle ball : balls) {
            ball.setCenterY(ball.getCenterY() + (delta / speed));
        }

        for (int ball = balls.size() - 1; ball >= 0; ball--) {
            Circle circle = balls.get(ball);
            if (circle.getCenterY() > 610) {
                balls.remove(ball);
                health--;
                if (health <= 0) {
                    music.setVolume(0.0f);
                    music.stop();
                    if (score > highScore){
                        highScore = score;
                    }
                    System.out.println(score);
                    stateBasedGame.getState(2).init(gameContainer, stateBasedGame);
                    stateBasedGame.enterState(2, new FadeOutTransition(), new FadeInTransition());
                }
            } else if (balls.get(ball).intersects(mouseBall)) {
                balls.remove(ball);
                score++;
            }
        }
    }

    public void destroyAll() {
        try {
            if (!balls.isEmpty()) {
                for (int ball = balls.size() - 1; ball >= 0; ball--) {
                    balls.remove(ball);
                    score++;
                }
            }
        } catch (java.lang.NullPointerException e) {
            System.out.println("null, moving on");
        }
    }

    private Random spawnPickups(int delta, ArrayList<Circle> pickupList, int chance) {
        Random rnd = new Random();
        int xPos = rnd.nextInt(800);
        if (chance == 0) {
            pickupList.add(new Circle(xPos, 0, 10));
        }

        for (Circle pickup : pickupList) {
            pickup.setCenterY((pickup.getCenterY() + (delta / speed)));
        }
        return rnd;
    }


    //Getters / Setters
    @Override
    public int getID() {
        return 1;
    }
}
