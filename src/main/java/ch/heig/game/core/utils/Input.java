/**
 *   Autheur: Theo Bensaci
 *   Date: 18:06 12.11.2025
 *   Description: Class use to manage input
 */

package ch.heig.game.core.utils;

import ch.heig.game.core.Game;
import ch.heig.game.core.render.GameRender;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;


public class Input implements KeyListener, MouseListener {

    public class InputMap{
        public static final int UP = KeyEvent.VK_W;
        public static final int DOWN = KeyEvent.VK_S;
        public static final int LEFT = KeyEvent.VK_A;
        public static final int RIGHT = KeyEvent.VK_D;
        public static final int A = KeyEvent.VK_J;
        public static final int B = KeyEvent.VK_K;
        public static final int C = KeyEvent.VK_SPACE;
        public static final int MOUSE_LEFT = MouseEvent.BUTTON1;
        public static final int MOUSE_RIGHT = MouseEvent.BUTTON3;
        public static final int NUM_0 = KeyEvent.VK_0;
        public static final int NUM_1 = KeyEvent.VK_1;
        public static final int NUM_2 = KeyEvent.VK_2;
        public static final int NUM_3 = KeyEvent.VK_3;
        public static final int NUM_4 = KeyEvent.VK_4;
        public static final int NUM_5 = KeyEvent.VK_5;
        public static final int NUM_6 = KeyEvent.VK_6;
        public static final int NUM_7 = KeyEvent.VK_7;
        public static final int NUM_8 = KeyEvent.VK_8;
        public static final int NUM_9 = KeyEvent.VK_9;

    }

    private boolean _up=false;
    private boolean _down=false;
    private boolean _right=false;
    private boolean _left=false;
    private boolean _a=false;
    private boolean _b=false;
    private boolean _c=false;
    private boolean _mouseLeft=false;
    private boolean _mouseRight=false;
    private boolean _num_0=false;
    private boolean _num_1=false;
    private boolean _num_2=false;
    private boolean _num_3=false;
    private boolean _num_4=false;
    private boolean _num_5=false;
    private boolean _num_6=false;
    private boolean _num_7=false;
    private boolean _num_8=false;
    private boolean _num_9=false;

    private final Game _game;

    @Override
    public void keyTyped(KeyEvent e) {

    }

    public boolean getUp(){
        return _up;
    }
    public boolean getDown(){
        return _down;
    }
    public boolean getLeft(){
        return _left;
    }
    public boolean getRight(){
        return _right;
    }

    public boolean getA(){ return _a;}
    public boolean getB(){
        return _b;
    }
    public boolean getC(){
        return _c;
    }
    public boolean getMouseLeft(){
        return _mouseLeft;
    }
    public boolean getMouseRight(){
        return _mouseRight;
    }
    public boolean getNum0(){ return _num_0;}
    public boolean getNum1(){ return _num_1;}
    public boolean getNum2(){ return _num_2;}
    public boolean getNum3(){ return _num_3;}
    public boolean getNum4(){ return _num_4;}
    public boolean getNum5(){ return _num_5;}
    public boolean getNum6(){ return _num_6;}
    public boolean getNum7(){ return _num_7;}
    public boolean getNum8(){ return _num_8;}
    public boolean getNum9(){ return _num_9;}



    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()){
            case InputMap.UP -> _up=true;
            case InputMap.DOWN -> _down=true;
            case InputMap.RIGHT -> _right=true;
            case InputMap.LEFT -> _left=true;
            case InputMap.A -> _a=true;
            case InputMap.B -> _b=true;
            case InputMap.C -> _c=true;
            case InputMap.NUM_0 -> _num_0=true;
            case InputMap.NUM_1 -> _num_1=true;
            case InputMap.NUM_2 -> _num_2=true;
            case InputMap.NUM_4 -> _num_4=true;
            case InputMap.NUM_5 -> _num_5=true;
            case InputMap.NUM_6 -> _num_6=true;
            case InputMap.NUM_7 -> _num_7=true;
            case InputMap.NUM_8 -> _num_8=true;
            case InputMap.NUM_9 -> _num_9=true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()){
            case InputMap.UP -> _up=false;
            case InputMap.DOWN -> _down=false;
            case InputMap.RIGHT -> _right=false;
            case InputMap.LEFT -> _left=false;
            case InputMap.A -> _a=false;
            case InputMap.B -> _b=false;
            case InputMap.C -> _c=false;
            case InputMap.NUM_0 -> _num_0=false;
            case InputMap.NUM_1 -> _num_1=false;
            case InputMap.NUM_2 -> _num_2=false;
            case InputMap.NUM_4 -> _num_4=false;
            case InputMap.NUM_5 -> _num_5=false;
            case InputMap.NUM_6 -> _num_6=false;
            case InputMap.NUM_7 -> _num_7=false;
            case InputMap.NUM_8 -> _num_8=false;
            case InputMap.NUM_9 -> _num_9=false;
        }
    }

    public Point getMousePos(){
        GameRender r = _game.window.gameCanvas;
        PointerInfo pi = MouseInfo.getPointerInfo();
        if(pi==null)return new Point();

        Point p = pi.getLocation();
        Point q = r.getLocationOnScreen();
        p.x-= q.x;
        p.y-= q.y;

        // correct the point to the right scaling
        p.x= (int) (p.x/r.getRenderScale());
        p.y= (int) (p.y/r.getRenderScale());

        return p;
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        switch (e.getButton()){
            case InputMap.MOUSE_LEFT -> _mouseLeft=true;
            case InputMap.MOUSE_RIGHT -> _mouseRight=true;
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        switch (e.getButton()){
            case InputMap.MOUSE_LEFT -> _mouseLeft=false;
            case InputMap.MOUSE_RIGHT -> _mouseRight=false;
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    public Input(Game game){
        _game=game;
    }
}
