package tools;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public abstract class Tool {
    ArrayList<Point> points;

    public Tool(){
        points = new ArrayList<>();
    }

    public Tool(Point p){
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
