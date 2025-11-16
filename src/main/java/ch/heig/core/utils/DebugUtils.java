/**
 *   Autheur: Theo Bensaci
 *   Date: 18:06 12.11.2025
 *   Description: debug utils
 */

package ch.heig.core.utils;

import java.awt.*;

public class DebugUtils {

    /**
     * Make a debug "UI" square
     * @param g graphics context
     * @param position position
     * @param offset offset
     * @param infos List of information to show
     */
    public static void drawEntityDebugInfo(Graphics g,Vector2f position,Vector2f offset, String[] infos){
        int debugRectW=0;
        int debugRectH=20;
        for (String s : infos) {
            debugRectH+=20;
            int l = s.length()*7;
            debugRectW=Math.max(debugRectW,l);
        }

        g.setColor(Color.YELLOW);
        ((Graphics2D) g).setStroke(new BasicStroke(2));
        g.drawLine((int)(position.x),(int)(position.y),(int)(position.x+offset.x),(int)(position.y+offset.y));


        g.setColor(Color.BLACK);
        g.fillRect((int)(position.x+offset.x),(int)(position.y+offset.y),debugRectW,debugRectH);
        g.setColor(Color.YELLOW);
        ((Graphics2D) g).setStroke(new BasicStroke(1));
        g.drawRect((int)(position.x+offset.x),(int)(position.y+offset.y),debugRectW,debugRectH);
        for (int i = 0; i < infos.length; i++) {
            g.drawString(infos[i], (int)(position.x+offset.x+5),(int)(position.y+offset.y+20)+(i*20));
        }
    }
}
