import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ListIterator;
import java.util.Random;

public class Population {
	public ArrayList<Dot> dots;
	double fitnessSum;
	int bestDot;
	int minSteps=1000;
	Random al;
	int gen=0;
	int arrive;
	MapInteractions map;
	Statistics stats;
	
	public Population(int x, Point goal, Point players, MapInteractions map, double tolerance) {
		dots = new ArrayList<Dot>();
		this.map = map;
		populate(x, players);
		al = new Random(System.nanoTime());
		stats = new Statistics(tolerance);
	}
	
	public void populate(int x, Point players) {
		int i=0;
		for(i=0;i<x;i++) {
			dots.add(new Dot(players, map));
		}
	}
	
	public void render(Graphics g) {
		int i;
		int n=dots.size();
		for(i=n-1;i>=0;i--) {
			dots.get(i).paint(g);
		}
	}
	
	public void update() {
		int i;
		int n=dots.size();
		for(i=0;i<n;i++) {
			dots.get(i).update();
		}
	}
	
	public boolean yaSePetatearon() {
		ListIterator<Dot> itr = dots.listIterator();
		Dot aux;
		while(itr.hasNext()) {
			aux=itr.next();
			if(!aux.dead && !aux.reachedGoal) {
				return false;
			}
		}
		return true;
	}
	
	public void calculateFitness() {
		ThreadCalculator tc = new ThreadCalculator();
		tc.calculateFitness(dots);
	}
	
	public void calculateFitnessSum() {
		this.fitnessSum=0.0;
		int i;
		int n=dots.size();
		for(i=0;i<n;i++) {
			this.fitnessSum += dots.get(i).fitness;
		}
		System.out.println("Fitness sum: " + fitnessSum);
	}
	
	public void mutateDemBeibes() {
		ThreadCalculator tc = new ThreadCalculator();
		tc.calculateMutations(dots);
	}
	
	public void setBestDot() {
		double max=0;
		int maxIndex=0;
		int i=0;
		int n=dots.size();
		arrive = 0;
		for(i=0;i<n;i++) {
			if(dots.get(i).reachedGoal) arrive++;
			if(dots.get(i).fitness>max) {
				maxIndex = i;
				max = dots.get(i).fitness;
			}
			if(dots.get(i).fitness==max) {
				if(dots.get(i).velocity>dots.get(maxIndex).velocity) {
					maxIndex = i;
					max = dots.get(i).fitness;
				}
			}
		}
		bestDot = maxIndex;
		if(dots.get(bestDot).reachedGoal) {
			minSteps = dots.get(bestDot).brain.step;
			System.out.println("BestFit: "+dots.get(bestDot).fitness);
			System.out.println("Generación "+gen+". Pasos: "+minSteps+" Llegaron: "+arrive);
		}
	}
	
	public void naturalSelection() {
		int i;
		int n=dots.size();
		ArrayList<Dot> newDots = new ArrayList<Dot>(dots.size());

		System.out.println("best fitness: " + dots.get(0).fitness + " shortest distance: " + dots.get(0).totalDistance);
		
		
		Collections.sort(dots, Dot.compareFitness());
		Collections.reverse(dots);
		/*for(i=0;i<n;i++) {
			System.out.println(dots.get(i).fitness);
		}*/
		bestDot=0;
		this.stats.setBestRoute(dots.get(bestDot).brain.getPoints(), dots.get(bestDot).brain.step);
		this.stats.repaint();
		
		calculateFitnessSum();
		
		if(dots.get(bestDot).reachedGoal)this.stats.setDistance(gen,dots.get(bestDot).totalDistance);
		
		System.out.println("best fitness: " + dots.get(bestDot).fitness + " shortest distance: " + dots.get(bestDot).totalDistance);
		newDots.add(dots.get(bestDot).babyPls());
		newDots.get(0).isBest = true;
		System.out.println("best fitness: " + newDots.get(0).fitness + " shortest distance: " + newDots.get(0).totalDistance);
		
		ThreadCalculator tc = new ThreadCalculator();
		tc.calculateParents(dots, newDots, fitnessSum);
		
		dots = new ArrayList<Dot>();
		for(i=0;i<n;i++) {
			dots.add(newDots.get(i).clone());
		}
		System.out.println("best fitness: " + dots.get(0).fitness + " shortest distance: " + dots.get(0).totalDistance);
		System.out.println(dots.size());
		gen++;
		System.out.println("Natural selection done");
	}
}
