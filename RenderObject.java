
import javax.media.opengl.GL;

public abstract class RenderObject
{
    public int id, matId;  // The object's unique ID and its material ID.

    public abstract void print();
    
    public abstract Matrix createQMatrix();
}


