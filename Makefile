JAVA_FILES = PA4.java  RenderObject.java RenderObjectCollection.java Material.java MaterialCollection.java Light.java LightCollection.java Camera.java Sphere.java Cylinder.java CSG.java Ellipsoid.java Box.java


all :
	javac -classpath $(CLASSPATH) $(JAVA_FILES)

clean :
	rm -f *.class typescript

run :
	java -classpath $(CLASSPATH) -Djava.library.path=$(LIBPATH) Previewer model3.dat 


