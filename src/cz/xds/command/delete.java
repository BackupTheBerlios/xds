package cz.xds.command;

import cz.xds.Command;
import cz.xds.FileSystem;
import cz.xds.FileSystemException;
import cz.xds.FileSystemItem;

import java.io.PrintStream;
import java.util.Iterator;

/**
 * Vymaze polozku
 */
public class delete implements Command {
    public void execute(FileSystem fs, PrintStream outStream, Object[] param) throws FileSystemException {
        if (param.length < 2) {
            throw new FileSystemException(new String("Invalid usage. Use : " + help(true)));
        }

        String name = (String) param[1];
        Iterator it = fs.getCurrentDirectory().getIterator();

        while (it.hasNext()) {
            FileSystemItem fsi = (FileSystemItem) it.next();
            if (fsi.getName().equals(name)) {
                fsi.delete();
                return;
            }
        }
        throw new FileSystemException("Item not found");
    }

    public String help(boolean briefOnly) {
        return new String(briefOnly ? "delete itemname" : "delete - remove item from xds\n" +
                "\titemname - item name. May be link, file or directory (which is deleted recursively)");
    }
}
