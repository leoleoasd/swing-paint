package tools;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;

public class Util {

    public static void dumpImage(BufferedImage img){
        JDialog dialog = new JDialog();
        dialog.setUndecorated(true);
        JLabel label = new JLabel(new ImageIcon(copyImage(img)));
        dialog.add(label);
        dialog.pack();
        dialog.setVisible(true);
    }

    public static BufferedImage copyImage(BufferedImage img) {
        BufferedImage b = new BufferedImage(img.getWidth(), img.getHeight(), img.getType());
        Graphics g = b.getGraphics();
        g.drawImage(img, 0, 0, null);
        g.dispose();
        return b;
    }

    public static BufferedImage rotateImageByDegrees(BufferedImage img, double rads) {
        double sin = Math.abs(Math.sin(rads)), cos = Math.abs(Math.cos(rads));
        int w = img.getWidth();
        int h = img.getHeight();
        int newWidth = (int) Math.floor(w * cos + h * sin);
        int newHeight = (int) Math.floor(h * cos + w * sin);

        BufferedImage rotated = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = rotated.createGraphics();
        AffineTransform at = new AffineTransform();
        at.translate((newWidth - w) / 2, (newHeight - h) / 2);

        int x = w / 2;
        int y = h / 2;

        at.rotate(rads, x, y);
        g2d.setTransform(at);
        g2d.drawImage(img, 0, 0, null);
        g2d.setColor(Color.RED);
        g2d.drawRect(0, 0, newWidth - 1, newHeight - 1);
        g2d.dispose();

        return rotated;
    }
}
