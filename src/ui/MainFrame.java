package ui;

import tools.Line;
import tools.Pencil;
import tools.Rectangle;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
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
        panel.setLayout(new BorderLayout());

        JPanel toolsBar = new JPanel();
        panel.add(toolsBar, BorderLayout.PAGE_START);
        PaintPanel canvas = new PaintPanel();
        panel.add(canvas, BorderLayout.CENTER);

        toolsBar.setLayout(new GridLayout(1,0));

        JButton undo = new JButton("撤销");
        undo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                canvas.undo();
            }
        });
        toolsBar.add(undo);

        JButton redo = new JButton("恢复");
        redo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                canvas.redo();
            }
        });
        toolsBar.add(redo);


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
        panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_SHIFT, InputEvent.SHIFT_DOWN_MASK), "SHIFT");
        panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_SHIFT, 0,true), "UNSHIFT");
        panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ctrl Z"), "SHIFT");
        panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ctrl Y"), "SHIFT");
        panel.getActionMap().put("SHIFT", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println(1);
                canvas.setShift(true);
            }
        });
        panel.getActionMap().put("UNSHIFT", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println(0);
                canvas.setShift(false);
            }
        });


        super.getContentPane().add(panel);
        super.pack();
        super.setVisible(true);
        super.setJMenuBar(menuBar);
        super.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent e){
                exit.doClick();
            }
        });
        super.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
                    System.out.println(1);
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
                    System.out.println(0);
                }
            }
        });
    }
}
