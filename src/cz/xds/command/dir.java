package cz.xds.command;

import cz.xds.Command;
import cz.xds.FileSystem;
import cz.xds.FileSystemItem;

import java.util.Iterator;
import java.io.PrintStream;

/**
 * Vypisuje obsah aktualniho adresare
 */
public class dir implements Command {
    public void execute(FileSystem fs, PrintStream outStream, Object[] param) {
        Iterator it = fs.getCurrentDirectory().getIterator();

        while (it.hasNext()) {
            FileSystemItem i = (FileSystemItem)it.next();

            if (!i.getAttributes().isHidden())
                outStream.println(i);
        }
    }
}
