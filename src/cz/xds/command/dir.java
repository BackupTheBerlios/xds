package cz.xds.command;

import cz.xds.*;

import java.io.PrintStream;
import java.util.Iterator;

/**
 * Vypisuje obsah aktualniho adresare
 */
public class dir implements Command {
    public void execute(FileSystem fs, PrintStream outStream, Object[] param) throws FileSystemException {
        Directory d;

        if (param.length == 1) {
            d = fs.getCurrentDirectory();
        } else {
            d = Path.parseDirectory(fs, (String) param[1]);
        }
        Iterator it = d.getIterator();

        while (it.hasNext()) {
            FileSystemItem i = (FileSystemItem) it.next();

            if (!i.getAttributes().isHidden())
                outStream.println(i);
        }
    }

    public String help(boolean briefOnly) {
        return new String(briefOnly ? "dir [direcory]" : "dir - list contents of the given directory.\n" +
                "\tdirectory - directory to list. If not given, current directory is used.");
    }
}
