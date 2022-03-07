import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class Drawing extends JPanel {
    /**
     * paintInstructions: instructions passed into paint to specify what to draw
     */
    public static final int DEFAULT_MENU = 1; // draw root menu
    public static final int HOVER_LOAD_BUTTON = 2; // draw root menu with thicker load button
    public static final int HOVER_CREATE_BUTTON = 3; // draw root menu with thicker create button.
    public static final int LOAD_MENU = 4; // draw load menu
    public static final int CREATE_MENU = 5; // draw create menu
    public static final int HOVER_CREATE_BACK = 6; // draw create menu with thicker back button
    public static final int HOVER_LOAD_BACK = 7; // draw load menu with thicker back button
    public static final int HOVER_CREATE_INSTRUCTIONS = 8; // draw create menu with thicker instructions button
    public static final int HOVER_CREATE_DRAW = 9; // draw create menu with thicker draw button
    public static final int HOVER_CREATE_SAVE = 10; // draw create menu with thicker save button
    /**
     * menuStates: signifier of current status of image on drawing
     */
    public static final int DEFAULT_MENU_STATE = 0; // Normal root menu state
    public static final int DEFAULT_HOVER_STATE = -1; // Button hovered on in root menu
    public static final int CREATE_MENU_STATE = -2; // Normal create menu state
    public static final int CREATE_HOVER_STATE = -3; // Button hovered on in create menu
    public static final int LOAD_MENU_STATE = -4; // Normal load menu state
    public static final int LOAD_HOVER_STATE = -5; // Button hovered on in Load menu
    public static final int DRAW_LINE_STATE = -6;
    /**
     * specialStates: signifier that something is happening on a current menu state
     */
    public static final int BASE_STATE = -100; // normal state
    public static final int DRAW_STATE = -101; // 2-click drawing active
    public static final int DELETE_STATE = -102;

    int paintInstructions;
    int drawingState;
    int specialState;
    Manager m;

    /**
     * initializes variables including Manager m
     * @param m
     */
    public Drawing(Manager m) {
        super();
        this.m = m;
        paintInstructions = DEFAULT_MENU;
        drawingState = DEFAULT_MENU_STATE;
        specialState = BASE_STATE;
    }

    /**
     * sets paintInstructions to given paintInstructions constant then calls repaint to invoke paintComponent()
     * @param paintInstructions
     */
    public void paint(int paintInstructions) {
        this.paintInstructions = paintInstructions;
        repaint();
    }

    /**
     * draws something with Graphics2D based on what paintInstruction is
     * @param g
     */
    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        switch (paintInstructions) {
            case DEFAULT_MENU: // draw root menu
                clearDraw(g2);
                defaultMenuDraw(g2);
                drawingState = DEFAULT_MENU_STATE;
                specialState = BASE_STATE;
                break;
            case HOVER_LOAD_BUTTON: // draw root menu with thicker load button
                drawRectangle(g2, Manager.LOAD_BUTTON, 4);
                drawingState = DEFAULT_HOVER_STATE;
                break;
            case HOVER_CREATE_BUTTON: // draw root menu with thicker create button
                drawRectangle(g2, Manager.CREATE_BUTTON, 4);
                drawingState = DEFAULT_HOVER_STATE;
                break;
            case LOAD_MENU: // draw load menu
                clearDraw(g2);
                loadMenuDraw(g2);
                drawingState = LOAD_MENU_STATE;
                break;
            case CREATE_MENU: // draw create menu
                clearDraw(g2);
                createMenuDraw(g2);
                drawingState = CREATE_MENU_STATE;
                break;
            case HOVER_CREATE_BACK: // draw create menu with thicker back button
                drawRectangle(g2, Manager.BACK_BUTTON, 4);
                drawingState = CREATE_HOVER_STATE;
                break;
            case HOVER_LOAD_BACK: // draw load menu with thicker back button
                drawRectangle(g2, Manager.BACK_BUTTON, 4);
                drawingState = LOAD_HOVER_STATE;
                break;
            case HOVER_CREATE_INSTRUCTIONS: // draw create menu with thicker instructions button
                drawRectangle(g2, Manager.INSTRUCTIONS_BUTTON, 4);
                drawingState = CREATE_HOVER_STATE;
                break;
            case HOVER_CREATE_DRAW: // draw create menu with thicker draw button
                drawRectangle(g2, Manager.DRAW_BUTTON, 4);
                drawingState = CREATE_HOVER_STATE;
                break;
            case HOVER_CREATE_SAVE: // draw create menu with thicker save button
                drawRectangle(g2, Manager.SAVE_BUTTON, 4);
                drawingState = CREATE_HOVER_STATE;
                break;
        }
    }

    /**
     * clears whole frame
     * @param g2
     */
    public void clearDraw(Graphics2D g2) {
        g2.clearRect(0, 0, 600, 600);
    }

    /**
     * draws given rectangle with given thickness
     * @param g2
     * @param r
     * @param thickness
     */
    public void drawRectangle(Graphics2D g2, Rectangle r, int thickness) {
        int x = r.x;
        int y = r.y;
        int w = r.width;
        int h = r.height;
        g2.setStroke(new BasicStroke(thickness));
        g2.drawRect(x, y, w, h);
        g2.setStroke(new BasicStroke(1));
    }

    /**
     * draws root menu
     * @param g2
     */
    public void defaultMenuDraw(Graphics2D g2) {
        g2.setFont(new Font("This doesn't change the font", Font.PLAIN, 50));
        g2.drawString("Platformer", 175, 140);
        g2.setFont(new Font("", Font.PLAIN, 20));
        g2.drawString("Load Level", 247, 250);
        g2.drawString("Create Level", 241, 300);
        drawRectangle(g2, Manager.LOAD_BUTTON, 2);
        drawRectangle(g2, Manager.CREATE_BUTTON, 2);
    }

    /**
     * draws create menu
     * @param g2
     */
    public void createMenuDraw(Graphics2D g2) {
        for (int i = 0; i < 600; i++) { // draws anything saved in level array
            for (int j = 0; j < 600; j++) {
                if (m.activeMap[i + m.xOffset][j] == 1) {
                    g2.fillRect(i, j, 1, 1);
                }
            }
        }
        if (specialState == DRAW_STATE) {
            g2.setFont(new Font("", 1, 25));
            g2.drawString("Drawing", 230, 50); // if in active drawing state signify with text
            if (m.startPoint.x != -69) { // if 2-click line is active draw line at current mouse location
                g2.setStroke(new BasicStroke(5));
                g2.drawLine(m.startPoint.x, m.startPoint.y, m.loc.x, m.loc.y);
            }
        }
        // draw back button
        g2.setColor(Color.WHITE);
        g2.fillRect(42, 32, 60, 35);
        g2.setColor(Color.BLACK);
        g2.setFont(new Font("", Font.PLAIN, 20));
        g2.drawString("Back", 50, 56);
        drawRectangle(g2, Manager.BACK_BUTTON, 2);
        // draw Instructions button
        g2.setColor(Color.WHITE);
        g2.fillRect(415, 32, 125, 35);
        g2.setColor(Color.BLACK);
        g2.drawString("Instructions", 427, 56);
        drawRectangle(g2, Manager.INSTRUCTIONS_BUTTON, 2);
        // draw Draw button
        g2.setColor(Color.WHITE);
        g2.fillRect(42, 490, 60, 35);
        g2.setColor(Color.BLACK);
        g2.drawString("Draw", 50, 514);
        drawRectangle(g2, Manager.DRAW_BUTTON, 2);
        // draw save button
        g2.setColor(Color.WHITE);
        g2.fillRect(480, 82, 60, 35);
        g2.setColor(Color.BLACK);
        g2.drawString("Save", 488, 106);
        drawRectangle(g2, Manager.SAVE_BUTTON, 2);
    }

    /**
     * draw load menu
     * @param g2
     */
    public void loadMenuDraw(Graphics2D g2) {
        g2.setFont(new Font("", Font.PLAIN, 20));
        g2.drawString("Back", 50, 55);
        drawRectangle(g2, Manager.BACK_BUTTON, 2);
    }
}
