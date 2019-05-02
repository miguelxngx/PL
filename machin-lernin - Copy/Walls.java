import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.ListIterator;

public class Walls {
	
	ArrayList<Line2D.Double> lines;
	
	public Walls() {
		lines = new ArrayList<Line2D.Double>();
	}
	
	public void addWall(int x1, int y1, int x2, int y2) {
		lines.add( new Line2D.Double(x1,y1,x2,y2));
	}
	
	public boolean possibleLine(Line2D.Double l) {
		ListIterator<Line2D.Double> itr = lines.listIterator();
		while(itr.hasNext()) {
			Line2D.Double aux = itr.next();
			if(aux.intersectsLine(l)) return false;
		}	
		return true;
	}
	
	public void paint(Graphics g) {
		ListIterator<Line2D.Double> itr = lines.listIterator();
		while(itr.hasNext()) {
			Line2D.Double aux = itr.next();
			g.setColor(Color.RED);
			g.drawLine((int)aux.x1, (int)aux.y1, (int)aux.x2, (int)aux.y2);
		}
	}
}
