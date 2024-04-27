/***********************************************************
* This Player class is a subclass of Blobs. It instantiates
* the player blob. Most of the methods in this class are
* getters and setters for the other classes to use.
*
* @author Quim Ramos
* @created_date 2022-12-22
*
***********************************************************/

package MainGameStage;

import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

class Player {
	private String name;
	private double xPos;
	private double yPos;
	private Image player = new Image("images/tank-up.png", 40, 40, false, false);
	private final static double INITIAL_X = 60;
	private final static double INITIAL_Y = 52;

	Player (String name){
		this.name = name;
		this.xPos = INITIAL_X;
		this.yPos = INITIAL_Y;
	}

	String getName(){
		return this.name;
	}

	double getXPos() {
		return this.xPos;
	}

	double getYPos() {
		return this.yPos;
	}

	Image getPlayer() {
		return this.player;
	}

	void setXPos(double newXPos) {
		this.xPos = newXPos;
	}
	
	void setYPos(double newYPos) {
		this.yPos = newYPos;
	}

	void loadImage(Image newImage) {
		this.player = newImage;
	}

	void render(GraphicsContext gc) {
		gc.drawImage(this.player, this.xPos, this.yPos);
	}

	public Rectangle2D getBounds(){
		return new Rectangle2D(this.xPos - this.player.getHeight()/2, this.yPos - this.player.getHeight()/2, this.player.getWidth(), this.player.getHeight());
	}

	protected boolean collidesWith(Player rect2)	{
		Rectangle2D rectangle1 = this.getBounds();
		Rectangle2D rectangle2 = rect2.getBounds();

		return rectangle1.intersects(rectangle2);
	}
}