package cz.xds.command;

import cz.xds.*;

import java.util.StringTokenizer;
import java.util.Iterator;

/**
 * Vypisuje obsah aktualniho adresare
 */
public class cd implements Command {
    public void execute(Object[] param) throws FileSystemException {
        FileSystem fs = (FileSystem)param[0];
        Directory workDir = fs.getCurrentDirectory();
        String newDir = (String)param[2];

        StringTokenizer st = new StringTokenizer(newDir, "/\\");

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
