package test;

import org.newdawn.slick.*;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

public class GameOverState extends BasicGameState {
    private Image gameOver;
    private Music music;
    private int timePassed;
    private int musicPlayedXTimes;


    @Override
    public void init(GameContainer gameContainer, StateBasedGame stateBasedGame) throws SlickException {
        music = new Music("resources/game_over.ogg");
        gameOver = new Image("resources/game_over.jpg");
        timePassed = 0;
        musicPlayedXTimes = 0;
    }

    @Override
    public void render(GameContainer gameContainer, StateBasedGame stateBasedGame, Graphics graphics) throws SlickException {
        graphics.drawImage(gameOver, 0, 0);
            music.setVolume(0.2f);
            if (!music.playing() && musicPlayedXTimes == 0){
                music.play();
                musicPlayedXTimes++;
            }
    }

    @Override
    public void update(GameContainer gameContainer, StateBasedGame stateBasedGame, int delta) throws SlickException {
        timePassed += delta;
        if (timePassed >= 16000 || gameContainer.getInput().isMousePressed(0)){
            music.setVolume(0.0f);
            stateBasedGame.enterState(0, new FadeOutTransition(), new FadeInTransition());
            timePassed = 0;
            stateBasedGame.getState(1).init(gameContainer, stateBasedGame);
        }
    }

    //Getters / Setters
    @Override
    public int getID() {
        return 2;
    }
}
