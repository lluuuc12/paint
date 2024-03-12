package paint;

import java.awt.Color;
import java.awt.Graphics;

public class Line extends Figure {

    public Line(int x1, int y1, int x2, int y2, Color color) {
        super(x1, y1, x2, y2, color);
    }
    
    @Override
    public void draw(Graphics g) {
        g.setColor(getColor());
        g.drawLine(getx1(), gety1(), getx2(), gety2());
    }
}

// este comentario está hecho en git
