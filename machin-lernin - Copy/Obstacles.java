import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.ListIterator;

public class Obstacles {
	
	ArrayList<Rectangle> rects;
	
	public Obstacles() {
		this.rects = new ArrayList<Rectangle>();
	}

	public void addObstacle(int x, int y, int width, int heigth) {
		this.rects.add(new Rectangle(x,y,width,heigth));
	}
	
	public boolean intersects(Line2D route) {
		ListIterator<Rectangle> itr = rects.listIterator();
		while(itr.hasNext()) {
			Rectangle aux = itr.next();
			if(aux.intersectsLine(route))return true;
		}
		return false;
	}
	
	public void paint(Graphics g) {
		ListIterator<Rectangle> itr = rects.listIterator();
		while(itr.hasNext()) {
			Rectangle aux = itr.next();
			g.fillRect(aux.x, aux.y, aux.width, aux.height);
		}
	}
}
