package cz.xds;

/**
 * Date: 12.12.2004
 * Time: 23:57:17
 */
public class Link extends FileSystemItem {
    private FileSystemItem target;

    public Link(FileSystemItem target, Directory parent, String name) {
        this.target = target;
        this.name = name;
        this.parent = parent;
    }

    public FileSystemItem getTarget() {
        return target;
    }

    public Link createLink(Directory linkDir, String linkName) throws FileSystemException {
        throw new FileSystemException("Can't link to link");
    }

    public void delete() throws FileSystemException {
        super.delete();

        target.removeLink(this);
    }

    protected Object clone() {
        Link newLink = new Link(target, null, name);

        target.addLink(newLink);

        return newLink;
    }
}
