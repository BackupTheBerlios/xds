package cz.xds;

import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.Vector;

/**
 * Cesta v souborovem systemu
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
                if ((fsi.getName().equals(nextDir)) && (fsi instanceof Directory)) {
                    workDir = (Directory) fsi;
                    continue start;
                }
            }

            throw new FileSystemException("Directory not found");
        }
        return workDir;
    }

    public String toString() {
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
}
