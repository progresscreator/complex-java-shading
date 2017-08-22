
import java.util.*;
import java.awt.image.BufferedImage;
import java.io.*;

public class PA4 {

	private RenderObjectCollection objects;
	private MaterialCollection materials;
	private LightCollection lights;
	private Camera camera;
	private String filename;
	private int imageheight, imagewidth;
	private Pixel[][] pixelarray;
	private double[] cop, look, up, u, n, v, startingpoint, pixelpoint;
	private double xRes, yRes;
	private double focalLength;
	private ArrayList<Light> lightArray;
 
	private class Pixel{
		public float red, green, blue;
		
		public Pixel(){
			red = green = blue = 0;
		}
		
		public Pixel(float red, float green, float blue){
			this.red = red;
			this.green = green;
			this.blue = blue;
		}
	}
	
	private class Ray{
		
		public double[] p;
		public double[] u;	
		
		public Ray(double[] p, double[] u){
			
			this.p = new double[4];
			this.u = new double[4];
			
			for (int i=0; i < p.length; ++i){
				this.p[i] = p[i];
			}
			for (int i=0; i < u.length; ++i){
				this.u[i] = u[i];
			}
			p[3] = 1;
			u[3] = 0;
		}	
	}
	
	private class Intersection{
		public double distance;
		public int id;
		public double[] normal;
		public Intersection(double distance, int id, double[] normal){
			this.distance = distance;
			this.id = id;
			this.normal = new double[4];
			for (int i=0; i < normal.length; ++i) this.normal[i] = normal[i];
			normal[3] = 0;
		}
	}
	
	public PA4(String filename){
		objects   = new RenderObjectCollection();
	    materials = new MaterialCollection();
	    lights    = new LightCollection();
	    camera    = new Camera();
	    this.filename = filename;
	}
	
	public static void main(String[] args) {
		
		if (args.length < 1) {
		      System.out.println( "Usage: viewer <model_file>\n" );
		      System.exit(1); 
		    }
		
		PA4 P = new PA4(args[0]);
		P.run();
	}

	public void run(){
		
		readModelFile(filename);
		
		pixelarray = new Pixel[imageheight][imagewidth];
		startingpoint = new double[3];
		pixelpoint = new double[3];
		// finding the values for the starting point (in WC) of the first pixel
		for (int i=0; i < startingpoint.length; ++i) startingpoint[i] = ((-imagewidth/2)*xRes*u[i]) + ((imageheight/2)*yRes*v[i]) + focalLength*n[i] + cop[i];
		
		Ray ray;
		
		for (int i=0; i < pixelarray.length; ++i){
			for (int p=0; p < pixelpoint.length; ++p) pixelpoint[p] = startingpoint[p] - yRes*i*v[p]; // from notes in class
			for (int j=0; j < pixelarray[i].length; ++j){
				ray = computeRay(pixelpoint);
				pixelarray[i][(imagewidth-1)-j] = computePixelColor(ray, 1);
				for (int q=0; q < pixelpoint.length; ++q) pixelpoint[q] += xRes*u[q];
			}
		}
		
		// writing image to file
		writeImageFile();
		
		// used for debugging purposes
//		objects.print();
//		materials.print();
//		lights.print();
//		camera.print();
	}
	
	private Pixel computePixelColor(Ray ray, int depth){
//		return new Pixel(100, 0, 0); // used for debugging purposes
		// At^2 + Bt + C where t is the distance from the image plane to the point	
		Pixel pixel = new Pixel();
		
		Intersection intersection = computeIntersection(ray);
		if (intersection.id != -1){
			RenderObject ro = objects.get_obj_by_id(intersection.id);
			pixel = computeShading(ro, ray, intersection.distance, intersection.normal, depth);	
		}
//		Material mat = materials.getMaterial(ro.matId);
//		pixel.red = mat.rgb[0];
//		pixel.green = mat.rgb[1];
//		pixel.blue = mat.rgb[2];
		return pixel;
	}
	
