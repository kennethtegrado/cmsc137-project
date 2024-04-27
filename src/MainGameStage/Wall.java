package MainGameStage;

import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Wall {
  private double xPos;
	private double yPos;
	private Image wall = new Image("images/wall-brick.png", 40, 40, false, false);
  private boolean visible;

	Wall (double xPos, double yPos){
		this.xPos = xPos;
		this.yPos = yPos;
    this.visible = true;
	}

  double getXPos() {
		return this.xPos;
	}

	double getYPos() {
		return this.yPos;
	}

  void setXPos(double newXPos) {
		this.xPos = newXPos;
	}
	
	void setYPos(double newYPos) {
		this.yPos = newYPos;
	}

  boolean getVisible() {
    return this.visible;
  }

  void setVisible(boolean newVisible) {
    this.visible = newVisible;
  }

  void render(GraphicsContext gc) {
		gc.drawImage(this.wall, this.xPos, this.yPos);
	}

  private Rectangle2D getBounds(){
		return new Rectangle2D(this.xPos - this.wall.getHeight()/2, this.yPos - this.wall.getHeight()/2, this.wall.getWidth(), this.wall.getHeight());
	}

	protected boolean collidesWith(Player rect2)	{
		Rectangle2D rectangle1 = this.getBounds();
		Rectangle2D rectangle2 = rect2.getBounds();
		
		return rectangle1.intersects(rectangle2);
	}
}
