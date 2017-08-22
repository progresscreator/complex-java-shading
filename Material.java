
/*******************************************************************
  Describes a  Material 
*******************************************************************/
import java.util.* ;
//import javax.media.opengl.GL;

// Material class reads and stores the parameters for a material.
public class Material
{
  public int id; // Material id
  public float rgb[] = { 0.0f, 0.0f, 0.0f };
  public float Ka, Kd, Ks, ns, Kt, Kr, Krefract;

  // Ka Kd Ks are the coefficients of ambient, diffuse, and specular reflection
  // ns is the specular reflection exponent
  // Kt Kr Krefract are the coefficients of transmission, reflection, and
  // refraction

  public void print()
  {
    System.out.print( "ID :" + id);
    System.out.print( " rgb:" + rgb[0] + " " + rgb[1] + " " + rgb[2] );
    System.out.print( " Ka:" + Ka + " Ks:" + Ks + " ns:" + ns);
    System.out.print( " Kt:" + Kt + " Kr:" + Kr + " Krefrace:" + Krefract);
    System.out.println();
  }

  public Material( StringTokenizer stok )
  {
    stok.nextToken(); // strip away the token "ID"
    id = Integer.parseInt( stok.nextToken() );
    
    for (int i=0; i<3; ++i)
      rgb[i] = Float.parseFloat( stok.nextToken() );

    Ka = Float.parseFloat( stok.nextToken() );
    Kd = Float.parseFloat( stok.nextToken() );
    Ks = Float.parseFloat( stok.nextToken() );
    ns = Float.parseFloat( stok.nextToken() );
    Kt = Float.parseFloat( stok.nextToken() );
    Kr = Float.parseFloat( stok.nextToken() );
    Krefract = Float.parseFloat( stok.nextToken() );
  }

}


