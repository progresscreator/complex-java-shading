
import java.util.*;

public class Sphere extends RenderObject
{
  private double x, y, z, radius; // center and radius of sphere
  private double[][] Qmatrix;
  private double[][] Qtranslation;
  //  private GLUT glut;

  public Sphere( StringTokenizer stok, int id_ )
  {
//    glut = new GLUT();

    id = id_;

    stok.nextToken(); // strip away the token "mat"
    stok.nextToken(); // strip away the token "ID"
    matId = Integer.parseInt( stok.nextToken() );

    x  = Double.parseDouble( stok.nextToken() );
    y  = Double.parseDouble( stok.nextToken() );
    z  = Double.parseDouble( stok.nextToken() );
    radius  = Double.parseDouble( stok.nextToken() );
    
    Qmatrix = new double[4][4];
    Qtranslation = new double[4][4];
  }
  
  public Matrix createQMatrix(){
	  
	  double J = -Math.pow(radius, 2);
	  
	  for (int i=0; i < 4; ++i)
		  for (int j=0; j < 4; ++j){
			  if (i==3 && j==3) Qmatrix[i][j] = J;
			  else if (i==j && (i!=3 && j!=3)) Qmatrix[i][j] = 1;
			  else Qmatrix[i][j] = 0;
		  }
	  
	  Matrix Qm = new Matrix(Qmatrix);
	  
	  double D, G, I;
	  
	  D = -x;
	  G = -y;
	  I = -z;
	  
	  for (int i=0; i < 4; ++i)
		  for (int j=0; j < 4; ++j){
			  if (i==j) Qtranslation[i][j] = 1;
			  else Qtranslation[i][j] = 0;
		  }
	  Qtranslation[0][3] = D;
	  Qtranslation[1][3] = G;
	  Qtranslation[2][3] = I;
	  
	  Matrix Qt = new Matrix(Qtranslation);
	  Matrix Qttranspose = Qt.transpose();
	  
	  Matrix temp = Qttranspose.times(Qm);
	  Matrix Q = temp.times(Qt);
	  return Q;
  }

  public void print()
  {
    System.out.print( "Sphere ID:" + id + " mat ID:" + matId );
    System.out.print( " x:" + x + " y:" + y + " z:" + z);
    System.out.print( " radius:" + radius );
    System.out.println();

  }
  public double getX(){
	  return x;
  }
  public double getY(){
	  return y;
  }
  public double getZ(){
	  return z;
  }
  public double getRadius(){
	  return radius;
  }
}

