package cz.xds;

import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.Vector;

/**
 * Cesta v souborovem systemu
 */
public class Path {
    Vector path = null;
    String directory, itemName;

    // Oddelovac adresaru v kompletni ceste
    public static final String PATH_SEPARATOR = "/";

    /**
     * Konstruktor pro sestaveni cesty od korenoveho adresare k jiz existujici polozce
     * @param fsi Polozka, jejiz cesta se ma sestavit
     */
    public Path(FileSystemItem fsi) {
        path = new Vector();

        FileSystemItem akt = fsi;

        if (fsi instanceof Directory)
            path.add(fsi);

        while ((akt = akt.getParent()) != null) {
            path.add(akt);
        }
    }

    /**
     * Konstruktor sestavujici cestu ze zadane cesty a parametru, zda posledni
     * polozka v ceste musi byt adresar, nebo na ni vubec nezalezi.
     * @param fullPath Retezec reprezentujici cestu k polozce
     * @param isOnlyDirectory Musi byt posledni polozka (tj. text za poslednim <i>Path.SEPARATOR</i>
     * ) cesty adresar?
     */
    public Path(String fullPath, boolean isOnlyDirectory) {
        if (isOnlyDirectory)
        {
            directory = fullPath;
            return;
        }

        int index = fullPath.lastIndexOf(PATH_SEPARATOR);

        if (index != -1) {
            directory = fullPath.substring(0, index + 1);
            itemName = fullPath.substring(index + 1);
        }
        else
            itemName = fullPath;
    }

    /**
     * Vraci adresar cesty
     * @return Retezec reprezentujici adresar polozky specifikovane v konstruktoru Path
     */
    public String getDirectory() {
        return directory;
    }

    /**
     * Vraci nazev polozky (jmena, adresare nebo odkazu)
     * @return Nazev polozky
     */
    public String getItemName() {
        return itemName;
    }

    /**
     * Vraci vektor jednotlivych adresaru smerem od cilove polozky do korenoveho adresare
     * @return Vektor objektu Directory s polozkami cesty
     */
    public Vector getPath() {
        return path;
    }

    /**
     * Prelozi textovou reprezentaci cesty na objekt Directory (pokud tento existuje)
     * @param fs Souborovy system, ktery se ma pro preklad pouzit
     * @return Vysledny adresar
     * @throws FileSystemException Pokud polozka neexistuje
     */
    public Directory getDirectory(FileSystem fs) throws FileSystemException {
        return directory == null ? fs.getCurrentDirectory() : parseDirectory(fs, directory);
    }

    /**
     * Prelozi adresar reprezentovany cestou na objekt typu Directory
     * @param fs Souborovy system, ktery se ma pro preklad pouzit
     * @param path Cesta pro preklad
     * @return Vysledny adresar
     * @throws FileSystemException Pokud neni adresar nalezen
     */
    public static Directory parseDirectory(FileSystem fs, String path) throws FileSystemException {
        Directory workDir;

        if (path.charAt(0) == Path.PATH_SEPARATOR.charAt(0)) {
            workDir = fs.getRootDirectory();
        } else
            workDir = fs.getCurrentDirectory();

        StringTokenizer st = new StringTokenizer(path, Path.PATH_SEPARATOR);

        start: while (st.hasMoreTokens()) {
            String nextDir = st.nextToken();

            if (nextDir.equals("..") && (workDir.getParent() != null)) {
                workDir = workDir.getParent();
                continue;
            }

            Iterator i = workDir.getIterator();
            while (i.hasNext()) {
                FileSystemItem fsi = (FileSystemItem) i.next();
                if ((fsi.getName().equals(nextDir))) {
                    if (fsi instanceof Directory) {
                        workDir = (Directory) fsi;
                        continue start;
                    } else if (fsi instanceof Link && (((Link)fsi).getTarget() instanceof Directory)) {
                        workDir = (Directory)((Link)fsi).getTarget();
                        continue start;
                    }

                }
            }

            throw new FileSystemException("Directory not found");
        }
        return workDir;
    }

    public String toString() {
        if (path != null) {
            StringBuffer sb = new StringBuffer();
            int size = path.size();
            for (int i = (size - 1); i >= 0; i--) {
                Directory d = (Directory) path.elementAt(i);

                sb.append(d.getName());

                if (i != size - 1)
                    sb.append(PATH_SEPARATOR);
            }
            return sb.toString();
        }
        else
            return directory + itemName;
    }
}
