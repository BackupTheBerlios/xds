package cz.xds;

import java.util.Iterator;
import java.util.Vector;

/**
 * Trida reprezentuje adresar souboroveho systemu
 */
public class Directory extends FileSystemItem implements Browseable {
    private Vector children = new Vector();

    /**
     * Specialni konstruktor pro root adresar
     *
     * @param idFactory Generator identifikacnich cisel
     */
    protected Directory(IDFactory idFactory) {
        super(idFactory);

        this.name = Path.PATH_SEPARATOR;
        this.parent = null;
        this.attributes = new Attributes(false, false);
    }

    /**
     * Standardni chraneny konstruktor
     *
     * @param name       Jmeno adresare
     * @param parent     Rodic adresare
     * @param attributes Atributy
     */
    protected Directory(String name, Directory parent, Attributes attributes) {
        super(parent.idFactory);

        this.name = name;
        this.parent = parent;
        this.attributes = attributes;
    }

    /**
     * Pridava podpolozku (soubor, adresar, ...) do tohoto adresare
     *
     * @param child Vkladana polozka
     * @return Vraci vlozenou polozku
     * @throws FileSystemException Vola se v pripade, ze polozka jiz existuje
     */
    protected FileSystemItem addChild(FileSystemItem child) throws FileSystemException {
        if (!children.contains(child)) {
            children.add(child);
        } else
            throw new ItemCreationException("Item with same name already exists");

        return child;
    }

    /**
     * Vytvari podadresar
     *
     * @param name Nazev
     * @return Vraci referenci na vytvoreny adresar
     * @throws FileSystemException
     */
    public Directory createSubDir(String name) throws FileSystemException {
        Attributes at = new Attributes(false, false);
        return createSubDir(name, at);
    }

    /**
     * Vytvari podadresar
     *
     * @param name       Nazev
     * @param attributes Atributy
     * @return Reference na novy adresar
     * @throws FileSystemException
     */
    public Directory createSubDir(String name, Attributes attributes) throws FileSystemException {
        return (Directory) addChild(new Directory(name, this, attributes));
    }

    /**
     * Vraci iterator vsemi polozkami tohoto adresare
     *
     * @return
     */
    public Iterator getIterator() {
        return children.iterator();
    }

    /**
     * Vytvari novy soubor
     *
     * @param name Jmeno souboru
     * @param type Typ souboru
     * @return Reference na vytvoreny soubor
     * @throws FileSystemException
     */
    public File createNewFile(String name, String type) throws FileSystemException {
        Attributes at = new Attributes(false, false);
        File newFile = createNewFile(name, type, at, null);
        return newFile;
    }

    /**
     * Vytvari novy soubor
     *
     * @param name Jmeno souboru
     * @param type Typ souboru
     * @param data Obsah souboru
     * @return Reference na vytvoreny soubor
     * @throws FileSystemException
     */
    public File createNewFile(String name, String type, byte[] data) throws FileSystemException {
        Attributes at = new Attributes(false, false);
        File newFile = createNewFile(name, type, at, data);
        return newFile;
    }

    /**
     * Vytvari novy soubor
     *
     * @param name       Jmeno souboru
     * @param type       Typ souboru
     * @param data       Obsah souboru
     * @param attributes Atributy
     * @return Reference na vytvoreny soubor
     * @throws FileSystemException
     */
    public File createNewFile(String name, String type, Attributes attributes, byte[] data) throws FileSystemException {
        return (File) addChild(new File(name, type, attributes, this, data));
    }

    /**
     * Vytvari odkaz na tento adresar
     *
     * @param linkDir Cilovy adresar pro nove vytvateny odkaz
     * @param name    Jmeno noveho odkazu
     * @return Vraci referenci na novy odkaz
     * @throws FileSystemException
     */
    public Link createLink(Directory linkDir, String name) throws FileSystemException {
        Link newLink = (Link) linkDir.addChild(new Link(this, linkDir, name));
        addLink(newLink);

        return newLink;
    }

    /**
     * Smaze tento adresar (vcetne vsech polozek)
     *
     * @throws FileSystemException
     */
    public void delete() throws FileSystemException {
        if (!isDeletable()) throw new AccessException(name);

        if (links.size() == 0) {
            while (children.size() > 0) {
                FileSystemItem fsi = (FileSystemItem) children.firstElement();
                fsi.delete();
            }
        }

        children = null;

        super.delete();
    }

    /**
     * Vraci polozku s danym nazvem, null pokud neexistuje
     *
     * @param name Hledana polozka
     * @return Nalezena polozka nebo null
     * @throws FileSystemException
     */
    public FileSystemItem findItem(String name) throws FileSystemException {
        Iterator i = getIterator();
        while (i.hasNext()) {
            FileSystemItem fsi = (FileSystemItem) i.next();
            if (fsi.getName().equals(name)) {
                return fsi;
            }
        }

        return null;
    }

    /**
     * Smaze konkretni polozku v tomto adresari
     *
     * @param fsi
     * @throws FileSystemException
     */
    public void delete(FileSystemItem fsi) throws FileSystemException {
        if (attributes.isReadOnly())
            throw new AccessException(name);

        children.remove(fsi);
    }

    /**
     * Vraci true, pokud aktualni adresar je podpolozkou adresare testDir. Jinak vraci false.
     *
     * @param testDir Testovany adresar
     * @return
     */
    public boolean isNondirectParentOf(Directory testDir) {
        while ((testDir = testDir.getParent()) != null)
            if (testDir == this)
                return true;

        return false;
    }

    /**
     * Vytvari deep-copy tohoto adresare
     *
     * @return
     */
    protected Object clone() {
        Directory newDir = new Directory(name, parent, attributes);

        // rekurzivne zkopirovat vsechny podslozky a soubory

        Iterator it = children.iterator();

        try {
            while (it.hasNext()) {
                // TODO: trosku divne, nicmene spravne. Mozna by bylo lepci se vysrat na
                // konvence a udelat neco vlastniho (clone() s parametrem parent a
                // vracejici FileSystemItem.. ?

                FileSystemItem newItem = (FileSystemItem) ((FileSystemItem) it.next()).clone();
                newItem.parent = newDir;
                newDir.addChild((FileSystemItem) newItem);
            }
        } catch (FileSystemException e) {
            return null;
        }

        return newDir;
    }
}
