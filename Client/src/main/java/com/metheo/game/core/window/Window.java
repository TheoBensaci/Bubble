package com.metheo.game.core.window;

import com.metheo.game.core.render.GameRender;
import com.metheo.game.core.utils.Input;

import javax.swing.*;


public class Window extends JFrame {
    public GameRender gameCanvas;


    public Window(){
        // create a empty canvas
        gameCanvas = new GameRender();

        add(gameCanvas);

        // add input listener
        addKeyListener(new Input());
        addMouseListener(new Input());


        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
    }
}
