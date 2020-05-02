package tools;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.List;

public class Rectangle extends Tool{

    public static RectangleOption option_selected = new RectangleOption(
            new BasicStroke(1),
            false
    );

    public static RectangleOption[] options = new RectangleOption[]{
            new RectangleOption(
                    new BasicStroke(1),
                    false
            ),
            new RectangleOption(
                    new BasicStroke(2),
                    false
            ),
            new RectangleOption(
                    new BasicStroke(5),
                    false
            ),
            new RectangleOption(
                    new BasicStroke(10),
                    false
            ),
            new RectangleOption(
                    new BasicStroke(20),
                    false
            ),
            new RectangleOption(
                    new BasicStroke(1),
                    true
            ),
    };

    @Override
    public void draw(Graphics2D g, boolean shift) {
        if(points.size() > 1){
            Point a = points.get(0);
            Point b = points.get(points.size() - 1);

            Point start = new Point(
                    Math.min(a.x, b.x),
                    Math.min(a.y, b.y)
            );
            Dimension size = new Dimension(
                    Math.abs(a.x - b.x),
                    Math.abs(a.y - b.y)
            );
            if(shift){
                size.height = Math.min(size.height, size.width);
                size.width = Math.min(size.height, size.width);
            }
            option_selected.draw(g, start, size);
        }
    }

    public static List<JButton> option_btns = new ArrayList<>();
}

class RectangleOption{
    public Stroke stroke;
    public boolean fill;

    RectangleOption(Stroke stroke, boolean fill){
        this.stroke = stroke;
        this.fill = fill;
    }

    public void draw(Graphics2D g, Point a, Dimension b){
        if(fill){
            g.fill(
                    stroke.createStrokedShape(
                            new java.awt.Rectangle(
                                    a,b
                            )
                    )
            );
        }else{
            g.draw(
                    stroke.createStrokedShape(
                            new java.awt.Rectangle(
                                    a,b
                            )
                    )
            );
        }
    }
}
