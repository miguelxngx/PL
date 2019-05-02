import java.awt.*;
import java.util.Comparator;
import java.awt.Rectangle;
import java.awt.geom.Line2D;

public class Dot {
	private int x;
	private int y;
	public Point start;
	private int width;
	private int height;
	Brain brain;
	
	boolean dead = false;
	boolean reachedGoal = false;
	boolean isBest = false;
	boolean hitWall = false;
	boolean hitObst = false;
	
	double fitness = 0;
	
	public double closestDistance;
	public int highestY=900;
	public int acum = 0;
	public int velocity = 0;
	public int totalX=0;
	public int totalY=0;
	
	public double totalDistance;
	
	public MapInteractions map;
	
	public Dot(Point start, MapInteractions map) {
		this.width = 20;
		this.height = 20;
		this.x = start.x;
		this.y = start.y;
		this.map = map;
		this.start = start;
		brain = new Brain(1000, map, this.x, this.y);
		closestDistance = Math.sqrt(Math.pow((x-450), 2)+Math.pow(y-80, 2));
		totalDistance=0.0;
	}
	public Dot(int x, int y, int width, int height) {
		this.width = width;
		this.height = height;
		this.x = (900/2)-(width/2);
		this.y = 20+height;
		totalDistance = 0.0;
	}
	
	public void move() {
		if(!dead && brain.step < brain.numSteps) {
			if(brain.step>0) {
				totalDistance += Math.sqrt(Math.pow(this.x - brain.getPoints().get(brain.step).x,2)+Math.pow(this.y - brain.getPoints().get(brain.step).y,2));
			}
			if(this.map.possibleLine(new Line2D.Double(this.x, this.y, brain.getPoints().get(brain.step).x, brain.getPoints().get(brain.step).y ))) {
				if(this.map.reachedGoal(new Line2D.Double(this.x, this.y, brain.getPoints().get(brain.step).x, brain.getPoints().get(brain.step).y ))){
					this.x = this.map.goalX;
					this.y = this.map.goalY;
					brain.getPoints().set(brain.step, new Point(this.x, this.y));
					reachedGoal = true;
				}else {
					this.x = brain.getPoints().get(brain.step).x;
					this.y = brain.getPoints().get(brain.step).y;
				}
			}else {
				dead = true;
			}
			brain.step++;
		}else {
			dead = true;
		}
	}
	
	public void update() {
		if (!dead && !reachedGoal) {
		      move();
		      if (x< 2|| y<40 || x>875-2 || y>875) {//if near the edges of the window then kill it 
		    	  dead = true;
		    	  hitWall = true;
		      } else if (this.map.reachedGoal(this.getBounds())) {//if reached goal
		        reachedGoal = true;
		        this.x = this.map.goalX;
				this.y = this.map.goalY;
				//brain.getPoints().set(brain.step, new Point(this.x, this.y));
		      }
		}
	}
	public void calculateVelocity() {
		velocity = acum/brain.step;
	}
	public void calculateFitness() {
		if(reachedGoal) {
			fitness = 1.0/16.0 + (1000000.0/(double)Math.pow(totalDistance,2));
		}else {
			double distanceToGoal = Math.sqrt(Math.pow((x-this.map.goalX), 2)+Math.pow(y-this.map.goalY, 2));
			fitness = 1/(double)Math.pow(distanceToGoal, 2);
		}
	}
	public Dot babyPls() {
		Dot baby = new Dot(this.start, map);
		baby.brain = new Brain(this.map);
		baby.brain = this.brain.clone();
		return baby;
	}
	public void paint(Graphics g) {
		if(reachedGoal) {
			g.setColor(Color.green);
			g.fillOval(x, y, width, height);
		}else if(isBest){
			g.setColor(Color.blue);
			g.fillOval(x, y, width, height);
		}else {
			g.setColor(Color.DARK_GRAY);
			g.fillOval(x, y, width, height);
		}
	}
	public Dot clone() {
		return this;
	}
	public Rectangle getBounds() {
		return new Rectangle(this.x, this.y, this.width, this.height);
	}
	
	public static Comparator<Dot> compareFitness(){
		Comparator<Dot> comp = new Comparator<Dot>() {
			@Override
			public int compare(Dot o1, Dot o2) {
				return Double.compare(o1.fitness, o2.fitness);
			}
		};
		return comp;
	}
}
