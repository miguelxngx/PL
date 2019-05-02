import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;

import javax.sound.sampled.Line;

public class Pasillo {
	int x1;
	int y1;
	int x2;
	int y2;
	Rectangle rect;
	int rectX1;
	int rectWidth;
	int rectY1;
	int rectHeigth;
	public Pasillo(int x1, int y1, int x2, int y2) {
		this.x1 = x1;
		this.x2 = x2;
		this.y1 = y1;
		this.y2 = y2;
		setRectangle();
	}
	
	public void setRectangle() {
		this.rectX1 = x1-50;
		this.rectWidth = x2-x1+50+50;
		this.rectY1 = y1 - 50;
		this.rectHeigth = y2 - y1 + 50 + 50;
		rect = new Rectangle(rectX1, rectY1, rectWidth, rectHeigth);
	}
	
	public Boolean contains(Rectangle r) {
		return rect.contains(r);
	}
	
	
	public Rectangle2D union(Rectangle r) {
		return this.rect.createUnion(r);
	}
	
	public void paint(Graphics g) {
		g.setColor(Color.BLACK);
		g.fillRect(rectX1, rectY1, rectWidth, rectHeigth);
		g.setColor(Color.red);
		g.drawOval(x1, y1, 4, 4);
		g.drawOval(x2, y2, 4, 4);
	}
}
