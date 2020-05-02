package ui;

import tools.Line;
import tools.Pencil;
import tools.Rectangle;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.ArrayList;

public class MainFrame extends JFrame {
    public MainFrame(){
        super("画图");
        //Create the menu bar.
        JMenuBar menuBar = new JMenuBar();

        //Build the first menu.
        JMenu menu = new JMenu("文件");
        menuBar.add(menu);

        JMenuItem create = new JMenuItem("新建");
        menu.add(create);
        menu.add(new JSeparator());

        JMenuItem exit = new JMenuItem("退出");
        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        menu.add(exit);


        JPanel panel = new JPanel();

        JPanel toolsBar = new JPanel();
        panel.add(toolsBar);
        PaintPanel canvas = new PaintPanel();

        toolsBar.setLayout(new GridLayout(1,0));
        ButtonGroup tools = new ButtonGroup();
        for(Class<?> c: new Class<?>[]{Pencil.class, Line.class, Rectangle.class}){
            JRadioButton btn = new JRadioButton();
            try{
                btn.setIcon(new ImageIcon(ImageIO.read(c.getResource("resources/" + c.getSimpleName() + ".bmp"))));
                btn.setSelectedIcon(new ImageIcon(ImageIO.read(c.getResource("resources/" + c.getSimpleName() + "Selected.bmp"))));
            } catch (IOException e){
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "读取资源失败!\n" + e.toString());
                System.exit(1);
            }
            btn.setSize(10,10);
            btn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    canvas.selected_tool = c;
                    System.out.println(c.getName());
                }
            });
            tools.add(btn);
            toolsBar.add(btn);
            if(c.getName().equals("tools.Pencil")){
                btn.doClick();
            }
        }

        super.getContentPane().add(panel);
        super.pack();
        super.setVisible(true);
        super.setResizable(false);
        super.setJMenuBar(menuBar);
        super.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent e){
                exit.doClick();
            }
        });
        super.setResizable(false);
    }
}
