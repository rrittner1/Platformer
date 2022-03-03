import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Manager implements Runnable {
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
        canvas = new Drawing();
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
        canvas.paint(Drawing.PAINT_MENU);
    }

    public void mouseClicked(MouseEvent e) {

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
        if (canvas.drawingState != -1 && loc.x >= 230 && loc.x <= 365 && loc.y >= 225 && loc.y <= 260) {
            canvas.paint(Drawing.HOVER_LOAD_BUTTON);
        } else if (canvas.drawingState != -1 && loc.x >= 230 && loc.x <= 365 && loc.y >= 275 && loc.y <= 310) {
            canvas.paint(Drawing.HOVER_CREATE_BUTTON);
        } else if (canvas.drawingState != 0) {
            canvas.paint(Drawing.PAINT_MENU);
        }
    }

    public void keyTyped(KeyEvent e) {

    }

    public void keyPressed(KeyEvent e) {

    }

    public void keyReleased(KeyEvent e) {

    }
}
