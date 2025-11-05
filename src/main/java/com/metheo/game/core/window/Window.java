package ch.heig.game.core.window;

import ch.heig.game.core.utils.Input;
import ch.heig.game.core.render.GameRender;

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
