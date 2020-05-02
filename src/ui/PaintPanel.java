package ui;

import tools.*;
import tools.Rectangle;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.lang.reflect.InvocationTargetException;
import java.util.Stack;

public class PaintPanel extends JPanel {

    Class<?> selected_tool;
    Class<?>[] tools;

    Color front = Color.BLACK;
    Color background = Color.white;

    Stack<Image> undoStack = new Stack<>();
    Stack<Image> redoStack = new Stack<>();

    Image img;
    Tool drawing;
    Graphics2D imgG = null;
    boolean shift = false;

    PaintPanel(){
        super();
        tools = new Class<?>[]{
                Pencil.class,
                Line.class,
                Rectangle.class
        };
        selected_tool = tools[0];

        MouseAdapter a = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                try {
                    drawing = (Tool) selected_tool.getDeclaredConstructor().newInstance();
                    drawing.addPoint(e.getPoint());
                    repaint();
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(PaintPanel.this, "未知错误!\n" + ex.toString());
                    System.exit(1);
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                pushStack();
                drawing.addPoint(e.getPoint());
                imgG.setPaint(front);
                drawing.draw(imgG, shift);
                drawing = null;
                repaint();
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                drawing.addPoint(e.getPoint());
                repaint();
            }
        };

        addMouseListener(a);
        addMouseMotionListener(a);

        setPreferredSize(new Dimension(800,600));
    }

    @Override
    protected void paintComponent(Graphics g1) {
        if (img == null) {
            img = createImage(getSize().width, getSize().height);
            imgG = (Graphics2D) img.getGraphics();
            imgG.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            imgG.setPaint(background);
            imgG.fillRect(0, 0, getSize().width, getSize().height);
            imgG.setPaint(front);
        }
        g1.drawImage(img, 0, 0, null);
        ((Graphics2D)g1).setPaint(front);

        if (drawing != null) {
            drawing.draw((Graphics2D) g1, shift);
        }
    }

    public void pushStack(){
        BufferedImage copyOfImage = new BufferedImage(getSize().width,
                getSize().height, BufferedImage.TYPE_INT_RGB);
        Graphics g = copyOfImage.createGraphics();
        g.drawImage(img, 0, 0, getWidth(), getHeight(), null);
        undoStack.push(copyOfImage);
        redoStack.clear();
    }

    public void undo(){
        if(undoStack.size() > 0){
            System.out.println(123);
            Image pop = undoStack.pop();
            redoStack.push(copyImage(img));

            img = copyImage(pop);
            imgG = (Graphics2D) img.getGraphics();
            imgG.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            repaint();
        }
    }

    public void redo(){
        if(redoStack.size() > 0){
            System.out.println(123);
            Image pop = redoStack.pop();
            undoStack.push(copyImage(img));

            img = copyImage(pop);
            imgG = (Graphics2D) img.getGraphics();
            imgG.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            repaint();
        }
    }

    private BufferedImage copyImage(Image img) {
        BufferedImage copyOfImage = new BufferedImage(getSize().width,
                getSize().height, BufferedImage.TYPE_INT_RGB);
        Graphics g = copyOfImage.createGraphics();
        g.drawImage(img, 0, 0, getWidth(), getHeight(), null);
        return copyOfImage;
    }

    public void setShift(boolean shift) {
        this.shift = shift;
    }
}