	private Intersection computeIntersection(Ray ray){
		double[][] u = new double[4][1];
		double[][] p = new double[4][1];
		for (int i=0; i < ray.u.length; ++i){
			u[i][0] = ray.u[i];
			p[i][0] = ray.p[i];
		}
		Matrix umatrix = new Matrix(u);
		Matrix pmatrix = new Matrix(p);
		Matrix utranspose = umatrix.transpose();
		Matrix ptranspose = pmatrix.transpose();
		Matrix Am, Bm, Cm;
		double A, B, C, t, t2, distance = -1, tempdistance;
		int currentid = -1; // will draw the background color if it doesn't intersect anything
		Matrix Q;
		
		for (int i=0; i < objects.num_obj(); ++i){
			RenderObject ro = objects.get_obj_by_id(i);
			Q = ro.createQMatrix();
			
			// A = u^t*Q*u
			Am = utranspose.times(Q);
			Am = Am.times(umatrix);
			A = Am.getValue();
			
			// B = 2*u^t*Q*p
			Bm = utranspose.times(Q);
			Bm = Bm.times(pmatrix);
			B = 2*Bm.getValue();
			
			// C = p^t*Q*p
			Cm = ptranspose.times(Q);
			Cm = Cm.times(pmatrix);
			C = Cm.getValue();
			
			// t = Max of quadratic equation
			if ((((Math.pow(B, 2)) - 4*A*C) >= 0) && ((2*A) != 0)){
				t = (-B + Math.sqrt((Math.pow(B, 2)) - 4*A*C)) / (2*A);
				t2 = (-B - Math.sqrt((Math.pow(B, 2)) - 4*A*C)) / (2*A);
				
				if (t > 0 && t2 > 0) tempdistance = Math.min(t, t2);
				else if (t > 0) tempdistance = t;
				else tempdistance = t2;
				
				if (tempdistance < distance || distance == -1){
					distance = tempdistance;
					currentid = i;
				}
			}
		} // end for loop
		
		double[] normal = new double[4];
		if (currentid >= 0){
			RenderObject ro = objects.get_obj_by_id(currentid);
			Q = ro.createQMatrix();

			// point x on the surface of the object
			double[][] x = new double[4][1];
			for (int i=0; i < ray.u.length; ++i) x[i][0]= ray.p[i] + distance*ray.u[i];
			x[3][0] = 1;

			Matrix xmatrix = new Matrix(x);
			Matrix normalmatrix = Q.times(xmatrix);
			normal = normalmatrix.getVector();
			normal[3] = 0;
			double normallength = Math.sqrt(Math.pow(normal[0], 2) + Math.pow(normal[1], 2) + Math.pow(normal[2], 2));
			for (int j=0; j < ray.u.length; ++j) normal[j] = normal[j]/normallength;
		}
		return new Intersection(distance, currentid, normal);
	}
	
