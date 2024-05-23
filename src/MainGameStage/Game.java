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

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.io.File;


public class Game {
	private Stage stage;
	private Scene splashScene;		// the splash scene
	private Scene gameScene;		// the game scene
	private StackPane root;
    private ChatApp chat;
	private Canvas canvas;			// the canvas where the animation happens
    private double bgOffsetX = 0; // Initial X offset for the background image
    private AnimationTimer animationTimer; // Declare AnimationTimer as a class member
    private ServerSocket ss;
    private int numPlayers;
    private int maxPlayers;

	public final static int WINDOW_WIDTH = 1500;
	public final static int WINDOW_HEIGHT = 800;
    

	public Game(){
        
		this.canvas = new Canvas( Game.WINDOW_WIDTH, Game.WINDOW_HEIGHT );
        this.chat = new ChatApp();
        this.root = new StackPane();
        VBox chatBox = chat.createContent();
        chatBox.setPadding(new Insets(0, 64, 0, 64));
        playStartSound();
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

    private void playStartSound() {
        Platform.runLater(() -> {
            try {
                String audioFilePath = "C:\\Users\\Quim\\Desktop\\cmsc137-project\\src\\images\\start.mp3";
                File audioFile = new File(audioFilePath);
                if (audioFile.exists()) {
                    Media sound = new Media(audioFile.toURI().toString());
                    MediaPlayer mediaPlayer = new MediaPlayer(sound);
                    mediaPlayer.play();
                } else {
                    System.err.println("Audio file not found: " + audioFilePath);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
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
        animationTimer = new AnimationTimer() {
            @Override
            public void handle(long currentNanoTime) {
                drawBackground(gc);
            }
        };
        animationTimer.start();

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

        //if (this.chat.getIsServer() == true) {
            Button b1 = new Button();

            b1.setStyle("-fx-background-color: black");
            b1.setPrefSize(220, 70);
            b1.setGraphic(newGameView);

            vbox.getChildren().addAll(b1);

            b1.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent e) {
                    // if (chat.getPlayers() == 4) {
                        setGame(stage);		// changes the scene into the game scene
                //     } else {
                //         System.out.print("ERROR: Insufficient number of players.\n");
                //     }
                }
            });
        // } else {
        //     Label infoLabel = new Label("Waiting for players...");
        //     infoLabel.setFont(Font.font("Arial", FontWeight.BOLD, 50));
        //     infoLabel.setTextFill(Color.WHITE);
        //     vbox.getChildren().addAll(infoLabel);

        // }
        

        return vbox;
    }

	void setGame(Stage stage) {
        if (animationTimer != null) {
            animationTimer.stop();
            stage.setScene( this.gameScene );

            GraphicsContext gc = this.canvas.getGraphicsContext2D();	// we will pass this gc to be able to draw on this Game's canvas
            GameTimer gameTimer = new GameTimer(stage, this.gameScene, gc, this.chat);
            gameTimer.start();			// this internally calls the handle() method of our GameTimer   
        }
	}
}