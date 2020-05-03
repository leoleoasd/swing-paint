package tools;

import ui.PaintPanel;

import java.awt.*;
import java.awt.event.MouseAdapter;

public abstract class AbstractTool {

    protected PaintPanel panel;
    protected MouseAdapter adapter;

    protected AbstractTool(PaintPanel panel){
        this.panel = panel;
        this.adapter = null;
    }

    /**
     * Paint current state to canvas.
     * @param canvas The Graphics2D Object of the panel
     */
    public abstract void paint(Graphics2D canvas);

    /**
     * Get the Mouse adapter of this Tool.
     * @return MouseAdapter
     */
    public final MouseAdapter getMouseAdapter(){
        if(this.adapter == null){
            this.adapter = this.makeMouseAdapter();
        }
        return this.adapter;
    }

    /**
     * Make a Mouse adapter of this Tool.
     * @return MouseAdapter
     */
    protected abstract MouseAdapter makeMouseAdapter();

    /**
     * Finishes the tool, do the cleaning job.
     */
    public void finish(){}

    /**
     * Begins the tool, do the initializing job.
     */
    public void begin(){}
}
