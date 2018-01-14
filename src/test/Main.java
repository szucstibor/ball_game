package test;

import org.newdawn.slick.*;
import org.newdawn.slick.state.StateBasedGame;

public class Main extends StateBasedGame {


    @Override
    public void initStatesList(GameContainer gameContainer) throws SlickException {
        this.addState(new NewGameState());
        this.addState(new GameState());
        this.addState(new GameOverState());
    }

    public Main(String title) {
        super(title);
    }



    public static void main(String[] args) throws SlickException {
        AppGameContainer appGameContainer = new AppGameContainer(new Main("Ball game"), 800, 600, false);
        appGameContainer.start();
    }
}
