/*______________________________________

CET 350 
Program 6
Created: 4/5/17 
Edited: 4/17/17 
Description: 
Program creates a frame with a canvas and a 
bouncing ball.  The program also creates a 
cannon and scrollbars to control it.  User
can adjust cannon to shoot ball.  The ball 
can also destroy the cannon
________________________________________*/

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.*;
import java.util.*;
import java.nio.file.*;
import java.lang.*;
import java.text.*;

public class CannonVSBall implements ActionListener, WindowListener, AdjustmentListener, ComponentListener,
ItemListener, Runnable 
{
	
   //declare program global variables
	private static final long serialVersionUID = 1L;
	
   // Initializing the components of the program	
   private Insets I;
   private Thread theThread;
   private Frame Game = new Frame(); 
   private board c = new board();
   private Panel control = new Panel();
   private Panel screen = new Panel(); 
   private MenuBar menubar;    
   private Menu Control, Size, Speed, Environment; 
   private CheckboxMenuItem xsmall, small, medium, large, xlarge; 
   private MenuItem Pause, Run, Restart, Quit; 
   private CheckboxMenuItem mercury, venus, earth, mars, jupiter, saturn, uranus, neptune, pluto, luna;
   private CheckboxMenuItem snail, slow, normal, fast, lightspeed;  
	
	BorderLayout bdr = new BorderLayout();
   
   boolean stop = false;
   boolean speed = false;
   boolean runProg = true; 
   int speedVar = 45; 
   int delay = 3; 
   double timer = 0;
   int compScore, humanScore = 0;
   String playclock;


   GridBagLayout gb = new GridBagLayout();
   GridBagConstraints gc = new GridBagConstraints(); 

   // Creating the scrollbars and different labels
   Scrollbar angleSB = new Scrollbar(Scrollbar.HORIZONTAL); 
   double angle = Math.PI/4;
   Scrollbar velocitySB = new Scrollbar(Scrollbar.HORIZONTAL); 
   Label velocityLabel = new Label(" Firing Velocity "); 
   Label speedL = new Label(" Firing Angle ");
   Label humanSc = new Label();
   Label humanLa = new Label("Human Score");
   Label playtime = new Label();
   Label timeLa = new Label();
   Label compSc = new Label();
   Label compLa = new Label();
	
    boolean shape = false; 
    boolean tails = false;  
    boolean more = true;
    int H=500;
	 int W=1000;
	 int sh, sw;
	 int xUnits;
	 int yUnits;
    int mx, my; //mouse pressed vars
    int rx, ry; //mouse released vars

   //declare component scrollbars, buttons, and labels

   
	
	CannonVSBall(){ //constructor for Bounce

     Game.setMinimumSize(new Dimension (500,500));
	  Game.setLayout(bdr); //set layout
	  Game.setVisible(true); //set layout visible
	  MakeSheet(); //create sheet 
	  try{ //run initcomponents to assemble screen and canvas objects
       initComponents(); 
     }
	  catch(Exception e) { //catch any errors or exceptions
       e.printStackTrace();
	  }
	  SizeScreen(); //run screen size
	}
	
	public void initComponents(){ //assembles components for bounce object
		         // Main Frame Parameters  
             
			// Creating the main menu components
            menubar = new MenuBar(); 
            Control = new Menu();  
            Size = new Menu(); 
            Speed = new Menu(); 
            Environment = new Menu(); 
            
            // Adding the main menu components to the frame and setting the labels
            menubar.add(Control); 
            Control.setLabel(" Control ");  
            menubar.add(Size); 
            Size.setLabel(" Size "); 
            menubar.add(Speed); 
            Speed.setLabel(" Speed "); 
            menubar.add(Environment); 
            Environment.setLabel(" Environment "); 
            
            // Creating subcomponents and their corresponding hot-keys
            Pause = Control.add(new MenuItem(" Pause ", new MenuShortcut(KeyEvent.VK_P))); 
            Run = Control.add(new MenuItem(" Run ", new MenuShortcut(KeyEvent.VK_S))); 
            Restart = Control.add(new MenuItem(" Restart ", new MenuShortcut(KeyEvent.VK_R))); 
            Quit = Control.add(new MenuItem(" Quit ", new MenuShortcut(KeyEvent.VK_Q))); 
            
            // Adding the listeners to the Control sub compoents
            Pause.addActionListener(this); 
            Run.addActionListener(this); 
            Restart.addActionListener(this); 
            Quit.addActionListener(this); 
            
            // Adding the sub components of the size menu
            Size.add(xsmall = new CheckboxMenuItem(" X-Small ", false)); 
            Size.add(small = new CheckboxMenuItem(" Small ", false)); 
            Size.add(medium = new CheckboxMenuItem(" Medium ", true)); 
            Size.add(large = new CheckboxMenuItem(" Large ", false)); 
            Size.add(xlarge = new CheckboxMenuItem(" X-Large", false)); 
            
            // Adding the listeners to the sub components of the size menu
            xsmall.addItemListener(this); 
            small.addItemListener(this); 
            medium.addItemListener(this); 
            large.addItemListener(this); 
            xlarge.addItemListener(this); 
            
            // Adding the sub components of the speed menu
            Speed.add(snail = new CheckboxMenuItem(" Snail ", false));
            Speed.add(slow = new CheckboxMenuItem(" Slow ", false)); 
            Speed.add(normal = new CheckboxMenuItem(" Normal ", true)); 
            Speed.add(fast = new CheckboxMenuItem(" Fast ", false)); 
            Speed.add(lightspeed = new CheckboxMenuItem(" Lightspeed ", false));  
            
            // Adding the listeners to the sub components of the speed menu
            snail.addItemListener(this); 
            slow.addItemListener(this); 
            normal.addItemListener(this); 
            fast.addItemListener(this); 
            lightspeed.addItemListener(this); 
            
            // Adding the solar system sub components to the Environment menu
            Environment.add(mercury = new CheckboxMenuItem(" Mercury ", false)); 
            Environment.add(venus = new CheckboxMenuItem(" Venus ", false)); 
            Environment.add(earth = new CheckboxMenuItem(" Earth ", true)); 
            Environment.add(mars = new CheckboxMenuItem(" Mars ", false)); 
            Environment.add(jupiter = new CheckboxMenuItem(" Jupiter ", false)); 
            Environment.add(saturn = new CheckboxMenuItem(" Saturn ", false)); 
            Environment.add(uranus = new CheckboxMenuItem(" Uranus ", false)); 
            Environment.add(neptune = new CheckboxMenuItem(" Neptune ", false)); 
            Environment.add(pluto = new CheckboxMenuItem(" Pluto ", false)); 
            Environment.add(luna = new CheckboxMenuItem(" Luna ", false)); 
            
            // Adding the listeners to the sub components of the Environment menu
            mercury.addItemListener(this); 
            venus.addItemListener(this); 
            earth.addItemListener(this); 
            mars.addItemListener(this); 
            jupiter.addItemListener(this); 
            saturn.addItemListener(this); 
            uranus.addItemListener(this); 
            neptune.addItemListener(this); 
            pluto.addItemListener(this); 
            luna.addItemListener(this); 
            
            // Setting the parameters of the Game Frame
			Game.setTitle(" Bounce: The Trilogy. Part III ");
			Game.setLayout(bdr);
			Game.setResizable(true);
			Game.setVisible(true);
			Game.setBackground(Color.darkGray);
			Game.addWindowListener(this);
			Game.addComponentListener(this);
			screen.add(c);  
            Game.add(screen, BorderLayout.CENTER); 
            Game.add(control, BorderLayout.SOUTH); 
            Game.setMenuBar(menubar);
             
            // Setting up the gridbag layout for all the Frame components
            double colWeight[] = {0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5};
            double rowWeight[] = {0.5, 0.5, 0.5}; 
            int colWidth[] = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}; 
            int rowHeight[] = {1,1,1}; 
    
            gb.rowHeights = rowHeight;
            gb.rowWeights = rowWeight; 
            gb.columnWidths = colWidth; 
            gb.columnWeights = colWeight; 
            gc.anchor = 10;
            gc.fill = 1;   
     
            gc.gridy = 10; 
            gc.gridx = 11; 
            gb.setConstraints(angleSB, gc);     //speed scrollbar
     
            gc.gridy = 10;  
             gc.gridx = 3; 
             gb.setConstraints(velocitySB, gc);     //size sb
             
             gc.gridy = 10; 
             gc.gridx = 5; 
             gb.setConstraints(humanSc, gc);      //human score display
             
             gc.gridy = 5; 
             gc.gridx = 5; 
             gb.setConstraints(humanLa, gc);	// human score label
    
             gc.gridy = 10; 
             gc.gridx = 7; 
             gb.setConstraints(playtime, gc);       //time display
             
             gc.gridy = 5; 
             gc.gridx = 7; 
             gb.setConstraints(timeLa, gc);       //time label
             
             gc.gridy = 10; 
             gc.gridx = 9;
             gb.setConstraints(compSc, gc);        //computer score display
             
             gc.gridy = 5; 
             gc.gridx = 9;
             gb.setConstraints(compLa, gc);        //computer score label
             
             gc.gridy = 5; 
             gc.gridx = 11; 
             gb.setConstraints(speedL, gc);     //speed label
             
             gc.gridy = 5; 
             gc.gridx = 3; 
             gb.setConstraints(velocityLabel, gc);       //size label
                    
             control.add(speedL); 
             control.add(angleSB); 
             control.add(humanSc);
             control.add(humanLa);
             control.add(compSc);
             control.add(compLa);
             control.add(playtime);
             control.add(timeLa);
             control.add(velocitySB);
             control.add(velocityLabel); 
             
             velocitySB.addAdjustmentListener(this); 
             velocitySB.setMinimum(10);
         	 velocitySB.setMaximum(100);
         	 velocitySB.setUnitIncrement(10);
         	 velocitySB.setValue(10); 
             
             angleSB.addAdjustmentListener(this); 
             angleSB.setMinimum(0);
         	 angleSB.setMaximum(100);
         	 angleSB.setUnitIncrement(10);
         	 angleSB.setValue(speedVar);  
             
         	 // Score and time-keeping components
             humanSc.setText("0"); 
             humanSc.setBackground(Color.WHITE);
             timeLa.setText("Time");
             playtime.setBackground(Color.WHITE); 
             compSc.setText(Integer.toString(compScore));
             compSc.setBackground(Color.WHITE); 
             compLa.setText("Computer Score");
             
             control.setLayout(gb);	
		
            c.setSize(xUnits/2);
            c.setSheet(sh, sw);
           

			   SizeScreen();
				Start();

				Game.validate();

	}

	public void Start() {  //creates thread for program 
	   if(theThread == null){
         theThread = new Thread(this);
         theThread.start();
         c.repaint();
      }   
	   
	}

	public void MakeSheet(){ //creates sheet, calculates sheet size
		 //environment is divided into 34x24 sections (unit = 1)

			xUnits = W/34;
			yUnits = H/24;

			     I = Game.getInsets();
		    // sh & sw are sent to Bounce for correct canvas sizing
			    sh = H - I.top - I.bottom - (yUnits*6);
                sw = W - I.left - I.right - (xUnits*2);            

//calc your inc size 
}
	
	private void SizeScreen() { //function to calculate screen size
								//uses calculated Xunits & yUnits from MakeSheet() 
	        					//ensures the components will adjust correctly upon screen resize
		
					
			    Game.setSize(W, H);
				Game.setLocation((xUnits), (yUnits));
				      
			       c.setSize((sw), (sh));
				   c.setLocation(xUnits, (yUnits*3));
				   c.setSheet(sh, sw);	  
	}
	
	
	public static void main(String[] args){ //main function, creates new instance of bounce
		CannonVSBall cvb = new CannonVSBall(); 
	}

	public void run() //activate and run thread
   {
		
		
		
		Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
      while(theThread != null)
      {
         if(!stop) //run thread while program is not paused
         {
            
            c.setSheet(sw,sh);  // these functions are all in bounce class
            c.boundary();
            c.step();
            c.drawit(tails);  
            c.addTime();
            
            timer = timer +.015;
            DecimalFormat df = new DecimalFormat("#.##");
            playclock = df.format(timer);
            playtime.setText(playclock);
          
            if(c.gamePause()==true){
            	if(c.addComp() == true){
            		compScore += 1;
            	}
            	if(c.addHuman() == true){
            		humanScore += 1;
            	}
            	try {
					Thread.sleep(50);
					c.resetBall();
					
					setScore();
					
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            	
          
            }
            
          
            
            try
            {
               Thread.sleep(delay);
            }catch(InterruptedException e){}
         }
         else
         {
           System.out.print(""); //this print line allows for the program to successfully pause/start
           //we don't know why, but if you take it out it will never leave pause
         }
     
      }
      	
	}

	@Override
	public void componentHidden(ComponentEvent arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void componentMoved(ComponentEvent arg0) {
		// TODO Auto-generated method stub
	}

	public void componentResized(ComponentEvent ce) { //function to adjust components if window is resized
	
		H = Game.getHeight();
		W = Game.getWidth();
      
		MakeSheet();
		SizeScreen();
		//let the ball object know the change too 
	}

	@Override
	public void componentShown(ComponentEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	// Handles scroll bar events and updates accordingly (finished)
	public void adjustmentValueChanged(AdjustmentEvent ae) { //function for scrollbar adjustments

		Object src0 = ae.getSource(); //create scrollbar source object
      
      if(src0 == angleSB){ //if user moves speed scrollbar, adjust speed
		Scrollbar s0 = (Scrollbar)ae.getSource();
      c.setAngle(Math.toRadians(s0.getValue()));
		
		      
     // delay = (100 - ae.getValue())/8;  //this changed speed of ball
     
     
		} else{ //if user adjusts size scrollbar, adjust size
			Scrollbar s0 = (Scrollbar)ae.getSource();
         //c.setSize(ae.getValue());  // ball size
         c.setVelocity(s0.getValue());
		}    		
  	}
   
   public void stop() //function to end program 
   {
       theThread = null;  
		 Game.dispose();		
		 Game.setVisible(false); 
   }
               
   //NOTE: Eclipse forces several functions to be implemented (all shown w/"@Override)
	@Override
	public void windowActivated(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosed(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void windowClosing(WindowEvent we) {
      stop();
	}

	@Override
	public void windowDeactivated(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeiconified(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowIconified(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowOpened(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}


	public void actionPerformed(ActionEvent ae) { //Function to handle control menu
		Object src1 = ae.getSource();
      
      if(src1 == Restart) 
      {
        stop(); 
        CannonVSBall cvb = new CannonVSBall(); 
      }
      
      if(src1 == Quit)
      {
        stop(); 
      }
      
      if(src1 == Pause)
      {
        stop = true;  
      }
      
      if(src1 == Run)
      {
        stop = false; 
      }
	}

  public void itemStateChanged(ItemEvent e) //method for item listener that handles ALL checkboxes
  {
   Object o = e.getSource();
   
   
   //change ball size
   if(o == xsmall){
   
      xsmall.setState(true);
      small.setState(false);
      medium.setState(false);
      large.setState(false);
      xlarge.setState(false);
      c.setSize(5);
   
   }
   
   if(o == small){
   
      xsmall.setState(false);
      small.setState(true);
      medium.setState(false);
      large.setState(false);
      xlarge.setState(false);
      c.setSize(10);
   
   }
   
   if(o == medium){
   
      xsmall.setState(false);
      small.setState(false);
      medium.setState(true);
      large.setState(false);
      xlarge.setState(false);
      c.setSize(15);
   
   }
   
   if(o == large){
   
      xsmall.setState(false);
      small.setState(false);
      medium.setState(false);
      large.setState(true);
      xlarge.setState(false);
      c.setSize(20);
   
   }
   
   if(o == xlarge){
   
      xsmall.setState(false);
      small.setState(false);
      medium.setState(false);
      large.setState(false);
      xlarge.setState(true);
      c.setSize(25);
   
   }
   
   //change ball speed
   if(o == snail) 
   {
     snail.setState(true); 
     slow.setState(false); 
     normal.setState(false); 
     fast.setState(false); 
     lightspeed.setState(false); 
     delay = 5; 
   }
   
   if(o == slow){
   
      snail.setState(false); 
      slow.setState(true);
      normal.setState(false);
      fast.setState(false);
      lightspeed.setState(false);
      delay = 4;
   
   }
   
   if(o == normal){
   
      snail.setState(false); 
      slow.setState(false);
      normal.setState(true);
      fast.setState(false);
      lightspeed.setState(false);
      delay = 3;
   
   }
   
   if(o == fast){
    
      snail.setState(false);   
      slow.setState(false);
      normal.setState(false);
      fast.setState(true);
      lightspeed.setState(false);
      delay = 2;
   
   }
   
   if(o == lightspeed){
   
      snail.setState(false); 
      slow.setState(false);
      normal.setState(false);
      fast.setState(false);
      lightspeed.setState(true);
      delay = 1;
   
   }

   //planets
   if(o == mercury){
   
       mercury.setState(true); 
       venus.setState(false); 
       earth.setState(false); 
       mars.setState(false); 
       jupiter.setState(false); 
       saturn.setState(false); 
       uranus.setState(false); 
       neptune.setState(false); 
       pluto.setState(false); 
       luna.setState(false);  
       c.setAccel(3.7);
   
   }
   
   if(o == venus){
   
       mercury.setState(false); 
       venus.setState(true); 
       earth.setState(false); 
       mars.setState(false); 
       jupiter.setState(false); 
       saturn.setState(false); 
       uranus.setState(false); 
       neptune.setState(false); 
       pluto.setState(false); 
       luna.setState(false);  
       c.setAccel(8.9);
   
   }
   
   if(o == earth){
   
       mercury.setState(false); 
       venus.setState(false); 
       earth.setState(true); 
       mars.setState(false); 
       jupiter.setState(false); 
       saturn.setState(false); 
       uranus.setState(false); 
       neptune.setState(false); 
       pluto.setState(false); 
       luna.setState(false);  
       c.setAccel(9.8);
   
   }
   
   if(o == mars){
   
       mercury.setState(false); 
       venus.setState(false); 
       earth.setState(false); 
       mars.setState(true); 
       jupiter.setState(false); 
       saturn.setState(false); 
       uranus.setState(false); 
       neptune.setState(false); 
       pluto.setState(false); 
       luna.setState(false);  
       c.setAccel(3.7);
   
   }
   
   if(o == jupiter){
   
       mercury.setState(false); 
       venus.setState(false); 
       earth.setState(false); 
       mars.setState(false); 
       jupiter.setState(true); 
       saturn.setState(false); 
       uranus.setState(false); 
       neptune.setState(false); 
       pluto.setState(false); 
       luna.setState(false);  
       c.setAccel(24.8);
   
   }

   if(o == saturn){
   
       mercury.setState(false); 
       venus.setState(false); 
       earth.setState(false); 
       mars.setState(false); 
       jupiter.setState(false); 
       saturn.setState(true); 
       uranus.setState(false); 
       neptune.setState(false); 
       pluto.setState(false); 
       luna.setState(false);  
       c.setAccel(10.4);
   
   }
   
   if(o == uranus){
   
       mercury.setState(false); 
       venus.setState(false); 
       earth.setState(false); 
       mars.setState(false); 
       jupiter.setState(false); 
       saturn.setState(false); 
       uranus.setState(true); 
       neptune.setState(false); 
       pluto.setState(false); 
       luna.setState(false);  
       c.setAccel(8.7);
   
   }

   if(o == neptune){
   
       mercury.setState(false); 
       venus.setState(false); 
       earth.setState(false); 
       mars.setState(false); 
       jupiter.setState(false); 
       saturn.setState(false); 
       uranus.setState(false); 
       neptune.setState(true); 
       pluto.setState(false); 
       luna.setState(false);  
       c.setAccel(11.2);
   
   }

   if(o == pluto){
   
       mercury.setState(false); 
       venus.setState(false); 
       earth.setState(false); 
       mars.setState(false); 
       jupiter.setState(false); 
       saturn.setState(false); 
       uranus.setState(false); 
       neptune.setState(false); 
       pluto.setState(true); 
       luna.setState(false);  
       c.setAccel(.6);
   
   }

   if(o == luna){
   
       mercury.setState(false); 
       venus.setState(false); 
       earth.setState(false); 
       mars.setState(false); 
       jupiter.setState(false); 
       saturn.setState(false); 
       uranus.setState(false); 
       neptune.setState(false); 
       pluto.setState(false); 
       luna.setState(true);  
       c.setAccel(1.6);
   
   }
   
  }  
  
  public void setScore(){ // This function simply adds the score to the Frame only when they change
	  compSc.setText(Integer.toString(compScore));
	  humanSc.setText(Integer.toString(humanScore));
  }

}//end class BallVSCannon

//------------------this class does all the drawing------------------------------------
class board extends Canvas implements MouseListener, MouseMotionListener//canvas class
{

private static final long serialVersionUID = 1L;

//rectangle vector
Vector <Rectangle> walls = new Vector<Rectangle>();


//declared variables for board class
 	  int x,y,w,h; 
 	  int down,right;
 	  int sw, sh;
   Point m = new Point(0,0);
   Point r = new Point(0,0);
  boolean shape, isClear, tails;
  boolean dragbox = false;
  boolean shoot = false;
  boolean pause = false;
  boolean addComp = false;
  boolean addHuman = false;
  boolean resetFlag = false;
 Graphics g;
 Image b;
 Polygon poly = new Polygon();
 Point bwbh = new Point(25,45);
 double angle = Math.PI/4;
 Rectangle base;
 Rectangle bouncingBall, cannonBall;
 double cbx,cby,accx,vx,vy;
 double accy = 9.8;
 double bs = 30;
 double sum = 0;
 double dt = .1;
 
 
 public void setAccel(double A){//applied gravity
 
      accy = A;
 }
 
 
 public void setVelocity(double V){ //changes velocity of ball
 
 bs = V;
 }
 
 public void addTime(){
 
 sum += dt;
 }
 
 
  public void setAngle(double A){  //angle of the barrel
  
  angle = A;
  
  }
   
  public void setSheet(int cw, int ch){ //function to set canvas sheet
  
  sw = cw;
  sh = ch;
  
  }

  public void setSize(int H) //function to set object size 
  {
    w = H*2;
    h = H*2;
  }
  
   public board() //constructor for board class
  {
          x = 0;
          y = 0;
       down = 1;
      right = 1;
    isClear = false;
    this.addMouseListener(this);
    this.addMouseMotionListener(this);

    setBackground(Color.GRAY); 
 
  }
  
  public void boundary(){ //function to set boundary on canvas
      
      //frame boundaries
         if(y > sh - h){
               down = -1;
         }
         if(y < 0){
               down = 1;
         }
         if(x > sw - w){
               right = -1;
         }
         if(x < 0){
               right = 1;
         }
      
      //rectangle boundaries
      Rectangle b = new Rectangle(x,y,w,h);
      
      for(int i = 0; i< walls.size(); i++){
            if(b.intersects(walls.elementAt(i))){
            
            b = b.intersection(walls.elementAt(i));
            //left bound
            if( b.getX() == walls.elementAt(i).getX()){
                  
                  right = -1;
            }
            //right bound
            if( b.getX() + b.getWidth() == walls.elementAt(i).getX() + walls.elementAt(i).getWidth()){
                  
                  right = 1;
            }
            //top bound
            if( b.getY() == walls.elementAt(i).getY()){
                  
                  down = -1;
            }
            //bottom bound
            if( b.getY() + b.getHeight() == walls.elementAt(i).getY() + walls.elementAt(i).getHeight()){
                  
                  down = 1;
            }
      }
    } 
  }
  
  
  public void drawit(boolean t){ //function to place object
    tails = t;
    repaint();    
  }
  
  public void update(Graphics sg) //function to draw object 
  {
	 
    b = createImage(sw,sh);   //next couple lines handles double buff
    if(g != null){
    
         g.dispose();
    }
    
    g = b.getGraphics();
    
    //cannon ball
     if(shoot){
      g.setColor(Color.RED);
      g.fillOval((int)cbx,(int)cby,20,20);
      
      cbx-= vx*dt;
      cby-= vy*dt;
      
      vx-= accx*dt;
      vy-= accy*dt;
      
      
      
      // remove walls when cb hits
      Rectangle canball = new Rectangle((int)cbx, (int)cby, 20, 20);
       int i = 0;
      while (i < walls.size()){
    
            if ( canball.intersects(walls.elementAt(i))){
               
                 walls.remove(i);                  
            }else{
               i++;
            }
       }

      
         if((cby > sh) || (cbx < 0)){
         
            shoot = false;
         }
      
      
      }else{
      
      cbx = (sw - 30);
      cby = (sh - 30);
      
      vx = bs*Math.cos(angle);
      vy = bs*Math.sin(angle);
      }
     
     bouncingBall = new Rectangle((int)cbx, (int)cby, 20, 20);
     
     int p = 6;  // changes border 
     
         
    //draws bouncing ball
     g.setColor(Color.BLACK);
     g.fillOval(x,y,w,h);
     g.setColor(Color.BLUE);
     g.fillOval(x+w/p,y+h/p,w- 2*w/p,h-2*h/p);
     
     cannonBall = new Rectangle(x, y, w, h);
     
     if(bouncingBall.intersects(cannonBall)){
         cbx = 0;
         cby = sh-50;
    	pause = true;
    	addHuman = true;
    	resetFlag = true;
     }
     
     
     
     // Rectangle is created for the purpose of contain
     base = new Rectangle((sw - 40),(sh - 40),50,50);    
     
     // If the cannon doesn't contain the ball, draw the cannon, 
     // else trigger destroy and add to computer score
     if(!(poly.intersects(cannonBall) || base.intersects(cannonBall) )){
      
    
    	 
     poly.reset();
    
    //draws cannon
    g.setColor(Color.BLACK);
    poly.addPoint((int)(sw - 20 - bwbh.x/2*Math.sin(angle)),(int)(sh - 20 + bwbh.x/2*Math.cos(angle)));
    poly.addPoint((int)(sw - 20 + bwbh.x/2*Math.sin(angle)),(int)(sh - 20 - bwbh.x/2*Math.cos(angle)));
    poly.addPoint((int)(sw - 20 - bwbh.y*Math.cos(angle) + bwbh.x/2*Math.sin(angle)),(int)(sh - 20 - bwbh.y*Math.sin(angle) - bwbh.x/2*Math.cos(angle)));
    poly.addPoint((int)(sw - 20 - bwbh.y*Math.cos(angle) - bwbh.x/2*Math.sin(angle)),(int)(sh - 20 - bwbh.y*Math.sin(angle) + bwbh.x/2*Math.cos(angle)));

    
    g.fillPolygon(poly);
    
    
    g.setColor(Color.BLACK);
    g.fillOval((sw - 40),(sh - 40),50,50);


    } else{ //Trigger pause and add score to the computer
    	pause = true;
    	addComp = true;
    	g.setColor(Color.GRAY);
    	g.fillRect((sw - 40),(sh - 40),50,50);
    	 

    }
     
     if(shoot){
    	 g.setColor(Color.RED);
    	 g.fillOval((sw - 40),(sh - 40),50,50);
     } else{
    	 g.setColor(Color.GREEN);
    	 g.fillOval((sw - 40),(sh - 40),50,50);
     }
     
     
    //rectangle stuff
    if (dragbox == true){
    
         g.setColor(Color.BLACK);
         g.drawRect(Math.min(m.x,r.x),Math.min(m.y,r.y),Math.abs(m.x-r.x),Math.abs(r.y-m.y));
         
    }
    for(int i = 0; i< walls.size(); i++){
    
         g.setColor(Color.BLACK);
         g.fillRect((int)walls.get(i).getX(),(int)walls.get(i).getY(),(int)walls.get(i).getWidth(),(int)walls.get(i).getHeight());
    
    
    }
     sg.drawImage(b,0,0,null);
     pause = false;
     addComp = false;
     addHuman = false;
    
  }
  
  public void step(){ //function to move object across the screen
  
      x = x + right;
      y = y + down;
  }
  
  
  
      public void mousePressed(MouseEvent e)
   {
     
     m.x = e.getX();
     m.y = e.getY(); 
     repaint();
   }
  
   public void mouseReleased(MouseEvent e)
   {
     r.x = e.getX(); 
     r.y = e.getY();
     repaint();
     dragbox = false;
     Rectangle a  = new Rectangle(Math.min(m.x,r.x),Math.min(m.y,r.y),Math.abs(m.x-r.x),Math.abs(r.y-m.y));
     
     //prevents inappropriate drawing of walls
     if( a.intersects(new Rectangle(x,y,w,h)) ||  r.x > sw  || r.y > sh  || r.x < 0  || r.y < 0){
     
     }
     else{
     
       walls.addElement(a);
     
     }
   }
   
   public void mouseExited(MouseEvent e)
   {
   
   }
   
   public void mouseEntered(MouseEvent e)
   {
   
   }
   
   public void mouseClicked(MouseEvent e)
   {
    Rectangle d  = new Rectangle(e.getX(),e.getY(),1,1);
    
 
    //removes walls 
    int i = 0;
    while (i < walls.size()){
    
            if ( d.intersects(walls.elementAt(i))  && (e.getButton() == MouseEvent.BUTTON1  && e.getClickCount() == 2)){
               
                 walls.remove(i);                  
            }else{
               i++;
            }
    }
   
    //shoot cannon
    if(poly.intersects(d) || base.intersects(d)){
      
      shoot = true;
    }
    
   }
   
   public void mouseMoved(MouseEvent e)
   {
   
   }
   
   public void mouseDragged(MouseEvent e)
   {
     dragbox = true;
   
     r.x = e.getX();
     r.y = e.getY(); 
     repaint();
   } 
   
   public boolean gamePause(){
	   
	   return pause;
   }
   
   public boolean addComp(){
	   return addComp;
   }
   
   public boolean addHuman(){
	   return addHuman;
   }
   
   public void resetBall(){
	   // This f'n is used to reset the ball in the event of a score for either side
	   if(resetFlag == false){
	   x=sw-90;
	   y=sh-90;
	   right = -1;
	   down = -1;
	   
   } else{
	   x = 0;
	   y = 0;
	   resetFlag = false;
   }
   }
} //end class board */
