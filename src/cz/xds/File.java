package cz.xds;

/**
    Soubor souboroveho systemu
 */
public class File extends FileSystemItem {
    protected String type;
    protected byte[] data;

    protected File(String name, String type, Attributes attributes, Directory parent, byte[] data) {
        this.name = name;
        this.type = type;
        this.id = 200;
        this.attributes = attributes;
        this.parent = parent;
        this.data = data;
    }

    public Link createLink(String name) throws FileSystemException {
        return addLink((Link)getParent().addChild(new Link(this, name)));
    }

    public void delete() throws FileSystemException {
        data = null;

        super.delete();
    }

    public void copy(Directory d) throws FileSystemException {

    }

    public void move(Directory d) throws FileSystemException {

    }

    public String toString() {
        return super.toString();
    }
}