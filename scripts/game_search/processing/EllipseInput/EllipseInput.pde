import javax.swing.JFrame;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;

float[] xRange = new float[]{0, 40};
float[] yRange = new float[]{0, 120};

float[] center = new float[]{(xRange[1]+xRange[0])/2, (yRange[1]+yRange[0])/2};

float rx  = 0.1f*(xRange[1]-xRange[0]);
float ry  = 0.1f*(yRange[1]-yRange[0]);
float angle = 0;

float scaleX = 0.0f;
float scaleY = 0.0f;

boolean PRINT_SUMMARY=false;

int MODE = 0;
String MODE_NAME = "MEAN";
// MODE == 0: mean  input
// MODE == 1: rx    input 
// MODE == 2: ry    input
// MODE == 3: angle input

PGraphics miniature;
int miniatureSize = 400;
int space = 10;

void settings(){
//  noSmooth();
  size(700,700);
}

void setup(){
  scaleX = width/xRange[1];
  scaleY = height/yRange[1];
  
  float scale = min(scaleX, scaleY);
  
  surface.setResizable(true);
  surface.setSize((int)(scale/scaleX*width)+miniatureSize+space*2,(int)(scale/scaleY*height));
  
  scaleX = scale;
  scaleY = scale;
  
  ellipseMode(RADIUS);
  background(255);
  
  customStart();
  
  miniature = createGraphics(miniatureSize,miniatureSize);
  
  ellipseSummary();
}

void customStart(){
  center = new float[]{25.542856,21.599998};
  rx=6.3705955;
  ry=15.558502;
  angle = -0.77001464;
}

void draw(){
  background(100);
  fill(255);
  noStroke();
  rect(0,0,xRange[1]*scaleX, yRange[1]*scaleY);
  
  translate(center[0]*scaleX, center[1]*scaleY);
  rotate(angle);
  
  // major and minor axis
  highlightMode(1);
  line(0,0,rx*scaleX,0);
  
  highlightMode(2);
  line(0,0,0,ry*scaleY);
  
  // ellipse
  highlightMode(3);
  ellipse(0, 0,rx*scaleX, ry*scaleY);
  
  // center
  resetMatrix();
  highlightMode(0);
  point(center[0]*scaleX, center[1]*scaleY);
  
  // create miniature
  miniature.beginDraw();
  
  PImage tmpScreen = get(0,0,(int)(xRange[1]*scaleX),(int)(yRange[1]*scaleY));
  miniature.image(tmpScreen, 0,0,miniatureSize, miniatureSize);
  miniature.endDraw();
  
  image(miniature, (int)(xRange[1]*scaleX)+space,space,miniatureSize,miniatureSize);
  noFill();
  strokeWeight(1);
  stroke(0,255,0);
  rect(0,0,(int)(xRange[1]*scaleX),(int)(yRange[1]*scaleY));
  
  // cursor
  stroke(150,100);
  strokeWeight(1);
  line(center[0]*scaleX, center[1]*scaleY, min(mouseX, xRange[1]*scaleX), min(mouseY, yRange[1]*scaleY));
}

void highlightMode(int mode){
  noFill();
  if(MODE==mode){
    strokeWeight(2);
    stroke(255,0,0);
  }else{
    strokeWeight(1);
    stroke(0,0,255);
  }
}

void mouseReleased(){
  switch(MODE){
    case 0:
    center[0] = min(scaleMouseX(), xRange[1]);
    center[1] = min(scaleMouseY(), yRange[1]);
    break;
    case 1:
    rx = dist(center[0], center[1], mouseX/scaleX, mouseY/scaleY);
    break;
    case 2:
    ry = dist(center[0], center[1], mouseX/scaleX, mouseY/scaleY);
    break;
    case 3:
    float xDelta = mouseX - center[0]*scaleX;
    float yDelta = mouseY - center[1]*scaleY; 
    angle = atan2(yDelta, xDelta);
    break;
  }
  ellipseSummary();
}

void ellipseSummary(){
  if(!PRINT_SUMMARY) return;
  println("\n------------- SUMMARY -------------");
  println("x_range     = ["+xRange[0]+","+xRange[1]+"]");
  println("y_range     = ["+yRange[0]+","+yRange[1]+"]");
  println("center      = ["+center[0]+","+center[1]+"]");
  println("angle[rad]  = "+angle);
  println("x semi-axis = "+rx);
  println("y semi-axis = "+ry);
  println("----------- MODE:"+MODE_NAME+" -----------\n");
}

void printJavaVariables(){
  println("\n-------------------------- JAVA VARIABLE --------------------------");
  println("EllipseGrid eg = new EllipseGrid(");
  println("\t\tnew Point2D.Double("+center[0]+","+center[1]+"),");
  println("\t\t"+rx+",");
  println("\t\t"+ry+",");
  println("\t\t"+angle+",");
  println("\t\tSIZE");
  println("\t);");
  println("---------------------------------------------------------------------");
}

void keyReleased(){
  
  if(keyCode==LEFT || keyCode==RIGHT){
    if(keyCode==LEFT){
      MODE = MODE-1<0 ?3:MODE-1;
    }
    
    if(keyCode==RIGHT){
      MODE = (MODE+1)%4;
    }
    
    
    
    switch(MODE){
      case 0:
      MODE_NAME = " MEAN ";
      break;
      case 1:
      MODE_NAME = "X AXIS";
      break;
      case 2:
      MODE_NAME = "Y AXIS";
      break;
      case 3:
      MODE_NAME = " ANGLE";
      break;
    }
    ellipseSummary();
  }else if(keyCode==ENTER){
    printJavaVariables();
  }
}

float scaleMouseX(){
  return mouseX/scaleX;
}

float scaleMouseY(){
  return mouseY/scaleY;
}
