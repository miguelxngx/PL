import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Random;

import org.jfree.data.xy.XYSeries;

public class ThreadCalculator {
	
	public FitnessCalculator fitness;
	
	public ThreadCalculator() {
		
	}
	
	public void updateDots(ArrayList<Dot> dots) {
		Runtime runtime = Runtime.getRuntime();
		int n = runtime.availableProcessors();
		UpdateDotsCalculator fitness[]= new UpdateDotsCalculator[n];
		int nDots = dots.size();
		for(int i=0;i<n;i++) {
			fitness[i] = new UpdateDotsCalculator(dots, nDots, i, n);
		}
		for(int i=0;i<n;i++) {
			try {
				fitness[i].join();
			}catch(Exception e) {
				
			}
		}
	}
	
	public void calculateFitness(ArrayList<Dot> dots) {
		Runtime runtime = Runtime.getRuntime();
		int n = runtime.availableProcessors();
		FitnessCalculator fitness[]= new FitnessCalculator[n];
		int nDots = dots.size();
		long startTime = System.currentTimeMillis();
		for(int i=0;i<n;i++) {
			fitness[i] = new FitnessCalculator(dots, nDots, i, n);
		}
		for(int i=0;i<n;i++) {
			try {
				fitness[i].join();
			}catch(Exception e) {
				
			}
		}
		long stopTime = System.currentTimeMillis();
		System.out.println("Calculate fitness time: "+(stopTime-startTime));
	}
	
	public void calculateMutations(ArrayList<Dot> dots) {
		Runtime runtime = Runtime.getRuntime();
		int n = runtime.availableProcessors();
		MutationCalculator fitness[]= new MutationCalculator[n];
		int nDots = dots.size();
		long startTime = System.currentTimeMillis();
		for(int i=0;i<n;i++) {
			fitness[i] = new MutationCalculator(dots, nDots, i,n);
		}
		for(int i=0;i<n;i++) {
			try {
				fitness[i].join();
			}catch(Exception e) {
				
			}
		}
		long stopTime = System.currentTimeMillis();
		System.out.println("Mutation time: "+(stopTime-startTime));
	}
	
	public void calculatePowerModel(ArrayList<Point2D.Double> points, XYSeries res, int nPoints,double tolerance) {
		new PowerModelCalculator(points, res,nPoints,tolerance);
	}
	
	public void calculateParents(ArrayList<Dot> dots, ArrayList<Dot> newDots, double fitnessSum) {
		Runtime runtime = Runtime.getRuntime();
		int n = runtime.availableProcessors();
		ParentCalculator fitness[]= new ParentCalculator[n];
		int nDots = dots.size();
		long startTime = System.currentTimeMillis();
		for(int i=0;i<n;i++) {
			fitness[i] = new ParentCalculator(dots, newDots, fitnessSum, nDots, i,n);
		}
		System.out.println("Parent selection started");
		for(int i=0;i<n;i++) {
			try {
				fitness[i].join();
			}catch(Exception e) {
				System.out.println(e.getMessage());
			}
		}
		long stopTime = System.currentTimeMillis();
		System.out.println("Parent selection time: "+(stopTime-startTime));
	}
	
	public class UpdateDotsCalculator extends Thread{
		ArrayList<Dot> dots;
		int values;
		int nThreads;
		int nPoints;
		public UpdateDotsCalculator(ArrayList<Dot> dots, int nPoints, int values, int nThreads) {
			this.dots = dots;
			this.values = values;
			this.nThreads = nThreads;
			this.nPoints = nPoints;
			start();
		}
		
		public void run() {
			for(int i=0; i+values<nPoints; i+=nThreads) {
				dots.get(i+values).update();
			}
		}
	}
	
	public class FitnessCalculator extends Thread{
		
		public ArrayList<Dot> dots;
		public int values;
		public int nDots;
		public int numThreads;
		
		public FitnessCalculator(ArrayList<Dot> dots, int nDots, int values, int numThreads) {
			this.dots = dots;
			this.values = values;
			this.nDots = nDots;
			this.numThreads=numThreads;
			start();
		}
		
		public void run() {
			for(int i=0; i+values<nDots; i+=numThreads) {
				dots.get(i+values).calculateFitness();
				//System.out.println(dots.get(i).fitness);
				dots.get(i+values).calculateVelocity();
			}
		}
	}
	
	public class MutationCalculator extends Thread{
		public ArrayList<Dot> dots;
		public int values;
		public int nDots;
		public int numThreads;
		
		public MutationCalculator(ArrayList<Dot> dots, int nDots, int values, int numThreads) {
			this.dots = dots;
			this.values = values;
			this.nDots = nDots;
			this.numThreads = numThreads;
			start();
		}
		
		public void run() {
			for(int i=1; i+values<nDots; i+=numThreads) {
				if(i+values!=0) {
					dots.get(i+values).brain.mutate3();
				}
			}
		}
	}
	
	public class ParentCalculator extends Thread{
		public ArrayList<Dot> dots;
		public ArrayList<Dot> newDots;
		public int values;
		public int nDots;
		public int numThreads;
		public Random al;
		public double fitnessSum;
		
