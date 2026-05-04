package view;



import javax.swing.*;
import java.awt.*;

public class DragLayer extends JComponent {

    private Image draggedImage;
    private Point mouse;

    public void setDraggedImage(Image img) {
        this.draggedImage = img;
    }

    public void setMouse(Point p) {
        this.mouse = p;
        repaint();
    }

    public void clear() {
        draggedImage = null;
        mouse = null;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (draggedImage == null || mouse == null)
            return;

        int size = 40;
        g.drawImage(draggedImage,
                mouse.x - size / 2,
                mouse.y - size / 2,
                size, size, null);
    }
}