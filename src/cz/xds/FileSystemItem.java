package cz.xds;

import java.util.Vector;

/**
 * Zakladni trida reprezentujici obecnou polozku souboroveho systemu.
 */
public abstract class FileSystemItem implements Cloneable {
    protected String name;
    protected Attributes attributes = new Attributes();
    protected long id;
    protected Directory parent;
    protected Vector links = new Vector();
    protected IDFactory idFactory;

    protected FileSystemItem(IDFactory idFactory) {
        this.idFactory = idFactory;
        id = idFactory.createNewID(this);
    }

    /**
     * Vraci jmeno polozky
     *
     * @return Jmeno polozky
     */
    public String getName() {
        return name;
    }

    /**
     * Vraci atributy polozky
     *
     * @return Atributy polozky
     */
    public Attributes getAttributes() {
        return attributes;
    }

    /**
     * Nastavuje atributy
     *
     * @param newAttr Nove atributy
     */
    public void setAttributes(Attributes newAttr) {
        attributes = newAttr;
    }

    /**
     * Vraci ID polozky
     *
     * @return Id
     */
    public long getId() {
        return id;
    }

    /**
     * Vraci rodice polozky
     *
     * @return Rodic
     */
    public Directory getParent() {
        return parent;
    }

    /**
     * Vraci kompletni cestu k teto polozce (sled adresaru)
     *
     * @return Cesta k polozce
     */
    public Path getFullPath() {
        return new Path(this);
    }

    /**
     * Pridava zaznam o novem odkazu na tuto polozku
     *
     * @param newLink Novy link
     * @return
     */
    public Link addLink(Link newLink) {
        links.add(newLink);

        return newLink;
    }

    /**
     * Odstrani vsechny odkazy na tuto plozku (v pripade jejiho smazani)
     *
     * @throws FileSystemException
     */
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

    /**
     * Odstrani konkretni odkaz
     *
     * @param l
     * @throws FileSystemException
     */
    protected void removeLink(Link l) throws FileSystemException {
        links.remove(l);
    }

    protected abstract Object clone();

    public abstract Link createLink(Directory linkDir, String name) throws FileSystemException;

    /**
     * Kopiruje tuto polozku
     *
     * @param d Cilovy adresar
     * @throws FileSystemException
     */
    public FileSystemItem copy(Directory d) throws FileSystemException {
        if (this == d) throw new FileSystemException("Cant't move to itself");
        if (d.findItem(name) != null) throw new FileSystemException("Target file already exists");

        FileSystemItem newItem = (FileSystemItem) clone();
        newItem.parent = d;
        d.addChild(newItem);
        return newItem;
    }

    /**
     * Presouva polozku
     *
     * @param d Cilovy adresar
     * @throws FileSystemException
     */
    public FileSystemItem move(Directory d) throws FileSystemException {
        if (this == d) throw new FileSystemException("Cant't move to itself");
        if (d.findItem(name) != null) throw new FileSystemException("Target file already exists");
        parent.delete(this);
        this.parent = d;
        d.addChild(this);
        return this;
    }

    /**
     * Smaze tuto polozku
     *
     * @throws FileSystemException V pripade, ze je polozka jen pro cteni
     */
    public void delete() throws FileSystemException {
        if (!isDeletable()) throw new AccessException(name);

        parent.delete(this);

        if (!(this instanceof Link))
            removeLinks();

        idFactory.deleteID(id);
    }

    /**
     * Kontroluje, zda polozku lze smazat.
     */
    public boolean isDeletable() {
        if (attributes.isReadOnly()) return false;
        return true;
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
