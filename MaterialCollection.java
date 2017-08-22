
/*******************************************************************
 
  Container class to hold all the Materials 
*******************************************************************/
import java.util.* ;
import javax.media.opengl.GL;

public class MaterialCollection 
{
  private ArrayList<Material> materialArray;

  public MaterialCollection()
  {
    materialArray = new ArrayList<Material>();
  }

  public void newMaterial( StringTokenizer stok ) 
  {
    Material mat = new Material( stok );

    // check for duplicate id
    for ( Material m : materialArray )
      if ( m.id == mat.id )
      {
        System.err.println( "Duplicate ID: " + m.id 
                            + " detected. Material not inserted.");
        return;
      }

    materialArray.add( mat );
  }

  public Material getMaterial(int id){
	  Material result = null;

	    for ( Material m : materialArray )
	      if ( m.id == id )
	      {
	        result = m;
	        break;
	      }
	    return result;
  }
  
  public void print()
  {
    System.out.println( "--------- Materials ----------" );
    for ( Material m : materialArray )
      m.print();
  }
}


