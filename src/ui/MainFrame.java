package ui;

import com.sun.tools.javac.Main;
import tools.*;
import tools.pens.*;
import tools.pens.Rectangle;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.lang.reflect.Array;

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
        JMenuItem open = new JMenuItem("打开");
        menu.add(open);
        JMenuItem save = new JMenuItem("保存");
        menu.add(save);
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

        Box toolsBar = new Box(BoxLayout.X_AXIS);
        panel.add(toolsBar, BorderLayout.PAGE_START);
        PaintPanel canvas = new PaintPanel();
        panel.add(canvas, BorderLayout.CENTER);
        ButtonGroup pens_btn_group = new ButtonGroup();
        PenTool.initialize(canvas);
        SelectingTool.initialize(canvas);
        EditTool.initialize(canvas);
        TextTool.initialize(canvas);
        ImageTool.initialize(canvas);

        Box tools1 = new Box(BoxLayout.Y_AXIS);
        JButton undo = new JButton("撤销");
        undo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                canvas.undo();
            }
        });
        tools1.add(undo);

        JButton redo = new JButton("恢复");
        redo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                canvas.redo();
            }
        });
        tools1.add(redo);
        toolsBar.add(tools1);


        JButton select = new JButton("选择");
        select.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!canvas.selected_tool.getClass().equals(SelectingTool.class)){
                    pens_btn_group.clearSelection();
                    canvas.setTool(SelectingTool.getInstance(EditTool.getInstance()));
                }
            }
        });
        toolsBar.add(select);

        Box tools2 = new Box(BoxLayout.Y_AXIS);
        JButton text = new JButton("插入文字");
        text.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!canvas.selected_tool.getClass().equals(TextTool.class)){
                    pens_btn_group.clearSelection();
                    canvas.setTool(TextTool.getInstance());
                }
            }
        });
        tools2.add(text);

        JButton image = new JButton("插入图片");
        image.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!canvas.selected_tool.getClass().equals(ImageTool.class)){
                    pens_btn_group.clearSelection();
                    canvas.setTool(ImageTool.getInstance());
                }
            }
        });
        tools2.add(image);
        toolsBar.add(tools2);

        Box pens = new Box(BoxLayout.X_AXIS);
        pens.setBorder(BorderFactory.createTitledBorder("画笔"));
        toolsBar.add(pens);

        Box pen_options = new Box(BoxLayout.X_AXIS);
        pen_options.setBorder(BorderFactory.createTitledBorder("画笔选项"));

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
            btn.setPreferredSize(new Dimension(40,40));
            btn.addActionListener(new ActionListener() {
                @Override
                @SuppressWarnings("unchecked")
                public void actionPerformed(ActionEvent e) {
                    if(canvas.selected_tool == null || !canvas.selected_tool.getClass().equals(PenTool.class)){
                        canvas.setTool(PenTool.getInstance());
                    }
                    ((PenTool)canvas.selected_tool).selected_pen = (Class<? extends AbstractPen>) c;
                    pen_options.removeAll();
                    try {
                        Object buttons = c.getDeclaredField("option_btns").get(null);
                        java.util.List<JRadioButton> btns = (java.util.List<JRadioButton>) buttons;
                        for(JRadioButton btn:btns){
                            pen_options.add(btn);
                        }
                        MainFrame.this.validate();
                    } catch (NoSuchFieldException | IllegalAccessException ex) {
                        JOptionPane.showMessageDialog(null, "Error!\n" + e.toString());
                        System.exit(1);
                    }
                }
            });
            pens_btn_group.add(btn);
            pens.add(btn);
            if(c.equals(Pencil.class)){
                btn.doClick();
            }
        }
        toolsBar.add(pen_options);

        Box color_btns = new Box(BoxLayout.Y_AXIS);
        color_btns.setBorder(BorderFactory.createTitledBorder("颜色设置"));
        JButton front = new JButton("前景色");
        front.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Color newColor = JColorChooser.showDialog(
                        MainFrame.this,
                        "设置前景色",
                        canvas.front);
                canvas.front = newColor;
                canvas.repaint();
            }
        });
        color_btns.add(front);
        JButton back = new JButton("背景色");
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Color newColor = JColorChooser.showDialog(
                        MainFrame.this,
                        "设置前景色",
                        canvas.back);
                canvas.back = newColor;
                canvas.repaint();
            }
        });
        color_btns.add(back);
        toolsBar.add(color_btns);

        panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_SHIFT, InputEvent.SHIFT_DOWN_MASK), "SHIFT");
        panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_SHIFT, 0,true), "UNSHIFT");
        panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_BACK_SPACE, 0, false), "BACKSPACE");
        panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ctrl Z"), "undo");
        panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ctrl shift Z"), "redo");
        panel.getActionMap().put("SHIFT", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                canvas.setShift(true);
                canvas.repaint();
            }
        });
        panel.getActionMap().put("UNSHIFT", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                canvas.setShift(false);
                canvas.repaint();
            }
        });
        panel.getActionMap().put("BACKSPACE", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(canvas.selected_tool.getClass().equals(EditTool.class)){
                    ((EditTool)canvas.selected_tool).delete();
                }
            }
        });
        panel.getActionMap().put("undo", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                canvas.undo();
            }
        });
        panel.getActionMap().put("redo", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                canvas.redo();
            }
        });


        create.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(JOptionPane.showConfirmDialog(MainFrame.this, "将会丢失所有未保存的更改, 是否继续?") == JOptionPane.OK_OPTION) {
                    canvas.clear();
                }
            }
        });

        open.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {//Create a file chooser
                final JFileChooser fc = new JFileChooser();
                fc.setFileFilter(new FileNameExtensionFilter("图片","png", "jpg", "PNG", "JPG", "jpeg"));
                int ret = fc.showOpenDialog(panel);
                if(ret == JFileChooser.APPROVE_OPTION) {
                    try {
                        BufferedImage i = ImageIO.read(fc.getSelectedFile());
                        canvas.clear();
                        canvas.imgG.drawImage(i.getScaledInstance(800,600,BufferedImage.SCALE_SMOOTH),0,0,null);
                        canvas.repaint();
                        MainFrame.this.validate();
                        JOptionPane.showMessageDialog(null, "读取成功!");
                    } catch (IOException exception) {
                        exception.printStackTrace();
                        JOptionPane.showMessageDialog(null, "读取失败:" + exception.getMessage());
                    }
                }
            }
        });
        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                final JFileChooser fc2 = new JFileChooser();
                fc2.setFileFilter(new FileNameExtensionFilter("图片","png"));
                fc2.setSelectedFile(new File("output.png"));
                int ret = fc2.showSaveDialog(MainFrame.this);
                if(ret == JFileChooser.APPROVE_OPTION){
                    try {
                        ImageIO.write(canvas.img, "png", fc2.getSelectedFile());
                        MainFrame.this.validate();
                        JOptionPane.showMessageDialog(null, "保存成功!");
                    } catch (IOException exception) {
                        exception.printStackTrace();
                        JOptionPane.showMessageDialog(null, "保存失败:" + exception.getMessage());
                    }
                }
            }
        });


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
    }
}
