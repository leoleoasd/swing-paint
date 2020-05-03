package tools;

import ui.PaintPanel;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class SelectingTool extends AbstractTool {
    AbstractNeedSelectionTool nextTool;

    private static SelectingTool instance = null;

    public static void initialize(PaintPanel panel){
        instance = new SelectingTool(panel);
    }

    public static SelectingTool getInstance(AbstractNeedSelectionTool nextTool){
        instance.nextTool = nextTool;
        return instance;
    }

    protected SelectingTool(PaintPanel panel) {
        super(panel);
    }

    @Override
    public void paint(Graphics2D canvas) {
        canvas.drawImage(panel.img, 0, 0, null);
        if (
                nextTool.selecting_start != null &&
                        nextTool.selecting_end != null &&
                        !nextTool.selecting_start.equals(nextTool.selecting_end
                        )) {
            canvas.draw(
                    new BasicStroke(3, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{9}, 0).createStrokedShape(
                            new java.awt.Rectangle(
                                    Math.min(nextTool.selecting_start.x, nextTool.selecting_end.x),
                                    Math.min(nextTool.selecting_start.y, nextTool.selecting_end.y),
                                    Math.max(nextTool.selecting_start.x, nextTool.selecting_end.x) - Math.min(nextTool.selecting_start.x, nextTool.selecting_end.x),
                                    Math.max(nextTool.selecting_start.y, nextTool.selecting_end.y) - Math.min(nextTool.selecting_start.y, nextTool.selecting_end.y)
                            )
                    )
            );
        }
    }

    @Override
    protected MouseAdapter makeMouseAdapter() {
        return new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                nextTool.selecting_start = e.getPoint();
                panel.repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if(e.getPoint().equals(nextTool.selecting_start)){
                    nextTool.selecting_start = null;
                    panel.repaint();
                    return;
                }
                nextTool.selecting_end = e.getPoint();
                panel.setTool(nextTool);
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                nextTool.selecting_end = e.getPoint();
                panel.repaint();
            }
        };
    }

    @Override
    public void begin() {
        panel.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
    }
}