		public ParentCalculator(ArrayList<Dot> dots, ArrayList<Dot> newDots, double fitnessSum, int nDots, int values, int numThreads) {
			this.dots = dots;
			this.values = values;
			this.nDots = nDots;
			this.numThreads = numThreads;
			this.al = new Random();
			this.fitnessSum = fitnessSum;
			this.newDots = newDots;
			start();
		}
		
		public void run() {
			double totalN = (nDots+1)*nDots/2;
			for(int i=0; i+values<nDots; i+=numThreads) {
				double rand = al.nextDouble()*totalN;
				double runningSum=0;
				int j;
				for(j=0;j<nDots;j++) {
					runningSum+=nDots-j;
					if(rand<runningSum) {
						synchronized(newDots) {
							newDots.add(dots.get(j).babyPls());
						}
						break;
					}
				}
			}
			//System.out.println(newDots.size());
		}
	}

	public class PowerModelCalculator extends Thread{
		public ArrayList<Point2D.Double> points, logs;
		public XYSeries res;
		public int nPoints;
		public double tolerance;
		public PowerModelCalculator(ArrayList<Point2D.Double> points, XYSeries res, int nPoints, double tolerance) {
			this.points = points;
			this.res = res;
			this.logs = new ArrayList<Point2D.Double>();
			this.nPoints = nPoints;
			this.tolerance = tolerance;
			start();
		}
		
		public void run() {
			Runtime runtime = Runtime.getRuntime();
			int n = runtime.availableProcessors();
			LogCalculator[] log = new LogCalculator[n];
			ArrayList<Point2D.Double> logs = new ArrayList<Point2D.Double>(nPoints);
			/*for(int i=0;i<n;i++) {
				log[i] = new LogCalculator(points, logs, n, i);
			}*/
			for(int i=0 ; i<nPoints; i++) {
				logs.add(new Point2D.Double(Math.log10(points.get(i).x), Math.log10(points.get(i).y)));
			}
			/*for(int i=0;i<n;i++) {
				try {
					log[i].join();
				}catch(Exception e) {
					
				}
			}*/
			
			double A[][] = new double[2][2];
			double B[] = new double [2];
			A[0][0] = nPoints;
			A[0][1] = 0.0;
			A[1][1] = 0.0;
			B[0] = 0.0;
			B[1] = 0.0;
			for(int i=0;i<nPoints;i++) {
				A[0][1] += logs.get(i).x;
				A[1][1] += Math.pow(logs.get(i).x, 2);
				B[0] += logs.get(i).y;
				B[1] += logs.get(i).x*logs.get(i).y;
			}
			A[1][0] = A[0][1];
			
			double det = (A[0][0] * A[1][1]) - (A[0][1] * A[1][0]);
			double invA[][] = new double[2][2];
			double a[] = new double[2];
			double iA[][] = new double[2][2];
			iA[0][0]=A[1][1];
			iA[1][1]=A[0][0];
			iA[0][1]=A[0][1]*-1.0;
			iA[1][0]=A[1][0]*-1.0;
			a[0] = 0.0;
			a[1] = 0.0;
			for(int i=0;i<2;i++) {
				for(int j=0; j<2 ; j++) {
					invA[i][j] = iA[i][j]/det;
					a[i] += invA[i][j]*B[j];
				}
			}
			n = runtime.availableProcessors();
			double auxValues = 0;
			PowerCalculator p[]=new PowerCalculator[n];
			System.out.println(a[0]+", "+a[1]);
			for(int i=0;i<n;i++) {
				auxValues+= (i * (1.0/n));
				p[i]=new PowerCalculator(a[0],a[1],res,nPoints,auxValues,tolerance);
			}
			for(int i=0;i<n;i++) {
				try {
					p[i].join();
				}catch(Exception e) {
					
				}
			}
			if(a[1]+tolerance/100>0) {
				GamePanel.STOPALL(Math.pow(10.0, (double)a[0])*(double)Math.pow(nPoints, a[1]));
			}
		}
		
		public class PowerCalculator extends Thread{
			double a1;
			double a2;
			XYSeries res;
			int nPoints;
			double values;
			public PowerCalculator(double a1, double a2, XYSeries res, int nPoints, double values, double tolerance) {
				this.a1 = a1;
				this.a2 =a2;
				this.res = res;
				this.nPoints = nPoints;
				this.values = values;
				start();
			}
			
			public void run() {
				double x, y;
				for(double i=0; i+values<nPoints; i++) {
					if(i+values!=0) {
						x=i+values;
						y= Math.pow(10.0, (double)a1)*(double)Math.pow(x, a2);
						//System.out.println(x + ", " + y);
						synchronized(res){
							res.add(x,y);
						}
					}
				}
			}
		}
		
		public class LogCalculator extends Thread{
			public ArrayList<Point2D.Double> points, res;
			public int nThreads, values;
			public LogCalculator(ArrayList<Point2D.Double> points, ArrayList<Point2D.Double> res, int nThreads, int values) {
				this.points = points;
				this.res = res;
				this.nThreads = nThreads;
				this.values = values;
				start();
			}
			
			public void run() {
				int n=points.size();
				for(int i=0;i+values<n;i+=nThreads) {
					res.set(i+values, new Point2D.Double(Math.log(points.get(i+values).x), Math.log(points.get(i+values).y)));
				}
			}
		}
	}
}
