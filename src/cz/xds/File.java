package cz.xds;

/**
 * Soubor souboroveho systemu
 */
public class File extends FileSystemItem {
    protected String type;
    protected byte[] data;

    /**
     * Implicitni chraneny konstruktor, soubory jsou bezne vytvareny metodami tridy Directory
     *
     * @param name       Jmeno souboru
     * @param type       Typ souboru
     * @param attributes Atributy
     * @param parent     Rodic souboru
     * @param data       Obsah
     */
    protected File(String name, String type, Attributes attributes, Directory parent, byte[] data) {
        super(parent.idFactory);

        this.name = name;
        this.type = type;
        this.attributes = attributes;
        this.parent = parent;
        this.data = data;  // deep!
    }

    /**
     * Vytvari novy odkaz na tento soubor v cilovem adresari linkDir
     *
     * @param linkDir Cilovy adresar
     * @param name    Jmeno odkazu
     * @return Reference na novy odkay
     * @throws FileSystemException
     */
    public Link createLink(Directory linkDir, String name) throws FileSystemException {
        return addLink((Link) linkDir.addChild(new Link(this, linkDir, name)));
    }

    /**
     * Smaze soubor
     *
     * @throws FileSystemException
     */
    public void delete() throws FileSystemException {
        super.delete();
        data = null;
    }

    public String toString() {
        return super.toString();
    }

    protected Object clone() {
        // data se musi zkopirovat v konstruktoru!
        return new File(name, type, attributes, parent, data);
    }
}