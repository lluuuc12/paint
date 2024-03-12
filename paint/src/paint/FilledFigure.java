package paint;

import java.awt.Color;

public abstract class FilledFigure extends Figure{
	private boolean filled;

	public FilledFigure(int x1, int y1, int x2, int y2, Color color, boolean filled) {
		super(x1, y1, x2, y2, color);
		this.filled = filled;
	}
	
	public boolean isFilled() {
        return filled;
    }

    public void setFilled(boolean filled) {
        this.filled = filled;
    }
}