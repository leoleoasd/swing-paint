package tools;

import ui.PaintPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class AbstractNeedSelectionTool extends AbstractTool {

    public Point selecting_start;
    public Point selecting_end;
    protected BufferedImage selecting_img;
    protected int img_x = 0;
    protected int img_y = 0;

    protected AbstractNeedSelectionTool(PaintPanel panel) {
        super(panel);
    }

    @Override
    public void paint(Graphics2D canvas) {
        canvas.drawImage(panel.img, 0, 0, null);
        canvas.drawImage(selecting_img, img_x,img_y,null);
        canvas.draw(
                new BasicStroke(3, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{9}, 0).createStrokedShape(
                        new java.awt.Rectangle(
                                img_x,
                                img_y,
                                selecting_img.getWidth(),
                                selecting_img.getHeight()
                        )
                )
        );
    }

    @Override
    public void begin() {
        panel.pushStack();
        img_x = Math.min(this.selecting_start.x, this.selecting_end.x);
        img_y = Math.min(this.selecting_start.y, this.selecting_end.y);
        int w =
                Math.max(this.selecting_start.x, this.selecting_end.x) - Math.min(this.selecting_start.x, this.selecting_end.x);
        w = Math.min(w, panel.img.getWidth() - img_x);
        int h =
                Math.max(this.selecting_start.y, this.selecting_end.y) - Math.min(this.selecting_start.y, this.selecting_end.y);
        h = Math.min(h, panel.img.getHeight() - img_y);
        selecting_img = Util.copyImage(panel.img.getSubimage(img_x, img_y,w,h));
        panel.imgG.setPaint(panel.back);
        panel.imgG.fill(
                new Rectangle(
                        img_x,
                        img_y,
                        selecting_img.getWidth(),
                        selecting_img.getHeight()
                )
        );
    }

    @Override
    public void finish() {
        panel.pushStack();
        panel.imgG.drawImage(selecting_img, img_x, img_y, null);
    }
}
