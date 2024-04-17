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
    private Image backgroundImage = Toolkit.getDefaultToolkit().getImage("sample-stage.png");
    private Image up = Toolkit.getDefaultToolkit().getImage("tank-up.png").getScaledInstance(30, 30, DO_NOTHING_ON_CLOSE);
    private Image down = Toolkit.getDefaultToolkit().getImage("tank-down.png").getScaledInstance(30, 30, DO_NOTHING_ON_CLOSE);
    private Image left = Toolkit.getDefaultToolkit().getImage("tank-left.png").getScaledInstance(30, 30, DO_NOTHING_ON_CLOSE);
    private Image right = Toolkit.getDefaultToolkit().getImage("tank-right.png").getScaledInstance(30, 30, DO_NOTHING_ON_CLOSE);
    private Image position = up;
    
    public Stage() {
        setTitle("Tank Battle Game");
        setSize(1215, 750); //size of the window
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 

        //create a panel to contain the components
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(backgroundImage, 0, 0, this);
                drawTank(g);
                if (bulletFired) {
                    drawBullet(g);
                }
            }
        };
        panel.setBackground(Color.BLACK);
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
        g.drawImage(position, tankX, tankY, this); //draw the tank 
    }

    private void drawBullet(Graphics g) {
        g.drawString("|", bulletX, bulletY); //draw the bullet
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (keyCode == KeyEvent.VK_SPACE && !bulletFired) {
            // Spacebar pressed and bullet not already fired, fire bullet
            bulletFired = true;
            bulletX = tankX + 2; //move bullet, not yet working
            bulletY = tankY - 15;
            repaint(); //redraw the panel to display the bullet
            timer.start(); //start the timer to control bullet firing rate
        } else if (keyCode == KeyEvent.VK_W) {
            if (tankY - 1 > 25) {
                tankY = tankY - 1;
            }
            position = up;
            repaint();
        } else if (keyCode == KeyEvent.VK_S) {
            if (tankY + 1 < 645) {
                tankY = tankY + 1;
            }
            position = down;
            repaint();
        } else if (keyCode == KeyEvent.VK_A) {
            if (tankX - 1 > 25) {
                tankX = tankX - 1;
            }
            position = left;
            repaint();
        } else if (keyCode == KeyEvent.VK_D) {
            if (tankX + 1 < 795) {
                tankX = tankX + 1;
            }
            position = right;
            repaint();
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
