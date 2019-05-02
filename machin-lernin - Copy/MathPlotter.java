 import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
 import org.jfree.chart.ChartPanel;
 import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
 import org.jfree.data.xy.XYSeriesCollection;
 import org.jfree.ui.ApplicationFrame;
 
public class MathPlotter extends ApplicationFrame{
	public XYSeries series = new XYSeries("Distance for each iteration");
	public XYSeries regression;
	final XYDataset dataset;
	public int numberOfPoints;
	public MathPlotter() {
		super("Graficas");
		numberOfPoints=0;
		regression = new XYSeries("Something");
		dataset = createDataset();
        final JFreeChart chart = createChart(dataset);
        final ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(600, 600));
        setContentPane(chartPanel);
		/*final XYSeriesCollection data = new XYSeriesCollection(series);
        final JFreeChart chart = ChartFactory.createScatterPlot(
            "XY Series Demo",
            "X", 
            "Y", 
            data,
            PlotOrientation.VERTICAL,
            true,
            true,
            false
        );
        final ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(500, 500));
        setContentPane(chartPanel);
        */
        pack();
        setVisible(true);
	}
	
	public void addSeries(double x, double y, ArrayList<Point2D.Double> distances, double tolerance) {
		series.add(x,y);
		numberOfPoints++;
		if(numberOfPoints>1) {
			this.regression.clear();
			ThreadCalculator tc = new ThreadCalculator();
			tc.calculatePowerModel(distances, regression, (int)x, tolerance);
		}
	}
	
	public void paint(Graphics g) {
	
	}
	
	private XYDataset createDataset() {
		final XYSeriesCollection dataset = new XYSeriesCollection();
	    dataset.addSeries(series);
	    dataset.addSeries(regression);
	            
	    return dataset;
	    
	}
	
	private JFreeChart createChart(final XYDataset dataset) {
	    
	    // create the chart...
	    final JFreeChart chart = ChartFactory.createXYLineChart(
	        "Line Chart Demo 6",      // chart title
	        "X",                      // x axis label
	        "Y",                      // y axis label
	        dataset,                  // data
	        PlotOrientation.VERTICAL,
	        true,                     // include legend
	        true,                     // tooltips
	        false                     // urls
	    );
	
	    // NOW DO SOME OPTIONAL CUSTOMISATION OF THE CHART...
	    chart.setBackgroundPaint(Color.white);
	
	//    final StandardLegend legend = (StandardLegend) chart.getLegend();
	//      legend.setDisplaySeriesShapes(true);
	    
	    // get a reference to the plot for further customisation...
	    final XYPlot plot = chart.getXYPlot();
	    plot.setBackgroundPaint(Color.lightGray);
	//    plot.setAxisOffset(new Spacer(Spacer.ABSOLUTE, 5.0, 5.0, 5.0, 5.0));
	    plot.setDomainGridlinePaint(Color.white);
	    plot.setRangeGridlinePaint(Color.white);
	    
	    final XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
	    renderer.setSeriesLinesVisible(0, false);
	    renderer.setSeriesShapesVisible(1, false);
	    plot.setRenderer(renderer);
	
	    // change the auto tick unit selection to integer units only...
	    final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
	    rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
	    // OPTIONAL CUSTOMISATION COMPLETED.
	    
	    rangeAxis.setRange(0, 1500);
	    plot.setDomainAxis(1500, rangeAxis);
	    
	    return chart;
	    
	}
}
