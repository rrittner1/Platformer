import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class Drawing extends JPanel {
    //paintInstructions
    public static final int DEFAULT_MENU = 1;
    public static final int HOVER_LOAD_BUTTON = 2;
    public static final int HOVER_CREATE_BUTTON = 3;
    public static final int LOAD_MENU = 4;
    public static final int CREATE_MENU = 5;
    public static final int HOVER_CREATE_BACK = 6;
    public static final int HOVER_LOAD_BACK = 7;
    public static final int HOVER_CREATE_INSTRUCTIONS = 8;
    public static final int HOVER_CREATE_DRAW = 9;
    //drawingStates
    public static final int DEFAULT_MENU_STATE = 0;
    public static final int DEFAULT_HOVER_STATE = -1;
    public static final int CREATE_MENU_STATE = -2;
    public static final int CREATE_HOVER_STATE = -3;
    public static final int LOAD_MENU_STATE = -4;
    public static final int LOAD_HOVER_STATE = -5;
    //specialStates
    public static final int BASE_STATE = 100;
    public static final int DRAWING_STATE = 101;
    public static final int DELETE_STATE = 102;

    int paintInstructions;
    int drawingState;
    int specialState;
    Manager m;

    public Drawing(Manager m) {
        super();
        this.m = m;
        paintInstructions = DEFAULT_MENU;
        drawingState = DEFAULT_MENU_STATE;
        specialState = BASE_STATE;
    }

    public void paint(int paintInstructions) {
        this.paintInstructions = paintInstructions;
        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        switch (paintInstructions) {
            case DEFAULT_MENU:
                clearDraw(g2);
                defaultMenuDraw(g2);
                drawingState = DEFAULT_MENU_STATE;
                break;
            case HOVER_LOAD_BUTTON:
                drawRectangle(g2, Manager.LOAD_BUTTON, 4);
                drawingState = DEFAULT_HOVER_STATE;
                break;
            case HOVER_CREATE_BUTTON:
                drawRectangle(g2, Manager.CREATE_BUTTON, 4);
                drawingState = DEFAULT_HOVER_STATE;
                break;
            case LOAD_MENU:
                clearDraw(g2);
                loadMenuDraw(g2);
                drawingState = LOAD_MENU_STATE;
                break;
            case CREATE_MENU:
                clearDraw(g2);
                createMenuDraw(g2);
                drawingState = CREATE_MENU_STATE;
                break;
            case HOVER_CREATE_BACK:
                drawRectangle(g2, Manager.BACK_BUTTON, 4);
                drawingState = CREATE_HOVER_STATE;
                break;
            case HOVER_LOAD_BACK:
                drawRectangle(g2, Manager.BACK_BUTTON, 4);
                drawingState = LOAD_HOVER_STATE;
                break;
            case HOVER_CREATE_INSTRUCTIONS:
                drawRectangle(g2, Manager.INSTRUCTIONS_BUTTON, 4);
                drawingState = CREATE_HOVER_STATE;
                break;
            case HOVER_CREATE_DRAW:
                drawRectangle(g2, Manager.DRAW_BUTTON, 4);
                drawingState = CREATE_HOVER_STATE;
                break;
        }
    }

    public void clearDraw(Graphics2D g2) {
        g2.clearRect(0, 0, 600, 600);
    }

    public void drawRectangle(Graphics2D g2, Rectangle r, int thickness) {
        int x = r.x;
        int y = r.y;
        int w = r.width;
        int h = r.height;
        g2.setStroke(new BasicStroke(thickness));
        g2.drawRect(x, y, w, h);
        g2.setStroke(new BasicStroke(1));
    }

    public void defaultMenuDraw(Graphics2D g2) {
        g2.setFont(new Font("This doesn't change the font", Font.PLAIN, 50));
        g2.drawString("Platformer", 175, 140);
        g2.setFont(new Font("", Font.PLAIN, 20));
        g2.drawString("Load Level", 247, 250);
        g2.drawString("Create Level", 241, 300);
        drawRectangle(g2, Manager.LOAD_BUTTON, 2);
        drawRectangle(g2, Manager.CREATE_BUTTON, 2);
    }

    public void createMenuDraw(Graphics2D g2) {
        g2.setFont(new Font("", Font.PLAIN, 20));
        g2.drawString("Back", 50, 56);
        drawRectangle(g2, Manager.BACK_BUTTON, 2);

        g2.drawString("Instructions", 427, 56);
        drawRectangle(g2, Manager.INSTRUCTIONS_BUTTON, 2);

        g2.drawString("Draw", 50, 514);
        drawRectangle(g2, Manager.DRAW_BUTTON, 2);
    }

    public void loadMenuDraw(Graphics2D g2) {
        g2.setFont(new Font("", Font.PLAIN, 20));
        g2.drawString("Back", 50, 55);
        drawRectangle(g2, Manager.BACK_BUTTON, 2);
    }
}
