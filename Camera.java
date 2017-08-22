// Based off previewer code by Tian Tai-Peng 
/*******************************************************************
  Camera setup 
*******************************************************************/
import java.util.*;
//import javax.media.opengl.*;
//import com.sun.opengl.util.GLUT;
//import javax.media.opengl.glu.GLU;


public class Camera 
{
  private enum ProjectionType { ORTHOGRAPHIC, PERSPECTIVE }

  private int id;
  public double xRes, yRes;
  public int viewportWidth, viewportHeight;
  public double cop[], look[], up[];
  public double focalLength;
  public double nearClipDistance, farClipDistance;
  public ProjectionType projection;

  public Camera()
  {
    cop  = new double[3];
    look = new double[3];
    up   = new double[3];
  }

  public void setResolution( StringTokenizer stok )
  {
    // Reads resolution values from input string. Called from
    // render.cpp:readModelFile().
    
    xRes = Double.parseDouble( stok.nextToken() );
    yRes = Double.parseDouble( stok.nextToken() );

  }

  public void setViewport( StringTokenizer stok )
  {
    viewportWidth  = Integer.parseInt( stok.nextToken() );
    viewportHeight = Integer.parseInt( stok.nextToken() );
  }

  public void setParameters( StringTokenizer stok )  
  {
    String pt = stok.nextToken();
    if (pt.equals( "perspective" ))
      projection = ProjectionType.PERSPECTIVE;
    else if (pt.equals( "orthographic" ))
      projection = ProjectionType.ORTHOGRAPHIC;

    cop[0] = Double.parseDouble ( stok.nextToken() );
    cop[1] = Double.parseDouble ( stok.nextToken() );
    cop[2] = Double.parseDouble ( stok.nextToken() );

    look[0] = Double.parseDouble ( stok.nextToken() );
    look[1] = Double.parseDouble ( stok.nextToken() );
    look[2] = Double.parseDouble ( stok.nextToken() );
    
    up[0] = Double.parseDouble ( stok.nextToken() );
    up[1] = Double.parseDouble ( stok.nextToken() );
    up[2] = Double.parseDouble ( stok.nextToken() );

    focalLength      = Double.parseDouble ( stok.nextToken() );
    nearClipDistance = Double.parseDouble ( stok.nextToken() );
    farClipDistance  = Double.parseDouble ( stok.nextToken() );

  }

  public void print()
  {
    System.out.println( "--------- Camera ---------" );
    System.out.print( "ID :" + id );
    System.out.print( " xRes:" + xRes + " yRes:" + yRes );
    System.out.print( " viewport w:" + viewportWidth + " h:" + viewportHeight);
    System.out.print( " cop:" + cop[0] + " " + cop[1] + " " +  cop[2] );
    System.out.print( " look:" + look[0] + " " + look[1] + " " +  look[2] );
    System.out.print( " up:" + up[0] + " " + up[1] + " " +  up[2] );
    System.out.print( " focal Length:" + focalLength );
    System.out.print( " near far Clip distance:" + nearClipDistance 
                                           + " " + farClipDistance);
    System.out.print( " projection type:");
    switch ( projection )
    {
      case ORTHOGRAPHIC : System.out.print( "Orthographic" ); break;
      case PERSPECTIVE  : System.out.print( "Perspective" ); break;
    }
    System.out.println();
  }

  public boolean isProjectionTypeOrthographic(){
	  if (ProjectionType.ORTHOGRAPHIC == projection) return true;
	  else return false; 
  }
  
  public double[] getCOP(){
	  return cop;
  }
  
  public double getXres(){
	  return xRes;
  }
  
  public double getYres(){
	  return yRes;
  }
  public double getFocalLength(){
	  return focalLength;
  }
  public double[] getLookAt(){
	  return look;
  }
  public double[] getUpVector(){ 
	  return up;
  }
}


