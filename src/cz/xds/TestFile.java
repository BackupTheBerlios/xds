package cz.xds;

/**
 * Test souboru
 */
import junit.framework.TestCase;



public class TestFile extends TestCase {
    
    private File file1 = null;
    private File file2 = null;
    private File file3 = null;
    private File file4 = null;
    private Directory root = null;
    private Directory level1 = null;
    private IDFactory fact = null;
    private Attributes attrib = null;
    private byte[] data = null;
    
    protected void setUp() throws Exception {
        super.setUp();
        fact = new IDFactory();
        attrib = new Attributes();
        root = new Directory(fact);
        level1 = new Directory("level1", root, attrib);
        data = new byte[1];
        data[0]=1;
        file1 = new File("file1","txt", attrib, root, data);
    }
    
    protected void tearDown() throws Exception {
        file1 = null;
        file2 = null;
        root = null;
        fact = null;
        attrib = null;
        data = null; 
    }
    
    public void testConstruction(){
        Attributes expectedAttrib = new Attributes(false,false);
        assertEquals(file1.getName(),"file1");
        assertEquals(file1.type,"txt");
        assertTrue(file1.getAttributes().equals(expectedAttrib));
        assertFalse(file1.data == null);
        assertTrue(file1.isDeletable());
        assertEquals(file1.getParent(),root);
    }
    
    public void testCopy() throws Exception{
        file3 = (File)file1.copy(level1);
        assertEquals(file3.name, file1.name);
        assertEquals(file3.type,file1.type);
        assertEquals(file3.attributes,file1.attributes);
        assertEquals(file3.data,file1.data);
        assertEquals(file3.getParent(),level1);
        assertEquals(level1.findItem(file3.getName()),file3);  
        //file4 = (File)file1.copy(level1); neprojde
        //file4 = (File)file1.copy(root); projde
        /*assertEquals(file4.name, file1.name);
        assertEquals(file4.type,file1.type);
        assertEquals(file4.attributes,file1.attributes);
        assertEquals(file4.getParent(),root);*/
    }
    
    public void testDelete() throws Exception{
        file1.delete();
        assertEquals(file1.data, null);
        assertEquals(file1.links.size(),0);
    }
    
    public void testClone(){
        file2 = (File)file1.clone();
        assertEquals(file1.name,file2.name);
        assertEquals(file1.type,file2.type);
        assertEquals(file1.attributes,file2.attributes);
        assertEquals(file1.data,file2.data);
    }
    
}