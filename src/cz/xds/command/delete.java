package cz.xds.command;

import cz.xds.Command;
import cz.xds.FileSystem;
import cz.xds.FileSystemItem;
import cz.xds.FileSystemException;

import java.util.Iterator;
import java.io.PrintStream;

/**
 * Vymaze polozku
 */
public class delete implements Command {
    public void execute(FileSystem fs, PrintStream outStream, Object[] param) throws FileSystemException {
        String name = (String)param[1];
        Iterator it = fs.getCurrentDirectory().getIterator();

        while (it.hasNext()) {
            FileSystemItem fsi = (FileSystemItem)it.next();
            if (fsi.getName().equals(name)) {
                fsi.delete();
                return;
            }
        }
        throw new FileSystemException("Item not found");
    }
}
