package cz.xds;

/**
 * Date: 12.12.2004
 * Time: 23:57:17
 */
public class Link extends FileSystemItem {
    private FileSystemItem target;

    public Link(FileSystemItem target, String name) {
        this.target = target;
        this.name = name;
        this.parent = target.parent;
    }

    public FileSystemItem getTarget() { return target; }

    public Link createLink(String linkName) throws FileSystemException {
        throw new FileSystemException("cant' link to link");
    }

    public void delete() throws FileSystemException {
        parent.delete(this);
        target.delete();
    }

    public void copy(Directory d) throws FileSystemException {

    }

    public void move(Directory d) throws FileSystemException {

    }

}
