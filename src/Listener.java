import javax.swing.*;
import java.awt.event.*;

public class Listener extends JPanel implements MouseListener, MouseMotionListener, KeyListener {
    /**
     * All listener methods in this class get passed straight to Manager
     */
    Manager m;

    /**
     * initializes Manager m, adds itself as its own listener
     * @param m
     */
    public Listener(Manager m) {
        super();
        this.m = m;
        addMouseListener(this);
        addMouseMotionListener(this);
        addKeyListener(this);
    }
    @Override
    public void mouseClicked(MouseEvent e) {
        m.mouseClicked(e);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        m.mousePressed(e);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        m.mouseReleased(e);
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        m.mouseEntered(e);
    }

    @Override
    public void mouseExited(MouseEvent e) {
        m.mouseExited(e);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        m.mouseDragged(e);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        m.mouseMoved(e);
    }

    @Override
    public void keyTyped(KeyEvent e) {
        m.keyTyped(e);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        m.keyPressed(e);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        m.keyReleased(e);
    }
}
