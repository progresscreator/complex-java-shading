import java.io.File;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class ImageToFile
{
  public static void main( String[] args )
  {
    int width =  255;
    int height = 300; 
	BufferedImage image = new BufferedImage( width, height, 
                                             BufferedImage.TYPE_INT_ARGB );

    // Create a shade of blue to magenta 
    byte mask = (byte)(0xff);

    for (int i=0; i < height; ++i ) // row
      for (int j=0; j < width; ++j) // col
      {
        int red   =   j & mask;  // retain the lowest btye
        int green =   0 & mask;
        int blue  = 255 & mask;
      
        // pack the rgb values into an int
        image.setRGB( j, i, ( 0xFF000000  )   |  // no transparency
                            ( red   << 16 )   |
                            ( green <<  8 )   |
                              blue            ); 
      }



    // Dumping the image to file
    File outputFile = new File( "dump.png" );
    try {
      javax.imageio.ImageIO.write( image, "PNG", outputFile );
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}