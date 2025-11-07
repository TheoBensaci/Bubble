package com.metheo.game.core.window;

import com.metheo.game.core.render.GameRender;
import com.metheo.game.core.utils.Input;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;


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

        addComponentListener(new ComponentListener()
        {
            @Override
            public void componentResized(ComponentEvent e) {
                int s = Math.min(e.getComponent().getWidth(),e.getComponent().getHeight())-10;
                GameCanvas.resizeCanavas(s,s);
                System.out.println("New SIZE : ");
                System.out.println(GameCanvas.actualWidth+"|"+GameCanvas.actualHeight);
                System.out.println(GameCanvas.getWidth()+"|"+GameCanvas.getHeight());
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
}
