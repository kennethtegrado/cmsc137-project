package MainGameStage;

import com.sun.glass.events.KeyEvent;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class ChatApp extends Application {

    private boolean isServer = false;

    private TextArea messages = new TextArea();
    private TextField input = new TextField();
    private NetworkConnection connection = isServer ? createServer() : createClient();

    public VBox createContent() {
        messages.setFont(Font.font(14));
        messages.setPrefHeight(360);
        messages.setWrapText(true);
        messages.setStyle("-fx-control-inner-background: #000000; -fx-border-style: none;");
        messages.setEditable(false);

        input.setPromptText("Press \'/\' to open the chat");
        input.setStyle("-fx-control-inner-background: #343434; -fx-prompt-text-fill: #aeaeae");
        input.setOnAction(event -> {
            String message = isServer ? "Server: " : "Client: ";
            message += input.getText();
            input.clear();

            messages.appendText(message + "\n");

            DataPacket packet = new DataPacket(
                    new Encryptor().enc(message.getBytes())
            );

            try {
                connection.send(packet);
            }
            catch (Exception e) {
                messages.appendText("Failed to send\n");
            }
        });

        // input.setOnKeyPressed(event -> {
        //     if (event.getCode() == KeyCode.ENTER) {
        //         // Request focus on another node to unfocus the TextField
        //         input.getParent().requestFocus();
        //     }
        // });

        messages.setFocusTraversable(false);
        input.setFocusTraversable(false);

        VBox root = new VBox(5, messages, input);
        // Set minimum width and height
        root.setMinWidth(100);
        root.setMinHeight(75);

        // Set maximum width and height
        root.setMaxWidth(362);
        root.setMaxHeight(460);
        return root;
    }

    public TextField getTextFieldNode() {
        return this.input;
    }

    @Override
    public void init() throws Exception {
        connection.startConnection();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setScene(new Scene(createContent()));
        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        connection.closeConnection();
    }

    private Server createServer() {
        return new Server(55555, data -> {
            DataPacket packet = (DataPacket) data;
            byte[] original = new Encryptor().dec(packet.getRawBytes());

            Platform.runLater(() -> {
                messages.appendText(new String(original) + "\n");
            });
        });
    }

    private Client createClient() {
        return new Client("127.0.0.1", 55555, data -> {
            DataPacket packet = (DataPacket) data;
            byte[] original = new Encryptor().dec(packet.getRawBytes());

            Platform.runLater(() -> {
                messages.appendText(new String(original) + "\n");
            });
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