	private Pixel computeShading(RenderObject obj, Ray ray, double distance, double[] normal, int depth){
		
		Pixel pixel = new Pixel();
		Pixel rpixel = new Pixel();
		Ray rRay, tRay, sRay; 
		double[] x = new double[4];
		// point on surface
		for (int i=0; i < ray.u.length; ++i) x[i]= ray.p[i] + distance*ray.u[i];
		x[3] = 1;
		// set material object
		Material mat = materials.getMaterial(obj.matId);
		pixel.red = mat.rgb[0]*mat.Ka;
		pixel.green = mat.rgb[1]*mat.Ka;
		pixel.blue = mat.rgb[2]*mat.Ka;
		
		double[] middle = new double[3];
		// compute shadow component of illumination equation (with diffuse and specular terms)
		for (int i=0; i < lightArray.size(); ++i){
			double temp = 0;
			double[] temp2 = new double[3];
			Light light = lightArray.get(i);
			// form the directional vector from point to the light (unitized)
			double[] sVector = new double[4];
			for (int j=0; j < light.pos.length; ++j) sVector[j] = light.pos[j] - x[j];
			double sVectorlength = Math.sqrt(Math.pow(sVector[0], 2) + Math.pow(sVector[1], 2) + Math.pow(sVector[2], 2));
			for (int j=0; j < ray.u.length; ++j) sVector[j] = sVector[j]/sVectorlength;
			sVector[3] = 0;
			// create the shadow ray
			sRay = new Ray(x, sVector);
			// check to see if pixel is in shadow
			double[] shadowVector = new double[4];
			for (int j=0; j < ray.u.length; ++j) shadowVector[j] = -sVector[j];  
			Ray shadowRay = new Ray(light.pos, shadowVector);
			Intersection checkintersection = computeIntersection(shadowRay);
			if (checkintersection.id != obj.id){
				continue;
			}
			// find the dot product (check if dot product of normal and direction to light is positive ==> if so add part to shadow illum)
			double dotproduct = 0;
			for (int j=0; j < sVector.length; ++j) dotproduct += normal[j]*sVector[j];
			if (dotproduct > 0){
				// finding "middle" by adding "temp" value
				double dlight = 0;
				for (int j=0; j < light.pos.length; ++j) dlight += Math.pow((light.pos[j] - x[j]), 2);
				dlight = Math.sqrt(dlight);
				double fd = 1/(light.radialAttenuation[0] + (light.radialAttenuation[1]*dlight) + (light.radialAttenuation[2]*Math.pow(dlight, 2)));
				fd = Math.min(1, fd);
				// finding diffuse term ==> Kd(N*Li)
				double[] Li = new double[4];
				for (int j=0; j < sRay.u.length; ++j) Li[j] = sRay.u[j];
				Li[3] = 0;
				double NLi = 0;
				for (int j=0; j < normal.length; ++j) NLi += normal[j]*Li[j];
				temp += mat.Kd*NLi;
				// finding specular term ==> Ks(V*Ri)^ns
				double[] Ri = new double[4];
				for (int j=0; j < normal.length; ++j) Ri[j] = 2*NLi*(normal[j]) - Li[j];
				double Rilength = Math.sqrt(Math.pow(Ri[0], 2) + Math.pow(Ri[1], 2) + Math.pow(Ri[2], 2));
				for (int j=0; j < sRay.u.length; ++j) Ri[j] = Ri[j]/Rilength;
				Ri[3] = 0;
				double VRi = 0;
				for (int j=0; j < normal.length; ++j) VRi += ray.u[j]*Ri[j];
				VRi = Math.pow(VRi, mat.ns);
				temp += mat.Ks*VRi;
				temp *= fd;
				for (int j=0; j < 3; ++j) temp2[j] = temp*light.rgb[j];
			}
			for (int j=0; j < 3; ++j) middle[j] += temp2[j];
		}
		pixel.red += middle[0];
		pixel.green += middle[1];
		pixel.blue += middle[2];
		
		// reflection and transparent computation
		if (depth < 3){
			if (mat.Kr > 0){ // if object is reflective
//				double[] Li = new double[4];
//				for (int j=0; j < normal.length; ++j) Li[j] = -ray.u[j]; // want incidence unit vector pointing out from point
//				Li[3] = 0;
//				double NLi = 0;
//				for (int j=0; j < normal.length; ++j) NLi += normal[j]*Li[j];
//				double[] Ri = new double[4];
//				for (int j=0; j < normal.length; ++j) Ri[j] = 2*NLi*(normal[j]) - Li[j];
//				double Rilength = Math.sqrt(Math.pow(Ri[0], 2) + Math.pow(Ri[1], 2) + Math.pow(Ri[2], 2));
//				for (int j=0; j < normal.length; ++j) Ri[j] = Ri[j]/Rilength;
//				Ri[3] = 0;
//				rRay = new Ray(x, Ri);
//				rpixel = computePixelColor(rRay, depth +1);
//				pixel.red += mat.Kr*rpixel.red; 
//				pixel.green += mat.Kr*rpixel.green;
//				pixel.blue += mat.Kr*rpixel.blue;
			}
			if (mat.Kt > 0){ // if object is transparent
				
			}
		}
		
		if (pixel.red > 1) pixel.red = 1;
		if (pixel.green > 1) pixel.green = 1;
		if (pixel.blue > 1) pixel.blue = 1;
		return pixel;
	}
	
	private Ray computeRay(double[] pixelpoint){
		
		double[] start = new double[4];
		double[] u = new double[4];
		
		if (camera.isProjectionTypeOrthographic()){ // if the camera is orthographic
			for (int i=0; i < u.length-1; ++i) u[i] = n[i];
			u[3] = 0;
			start[0] = pixelpoint[0];
			start[1] = pixelpoint[1];
			start[2] = pixelpoint[2];
			
		}
		else { // if the camera is perspective ==> start point is COP
			for (int i=0; i < start.length-1; ++i){
				start[i] = cop[i];
			}
			for (int i=0; i < u.length-1; ++i) u[i] = pixelpoint[i] - cop[i];
			double ulength = Math.sqrt(Math.pow(u[0], 2) + Math.pow(u[1], 2) + Math.pow(u[2], 2));
			// normalized u vector
			u[0] = u[0]/ulength;
			u[1] = u[1]/ulength;
			u[2] = u[2]/ulength;
			u[3] = 0;
		}
		start[3] = 1;
		Ray ray = new Ray(start, u);
		return ray;
		
	}
	
