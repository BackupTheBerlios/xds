package cz.xds.command;

import cz.xds.*;

import java.util.StringTokenizer;
import java.util.Iterator;
import java.io.*;

/**
 * Vypisuje obsah aktualniho adresare
 */
public class cd implements Command {
    public void execute(FileSystem fs, PrintStream outStream, Object[] param) throws FileSystemException {
        if (param.length == 1) {
            outStream.println(fs.getCurrentDirectory().getFullPath());
            return;
        }

        Directory workDir = fs.getCurrentDirectory();
        String newDir = (String)param[1];

        StringTokenizer st = new StringTokenizer(newDir, Path.PATH_SEPARATOR);

        start: while (st.hasMoreTokens()) {
            String nextDir = st.nextToken();

            if (nextDir.equals("..") && (workDir.getParent() != null)) {
                workDir = workDir.getParent();
                continue;
            }

            Iterator i = workDir.getIterator();
            while (i.hasNext()) {
                FileSystemItem fsi =(FileSystemItem)i.next();
                if ((fsi.getName().equals(nextDir)) && (fsi instanceof Directory)) {
                    workDir = (Directory)fsi;
                    break start;
                }
            }

            throw new FileSystemException("Directory not found");
        }

        fs.changeDirectory(workDir);
    }
}
