import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Stage stage = new Stage();
            stage.setVisible(true);
        });
    }
}
