package cz.xds.command;

import cz.xds.Command;
import cz.xds.FileSystem;

import java.util.Iterator;
import java.io.PrintStream;

/**
 * Vypisuje obsah aktualniho adresare
 */
public class dir implements Command {
    public void execute(FileSystem fs, PrintStream outStream, Object[] param) {
        Iterator it = fs.getCurrentDirectory().getIterator();

        while (it.hasNext())
            outStream.println(it.next());
    }
}
