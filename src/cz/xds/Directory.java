package cz.xds;

import java.util.Iterator;
import java.util.Vector;

/**
    Trida reprezentuje adresar souboroveho systemu
 */
public class Directory extends FileSystemItem implements Browseable {
    private Vector children;
    private boolean deleting;

    protected Directory(String name, Directory parent, Attributes attributes) {
        this.name = name;
        this.id = 100;
        this.parent = parent;
        this.attributes = attributes;

        children = new Vector();
    }

    public FileSystemItem addChild(FileSystemItem child) throws FileSystemException {
        if (!children.contains(child)) {
            children.add(child);
        }
        else
            throw new ItemCreationException("Item with same name already exists");

        return child;
    }

    public Directory createSubDir(String name) throws FileSystemException {
        Attributes at = new Attributes(false, false);
        return createSubDir(name, at);
    }

    public Directory createSubDir(String name, Attributes attributes) throws FileSystemException {
        return (Directory)addChild(new Directory(name, this, attributes));
    }

    public Iterator getIterator() {
        return children.iterator();
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
        return (File)addChild(new File(name, type, attributes, this, data));
    }

    public Link createLink(String name) throws FileSystemException {
        Link newLink = (Link)getParent().addChild(new Link(this, name));
        addLink(newLink);

        return newLink;
    }

    public void delete() throws FileSystemException {
        if (links.size() == 0) {
            Iterator i = getIterator();
            while (i.hasNext()) {
                FileSystemItem fsi = (FileSystemItem)i.next();
                fsi.delete();
            }
        }
        else
            removeLinks();

        parent.delete(this);

        children = null;
        links = null;
    }

    public void delete(FileSystemItem fsi) throws FileSystemException {
        //if (attributes.isReadOnly())
        //    throw new FileSystemException("Protected");

        children.remove(fsi);
    }

    public void copy(Directory d) throws FileSystemException {

    }

    public void move(Directory d) throws FileSystemException {

    }
}
