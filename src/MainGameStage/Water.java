package MainGameStage;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Water {
  private double xPos;
	private double yPos;
  private Image water = new Image("images/water.png", 40, 40, false, false);

	Water (double xPos, double yPos){
		this.xPos = xPos;
		this.yPos = yPos;
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

  void loadImage(Image newImage) {
		this.water = newImage;
	}

  void render(GraphicsContext gc) {
		gc.drawImage(this.water, this.xPos, this.yPos);
	}
}
