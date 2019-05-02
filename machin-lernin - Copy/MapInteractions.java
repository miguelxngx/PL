import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.ListIterator;

public class MapInteractions {
	
	int width;
	int heigth;
	protected ArrayList<Pasillo> objetos;
	private ListIterator<Pasillo> objetosItr;
	public Obstacles obstacles;
	public Walls walls;
	public int goalX;
	public int goalY;
	
	public Rectangle2D map;
	public Rectangle goalRect;
	
	public MapInteractions() {
		width=900;
		heigth=900;
		
		this.goalX = -1;
		this.goalY = -1;
		
		
		objetos = new ArrayList<Pasillo>();
		objetos.add(new Pasillo(450,500,450,900));
		objetos.add(new Pasillo(210,500,450,500));
		objetos.add(new Pasillo(210,300,210,500));
		objetos.add(new Pasillo(210,300,450,300));
		objetos.add(new Pasillo(450,50,450,300));
		
		objetos.add(new Pasillo(450,750,650,750));
		objetos.add(new Pasillo(650,250,650,750));
		objetos.add(new Pasillo(450,250,650,250));
		
		obstacles = new Obstacles();
		walls = new Walls();
		
		walls.addWall(400, 550, 400, 900);
		walls.addWall(160, 550, 400, 550);
		walls.addWall(160, 250, 160, 550);
		walls.addWall(160, 250, 400, 250);
		walls.addWall(400, 0, 400, 250);
		walls.addWall(500, 800, 500, 900);
		walls.addWall(500, 800, 700, 800);
		walls.addWall(700, 200, 700, 800);
		walls.addWall(500, 200, 700, 200);
		walls.addWall(500, 0, 500, 200);
		
		walls.addWall(500, 450, 500, 700);
		walls.addWall(260,450,500,450);
		walls.addWall(260, 350, 260, 450);
		walls.addWall(260, 350, 500, 350);
		walls.addWall(500, 300, 500, 350);
		walls.addWall(500, 300, 600, 300);
		walls.addWall(600, 300, 600, 700);
		walls.addWall(500, 700, 600, 700);
		/*
		dbg.setColor(Color.white);
        dbg.fillRect(0,0,900,900);
        goal.paint(dbg, Color.BLACK);
        dbg.setColor(Color.orange);
		dbg.fillRect(250, 650, 800, 20);
		dbg.fillRect(0, 400, 650, 20);
        population.render(dbg);
        */
		buildRect();
	}
	
	public void setGoal(int x, int y) {
		this.goalX = x;
		this.goalY = y;
		this.goalRect = new Rectangle(goalX-20,goalY-20,40,40);
	}
	
	public boolean intersects(Rectangle dot) {
		objetosItr=objetos.listIterator();
		while(objetosItr.hasNext()) {
			Pasillo aux = objetosItr.next();
			if(aux.contains(dot)) return true;
		}
		return false;
	}
	
	public boolean possibleLine(Line2D.Double l) {
		return walls.possibleLine(l);
	}
	
	public void buildRect() {
		int n=objetos.size();
		map = objetos.get(0).rect;
		for(int i=1;i<n;i++) {
			map = map.createUnion(objetos.get(i).rect);
		}
	}
	
	public double distanceToGoal(int x, int y) {
		return Math.sqrt(Math.pow(this.goalX-x, 2) + Math.pow(this.goalY-y, 2));
	}
	
	public void render(Graphics g) {
		objetosItr=objetos.listIterator();
		g.setColor(Color.BLACK);
		while(objetosItr.hasNext()) {
			Pasillo aux = objetosItr.next();
			//g.fillRect(aux.x+5, aux.y, aux.width, aux.height);
			aux.paint(g);
		}
		if(goalX!=-1&&goalY!=-1) {
			g.setColor(Color.red);
			g.fillOval(this.goalX-20, this.goalY-20, 40, 40);
		}
		walls.paint(g);
	}
	
	public boolean reachedGoal(Line2D.Double l) {
		return l.intersects(goalRect);
	}
	
	public boolean reachedGoal(Rectangle p) {
		return goalRect.contains(p) || goalRect.intersects(p);
	}
}
