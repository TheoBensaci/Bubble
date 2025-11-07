package com.metheo.game.core.render;

import com.metheo.game.core.Game;
import com.metheo.game.core.utils.Input;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class GameRender extends JPanel implements ActionListener {

    public float x,y = 100;

    public Color backgroundColor=new Color(0x16162a);

    private final Semaphore _semaphore = new Semaphore(1);

    private final ArrayList<IDrawable> _drawables = new ArrayList<>();


    // GAME SETTINGS
    public static final int WIDTH=800;
    public static final int HEIGHT=800;
    public static final int REFRESH_RATE=3;

    public int actualWidth=WIDTH;
    public int actualHeight=HEIGHT;
    private float _renderScale=1f;


    private float _deltaTime;
    private long _updateStart;


    public GameRender(){
        actualWidth=WIDTH;
        actualHeight=HEIGHT;
        setBackground(new Color(0xff0055));
        setFocusable(false);


        // this timer will call the actionPerformed() method every DELAY ms
        GameRenderThread th = new GameRenderThread(this);
        th.start();
    }

    public void resizeCanavas(int targetWidth, int targetHeight){
        actualWidth=targetWidth;
        actualHeight=targetHeight;
        _renderScale= (float) actualWidth /WIDTH;
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(actualWidth, actualHeight);
    }

    @Override
    public Dimension getMinimumSize() {
        return getPreferredSize();
    }

    @Override
    public Dimension getMaximumSize() {
        return getPreferredSize();
    }

    public float getRenderScale(){
        return _renderScale;
    }

    public float getSizeComposation(){
        return WIDTH - actualWidth;
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

        AffineTransform renderTransform = new AffineTransform();
        //renderTransform.rotate(Math.PI/1,WIDTH/2,HEIGHT/2);
        renderTransform.translate(0, 0);
        renderTransform.scale(_renderScale,_renderScale);
        ((Graphics2D)g).transform(renderTransform);


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
        g.drawString("Game engine delta Time : "+Game.getGame().getDeltaTime()+"ms",10,40);
        g.drawString("Paint delta time : "+a+"ms",10,60);

        g.setColor(Color.ORANGE);
        Point p = Input.getMousePos();
        g.fillRect(p.x,p.y,10,10);
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
        g.fillRect(0,0,actualWidth+10,actualHeight+10);
    }

    /**
     * Get time stamp of the last update start (in nano)
     * @return
     */
    public long getLastUpdateStart(){
        return _updateStart;
    }

    public void registerDrawable(IDrawable drawable){
        addDrawables(drawable);
    }

    public void unregisterDrawable(IDrawable drawable){
        delDrawables(drawable);
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
