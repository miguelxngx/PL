import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.ArrayList;
//import com.mathworks.toolbox.javabuilder.*;

import javax.swing.JFrame;

public class Statistics extends JFrame{
	public boolean signal;
	public MapInteractions map;
	public Dot goal;
	public ArrayList<Point> route;
	public double distance;
	public int generacion=1;
	public MathPlotter grapher;
	public ArrayList<Point2D.Double> distances;
	public int movements;
	public double tolerance;
	public Statistics(double tolerance) {
		super();
		this.tolerance = tolerance;
        signal = false;
        //goal = new Dot(40, 40, 40, 40);
        map = new MapInteractions();
        this.route = new ArrayList<Point>();
        setSize(900, 900);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        grapher = new MathPlotter();
        distances = new ArrayList<Point2D.Double>();
	}
	
	public void setDistance(int gen, double distance) {
		this.distance = distance;
		distances.add(new Point2D.Double((double)gen+1, distance));
		this.grapher.addSeries(gen+1,distance, distances, tolerance);
		generacion++;
	}
	
	public void paint(Graphics g) {
		map.render(g);
		paintBestRoute(g);
		g.setColor(Color.black);
		g.fillRect(60,60,150,30);
		g.setColor(Color.white);
		g.drawString(Double.toString(this.distance), 80, 80);
	}
	
	public void setBestRoute(ArrayList<Point> route, int movements) {
		this.route = route;
		this.movements = movements;
	}
	
	public void paintBestRoute(Graphics g) {
	    Graphics2D g2 = (Graphics2D) g;
		int n=route.size();
		for(int i=1; i<movements; i++) {
			g2.setStroke(new BasicStroke(10));
			g2.drawLine(route.get(i-1).x, route.get(i-1).y, route.get(i).x, route.get(i).y);
		}
	}

}
