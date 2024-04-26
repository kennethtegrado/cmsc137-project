/***********************************************************
* This GameTimer class is a subclass of the AnimationTimer.
* This is where most of the activity in the game is created.
* It includes spawning, moving and rendering of all the
* entities.
*
* @author Quim Ramos
* @created_date 2022-12-22
*
***********************************************************/

package MainGameStage;

import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
//import javafx.scene.text.Font;
//import javafx.scene.text.FontWeight;
import javafx.scene.paint.Color;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
//import java.util.ArrayList;
//import java.util.Random;

class GameTimer extends AnimationTimer{
	private GraphicsContext gc;
	private Player player;
	private Scene scene;

	private static boolean goLeft;
	private static boolean goRight;
	private static boolean goUp;
	private static boolean goDown;

	public final static int MAP_WIDTH = 2400;
	public final static int MAP_HEIGHT = 2400;

	private final static Image GAME_BG = new Image("images/gameBg.png");	
	private Image up = new Image("images/tank-up.png", 40, 40, false, false);
	private Image left = new Image("images/tank-left.png", 40, 40, false, false);
	private Image down = new Image("images/tank-down.png", 40, 40, false, false);
	private Image right = new Image("images/tank-right.png", 40, 40, false, false);

	GameTimer(Scene scene, GraphicsContext gc) {
		this.gc = gc;
		this.scene = scene;
		this.gc.drawImage(GameTimer.GAME_BG, 0, 0);
    	this.scene.setFill(Color.BLACK);
    	this.player = new Player("Tank");
    	this.prepareActionHandlers();
	}

	@Override
	public void handle(long currentNanoTime) {
		this.gc.drawImage(GameTimer.GAME_BG, 0, 0);
		this.player.render(this.gc);
		this.movePlayer();
	}

	private void prepareActionHandlers() {	// method for the player controls
    	this.scene.setOnKeyPressed(new EventHandler<KeyEvent>()
        {
            public void handle(KeyEvent e)
            {
                String code = e.getCode().toString();
                if(code.equals("A")) {
                	GameTimer.goLeft = true;
                }else if(code.equals("D")) {
                	GameTimer.goRight = true;
                }else if(code.equals("W")) {
                	GameTimer.goUp = true;
                }else if(code.equals("S")) {
                	GameTimer.goDown = true;
                }

            }
        });

    	this.scene.setOnKeyReleased(new EventHandler<KeyEvent>()
        {
            public void handle(KeyEvent e)
            {
                String code = e.getCode().toString();
                if(code.equals("A")) {
                	GameTimer.goLeft = false;
                }else if(code.equals("D")) {
                	GameTimer.goRight = false;
                }else if(code.equals("W")) {
                	GameTimer.goUp = false;
                }else if(code.equals("S")) {
                	GameTimer.goDown = false;
                }
            }
        });
    }

	private void movePlayer() {		// method for controlling the player
		if (GameTimer.goLeft) {
			if (this.player.getXPos() <= 1088 && this.player.getXPos() > 60) {
				this.player.setXPos(this.player.getXPos() - 2);
			}
			this.player.loadImage(left);
		} else if (GameTimer.goRight) {
			if (this.player.getXPos() < 1088 && this.player.getXPos() >= 60) {
				this.player.setXPos(this.player.getXPos() + 2);
			}
			this.player.loadImage(right);
		} else if (GameTimer.goUp) {
			if (this.player.getYPos() <= 708 && this.player.getYPos() > 52) {
				this.player.setYPos(this.player.getYPos() - 2);
			}
			this.player.loadImage(up);
		} else if (GameTimer.goDown) {
			if (this.player.getYPos() < 708 && this.player.getYPos() >= 52) {
				this.player.setYPos(this.player.getYPos() + 2);
			}
			this.player.loadImage(down);
		}
		this.player.render(this.gc);
	}
}