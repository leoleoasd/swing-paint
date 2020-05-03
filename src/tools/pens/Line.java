package tools.pens;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Line2D;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Line extends AbstractPen {

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
            g.fill(
                    option_selected.createStrokedShape(
                            new Line2D.Double(
                                    points.get(0),
                                    points.get(points.size() - 1)
                            )
                    )
            );
        }
    }

    public static List<JRadioButton> option_btns = null;

    public static void initialize(){
        option_btns = new ArrayList<>();
        ButtonGroup group = new ButtonGroup();
        int cc = 1;
        for(Stroke i :options){
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
