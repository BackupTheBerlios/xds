package cz.xds;

import java.util.Vector;

/**
 * Zakladni trida reprezentujici obecnou polozku souboroveho systemu.
 */
public abstract class FileSystemItem {
    protected String name;
    protected Attributes attributes = new Attributes();
    protected long id;
    protected Directory parent;
    protected Vector links = new Vector();

    public String getName() {
        return name;
    }

    public Attributes getAttributes() {
        return attributes;
    }

    public void setAttributes(Attributes newAttr) {
        attributes = newAttr;
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

    public Link addLink(Link newLink) {
        links.add(newLink);

        return newLink;
    }

    protected void removeLinks() throws FileSystemException {
        // musi byt links.size() - pocet se zjistuje v kazdem loopu!
        for (int x = 0; x < links.size(); x++) {
            Link i = (Link) links.get(x);

            links.remove(x);

            i.getParent().delete(i);
            System.out.println("Link " + i.getName() + " deleted!");
            x--;
        }
    }

    public abstract Link createLink(String name) throws FileSystemException;

    public abstract void copy(Directory d) throws FileSystemException;

    public void move(Directory d) throws FileSystemException {
        if (this == d) throw new FileSystemException("Cant't move to itself");
        d.findItem(name);
        parent.delete(this);
        this.parent = d;
        d.addChild(this);
    }

    public void delete() throws FileSystemException {
        if (attributes.isReadOnly())
            throw new AccessException(name);

        parent.delete(this);

        if (!(this instanceof Link))
            removeLinks();

        links = null;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();

        sb.append((this instanceof Link) ? "*" : ((this instanceof Directory) ? "D" : " "));
        sb.append(" ").append(attributes).append(" ").append(name).append(" (").append(id).append(")");

        if (this instanceof Link) {
            FileSystemItem target = ((Link) this).getTarget();
            sb.append("  -> ").append(target.getFullPath()).append(!(target instanceof Directory) ? target.getName() : "");
        }

        return sb.toString();
    }

    public boolean equals(Object obj) {
        if (obj instanceof FileSystemItem) {
            FileSystemItem d = (FileSystemItem) obj;
            if ((d.parent == parent) && (d.getName().equals(name)))
                return true;
            else
                return false;
        } else
            return false;
    }
}
