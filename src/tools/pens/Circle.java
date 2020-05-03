package tools.pens;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Ellipse2D;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Circle extends AbstractPen {

    public static CircleOption option_selected = new CircleOption(
            new BasicStroke(1),
            false
    );

    public static CircleOption[] options = new CircleOption[]{
            new CircleOption(
                    new BasicStroke(1),
                    false
            ),
            new CircleOption(
                    new BasicStroke(2),
                    false
            ),
            new CircleOption(
                    new BasicStroke(5),
                    false
            ),
            new CircleOption(
                    new BasicStroke(10),
                    false
            ),
            new CircleOption(
                    new BasicStroke(20),
                    false
            ),
            new CircleOption(
                    new BasicStroke(1),
                    true
            ),
    };

    @Override
    public void draw(Graphics2D g, boolean shift) {
        if(this.points.size() > 1){
            Point a = points.get(0);
            Point b = points.get(points.size() - 1);

            if(shift){
                if(b.x - a.x == 0 || b.y - a.y == 0){
                    b.setLocation(a);
                }else{
                    int xx,yy;
                    xx = (b.x - a.x) / Math.abs(b.x - a.x);
                    yy = (b.y - a.y) / Math.abs(b.y - a.y);
                    b.setLocation(
                            a.x + xx * (Math.min(Math.abs(a.x-b.x), Math.abs(a.y - b.y))),
                            a.y + yy * (Math.min(Math.abs(a.x-b.x), Math.abs(a.y - b.y)))
                    );
                }
            }
            option_selected.draw(g, a, b);
        }
    }

    public static List<JRadioButton> option_btns = null;

    public static void initialize(){
        option_btns = new ArrayList<>();
        ButtonGroup group = new ButtonGroup();
        int cc = 1;
        for(CircleOption i :options){
            JRadioButton btn = new JRadioButton();
            try{
                btn.setIcon(new ImageIcon(ImageIO.read(Line.class.getResource("resources/Line" + cc + ".bmp"))));
                btn.setSelectedIcon(new ImageIcon(ImageIO.read(Line.class.getResource("resources/Line" + cc++ + "Selected.bmp"))));
            } catch (IOException e){
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "读取资源失败!\n" + e.toString());
                System.exit(1);
            }
            btn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    option_selected = i;
                }
            });
            option_btns.add(btn);
            group.add(btn);
        }
    }
}

class CircleOption{
    public Stroke stroke;
    public boolean fill;

    CircleOption(Stroke stroke, boolean fill){
        this.stroke = stroke;
        this.fill = fill;
    }

    public void draw(Graphics2D g, Point a, Point b){
        if(fill){
            g.fill(
                    new Ellipse2D.Double(
                            Math.min(a.x, b.x),
                            Math.min(a.y, b.y),
                            Math.max(a.x, b.x) - Math.min(a.x, b.x),
                            Math.max(a.y, b.y) - Math.min(a.y, b.y)
                    )
            );
        }else{
            g.fill(
                    stroke.createStrokedShape(
                            new Ellipse2D.Double(
                                    Math.min(a.x, b.x),
                                    Math.min(a.y, b.y),
                                    Math.max(a.x, b.x) - Math.min(a.x, b.x),
                                    Math.max(a.y, b.y) - Math.min(a.y, b.y)
                            )
                    )
            );
        }
    }
}
