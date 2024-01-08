import client.Client;

import java.awt.Graphics;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.LinkedList;
import java.util.ListIterator;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class MessagingApp extends JPanel {
    LinkedList<String> messages = new LinkedList<>();

    public MessagingApp() {
        setFont(getFont().deriveFont(20f));
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (messages.size() > 10) {
            messages.removeFirst();
        }

        ListIterator<String> iterator = messages.listIterator();
        int y = 150;
        while (iterator.hasNext()) {
            String message = iterator.next();
            g.drawString(message, 50, y);
            y += 30;
        }
    }

    public static void main(String[] args) {
        Client client = new Client();
        client.connect("0.0.0.0", 8080);

        JFrame frame = new JFrame("Messaging App");
        MessagingApp app = new MessagingApp();
        JTextField input = new JTextField();
        input.addActionListener(e -> {
            String message = input.getText();
            client.send(message);
            input.setText("");
        });
        frame.add(input);
        input.setBounds(50, 50, 400, 50);

        frame.add(app);
        frame.setSize(500, 500);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                client.close();
                System.exit(0);
            }
        });
        frame.setVisible(true);

        client.setHandler(message -> {
            app.messages.add(message);
            app.repaint();
        });
    }
}
