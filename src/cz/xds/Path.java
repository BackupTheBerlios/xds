package cz.xds;

import java.util.Vector;
import java.util.Iterator;
import java.util.StringTokenizer;

/**
    Cesta v souborovem systemu
 */
public class Path {
    Vector path;
    public static final String PATH_SEPARATOR = "/";

    //TODO: make a nice parser, wildcards/regexp checker etc.

    public Path(FileSystemItem fsi) {
        path = new Vector();

        FileSystemItem akt = fsi;

        if (fsi instanceof Directory)
            path.add(fsi);

        while ((akt = akt.getParent()) != null) {
            path.add(akt);
        }
    }

    public Vector getPath() {
        return path;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        int size = path.size();
        for (int i = (size-1); i >= 0; i--) {
            Directory d = (Directory)path.elementAt(i);

            sb.append(d.getName());

            if (i!= size - 1)
                sb.append(PATH_SEPARATOR);
        }
        return sb.toString();
    }
}
