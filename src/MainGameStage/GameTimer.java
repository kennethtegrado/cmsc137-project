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

import javafx.animation.Animation;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
//import javafx.scene.text.Font;
//import javafx.scene.text.FontWeight;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import java.util.ArrayList;
//import java.util.Random;

class GameTimer extends AnimationTimer{
	private GraphicsContext gc;
	private Player player;
	private Player player1;
	private ArrayList<Bullet> bullet;
	private ArrayList<Wall> wall;
	private String currentFacing;
	private Scene scene;

	private static boolean goLeft;
	private static boolean goRight;
	private static boolean goUp;
	private static boolean goDown;
	private static boolean fireBullet;

	public final static int SHOOT_DELAY = 1;

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
		this.player1 = new Player("yey");
		this.bullet = new ArrayList<Bullet>();
		this.wall = new ArrayList<Wall>();
		this.currentFacing = "up";
		this.prepareActionHandlers();
		this.player1.setXPos(600);
		this.player1.setYPos(100);
		this.initializeMap();
	}

	@Override
	public void handle(long currentNanoTime) {
		this.gc.drawImage(GameTimer.GAME_BG, 0, 0);
		this.renderMap();
		this.player.render(this.gc);
		this.player1.render(this.gc);
		this.movePlayer();
		this.checkWallCollision();
		for (Bullet fire: this.bullet) {
			this.moveBullet(fire);
		}
	}

	void initializeMap() {
		boolean isAlternateX = true;
		boolean isAlternateY = true;
		for (int i=100; i < 1090; i = i + 45) {
			if (isAlternateX) {
				for (int j=92; j < 708; j = j + 45) {
					if (isAlternateY) {
						Wall newWall = new Wall(i, j);
						this.wall.add(newWall);
					}
					isAlternateY = !isAlternateY;
				}
			}
			isAlternateX = !isAlternateX;
		}
	}

	void renderMap() {
		for (Wall wall: this.wall) {
			wall.render(this.gc);
		}
	}

	void checkWallCollision() {
		for (Wall wall: this.wall) {
			if (wall.collidesWith(this.player)) {
				if (currentFacing == "up") {
					GameTimer.goUp = false;
					//this.player.setYPos(this.player.getYPos()-1);
				} else if (currentFacing == "down") {
					GameTimer.goDown = false;
					//this.player.setYPos(this.player.getYPos()+1);
				} else if (currentFacing == "left") {
					GameTimer.goLeft = false;
					//this.player.setYPos(this.player.getXPos()-1);
				} else if (currentFacing == "right") {
					GameTimer.goRight = false;
					//this.player.setYPos(this.player.getXPos()+1);
				}
			}
		}
	}

	private void prepareActionHandlers() {	// method for the player controls
			Duration firingInterval = Duration.millis(500);
			Timeline firing = new Timeline(
				new KeyFrame(Duration.ZERO, event -> fireBullet()),
				new KeyFrame(firingInterval));
			firing.setCycleCount(Animation.INDEFINITE);

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
                }else if(code.equals("SPACE") && firing.getStatus() != Animation.Status.RUNNING) {
									GameTimer.fireBullet = true;
									firing.playFromStart();
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
                }else if(code.equals("SPACE")) {
									GameTimer.fireBullet = false;
									firing.stop();
								}
            }
        });
    }

	private void movePlayer() {		// method for controlling the player
		if (GameTimer.goLeft) {
			//if (!this.player.collidesWith(this.player1)) {
				if (this.player.getXPos() <= 1088 && this.player.getXPos() > 60) {
					this.player.setXPos(this.player.getXPos() - 0.5);
				}
			// } else {
			// 	while (this.player.collidesWith(this.player1)) {
			// 		this.player.setXPos(this.player.getXPos() + 1);
			// 	}
			// }
			this.player.loadImage(left);
			currentFacing = "left";
		} else if (GameTimer.goRight) {
			//if (!this.player.collidesWith(this.player1)) {
				if (this.player.getXPos() < 1088 && this.player.getXPos() >= 60) {
					this.player.setXPos(this.player.getXPos() + 0.5);
				}
			// } else {
			// 	while (this.player.collidesWith(this.player1)) {
			// 		this.player.setXPos(this.player.getXPos() - 1);
			// 		this.player.setXPos(this.player.getXPos() - 1);
			// 	}
			// }
			this.player.loadImage(right);
			currentFacing = "right";
		} else if (GameTimer.goUp) {
			//if (!this.player.collidesWith(this.player1)) {
				if (this.player.getYPos() <= 708 && this.player.getYPos() > 52) {
					this.player.setYPos(this.player.getYPos() - 0.5);
				}
			// } else {
			// 	while (this.player.collidesWith(this.player1)) {
			// 		this.player.setYPos(this.player.getYPos() + 1);
			// 	}
			// }
			this.player.loadImage(up);
			currentFacing = "up";
		} else if (GameTimer.goDown) {
			//if (!this.player.collidesWith(this.player1)) {
				if (this.player.getYPos() < 708 && this.player.getYPos() >= 52) {
					this.player.setYPos(this.player.getYPos() + 0.5);
				}
			// } else {
			// 	while (this.player.collidesWith(this.player1)) {
			// 		this.player.setYPos(this.player.getYPos() - 1);
			// 	}
			// }
			this.player.loadImage(down);
			currentFacing = "down";
		}
		this.player.render(this.gc);
	}

	private void fireBullet() {
		if (GameTimer.fireBullet) {
			Bullet fire = new Bullet(0, 0, "");
			if (currentFacing == "up") {
				fire.setDirection(currentFacing);
				fire.setVisible(true);
				fire.setXPos(this.player.getXPos()+17);
				fire.setYPos(this.player.getYPos()-10);
			} else if (currentFacing == "down") {
				fire.setDirection(currentFacing);
				fire.setVisible(true);
				fire.setXPos(this.player.getXPos()+18);
				fire.setYPos(this.player.getYPos()+40);
			} else if (currentFacing == "left") {
				fire.setDirection(currentFacing);
				fire.setVisible(true);
				fire.setXPos(this.player.getXPos()-10);
				fire.setYPos(this.player.getYPos()+17);
			} else if (currentFacing == "right") {
				fire.setDirection(currentFacing);
				fire.setVisible(true);
				fire.setXPos(this.player.getXPos()+40);
				fire.setYPos(this.player.getYPos()+17);
			}
			this.bullet.add(fire);
			GameTimer.fireBullet = false;
		}
	}

	private void moveBullet(Bullet fire) {
		if (fire.getDirection() == "up") {
			if (fire.getYPos() > 52) {
				fire.setYPos(fire.getYPos()-5);
			} else {
				fire.setVisible(false);
			}
		} else if (fire.getDirection() == "down") {
			if (fire.getYPos() < 750) {
				fire.setYPos(fire.getYPos()+5);
			} else {
				fire.setVisible(false);
			}
		} else if (fire.getDirection() == "left") {
			if (fire.getXPos() > 60) {
				fire.setXPos(fire.getXPos()-5);
			} else {
				fire.setVisible(false);
			}
		} else if (fire.getDirection() == "right") {
			if (fire.getXPos() < 1130) {
				fire.setXPos(fire.getXPos()+5);
			} else {
				fire.setVisible(false);
			}
		}

		if (fire.getVisible()) {
			fire.render(this.gc);
		}
	}
}