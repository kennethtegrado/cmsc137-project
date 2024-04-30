/***********************************************************
* This Game class is where the root of the scene is
* instantiated. All of its children are added to the root
* so that it can be used in the interface.
*
* Found here are the splash scene, game scene, instructions
* scene, and about scene. It switches scenes depending on
* which button was clicked.
*
* It is also in this class that the GameTimer class is created
* and started.
*
* @author Quim Ramos
* @created_date 2024-04-25
*
***********************************************************/

package MainGameStage;

import com.sun.prism.paint.Color;

import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Game {
	private Stage stage;
	private Scene splashScene;		// the splash scene
	private Scene gameScene;		// the game scene
	private StackPane root;
	private Canvas canvas;			// the canvas where the animation happens
    private double bgOffsetX = 0; // Initial X offset for the background image

	public final static int WINDOW_WIDTH = 1500;
	public final static int WINDOW_HEIGHT = 800;
    

	public Game(){
		this.canvas = new Canvas( Game.WINDOW_WIDTH, Game.WINDOW_HEIGHT );
        ChatApp chat = new ChatApp();
        this.root = new StackPane();
        VBox chatBox = chat.createContent();
        chatBox.setPadding(new Insets(0, 64, 0, 64));

        StackPane.setAlignment(this.canvas, Pos.CENTER);
        StackPane.setAlignment(chatBox, Pos.BOTTOM_RIGHT);

        this.root.getChildren().addAll(this.canvas, chatBox);
        this.gameScene = new Scene( this.root );
	}

	public void setStage(Stage stage) {
		this.stage = stage;
		stage.setTitle( "Tanks 2024" );

		this.initSplash(stage);			// initializes the Splash Screen with the New Game button

		stage.setScene( this.splashScene );
        stage.setResizable(false);
		stage.show();
	}

	private void initSplash(Stage stage) {
		StackPane root = new StackPane();
        root.getChildren().addAll(this.createCanvas(), this.createVBox());
        this.splashScene = new Scene(root);
	}

	private Canvas createCanvas() {
        Canvas canvas = new Canvas(Game.WINDOW_WIDTH, Game.WINDOW_HEIGHT);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        // Use an AnimationTimer to continuously redraw the background
        new AnimationTimer() {
            @Override
            public void handle(long currentNanoTime) {
                drawBackground(gc);
            }
        }.start();

        return canvas;
    }

    // Method to draw the background image with scrolling effect
    private void drawBackground(GraphicsContext gc) {
        // Clear canvas
        gc.clearRect(0, 0, Game.WINDOW_WIDTH, Game.WINDOW_HEIGHT);

        // Draw background image
        Image bg = new Image("images/gameScreen.png");

        // Calculate the new offset based on time or player position
        // For example, you can use time to make it scroll automatically
        bgOffsetX -= 1; // Adjust the scrolling speed as needed
        
        // Draw the background image twice to create the scrolling effect
        gc.drawImage(bg, bgOffsetX, 0);
        gc.drawImage(bg, bgOffsetX + bg.getWidth(), 0);

        // If the first image is out of view, reset the offset
        if (bgOffsetX <= -bg.getWidth()) {
            bgOffsetX = 0;
        }
    }

    private VBox createVBox() {
        Image newGame = new Image("images/new.png");
        ImageView title = new ImageView("images/logo.png");
        ImageView newGameView = new ImageView(newGame);

    	VBox vbox = new VBox(title);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(120));
        vbox.setSpacing(120);

        title.setFitWidth(600);
        title.setPreserveRatio(true);
        newGameView.setFitHeight(70);
        newGameView.setFitWidth(200);
        newGameView.setPreserveRatio(true);

        Button b1 = new Button();

        b1.setStyle("-fx-background-color: black");
        b1.setPrefSize(220, 70);
        b1.setGraphic(newGameView);

        vbox.getChildren().addAll(b1);

        b1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                setGame(stage);		// changes the scene into the game scene
            }
        });

        return vbox;
    }

	void setGame(Stage stage) {
        stage.setScene( this.gameScene );

        GraphicsContext gc = this.canvas.getGraphicsContext2D();	// we will pass this gc to be able to draw on this Game's canvas
        GameTimer gameTimer = new GameTimer(this.gameScene, gc);
        gameTimer.start();			// this internally calls the handle() method of our GameTimer
	}
}