package cz.xds.command;

import cz.xds.Command;
import cz.xds.FileSystem;
import cz.xds.FileSystemItem;
import cz.xds.FileSystemException;

import java.util.Iterator;

/**
 * Vymaze polozku
 */
public class delete implements Command {
    public void execute(Object[] param) throws FileSystemException {
        String name = (String)param[2];
        Iterator it = ((FileSystem) param[0]).getCurrentDirectory().getIterator();
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
