package ui;

import tools.Pencil;

import javax.swing.*;
import java.awt.*;

public class PaintPanel extends JPanel {

    Class<?> selected_tool;

    Class<?>[] tools;

    Paint paint;

    PaintPanel(){
        tools = new Class<?>[]{
                Pencil.class
        };
        selected_tool = tools[0];

    }
}
