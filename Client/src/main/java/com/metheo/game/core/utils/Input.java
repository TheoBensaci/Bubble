package com.metheo.game.core.utils;

import com.metheo.game.core.Game;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Input implements KeyListener, MouseListener {

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
            case KeyEvent.VK_W -> _up=true;
            case KeyEvent.VK_S -> _down=true;
            case KeyEvent.VK_D -> _right=true;
            case KeyEvent.VK_A -> _left=true;
            case KeyEvent.VK_J -> _a=true;
            case KeyEvent.VK_K -> _b=true;
            case KeyEvent.VK_R -> _c=true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()){
            case KeyEvent.VK_W -> _up=false;
            case KeyEvent.VK_S -> _down=false;
            case KeyEvent.VK_D -> _right=false;
            case KeyEvent.VK_A -> _left=false;
            case KeyEvent.VK_J -> _a=false;
            case KeyEvent.VK_K -> _b=false;
            case KeyEvent.VK_R -> _c=false;
        }
    }

    public static Point getMousePos(){
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
            case MouseEvent.BUTTON1 -> _mouseLeft=true;
            case MouseEvent.BUTTON3 -> _mouseRight=true;
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        switch (e.getButton()){
            case MouseEvent.BUTTON1 -> _mouseLeft=false;
            case MouseEvent.BUTTON3 -> _mouseRight=false;
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