	private void writeImageFile(){
		int width =  imagewidth;
	    int height = imageheight; 
		BufferedImage image = new BufferedImage( width, height, 
	                                             BufferedImage.TYPE_INT_ARGB );

	    for (int i=0; i < height; ++i ) // row
	      for (int j=0; j < width; ++j) // col
	      {
	    	  int red   =   (int)Math.floor(pixelarray[i][j].red*255);  // retain the lowest btye
	          int green =   (int)Math.floor(pixelarray[i][j].green*255);
	          int blue  = (int)Math.floor(pixelarray[i][j].blue*255);
	    	  
	          // pack the rgb values into an int
	          image.setRGB( j, i, ( 0xFF000000  )   |  // no transparency
	                              ( red   << 16 )   |
	                              ( green <<  8 )   |
	                                blue            ); 
	      }


	    // Dumping the image to file
	    File outputFile = new File( "ModelFile.png" );
	    try {
	      javax.imageio.ImageIO.write( image, "PNG", outputFile );
	    } catch (IOException e) {
	      e.printStackTrace();
	    }
	}
	
	private void readModelFile(String filename)
	{
		try 
		{
			BufferedReader input = new BufferedReader( new FileReader(filename) );

			try 
			{
				String line = null;
				while ( (line = input.readLine()) != null )
				{
					StringTokenizer stok = new StringTokenizer( line );
					if (stok.hasMoreTokens())
					{
						String tok = stok.nextToken();

						if (tok.equals( "obj" )) objects.newObject( stok );
						else if (tok.equals( "light" )) lights.newLight( stok );
						else if (tok.equals( "mat" )) materials.newMaterial( stok );
						else if (tok.equals( "resolution" )) camera.setResolution( stok );
						else if (tok.equals( "camera" )) camera.setParameters( stok );
						else if (tok.equals( "viewport" )) camera.setViewport( stok );
						else if (tok.equals( "render" )) objects.setRenderObjIds( stok );
					}

				}
			}
			finally
			{
				input.close();
			}

		}
		catch (IOException ex) 
		{
			System.err.println( "File input error" );
		}

		objects.reorganizeObjects(); // remove objects that are not specified 
		// in render option.
		
		imageheight = camera.viewportHeight;
		imagewidth = camera.viewportWidth;
		xRes = camera.getXres();
		yRes = camera.getYres();
		cop = camera.getCOP();
		look = camera.getLookAt();
		up = camera.getUpVector();
		focalLength = camera.isProjectionTypeOrthographic() ? 0 : camera.getFocalLength();
		lightArray = lights.getLightList();
		
		u = new double[3]; // x view
		v = new double[3]; // y view
		n = new double[3]; // z view
		
		// n = lookat vector normalized
		for (int i=0; i < n.length; ++i) n[i] = look[i] - cop[i];
		double nlength = Math.sqrt(Math.pow(n[0], 2) + Math.pow(n[1], 2) + Math.pow(n[2], 2));
		for (int i=0; i < n.length; ++i) n[i] = n[i]/nlength;
		
		// u = upvector x n / length of upvector
		u[0] = (up[1]*n[2]) - (up[2]*n[1]);
		u[1] = (up[2]*n[0]) - (up[0]*n[2]);
		u[2] = (up[0]*n[1]) - (up[1]*n[0]);
		
		double ulength = Math.sqrt(Math.pow(u[0], 2) + Math.pow(u[1], 2) + Math.pow(u[2], 2));
		for (int i=0; i < u.length; ++i) u[i] = u[i]/ulength;
		
		// v = n x u
		v[0] = (n[1]*u[2]) - (u[1]*n[2]);
		v[1] = (n[2]*u[0]) - (n[0]*u[2]);
		v[2] = (n[0]*u[1]) - (n[1]*u[0]);
		
	}
	
	
	
}
