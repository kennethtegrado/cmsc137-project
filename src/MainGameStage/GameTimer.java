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
	private ArrayList<Bullet> bullet;
	private ArrayList<Wall> wall;
	private ArrayList<Bush> bush;
	private ArrayList<Water> water;
	private ArrayList<Metal> metal;
	private ArrayList<Steel> steel;
	private String currentFacing;
	private ChatApp chat;
	private Scene scene;

	private static boolean goLeft;
	private static boolean goRight;
	private static boolean goUp;
	private static boolean goDown;
	private static boolean fireBullet;

	public final static int START_MAP_WIDTH = 55;
	public final static int START_MAP_HEIGHT = 50;
	public final static int END_MAP_WIDTH = 1165;
	public final static int END_MAP_HEIGHT = 740;

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
		this.bullet = new ArrayList<Bullet>();
		this.wall = new ArrayList<Wall>();
		this.bush = new ArrayList<Bush>();
		this.water = new ArrayList<Water>();
		this.metal = new ArrayList<Metal>();
		this.steel = new ArrayList<Steel>();
		this.chat = new ChatApp();
		this.currentFacing = "up";
		this.prepareActionHandlers();
		this.initializeMap();
		//this.chat.createContent();
	}

	@Override
	public void handle(long currentNanoTime) {
		this.gc.drawImage(GameTimer.GAME_BG, 0, 0);
		this.player.render(this.gc);

		for (Steel steel: this.steel) {
			steel.render(this.gc);
		}
		
		this.movePlayer();
		//this.renderMap();
		//this.checkWaterCollision();
		//this.checkWallCollision();
		//this.checkMetalCollision();
		//this.checkSteelCollision();
		for (Bullet fire: this.bullet) {
			this.moveBullet(fire);
		}
	}

	void initializeMap() {
		boolean isAlternateX = true;
		boolean isAlternateY = true;
		int a = 0;
		for (int i=GameTimer.START_MAP_WIDTH; i+42 < GameTimer.END_MAP_WIDTH; i = i + 42) {
			if (i == GameTimer.START_MAP_HEIGHT) {
				for (int j=GameTimer.START_MAP_WIDTH; j+42 < GameTimer.END_MAP_HEIGHT; j = j + 42) {
					Steel newSteel = new Steel(i, j);
					this.steel.add(newSteel);
				}
				continue;
			}
			if (isAlternateX) {
				for (int j=92; j+42 < GameTimer.END_MAP_HEIGHT; j = j + 42) {
					if (isAlternateY) {
						Wall newWall = new Wall(i, j);
						this.wall.add(newWall);
					} else {
						if (a == 1) {
							Water newWater = new Water(i, j);
							this.water.add(newWater);
							a = 0;
						} else {
							Metal newMetal = new Metal(i, j);
							this.metal.add(newMetal);
							a = 1;
						}
					}
					isAlternateY = !isAlternateY;
				}
			} else {
				for (int j=92; j < GameTimer.END_MAP_HEIGHT; j = j + 42) {
					Bush newBush = new Bush(i, j);
					this.bush.add(newBush);
				}
			}

			Steel newSteel = new Steel(i, GameTimer.START_MAP_WIDTH);
			this.steel.add(newSteel);
			isAlternateX = !isAlternateX;
		}
	}

	void renderMap() {
		for (int i = 0; i < this.wall.size(); i++) {
			Wall wall = this.wall.get(i);
			if (wall.getHealth() > 0) {
				wall.render(this.gc);
			} else {
				this.wall.remove(i);
			}
		}

		for (Bush bush: this.bush) {
			bush.render(this.gc);
		}

		for (Water water: this.water) {
			water.render(this.gc);
		}

		for (Metal metal: this.metal) {
			metal.render(this.gc);
		}
	}

	void checkWallCollision() {
		for (Wall wall: this.wall) {
			if (currentFacing == "up") {
				boolean hasCollisionX = (this.player.getXPos() >= wall.getXPos() 
																&& this.player.getXPos() <= wall.getXPos()+40)
																|| (this.player.getXPos()+40 >= wall.getXPos()
																&& this.player.getXPos()+40 <= wall.getXPos()+40);
				boolean hasCollisionY = (this.player.getYPos() <= wall.getYPos()+40
																&& this.player.getYPos()+40 > wall.getYPos()+40);
				if (hasCollisionY && hasCollisionX) {
					GameTimer.goUp = false;
				}
			} else if (currentFacing == "down") {
				boolean hasCollisionX = (this.player.getXPos() > wall.getXPos() 
																&& this.player.getXPos() < wall.getXPos()+40)
																|| (this.player.getXPos()+40 > wall.getXPos()
																&& this.player.getXPos()+40 < wall.getXPos()+40);
				boolean hasCollisionY = (this.player.getYPos()+40 >= wall.getYPos()
																&& this.player.getYPos() < wall.getYPos());
				if (hasCollisionY && hasCollisionX) {
					GameTimer.goDown = false;
				}
			} else if (currentFacing == "left") {
				boolean hasCollisionY = (this.player.getYPos() >= wall.getYPos() 
																&& this.player.getYPos() <= wall.getYPos()+40)
																|| (this.player.getYPos()+40 >= wall.getYPos()
																&& this.player.getYPos()+40 <= wall.getYPos()+40);
				boolean hasCollisionX = (this.player.getXPos() <= wall.getXPos()+40
																&& this.player.getXPos()+40 > wall.getXPos()+40);
				if (hasCollisionX && hasCollisionY) {
					GameTimer.goLeft = false;
				}
			} else if (currentFacing == "right") {
				boolean hasCollisionY = (this.player.getYPos() > wall.getYPos() 
																&& this.player.getYPos() < wall.getYPos()+40)
																|| (this.player.getYPos()+40 > wall.getYPos()
																&& this.player.getYPos()+40 < wall.getYPos()+40);
				boolean hasCollisionX = (this.player.getXPos()+40 >= wall.getXPos()
																&& this.player.getXPos() < wall.getXPos());
				if (hasCollisionY && hasCollisionX) {
					GameTimer.goRight = false;
				}
			}

			for (int i = 0; i < this.bullet.size(); i++) {
				Bullet bullet = this.bullet.get(i);
				if (wall.collidesWith(bullet)) {
					this.bullet.remove(i);
					wall.setHealth();
					break;
				}
			}
		}
	}

	void checkWaterCollision() {
		for (Water water: this.water) {
			if (currentFacing == "up") {
				boolean hasCollisionX = (this.player.getXPos() >= water.getXPos() 
																&& this.player.getXPos() <= water.getXPos()+40)
																|| (this.player.getXPos()+40 >= water.getXPos()
																&& this.player.getXPos()+40 <= water.getXPos()+40);
				boolean hasCollisionY = (this.player.getYPos() <= water.getYPos()+40
																&& this.player.getYPos()+40 > water.getYPos()+40);
				if (hasCollisionY && hasCollisionX) {
					GameTimer.goUp = false;
				}
			} else if (currentFacing == "down") {
				boolean hasCollisionX = (this.player.getXPos() > water.getXPos() 
																&& this.player.getXPos() < water.getXPos()+40)
																|| (this.player.getXPos()+40 > water.getXPos()
																&& this.player.getXPos()+40 < water.getXPos()+40);
				boolean hasCollisionY = (this.player.getYPos()+40 >= water.getYPos()
																&& this.player.getYPos() < water.getYPos());
				if (hasCollisionY && hasCollisionX) {
					GameTimer.goDown = false;
				}
			} else if (currentFacing == "left") {
				boolean hasCollisionY = (this.player.getYPos() >= water.getYPos() 
																&& this.player.getYPos() <= water.getYPos()+40)
																|| (this.player.getYPos()+40 >= water.getYPos()
																&& this.player.getYPos()+40 <= water.getYPos()+40);
				boolean hasCollisionX = (this.player.getXPos() <= water.getXPos()+40
																&& this.player.getXPos()+40 > water.getXPos()+40);
				if (hasCollisionX && hasCollisionY) {
					GameTimer.goLeft = false;
				}
			} else if (currentFacing == "right") {
				boolean hasCollisionY = (this.player.getYPos() > water.getYPos() 
																&& this.player.getYPos() < water.getYPos()+40)
																|| (this.player.getYPos()+40 > water.getYPos()
																&& this.player.getYPos()+40 < water.getYPos()+40);
				boolean hasCollisionX = (this.player.getXPos()+40 >= water.getXPos()
																&& this.player.getXPos() < water.getXPos());
				if (hasCollisionY && hasCollisionX) {
					GameTimer.goRight = false;
				}
			}
		}
	}

	void checkMetalCollision() {
		for (Metal metal: this.metal) {
			if (currentFacing == "up") {
				boolean hasCollisionX = (this.player.getXPos() >= metal.getXPos() 
																&& this.player.getXPos() <= metal.getXPos()+40)
																|| (this.player.getXPos()+40 >= metal.getXPos()
																&& this.player.getXPos()+40 <= metal.getXPos()+40);
				boolean hasCollisionY = (this.player.getYPos() <= metal.getYPos()+40
																&& this.player.getYPos()+40 > metal.getYPos()+40);
				if (hasCollisionY && hasCollisionX) {
					GameTimer.goUp = false;
				}
			} else if (currentFacing == "down") {
				boolean hasCollisionX = (this.player.getXPos() > metal.getXPos() 
																&& this.player.getXPos() < metal.getXPos()+40)
																|| (this.player.getXPos()+40 > metal.getXPos()
																&& this.player.getXPos()+40 < metal.getXPos()+40);
				boolean hasCollisionY = (this.player.getYPos()+40 >= metal.getYPos()
																&& this.player.getYPos() < metal.getYPos());
				if (hasCollisionY && hasCollisionX) {
					GameTimer.goDown = false;
				}
			} else if (currentFacing == "left") {
				boolean hasCollisionY = (this.player.getYPos() >= metal.getYPos() 
																&& this.player.getYPos() <= metal.getYPos()+40)
																|| (this.player.getYPos()+40 >= metal.getYPos()
																&& this.player.getYPos()+40 <= metal.getYPos()+40);
				boolean hasCollisionX = (this.player.getXPos() <= metal.getXPos()+40
																&& this.player.getXPos()+40 > metal.getXPos()+40);
				if (hasCollisionX && hasCollisionY) {
					GameTimer.goLeft = false;
				}
			} else if (currentFacing == "right") {
				boolean hasCollisionY = (this.player.getYPos() > metal.getYPos() 
																&& this.player.getYPos() < metal.getYPos()+40)
																|| (this.player.getYPos()+40 > metal.getYPos()
																&& this.player.getYPos()+40 < metal.getYPos()+40);
				boolean hasCollisionX = (this.player.getXPos()+40 >= metal.getXPos()
																&& this.player.getXPos() < metal.getXPos());
				if (hasCollisionY && hasCollisionX) {
					GameTimer.goRight = false;
				}
			}

			for (int i = 0; i < this.bullet.size(); i++) {
				Bullet bullet = this.bullet.get(i);
				if (metal.collidesWith(bullet)) {
					this.bullet.remove(i);
					break;
				}
			}
		}
	}

	void checkSteelCollision() {
		for (Steel steel: this.steel) {
			if (steel.collidesWith(this.player)) {
				this.player.setSpeed(1);
				break;
			} else {
				this.player.setSpeed(0.5);
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
								}else if (code.equals("SLASH")) {
									System.out.println("yes");
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
			if (this.player.getXPos() <= GameTimer.END_MAP_WIDTH && this.player.getXPos() > GameTimer.START_MAP_WIDTH) {
				this.player.setXPos(this.player.getXPos() - this.player.getSpeed());
			}
			this.player.loadImage(left);
			currentFacing = "left";
		} else if (GameTimer.goRight) {
				if (this.player.getXPos()+40 < GameTimer.END_MAP_WIDTH && this.player.getXPos() >= GameTimer.START_MAP_WIDTH) {
					this.player.setXPos(this.player.getXPos() + this.player.getSpeed());
				}
			this.player.loadImage(right);
			currentFacing = "right";
		} else if (GameTimer.goUp) {
				if (this.player.getYPos() <= GameTimer.END_MAP_HEIGHT && this.player.getYPos() > GameTimer.START_MAP_HEIGHT) {
					this.player.setYPos(this.player.getYPos() - this.player.getSpeed());
				}
			this.player.loadImage(up);
			currentFacing = "up";
		} else if (GameTimer.goDown) {
				if (this.player.getYPos()+40 < GameTimer.END_MAP_HEIGHT && this.player.getYPos() >= GameTimer.START_MAP_HEIGHT) {
					this.player.setYPos(this.player.getYPos() + this.player.getSpeed());
				}
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
			if (fire.getYPos() > GameTimer.START_MAP_HEIGHT) {
				fire.setYPos(fire.getYPos()-5);
			} else {
				fire.setVisible(false);
			}
		} else if (fire.getDirection() == "down") {
			if (fire.getYPos() < GameTimer.END_MAP_HEIGHT) {
				fire.setYPos(fire.getYPos()+5);
			} else {
				fire.setVisible(false);
			}
		} else if (fire.getDirection() == "left") {
			if (fire.getXPos() > GameTimer.START_MAP_WIDTH) {
				fire.setXPos(fire.getXPos()-5);
			} else {
				fire.setVisible(false);
			}
		} else if (fire.getDirection() == "right") {
			if (fire.getXPos() < GameTimer.END_MAP_WIDTH) {
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