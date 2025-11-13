/**
 *   Autheur: Theo Bensaci
 *   Date: 18:06 12.11.2025
 *   Description: panel use to render the game
 */

package ch.heig.game.core.render;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;

import javax.swing.JPanel;

import ch.heig.game.core.Entity;
import ch.heig.game.core.Game;

public class GameRender extends JPanel implements ActionListener {

    public Color backgroundColor=new Color(0x16162a);                   // game render background colore

    private final Semaphore _semaphore = new Semaphore(1);           // semphore use to protect the list of drawables

    private final ArrayList<IDrawable> _drawables = new ArrayList<>();      // list of entity to be render

    public final Game game;                                                 // game linked to this game render

    public int actualGroupRender=0;                                         // actual group of entity rendered


    // GAME SETTINGS
    public static final int WIDTH=800;                                      // default render width
    public static final int HEIGHT=800;                                     // default render height

    public int actualWidth=WIDTH;                                           // actual render width
    public int actualHeight=HEIGHT;                                         // actual render height
    private float _renderScale=1f;                                          // actual render scale

    private long _updateStart;                                              // last render update time, use to monitor render perofmance

    private GameRenderThread _th;                                           // thread use to render, need to be thread to avoid slowing down the entier UI


    public GameRender(Game game){
        this.actualWidth=WIDTH;
        this.actualHeight=HEIGHT;
        setBackground(new Color(0xff0055));
        setFocusable(false);

        this.game = game;


        // this timer will call the actionPerformed() method every DELAY ms
        _th = new GameRenderThread(this);
        _th.start();
    }

    /**
     * Resize the game render
     * @param targetWidth target width
     * @param targetHeight target height
     */
    public void resizeGameRender(int targetWidth, int targetHeight){
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

    /**
     * get the actual render scale
     * @return
     */
    public float getRenderScale(){
        return _renderScale;
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
                if(drawable instanceof Entity ent){
                    if(ent.getGroup()!=actualGroupRender)continue;
                }
                drawable.draw(g);
            }
            _semaphore.release();
        } catch (InterruptedException e) {
            System.out.println(e.toString());
        }
        g.setColor(Color.ORANGE);
        float a = (float)(System.nanoTime()-_updateStart)/1000000;
        g.drawString("Game engine delta Time : "+game.getDeltaTime()+"ms",10,40);
        g.drawString("Paint delta time : "+a+"ms",10,60);
        g.drawString("Number of IUpdateble : "+game.getNumberOfUpdatable(),10,80);

        g.setColor(Color.ORANGE);
        Point p = game.input.getMousePos();
        g.fillRect(p.x-5,p.y-5,10,10);

    }


    public class GameRenderThread extends Thread{
        private final GameRender _render;
        private boolean _isRunning=true;
        public GameRenderThread(GameRender render){
            _render=render;
        }

        @Override
        public void run() {
            super.run();
            while (_isRunning) {
                _render.repaint();
            }
        }

        public void close(){
            _isRunning=false;
        }
    }



    // -------------- DEFAULT GRAPHIC --------------

    /**
     * Draw a background color
     * @param g
     */
    private void drawBackground(Graphics g){
        g.setColor(backgroundColor);
        g.fillRect(0,0,actualWidth+10,actualHeight+10);
    }

    /**
     * Register a drawable
     * @param drawable object (wich implement IDrawable) to be register
     */
    public void registerDrawable(IDrawable drawable){
        addDrawables(drawable);
    }

    /**
     * Unregister a drawable
     * @param drawable object (wich implement IDrawable) to be unregister
     */
    public void unregisterDrawable(IDrawable drawable){
        delDrawables(drawable);
    }

    /**
     * Add a new drawable
     * @param drawable
     */
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

    /**
     * remove a new drawable
     * @param drawable
     */
    private void delDrawables(IDrawable drawable){
        try {
            _semaphore.acquire();
            _drawables.remove(drawable);
            _semaphore.release();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * Close the game render
     */
    public void close(){
        _th.close();
    }


}
