package cz.xds;

/**
 * Zakladni trida reprezentujici obecnou polozku souboroveho systemu.
 */
public abstract class FileSystemItem {
    protected String name;
    protected Attributes attributes;
    protected long id;
    protected Directory parent;

    public String getName() {
        return name;
    }

    public Attributes getAttributes() {
        return attributes;
    }

    public long getId() {
        return id;
    }

    public Directory getParent() {
        return parent;
    }

    public Path getFullPath() {
        return new Path(this);
    }

    public abstract FileSystemItem createLink(Path path) throws FileSystemException;

    public abstract void delete() throws FileSystemException;

    public abstract void copy(Directory d) throws FileSystemException;

    public abstract void move(Directory d) throws FileSystemException;

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(id).append(" - ").append(name);
        return sb.toString();
    }

    public boolean equals(Object obj) {
        if (obj instanceof FileSystemItem) {
            FileSystemItem d = (FileSystemItem)obj;
            if ((d.parent == parent) && (d.getName().equals(name))) return true;
            else return false;
        } else return false;
    }
}
