package cz.xds.command;

import cz.xds.*;

import java.io.PrintStream;
import java.util.Iterator;
import java.util.StringTokenizer;

/**
 * Vypisuje obsah aktualniho adresare
 */
public class cd implements Command {
    public void execute(FileSystem fs, PrintStream outStream, Object[] param) throws FileSystemException {
        if (param.length == 1) {
            outStream.println(fs.getCurrentDirectory().getFullPath());
            return;
        }

        Directory workDir;
        String newDir = (String) param[1];

        if (newDir.charAt(0) == Path.PATH_SEPARATOR.charAt(0)) {
            workDir = fs.getRootDirectory();
        } else
            workDir = fs.getCurrentDirectory();

        StringTokenizer st = new StringTokenizer(newDir, Path.PATH_SEPARATOR);

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

        fs.changeDirectory(workDir);
    }

    public String help(boolean briefOnly) {
        if (briefOnly)
            return new String("cd [dir]");

        return new String("cd - change current directory\n\tdir - path to set as current. If not given, the current " +
                "directory is printed out");
    }
}
