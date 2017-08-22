import java.util.*;
//import javax.media.opengl.GL;
//import com.sun.opengl.util.GLUT;

public class Ellipsoid extends RenderObject
{
  private double x,y,z;	     // Center of the ellpsoid.
  private double rx,ry,rz;   // Radii of the ellipsoid.
  private double[][] Qmatrix;
  private double[][] Qtranslation;
//  private GLUT glut;

  public Ellipsoid( StringTokenizer stok, int id_ )
  {
//    glut = new GLUT();
    
    id = id_;

    stok.nextToken(); // strip away the token "mat"
    stok.nextToken(); // strip away the token "ID"
    matId = Integer.parseInt( stok.nextToken() );

    x  = Double.parseDouble( stok.nextToken() );
    y  = Double.parseDouble( stok.nextToken() );
    z  = Double.parseDouble( stok.nextToken() );
    rx = Double.parseDouble( stok.nextToken() );
    ry = Double.parseDouble( stok.nextToken() );
    rz = Double.parseDouble( stok.nextToken() );
    
    Qmatrix = new double[4][4];
    Qtranslation = new double[4][4];
  }

  public Matrix createQMatrix(){

	  double A, E, H;
	  
	  A = 1/Math.pow(rx, 2);
	  E = 1/Math.pow(ry, 2);
	  H = 1/Math.pow(rz, 2);
	  
	  for (int i=0; i < 4; ++i)
		  for (int j=0; j < 4; ++j){
			  if (i==3 && j==3) Qmatrix[i][j] = -1;
			  else Qmatrix[i][j] = 0;
		  }
	  Qmatrix[0][0] = A;
	  Qmatrix[1][1] = E;
	  Qmatrix[2][2] = H;
	  
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
    System.out.print( "Ellipsoid ID:" + id + " mat ID:" + matId );
    System.out.print( " x:" + x + " y:" + y + " z:" + z);
    System.out.print( " rx:" + rx + " ry:" + ry + " rz:" + rz);
    System.out.println();
  }
}

