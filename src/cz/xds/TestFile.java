package cz.xds;

/**
 * Test souboru
 */
import junit.framework.TestCase;



public class TestFile extends TestCase {
    
    private File file1 = null;
    private File file2 = null;
    private Directory root = null;
    private IDFactory fact = null;
    private Attributes attrib = null;
    private byte[] data = null;
    
    protected void setUp() throws Exception {
        super.setUp();
        fact = new IDFactory();
        attrib = new Attributes();
        root = new Directory(fact);
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
    }
    
    public void testDelete() throws Exception{
        file1.delete();
        assertEquals(file1.data, null);
    }
    
    public void testClone(){
        file2 = (File)file1.clone();
        assertEquals(file1.name,file2.name);
        assertEquals(file1.type,file2.type);
        assertEquals(file1.attributes,file2.attributes);
        assertEquals(file1.data,file2.data);
    }
    
    public void testToString(){
        String expectedText = new String("  -- file1 (1)");
        assertEquals(file1.toString(), expectedText);
    }
    
}