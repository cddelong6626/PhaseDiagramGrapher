/*
 *  Cole Delong
 *  Phase Diagram Grapher
 *  10-18-22
 *  
 *  Define lambdas f(x, y) and g(x, y) and this program 
 *  visualizes the system of DE's f and g using a phase
 *  diagram. Clicking on the diagram will cause a phase
 *  line to appear and follow the field.
 *  
 */

import processing.core.PApplet;
import processing.core.PVector;

public class PhaseDiagramGrapher extends PApplet {

  // Step size
  private final float h = 0.00001f;
  
  // Animation speed
  private final int animSpeed = 5000;

  // Initial position values
  private final float x0 = 0;
  private final float y0 = 0;
  PVector r;
  
  // x and y screen constraints
  private final float XMIN = -8;
  private final float XMAX = 8;
  private final float YMIN = -5;
  private final float YMAX = 5;
  private final float XRANGE = XMAX - XMIN;
  private final float YRANGE = YMAX - YMIN;
  
  // Axis constraints
  private final float XSTEP = (XMAX-XMIN)/30;
  private final float YSTEP = (YMAX-YMIN)/30;
  
  Grid grid;
  
  // Functions f(x, y) and g(x, y) to use as the y and x 
  // components of the vector field
  DifferentialEquation f = (float x, float y) -> {
    return (float) (-sin((float) 1.5*x) - 0.15*y);
  };
  
  DifferentialEquation g = (float x, float y) -> {
    return (float) (y);
  };

  
  // Sets the size of the window
  @Override
  public void settings(){
    size(800, 600);
  }

  // Runs once before the program begins
  @Override
  public void setup(){
   
    // Set up window
    background(255);
    
    // Construct Grid object
    grid = new Grid(this, f, g, XMIN, XMAX, YMIN, YMAX, XRANGE, 
        YRANGE, XSTEP, YSTEP);
        
    // Draw x and y axis
    grid.drawAxis();
    
    // Initial position vector
    r = new PVector(x0, y0);
    
    // Set the text alignment to center
    this.textAlign(CENTER);
    
  }
  
  // Uses RK4 to solve the differential equations for one 
  // step of size h
  private PVector solveDE(PVector rcur) {
    
    // for use in calculuation
    float k1, k2, k3, k4;
    
    // estimate new x position
    k1 = h*g.evaluate(rcur.x, rcur.y);
    k2 = h*g.evaluate(rcur.x + h/2, rcur.y + k1/2);
    k3 = h*g.evaluate(rcur.x + h/2, rcur.y + k2/2);
    k4 = h*g.evaluate(rcur.x + h, rcur.y + k3);
    float xnew = rcur.x + k1/6 + k2/3 + k3/3 + k4/6;
    
    // estimate new x position
    k1 = h*f.evaluate(rcur.x, rcur.y);
    k2 = h*f.evaluate(rcur.x + k1/2, rcur.y + h/2);
    k3 = h*f.evaluate(rcur.x + k2/2, rcur.y + h/2);
    k4 = h*f.evaluate(rcur.x + k3, rcur.y + h);
    float ynew = rcur.y + k1/6 + k2/3 + k3/3 + k4/6;
    
    return new PVector(xnew, ynew); 
    
  }
  
  // Runs continuously during the progrm
  @Override
  public void draw(){
        
    // Draw the solution curve
    strokeWeight(2);
    stroke(0, 255);
    for (int i = 0; i < animSpeed; i++) {
      point(grid.graphConversion(r).x, 
          grid.graphConversion(r).y);
      r.set(solveDE(r));
    }
        
    
  }
  
  // If the mouse is clicked in the window, start drawing 
  // the solution curve starting at that point
  @Override
  public void mouseReleased() {
    float xnew = map(mouseX, 0, width, XMIN, XMAX);
    float ynew = map(mouseY, height, 0, YMIN, YMAX);
    r.set(xnew, ynew);
  }
  
  // The argument passed to main must match the class name
  public static void main(String[] args) {
   PApplet.main("PhaseDiagramGrapher");
 }

  
}