package tools;

import tools.pens.AbstractPen;
import ui.PaintPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.reflect.InvocationTargetException;

public class EraserTool extends AbstractTool{

    AbstractPen drawing;
    public Class<? extends AbstractPen> selected_pen;
    private static EraserTool instance = null;

    public static void initialize(PaintPanel panel){
        instance = new EraserTool(panel);
    }

    public static EraserTool getInstance(){
        return instance;
    }

    protected EraserTool(PaintPanel panel) {
        super(panel);
        selected_pen = null;
    }

    @Override
    public void paint(Graphics2D canvas) {
        canvas.drawImage(panel.img, 0, 0, null);
        if (drawing != null) {
            canvas.setPaint(panel.front);
            drawing.draw(canvas, panel.shift);
        }
    }

    @Override
    protected MouseAdapter makeMouseAdapter() {
        return new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                try {
                    drawing = (AbstractPen) selected_pen.getDeclaredConstructor().newInstance();
                    drawing.addPoint(e.getPoint());
                    panel.repaint();
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(panel, "未知错误!\n" + ex.toString());
                    System.exit(1);
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                panel.pushStack();
                drawing.addPoint(e.getPoint());
                panel.imgG.setPaint(panel.front);
                drawing.draw(panel.imgG, panel.shift);
                drawing = null;
                panel.repaint();
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                drawing.addPoint(e.getPoint());
                panel.repaint();
            }
        };
    }


    @Override
    public void begin() {
        panel.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
    }

    @Override
    public void finish() {
        panel.setCursor(Cursor.getDefaultCursor());
    }
}
