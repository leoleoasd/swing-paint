package tools;

import ui.PaintPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.font.GlyphVector;

public class TextTool extends AbstractTool{

    private static TextTool instance = null;

    public static void initialize(PaintPanel panel){
        instance = new TextTool(panel);
    }

    public static TextTool getInstance(){
        return instance;
    }

    private TextTool(PaintPanel panel) {
        super(panel);
    }

    @Override
    public void paint(Graphics2D canvas) {
        canvas.drawImage(panel.img, 0, 0, null);
    }

    @Override
    protected MouseAdapter makeMouseAdapter() {
        return new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                panel.pushStack();
                String text = JOptionPane.showInputDialog("请输入文字");
                System.out.println(text);
                Font f = panel.getFont().deriveFont(Font.BOLD, 40);
                GlyphVector v = f.createGlyphVector(panel.getFontMetrics(f).getFontRenderContext(), text);
                Shape textshape = v.getOutline(e.getX(), e.getY());
                panel.imgG.setPaint(panel.front);
                panel.imgG.fill(textshape);
                EditTool t = EditTool.getInstance();
                t.selecting_start = textshape.getBounds().getLocation();
                t.selecting_end = new Point((int)textshape.getBounds().getMaxX(),(int)textshape.getBounds().getMaxY());
                panel.setTool(t);
            }
        };
    }

    @Override
    public void begin() {
        panel.setCursor(new Cursor(Cursor.TEXT_CURSOR));
    }

    @Override
    public void finish() {
        panel.setCursor(Cursor.getDefaultCursor());
    }
}
