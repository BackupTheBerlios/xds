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

    public FileSystemItem getTarget() {
        return target;
    }

    public Link createLink(String linkName) throws FileSystemException {
        throw new FileSystemException("Can't link to link");
    }

    public void delete() throws FileSystemException {
        target.delete();

        super.delete();
    }

    public void copy(Directory d) throws FileSystemException {

    }
}
