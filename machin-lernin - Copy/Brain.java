import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.Random;

public class Brain {
	private ArrayList<Point> points;
	private Random al;
	public int step = 0;

	public int size;
	public MapInteractions map;
	
	public int x;
	public int y;
	public int height;
	public int width;
	public int numSteps;
	
	public Brain(int size, MapInteractions map, int x, int y) {
		points = new ArrayList<Point>();
		al = new Random(System.nanoTime());
		this.size=size;
		this.map = map;
		this.width = 20;
		this.height = 20;
		this.x = x;
		this.y = y;
		this.numSteps=1;
		points.add(new Point(this.x, this.y));
		setRoute();
	}
	
	public Brain(MapInteractions map) {
		this.size = 0;
		this.points = new ArrayList<Point>();
		al = new Random(System.nanoTime());
		this.map = map;
		this.width = 20;
		this.height = 20;
		this.x = (900/2)-(this.width/2);
		this.y = 850-this.height;
	}
	
	public ArrayList<Point> getPoints(){
		return this.points;
	}
	
	public void setPoints(ArrayList<Point> clone) {
		int n=clone.size();
		this.numSteps = n;
		 for (int i = 0; i < n; i++) {
			 Point aux = new Point();
			 aux.x = clone.get(i).x;
			 aux.y = clone.get(i).y;
		      this.points.add(aux);
		      this.size++;
		 }
	}
	
	public void setRoute() {
		int n = size;
		int i;
		Point aux;
		double randomT;
		//int randomR;
		int randomX;
		int randomY;
		double val = Math.PI;
		double direction=0;
		for(i=0;i<n;i++) {
		do{
			al.setSeed(System.nanoTime());
			double res = al.nextDouble() * val;
			randomT = direction+res;//Math.sqrt(res);
			if(randomT> direction+(7*(Math.PI/8))) {
				direction+=Math.PI/2;
			}else if(randomT<direction-(7*(Math.PI/8))){
				direction-=Math.PI/2;
			}
			randomX = (int)((double)5 * Math.cos(randomT));
			randomY = -(int)((double)5 * Math.sin(randomT));
			aux = new Point();
			aux.x = randomX;
			aux.y = randomY;
			//System.out.println(this.y);
		}while(!this.map.possibleLine(new Line2D.Double(this.x, this.y, this.x+randomX, this.y+randomY)));
		/*if(this.numSteps>2) {
			if(this.map.possibleLine(new Line2D.Double(points.get(numSteps-2).x, points.get(numSteps-2).y,points.get(numSteps-1).x+randomX, points.get(numSteps-1).y+randomY))
					&& distanceBetweenP1P3lessthanP1P2P3(points.get(numSteps-2), points.get(numSteps-1), new Point(points.get(numSteps-1).x+randomX, points.get(numSteps-1).y+randomY))
			){
				points.set(numSteps-1, new Point(points.get(numSteps-1).x+randomX, points.get(numSteps-1).y+randomY));
				this.x = points.get(numSteps-1).x;
				this.y = points.get(numSteps-1).y;
			}else {
				this.x += randomX;
				this.y += randomY;
				numSteps++;
				this.points.add(new Point(this.x, this.y));
			}
		}else {
			this.x += randomX;
			this.y += randomY;
			numSteps++;
			this.points.add(new Point(this.x, this.y));
		}*/
		this.x += randomX;
		this.y += randomY;
		numSteps++;
		this.points.add(new Point(this.x, this.y));
		}
	}
	
	public boolean distanceBetweenP1P3lessthanP1P2P3(Point a, Point b, Point c) {
		return 	Math.sqrt(
					Math.pow(a.x-c.x, 2) + Math.pow(a.y-c.y, 2)
				)
				<
				Math.sqrt(
					Math.pow(a.x-b.x, 2) + Math.pow(a.y-b.y, 2)
				) + Math.sqrt(
					Math.pow(b.x-c.x, 2) + Math.pow(b.y-c.y, 2)
				);
	}
	
	public Brain clone() {
		Brain clone = new Brain(this.map);
		clone.setPoints(points);
		return clone;
	}
	
	
	public void mutate3() {
		double mutationRate = 0.01;
		int i;
		int randomX, randomY;
		for(i=1; i<numSteps;i++) {
			double rand = al.nextDouble();
			if(rand<mutationRate) {
				al.setSeed(System.nanoTime());
				randomX = al.nextInt(31)-15;
				randomY = -(al.nextInt(31)-15);
				points.get(i).x += randomX;
				points.get(i).y += randomY;
			}
			if(i<numSteps-1) {
				if(this.map.possibleLine(new Line2D.Double(points.get(i-1).x, points.get(i-1).y, points.get(i+1).x, points.get(i+1).y))){
					points.remove(i);
					i--;
					numSteps--;
				}
			}
		}
	}
	
	public void analizeRoute() {
		
	}
}
