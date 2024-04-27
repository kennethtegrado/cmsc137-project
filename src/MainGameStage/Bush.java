package MainGameStage;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Bush {
  private double xPos;
	private double yPos;
  private Image bush = new Image("images/bush.png", 40, 40, false, false);
  private boolean visible;

	Bush (double xPos, double yPos){
		this.xPos = xPos;
		this.yPos = yPos;
    this.visible = true;
    this.loadImage(this.bush);
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

  void loadImage(Image newImage) {
		this.bush = newImage;
	}

  void render(GraphicsContext gc) {
		gc.drawImage(this.bush, this.xPos, this.yPos);
	}
}
