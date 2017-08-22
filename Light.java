/*******************************************************************
   Single Light
*******************************************************************/
import java.util.*;
//import javax.media.opengl.*;

public class Light  
{

  public int id;

  public enum LightType { POINT, INFINITE, AMBIENT }
  public LightType type;
  public double pos[] = { 0, 0, 0, 0}; // Location
  public double dir[] = { 0, 0, 0, 0}; // light direction
  public double rgb[] = { 0, 0, 0, 0}; // color of light

  public boolean shadow_on; // Turns on or off shadows

  public double radialAttenuation[] = { 0, 0, 0}; // Pg 560 eq 10-1 of H&B text
  public double angularAttenuation;   // Pg 561 eq 10-4 of H&B text

  public void print()
  {
    System.out.print( "Light ID:" + id );
    System.out.print( " type:" );
    switch (type)
    {
      case POINT    : System.out.print( "POINT" ); break; 
      case INFINITE : System.out.print( "INFINITE" ); break; 
      case AMBIENT  : System.out.print( "AMBIENT" ); break; 
    }
    System.out.print( " pos:" + pos[0]+","+pos[1]+","+pos[2]+","+pos[3] );
    System.out.print( " dir:" + dir[0]+","+dir[1]+","+dir[2]+","+dir[3] );
    System.out.print( " rgb:" + rgb[0]+","+rgb[1]+","+rgb[2]+","+rgb[3] );
    System.out.print( " radialAttenuation:" + radialAttenuation[0]
                       +","+radialAttenuation[1]+","+radialAttenuation[2]);
    System.out.print( " angularAttenuation:" + angularAttenuation );
//    System.out.print( " glLightId:" + glLightId );
    System.out.println();

  }

  public Light( StringTokenizer stok )
  {

    stok.nextToken(); // Strip away the token "ID"
    id = Integer.parseInt( stok.nextToken() );

    String type_str = stok.nextToken();
    if ( type_str.equals( "pnt" )) type = LightType.POINT;
    else if ( type_str.equals( "inf" )) type = LightType.INFINITE;
    else if ( type_str.equals( "amb" )) type = LightType.AMBIENT;
    else 
      System.err.println( "Light Type : " + type_str + " not recognized. ");
   
    for (int i=0; i<3; ++i)
      pos[i] = Double.parseDouble( stok.nextToken() );

    if ( type == LightType.INFINITE )
      pos[3] = 0; // 0 if it is infinite
    else
      pos[3] = 1; // 1 if it is not infinite

    for (int i=0; i<3; ++i)
      dir[i] = Double.parseDouble( stok.nextToken() );
    dir[3] = 1;
    
    for (int i=0; i<3; ++i)
      rgb[i] = Double.parseDouble( stok.nextToken() );
    rgb[3] = 1;

    /* added myself */
    for (int i=0; i<3; ++i)
    	radialAttenuation[i] = Double.parseDouble( stok.nextToken() );
    
    angularAttenuation = Double.parseDouble( stok.nextToken() );
    /* end of my addition */
    
    String shadow_str = stok.nextToken();
    if ( shadow_str.equals( "shadow_on" ))
      shadow_on = true;
    else
      shadow_on = false;

  }

}


