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
	
	/*
	 * Map-tolerance/default constructor: population needs a perception of the map they are in and also, tolerance is needed since statistics are
	 * managed from this class
	 */
	public Population(int x, Point players, MapInteractions map, double tolerance) {
		dots = new ArrayList<Dot>();
		this.map = map;
		populate(x, players);
		al = new Random(System.nanoTime());
		stats = new Statistics(tolerance);
	}
	
	/*
	 * populate: it calls for a thread that populates the array with new, default constructed, dots
	 */
	public void populate(int x, Point players) {
		ThreadCalculator tc = new ThreadCalculator();
		tc.populateDots(dots, x, players, map);
	}
	
	/*
	 * render: to render all the dots, this calls ThreadClculator to create the respective render Threads
	 */
	public void render(Graphics g) {
		ThreadCalculator tc = new ThreadCalculator();
		tc.renderDots(dots, g);
	}
	
	/*
	 * render: to update all the dots, this calls ThreadClculator to create the respective update Threads
	 */
	public void update() {
		ThreadCalculator tc = new ThreadCalculator();
		tc.updateDots(dots);
	}
	
	/*
	 * Method that checks if all the dots are dead yet.
	 */
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
	
	/*
	 * Method that calls for thread calculator that is in charge of calculateing all dots fitness
	 */
	public void calculateFitness() {
		ThreadCalculator tc = new ThreadCalculator();
		tc.calculateFitness(dots);
	}
	
	/*
	 * Calculates the fitness sum
	 */
	public void calculateFitnessSum() {
		this.fitnessSum=0.0;
		int i;
		int n=dots.size();
		for(i=0;i<n;i++) {
			this.fitnessSum += dots.get(i).fitness;
		}
		System.out.println("Fitness sum: " + fitnessSum);
	}
	
	/*
	 * Method that calls Thread calculator to mutate dem babies
	 */
	public void mutateDemBeibes() {
		ThreadCalculator tc = new ThreadCalculator();
		tc.calculateMutations(dots);
	}
	
	/*
	 * Natural selection process:
	 * it creates a new Arrey of new dots
	 * Sorts the dots according to the fitness from high to low
	 * sends the best route found in the generation to the stats and also the best distance
	 * adds the best dot of this generation to the other
	 * Calls for a thread calculator that selects the parents of the new dots and breed them
	 * Copies the newborns into the dots array
	 */
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
