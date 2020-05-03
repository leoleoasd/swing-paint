package tools;

import ui.MainFrame;
import ui.PaintPanel;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.font.GlyphVector;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class ImageTool extends AbstractTool{

    private static ImageTool instance = null;

    public static void initialize(PaintPanel panel){
        instance = new ImageTool(panel);
    }

    public static ImageTool getInstance(){
        return instance;
    }

    private ImageTool(PaintPanel panel) {
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
                JFileChooser fc = new JFileChooser();
                fc.setFileFilter(new FileNameExtensionFilter("图片","png", "jpg", "PNG", "JPG", "jpeg"));
                int ret = fc.showOpenDialog(panel);
                if(ret == JFileChooser.APPROVE_OPTION){
                    try {
                        BufferedImage img = ImageIO.read(fc.getSelectedFile());
                        panel.pushStack();
                        panel.imgG.drawImage(img,e.getX(),e.getY(), null);

                        EditTool t = EditTool.getInstance();
                        t.selecting_start = e.getPoint();
                        t.selecting_end = new Point(e.getPoint());
                        t.selecting_end.translate(
                                img.getWidth(),
                                img.getHeight()
                        );
                        panel.setTool(t);
                    } catch (IOException ioException) {
                        JOptionPane.showMessageDialog(panel, "读取图片失败!");
                    }
                }
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
