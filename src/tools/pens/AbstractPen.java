package tools.pens;

import java.awt.*;
import java.util.ArrayList;

public abstract class AbstractPen {
    protected ArrayList<Point> points;

    public AbstractPen(){
        points = new ArrayList<>();
    }

    public AbstractPen(Point p){
        this();
        if(p != null) {
            points.add(p);
        }
    }

    public void addPoint(Point p){
        points.add(p);
    }

    public abstract void draw(Graphics2D g, boolean shift);
}
