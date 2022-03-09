import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Manager implements Runnable {
    // Rectangle objects representing various buttons
    static final Rectangle LOAD_BUTTON = new Rectangle(230, 225, 135, 35);
    static final Rectangle CREATE_BUTTON = new Rectangle(230, 275, 135, 35);
    static final Rectangle BACK_BUTTON = new Rectangle(42, 32, 60, 35);
    static final Rectangle INSTRUCTIONS_BUTTON = new Rectangle(415, 32, 125, 35);
    static final Rectangle DRAW_BUTTON = new Rectangle(42, 490, 60, 35);
    static final Rectangle SAVE_BUTTON = new Rectangle(480, 82, 60, 35);

    // bounds of level
    static final int mapWidth = 60000;
    static final int mapHeight = 600;

    JFrame frame; // Container frame
    Drawing canvas; // JPanel with overwritten paintComponent()
    Listener listener; // JPanel that acts as listener for all events
    Container contentPane; // frame's contentPane
    int[][] activeMap; // array of all pixels in a given level
    Point startPoint; // start point for 2-click lines -- Absolute coordinate
    Point endPoint; // end point for 2-click lines -- Absolute coordinate
    Point loc; // current location of mouse, updates on every movement/click -- relative coordinate
    int xOffset; // how many pixels into the level the screen is
    Timer manualScrollTimer; // timer to scroll screen while a or d keys pressed
    char scrollDirection; // a to scroll left, d to scroll right

    /**
     * Sets up JFrame, initializes instance variables.
     */
    public Manager() {
        frame = new JFrame("Platformer");
        frame.setSize(600, 600);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setFocusable(true);
        frame.addWindowFocusListener(new WindowAdapter() { // gives listener focus
            @Override
            public void windowGainedFocus(WindowEvent e) {
                super.windowGainedFocus(e);
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        listener.requestFocusInWindow();
                    }
                });
            }
        });
        contentPane = frame.getContentPane();
        // canvas is the JPanel that menus and buttons get drawn on
        canvas = new Drawing(this);
        canvas.setBounds(0, 0, 600, 600);
        canvas.setBackground(new Color(0, 0, 0, 0));
        // listener is a transparent JPanel on top of drawing that is its own action listener,
        // all event methods just get passed to methods of the same name below
        listener = new Listener(this);
        listener.setBounds(0, 0, 600, 600);
        listener.setBackground(new Color(0, 0, 0, 0));
        listener.setFocusable(true);
        contentPane.add(listener);
        contentPane.add(canvas);
        activeMap = new int[mapWidth][mapHeight];
        startPoint = new Point(-69, -69); // default OOB values
        endPoint = new Point(-69, -69);
        xOffset = 0;
        loc = new Point(-69, -69);
        manualScrollTimer = new Timer(10, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                scrollScreen();
            }
        });
        scrollDirection = '\0';
    }
    @Override
    public void run() {
        frame.setVisible(true);
        frame.requestFocusInWindow();
        menuSetup();
    }

    /**
     * Initial graphical setup of menu screen
     */
    public void menuSetup() {
        canvas.paint(Drawing.DEFAULT_MENU);
    }

    /**
     * mouseClick listener: what to do if a certain point is clicked given a certain drawing and special state
     * @param e
     */
    public void mouseClicked(MouseEvent e) {
        loc = e.getPoint();
        switch (canvas.drawingState) {
            case Drawing.DEFAULT_HOVER_STATE: // something being hovered on in root menu
                if (LOAD_BUTTON.contains(loc)) {
                    canvas.paint(Drawing.LOAD_MENU);
                } else if (CREATE_BUTTON.contains(loc)) {
                    canvas.specialState = Drawing.BASE_STATE;
                    canvas.paint(Drawing.CREATE_MENU);
                }
                break;
            case Drawing.CREATE_HOVER_STATE: // something being hovered on in create level menu
                if (BACK_BUTTON.contains(loc)) {
                    canvas.specialState = Drawing.BASE_STATE; // special state returned to base if not drawing button
                    canvas.paint(Drawing.DEFAULT_MENU);
                } else if (INSTRUCTIONS_BUTTON.contains(loc)) {
                    canvas.specialState = Drawing.BASE_STATE;
                    canvas.paint(Drawing.DEFAULT_MENU); // This is a placeholder
                } else if (DRAW_BUTTON.contains(loc)) { // toggle from drawing to base or vise versa
                    if (canvas.specialState == Drawing.BASE_STATE) {
                        canvas.specialState = Drawing.DRAW_STATE;
                    } else if (canvas.specialState == Drawing.DRAW_STATE) {
                        canvas.specialState = Drawing.BASE_STATE;
                    }
                    canvas.paint(Drawing.HOVER_CREATE_DRAW);
                } else if (SAVE_BUTTON.contains(loc)) {
                    System.out.println("here");
                }
                break;
            case Drawing.LOAD_HOVER_STATE: // something being hovered on in load menu
                if (BACK_BUTTON.contains(loc)) {
                    canvas.paint(Drawing.DEFAULT_MENU);
                }
                break;
            case Drawing.CREATE_MENU_STATE: // normal create menu state
                if (canvas.specialState == Drawing.DRAW_STATE) { // if in draw state update/draw 2-click line
                    if (startPoint.x == -69) {
                        startPoint = loc;
                        startPoint.x += xOffset;
                    } else {
                        endPoint = loc;
                        endPoint.x += xOffset;
                        saveLine(); // saves drawn line to activeMap level array
                        startPoint.x = -69; // resets start point to default OOB value
                        startPoint.y = -69;
                        canvas.paint(Drawing.CREATE_MENU);
                    }
                }
        }
    }

    public void mousePressed(MouseEvent e) {

    }

    public void mouseReleased(MouseEvent e) {

    }

    public void mouseEntered(MouseEvent e) {

    }

    public void mouseExited(MouseEvent e) {

    }

    public void mouseDragged(MouseEvent e) {

    }

    /**
     * mouse movement listener: what to do if mouse moves to a certain point given current canvas states
     * @param e
     */
    public void mouseMoved(MouseEvent e) {
        loc = e.getPoint();
        switch (canvas.drawingState) {
            case Drawing.DEFAULT_MENU_STATE: // Normal root menu state
                if (LOAD_BUTTON.contains(loc)) { // if mouse on one of the buttons thicken borders
                    canvas.paint(Drawing.HOVER_LOAD_BUTTON);
                } else if (CREATE_BUTTON.contains(loc)) {
                    canvas.paint(Drawing.HOVER_CREATE_BUTTON);
                }
                break;
            case Drawing.DEFAULT_HOVER_STATE: // Button hovered over in root menu
                if (!LOAD_BUTTON.contains(loc) && !CREATE_BUTTON.contains(loc)) {
                    canvas.paint(Drawing.DEFAULT_MENU); // if mouse no longer on button revert border to default
                }
                break;
            case Drawing.CREATE_MENU_STATE: // Normal level creation menu state
                if (BACK_BUTTON.contains(loc)) { // redraw hovered on button borders
                    canvas.paint(Drawing.HOVER_CREATE_BACK);
                } else if (INSTRUCTIONS_BUTTON.contains(loc)) {
                    canvas.paint(Drawing.HOVER_CREATE_INSTRUCTIONS);
                } else if (DRAW_BUTTON.contains(loc)) {
                    canvas.paint(Drawing.HOVER_CREATE_DRAW);
                } else if (SAVE_BUTTON.contains(loc)) {
                    canvas.paint(Drawing.HOVER_CREATE_SAVE);
                } else {
                    if (canvas.specialState == Drawing.DRAW_STATE) { // if not on button and active 2-click redraw menu
                        if (startPoint.x != -69) {
                            canvas.paint(Drawing.CREATE_MENU);
                        }
                    }
                }
                break;
            case Drawing.CREATE_HOVER_STATE: // Button hovered over in level creation menu
                if (!BACK_BUTTON.contains(loc) && !INSTRUCTIONS_BUTTON.contains(loc) &&
                        !DRAW_BUTTON.contains(loc) && !SAVE_BUTTON.contains(loc)) {
                    canvas.paint(Drawing.CREATE_MENU); // if no longer hovered on redraw menu to revert borders
                }
                break;
            case Drawing.LOAD_MENU_STATE: // Normal load menu state
                if (BACK_BUTTON.contains(loc)) { // redraw hovered on button borders
                    canvas.paint(Drawing.HOVER_LOAD_BACK);
                }
                break;
            case Drawing.LOAD_HOVER_STATE: // Button hovered on in load menu state
                if (!BACK_BUTTON.contains(loc)) { // if no longer hovered on redraw menu to revert borders
                    canvas.paint(Drawing.LOAD_MENU);
                }
                break;
        }
    }

    public void keyTyped(KeyEvent e) {

    }

    /**
     * key pressed listener: starts timer if a or d is pressed
     * @param e
     */
    public void keyPressed(KeyEvent e) {
        char c = e.getKeyChar();
        if (c == 'a' || c == 'd') {
            if (scrollDirection == 'a' || scrollDirection == 'd') {
                manualScrollTimer.stop();
            }
            scrollDirection = c;
            manualScrollTimer.start();
        }
    }

    /**
     * key released listener: stops timer if a or d is released.
     * @param e
     */
    public void keyReleased(KeyEvent e) {
        char c = e.getKeyChar();
        if (c == 'a' || c == 'd') {
            manualScrollTimer.stop();
            scrollDirection = '\0';
        }
    }

    /**
     * called by timer, moves the screen 5 pixels every hundredth of a second while timer runs.
     */
    public void scrollScreen() {
        if (scrollDirection == 'a') {
            if (xOffset > 0) {
                xOffset -= 5;
            }
        } else if (scrollDirection == 'd') {
            if (xOffset < 54000) {
                xOffset += 5;
            }
        } else {
            System.out.println("error246");
        }
        canvas.paint(Drawing.CREATE_MENU);
    }

    /**
     * saves current 2-click line to activeMap array
     * works other than perfectly vertical lines
     * the lines don't look good but can confirm it is a problem with drawing
     * they get saved to array properly
     */
    public void saveLine() {
        int width = 5;
        int sx = startPoint.x;
        int sy = startPoint.y;
        int ex = endPoint.x;
        int ey = endPoint.y;
        int smallerX;
        int largerX;
        int smallerY;
        int largerY;
        if (sx < ex) {
            smallerX = sx;
            largerX = ex;
        } else {
            smallerX = ex;
            largerX = sx;
        }
        if (sy < ey) {
            smallerY = sy;
            largerY = ey;
        } else {
            smallerY = ey;
            largerY = sy;
        }
        if (Math.abs(ex - sx) > Math.abs(ey - sy)) {
            double m = 1.0 * (ey - sy) / (ex - sx);
            double r = 0;
            if (m != 0) {
                r = -1 / m;
            }
            for (int i = xOffset + smallerX - width / 2; i < xOffset + largerX + width / 2; i++) {
                for (int j = smallerY - width / 2; j < largerY + width / 2; j++) {
                    double lLineY = m * (i - sx) + sy - (width / 2.0) * Math.sqrt(1 + m * m);
                    double uLineY = m * (i - sx) + sy + (width / 2.0) * Math.sqrt(1 + m * m);
                    double sTipY = r * (i - sx) + sy;
                    double eTipY = r * (i - ex) + ey;
                    double uTipY;
                    double lTipY;
                    if (sTipY > eTipY) {
                        uTipY = sTipY;
                        lTipY = eTipY;
                    } else {
                        uTipY = eTipY;
                        lTipY = sTipY;
                    }
                    if (m != 0) {
                        if (j >= lLineY && j >= lTipY && j <= uLineY && j <= uTipY) {
                            if (i >= 0 && i < 60000 && j >= 0 && j < 600) {
                                activeMap[i][j] = 1;
                            }
                        }
                    } else {
                        if (j >= lLineY && j <= uLineY) {
                            if (i >= 0 && i < 60000 && j >= 0 && j < 600) {
                                activeMap[i][j] = 1;
                            }
                        }
                    }
                }
            }
        } else {
            double m = 1.0 * (ex - sx) / (ey - sy);
            double r = 0;
            if (m != 0) {
                r = -1 / m;
            }
            for (int i = xOffset + smallerX - width / 2; i < xOffset + largerX + width / 2; i++) {
                for (int j = smallerY - width / 2; j < largerY + width / 2; j++) {
                    double lLineX = m * (j - sy) + sx - (width / 2.0) * Math.sqrt(1 + m * m);
                    double uLineX = m * (j - sy) + sx + (width / 2.0) * Math.sqrt(1 + m * m);
                    double sTipX = r * (j - sy) + sx;
                    double eTipX = r * (j - ey) + ex;
                    double uTipX;
                    double lTipX;
                    if (sTipX > eTipX) {
                        uTipX = sTipX;
                        lTipX = eTipX;
                    } else {
                        uTipX = eTipX;
                        lTipX = sTipX;
                    }
                    if (m != 0) {
                        if (i >= lLineX && i >= lTipX && i <= uLineX && i <= uTipX) {
                            if (i >= 0 && i < 60000 && j >= 0 && j < 600) {
                                activeMap[i][j] = 1;
                            }
                        }
                    } else {
                        if (i >= lLineX && j <= uLineX) {
                            if (i >= 0 && i < 60000 && j >= 0 && j < 600) {
                                activeMap[i][j] = 1;
                            }
                        }
                    }
                }
            }
        }
    }
}
