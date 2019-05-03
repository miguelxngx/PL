import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class GamePanel extends JFrame implements Runnable, MouseListener, ActionListener{
	public static final long serialVersionUID= 1L;
    Timer reloj;
    private boolean running;
    private Thread animator;
    private Population population;
    Dot algo;
    MapInteractions map;
    private static int state=0;
    public Point players;
    public JTextField texto;
    public int tolerance;
    /**
     * Constructor Default: Start the state sucession from 0 and starts the animator
     */
    public GamePanel()//Constructor Default para el menu
    {	
    	super();
        map = new MapInteractions();
        state=0;
        texto = new JTextField(3);
        texto.addActionListener(this);
        this.add(texto);
        addMouseListener(this);
        startHungerGames();
    }
    
    /*
     * startHungerGames: starts the animator thread wich is the one responsible of the game updating, rendering and painting
     */
    public void startHungerGames() {
    	if(animator==null||!running) {
    		animator = new Thread(this);
    		animator.start();
    	}
    }
    
    /*
     * run: method run when the thread is activated and running variable is true
     */
    public void run(){
        running = true;
        while(running){
            gameUpdate();
            gameRender();
            paintScreen();

            try{
                Thread.sleep(15);
            }catch(InterruptedException ex){}
        }
        System.exit(0);
    }//run()
    
    /*
     * is the resposible for updating the components active on the screen
     */
    public void gameUpdate() {
    	if(state==3) {
    		//if there's still dots alive, the population updates, otherwise, the evolution proccess starts
	    	if(population.yaSePetatearon()) {
	    		//Its evolution babyyyyyyy...
	    		population.calculateFitness();
	    		population.naturalSelection();
	    		population.mutateDemBeibes();
	    	}else {
	    		population.update();
	    	}
    	}
    }
    
    /*
     * is the responsible for drawing a image of the current graphic states of the components on screen, according to the state is
     * what it renders
     */
    private Graphics dbg;
    private Image dbImage = null;
    public void gameRender() {
    	if(dbImage == null){
    		if(state!=0) {dbImage = createImage(900,900);}
    		else{dbImage=createImage(900,350);}
            if(dbImage == null){
                System.out.println("dbImage is null");
                return;
            }else{
                dbg = dbImage.getGraphics();
            }
        }
    	if(state==0) {
    		dbg.drawString("Type the tolerance:", 300, 250);
    	}
    	if(state==1) {
            map.render(dbg);
    	}
    	if(state==2) {
    		map.render(dbg);
    		dbg.setColor(Color.blue);
    		dbg.fillOval(players.x-20, players.y-20, 40, 40);
    	}
    	if(state==3) {
    		dbg.setColor(Color.white);
        	dbg.fillRect(0,0,900,900);
        	map.render(dbg);
        	population.render(dbg);
    	}
    	if(state==4) {
    		dbg.setColor(Color.white);
    		dbg.fillRect(0,0,900,900);
    		dbg.setColor(Color.black);
    		dbg.drawString("The fastest route was reached according to the tolerance:", 300, 250);
    		dbg.drawString(String.format("%.2f", asymptote), 450, 350);
    	}
    }
    
    /*
     * paintScreen: is the one responsible to draw the prevously rendered image
     */
    private void paintScreen(){
        Graphics g;
        try{
            g = this.getGraphics();
            if((g != null) && (dbImage != null))
                g.drawImage(dbImage,0,0,null);
            Toolkit.getDefaultToolkit().sync();
            g.dispose();
        }
        catch(Exception e){
            System.out.println("Graphics context error: "+e);
        }
    }

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		if(state==1) {
			players = new Point(e.getX(), e.getY());
			state++;
		}else if(state==2) {
			map.setGoal(e.getX(), e.getY());
	        population = new Population(5000, players, map, tolerance);
	        state++;
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if( e.getSource() == texto){
			tolerance = Integer.parseInt(texto.getText());
			this.remove(texto);
			state++;
			dbImage = null;
        }
	}
	
	/*
	 * when the Power method reaches the tolerance of the asymptote, it steps up the state showing the distance on the screen
	 */
	public static double asymptote;
	public static synchronized void STOPALL(double a) {
		asymptote = a;
		state++;
	}
}