package cz.xds;

/**
 * Soubor souboroveho systemu
 */
public class File extends FileSystemItem {
    protected String type;
    protected byte[] data;

    protected File(String name, String type, Attributes attributes, Directory parent, byte[] data) {
        super(parent.idFactory);
        
        this.name = name;
        this.type = type;
        this.attributes = attributes;
        this.parent = parent;
        this.data = data;  // deep!
    }

    public Link createLink(Directory linkDir, String name) throws FileSystemException {
        return addLink((Link) linkDir.addChild(new Link(this, linkDir, name)));
    }

    public void delete() throws FileSystemException {
        super.delete();
        data = null;
    }

    public String toString() {
        return super.toString();
    }

    protected Object clone() {
        // data se musi zkopirovat v konstruktoru!
        return new File(name, type, attributes, null, data);
    }
}