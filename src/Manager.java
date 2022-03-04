import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Manager implements Runnable {
    static final Rectangle LOAD_BUTTON = new Rectangle(230, 225, 135, 35);
    static final Rectangle CREATE_BUTTON = new Rectangle(230, 275, 135, 35);
    static final Rectangle BACK_BUTTON = new Rectangle(42, 32, 60, 35);
    static final Rectangle INSTRUCTIONS_BUTTON = new Rectangle(415, 32, 125, 35);
    static final Rectangle DRAW_BUTTON = new Rectangle(42, 490, 60, 35);

    JFrame frame;
    Drawing canvas;
    Listener listener;
    Container contentPane;

    public Manager() {
        frame = new JFrame("Platformer");
        frame.setSize(600, 600);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setFocusable(true);
        frame.addWindowFocusListener(new WindowAdapter() {
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
        canvas = new Drawing(this);
        canvas.setBounds(0, 0, 600, 600);
        listener = new Listener(this);
        listener.setBounds(0, 0, 600, 600);
        listener.setBackground(new Color(0, 0, 0, 0));
        listener.setFocusable(true);
        contentPane.add(listener);
        contentPane.add(canvas);
    }
    @Override
    public void run() {
        frame.setVisible(true);
        frame.requestFocusInWindow();
        menuSetup();
    }

    /*
     * Initial graphical setup of menu screen
     */
    public void menuSetup() {
        canvas.paint(Drawing.DEFAULT_MENU);
    }

    public void mouseClicked(MouseEvent e) {
        Point loc = e.getPoint();
        switch (canvas.drawingState) {
            case Drawing.DEFAULT_HOVER_STATE:
                if (LOAD_BUTTON.contains(loc)) {
                    canvas.paint(Drawing.LOAD_MENU);
                } else if (CREATE_BUTTON.contains(loc)) {
                    canvas.paint(Drawing.CREATE_MENU);
                }
                break;
            case Drawing.CREATE_HOVER_STATE:
                if (BACK_BUTTON.contains(loc)) {
                    canvas.paint(Drawing.DEFAULT_MENU);
                } else if (INSTRUCTIONS_BUTTON.contains(loc)) {
                    canvas.paint(Drawing.DEFAULT_MENU); // This is a placeholder
                } else if (DRAW_BUTTON.contains(loc)) {
                    // do something
                }
                break;
            case Drawing.LOAD_HOVER_STATE:
                if (BACK_BUTTON.contains(loc)) {
                    canvas.paint(Drawing.DEFAULT_MENU);
                }
                break;
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

    public void mouseMoved(MouseEvent e) {
        Point loc = e.getPoint();
        switch (canvas.drawingState) {
            case Drawing.DEFAULT_MENU_STATE:
                if (LOAD_BUTTON.contains(loc)) {
                    canvas.paint(Drawing.HOVER_LOAD_BUTTON);
                } else if (CREATE_BUTTON.contains(loc)) {
                    canvas.paint(Drawing.HOVER_CREATE_BUTTON);
                }
                break;
            case Drawing.DEFAULT_HOVER_STATE:
                if (!LOAD_BUTTON.contains(loc) && !CREATE_BUTTON.contains(loc)) {
                    canvas.paint(Drawing.DEFAULT_MENU);
                }
                break;
            case Drawing.CREATE_MENU_STATE:
                if (BACK_BUTTON.contains(loc)) {
                    canvas.paint(Drawing.HOVER_CREATE_BACK);
                } else if (INSTRUCTIONS_BUTTON.contains(loc)) {
                    canvas.paint(Drawing.HOVER_CREATE_INSTRUCTIONS);
                } else if (DRAW_BUTTON.contains(loc)) {
                    canvas.paint(Drawing.HOVER_CREATE_DRAW);
                }
                break;
            case Drawing.CREATE_HOVER_STATE:
                if (!BACK_BUTTON.contains(loc) && !INSTRUCTIONS_BUTTON.contains(loc) &&
                        !DRAW_BUTTON.contains(loc)) {
                    canvas.paint(Drawing.CREATE_MENU);
                }
                break;
            case Drawing.LOAD_MENU_STATE:
                if (BACK_BUTTON.contains(loc)) {
                    canvas.paint(Drawing.HOVER_LOAD_BACK);
                }
                break;
            case Drawing.LOAD_HOVER_STATE:
                if (!BACK_BUTTON.contains(loc)) {
                    canvas.paint(Drawing.LOAD_MENU);
                }
                break;
        }
    }

    public void keyTyped(KeyEvent e) {

    }

    public void keyPressed(KeyEvent e) {

    }

    public void keyReleased(KeyEvent e) {

    }
}
