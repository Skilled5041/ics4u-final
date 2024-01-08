import client.Client;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class MovingSquare extends JPanel implements KeyListener {
    public MovingSquare() {
        setFont(getFont().deriveFont(20f));
    }

    double squareX = 50;
    double squareY = 50;
    double currentSpeed = 0;
    double maxSpeed = 100;
    double acceleration = 1;

    int otherX = -1;
    int otherY = -1;

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.fillRect((int) squareX, (int) squareY, 50, 50);

        if (otherX != -1 && otherY != -1) {
            g.fillRect(otherX, otherY, 50, 50);
        }
    }
    static Client client;
    static String uuid;
    public static void main(String[] args) {
        StringBuilder uuidSb = new StringBuilder();
        for (int i = 0; i < 16; i++) {
            uuidSb.append((char) (Math.random() * 26 + 97));
        }
        uuid = uuidSb.toString();

        client = new Client();
        client.connect("0.0.0.0", 8080);

        JFrame frame = new JFrame("App");
        MovingSquare app = new MovingSquare();
        frame.add(app);
        frame.setSize(500, 500);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                client.close();
                System.exit(0);
            }
        });
        frame.addKeyListener(app);
        frame.setVisible(true);

        client.setHandler(message -> {
            String[] parts = message.split(" ");
            if (parts[0].equals(uuid)) {
                return;
            }
            app.otherX = Integer.parseInt(parts[1]);
            app.otherY = Integer.parseInt(parts[2]);
            app.repaint();
        });
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                moveLeft();
                break;
            case KeyEvent.VK_RIGHT:
                moveRight();
                break;
            case KeyEvent.VK_UP:
                moveUp();
                break;
            case KeyEvent.VK_DOWN:
                moveDown();
                break;
        }
        client.send(uuid + " " + (int) squareX + " " + (int) squareY);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        currentSpeed = 0;
    }

    public void moveLeft() {
        currentSpeed = Math.min(currentSpeed + acceleration, maxSpeed);
        squareX -= currentSpeed;
        repaint();
    }

    public void moveRight() {
        currentSpeed = Math.min(currentSpeed + acceleration, maxSpeed);
        squareX += currentSpeed;
        repaint();
    }

    public void moveUp() {
        currentSpeed = Math.min(currentSpeed + acceleration, maxSpeed);
        squareY -= currentSpeed;
        repaint();
    }

    public void moveDown() {
        currentSpeed = Math.min(currentSpeed + acceleration, maxSpeed);
        squareY += currentSpeed;
        repaint();
    }
}
