package cz.xds.command;

import cz.xds.Command;
import cz.xds.FileSystem;
import cz.xds.FileSystemItem;

import java.io.PrintStream;
import java.util.Iterator;

/**
 * Vypisuje obsah aktualniho adresare
 */
public class dir implements Command {
    public void execute(FileSystem fs, PrintStream outStream, Object[] param) {
        Iterator it = fs.getCurrentDirectory().getIterator();

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
