package cz.xds;

import java.util.Iterator;
import java.util.Vector;

/**
    Trida reprezentuje adresar souboroveho systemu
 */
public class Directory extends FileSystemItem implements Browseable {
    private Vector references;

    protected Directory(String name, Directory parent, Attributes attributes) {
        this.name = name;
        this.id = 100;
        this.parent = parent;
        this.attributes = attributes;

        references = new Vector();
    }

    public Directory createSubDir(String name) throws FileSystemException {
        Attributes at = new Attributes(false, false);
        return createSubDir(name, at);
    }

    public Directory createSubDir(String name, Attributes attributes)  throws FileSystemException {
        Directory newDir = new Directory(name, this, attributes);
        if (!references.contains(newDir)) {
            references.add(newDir);
        } else throw new ItemCreationException("Item with same name already exists");
        return newDir;
    }

    public Iterator getIterator() {
        return references.iterator();
    }

    public File createNewFile(String name, String type) throws FileSystemException {
        Attributes at = new Attributes(false, false);
        File newFile = createNewFile(name, type, at, null);
        return newFile;
    }

    public File createNewFile(String name, String type, byte[] data) throws FileSystemException {
        Attributes at = new Attributes(false, false);
        File newFile = createNewFile(name, type, at, data);
        return newFile;
    }

    public File createNewFile(String name, String type, Attributes attributes, byte[] data) throws FileSystemException {
        File newFile = new File(name, type, attributes, this, data);
        if (!references.contains(newFile)) {
            references.add(newFile);
        } else throw new ItemCreationException("Item with same name already exists");
        return newFile;
    }

    public FileSystemItem createLink(Path path) throws FileSystemException {
        return null;
    }

    public void delete() throws FileSystemException {
        Iterator i = getIterator();
        while (i.hasNext()) {
            FileSystemItem fsi = (FileSystemItem)i.next();
            fsi.delete();
        }
        parent.delete(this);
        references = null;
    }

    public void delete(FileSystemItem fsi) throws FileSystemException {
        references.remove(fsi);
    }

    public void copy(Directory d) throws FileSystemException {

    }

    public void move(Directory d) throws FileSystemException {

    }
}
