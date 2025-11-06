package com.metheo.game.core.render;

import com.metheo.game.core.Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class GameRender extends JPanel implements ActionListener {

    public float x,y = 100;

    public Color backgroundColor=new Color(0x16162a);

    private final Semaphore _semaphore = new Semaphore(1);

    private final ArrayList<IDrawable> _drawables = new ArrayList<>();


    // GAME SETTINGS
    public static final int WIDTH=1020;
    public static final int HEIGHT=800;
    public static final int REFRESH_RATE=3;

    private float _deltaTime;
    private long _updateStart;


    public GameRender(){
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(new Color(0xff0055));
        setFocusable(false);


        // this timer will call the actionPerformed() method every DELAY ms
        GameRenderThread th = new GameRenderThread(this);
        th.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        _updateStart=System.nanoTime();
        drawBackground(g);

        // apply tranform, use to creat screen shake
        /*
        AffineTransform at = new AffineTransform();
        //at.rotate(2);
        at.rotate(Math.PI/4,WIDTH / 2, HEIGHT / 2);
        ((Graphics2D) g).transform(at);
        */

        try {
            _semaphore.acquire();
            for (IDrawable drawable : _drawables){
                drawable.draw(g);
            }
            _semaphore.release();
        } catch (InterruptedException e) {
            System.out.println(e.toString());
        }
        g.setColor(Color.ORANGE);
        float a = (float)(System.nanoTime()-_updateStart)/1000000;
        g.drawString("Game engine delta Time : "+Game.getDeltaTime()+"ms",10,40);
        g.drawString("Paint delta time : "+a+"ms",10,60);
    }


    public class GameRenderThread extends Thread{
        private final GameRender _render;
        public GameRenderThread(GameRender render){
            _render=render;
        }

        @Override
        public void run() {
            super.run();
            while (true) {
                _render.repaint();
            }
        }
    }



    // -------------- DEFAULT GRAPHIC --------------

    /**
     * Draw a backrgound color
     * @param g
     */
    private void drawBackground(Graphics g){
        g.setColor(backgroundColor);
        g.fillRect(0,0,WIDTH,HEIGHT);
    }

    /**
     * Get time stamp of the last update start (in nano)
     * @return
     */
    public long getLastUpdateStart(){
        return _updateStart;
    }

    public static void registerDrawable(IDrawable drawable){
        if(Game.Instance==null || Game.Instance.Window==null)return;
        Game.Instance.Window.gameCanvas.addDrawables(drawable);
    }

    public static void unregisterDrawable(IDrawable drawable){
        if(Game.Instance==null || Game.Instance.Window==null)return;
        Game.Instance.Window.gameCanvas.delDrawables(drawable);
    }

    private void addDrawables(IDrawable drawable){
        // add in to the right order
        if(_drawables.isEmpty()){
            try {
                _semaphore.acquire();
                _drawables.add(drawable);
                _semaphore.release();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return;
        }

        if(_drawables.getLast().getLayer()>drawable.getLayer()){
            try {
                _semaphore.acquire();
                for (int i = _drawables.size()-1; i >= 0; i--) {
                    if(_drawables.get(i).getLayer()<drawable.getLayer()){
                        _drawables.add(i,drawable);
                        return;
                    }
                }
                _drawables.addFirst(drawable);
                _semaphore.release();
                return;
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }
        try {
            _semaphore.acquire();
            _drawables.add(drawable);
            _semaphore.release();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void delDrawables(IDrawable drawable){
        try {
            _semaphore.acquire();
            _drawables.remove(drawable);
            _semaphore.release();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


}
