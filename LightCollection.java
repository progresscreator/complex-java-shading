/*******************************************************************
  Container class to hold all the Lights 
*******************************************************************/
import java.util.*;

public class LightCollection 
{
  private ArrayList<Light> lightArray;

  public LightCollection()
  {
    lightArray = new ArrayList<Light>();
  }
  

  public void newLight( StringTokenizer stok )
  {
    Light light = new Light( stok /*, glLightId */);

    // Check for duplicate light IDs
    for ( Light l : lightArray )
      if ( l.id == light.id )
      {
        System.err.println( "Duplicate ID: " + l.id 
                            + " detected. Object not inserted " );
        return;
      }

    lightArray.add( light );  
  }

  public ArrayList<Light> getLightList(){
	  return lightArray;
  }
  
  public void print()
  {
    System.out.println( "---------- LightCollection ----------" );
    for ( Light l : lightArray )
      l.print();
  }
}


