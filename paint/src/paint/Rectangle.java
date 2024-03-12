package paint;

import java.awt.Color;
import java.awt.Graphics;

public class Rectangle extends FilledFigure {

    public Rectangle(int x1, int y1, int x2, int y2, Color color, boolean filled) {
    	super(x1, y1, x2, y2, color, filled);
    }
    
    @Override
    public void draw(Graphics g) {
        g.setColor(getColor());
        int x = Math.min(getx1(), getx2());
        int y = Math.min(gety1(), gety2());
        int width = Math.abs(getx2() - getx1());
        int height = Math.abs(gety2() - gety1());
        if (isFilled()) {
        	g.fillRect(x, y, width, height);
        } else {
            g.drawRect(x, y, width, height);
        }
    }
}