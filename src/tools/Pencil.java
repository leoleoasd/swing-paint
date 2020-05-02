package tools;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.List;

public class Pencil extends Tool{

    public static Stroke option_selected = new BasicStroke(1, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);

    public static Stroke[] options = new Stroke[]{
            new BasicStroke(1, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND),
            new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND),
            new BasicStroke(5, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND),
            new BasicStroke(10, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND),
            new BasicStroke(20, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND),
    };

    @Override
    public void draw(Graphics2D g, boolean shift) {
        if(points.size() > 1){
            for(int i = 0; i <  points.size() - 1; ++ i){
                g.draw(
                        option_selected.createStrokedShape(
                                new Line2D.Double(
                                        points.get(i),
                                        points.get(i + 1)
                                )
                        )
                );
            }
        }
    }

    public static List<JButton> option_btns = new ArrayList<>();
}
