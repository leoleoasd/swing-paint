package ui;

import tools.AbstractTool;
import tools.PenTool;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Stack;
import tools.Util;

public class PaintPanel extends JPanel {
    public AbstractTool selected_tool;

    public Color front = Color.BLACK;
    public Color back = Color.white;

    Stack<BufferedImage> undoStack = new Stack<>();
    Stack<BufferedImage> redoStack = new Stack<>();

    public BufferedImage img;
    public Graphics2D imgG;
    public boolean shift = false;

    PaintPanel() {
        super();

        setPreferredSize(new Dimension(800,600));
        setBorder(BorderFactory.createLineBorder(Color.green));

        img = new BufferedImage(800,600,BufferedImage.TYPE_INT_RGB);
        imgG = (Graphics2D) img.getGraphics();
        imgG.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        imgG.setPaint(back);
        imgG.fillRect(0, 0, 800,600);
        imgG.setPaint(front);
    }

    PaintPanel(BufferedImage img1) {
        super();

        setPreferredSize(new Dimension(img1.getWidth(),img1.getHeight()));

        img = new BufferedImage(img1.getWidth(),img1.getHeight(),BufferedImage.TYPE_INT_RGB);
        imgG = (Graphics2D) img.getGraphics();
        imgG.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        imgG.setPaint(back);
        imgG.drawImage(img1,0,0,null);
        imgG.setPaint(front);
    }

    @Override
    protected void paintComponent(Graphics g1) {
        ((Graphics2D)g1).setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        selected_tool.paint(((Graphics2D)g1));
    }

    public void pushStack(){
        undoStack.push(Util.copyImage(img));
        redoStack.clear();
    }

    public void undo(){
        if(undoStack.size() > 0){
            if(this.selected_tool.getClass() != PenTool.class)
                this.setTool(PenTool.getInstance());
            Image pop = undoStack.pop();
            redoStack.push(Util.copyImage(img));

            imgG.drawImage(pop, 0,0,null);
            repaint();
        }
    }

    public void redo(){
        if(redoStack.size() > 0){
            if(this.selected_tool.getClass() != PenTool.class)
                this.setTool(PenTool.getInstance());
            Image pop = redoStack.pop();
            undoStack.push(Util.copyImage(img));

            imgG.drawImage(pop, 0,0,null);
            repaint();
        }
    }

    public void setShift(boolean shift) {
        this.shift = shift;
    }

    public void setTool(AbstractTool tool){
        System.out.print("Changing tool to ");
        System.out.println(tool.getClass());
        if(this.selected_tool != null){
            this.selected_tool.finish();
            removeMouseListener(this.selected_tool.getMouseAdapter());
            removeMouseMotionListener(this.selected_tool.getMouseAdapter());
        }

        this.selected_tool = tool;
        this.selected_tool.begin();
        addMouseListener(selected_tool.getMouseAdapter());
        addMouseMotionListener(selected_tool.getMouseAdapter());

        repaint();
    }

    public void clear(){
        this.front = Color.BLACK;
        this.back = Color.white;
        this.imgG.setPaint(Color.white);
        this.imgG.fill(new Rectangle(0,0,this.img.getWidth(), this.img.getHeight()));
        repaint();
    }
}
