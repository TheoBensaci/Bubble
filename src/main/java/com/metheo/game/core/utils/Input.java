package com.metheo.game.core.utils;

import com.metheo.game.core.Game;

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

    private static boolean _up=false;
    private static boolean _down=false;
    private static boolean _right=false;
    private static boolean _left=false;
    private static boolean _a=false;
    private static boolean _b=false;
    private static boolean _c=false;
    private static boolean _mouseLeft=false;
    private static boolean _mouseRight=false;

    @Override
    public void keyTyped(KeyEvent e) {

    }

    public static boolean getUp(){
        return _up;
    }
    public static boolean getDown(){
        return _down;
    }
    public static boolean getLeft(){
        return _left;
    }
    public static boolean getRight(){
        return _right;
    }

    public static boolean getA(){ return _a;}
    public static boolean getB(){
        return _b;
    }
    public static boolean getC(){
        return _c;
    }
    public static boolean getMouseLeft(){
        return _mouseLeft;
    }
    public static boolean getMouseRight(){
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

    public static Point getMousePos(){
        if(Game.Instance==null || Game.Instance.Window==null){
            return new Point();
        }
        Point p = MouseInfo.getPointerInfo().getLocation();
        Point q = Game.Instance.Window.gameCanvas.getLocationOnScreen();
        p.x-=q.x;
        p.y-=q.y;
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
}
