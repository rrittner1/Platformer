import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class Drawing extends JPanel {
    public static final int PAINT_MENU = 1;
    public static final int HOVER_LOAD_BUTTON = 2;
    public static final int HOVER_CREATE_BUTTON = 3;

    public static final int DEFAULT_MENU_STATE = 0;
    int paintInstructions;
    int drawingState;

    public Drawing() {
        super();
        paintInstructions = 1;
        drawingState = 0;
    }

    public void paint(int paintInstructions) {
        this.paintInstructions = paintInstructions;
        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        switch (paintInstructions) {
            case PAINT_MENU:
                clearDraw(g2);
                defaultMenuDraw(g2);
                break;
            case HOVER_LOAD_BUTTON:
                thickerButtonDraw(g2, 0);
                drawingState = -1;
                break;
            case HOVER_CREATE_BUTTON:
                thickerButtonDraw(g2, 1);
                drawingState = -1;
                break;
        }
    }

    public void clearDraw(Graphics2D g2) {
        g2.clearRect(0, 0, 600, 600);
    }

    public void defaultMenuDraw(Graphics2D g2) {
        g2.setFont(new Font("This doesn't change the font", Font.PLAIN, 50));
        g2.drawString("Platformer", 175, 140);

        g2.setFont(new Font("", Font.PLAIN, 20));
        g2.drawString("Load Level", 247, 250);
        g2.fillRect(230, 225, 135, 2);
        g2.fillRect(230, 260, 135, 2);
        g2.fillRect(230, 225, 2, 35);
        g2.fillRect(363, 225, 2, 35);

        g2.drawString("Create Level", 241, 300);
        g2.fillRect(230, 275, 135, 2);
        g2.fillRect(230, 310, 135, 2);
        g2.fillRect(230, 275, 2, 35);
        g2.fillRect(363, 275, 2, 35);
    }

    public void thickerButtonDraw(Graphics2D g2, int button) {
        g2.fillRect(230, 225 + 50 * button, 135, 4);
        g2.fillRect(230, 258 + 50 * button, 135, 4);
        g2.fillRect(230, 225 + 50 * button, 4, 35);
        g2.fillRect(361, 225 + 50 * button, 4, 35);
    }
}
