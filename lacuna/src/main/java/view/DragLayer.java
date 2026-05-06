package view;

import javax.swing.*;
import java.awt.*;

public class DragLayer extends JComponent {

    private Image draggedImage;
    private Point mouse;
    private Object payload;

    public void startDrag(Image img, Object payload, Point p) {
        this.draggedImage = img;
        this.payload = payload;
        this.mouse = p;
        setVisible(true);
        repaint();
    }

    public void updateMouse(Point p) {
        this.mouse = p;
        repaint();
    }

    public Object getPayload() {
        return payload;
    }

    public void clear() {
        draggedImage = null;
        mouse = null;
        payload = null;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (draggedImage == null || mouse == null) return;

        int size = 40;
        g.drawImage(draggedImage,
                mouse.x - size / 2,
                mouse.y - size / 2,
                size, size, null);
    }
}