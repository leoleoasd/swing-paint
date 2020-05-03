package tools;

import ui.PaintPanel;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.nio.Buffer;

public class EditTool extends AbstractNeedSelectionTool {

    Point operation_start;
    int operation = 0;
    static final int OP_NUL = 0;
    static final int OP_MOVE = 1;
    static final int OP_SCALE = 2;

    int op_param1 = 0;
    int op_param2 = 0;
    int op_param3 = 0;
    int op_param4 = 0;

    private static EditTool instance = null;

    public static void initialize(PaintPanel panel){
        instance = new EditTool(panel);
    }

    public static EditTool getInstance(){
        return instance;
    }

    protected EditTool(PaintPanel panel) {
        super(panel);
    }

    @Override
    public void paint(Graphics2D canvas) {
        canvas.drawImage(panel.img, 0, 0, null);
        switch(operation){
            case OP_NUL:
                canvas.drawImage(selecting_img, img_x,img_y,null);
                canvas.draw(
                        new BasicStroke(3, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{9}, 0).createStrokedShape(
                                new java.awt.Rectangle(
                                        img_x,img_y, selecting_img.getWidth(), selecting_img.getHeight()
                                )
                        )
                );
                break;
            case OP_MOVE:
                canvas.drawImage(selecting_img, img_x + op_param1,img_y + op_param2,null);
                canvas.draw(
                        new BasicStroke(3, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{9}, 0).createStrokedShape(
                                new java.awt.Rectangle(
                                        img_x + op_param1,img_y + op_param2, selecting_img.getWidth(), selecting_img.getHeight()
                                )
                        )
                );
                break;
            case OP_SCALE:
                if(selecting_img.getWidth() + op_param3 != 0 && selecting_img.getHeight() + op_param4 != 0){
                    Image ni = selecting_img.getScaledInstance(selecting_img.getWidth() + op_param3, selecting_img.getHeight() + op_param4, BufferedImage.SCALE_SMOOTH);
                    if(ni != null){
                        canvas.drawImage(ni, img_x, img_y,null);
                        canvas.draw(
                                new BasicStroke(3, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{9}, 0).createStrokedShape(
                                        new java.awt.Rectangle(
                                                img_x,img_y, ni.getWidth(null), ni.getHeight(null)
                                        )
                                )
                        );
                    }
                }
        }
    }

    @Override
    protected MouseAdapter makeMouseAdapter() {
        return new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if(e.getPoint().distance(img_x + selecting_img.getWidth(), img_y + selecting_img.getHeight()) <= 5){
                    operation = OP_SCALE;
                    op_param1 = 1;
                    op_param2 = 1;
                    op_param3 = 0;
                    op_param4 = 0;
                }else if(img_x < e.getX() && e.getX() < img_x + selecting_img.getWidth() && Math.abs(e.getY() - img_y - selecting_img.getHeight()) <= 5){
                    operation = OP_SCALE;
                    op_param1 = 0;
                    op_param2 = 1;
                    op_param3 = 0;
                    op_param4 = 0;
                }else if(img_y < e.getY() && e.getY() < img_y + selecting_img.getHeight() && Math.abs(e.getX() - img_x - selecting_img.getWidth()) <= 5){
                    operation = OP_SCALE;
                    op_param1 = 1;
                    op_param2 = 0;
                    op_param3 = 0;
                    op_param4 = 0;
                }else if(new Rectangle(img_x, img_y, selecting_img.getWidth(), selecting_img.getHeight()).contains(e.getPoint())){
                    operation = OP_MOVE;
                    op_param1 = 0;
                    op_param2 = 0;
                }
                operation_start = e.getPoint();
                panel.repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if(operation == OP_NUL){
                    panel.setTool(SelectingTool.getInstance(EditTool.getInstance()));
                    return;
                }else if(operation == OP_MOVE){
                    operation = OP_NUL;
                    img_x += (int) (e.getPoint().getX() - operation_start.getX());
                    img_y += (int) (e.getPoint().getY() - operation_start.getY());
                }else if(operation == OP_SCALE){
                    operation = OP_NUL;
                    if(selecting_img.getWidth() + op_param3 != 0 && selecting_img.getHeight() + op_param4 != 0){
                        Image ni = selecting_img.getScaledInstance(selecting_img.getWidth() + op_param3, selecting_img.getHeight() + op_param4, BufferedImage.SCALE_SMOOTH);
                        if(ni != null){
                            selecting_img = new BufferedImage(ni.getWidth(null), ni.getHeight(null), BufferedImage.TYPE_INT_RGB);
                            selecting_img.getGraphics().drawImage(ni,0,0,null);
                        }
                    }
                }

                panel.repaint();
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                if(operation == OP_MOVE){
                    op_param1 = (int) (e.getPoint().getX() - operation_start.getX());
                    op_param2 = (int) (e.getPoint().getY() - operation_start.getY());
                    panel.repaint();
                }else if(operation == OP_SCALE){
                    if(op_param1 == 1)
                        op_param3 = (int) (e.getPoint().getX() - operation_start.getX());
                    if(op_param2 == 1)
                        op_param4 = (int) (e.getPoint().getY() - operation_start.getY());
                    panel.repaint();
                }
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                if(e.getPoint().distance(img_x, img_y) <= 5){
                    panel.setCursor(new Cursor(Cursor.NW_RESIZE_CURSOR));

                }else if(e.getPoint().distance(img_x + selecting_img.getWidth(), img_y + selecting_img.getHeight()) <= 5){
                    panel.setCursor(new Cursor(Cursor.SE_RESIZE_CURSOR));
                }else if(img_x < e.getX() && e.getX() < img_x + selecting_img.getWidth() && Math.abs(e.getY() - img_y - selecting_img.getHeight()) <= 5){
                    panel.setCursor(new Cursor(Cursor.S_RESIZE_CURSOR));
                }else if(img_y < e.getY() && e.getY() < img_y + selecting_img.getHeight() && Math.abs(e.getX() - img_x - selecting_img.getWidth()) <= 5){
                    panel.setCursor(new Cursor(Cursor.E_RESIZE_CURSOR));
                }else if(new Rectangle(img_x, img_y, selecting_img.getWidth(), selecting_img.getHeight()).contains(e.getPoint())){
                    panel.setCursor(new Cursor(Cursor.HAND_CURSOR));
                }else{
                    panel.setCursor(Cursor.getDefaultCursor());
                }
            }
        };
    }

    @Override
    public void begin() {
        super.begin();
    }

    @Override
    public void finish() {
        super.finish();
        instance.img_x = 0;
        instance.img_y = 0;
        instance.op_param1 = 0;
        instance.op_param2 = 0;
        instance.op_param3 = 0;
        instance.op_param4 = 0;
        instance.operation = 0;
        instance.selecting_img = null;
        instance.selecting_start = null;
        instance.selecting_end = null;
        instance.operation_start = null;
    }

    public void delete() {
        selecting_img = null;
        panel.setTool(SelectingTool.getInstance(EditTool.getInstance()));
    }
}
