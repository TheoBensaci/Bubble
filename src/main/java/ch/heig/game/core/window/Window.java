/**
 *   Autheur: Theo Bensaci
 *   Date: 18:06 12.11.2025
 *   Description: Class use to manage the window where the ame is rendered
 */

package ch.heig.game.core.window;

import ch.heig.game.core.Game;
import ch.heig.game.core.render.GameRender;
import ch.heig.game.core.utils.Input;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


public class Window extends JFrame {
    public GameRender gameCanvas;
    private boolean _close=false;
    public final Game game;
    private JPanel _panel = new JPanel( );


    public Window(Game game, String title){
        super(title);

        this.game=game;

        _panel.setLayout(new GridBagLayout());

        // create a empty canvas
        gameCanvas = new GameRender(this.game);

        _panel.add(gameCanvas);

        add(_panel);

        // create input
        game.input=new Input(game);

        // add input listener
        addKeyListener(game.input);
        addMouseListener(game.input);
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


        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener( new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent we) {
                game.close();
            }
        } );
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    /**
     * resize the game canavas to fit new window size
     * @param newWindowWidth new window width
     * @param newWindowHeight new window height
     */
    private void resizeGameCanavas(int newWindowWidth, int newWindowHeight){
        int s = Math.min(newWindowWidth,newWindowHeight)-10;
        gameCanvas.resizeGameRender(s,s);
    }
}
