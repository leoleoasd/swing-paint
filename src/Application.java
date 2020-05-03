import ui.MainFrame;

import javax.swing.*;

public class Application {
    public static void main(String[] args){
        System.setProperty("apple.laf.useScreenMenuBar", "true");

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MainFrame();
            }
        });
    }
}
