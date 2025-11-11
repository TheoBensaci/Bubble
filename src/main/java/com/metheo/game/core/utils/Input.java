package com.metheo.game.core.utils;

import com.metheo.game.core.Game;
import com.metheo.game.core.render.GameRender;

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
        }
    }

    public Point getMousePos(){
        GameRender r = _game.window.gameCanvas;
        Point p = MouseInfo.getPointerInfo().getLocation();
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
