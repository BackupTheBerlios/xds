package cz.xds;

/**
 * Testy
 */

import junit.framework.TestCase;


public class TestFile extends TestCase {

    private File file1 = null;
    private File file2 = null;
    private File file3 = null;
    private File file4 = null;
    private File file5 = null;
    private Link link1 = null;
    private Link link2 = null;
    private Directory root = null;
    private Directory level1 = null;
    private Directory level1_1 = null;
    private Directory level2 = null;
    private Directory level3 = null;
    private Directory tmpDir = null;
    private IDFactory fact = null;
    private Attributes attrib = null;
    private Attributes attribhr = null;
    private byte[] data = null;

    protected void setUp() throws Exception {
        super.setUp();
        fact = new IDFactory();
        attrib = new Attributes();
        attribhr = new Attributes(true, true);
        root = new Directory(fact);
        level1 = root.createSubDir("level1");
        data = new byte[1];
        data[0] = 1;

    }

    protected void tearDown() throws FileSystemException {
        file1 = null;
        file2 = null;
        file3 = null;
        file4 = null;
        level1 = null;
        level1_1 = null;
        level2 = null;
        level3 = null;
        tmpDir = null;
        root = null;
        fact = null;
        attrib = null;
        attribhr = null;
        data = null;
        link1 = null;
        link2 = null;
    }

    public void testConstructionFile() throws FileSystemException {
        file1 = root.createNewFile("file1", "txt", data);
        Attributes expectedAttrib = new Attributes(false, false);
        assertEquals(file1.getName(), "file1");
        assertEquals(file1.type, "txt");
        assertTrue(file1.getAttributes().equals(expectedAttrib));
        assertFalse(file1.data == null);
        assertTrue(file1.isDeletable());
        assertEquals(file1.getParent(), root);
    }

    public void testConstructionDir() {
        Attributes expectedAttrib = new Attributes(false, false);
        assertEquals(level1.getName(), "level1");
        assertTrue(level1.getAttributes().equals(expectedAttrib));
        assertTrue(level1.isDeletable());
        assertEquals(level1.getParent(), root);
        assertNotNull(level1.get_children());
    }

    public void testCopyFile() throws FileSystemException {
        file1 = root.createNewFile("file1", "txt", data);
        file3 = (File) file1.copy(level1);
        assertEquals(file3.name, file1.name);
        assertEquals(file3.type, file1.type);
        assertEquals(file3.attributes, file1.attributes);
        assertEquals(file3.data, file1.data);
        assertEquals(file3.getParent(), level1);
        assertEquals(level1.findItem(file3.getName()), file3);
        try {
            file4 = (File) file1.copy(level1);
            file5 = (File) file1.copy(root);
        } catch (FileSystemException e) {
            return;
        }
        fail("Raise FileSystemException: Target file already exists");
    }

    public void testCopyDir() throws FileSystemException {
        level2 = level1.createSubDir("level2");
        file1 = level2.createNewFile("file1", "txt");
        assertEquals(level2.findItem(file1.getName()), file1);
        tmpDir = (Directory) level2.copy(root);
        assertEquals(tmpDir.name, level2.name);
        assertEquals(tmpDir.attributes, level2.attributes);
        assertEquals(tmpDir.getParent(), root);
        assertTrue(tmpDir.hasSameChildren(level2));
        try {
            tmpDir = (Directory) level1.copy(level1);
        } catch (FileSystemException e) {
            return;
        }
        fail("Raise FileSystemException: Can't move to itself");
    }

    public void testMoveFile() throws FileSystemException {
        file1 = root.createNewFile("file1", "txt", data);
        assertEquals(file1.parent, root);
        assertEquals(root.findItem(file1.getName()), file1);
        file1.move(level1);
        assertEquals(file1.parent, level1);
        assertEquals(level1.findItem(file1.getName()), file1);
        assertEquals(root.findItem(file1.getName()), null);
        file2 = root.createNewFile("file2", "txt", data);
        try {
            file2.move(root);
        } catch (FileSystemException e) {
            return;
        }
        fail("Raise FileSystemException: Target file already exists");
    }

    public void testMoveDir() throws FileSystemException {
        level2 = level1.createSubDir("level2");
        file2 = level2.createNewFile("file2", "txt");
        assertTrue(((Directory) level2.move(root)).hasSameChildren(level2));
        assertEquals(level2.parent, root);
    }


    public void testDeleteFile() throws FileSystemException {
        file1 = root.createNewFile("file1", "txt", data);
        file1.delete();
        assertEquals(file1.data, null);
        assertEquals(file1.links.size(), 0);
    }

    public void testDeleteDir() throws FileSystemException {
        level1.delete();
        assertNull(level1.get_children());
    }

    public void testClone() throws FileSystemException {
        file1 = root.createNewFile("file1", "txt", data);
        file2 = (File) file1.clone();
        assertEquals(file1.name, file2.name);
        assertEquals(file1.type, file2.type);
        assertEquals(file1.attributes, file2.attributes);
        assertEquals(file1.data, file2.data);
    }

    public void testLinkToFile() throws FileSystemException {
        file1 = root.createNewFile("file1", "txt", data);
        link1 = file1.createLink(level1, "linkToFile1");
        assertEquals(link1.getTarget(), file1);
        assertEquals(link1.getParent(), level1);
        assertNotNull(link1.getParent().findItem(link1.getName()));
        file1.delete();
        assertNull(link1.getParent().findItem(link1.getName()));
    }

    public void testLinkToDir() throws FileSystemException {
        file1 = level1.createNewFile("file1", "txt", data);
        link1 = level1.createLink(root, "linkToLevel1");
        level2 = root.createSubDir("level2");
        link2 = level2.createLink(root, "linkToLevel2");
        assertEquals(link1.getTarget(), level1);
        assertEquals(link1.getParent(), root);
        assertEquals(link2.getTarget(), level2);
        assertEquals(link2.getParent(), root);
        assertNotNull(link1.getParent().findItem(link1.getName()));
        assertNotNull(link1.getParent().findItem(link2.getName()));
        level1.delete();
        link2.delete();
        assertNull(link1.getParent().findItem(link1.getName()));
        assertEquals(root.findItem(level2.getName()), level2);
    }
}