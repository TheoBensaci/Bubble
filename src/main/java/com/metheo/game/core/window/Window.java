package com.metheo.game.core.window;

import com.metheo.game.core.render.GameRender;
import com.metheo.game.core.utils.Input;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


public class Window extends JFrame {
    public GameRender GameCanvas;
    private JPanel panel = new JPanel( );


    public Window(){
        super("Bubble");

        panel.setLayout(new GridBagLayout());

        // create a empty canvas
        GameCanvas = new GameRender();

        panel.add(GameCanvas);

        add(panel);

        // add input listener
        addKeyListener(new Input());
        addMouseListener(new Input());
        addWindowStateListener(e -> {
            resizeGameCanavas(e.getComponent().getWidth(),e.getComponent().getHeight());
        });

        addComponentListener(new ComponentListener()
        {
            @Override
            public void componentResized(ComponentEvent e) {
                resizeGameCanavas(e.getComponent().getWidth(),e.getComponent().getHeight());
            }

            @Override
            public void componentMoved(ComponentEvent e) {

            }

            @Override
            public void componentShown(ComponentEvent e) {

            }

            @Override
            public void componentHidden(ComponentEvent e) {

            }

        });


        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void resizeGameCanavas(int newWindowWidth, int newWindowHeight){
        int s = Math.min(newWindowWidth,newWindowHeight)-10;
        GameCanvas.resizeCanavas(s,s);
    }
}
