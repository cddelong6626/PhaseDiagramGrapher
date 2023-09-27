/*
 *  Cole Delong
 *  Grid Object
 *  10-18-22
 *  
 *  Contains methods that handle drawing the background of the phase diagram.
 *  
 */

import processing.core.PApplet;
import processing.core.PVector;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class Grid {

  // PApplet window to draw onto
  PApplet window;
  
  // Grid constraints
  private final float XMIN;
  private final float XMAX;
  private final float YMIN;
  private final float YMAX;
  private final float XRANGE;
  private final float YRANGE;
  
  // Axis constraints
  private final float XSTEP;
  private final float YSTEP;
  
  // DE's
  DifferentialEquation f;
  DifferentialEquation g;
  
  public Grid(PApplet window, DifferentialEquation f, DifferentialEquation g, 
      float XMIN, float XMAX, float YMIN, float YMAX, float XRANGE, 
      float YRANGE, float XSTEP, float YSTEP) {
    
    this.window = window;
    
    this.f = f;
    this.g = g;
    
    this.XMIN = XMIN;
    this.XMAX = XMAX;
    this.YMIN = YMIN;
    this.YMAX = YMAX;
    this.XRANGE = YRANGE;
    this.YRANGE = YRANGE;
    
    this.XSTEP = XSTEP;
    this.YSTEP = YSTEP;
    
  }
  
  //Draws a line in the translated coordinate system
  private void convertedLine(float x0, float y0, float xf, float yf) {
    window.line(graphConversion(x0, y0).x, 
        graphConversion(x0, y0).y, 
        graphConversion(xf, yf).x, 
        graphConversion(xf, yf).y);
  }
  
  //Draw an arrow from an inital point to a displaced point
  void drawArrow(float x0, float y0, PVector v) {
    x0 = graphConversion(x0, y0).x;
    y0 = graphConversion(x0, y0).y;
    float angle = -v.heading();
    v.set(graphConversion(v));
    float len = (float) (3*Math.log(v.mag())+8);
    window.pushMatrix();
    window.translate(x0, y0);
    window.rotate(angle);
    window.line(0,0,len,0);
    window.line(len, 0, len-2, -2);
    window.line(len, 0, len-2, 2);
    window.popMatrix();
  }
 void drawArrow(float x0, float y0, float dx, float dy) {
    drawArrow(x0, y0, new PVector(dx, dy));
 }
  
  //Draw x and y axis and background grid
  public void drawAxis() {
        
    // Draw x and y axis
    window.strokeWeight(2);
    window.stroke(0, 255);
    convertedLine(0, YMIN, 0, YMAX);
    convertedLine(XMIN, 0, XMAX, 0);
    window.fill(0);
    window.text("x1", graphConversion(XMAX-XRANGE/40, 0).x, 
        graphConversion(0, YRANGE/60).y);
    window.text("x2", graphConversion(-XRANGE/50, 0).x, 
        graphConversion(0, YMAX-YRANGE/40).y);
    
    // Draw background grid
    window.strokeWeight(1);
    window.stroke(0, 100);
    for (float x = XMIN; x < XMAX; x += XSTEP) {
      convertedLine(x, YMIN, x, YMAX);
    }
    for (float y = YMIN; y < YMAX; y += YSTEP) {
      convertedLine(XMIN, y, XMAX, y);
    }
    
    // Draw axis number lables
    for (float x = XMIN; x < XMAX; x += XSTEP*2) {
      if (Math.abs(x) > XSTEP/2) {
        BigDecimal bd = new BigDecimal(x);
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        window.text(bd.toString(), graphConversion(x, 0).x - 10, 
            graphConversion(0, 0).y + 15);
      }
    }
    for (float y = YMIN; y <= YMAX; y += YSTEP*2) {
      if (Math.abs(y) > YSTEP/2) {
        BigDecimal bd = new BigDecimal(y);
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        window.text(bd.toString(), graphConversion(0, 0).x + 5, 
            graphConversion(0, y).y + 5);
      }
    }
    
    // Draw vector field
    window.strokeWeight(1);
    window.stroke(0, 0, 255, 75);
    for (float x = XMIN; x <= XMAX; x += XSTEP) {
      for (float y = YMIN; y < YMAX; y += YSTEP) {
        drawArrow(x, y, g.evaluate(x, y), f.evaluate(x, y));
      }
    }
  }
  
  // Converts x and y coordinates in the cartesian plane to x and 
  // y coordinates in the window
  public PVector graphConversion(float x, float y) { 

    
    float xnew = PApplet.map(x, XMIN, XMAX, 0, window.width);
    float ynew = PApplet.map(y, YMIN, YMAX, window.height, 0);
    
    return new PVector(xnew, ynew);
    
  }
  public PVector graphConversion(PVector r) {
 
    
    float xnew = PApplet.map(r.x, XMIN, XMAX, 0, window.width);
    float ynew = PApplet.map(r.y, YMIN, YMAX, window.height, 0);
    
    return new PVector(xnew, ynew);
    
  }
  
}
