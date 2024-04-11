import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.*;

public class Stage extends JFrame implements KeyListener {
    private int tankX = 200; //initial position of the tank
    private int tankY = 200;
    private boolean bulletFired = false;
    private int bulletX;
    private int bulletY;
    private Timer timer;

    public Stage() {
        setTitle("Tank Battle Game");
        setSize(400, 400); //size of the window
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 

        //create a panel to contain the components
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawTank(g);
                if (bulletFired) {
                    drawBullet(g);
                }
            }
        };
        getContentPane().add(panel);

        addKeyListener(this); //register the key listener
        setFocusable(true); //allow the frame to receive keyboard input
        
        //create a timer to control bullet firing rate
        timer = new Timer(500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                bulletFired = false; //reset bullet flag after bullet fired
                repaint(); //redraw the panel
            }
        });
    }

    private void drawTank(Graphics g) {
        g.drawString("T", tankX, tankY); //draw the tank 
    }

    private void drawBullet(Graphics g) {
        g.drawString(".", bulletX, bulletY); //draw the bullet
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (keyCode == KeyEvent.VK_SPACE && !bulletFired) {
            // Spacebar pressed and bullet not already fired, fire bullet
            bulletFired = true;
            bulletX = tankX + 10; //move bullet, not yet working
            bulletY = tankY - 10;
            repaint(); //redraw the panel to display the bullet
            timer.start(); //start the timer to control bullet firing rate
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Stage stage = new Stage();
            stage.setVisible(true);
        });
    }
}
