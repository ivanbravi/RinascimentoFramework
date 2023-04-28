float[] xRange = new float[]{0, 120};
float[] yRange = new float[]{0, 40};

float[] mean = new float[]{(xRange[1]+xRange[0])/2, (yRange[1]+yRange[0])/2};

float lx  = 0.1f*(xRange[1]-xRange[0]);
float ly  = 0.1f*(yRange[1]-yRange[0]);
float angle = 0;

float xVar;
float yVar;
float cov;

float scaleX = 0.0f;
float scaleY = 0.0f;
float nstd = 3;

int MODE = 0;
// MODE == 0: mean input
// MODE == 1: xVar input 
// MODE == 2: yVar input
// MODE == 3: cov  input
String MODE_NAME = "MEAN";


void setup(){
  size(700,700);
  scaleX = width/xRange[1];
  scaleY = height/yRange[1];
  ellipseMode(RADIUS);
  background(255);
  
  statsSummary();
}

void draw(){ 
  background(255);
  noFill();
  strokeWeight(1);
  
  // center / mean
  stroke(255,0,0);
  point(mean[0]*scaleX, mean[1]*scaleY);
  
  // spread / variances
  stroke(0,0,255);
  translate(mean[0]*scaleX, mean[1]*scaleY);
  rotate(angle);
  ellipse(0, 0,nstd*sqrt(lx)*scaleX, nstd*sqrt(ly)*scaleY);
  
  // eigenvalues
  fill(0,255,0);
  noStroke();
  circle(lx*scaleX/2,0,4);
  circle(0, ly*scaleY/2,4);
  
}

void mouseReleased(){
  switch(MODE){
    case 0:
    mean[0] = scaleMouseX();
    mean[1] = scaleMouseY();
    break;
    case 1:
    lx = dist(mean[0], mean[1], mouseX/scaleX, mouseY/scaleY)*2;
    break;
    case 2:
    ly = dist(mean[0], mean[1], mouseX/scaleX, mouseY/scaleY)*2;
    break;
    case 3:
    float xDelta = mouseX - mean[0]*scaleX;
    float yDelta = mouseY - mean[1]*scaleY; 
    angle = atan2(yDelta, xDelta);
    break;
  }
  statsSummary();
}

void statsUpdate(){
  xVar = lx*cos(angle)*cos(angle)+ly*sin(angle)*sin(angle);
  yVar = lx*sin(angle)*sin(angle)+ly*cos(angle)*cos(angle);
  cov = -lx*cos(angle)*sin(angle)+ly*sin(angle)*cos(angle);
}

void statsSummary(){
  statsUpdate();
  println("\n------------- SUMMARY -------------");
  println("x_range = ["+xRange[0]+","+xRange[1]+"]");
  println("y_range = ["+yRange[0]+","+yRange[1]+"]");
  println("mean = ["+mean[0]+","+mean[1]+"]");
  println("cov  = "+cov);
  println("var_x = "+xVar);
  println("var_y = "+yVar);
  println("correlation angle= "+degrees(angle));
  println("------------ MODE:"+MODE_NAME+" ------------\n");
}

void keyReleased(){
  
  if(keyCode==LEFT){
    MODE = MODE-1<0 ?3:MODE-1;
  }
  
  if(keyCode==RIGHT){
    MODE = (MODE+1)%4;
  }
  
  switch(MODE){
    case 0:
    MODE_NAME = "MEAN";
    break;
    case 1:
    MODE_NAME = "xVAR";
    break;
    case 2:
    MODE_NAME = "yVAR";
    break;
    case 3:
    MODE_NAME = " COV";
    break;
  }
  statsSummary();
}

float scaleMouseX(){
  return mouseX/scaleX;
}

float scaleMouseY(){
  return mouseY/scaleY;
}
